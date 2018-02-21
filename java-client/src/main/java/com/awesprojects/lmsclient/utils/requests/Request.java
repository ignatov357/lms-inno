package com.awesprojects.lmsclient.utils.requests;

import lombok.Getter;

import java.util.LinkedList;
import java.util.List;

public abstract class Request {

    private List<String[]> headers = new LinkedList<>();
    @Getter
    private String url;

    Request(){

    }

    public boolean hasHeaders(){
        return headers.size() > 0;
    }

    public void compileHeaders(StringBuilder sb){
        for (String[] pair : headers){
            sb.append(pair[0]).append(": ").append(pair[1]).append("\n");
        }
    }

    public void url(String url){
        this.url = url;
    }

    public void header(String key,String value){
        headers.add(new String[]{key,value});
    }

    public abstract String buildRequestAndGetResponse(StringBuilder sb);

    public final String execute(){
        StringBuilder sb = new StringBuilder();
        return buildRequestAndGetResponse(sb);
    }


    protected abstract static class AbstractBuilder<T extends AbstractBuilder,R extends Request>{

        private R ret;
        private T builder;

        protected void returns(T builder,R ret){
            this.ret = ret;
            this.builder = builder;
        }

        public T withURL(String url){
            ret.url(url);
            return builder;
        }

        protected void withHeaderInternal(String name,String value){
            ret.header(name,value);
        }

        public abstract T withHeader(String key,String value);

        public final R create(){
            return ret;
        }
    }


}
