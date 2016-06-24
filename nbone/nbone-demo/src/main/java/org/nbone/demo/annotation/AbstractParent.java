package org.nbone.demo.annotation;

@InheritedAnnotation(value="parent") 
public abstract class AbstractParent {

	     @InheritedAnnotation(value = "parent abstractMethod ")  
	     public abstract void abstractMethod();  
	      
	    @InheritedAnnotation(value = "Parent's doExtends")  
	     public void doExtends() {  
	      System.out.println(" AbstractParent doExtends ...");  
	     }  
	  
	      
}  