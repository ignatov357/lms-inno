package com.awesprojects.lmsclient.api.internal;

import com.awesprojects.lmsclient.api.Response;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ResponseType {

    Class<? extends Responsable>[] returns() default {Responsable.class};

}
