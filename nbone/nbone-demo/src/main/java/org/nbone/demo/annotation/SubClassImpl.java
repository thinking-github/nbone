package org.nbone.demo.annotation;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;

@Primary
@Lazy
public class SubClassImpl extends AbstractParent implements Feature {
	
	 @Override  
	    public void abstractMethod() {  
	    System.out.println("子类实现抽象父类的抽象方法");  
	          
	    }  

}
