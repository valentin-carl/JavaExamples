import java.io.*;
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
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                ) {
            // confirm connection
            System.out.println(this + " connected to " + socket);

            // send number
            bw.write(String.valueOf(num));
            bw.newLine();
            bw.flush();
            System.out.println(this + " sent " + num);

            // wait for input
            String sum = br.readLine();
            System.out.println(this + " got " + sum + " as result.");

        } catch (IOException e) {
            e.printStackTrace();
        }

        // signals end of client thread
        System.out.println(this + " finished");
    }
}
