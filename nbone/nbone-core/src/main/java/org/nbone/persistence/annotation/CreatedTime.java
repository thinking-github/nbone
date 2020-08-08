package org.nbone.persistence.annotation;

import java.lang.annotation.*;


/**
 * Marks a property as the creation timestamp of the containing entity. The property value will be set to the current
 * VM date exactly once when saving the owning entity for the first time.
 * <p>
 * Supported property types:
 * <ul>
 * <li>{@link java.util.Date}</li>
 * <li>{@link java.util.Calendar}</li>
 * <li>{@link java.sql.Date}</li>
 * <li>{@link java.sql.Time}</li>
 * <li>{@link java.sql.Timestamp}</li>
 * </ul>
 *
 * @author thinking
 * @version 1.0
 * @since 2020-01-08
 * @see org.hibernate.annotations.CreationTimestamp
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CreatedTime {
}
