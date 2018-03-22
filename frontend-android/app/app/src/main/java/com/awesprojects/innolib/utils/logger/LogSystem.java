package com.awesprojects.innolib.utils.logger;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

/**
 * Created by ilya on 3/5/18.
 */

public class LogSystem{

    public static PrintStream ui;
    public static PrintStream out;
    public static PrintStream err;

    private static LogOutputStream outputStream;
    private static VoidOutputStream voidOutputStream;

    private static ArrayList<LineReceiver> lineReceivers;

    static{
        lineReceivers = new ArrayList<>(16);
        outputStream = new LogOutputStream("[OUT]:");
        voidOutputStream = new VoidOutputStream();
        ui = new PrintStream(voidOutputStream);
        out = new PrintStream(outputStream);
        err = new PrintStream(voidOutputStream);
        outputStream.setLineReceiver((line) -> {
            onOutLineReceived(line);
        });
        System.setErr(err);
        System.setOut(out);
    }

    public static void ensureInit(){
        String str = " this method calls static initialization";
    }

    public interface LineReceiver{
        void onLineReceived(String line);
    }

    public static void onOutLineReceived(String line){
        for (LineReceiver l : lineReceivers){
            try{
                l.onLineReceived(line);
            }catch(Throwable t){}
        }
    }

    public static void attachLineReceiver(LineReceiver lineReceiver){
        lineReceivers.add(lineReceiver);
    }

    public static void detachLineReceiver(LineReceiver lineReceiver){
        lineReceivers.remove(lineReceiver);
    }

    public static class VoidOutputStream extends OutputStream{
        @Override
        public void write(int i) throws IOException {

        }
    }

    public static class LogOutputStream extends OutputStream{

        final String mPrefix;
        final StringBuilder stringBuilder;
        LineReceiver lineReceiver;

        public LogOutputStream(String prefix){
            this.mPrefix = prefix;
            stringBuilder = new StringBuilder(1024);
        }

        public void setLineReceiver(LineReceiver receiver){
            lineReceiver = receiver;
        }

        @Override
        public void write(int i) throws IOException {
            //if ((char)i!='\n')
                stringBuilder.append((char) i);
            if ((char)i=='\n'){
                String line = stringBuilder.toString();
                if (lineReceiver!=null)
                    lineReceiver.onLineReceived(line);
                stringBuilder.delete(0,stringBuilder.length()-1);
            }
        }


    }




}
