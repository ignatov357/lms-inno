package com.awesprojects.lmsclient;

import com.awesprojects.lmsclient.interactive.Terminal;

public class Main {

    public static void main(String[] args){
        LMSClient client = new LMSClient();
        Terminal terminal = new Terminal(client);
        terminal.run();
    }

}
