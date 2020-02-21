package com.scosyf.learn.string;

/**
 * 字符串常量池、class常量池和运行时常量池
 * https://blog.csdn.net/qq_26222859/article/details/73135660
 *
 * 1. 全局字符串池（String Pool / String Literal Pool）
 * 2. 类文件常量池（Class Constant Pool）
 * 3. 运行时常量池（Runtime Constant Pool）
 *
 *
 * @author kunbu
 **/
public class StringPool {

    public static void main(String[] args) {

        // 1.创建abc实例，放在堆中，然后在全局字符串中存放abc的引用值
        String literalStr       = "abc";
        // 2.先在全局字符串中查找abc，若已存在则返回该引用值，否则像之前一样创建
        String literalStr2      = "abc";
        // 3.如果abc已存在（即全局字符串中有）则不会再创建abc实例；new会创建一个新的abc实例
        String newStr           = new String("abc");
        // 4.
        String internStr        = newStr.intern();
        String internStr2       = literalStr.intern();

        System.out.println(literalStr == literalStr2);
        System.out.println(literalStr == newStr);
        System.out.println(literalStr == internStr);
        System.out.println(literalStr == internStr2);

        System.out.println(newStr == internStr);
        System.out.println(newStr == internStr2);

        System.out.println(internStr == internStr2);
    }



}
