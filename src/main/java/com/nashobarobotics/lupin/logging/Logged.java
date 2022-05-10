package com.nashobarobotics.lupin.logging;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Logged {
    String name() default "";
}
