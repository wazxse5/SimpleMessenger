package wazxse5.server;

import javafx.concurrent.Task;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Connection extends Task {
    private Socket socket;
    private Scanner input;
    private PrintWriter output;

    public Connection(Socket socket) {
        this.socket = socket;
        try {
            this.input = new Scanner(socket.getInputStream());
            this.output = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override protected Object call() throws Exception {
        return null;
    }

    public String getInputLine() {
        return input.nextLine();
    }

    public void send(String from, String message) {
        output.println(from + "_" + message);
    }

    public void sendServer(String message) {
        output.println("_serv_" + message);
    }

    public void close() throws IOException {
        socket.close();
    }

    public PrintWriter getOutput() {
        return output;
    }
}
