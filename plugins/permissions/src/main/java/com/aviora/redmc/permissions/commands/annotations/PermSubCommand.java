package com.aviora.redmc.permissions.commands.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface PermSubCommand {

    String[] path();

    String description() default "";

    String permission() default "";

    boolean opOnly() default true;
}
