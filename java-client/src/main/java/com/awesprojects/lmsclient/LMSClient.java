package com.awesprojects.lmsclient;

import com.awesprojects.lmsclient.utils.Config;

import java.io.PrintStream;
import java.util.logging.Logger;

public class LMSClient {

    public static final String TAG = "RequestExecutor";
    public static Logger log = Logger.getLogger(TAG);

    public final PrintStream out;
    public final PrintStream err;

    public LMSClient(){
        this(Config.getDefaultConfig());
    }

    public LMSClient(Config config){
        out = config.getOut();
        err = config.getErr();
        log.info("lms client initialized");
    }

    public void destroy(){
        out.close();
        err.close();
    }

}
