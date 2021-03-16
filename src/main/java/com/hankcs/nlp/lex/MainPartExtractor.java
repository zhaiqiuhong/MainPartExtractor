package com.hankcs.nlp.lex;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;
import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.trees.international.pennchinese.ChineseTreebankLanguagePack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.applet.Main;

import java.util.*;

/**
 * 提取主谓宾
 *
 * @author hankcs
 */
public class MainPartExtractor {

    private static final Logger LOG = LoggerFactory.getLogger(MainPartExtractor.class);
    private static LexicalizedParser lp;
    private static GrammaticalStructureFactory gsf;

    static {
        //模型
        String models = "models/chineseFactored.ser";
        LOG.info("载入文法模型：" + models);
        lp = LexicalizedParser.loadModel(models);
        //汉语
        TreebankLanguagePack tlp = new ChineseTreebankLanguagePack();
        gsf = tlp.grammaticalStructureFactory();
    }

    public static String getNewSent(String sentence, MainPart mainPart) {
        // 去掉不可见字符
        sentence = sentence.replace("\\s+", "");
        // 分词，用空格隔开
        //从wordlist中删除原本的三个主谓宾单词并且重组句子

//        TreeGraphNode subject = mainPart.subject;  //主语
//        TreeGraphNode predicate = mainPart.predicate;   //谓语
        String subject = "";
        String predicate = "";
        String object = "";
        try {
            subject = mainPart.subject.toString().split("\\-")[0];    //主语
        } catch (NullPointerException e) {
            subject = "";
        }
        try {
            predicate = mainPart.predicate.toString().split("\\-")[0];    //谓语
        } catch (NullPointerException e) {
            predicate = "";
        }
        try {
            object = mainPart.object.toString().split("\\-")[0];    //宾语
        } catch (NullPointerException e) {
            object = "";
        }

        // 至少要有主谓宾中2项才有接下来的重组句子操作
        if ((subject.equals("") && predicate.equals("")) ||
                (subject.equals("") && object.equals("")) ||
                (object.equals("") && predicate.equals(""))) {
            return sentence;
        }

        // 删除wordList中的主谓宾三个单词
//        List<Word> removeWordList = new ArrayList<>();
//        for (Word word : wordList) {
//            if (word.toString().equals(subject) || word.toString().equals(predicate) || word.toString().equals(object)) {
//                removeWordList.add(word);
//            }
//        }
//        wordList.removeAll(removeWordList);

        //删除句子中的主谓宾
        String newSent = sentence.replace(subject, "");
        newSent = newSent.replace(predicate, "");
        newSent = newSent.replace(object, "");
        newSent = newSent + subject + predicate + object;
        return newSent;

//        String newSent = wordList.get(0).toString();
//        newSent += mainPart;
//        StringBuilder newSent = new StringBuilder();
//        for (Word word : wordList) {
//            if (newSent.length() == 0) {
//                newSent = new StringBuilder(word.toString());   //初始化
//                newSent.append(mainPart);    //句子的第234个位子放的是主谓宾
//                continue;
//            }
//            newSent.append(word.toString());
//        }
//        return newSent.toString();
//        return getMainPart(wordList);
    }

    /**
     * 获取句子的主谓宾
     *
     * @param sentence 问题
     * @return 问题结构
     */
    public static MainPart getMainPart(String sentence) {
        // 去掉不可见字符
        sentence = sentence.replace("\\s+", "");
        // 分词，用空格隔开
        List<Word> wordList = seg(sentence);
        return getMainPart(wordList);
    }

    /**
     * 获取句子的主谓宾
     *
     * @param words HashWord列表
     * @return 问题结构
     */
    public static MainPart getMainPart(List<Word> words) {
        MainPart mainPart = new MainPart();
        if (words == null || words.size() == 0) return mainPart;
        Tree tree = lp.apply(words);
        LOG.info("句法树:{}", tree.pennString());
        // 根据整个句子的语法类型来采用不同的策略提取主干
        switch (tree.firstChild().label().toString()) {
            case "NP":
                // 名词短语，认为只有主语，将所有短NP拼起来作为主语即可
                mainPart = getNPPhraseMainPart(tree);
                break;
            default:
                GrammaticalStructure gs = gsf.newGrammaticalStructure(tree);
                Collection<TypedDependency> tdls = gs.typedDependenciesCCprocessed(true);
                LOG.info("依存关系：{}", tdls);
                TreeGraphNode rootNode = getRootNode(tdls);
                if (rootNode == null) {
                    return getNPPhraseMainPart(tree);
                }
                LOG.info("中心词语：", rootNode);
                mainPart = new MainPart(rootNode);
                for (TypedDependency td : tdls) {
                    // 依存关系的出发节点，依存关系，以及结束节点
                    TreeGraphNode gov = td.gov();
                    GrammaticalRelation reln = td.reln();
                    String shortName = reln.getShortName();
                    TreeGraphNode dep = td.dep();
                    if (gov == rootNode) {
                        switch (shortName) {
                            case "nsubjpass":
                            case "dobj":
                            case "attr":
                                mainPart.object = dep;
                                break;
                            case "nsubj":
                            case "top":
                                mainPart.subject = dep;
                                break;
                        }
                    }
                    if (mainPart.object != null && mainPart.subject != null) {
                        break;
                    }
                }
                // 尝试合并主语和谓语中的名词性短语
                combineNN(tdls, mainPart.subject);
                combineNN(tdls, mainPart.object);
                if (!mainPart.isDone()) mainPart.done();
        }

        return mainPart;
    }

    public static MainPart getMainPart(List<Word> words, MainPart mainPart) {
        if (words == null || words.size() == 0) return mainPart;
        Tree tree = lp.apply(words);
        LOG.info("句法树:{}", tree.pennString());
        // 根据整个句子的语法类型来采用不同的策略提取主干
        switch (tree.firstChild().label().toString()) {
            case "NP":
                // 名词短语，认为只有主语，将所有短NP拼起来作为主语即可
                mainPart = getNPPhraseMainPart(tree);
                break;
            default:
                GrammaticalStructure gs = gsf.newGrammaticalStructure(tree);
                Collection<TypedDependency> tdls = gs.typedDependenciesCCprocessed(true);
                LOG.info("依存关系：{}", tdls);
                TreeGraphNode rootNode = getRootNode(tdls);
                if (rootNode == null) {
                    return getNPPhraseMainPart(tree);
                }
                LOG.info("中心词语：", rootNode);
                mainPart = new MainPart(rootNode);
                for (TypedDependency td : tdls) {
                    // 依存关系的出发节点，依存关系，以及结束节点
                    TreeGraphNode gov = td.gov();
                    GrammaticalRelation reln = td.reln();
                    String shortName = reln.getShortName();
                    TreeGraphNode dep = td.dep();
                    if (gov == rootNode) {
                        switch (shortName) {
                            case "nsubjpass":
                            case "dobj":
                            case "attr":
                                mainPart.object = dep;
                                break;
                            case "nsubj":
                            case "top":
                                mainPart.subject = dep;
                                break;
                        }
                    }
                    if (mainPart.object != null && mainPart.subject != null) {
                        break;
                    }
                }
                // 尝试合并主语和谓语中的名词性短语
                combineNN(tdls, mainPart.subject);
                combineNN(tdls, mainPart.object);
                if (!mainPart.isDone()) mainPart.done();
        }

        return mainPart;
    }

    private static MainPart getNPPhraseMainPart(Tree tree) {
        MainPart mainPart = new MainPart();
        StringBuilder sbResult = new StringBuilder();
        List<String> phraseList = getPhraseList("NP", tree);
        for (String phrase : phraseList) {
            sbResult.append(phrase);
        }
        mainPart.result = sbResult.toString();
        return mainPart;
    }

    /**
     * 从句子中提取最小粒度的短语
     *
     * @param type
     * @param sentence
     * @return
     */
    public static List<String> getPhraseList(String type, String sentence) {
        return getPhraseList(type, lp.apply(seg(sentence)));
    }

    private static List<String> getPhraseList(String type, Tree tree) {
        List<String> phraseList = new LinkedList<String>();
        for (Tree subtree : tree) {
            if (subtree.isPrePreTerminal() && subtree.label().value().equals(type)) {
                StringBuilder sbResult = new StringBuilder();
                for (Tree leaf : subtree.getLeaves()) {
                    sbResult.append(leaf.value());
                }
                phraseList.add(sbResult.toString());
            }
        }
        return phraseList;
    }

    /**
     * 合并名词性短语为一个节点
     *
     * @param tdls   依存关系集合
     * @param target 目标节点
     */
    private static void combineNN(Collection<TypedDependency> tdls, TreeGraphNode target) {
        if (target == null) return;
        for (TypedDependency td : tdls) {
            // 依存关系的出发节点，依存关系，以及结束节点
            TreeGraphNode gov = td.gov();
            GrammaticalRelation reln = td.reln();
            String shortName = reln.getShortName();
            TreeGraphNode dep = td.dep();
            if (gov == target) {
                switch (shortName) {
                    case "nn":
                        target.setValue(dep.toString("value") + target.value());
                        return;
                }
            }
        }
    }

    private static TreeGraphNode getRootNode(Collection<TypedDependency> tdls) {
        for (TypedDependency td : tdls) {
            if (td.reln() == GrammaticalRelation.ROOT) {
                return td.dep();
            }
        }

        return null;
    }

    /**
     * 分词
     *
     * @param sentence 句子
     * @return 分词结果
     */
    private static List<Word> seg(String sentence) {
        //分词
        LOG.info("正在对短句进行分词：" + sentence);
        List<Word> wordList = new LinkedList<>();
        List<Term> terms = HanLP.segment(sentence);
        StringBuffer sbLogInfo = new StringBuffer();
        for (Term term : terms) {
            Word word = new Word(term.word);
            wordList.add(word);
            sbLogInfo.append(word);
            sbLogInfo.append(' ');
        }
        LOG.info("分词结果为：" + sbLogInfo);
        return wordList;
    }

    public static MainPart getNewSent(String sentence, String delimiter) {
        List<Word> wordList = new LinkedList<>();
        for (String word : sentence.split(delimiter)) {
            wordList.add(new Word(word));
        }
        return getMainPart(wordList);
    }

//    /**
//     * 调用演示
//     *
//     * @param args
//     */
//    public static void main(String[] args) {
//        String[] testCaseArray = {
//                "我一直很喜欢你",
//                "你被我喜欢",
//                "美丽又善良的你被卑微的我深深的喜欢着……",
//                "只有自信的程序员才能把握未来",
//                "主干识别可以提高检索系统的智能",
//                "这个项目的作者是hankcs",
//                "hankcs是一个无门无派的浪人",
//                "搜索hankcs可以找到我的博客",
//                "静安区体育局2013年部门决算情况说明",
//                "这类算法在有限的一段时间内终止",
//        };
//        for (String testCase : testCaseArray) {
//            MainPart mp = MainPartExtractor.getMainPart(testCase);
//            System.out.printf("%s\t%s\n", testCase, mp);
//        }
//    }

    // 提取出主谓宾并且重组句子（注意：代码部分是narrow所以主谓宾不可以放在第一二三的位置，否则会导致有所损失）
    // input: 拆分开的句子数组
    // output: 拆分开的句子数组
    public static String reformPara(String[] originlSents) {
        StringBuilder newPara = new StringBuilder();   //重组后的段落
        String newSent = "";
        MainPart mainPart = new MainPart();
        for (String sent : originlSents) {
            newSent = MainPartExtractor.getNewSent(sent, getMainPart(seg(sent), mainPart));
            newPara.append(newSent);
        }
        System.out.println("newPara:" + newPara);
        return newPara.toString();
    }
}