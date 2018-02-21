package com.awesprojects.lmsclient.interactive;

import com.awesprojects.lmsclient.LMSClient;

import java.util.Scanner;

public class Terminal {

    private final LMSClient client;
    private final Commands reflector;
    private final Scanner scanner;
    private final CommandUtils commandUtils;

    public Terminal(LMSClient client){
        this.client = client;
        reflector = new Commands();
        scanner = new Scanner(System.in);
        commandUtils = new CommandUtils(client);
    }

    public void run(){
        boolean loggedIn = false;
        for (int i = 0; i < 3 && !loggedIn; i++) {
            loggedIn |= commandUtils.login(scanner);
        }
        if (!loggedIn){
            client.err.println("unable to login, exiting");
            System.exit(2);
        }
        client.out.print("# ");
        do{
            String line = scanner.nextLine();
            client.out.println("read : "+line);
            parse(line);
            client.out.print("# ");
        } while (scanner.hasNextLine());
    }

    public void parse(String line){
        switch (line){
            default: reflector.parseCommand(line);
        }
    }




}
