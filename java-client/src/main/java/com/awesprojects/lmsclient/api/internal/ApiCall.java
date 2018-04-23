package com.awesprojects.lmsclient.api.internal;

/*
 * methods marked this annotation do synchronous calls of server api
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiCall{

    String path() default "unspecified";

    Method method() default Method.GET;

    int port() default 80;

}
