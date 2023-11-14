package View;

import Controller.Controller;

import java.util.regex.Matcher;

public class ProfileMenu {
    private final Controller controller;

    public ProfileMenu(Controller controller) {
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
            } else if ((matcher = Menu.getMatcher(command, "^change password old password (?<oldPass>.+) new password (?<newPass>.+)$")) != null) {
                System.out.println(controller.changePassword(matcher.group("oldPass"), matcher.group("newPass")));
            } else if ((matcher = Menu.getMatcher(command, "^remove from battle deck (?<name>.+)$")) != null) {
                System.out.println(controller.removeFromBattleDeck(matcher.group("name")));
            } else if ((matcher = Menu.getMatcher(command, "^add to battle deck (?<name>.+)$")) != null) {
                System.out.println(controller.addToBattleDeck(matcher.group("name")));
            } else if (command.matches("^show battle deck$")) {
                controller.showBattleDeck();
            } else if (command.matches("^show current menu$")) {
                System.out.println("Profile Menu");
            } else if (command.matches("^Info$")) {
                controller.userInfo();
            } else {
                System.out.println("Invalid command!");
            }
        }
    }
}