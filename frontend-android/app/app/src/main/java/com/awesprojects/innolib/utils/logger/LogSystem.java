package com.awesprojects.innolib.utils.logger;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * Created by ilya on 3/5/18.
 */

public class LogSystem {

    public static final String TAG = "LogSystem";
    private static Logger log = null;

    static {
        try {
            LogManager.getLogManager().readConfiguration(new ByteArrayInputStream(new String(
                    "handlers = com.awesprojects.innolib.utils.logger.LogHandler\n" +
                            ".level = ALL \0"
            ).getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        log = Logger.getLogger(TAG);
        log.config("log system configured");
    }

    public static void ensureInit() {
        String str = " this method calls static initialization";
    }

}
