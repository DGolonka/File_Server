package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Server  {
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 23456;
    private static ServerSocket server;


    public static void main(String[] args) throws IOException {
        boolean start = true;
        System.out.println("Server started!");
        try (ServerSocket server = new ServerSocket(SERVER_PORT, 50, InetAddress.getByName(SERVER_ADDRESS))) {
            while (start) {
            try (
                        Socket socket = server.accept();
                        DataInputStream input = new DataInputStream(socket.getInputStream());
                        DataOutputStream output = new DataOutputStream(socket.getOutputStream())
                ) {
                    MenuServer menu = new MenuServer(output, input);
                        switch (input.readUTF()) {
                            case "get":
                                menu.getAFile();
                                break;
                            case "put":
                                menu.createAFile();
                                break;
                            case "delete":
                                menu.deleteAFile();
                                break;
                            case "exit":
                                start = false;
                                break;
                        }


                } catch (IOException e) {
                    e.printStackTrace();
            }

            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

