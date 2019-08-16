package org.springframework.core.env;

import java.util.*;

/**
 * @author thinking
 * @version 1.0
 * @since 2019-08-07
 */

public class EnvironmentUtils {

    /**
     * spring boot  ConfigFileApplicationListener
     */
    public static final String APPLICATION_CONFIGURATION_PROPERTY_SOURCE_NAME = "applicationConfigurationProperties";


    /**
     * 判断环境变量里面是否含有某个前缀key
     *
     * @param environment
     * @param prefix
     * @return
     */
    public static boolean containsPrefix(ConfigurableEnvironment environment, String prefix) {
        MutablePropertySources mutablePropertySources = environment.getPropertySources();
        List<String> names = new ArrayList<String>(50);
        for (PropertySource<?> source : mutablePropertySources) {
            if (source instanceof EnumerablePropertySource) {
                names.addAll(Arrays.asList(((EnumerablePropertySource<?>) source).getPropertyNames()));
            }
        }

        return containsPrefix(names, prefix);
    }

    public static boolean envContainsPrefix(ConfigurableEnvironment environment, String prefix) {
        Map<String, Object> map = environment.getSystemEnvironment();
        return containsPrefix(map.keySet(), prefix);
    }

    public static boolean systemContainsPrefix(ConfigurableEnvironment environment, String prefix) {
        Map<String, Object> map = environment.getSystemProperties();
        return containsPrefix(map.keySet(), prefix);
    }


    private static boolean containsPrefix(Collection<String> names, String prefix) {
        if (names == null) {
            return false;
        }
        for (String name : names) {
            if (name.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }

}
