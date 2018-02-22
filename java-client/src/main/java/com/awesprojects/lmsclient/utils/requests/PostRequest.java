package com.awesprojects.lmsclient.utils.requests;

import com.awesprojects.lmsclient.utils.Config;
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

    public int computeDataLength(){
        int length = 0;
        Iterator<String[]> iterator = formData.iterator();
        while (iterator.hasNext()){
            String[] pair = iterator.next();
            length+= pair[0].length();
            length+= pair[1].length();
            length+= 1;
            if (iterator.hasNext())
                length+= 1;
        }
        return length;
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

    public String buildRequest(StringBuilder sb){
        sb.append("POST ").append(getUrl());
        sb.append(" HTTP/1.1").append("\n");
        sb.append("Host: ").append(Config.getCurrentConfig().getApiDomain()).append("\n");
        if (hasData()) {
            sb.append("Content-Type: application/x-www-form-urlencoded").append("\n");
            sb.append("Content-Length: ").append(computeDataLength()).append("\n");
        }
        sb.append("Connection: close\n");
        if (hasHeaders())
            compileHeaders(sb);
        sb.append("\n");
        if (hasData())
            compileData(sb);
        return sb.toString();
    }
    @Override
    public String buildRequestAndGetResponse(StringBuilder sb) {
        String requestStr = buildRequest(sb);
        String response = RequestExecutor.executeRequest(requestStr);
        return response;
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
