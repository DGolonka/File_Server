package client;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class MenuClient {
    private final DataOutputStream out;
    private final DataInputStream input;
    private final Scanner scanner = new Scanner(System.in);

    public MenuClient(DataOutputStream out, DataInputStream input) {
        this.out = out;
        this.input = input;
    }

    public void createAFile() {
        try {
            System.out.println("Enter name of the file:");
            String nameDataForTransfer = scanner.nextLine();
            System.out.println("Enter name of the file to be saved on server:");
            String nameDataToSave = scanner.nextLine();
            Path path = Path.of(nameDataForTransfer);
            if ("".equals(nameDataToSave)) {
               out.writeUTF("." + nameDataForTransfer.split("\\.")[1]);
            } else {
                out.writeUTF(nameDataToSave);
            }
            byte[] message = fileToByte(path);
            out.writeInt(message.length);
            out.write(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("The request was sent." );
        try {
            String results = input.readUTF();
            if (results.equals("200")) {
                System.out.println("Response says that file is saved! ID = " + input.readInt());
            } else {
                System.out.println("The response says that creating the file was forbidden!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void getAFile() {
        choseOptions();
        System.out.println("The request was sent.");
        try {
            int results = (input.readInt());
            if (results == 404) {
                System.out.println("The response says that this file is not found!");
            } else {
                int length = input.readInt();
                byte[] message = new byte[length];
                input.readFully(message, 0, message.length);
                System.out.println("The file was downloaded! Specify a name for it:");
                String name = scanner.nextLine();
                Path path = Path.of( name);
                Files.write(path, message);
                System.out.println("File saved on the hard drive!");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteAFile() {
        choseOptions();
        System.out.println("The request was sent." );
        try {
            int results = input.readInt();
            if (results == 200) {
                System.out.println("The response says that the file was successfully deleted!");
            } else {
                System.out.println("The response says that the file was not found!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getNameFile() {
        System.out.println("Enter filename: ");
        return scanner.nextLine();
    }

    private byte[] fileToByte(Path path) {
        byte[] file = null;
        try {
            file = Files.readAllBytes(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
    private void choseOptions() {
        try {
            System.out.println("Do you want to get the file by name or by id (1 - name, 2 - id): ");
            int choseOption = Integer.parseInt(scanner.nextLine());
            out.writeInt(choseOption);
            switch (choseOption) {
                case 2:
                    System.out.println("Enter id:");
                    int id = Integer.parseInt(scanner.nextLine());
                    out.writeInt(id);
                    break;
                case 1:
                    System.out.println("Enter name:");
                    String nameFile = scanner.nextLine();
                    out.writeUTF(nameFile);
                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
