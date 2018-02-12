package com.awesprojects.lmsclient;

import com.awesprojects.lmsclient.utils.Config;

import java.io.PrintStream;

public class LMSClient {

    public final PrintStream out;
    public final PrintStream err;

    public LMSClient(){
        this(Config.getDefaultConfig());
    }

    public LMSClient(Config config){
        out = config.getOut();
        err = config.getErr();
    }

    public void destroy(){
        out.close();
        err.close();
    }

}
