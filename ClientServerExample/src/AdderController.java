import java.util.Scanner;

public class AdderController {

    private AdderController () {}

    public static void main (String[] args) {

        System.out.println("What port number do you want to use?");
        int portNumber = new Scanner(System.in).nextInt();

        System.out.println("How many clients do you want to create?");
        int numClients = new Scanner(System.in).nextInt();

        // create and start server
        AdderServer server = AdderServer.getInstance(portNumber, numClients);
        server.start();

        // create and start clients
        AdderClient[] clients = new AdderClient[numClients];
        for (int i = 0; i < numClients; i++) {
            clients[i] = new AdderClient("localhost", portNumber);
            clients[i].start();
        }
    }
}
