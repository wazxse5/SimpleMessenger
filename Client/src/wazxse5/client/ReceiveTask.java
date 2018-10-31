package wazxse5.client;

import javafx.concurrent.Task;

import java.util.Scanner;

public class ReceiveTask extends Task {
    private Scanner input;

    public ReceiveTask(Scanner input) {
        this.input = input;
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
