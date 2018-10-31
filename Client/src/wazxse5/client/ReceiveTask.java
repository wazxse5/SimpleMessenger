package wazxse5.client;

import javafx.concurrent.Task;

import java.io.InputStream;
import java.util.Scanner;

public class ReceiveTask extends Task {
    private Scanner input;

    public ReceiveTask(InputStream inputStream) {
        this.input = new Scanner(inputStream);
    }

    @Override protected Object call() throws Exception {
        while (!isCancelled()) {
            if (input.hasNextLine()) {
                updateMessage(input.nextLine());
            }
        }
        return null;
    }
}
