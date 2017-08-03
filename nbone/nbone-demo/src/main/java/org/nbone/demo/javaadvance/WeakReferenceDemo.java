package org.nbone.demo.javaadvance;

import java.util.WeakHashMap;
 
class Element {
    private String ident;
 
    public Element(String id) {
       ident = id;
    }
 
    public String toString() {
       return ident;
    }
 
    public int hashCode() {
       return ident.hashCode();
    }
 
    public boolean equals(Object obj) {
       return obj instanceof Element && ident.equals(((Element) obj).ident);
    }
   
    protected void finalize(){
       System.out.println("Finalizing "+getClass().getSimpleName()+" "+ident);
    }
}
 
class Key extends Element{
    public Key(String id){
       super(id);
    }
}
 
class Value extends Element{
    public Value (String id){
       super(id);
    }
}
/**
 * CanonicalMapping
 * Java中的弱应用案例   案例中奇数存在弱应用被垃圾收集，偶数存在强引用被保留了下来
 * @author thinking
 *
 */
public class WeakReferenceDemo {
    public static void main(String[] args){
       int size=1000;
       Key[] keys=new Key[size];
       WeakHashMap<Key,Value> map=new WeakHashMap<Key,Value>();
       for(int i=0;i<size;i++){
           Key k=new Key(Integer.toString(i));
           Value v=new Value(Integer.toString(i));
           if(i%2==0)
              keys[i]=k;
           map.put(k, v);
       }
       System.out.println(map);
       System.gc();
       System.out.println(map);
    }
}
