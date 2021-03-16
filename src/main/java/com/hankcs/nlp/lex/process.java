package com.hankcs.nlp.lex;

import com.hankcs.nlp.lex.MainPartExtractor;

import java.io.*;
import java.util.ArrayList;

public class process {
    public static void main(String[] args) throws IOException {

        try {
            String pathname = "src/main/java/com/hankcs/nlp/data/fudan.train.6.txt";
            File filename = new File(pathname); // 要读取以上路径的input txt文件
            InputStreamReader reader = new InputStreamReader(
                    new FileInputStream(filename)); // 建立一个输入流对象reader
            BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言
            String line = br.readLine();
            String[] paraList = new String[0];
            String[] sents = new String[0];
            String newPara = "";
            File f=new File(
                    "fudan.train.processed.6.txt");
            FileWriter fos1=new FileWriter(f,true);
            while (line != null) {
                paraList = null;
                sents = null;
                // 将读到的段落拆分为句子，再扔进reformPara
                paraList = line.split("\\t");
                String label = paraList[0];
//
                sents = paraList[1].split("[\\，\\。\\？\\!\\：\\,\\.\\:\\?\\(\\)\\（\\）]");
                //重组
                newPara = MainPartExtractor.reformPara(sents);
                fos1.write(label + "\t" + newPara + "\n");
                //下一行
                line = br.readLine();

            }
            fos1.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
