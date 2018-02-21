package com.awesprojects.lmsclient.utils.requests;

import lombok.Getter;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class PostRequest extends Request{

    @Getter
    private List<String[]> formData = new LinkedList<>();

    PostRequest(){
        super();
    }



    public boolean hasData(){
        return formData.size() > 0;
    }

    public void compileData(StringBuilder sb){
        Iterator<String[]> iterator = formData.iterator();
        while (iterator.hasNext()){
            String[] pair = iterator.next();
            sb.append(pair[0]+"="+pair[1]);
            if (iterator.hasNext())
                sb.append("&");
        }
    }

    public void data(String name,String value){
        formData.add(new String[]{name,value});
    }

    @Override
    public String buildRequestAndGetResponse(StringBuilder sb) {
       // sb.append("GET ").append()
        return null;
    }




    public static class Builder extends AbstractBuilder<Builder,PostRequest> {

        private PostRequest request;

        Builder(){
            super();
            request = new PostRequest();
            returns(this,request);
        }

        public Builder withData(String name, String value){
            request.data(name,value);
            return this;
        }

        @Override
        public Builder withHeader(String key, String value) {
            withHeaderInternal(key,value);
            return this;
        }


    }
}
