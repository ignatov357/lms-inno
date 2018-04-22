package com.awesprojects.lmsclient.utils.requests;

import com.awesprojects.lmsclient.utils.Config;
import lombok.Getter;

import java.net.Socket;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class GetRequest extends Request{

    @Getter
    private List<String[]> query = new LinkedList<>();

    GetRequest(){
        super();
    }


    public boolean hasQuery(){
        return query.size() > 0;
    }

    public void compileQuery(StringBuilder sb){
        Iterator<String[]> iterator = query.iterator();
        while (iterator.hasNext()){
            String[] pair = iterator.next();
            sb.append(pair[0]+"="+pair[1]);
            if (iterator.hasNext())
                sb.append("&");
        }
    }

    public void query(String key,String value){
        try {
            key = URLEncoder.encode(key, "UTF-16");
            value = URLEncoder.encode(value,"UTF-16");
        }catch (Throwable t){}
        query.add(new String[]{key,value});
    }

    protected String buildRequest(StringBuilder sb){
        sb.append("GET ").append(getUrl());
        if (hasQuery()) {
            sb.append("?");
            compileQuery(sb);
        }
        sb.append(" HTTP/1.1").append(LINE_END);
        sb.append("Host: ").append(Config.getCurrentConfig().getApiDomain()).append(LINE_END);
        if (isCloseConnection())
            sb.append("Connection: close").append(LINE_END);
        if (hasHeaders())
            compileHeaders(sb);
        sb.append(LINE_END);
        return sb.toString();
    }

    @Override
    public String buildRequestAndGetResponse(StringBuilder sb) {
        String requestStr = buildRequest(sb);
        String response = RequestExecutor.executeRequest(requestStr);
        return response;
    }



    public static class Builder extends AbstractBuilder<Builder,GetRequest> {

        private GetRequest request;

        Builder(){
            super();
            request = new GetRequest();
            returns(this,request);
        }

        public Builder withQuery(String name,String value){
            request.query(name,value);
            return this;
        }

        @Override
        public Builder withHeader(String key, String value) {
            withHeaderInternal(key,value);
            return this;
        }
    }

}
