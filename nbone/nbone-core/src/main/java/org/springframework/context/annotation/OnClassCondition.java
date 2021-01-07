package org.springframework.context.annotation;

import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.ClassUtils;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * {@link Condition} that matches based on the value of a {@link ConditionalOnClass @ConditionalOnClass} annotation.
 *
 * @author thinking
 * @version 1.0
 * @see ProfileCondition
 * see org.springframework.boot.autoconfigure.condition.OnClassCondition
 * @since 2019-01-04
 */
class OnClassCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        ClassLoader classLoader = context.getClassLoader();
        if (classLoader == null) {
            classLoader = ClassUtils.getDefaultClassLoader();
        }
        MultiValueMap<String, Object> attrs = metadata.getAllAnnotationAttributes(ConditionalOnClass.class.getName(), true);

        if (attrs == null) {
            return false;
        }

        if (attrs != null) {
            List<String> candidates = new ArrayList<String>();
            addAll(candidates, attrs.get("value"));
            addAll(candidates, attrs.get("name"));
            for (Object value : candidates) {
                boolean present = MatchType.PRESENT.matches((String) value, classLoader);
                if (!present) {
                    return false;
                }
            }
        }

        return true;
    }

    private void addAll(List<String> list, List<Object> itemsToAdd) {
        if (itemsToAdd != null) {
            for (Object item : itemsToAdd) {
                Collections.addAll(list, (String[]) item);
            }
        }
    }


    private enum MatchType {
        PRESENT {
            @Override
            public boolean matches(String className, ClassLoader classLoader) {
                return ClassUtils.isPresent(className, classLoader);
            }

        },

        MISSING {
            @Override
            public boolean matches(String className, ClassLoader classLoader) {
                return !ClassUtils.isPresent(className, classLoader);
            }

        };

        public abstract boolean matches(String className, ClassLoader classLoader);

    }

}
