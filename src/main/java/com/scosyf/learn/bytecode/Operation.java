package com.scosyf.learn.bytecode;

/**
 * @author: KunBu
 * @time: 2019/2/21 17:40
 * @description:
 */
public class Operation {

    public static int autoIncBefore() {
        int i = 0;
        i = i++;
        System.out.println(i++);
        return i;
    }

    public static void autoIncAfter() {
        int i = 0;
        i = ++i;
    }

    public static void compare() {
        int i = 0;
        i++;
    }

    public static void main(String[] args) {
        System.out.println(autoIncBefore());
    }
}
