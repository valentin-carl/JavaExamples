import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class AdderClient extends Thread {

    static int nextID = 0;
    int id = nextID++;
    String hostName;
    int portNumber;

    public AdderClient (String hostName, int portNumber) {
        this.hostName = hostName;
        this.portNumber = portNumber;
    }

    @Override
    public String toString () {
        return "adderClient" + this.id;
    }

    public void run () {

        // get inputs
        System.out.println(this + " is asking for a number");
        int num = new Scanner(System.in).nextInt();

        try (
                // connect to server
                Socket socket = new Socket(this.hostName, this.portNumber);
                OutputStream output = socket.getOutputStream();
                InputStream inputStream = socket.getInputStream();
                ) {
            // confirm connection
            System.out.println(this + " connected to " + socket);

            // send number
            output.write(num);
            System.out.println(this + " sent " + num);

            // wait for input
            int sum = inputStream.read();
            System.out.println(this + " got " + sum + " as result.");

        } catch (IOException e) {
            e.printStackTrace();
        }

        // signals end of client thread
        System.out.println(this + " finished");
    }
}
