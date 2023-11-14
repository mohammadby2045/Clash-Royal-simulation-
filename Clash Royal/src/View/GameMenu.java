package View;

import Controller.Controller;

import java.util.regex.Matcher;

public class GameMenu {
    private final Controller controller;

    public GameMenu(Controller controller) {
        this.controller = controller;
    }

    public void run() {
        Matcher matcher;
        String command, result;
        while (true) {
            command = Menu.getScanner().nextLine();

            if (command.matches("^show the hitpoints left of my opponent$")) {
                controller.showHitpoint();
            } else if ((matcher = Menu.getMatcher(command, "^show line info (?<dir>\\S+)$")) != null) {
                controller.showLineInfo(matcher.group("dir"));
            } else if (command.matches("^number of cards to play$")) {
                controller.numberOfCardsToPlay();
            } else if (command.matches("^number of moves left$")) {
                controller.numberOfMovesLeft();
            } else if ((matcher = Menu.getMatcher(command, "^deploy troop (?<name>.+) in line (?<dir>\\S+) and row (?<number>-?\\d+)$")) != null) {
                System.out.println(controller.deployTroop(matcher.group("name"), matcher.group("dir"), matcher.group("number")));
            } else if ((matcher = Menu.getMatcher(command, "^move troop in line (?<lineDir>\\S+) and row (?<row>-?\\d+) (?<dir>\\S+)$")) != null) {
                System.out.println(controller.moveTroop(matcher.group("lineDir"), matcher.group("dir"), matcher.group("row")));
            } else if ((matcher = Menu.getMatcher(command, "^deploy spell Heal in line (?<lineDir>\\S+) and row (?<row>-?\\d+)$")) != null) {
                System.out.println(controller.deployHeal(matcher.group("lineDir"), matcher.group("row")));
            } else if ((matcher = Menu.getMatcher(command, "^deploy spell Fireball in line (?<lineDir>\\S+)$")) != null) {
                System.out.println(controller.deployFireball(matcher.group("lineDir")));
            } else if (command.matches("^next turn$")) {
                result = controller.nextTurn();
                if(result.equals("endgame"))
                    return;
            } else if (command.matches("^show current menu$")) {
                System.out.println("Game Menu");
            } else {
                System.out.println("Invalid command!");
            }
        }
    }
}