package org.nbone.demo.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;


public class InheritedAnnotationTest {
	
	public static void main(String[] args) throws SecurityException, NoSuchMethodException {  
        
        Class<SubClassImpl> clazz=SubClassImpl.class;  
         
          
          //abstractMethod  
        Method method = clazz.getMethod("abstractMethod", new Class[]{});  
        if(method.isAnnotationPresent(InheritedAnnotation.class)){  
            InheritedAnnotation ma = method.getAnnotation(InheritedAnnotation.class);  
         System.out.println("子类实现的抽象方法继承到父类抽象方法中的Annotation,其信息如下：");  
         System.out.println(ma.value());  
        }else{  
         System.out.println("子类实现的抽象方法没有继承到父类抽象方法中的Annotation");  
        }  
          
        
        Method methodOverride = clazz.getMethod("doExtends", new Class[]{});  
        if(methodOverride.isAnnotationPresent(InheritedAnnotation.class)){  
            InheritedAnnotation ma = methodOverride.getAnnotation(InheritedAnnotation.class);  
         System.out.println("子类doExtends方法继承到父类doExtends方法中的Annotation,其信息如下：");  
         System.out.println(ma.value());  
        }else{  
         System.out.println("子类doExtends方法没有继承到父类doExtends方法中的Annotation");  
        }  
        
        Annotation[] ann =  clazz.getAnnotations();
        Annotation[] ann1 =  clazz.getDeclaredAnnotations();
        
        if(clazz.isAnnotationPresent(InheritedAnnotation.class)){  
            InheritedAnnotation cla = clazz.getAnnotation(InheritedAnnotation.class);  
         System.out.println("子类继承到父类类上Annotation,其信息如下：");  
         System.out.println(cla.value());  
        }else{  
            System.out.println("子类没有继承到父类类上Annotation");  
        }  

        

  }    

}
