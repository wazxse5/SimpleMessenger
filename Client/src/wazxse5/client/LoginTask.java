package wazxse5.client;

import wazxse5.client.exception.NameIsInUseException;
import wazxse5.client.exception.NoSuchUserException;
import wazxse5.client.exception.WrongPasswordException;

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
                if (answer.equals("login_ok")) return true;
                else if (answer.equals("login_no_such_user")) throw new NoSuchUserException();
                else if (answer.equals("login_name_is_in_use")) throw new NameIsInUseException();
                else if (answer.equals("login_wrong_password")) throw new WrongPasswordException();
                else return false;
            }
            if (Thread.interrupted()) return false;
        }
    }
}
