package server;

import java.io.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class MenuServer {
    private FileWriter fileWriter;
    private static  List<String> listFile;
    private Scanner fileReader;
    private File file;
    private final DataOutputStream out;
    private final DataInputStream input;
    private final Scanner scanner = new Scanner(System.in);

    static {
        try {
            listFile = (List<String>) SerializationUtils.deserialize("server\\cos.data");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public MenuServer(DataOutputStream out, DataInputStream input) {
        this.out = out;
        this.input = input;
    }

    public void createAFile() {
        try {

            String nameFile = input.readUTF();
            if (nameFile.matches("^\\.\\w*")) {
                nameFile = "file" + listFile.size() + nameFile;
            }
            String pathToFile = "data\\" + nameFile;
            Path path = Path.of(pathToFile);
            int length = input.readInt();
            byte[] message = new byte[length];
            input.readFully(message, 0, message.length);
            Files.write(path, message);
            listFile.add(pathToFile);
            out.writeUTF("200");
            out.writeInt(listFile.size());
            SerializationUtils.serialize(listFile, "file.data");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void getAFile() {
        try {
            String path = getFile();
            if (path != null) {
                Path path1 = Path.of(path);
                byte[] bytesFile = Files.readAllBytes(path1);
                out.writeInt(200);
                out.writeInt(bytesFile.length);
                out.write(bytesFile);
            } else {
                out.writeInt(404);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteAFile() {
        try {
            Path path = Path.of(getFile());
            file = path.toFile();
            if (file.exists()) {
                file.delete();
                out.writeInt(200);
            } else {
                out.writeInt(404);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private String getFile() {
        try {
            int choseOption = input.readInt();
            if (choseOption == 2) {
                int id = input.readInt();
                if (id <= listFile.size() && id >= 1) {
                    return listFile.get(id - 1);
                }
            } else {
                String nameFile = input.readUTF();
                String fullPath = "data\\" + nameFile;

                file = new File(fullPath);
                if (file.exists()) {
                    return fullPath;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
