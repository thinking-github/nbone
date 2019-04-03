package org.nbone.demo.javabase;

/**
 * @author chenyicheng
 * @version 1.0
 * @since 2019-04-02
 */
public class ArithmeticExceptionDemo {


    public   ArithmeticExceptionDemo() {

    }

    public  static void main(String[] args) {

        int i = 1;
        int j = 1;

        try {
            i++; //becomes 2
            j--; //becomes 0

            if (i / j > 1) {
                i++;
            }
        } catch (ArithmeticException e) {
            System.out.println("arithmetic error.");
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Array index out of bounds");
        } catch (Exception e) {
            System.out.println("generic exception.");
        } finally {

            System.out.println("In the finally block");

        }
    }
}
