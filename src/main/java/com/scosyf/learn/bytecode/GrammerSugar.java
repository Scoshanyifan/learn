package com.scosyf.learn.bytecode;

/**
 * @author: KunBu
 * @time: 2019/2/21 10:35
 * @description:
 */
public class GrammerSugar {

    public static void wrapperTest_1() {
        int i = 128;
        boolean b = i == new Integer(128);
    }

    public static void wapperTest_2() {
        Integer i1 = 127;
        Integer i2 = new Integer(127);
        boolean b1 = i1 == i2;
        boolean b2 = i1.equals(i2);
        System.out.println(b1);
        System.out.println(b2);
    }

    public static int switchTest(String key) {
        switch (key) {
            case "test":
                return 1;
            case "demo":
                return 2;
            default:
                return -1;
        }
    }

    public static void foreachTest(Object... params) {
        for(Object obj : params) {
            //
        }
    }

    public static void main(String[] args) {
        wapperTest_2();
    }
}
