package View;

import Controller.Controller;

import java.util.regex.Matcher;

public class MainMenu {
    private final Controller controller;

    public MainMenu(Controller controller) {
        this.controller = controller;
    }

    public String run() {
        String command;
        Matcher matcher;
        while (true) {
            command = Menu.getScanner().nextLine();
            if (command.matches("^profile menu$")) {
                System.out.println("Entered profile menu!");
                return "profile menu";
            } else if ((matcher = Menu.getMatcher(command, "^start game turns count (?<count>\\d+) username (?<username>.+)$")) != null) {
                controller.startNewGame(matcher.group("username"), matcher.group("count"));
            } else if (command.matches("^shop menu$")) {
                System.out.println("Entered shop menu!");
                return "shop menu";
            } else if (command.matches("^logout$")) {
                controller.logout();
                return "logout";
            } else if (command.matches("^show current menu$")) {
                System.out.println("Main Menu");
            } else if (command.matches("^list of users$")) {
                controller.printUsers();
            } else if (command.matches("^scoreboard$")) {
                controller.printScoreboard();
            } else System.out.println("Invalid command!");
        }
    }
}