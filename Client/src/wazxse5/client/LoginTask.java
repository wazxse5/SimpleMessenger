package wazxse5.client;

import java.util.Scanner;
import java.util.concurrent.Callable;

public class LoginTask implements Callable<Boolean> {
    private Scanner input;

    public LoginTask(Scanner input) {
        this.input = input;
    }

    @Override public Boolean call() throws Exception {
        while (true) {
            if (input.hasNextLine()) {
                String answer = input.nextLine().substring(6);
                if (answer.equals("loginok")) {
                    return true;
                }
            }
            if (Thread.interrupted()) {
                return false;
            }
        }
    }
}
