package com.scosyf.learn.bytecode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author: KunBu
 * @time: 2019/2/21 11:02
 * @description:
 */
public class Concurrency {

    public synchronized static void syncMethod() {
        //TODO
    }

    public void syncBlock() {
        synchronized (this) {
            //TODO
        }
    }

    public static int tryCatch() {
        int i = 1;
        try {
            i = 2;
//            return i;
            throw new RuntimeException("1");
        } catch (Exception e) {
            i = 3;
            return i;
        } finally {
            i = 4;
            return i;
        }
    }

    public static void tryResource() {
        try(BufferedReader br = new BufferedReader(new FileReader("hello"))) {
            //TODO
            br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println(tryCatch());
    }
}
