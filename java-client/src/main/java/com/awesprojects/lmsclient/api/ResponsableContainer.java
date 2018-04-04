package com.awesprojects.lmsclient.api;

import com.awesprojects.lmsclient.api.internal.Responsable;

public class ResponsableContainer<A> implements Responsable {

    final A object;

    public ResponsableContainer(A obj) {
        object = obj;
    }

    public A get() {
        return object;
    }

}
