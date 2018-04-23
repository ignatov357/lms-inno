package com.awesprojects.lmsclient.utils.requests;

import com.awesprojects.lmsclient.utils.Config;
import lombok.Getter;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
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

    public void data(String key,String value){
        try{
            key = URLEncoder.encode(key,"UTF-8");
            value = URLEncoder.encode(value, "UTF-8");
        }catch(UnsupportedEncodingException use){
            if (Config.getCurrentConfig().isDebug());
                use.printStackTrace();
        }
        formData.add(new String[]{key,value});
    }

    public String buildRequest(StringBuilder sb){
        sb.append("POST ").append(getUrl());
        if (!getUrl().endsWith("/"))
            sb.append("/");
        sb.append(" HTTP/1.1").append(LINE_END);
        sb.append("Host: ").append(Config.getCurrentConfig().getApiDomain()).append(LINE_END);
        sb.append("Accept: application/json, text/javascript, */* ").append(LINE_END);
        sb.append("Accept-Encoding: deflate ").append(LINE_END);
        if (hasData()) {
            sb.append("Content-Type: application/x-www-form-urlencoded; charset=UTF-8").append(LINE_END);
            sb.append("Content-Length: ").append(computeDataLength()).append(LINE_END);
        }
        if (isCloseConnection())
            sb.append("Connection: close").append(LINE_END);
        if (hasHeaders())
            compileHeaders(sb);
        sb.append(LINE_END);
        if (hasData())
            compileData(sb);
        return sb.toString();
    }

    @Override
    public String buildRequestAndGetResponse(StringBuilder sb) {
        String requestStr = buildRequest(sb);
        //requestStr = new String(Charset.forName("UTF-8").encode(requestStr).array());
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
