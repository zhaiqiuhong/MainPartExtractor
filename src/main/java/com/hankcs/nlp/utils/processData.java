package com.hankcs.nlp.utils;

import java.io.*;

public class processData {
    //测试push
    //cut 提取关键词之后的并输出
    private static void cutData() throws IOException {
        String pathname = "src/main/java/com/hankcs/nlp/data/fudan.train.5.txt";
        File filename = new File(pathname); // 要读取以上路径的input txt文件
        InputStreamReader reader = new InputStreamReader(
                new FileInputStream(filename)); // 建立一个输入流对象reader
        BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言
        String line = br.readLine();
        while (line != null) {
            // 将读到的段落拆分为句子，再扔进reformPara
            String[] paraList = line.split("\\t");
            String label = paraList[0];
            String[] sents = paraList[1].split("[\\，\\。\\？\\!\\：\\,\\.\\:\\?\\(\\)\\（\\）]");
            if (label.equals("艺术") &&
                    sents[0].equals("【 文献号 】4-94【原文出处】《文艺研究》【原刊地名】京【原刊期号】200104【原刊页号】41～47【分 类 号】J1【分 类 名】文艺理论【复印期号】200112【 标  题 】“非客观性”艺术形态学的理解与分类问题【 作  者 】杨小清【作者简介】杨小清　1954年生")) {
                File f = new File(
                        "src/main/java/com/hankcs/nlp/data/fudan.train.5.txt");
                FileWriter fos1 = new FileWriter(f, true);
                while (line != null) {
                    fos1.write(line + "\n");
                    line = br.readLine();
                }
                fos1.close();
                break;
            }


            line = br.readLine();
        }
    }

    //cut2  提取关键词之前的并输出
    private static void cutDataBefore() throws IOException {
        String pathname = "src/main/java/com/hankcs/nlp/data/fudan.train.4.txt";
        File filename = new File(pathname); // 要读取以上路径的input txt文件
        InputStreamReader reader = new InputStreamReader(
                new FileInputStream(filename)); // 建立一个输入流对象reader
        BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言
        String line = br.readLine();
        while (line != null) {
            // 将读到的段落拆分为句子，再扔进reformPara
            String[] paraList = line.split("\\t");
            String label = paraList[0];
            String[] sents = paraList[1].split("[\\，\\。\\？\\!\\：\\,\\.\\:\\?\\(\\)\\（\\）]");
            if (label.equals("政治") &&
                    sents[0].equals("外事简讯新华社北京５月２５日电外事简讯    全国人大常委会副委员长习仲勋２５日在人民大会堂会见并宴请了由全国选举书记卡罗利娜・托莱多为团长的玻利维亚民族主义民主行动党代表团")) {
                break;
            }
            File f = new File(
                    "src/main/java/com/hankcs/nlp/data/fudan.train.4.txt");
            FileWriter fos1 = new FileWriter(f, true);
            fos1.write(line + "\n");
            fos1.close();

            line = br.readLine();
        }
    }

    //去除空格 + 用来转换txt文件名
    private static void delSpace() throws IOException {
        for (int i = 1; i <= 6; i++) {
            //原文档（需去空格）
            String pathname = "src/main/java/com/hankcs/nlp/data/fudan.train." +
                    String.valueOf(i) + String.valueOf(i) + ".txt";
            File filename = new File(pathname); // 要读取以上路径的input txt文件
            InputStreamReader reader = new InputStreamReader(
                    new FileInputStream(filename)); // 建立一个输入流对象reader
            BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言
            String line = br.readLine();
            //去空格后输出的文档路径
            File f = new File(
                    "src/main/java/com/hankcs/nlp/data/fudan.train." + i + ".txt");
            FileWriter fos1 = new FileWriter(f, true);
            while (line != null) {
                fos1.write(line.replace(" ", "") + "\n");
                line = br.readLine();
            }
            fos1.close();
        }
    }

    //去除空白符
    private static void delSpecialSpace() throws IOException {
        for (int i = 1; i <= 6; i++) {
            //原文档（需去空格）
            String pathname = "src/main/java/com/hankcs/nlp/data/fudan.test." +
                     i + ".txt";
            File filename = new File(pathname); // 要读取以上路径的input txt文件
            InputStreamReader reader = new InputStreamReader(
                    new FileInputStream(filename)); // 建立一个输入流对象reader
            BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言
            String line = br.readLine();
            //去空格后输出的文档路径
            File f = new File(
                    "src/main/java/com/hankcs/nlp/data/fudan.test." +
                            String.valueOf(i) + String.valueOf(i)+ ".txt");
            FileWriter fos1 = new FileWriter(f, true);
            while (line != null) {
                fos1.write(line.split("\t")[0] + "\t"+
                        line.split("\t")[1].replace("\\s*", "") + "\n");
                line = br.readLine();
            }
            fos1.close();
        }
    }

    //提取正文
    private static void getContent() throws IOException {
        for (int i = 1; i <= 6; i++) {
            //原文档（需去空格）
            String pathname = "src/main/java/com/hankcs/nlp/data/fudan.train." +
                    i + ".txt";
            File filename = new File(pathname); // 要读取以上路径的input txt文件
            InputStreamReader reader = new InputStreamReader(
                    new FileInputStream(filename)); // 建立一个输入流对象reader
            BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言
            String line = br.readLine();
            //去空格后输出的文档路径
            File f = new File(
                    "src/main/java/com/hankcs/nlp/data/fudan.train." +
                            String.valueOf(i) + String.valueOf(i) + ".txt");
            FileWriter fos1 = new FileWriter(f, true);
            while (line != null) {
                String[] lineArray = line.split("【正文】", 2);
                if (lineArray.length > 1) {  //表示是文献，则提取正文内容
                    //避免出现【正文】后为空的情况
                    if (lineArray[1].replace("\\s*", "").length() > 0) {
                        String label = line.split("\t")[0];
                        fos1.write(label + "\t" + lineArray[1] + "\n");
                    }
                } else {
                    fos1.write(line + "\n");
                }
                line = br.readLine();
            }
            fos1.close();
        }
    }

    //去除参考文献
    private static void delReference() throws IOException {
        for (int i = 1; i <= 6; i++) {
            //原文档（需去空格）
            String pathname = "src/main/java/com/hankcs/nlp/data/fudan.test." +
                     i + ".txt";
            File filename = new File(pathname); // 要读取以上路径的input txt文件
            InputStreamReader reader = new InputStreamReader(
                    new FileInputStream(filename)); // 建立一个输入流对象reader
            BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言
            String line = br.readLine();
            //去空格后输出的文档路径
            File f = new File(
                    "src/main/java/com/hankcs/nlp/data/fudan.test." +
                             String.valueOf(i) + String.valueOf(i) + ".txt");
            FileWriter fos1 = new FileWriter(f, true);
            while (line != null) {
//                String[] lineArray = line.split("参考文献", 2);
                if (line.split("注释①", 2).length > 1) {  //表示有【参考文献】，则提取正文内容
                    if (line.split("注释①", 2)[0].replace("\\s*", "").length() > 0) {
                        fos1.write(line.split("注释①", 2)[0] + "\n");
                    }
                } else if (line.split("注释（1）", 2).length > 1) {  //表示有【参考文献】，则提取正文内容
                    if (line.split("注释（1）", 2)[0].replace("\\s*", "").length() > 0) {
                        fos1.write(line.split("注释（1）", 2)[0] + "\n");
                    }
                }else if (line.split("注释＠①", 2).length > 1) {  //表示有【参考文献】，则提取正文内容
                    if (line.split("注释＠①", 2)[0].replace("\\s*", "").length() > 0) {
                        fos1.write(line.split("注释＠①", 2)[0] + "\n");
                    }
                } else if (line.split("【注释】", 2).length > 1) {  //表示有【参考文献】，则提取正文内容
                    if (line.split("【注释】", 2)[0].replace("\\s*", "").length() > 0) {
                        fos1.write(line.split("【注释】", 2)[0] + "\n");
                    }
                }else if (line.split("注释〔1〕", 2).length > 1) {  //表示有【参考文献】，则提取正文内容
                    if (line.split("注释〔1〕", 2)[0].replace("\\s*", "").length() > 0) {
                        fos1.write(line.split("注释〔1〕", 2)[0] + "\n");
                    }
                }else if (line.split("参考文献", 2).length > 1) {  //表示有【参考文献】，则提取正文内容
                    if (line.split("参考文献", 2)[0].replace("\\s*", "").length() > 0) {
                        fos1.write(line.split("参考文献", 2)[0] + "\n");
                    }
                }else if (line.split("注释：", 2).length > 1) {  //表示有【参考文献】，则提取正文内容
                    if (line.split("注释：", 2)[0].replace("\\s*", "").length() > 0) {
                        fos1.write(line.split("注释：", 2)[0] + "\n");
                    }
                }else {
                    fos1.write(line + "\n");
                }
                line = br.readLine();
            }
            fos1.close();
        }
    }


    //去除【正文】为空的条目
    private static void delNullContent() throws IOException {
        for (int i = 1; i <= 6; i++) {
            //原文档（需去空格）
            String pathname = "src/main/java/com/hankcs/nlp/data/fudan.test." +
                    i + ".txt";
            File filename = new File(pathname); // 要读取以上路径的input txt文件
            InputStreamReader reader = new InputStreamReader(
                    new FileInputStream(filename)); // 建立一个输入流对象reader
            BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言
            String line = br.readLine();
            //去空格后输出的文档路径
            File f = new File(
                    "src/main/java/com/hankcs/nlp/data/fudan.test." +
                            String.valueOf(i) + String.valueOf(i) + ".txt");
            FileWriter fos1 = new FileWriter(f, true);
            while (line != null) {
                if (line.split("\t").length > 1 &&
                        line.split("\t")[1].replace("\\s*", "").length() > 0) {
                    fos1.write(line + "\n");
                    line = br.readLine();
                } else {
                    System.out.println("train. " + i + ".txt\t");
                    line = br.readLine();
                    System.out.println("the line after the null line is: " + line + "\n");
                }


            }
            fos1.close();
        }
    }


    //联结多个文件
    private static void concatData() throws IOException {
        File f = new File(
                "src/main/java/com/hankcs/nlp/data/fudan.test.processed.txt");
        FileWriter fos1 = new FileWriter(f, true);
        for (int i = 1; i <= 6; i++) {
            String pathname = "src/main/java/com/hankcs/nlp/data/fudan.test.processed."
                    + i + ".txt";
            File filename = new File(pathname); // 要读取以上路径的input txt文件
            InputStreamReader reader = new InputStreamReader(
                    new FileInputStream(filename)); // 建立一个输入流对象reader
            BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言
            String line = br.readLine();
            while (line != null) {
                fos1.write(line + "\n");
                line = br.readLine();
            }

        }
        fos1.close();
    }

    //构造新的train和test文本文件
    private static void mkFile() throws IOException {
        String pathnameTest = "src/main/java/com/hankcs/nlp/data/cnews.test.processed.txt";
        File filenameTest = new File(pathnameTest); // 要读取以上路径的input txt文件
        InputStreamReader readerTest = new InputStreamReader(
                new FileInputStream(filenameTest)); // 建立一个输入流对象reader
        BufferedReader brTest = new BufferedReader(readerTest); // 建立一个对象，它把文件内容转成计算机能读懂的语言
        String lineTest = brTest.readLine();
        int i;
        //test
        File fTest = new File(
                "src/main/java/com/hankcs/nlp/data/cnews.test.new.txt");
        FileWriter fosTest1 = new FileWriter(fTest, true);
        for (i = 0; i < 600; i++) {
            fosTest1.write(lineTest + "\n");
            lineTest = brTest.readLine();
        }
        fosTest1.close();

        //train
        File fTrain = new File(
                "src/main/java/com/hankcs/nlp/data/cnews.train.new.txt");
        FileWriter fosTrain1 = new FileWriter(fTrain, true);
        while (lineTest != null) {
            fosTrain1.write(lineTest + "\n");
            lineTest = brTest.readLine();
        }

        String pathnameTrain = "src/main/java/com/hankcs/nlp/data/cnews.train.processed.txt";
        File filenameTrain = new File(pathnameTrain); // 要读取以上路径的input txt文件
        InputStreamReader readerTrain = new InputStreamReader(
                new FileInputStream(filenameTrain)); // 建立一个输入流对象reader
        BufferedReader brTrain = new BufferedReader(readerTrain); // 建立一个对象，它把文件内容转成计算机能读懂的语言
        String lineTrain = brTrain.readLine();
        while (lineTrain != null) {
            fosTrain1.write(lineTrain + "\n");
            lineTrain = brTrain.readLine();
        }
        fosTrain1.close();

    }

    //构造对应的train和test文本文件
    private static void mkOldFile() throws IOException {
        String pathnameTest = "src/main/java/com/hankcs/nlp/data/cnews.test.txt";
        File filenameTest = new File(pathnameTest);
        InputStreamReader readerTest = new InputStreamReader(
                new FileInputStream(filenameTest));
        BufferedReader brTest = new BufferedReader(readerTest);
        String lineTest = brTest.readLine();
        int i;
        //test
        File fTest = new File(
                "src/main/java/com/hankcs/nlp/data/cnews.test.old.txt");
        FileWriter fosTest1 = new FileWriter(fTest, true);
        for (i = 0; i < 600; i++) {
            fosTest1.write(lineTest + "\n");
            lineTest = brTest.readLine();
        }
        fosTest1.close();

        //train
        File fTrain = new File(
                "src/main/java/com/hankcs/nlp/data/cnews.train.old.txt");
        FileWriter fosTrain1 = new FileWriter(fTrain, true);
        for (i = 600; i < 4895; i++) {
            fosTrain1.write(lineTest + "\n");
            lineTest = brTest.readLine();
        }

        String pathnameTrain = "src/main/java/com/hankcs/nlp/data/cnews.train.txt";
        File filenameTrain = new File(pathnameTrain); // 要读取以上路径的input txt文件
        InputStreamReader readerTrain = new InputStreamReader(
                new FileInputStream(filenameTrain)); // 建立一个输入流对象reader
        BufferedReader brTrain = new BufferedReader(readerTrain); // 建立一个对象，它把文件内容转成计算机能读懂的语言
        String lineTrain = brTrain.readLine();
        for (i = 0; i < 1889; i++) {
            fosTrain1.write(lineTrain + "\n");
            lineTrain = brTrain.readLine();
        }
        fosTrain1.close();

    }

    public static void main(String[] args) throws IOException {
//        cutData();
//        cutDataBefore();
        concatData();
//        mkFile();
//        mkOldFile();
//        delSpace();   //去除空格 + 用来转换txt文件名
//        getContent();   //提取正文
//        delNullContent();   //删除【正文】为空的内容
//        delSpecialSpace();  //删除空白符
//        delReference();   //去除参考文献
    }
}
