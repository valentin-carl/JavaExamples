import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
            HashMap<Integer, InputStream> inputs = new HashMap<>();
            for (int i = 0; i < this.numClients; i++) {
                inputs.put(i, this.clients.get(i).getInputStream());
                System.out.println(this + " received input stream " + inputs.get(i));
                integerStack.push(inputs.get(i).read());
                System.out.println(this + " received " + integerStack.get(i));
            }

            // get stack sum
            int sum = integerStack.stream().reduce(0, Integer::sum);

            // send sum to all clients
            HashMap<Integer, OutputStream> outputs = new HashMap<>();
            for (int i = 0; i < this.numClients; i++) {
                outputs.put(i, this.clients.get(i).getOutputStream());
                System.out.println(this + " sent " + sum + " to " + this.clients.get(i));
                outputs.get(i).write(sum);
            }

            // close connections
            for (int i = 0; i < this.numClients; i++) {
                inputs.get(i).close();
                outputs.get(i).close();
                this.clients.get(i).close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // signals end of server thread
        System.out.println(this + " finished.");
    }
}
