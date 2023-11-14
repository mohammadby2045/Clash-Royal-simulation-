package View;

import Controller.Controller;

import java.util.regex.Matcher;

public class LoginMenu {
    private final Controller controller;

    public LoginMenu(Controller controller) {
        this.controller = controller;
    }

    public String run() {
        Matcher matcher;
        String command, result;

        while (true) {
            command = Menu.getScanner().nextLine();

            if (command.matches("^Exit$")) {
                return "Exit";
            } else if ((matcher = Menu.getMatcher(command, "^register username (?<username>.+) password (?<password>.+)$")) != null) {
                System.out.println(controller.register(matcher.group("username"), matcher.group("password")));
            } else if ((matcher = Menu.getMatcher(command, "^login username (?<username>.+) password (?<password>.+)$")) != null) {
                result = controller.login(matcher.group("username"), matcher.group("password"));
                System.out.println(result);
                if (result.matches("User (?<username>\\S+) logged in!"))
                    return "Logged in";
            } else if (command.matches("^show current menu$")) {
                System.out.println("Register/Login Menu");
            } else {
                System.out.println("Invalid command!");
            }
        }
    }
}