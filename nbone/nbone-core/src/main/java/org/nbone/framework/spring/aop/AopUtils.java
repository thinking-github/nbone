package org.nbone.framework.spring.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * 
 * @author thinking
 * @version 1.0 
 * @since 2014-07-14
 * 
 */
public class AopUtils {

	/**
	 * 获取class的全路径（包名+类名）
	 * 
	 * @param jp
	 * @return
	 */
	public static String getLongClassName(JoinPoint jp) {
		//getSignature如果没有启用AOP代理,目标类是接口的话,方法getSignature获取的将是接口的名称
		//String classNameAll = jp.getSignature().getDeclaringTypeName();// 类路径+名称
		String classNameAll = jp.getTarget().getClass().getName();// 类路径+名称
		return classNameAll;
	}

	/**
	 * 只包括类名(不包括包名)
	 * 
	 * @param jp
	 * @return
	 */
	public static String getShortClassName(JoinPoint jp) {

		String className = jp.getTarget().getClass().getSimpleName();

		return className;
	}

	/**
	 * 获取当前调用的方法名称
	 * 
	 * @param jp
	 * @return
	 */
	public static String getMethodName(JoinPoint jp) {

		String className = jp.getSignature().getName();

		return className;
	}

	/**
	 * 获取全部ClassName和方法名称
	 * @param jp
	 * @return
	 */
	public static String getClassNameAndMethodName(JoinPoint jp) {

		String className = getLongClassName(jp);
		String methodName = getMethodName(jp);
		
		String classAndMethod = className + "." + methodName;

		return classAndMethod;
	}
	/**
	 * 获取ClassName和方法名称, 例如: <b>AopUtils.getShortClassAndMethodName</b>
	 * 
	 * @param jp
	 * @return
	 */
	public static String getShortClassAndMethodName(JoinPoint jp) {

		String className = getShortClassName(jp);
		String methodName = getMethodName(jp);
		String classAndMethod = className + "." + methodName;

		return classAndMethod;
	}
	
	/**
	 * 获取拦截的方法
	 * @param jp
	 * @return
	 */
	public static Method getMethod(JoinPoint jp) {
		Signature signature = jp.getSignature();
		if(signature instanceof MethodSignature){
			MethodSignature methodSignature = (MethodSignature) signature;
			return methodSignature.getMethod();
		}
		return null;
	}
	
	public static <T extends Annotation> T getMethodAnnotation(JoinPoint jp,Class<T> annotationClass) {
		Method method =  getMethod(jp);
		if(method != null){
			return  method.getAnnotation(annotationClass);
		}
		return null;
	}
	
	
    public boolean isMethodAnnotationPresent(JoinPoint jp,Class<? extends Annotation> annotationClass) {
            return getMethodAnnotation(jp,annotationClass) != null;
    }
	

}
