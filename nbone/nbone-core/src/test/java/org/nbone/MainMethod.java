package org.nbone;

public class MainMethod {  
    public static void main(String[] args) {  
        boolean flag = true;  
        flag &= true;  
        System.out.println("true\t&=\ttrue\t==>\t" + flag);  
        flag = true;  
        flag &= false;  
        System.out.println("true\t&=\tfalse\t==>\t" + flag);   
        flag = false;  
        flag &= true;  
        System.out.println("false\t&=\ttrue\t==>\t" + flag);   
        flag = false;  
        flag &= false;  
        System.out.println("false\t&=\tfalse\t==>\t" + flag+"\n");   
          
        flag = true;  
        flag |= true;  
        System.out.println("true\t|=\ttrue\t==>\t" + flag);  
        flag = true;  
        flag |= false;  
        System.out.println("true\t|=\tfalse\t==>\t" + flag);   
        flag = false;  
        flag |= true;  
        System.out.println("false\t|=\ttrue\t==>\t" + flag);   
        flag = false;  
        flag |= false;  
        System.out.println("false\t|=\tfalse\t==>\t" + flag+"\n");   
          
        System.out.println("^=  相同为真，不同为假");  
        flag = true;  
        flag ^= true;  
        System.out.println("true\t^=\ttrue\t==>\t" + flag);  
        flag = true;  
        flag ^= false;  
        System.out.println("true\t^=\tfalse\t==>\t" + flag);   
        flag = false;  
        flag ^= true;  
        System.out.println("false\t^=\ttrue\t==>\t" + flag);   
        flag = false;  
        flag ^= false;  
        System.out.println("false\t^=\tfalse\t==>\t" + flag);   
    }  
}  