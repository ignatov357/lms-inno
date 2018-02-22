package com.awesprojects.lmsclient.utils;

import lombok.Getter;
import lombok.Setter;

import java.io.PrintStream;

public class Config {

    @Getter @Setter
    private PrintStream out;
    @Getter @Setter
    private PrintStream err;
    @Getter @Setter
    private String apiDomain;
    @Getter @Setter
    private boolean secure;
    @Getter @Setter
    private boolean debug;
    @Getter @Setter
    private boolean verbose;
    @Setter
    private static Config currentConfig;


    public static Config getCurrentConfig() {
        if (currentConfig==null)
            currentConfig = getDefaultConfig();
        return currentConfig;
    }

    public static Config getDefaultConfig(){
        Config config = new Config();
        config.debug = true;
        config.verbose = true;
        config.err = System.err;
        config.out = System.out;
        config.apiDomain = "api.awes-projects.com";
        return config;
    }

}
