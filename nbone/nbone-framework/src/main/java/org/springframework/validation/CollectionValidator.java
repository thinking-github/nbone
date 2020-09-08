package org.springframework.validation;

import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.Collection;

/**
 * @author thinking
 * @version 1.0
 * @since 2019-12-19
 */
public class CollectionValidator implements Validator {

    private final Validator validator;

    public CollectionValidator(Validator Validator) {
        this.validator = Validator;
    }

    public CollectionValidator(LocalValidatorFactoryBean validatorFactory) {
        this.validator = validatorFactory;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Collection.class.isAssignableFrom(clazz) || clazz.isArray();
    }

    @Override
    @SuppressWarnings("rawtypes")
    public void validate(Object target, Errors errors) {
        if (target instanceof Collection) {
            Collection collection = (Collection) target;
            for (Object object : collection) {
                ValidationUtils.invokeValidator(validator, object, errors);
            }
        } else if (target.getClass().isArray()) {
            Object[] collection = (Object[]) target;
            for (Object object : collection) {
                ValidationUtils.invokeValidator(validator, object, errors);
            }
        }

    }
}
