package com.namics.oss.magnolia.appbuilder.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface ChooserDialogFactory {
    String id();
    String label();
    boolean hasFullTextSearch() default true;

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD})
    @interface Order {
        int value();
    }
}
