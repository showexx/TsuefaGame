package io.proj3ct.TsuefaGame.service;

import org.apache.commons.io.*;

import java.io.*;

public class FileCheck {

    public String split(String userName, String array[]) throws IOException {
        String str = readFirstLine(userName,array) + "\n" + readSecondLine(userName,array) + "\n" + readThirdLine(userName,array);
        return str;
    }

    public String readFirstLine(String userName, String array[]) throws IOException {
        File file = new File("C:\\var\\" + userName + ".txt");
        String firstLine = FileUtils.readLines(file).get(0);

        return firstLine.substring(8);
    }

    public String readSecondLine(String userName, String array[]) throws IOException {
        File file = new File("C:\\var\\" + userName + ".txt");
        String secondLine = FileUtils.readLines(file).get(1);

        return secondLine.substring(10);
    }

    public String readThirdLine(String userName, String array[]) throws IOException {
        File file = new File("C:\\var\\" + userName + ".txt");
        String thirdLine = FileUtils.readLines(file).get(2);

        return thirdLine.substring(9);
    }

    public String readFourthlINE(String userName, String array[]) throws IOException {
        File file = new File("C:\\var\\" + userName + ".txt");
        String thirdLine = FileUtils.readLines(file).get(3);

        return thirdLine.substring(10);
    }

}
