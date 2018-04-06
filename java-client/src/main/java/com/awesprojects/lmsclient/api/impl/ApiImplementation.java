package com.awesprojects.lmsclient.api.impl;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ApiImplementation {
    Class<?> api();
}
