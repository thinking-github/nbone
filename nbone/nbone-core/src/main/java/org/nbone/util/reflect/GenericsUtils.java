package org.nbone.util.reflect;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Generics的util类.(泛型工具)
 *
 * @author thinking
 * @version 1.0
 * @since 2016年4月4日
 */
public class GenericsUtils {
    private static final Log log = LogFactory.getLog(GenericsUtils.class);

    private GenericsUtils() {
    }

    /**
     * 通过反射,获得定义Class时声明的父类的范型参数的类型. 如public BookManager extends GenricManager<Book>
     *
     * @param clazz The class to introspect
     * @return the first generic declaration, or <code>Object.class</code> if cannot be determined
     */
    public static Class<?> getSuperClassGenricType(Class<?> clazz) {
        return getSuperClassGenricType(clazz, 0);
    }

    /**
     * 通过反射,获得定义Class时声明的父类的范型参数的类型. 如public BookManager extends GenricManager<Book>
     * <p>
     * 如public UserDao extends HibernateDao<User,Long>
     *
     * @param clazz clazz The class to introspect
     * @param index the Index of the generic ddeclaration,start from 0.
     * @return the index generic declaration, or <code>Object.class</code> if cannot be determined
     */
    @SuppressWarnings("rawtypes")
    public static Class<?> getSuperClassGenricType(Class<?> clazz, int index) {

        Type genType = clazz.getGenericSuperclass();

        if (!(genType instanceof ParameterizedType)) {
            log.warn(clazz.getSimpleName() + "'s superclass not ParameterizedType");
            return Object.class;
        }

        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

        if (index >= params.length || index < 0) {
            log.warn("Index: " + index + ", Size of " + clazz.getSimpleName() + "'s Parameterized Type: " + params.length);
            return Object.class;
        }
        Type type = params[index];
        if ((type instanceof ParameterizedType)) {
            Type rawType = ((ParameterizedType) type).getRawType();
            return (Class<?>) rawType;
        }

        if ((type instanceof Class)) {
            return (Class<?>)type;
        }
        //log.warn(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
        return Object.class;

    }


    public static Type[] getGenericReturnType(Method method, Class<?> clazz) {
        Type returnType = method.getGenericReturnType();
        if (returnType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) returnType;
            Class<?> rawType = (Class<?>) parameterizedType.getRawType();
            Type[] actualTypeArguments =  parameterizedType.getActualTypeArguments();
            if (clazz.isAssignableFrom(rawType)) {
                return actualTypeArguments;
            }
        }
        return null;
    }

    public static <T> Class<T> getGenericReturnTypeForMapKeyType(Method method) {
        Type[] actualTypeArguments = getGenericReturnType(method, Map.class);

        if (actualTypeArguments != null && actualTypeArguments.length == 2) {
            Type returnTypeParameter = actualTypeArguments[0];
            if (returnTypeParameter instanceof Class<?>) {
                return (Class<T>) returnTypeParameter;
            } else if (returnTypeParameter instanceof ParameterizedType) {
                return (Class<T>) ((ParameterizedType) returnTypeParameter).getRawType();
            }
        }
        return null;
    }
}
