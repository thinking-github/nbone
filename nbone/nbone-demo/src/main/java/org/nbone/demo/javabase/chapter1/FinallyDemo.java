package org.nbone.demo.javabase.chapter1;


/**
 * @author chenyicheng
 * @version 1.0
 * @since 2019-04-01
 */
public class FinallyDemo {

    public static void main(String[] args) {
        System.out.println(proveIt());
        System.out.println(getANumber());
        System.out.println(getNumber());

    }

    /**
     * finally 语句在 return 之前执行
     * @return
     */
    public static int proveIt() {
        try {
            return 1;
        } finally {
            System.out.println("finally block is run before method returns.");
        }
    }


    public static int getANumber() {
        try {
            return 7;
        } finally {
            return 43;
        }
    }

    /**
     * 如果finally块返回一个值，它将覆盖try / catch块中抛出的任何异常
     * @return
     */
    public static int getNumber()  {
        try {
            String  ss = null;
            ss.length();
        }  finally {
            System.out.println("finally");
            return 43;
        }
        //return 43;
    }

}
