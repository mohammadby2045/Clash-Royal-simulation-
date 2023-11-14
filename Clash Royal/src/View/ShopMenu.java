package View;

import Controller.Controller;

import java.util.regex.Matcher;

public class ShopMenu {
    private final Controller controller;

    public ShopMenu(Controller controller) {
        this.controller = controller;
    }

    public void run() {
        Matcher matcher;
        String command;

        while (true) {
            command = Menu.getScanner().nextLine();

            if (command.matches("^back$")) {
                System.out.println("Entered main menu!");
                return;
            } else if ((matcher = Menu.getMatcher(command, "^buy card (?<name>.+)$")) != null) {
                System.out.println(controller.buyCard(matcher.group("name")));
            } else if ((matcher = Menu.getMatcher(command, "^sell card (?<name>.+)$")) != null) {
                System.out.println(controller.sellCard(matcher.group("name")));
            } else if (command.matches("^show current menu$")) {
                System.out.println("Shop Menu");
            } else {
                System.out.println("Invalid command!");
            }
        }
    }
}