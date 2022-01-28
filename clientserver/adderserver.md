---
title: adderserver.java
---

[back](https://valentin-carl.github.io/JavaExamples/clientserver)

~~~java

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Stack;

public class AdderServer extends Thread {

    int portNumber;
    int numClients;
    HashMap<Integer, Socket> clients;
    private static AdderServer singleton;

    private AdderServer (int portNumber, int numClients) {
        this.portNumber = portNumber;
        this.numClients = numClients;
        this.clients = new HashMap<>();
    }

    static public AdderServer getInstance (int portNumber, int numClients) {
        if (singleton == null) singleton = new AdderServer(portNumber, numClients);
        return singleton;
    }

    @Override
    public String toString () {
        return "adderServer";
    }

    @Override
    public void run () {

        try {
            // get connections
            ServerSocket s = new ServerSocket(this.portNumber);
            for (int i = 0; i < this.numClients; i++) {
                this.clients.put(i, s.accept());
                System.out.println(this + " connected to " + this.clients.get(i));
            }

            // read ints
            Stack<Integer> integerStack = new Stack<>();
            HashMap<Integer, BufferedReader> inputs = new HashMap<>();
            for (int i = 0; i < this.numClients; i++) {
                inputs.put(i, new BufferedReader(new InputStreamReader(this.clients.get(i).getInputStream())));
                System.out.println(this + " received input stream " + inputs.get(i));
                String in = inputs.get(i).readLine();
                integerStack.push(Integer.parseInt(in));
                System.out.println(this + " received " + integerStack.get(i));
            }

            // get stack sum
            int sum = integerStack.stream().reduce(0, Integer::sum);

            // send sum to all clients
            HashMap<Integer, BufferedWriter> outputs = new HashMap<>();
            for (int i = 0; i < this.numClients; i++) {
                outputs.put(i, new BufferedWriter(new OutputStreamWriter(this.clients.get(i).getOutputStream())));
                System.out.println(this + " sent " + sum + " to " + this.clients.get(i));
                outputs.get(i).write(String.valueOf(sum));
                outputs.get(i).newLine();
                outputs.get(i).flush();
            }

            // close connections
            for (int i = 0; i < this.numClients; i++) {
                inputs.get(i).close();
                this.clients.get(i).close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // signals end of server thread
        System.out.println(this + " finished.");
    }
}
~~~
