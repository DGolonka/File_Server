package client;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;


public class Client {
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 23456;

    public static void main(String[] args) {
        try (
                Socket socket = new Socket(InetAddress.getByName(SERVER_ADDRESS), SERVER_PORT);
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ) {
            boolean start = true;
            Scanner scanner = new Scanner(System.in);
            MenuClient menu = new MenuClient(output, input);
            String command = "";
                System.out.println("Enter action (1 - get a file, 2 - save a file, 3 - delete a file):");
                command = scanner.nextLine();
                switch (command) {
                    case "1":
                        output.writeUTF("get");
                        menu.getAFile();
                        break;
                    case "2":
                        output.writeUTF("put");
                        menu.createAFile();
                        break;
                    case "3":
                        output.writeUTF("delete");
                        menu.deleteAFile();
                        break;
                    case "exit":
                        output.writeUTF("exit");
                        start = false;
                        break;


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

