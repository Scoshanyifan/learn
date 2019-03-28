package com.scosyf.learn.bytecode;

/**
 * @author: KunBu
 * @time: 2019/2/21 17:44
 * @description:
 */
public class This {

    private int i = 1;
    private static int ii = 2;
    private static final int iii = 111;

    public This() {
        i = 3;
        ii = 4;
    }

    public static int getInt() {
        return ii;
    }

    public static int getStaticInt() {
        return iii;
    }

    public static void main(String[] args) {

    }
}
