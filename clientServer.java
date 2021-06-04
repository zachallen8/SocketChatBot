import java.io.*;
import java.net.*;
import java.util.Scanner;

//class for the client/server app
public class clientServer {
    // nested client class
    public static class client {
        private Socket socket = null;
        private DataInputStream input = null;
        private DataOutputStream output = null;
        private Scanner scanner = new Scanner(System.in);
        private String delim = "";

        // constructor with ip and port
        public client(String ip, int port) {
            // try to establish connection
            try {
                socket = new Socket(ip, port);
                input = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                output = new DataOutputStream(socket.getOutputStream());
                System.out.println("Connection to server established. Type End to end the chat.");
            } catch (UnknownHostException u) {
                System.out.println(u);
            } catch (IOException i) {
                System.out.println(i);
            }

            // reads until delimiter "End" is input
            while (!delim.equals("End")) {
                try {
                    delim = scanner.nextLine();
                    output.writeUTF(delim);
                    delim = input.readUTF();
                    System.out.println("Server Client---> " + delim);
                } catch (IOException i) {
                    System.out.println(i);
                }
            }

            // closes the connection
            try {
                input.close();
                output.close();
                socket.close();
            } catch (IOException i) {
                System.out.println(i);
            }
        }
    }

    // nested server class
    public static class server {
        private Socket socket = null;
        private ServerSocket server = null;
        private DataInputStream input = null;
        private DataOutputStream output = null;
        private Scanner scanner = new Scanner(System.in);
        private String delim = "";

        // constructor with port
        public server(int port) {
            // starts server
            try {
                server = new ServerSocket(port);
                System.out.println("Server started, waiting for connection.");

                // waits for client connection
                socket = server.accept();
                System.out.println("Connection with client established. Type End to end the chat.");

                // takes input from client and output back to client
                input = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                output = new DataOutputStream(socket.getOutputStream());
                // reads until delimiter "End is input"
                while (!delim.equals("End")) {
                    try {
                        delim = input.readUTF();
                        System.out.println("Client---> " + delim);
                        if(delim.equals("End"))
                            break;
                        delim = scanner.nextLine();
                        output.writeUTF(delim);
                    } catch (IOException i) {
                        System.out.println(i);
                    }
                }

                // closes connection
                socket.close();
                input.close();
            } catch (IOException i) {
                System.out.println(i);
            }
        }
    }

    public static void main(String[] args) {
        String ip = "127.0.0.1";
        int port = 6900;
        Scanner scanner = new Scanner(System.in);
        int choice;
        server newServer;
        client newClient;
        System.out.println("Welcome to Zach's Client-Server chat application! Type 1 if you are the server or 2 if you are the client.");
        choice = scanner.nextInt();
        if(choice == 1)
            newServer = new server(port);
        else if(choice == 2)
            newClient = new client(ip, port);
    }
}