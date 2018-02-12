package com.awesprojects.lmsclient.utils;

import com.awesprojects.lmsclient.utils.requests.GetRequest;
import com.awesprojects.lmsclient.utils.requests.PostRequest;
import com.awesprojects.lmsclient.utils.requests.Request;
import lombok.Getter;

import java.util.LinkedList;
import java.util.List;

public class RequestFactory {

    public static GetRequestBuilder get(){
        return new GetRequestBuilder();
    }

    public static PostRequestBuilder post(){
        return new PostRequestBuilder();
    }

    public static class GetRequestBuilder extends RequestBuilder<GetRequestBuilder,GetRequest>{

        private GetRequest request;

        public GetRequestBuilder(){
            super();
            request = new GetRequest();
            returns(request);
        }
        @Getter
        private List<String[]> formData = new LinkedList<>();
        public GetRequestBuilder withData(String name,String value){
            formData.add(new String[]{name,value});
            return this;
        }

        @Override
        public GetRequestBuilder withHeader(String key, String value) {
            withHeaderInternal(key,value);
            return this;
        }
    }

    public static class PostRequestBuilder extends RequestBuilder<PostRequestBuilder,PostRequest>{

        private PostRequest request;

        public PostRequestBuilder(){
            super();
            request = new PostRequest();
            returns(request);
        }

        @Getter
        private List<String[]> query = new LinkedList<>();
        public PostRequestBuilder withQuery(String name,String value){
            query.add(new String[]{name,value});
            return this;
        }

        @Override
        public PostRequestBuilder withHeader(String key, String value) {
            withHeaderInternal(key,value);
            return this;
        }
    }

    protected abstract static class RequestBuilder<T extends RequestBuilder,R extends Request>{

        private R ret;

        protected void returns(R ret){
            this.ret = ret;
        }

        private List<String[]> headers = new LinkedList<>();

        protected void withHeaderInternal(String name,String value){
            headers.add(new String[]{name,value});
        }

        public abstract T withHeader(String key,String value);

        public final R create(){
            return ret;
        }
    }

}
