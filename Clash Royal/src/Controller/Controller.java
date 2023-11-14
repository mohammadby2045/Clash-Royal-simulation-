package Controller;

import java.util.*;

import Model.*;
import View.*;

public class Controller {
    boolean isHostsTurn;
    private Game game;
    private User loggedInUser;
    private final LoginMenu loginMenu;
    private final ShopMenu shopMenu;
    private final ProfileMenu profileMenu;
    private final MainMenu mainMenu;
    private final GameMenu gameMenu;

    public Controller() {
        loginMenu = new LoginMenu(this);
        mainMenu = new MainMenu(this);
        shopMenu = new ShopMenu(this);
        profileMenu = new ProfileMenu(this);
        gameMenu = new GameMenu(this);
    }

    public void run() {
        if (loginMenu.run().equals("Exit")) return;
        while (true) {
            switch (mainMenu.run()) {
                case "profile menu":
                    profileMenu.run();
                    break;
                case "shop menu":
                    shopMenu.run();
                    break;
                case "logout":
                    if (loginMenu.run().equals("Exit")) return;
                    break;
            }
        }
    }

    public String register(String username, String password) {
        if (!username.matches("^[a-zA-Z]+$")) {
            return "Incorrect format for username!";
        } else if (Menu.problemInThePassword(password)) {
            return "Incorrect format for password!";
        } else if (User.getUserByUsername(username) != null) {
            return "Username already exists!";
        }
        User.addUser(username, password);
        return "User " + username + " created successfully!";
    }

    public String login(String username, String password) {
        if (!username.matches("^[a-zA-Z]+$")) {
            return "Incorrect format for username!";
        } else if (Menu.problemInThePassword(password)) {
            return "Incorrect format for password!";
        } else if (User.getUserByUsername(username) == null) {
            return "Username doesn't exist!";
        } else if (!User.getUserByUsername(username).isPasswordCorrect(password)) {
            return "Password is incorrect!";
        }
        loggedInUser = User.getUserByUsername(username);
        return "User " + username + " logged in!";
    }

    public void logout() {
        System.out.println("User " + loggedInUser.getUsername() + " logged out successfully!");
        loggedInUser = null;
    }

    public void startNewGame(String username, String count) {
        if (Integer.parseInt(count) < 5 || Integer.parseInt(count) > 30) {
            System.out.println("Invalid turns count!");
            return;
        } else if (!username.matches("^[a-zA-Z]+$")) {
            System.out.println("Incorrect format for username!");
            return;
        } else if (User.getUserByUsername(username) == null) {
            System.out.println("Username doesn't exist!");
            return;
        }
        isHostsTurn = true;
        int hostLevel = loggedInUser.getLevel();
        int opponentLevel = User.getUserByUsername(username).getLevel();
        int hostSideCastle = hostLevel * 2500;
        int hostMiddleCastle = hostLevel * 3600;
        int opponentSideCastle = opponentLevel * 2500;
        int opponentMiddleCastle = opponentLevel * 3600;
        game = new Game(Integer.parseInt(count), loggedInUser, User.getUserByUsername(username), hostSideCastle, hostSideCastle, hostMiddleCastle, opponentSideCastle, opponentSideCastle, opponentMiddleCastle);
        System.out.println("Battle started with user " + username);
        gameMenu.run();
    }

    public void printUsers() {
        for (int i = 0; i < User.getUsers().size(); i++) {
            System.out.println("user " + (i + 1) + ": " + User.getUsers().get(i).getUsername());
        }
    }

    public String changePassword(String oldPass, String newPass) {
        if (!loggedInUser.isPasswordCorrect(oldPass)) {
            return "Incorrect password!";
        } else if (Menu.problemInThePassword(newPass)) {
            return "Incorrect format for new password!";
        }
        loggedInUser.setPassword(newPass);
        return "Password changed successfully!";
    }

    public void userInfo() {
        System.out.println("username: " + loggedInUser.getUsername());
        System.out.println("password: " + loggedInUser.getPassword());
        System.out.println("level: " + loggedInUser.getLevel());
        System.out.println("experience: " + loggedInUser.getExperience());
        System.out.println("gold: " + loggedInUser.getGold());
        User[] users = new User[User.getUsers().size()];
        for (int i = 0; i < User.getUsers().size(); i++) {
            users[i] = User.getUsers().get(i);
        }
        for (int i = 0; i < users.length; i++) {
            for (int j = i + 1; j < users.length; j++) {
                if (users[i].getLevel() < users[j].getLevel()) {
                    User user = users[i];
                    users[i] = users[j];
                    users[j] = user;
                }
            }
        }
        for (int i = 0; i < users.length; i++) {
            for (int j = i + 1; j < users.length; j++) {
                if (users[i].getLevel() == users[j].getLevel() && users[i].getExperience() < users[j].getExperience()) {
                    User user = users[i];
                    users[i] = users[j];
                    users[j] = user;
                }
            }
        }
        for (int i = 0; i < users.length; i++) {
            for (int j = i + 1; j < users.length; j++) {
                int x = users[i].getUsername().compareTo(users[j].getUsername());
                if (users[i].getLevel() == users[j].getLevel() && users[i].getExperience() == users[j].getExperience() && x > 0) {
                    User user = users[i];
                    users[i] = users[j];
                    users[j] = user;
                }
            }
        }
        int rank = 0;
        for (int i = 0; i < users.length; i++) {
            if (users[i].getUsername().equals(loggedInUser.getUsername())) {
                rank = i + 1;
                break;
            }
        }
        System.out.println("rank: " + rank);
    }

    public String removeFromBattleDeck(String name) {
        if (!name.matches("^Fireball$") && !name.matches("^Heal$") && !name.matches("^Barbarian$") && !name.matches("^Baby Dragon$") && !name.matches("^Ice Wizard$")) {
            return "Invalid card name!";
        } else if (!loggedInUser.getBattleDeck().contains(name)) {
            return "This card isn't in your battle deck!";
        } else if (loggedInUser.getBattleDeck().size() == 1) {
            return "Invalid action: your battle deck will be empty!";
        }
        loggedInUser.getBattleDeck().remove(name);
        return "Card " + name + " removed successfully!";
    }

    public String addToBattleDeck(String name) {
        if (!name.matches("^Fireball$") && !name.matches("^Heal$") && !name.matches("^Barbarian$") && !name.matches("^Baby Dragon$") && !name.matches("^Ice Wizard$")) {
            return "Invalid card name!";
        } else if (!loggedInUser.getAllCards().contains(name)) {
            return "You don't have this card!";
        } else if (loggedInUser.getBattleDeck().contains(name)) {
            return "This card is already in your battle deck!";
        } else if (loggedInUser.getBattleDeck().size() == 4) {
            return "Invalid action: your battle deck is full!";
        }
        loggedInUser.addBattleDeck(name);
        return "Card " + name + " added successfully!";
    }

    public void showBattleDeck() {
        String[] Array = new String[loggedInUser.getBattleDeck().size()];
        Array = loggedInUser.getBattleDeck().toArray(Array);
        Arrays.sort(Array, String.CASE_INSENSITIVE_ORDER);
        for (int i = 0; i < Array.length; i++) {
            System.out.println(Array[i]);
        }
    }

    public String buyCard(String name) {
        if (!name.matches("^Fireball$") && !name.matches("^Heal$") && !name.matches("^Barbarian$") && !name.matches("^Baby Dragon$") && !name.matches("^Ice Wizard$")) {
            return "Invalid card name!";
        } else if (name.matches("^Fireball$")) {
            if (loggedInUser.getGold() < 100) {
                return "Not enough gold to buy " + name + "!";
            } else if (loggedInUser.getAllCards().contains(name)) {
                return "You have this card!";
            }
            int money = loggedInUser.getGold();
            loggedInUser.setGold(money - 100);
            loggedInUser.getAllCards().add(name);
            return "Card " + name + " bought successfully!";
        } else if (name.matches("^Heal$")) {
            if (loggedInUser.getGold() < 150) {
                return "Not enough gold to buy " + name + "!";
            } else if (loggedInUser.getAllCards().contains(name)) {
                return "You have this card!";
            }
            int money = loggedInUser.getGold();
            loggedInUser.setGold(money - 150);
            loggedInUser.getAllCards().add(name);
            return "Card " + name + " bought successfully!";
        } else if (name.matches("^Barbarian$")) {
            if (loggedInUser.getGold() < 100) {
                return "Not enough gold to buy " + name + "!";
            } else if (loggedInUser.getAllCards().contains(name)) {
                return "You have this card!";
            }
            int money = loggedInUser.getGold();
            loggedInUser.setGold(money - 100);
            loggedInUser.getAllCards().add(name);
            return "Card " + name + " bought successfully!";
        } else if (name.matches("^Baby Dragon$")) {
            if (loggedInUser.getGold() < 200) {
                return "Not enough gold to buy " + name + "!";
            } else if (loggedInUser.getAllCards().contains(name)) {
                return "You have this card!";
            }
            int money = loggedInUser.getGold();
            loggedInUser.setGold(money - 200);
            loggedInUser.getAllCards().add(name);
            return "Card " + name + " bought successfully!";
        } else {
            if (loggedInUser.getGold() < 180) {
                return "Not enough gold to buy " + name + "!";
            } else if (loggedInUser.getAllCards().contains(name)) {
                return "You have this card!";
            }
            int money = loggedInUser.getGold();
            loggedInUser.setGold(money - 180);
            loggedInUser.getAllCards().add(name);
            return "Card " + name + " bought successfully!";
        }
    }

    public String sellCard(String name) {
        if (!name.matches("^Fireball$") && !name.matches("^Heal$") && !name.matches("^Barbarian$") && !name.matches("^Baby Dragon$") && !name.matches("^Ice Wizard$")) {
            return "Invalid card name!";
        } else if (name.matches("^Fireball$")) {
            if (!loggedInUser.getAllCards().contains(name)) {
                return "You don't have this card!";
            } else if (loggedInUser.getBattleDeck().contains(name)) {
                return "You cannot sell a card from your battle deck!";
            }
            int money = loggedInUser.getGold();
            loggedInUser.setGold(money + 80);
            loggedInUser.getAllCards().remove(name);
            return "Card " + name + " sold successfully!";
        } else if (name.matches("^Heal$")) {
            if (!loggedInUser.getAllCards().contains(name)) {
                return "You don't have this card!";
            } else if (loggedInUser.getBattleDeck().contains(name)) {
                return "You cannot sell a card from your battle deck!";
            }
            int money = loggedInUser.getGold();
            loggedInUser.setGold(money + 120);
            loggedInUser.getAllCards().remove(name);
            return "Card " + name + " sold successfully!";
        } else if (name.matches("^Barbarian$")) {
            if (!loggedInUser.getAllCards().contains(name)) {
                return "You don't have this card!";
            } else if (loggedInUser.getBattleDeck().contains(name)) {
                return "You cannot sell a card from your battle deck!";
            }
            int money = loggedInUser.getGold();
            loggedInUser.setGold(money + 80);
            loggedInUser.getAllCards().remove(name);
            return "Card " + name + " sold successfully!";
        } else if (name.matches("^Baby Dragon$")) {
            if (!loggedInUser.getAllCards().contains(name)) {
                return "You don't have this card!";
            } else if (loggedInUser.getBattleDeck().contains(name)) {
                return "You cannot sell a card from your battle deck!";
            }
            int money = loggedInUser.getGold();
            loggedInUser.setGold(money + 160);
            loggedInUser.getAllCards().remove(name);
            return "Card " + name + " sold successfully!";
        } else {
            if (!loggedInUser.getAllCards().contains(name)) {
                return "You don't have this card!";
            } else if (loggedInUser.getBattleDeck().contains(name)) {
                return "You cannot sell a card from your battle deck!";
            }
            int money = loggedInUser.getGold();
            loggedInUser.setGold(money + 144);
            loggedInUser.getAllCards().remove(name);
            return "Card " + name + " sold successfully!";
        }
    }

    public void showHitpoint() {
        if (isHostsTurn) {
            if (game.getOpponentMiddleHitPoint() > 0) {
                System.out.println("middle castle: " + game.getOpponentMiddleHitPoint());
            } else {
                System.out.println("middle castle: -1");
            }
            if (game.getOpponentLeftHitPoint() > 0)
                System.out.println("left castle: " + game.getOpponentLeftHitPoint());
            else {
                System.out.println("left castle: -1");
            }
            if (game.getOpponentRightHitPoint() > 0)
                System.out.println("right castle: " + game.getOpponentRightHitPoint());
            else {
                System.out.println("right castle: -1");
            }
        } else {
            if (game.getHostMiddleHitPoint() > 0) {
                System.out.println("middle castle: " + game.getHostMiddleHitPoint());
            } else {
                System.out.println("middle castle: -1");
            }
            if (game.getHostLeftHitPoint() > 0)
                System.out.println("left castle: " + game.getHostLeftHitPoint());
            else {
                System.out.println("left castle: -1");
            }
            if (game.getHostRightHitPoint() > 0)
                System.out.println("right castle: " + game.getHostRightHitPoint());
            else {
                System.out.println("right castle: -1");
            }
        }
    }

    public void showLineInfo(String dir) {
        if (!dir.matches("^right$") && !dir.matches("^left$") && !dir.matches("^middle$")) {
            System.out.println("Incorrect line direction!");
            return;
        }
        if (dir.equals("left")) {
            System.out.println(dir + " line:");
            for (int i = 0; i < 15; i++) {
                for (int j = 0; j < game.getLeftSide()[i].size(); j++) {
                    if (game.getLeftSide()[i].get(j).isInHostTeam())
                        System.out.println("row " + (i + 1) + ": " + game.getLeftSide()[i].get(j).getName() + ": " + game.getHost().getUsername());
                    else
                        System.out.println("row " + (i + 1) + ": " + game.getLeftSide()[i].get(j).getName() + ": " + game.getOpponent().getUsername());
                }
            }
        }
        if (dir.equals("right")) {
            System.out.println(dir + " line:");
            for (int i = 0; i < 15; i++) {
                for (int j = 0; j < game.getRightSide()[i].size(); j++) {
                    if (game.getRightSide()[i].get(j).isInHostTeam())
                        System.out.println("row " + (i + 1) + ": " + game.getRightSide()[i].get(j).getName() + ": " + game.getHost().getUsername());
                    else
                        System.out.println("row " + (i + 1) + ": " + game.getRightSide()[i].get(j).getName() + ": " + game.getOpponent().getUsername());
                }
            }
        }
        if (dir.equals("middle")) {
            System.out.println(dir + " line:");
            for (int i = 0; i < 15; i++) {
                for (int j = 0; j < game.getMiddleSide()[i].size(); j++) {
                    if (game.getMiddleSide()[i].get(j).isInHostTeam())
                        System.out.println("row " + (i + 1) + ": " + game.getMiddleSide()[i].get(j).getName() + ": " + game.getHost().getUsername());
                    else
                        System.out.println("row " + (i + 1) + ": " + game.getMiddleSide()[i].get(j).getName() + ": " + game.getOpponent().getUsername());
                }
            }
        }
    }

    public void numberOfCardsToPlay() {
        if (isHostsTurn) {
            if (game.isHostPlayedCard()) System.out.println("You can play 0 cards more!");
            else System.out.println("You can play 1 cards more!");
        } else {
            if (game.isOpponentPlayedCard()) System.out.println("You can play 0 cards more!");
            else System.out.println("You can play 1 cards more!");
        }
    }

    public void numberOfMovesLeft() {
        if (isHostsTurn) System.out.println("You have " + game.getHostNumberOfMoves() + " moves left!");
        else System.out.println("You have " + game.getOpponentNumberOfMoves() + " moves left!");
    }

    public String deployTroop(String name, String dir, String rowNumber) {
        if (!name.matches("^Baby Dragon$") && !name.matches("^Barbarian$") && !name.matches("^Ice Wizard$")) {
            return "Invalid troop name!";
        } else if (isHostsTurn && !game.getHost().getBattleDeck().contains(name)) {
            return "You don't have " + name + " card in your battle deck!";
        } else if (!isHostsTurn && !game.getOpponent().getBattleDeck().contains(name)) {
            return "You don't have " + name + " card in your battle deck!";
        } else if (!dir.matches("^right$") && !dir.matches("^left$") && !dir.matches("^middle$")) {
            return "Incorrect line direction!";
        } else if (Integer.parseInt(rowNumber) > 15 || Integer.parseInt(rowNumber) < 1) {
            return "Invalid row number!";
        } else if (isHostsTurn && Integer.parseInt(rowNumber) > 4) {
            return "Deploy your troops near your castles!";
        } else if (!isHostsTurn && Integer.parseInt(rowNumber) < 12) {
            return "Deploy your troops near your castles!";
        } else if (isHostsTurn && game.isHostPlayedCard()) {
            return "You have deployed a troop or spell this turn!";
        } else if (!isHostsTurn && game.isOpponentPlayedCard()) {
            return "You have deployed a troop or spell this turn!";
        }
        int hitPoint;
        if (name.equals("Baby Dragon")) hitPoint = 3300;
        else if (name.equals("Barbarian")) hitPoint = 2000;
        else hitPoint = 3500;
        if (dir.equals("left")) {
            game.getLeftSide()[Integer.parseInt(rowNumber) - 1].add(new Troops(name, hitPoint, isHostsTurn));
        } else if (dir.equals("right")) {
            game.getRightSide()[Integer.parseInt(rowNumber) - 1].add(new Troops(name, hitPoint, isHostsTurn));
        } else {
            game.getMiddleSide()[Integer.parseInt(rowNumber) - 1].add(new Troops(name, hitPoint, isHostsTurn));
        }
        if (isHostsTurn)
            game.setHostPlayedCard(true);
        else
            game.setOpponentPlayedCard(true);
        return "You have deployed " + name + " successfully!";
    }

    public String moveTroop(String lineDir, String dir, String row) {
        int rowNumber = Integer.parseInt(row);
        if (!lineDir.matches("^right$") && !lineDir.matches("^left$") && !lineDir.matches("^middle$")) {
            return "Incorrect line direction!";
        } else if (rowNumber > 15 || rowNumber < 1) {
            return "Invalid row number!";
        } else if (!dir.matches("^upward$") && !dir.matches("^downward$")) {
            return "you can only move troops upward or downward!";
        } else if (isHostsTurn && game.getHostNumberOfMoves() == 0) {
            return "You are out of moves!";
        } else if (!isHostsTurn && game.getOpponentNumberOfMoves() == 0) {
            return "You are out of moves!";
        }
        int indexOfYourTroop = indexOfYourTroop(lineDir, rowNumber);
        if (indexOfYourTroop == -1)
            return "You don't have any troops in this place!";
        if (rowNumber == 15 && dir.equals("upward")) {
            return "Invalid move!";
        } else if (rowNumber == 1 && dir.equals("downward")) {
            return "Invalid move!";
        }
        if (isHostsTurn) {
            int moves = game.getHostNumberOfMoves();
            game.setHostNumberOfMoves(moves - 1);
        } else {
            int moves = game.getOpponentNumberOfMoves();
            game.setOpponentNumberOfMoves(moves - 1);
        }
        if (lineDir.equals("left")) {
            Troops troop = game.getLeftSide()[rowNumber - 1].get(indexOfYourTroop);
            game.getLeftSide()[rowNumber - 1].remove(indexOfYourTroop);
            if (dir.equals("upward")) {
                game.getLeftSide()[rowNumber].add(troop);
                return troop.getName() + " moved successfully to row " + (rowNumber + 1) + " in line " + lineDir;
            } else {
                game.getLeftSide()[rowNumber - 2].add(troop);
                return troop.getName() + " moved successfully to row " + (rowNumber - 1) + " in line " + lineDir;
            }
        } else if (lineDir.equals("right")) {
            Troops troop = game.getRightSide()[rowNumber - 1].get(indexOfYourTroop);
            game.getRightSide()[rowNumber - 1].remove(indexOfYourTroop);
            if (dir.equals("upward")) {
                game.getRightSide()[rowNumber].add(troop);
                return troop.getName() + " moved successfully to row " + (rowNumber + 1) + " in line " + lineDir;
            } else {
                game.getRightSide()[rowNumber - 2].add(troop);
                return troop.getName() + " moved successfully to row " + (rowNumber - 1) + " in line " + lineDir;
            }
        } else {
            Troops troop = game.getMiddleSide()[rowNumber - 1].get(indexOfYourTroop);
            game.getMiddleSide()[rowNumber - 1].remove(indexOfYourTroop);
            if (dir.equals("upward")) {
                game.getMiddleSide()[rowNumber].add(troop);
                return troop.getName() + " moved successfully to row " + (rowNumber + 1) + " in line " + lineDir;
            } else {
                game.getMiddleSide()[rowNumber - 2].add(troop);
                return troop.getName() + " moved successfully to row " + (rowNumber - 1) + " in line " + lineDir;
            }
        }
    }

    public int indexOfYourTroop(String lineDir, int rowNumber) {
        int indexOfYourTroop = -1;
        if (lineDir.equals("left") && isHostsTurn) {
            for (int i = 0; i < game.getLeftSide()[rowNumber - 1].size(); i++) {
                if (game.getLeftSide()[rowNumber - 1].get(i).isInHostTeam() && !game.getLeftSide()[rowNumber - 1].get(i).getName().equals("Heal")) {
                    indexOfYourTroop = i;
                    break;
                }
            }
        } else if (lineDir.equals("right") && isHostsTurn) {
            for (int i = 0; i < game.getRightSide()[rowNumber - 1].size(); i++) {
                if (game.getRightSide()[rowNumber - 1].get(i).isInHostTeam() && !game.getRightSide()[rowNumber - 1].get(i).getName().equals("Heal")) {
                    indexOfYourTroop = i;
                    break;
                }
            }
        } else if (lineDir.equals("middle") && isHostsTurn) {
            for (int i = 0; i < game.getMiddleSide()[rowNumber - 1].size(); i++) {
                if (game.getMiddleSide()[rowNumber - 1].get(i).isInHostTeam() && !game.getMiddleSide()[rowNumber - 1].get(i).getName().equals("Heal")) {
                    indexOfYourTroop = i;
                    break;
                }
            }
        } else if (lineDir.equals("left") && !isHostsTurn) {
            for (int i = 0; i < game.getLeftSide()[rowNumber - 1].size(); i++) {
                if (!game.getLeftSide()[rowNumber - 1].get(i).isInHostTeam() && !game.getLeftSide()[rowNumber - 1].get(i).getName().equals("Heal")) {
                    indexOfYourTroop = i;
                    break;
                }
            }
        } else if (lineDir.equals("right") && !isHostsTurn) {
            for (int i = 0; i < game.getRightSide()[rowNumber - 1].size(); i++) {
                if (!game.getRightSide()[rowNumber - 1].get(i).isInHostTeam() && !game.getRightSide()[rowNumber - 1].get(i).getName().equals("Heal")) {
                    indexOfYourTroop = i;
                    break;
                }
            }
        } else if (lineDir.equals("middle") && !isHostsTurn) {
            for (int i = 0; i < game.getMiddleSide()[rowNumber - 1].size(); i++) {
                if (!game.getMiddleSide()[rowNumber - 1].get(i).isInHostTeam() && !game.getMiddleSide()[rowNumber - 1].get(i).getName().equals("Heal")) {
                    indexOfYourTroop = i;
                    break;
                }
            }
        }
        return indexOfYourTroop;
    }

    public String deployHeal(String lineDir, String row) {
        int rowNumber = Integer.parseInt(row);
        if (!lineDir.matches("^right$") && !lineDir.matches("^left$") && !lineDir.matches("^middle$")) {
            return "Incorrect line direction!";
        } else if (isHostsTurn && !game.getHost().getBattleDeck().contains("Heal")) {
            return "You don't have Heal card in your battle deck!";
        } else if (!isHostsTurn && !game.getOpponent().getBattleDeck().contains("Heal")) {
            return "You don't have Heal card in your battle deck!";
        } else if (rowNumber > 15 || rowNumber < 1) {
            return "Invalid row number!";
        } else if (isHostsTurn && game.isHostPlayedCard()) {
            return "You have deployed a troop or spell this turn!";
        } else if (!isHostsTurn && game.isOpponentPlayedCard()) {
            return "You have deployed a troop or spell this turn!";
        }
        if (lineDir.equals("left")) {
            game.getLeftSide()[rowNumber - 1].add(new Troops("Heal", 2, isHostsTurn));
        } else if (lineDir.equals("right")) {
            game.getRightSide()[rowNumber - 1].add(new Troops("Heal", 2, isHostsTurn));
        } else {
            game.getMiddleSide()[rowNumber - 1].add(new Troops("Heal", 2, isHostsTurn));
        }
        if (isHostsTurn)
            game.setHostPlayedCard(true);
        else
            game.setOpponentPlayedCard(true);
        return "You have deployed Heal successfully!";
    }

    public String deployFireball(String lineDir) {
        if (!lineDir.matches("^right$") && !lineDir.matches("^left$") && !lineDir.matches("^middle$")) {
            return "Incorrect line direction!";
        } else if (isHostsTurn && !game.getHost().getBattleDeck().contains("Fireball")) {
            return "You don't have Fireball card in your battle deck!";
        } else if (!isHostsTurn && !game.getOpponent().getBattleDeck().contains("Fireball")) {
            return "You don't have Fireball card in your battle deck!";
        } else if (isHostsTurn && game.isHostPlayedCard()) {
            return "You have deployed a troop or spell this turn!";
        } else if (!isHostsTurn && game.isOpponentPlayedCard()) {
            return "You have deployed a troop or spell this turn!";
        } else if (isHostsTurn && lineDir.equals("right") && game.getOpponentRightHitPoint() <= 0) {
            return "This castle is already destroyed!";
        } else if (isHostsTurn && lineDir.equals("middle") && game.getOpponentMiddleHitPoint() <= 0) {
            return "This castle is already destroyed!";
        } else if (isHostsTurn && lineDir.equals("left") && game.getOpponentLeftHitPoint() <= 0) {
            return "This castle is already destroyed!";
        } else if (!isHostsTurn && lineDir.equals("left") && game.getHostLeftHitPoint() <= 0) {
            return "This castle is already destroyed!";
        } else if (!isHostsTurn && lineDir.equals("right") && game.getHostRightHitPoint() <= 0) {
            return "This castle is already destroyed!";
        } else if (!isHostsTurn && lineDir.equals("middle") && game.getHostMiddleHitPoint() <= 0) {
            return "This castle is already destroyed!";
        }
        if (isHostsTurn && lineDir.equals("right")) {
            int hitPoint = game.getOpponentRightHitPoint();
            if (hitPoint < 1600)
                game.setOpponentRightHitPoint(0);
            else game.setOpponentRightHitPoint(hitPoint - 1600);
        } else if (isHostsTurn && lineDir.equals("left")) {
            int hitPoint = game.getOpponentLeftHitPoint();
            if (hitPoint < 1600) game.setOpponentLeftHitPoint(0);
            else game.setOpponentLeftHitPoint(hitPoint - 1600);
        } else if (isHostsTurn && lineDir.equals("middle")) {
            int hitPoint = game.getOpponentMiddleHitPoint();
            if (hitPoint < 1600)
                game.setOpponentMiddleHitPoint(0);
            else game.setOpponentMiddleHitPoint(hitPoint - 1600);
        } else if (!isHostsTurn && lineDir.equals("left")) {
            int hitPoint = game.getHostLeftHitPoint();
            if (hitPoint < 1600)
                game.setHostLeftHitPoint(0);
            else game.setHostLeftHitPoint(hitPoint - 1600);
        } else if (!isHostsTurn && lineDir.equals("right")) {
            int hitPoint = game.getHostRightHitPoint();
            if (hitPoint < 1600)
                game.setHostRightHitPoint(0);
            else game.setHostRightHitPoint(hitPoint - 1600);
        } else if (!isHostsTurn && lineDir.equals("middle")) {
            int hitPoint = game.getHostMiddleHitPoint();
            if (hitPoint < 1600)
                game.setHostMiddleHitPoint(0);
            else game.setHostMiddleHitPoint(hitPoint - 1600);
        }
        if (isHostsTurn)
            game.setHostPlayedCard(true);
        else
            game.setOpponentPlayedCard(true);
        return "You have deployed Fireball successfully!";
    }

    public String nextTurn() {
        if (isHostsTurn) {
            isHostsTurn = false;
            System.out.println("Player " + game.getOpponent().getUsername() + " is now playing!");
            game.setOpponentNumberOfMoves(3);
            game.setOpponentPlayedCard(false);
            return "";
        } else {
            checkGameForNextTurn();
            int totalOpponentHitPoint = game.getOpponentLeftHitPoint() + game.getOpponentRightHitPoint() + game.getOpponentMiddleHitPoint();
            int totalHostHitPoint = game.getHostLeftHitPoint() + game.getHostMiddleHitPoint() + game.getHostRightHitPoint();
            if (game.getTurnNumber() == 1) {//end of the game
                if (totalHostHitPoint == totalOpponentHitPoint) {
                    System.out.println("Game has ended. Result: Tie");
                    giveTheirGifts(game.getHost(), game.getOpponent(), game.getHostRightHitPoint(), game.getHostLeftHitPoint(), game.getHostMiddleHitPoint(), game.getOpponentRightHitPoint(), game.getOpponentLeftHitPoint(), game.getOpponentMiddleHitPoint());
                    return "endgame";
                } else if (totalHostHitPoint > totalOpponentHitPoint) {
                    System.out.println("Game has ended. Winner: " + game.getHost().getUsername());
                    giveTheirGifts(game.getHost(), game.getOpponent(), game.getHostRightHitPoint(), game.getHostLeftHitPoint(), game.getHostMiddleHitPoint(), game.getOpponentRightHitPoint(), game.getOpponentLeftHitPoint(), game.getOpponentMiddleHitPoint());
                    return "endgame";
                } else {
                    System.out.println("Game has ended. Winner: " + game.getOpponent().getUsername());
                    giveTheirGifts(game.getHost(), game.getOpponent(), game.getHostRightHitPoint(), game.getHostLeftHitPoint(), game.getHostMiddleHitPoint(), game.getOpponentRightHitPoint(), game.getOpponentLeftHitPoint(), game.getOpponentMiddleHitPoint());
                    return "endgame";
                }
            }
            if (totalHostHitPoint == 0 && totalOpponentHitPoint == 0) {
                System.out.println("Game has ended. Result: Tie");
                giveTheirGifts(game.getHost(), game.getOpponent(), game.getHostRightHitPoint(), game.getHostLeftHitPoint(), game.getHostMiddleHitPoint(), game.getOpponentRightHitPoint(), game.getOpponentLeftHitPoint(), game.getOpponentMiddleHitPoint());
                return "endgame";
            }
            if (totalHostHitPoint == 0) {
                System.out.println("Game has ended. Winner: " + game.getOpponent().getUsername());
                giveTheirGifts(game.getHost(), game.getOpponent(), game.getHostRightHitPoint(), game.getHostLeftHitPoint(), game.getHostMiddleHitPoint(), game.getOpponentRightHitPoint(), game.getOpponentLeftHitPoint(), game.getOpponentMiddleHitPoint());
                return "endgame";
            }
            if (totalOpponentHitPoint == 0) {
                System.out.println("Game has ended. Winner: " + game.getHost().getUsername());
                giveTheirGifts(game.getHost(), game.getOpponent(), game.getHostRightHitPoint(), game.getHostLeftHitPoint(), game.getHostMiddleHitPoint(), game.getOpponentRightHitPoint(), game.getOpponentLeftHitPoint(), game.getOpponentMiddleHitPoint());
                return "endgame";
            }
            System.out.println("Player " + game.getHost().getUsername() + " is now playing!");
            game.setHostNumberOfMoves(3);
            game.setHostPlayedCard(false);
            isHostsTurn = true;
            int saver = game.getTurnNumber();
            game.setTurnNumber(saver - 1);
        }
        return "";
    }

    public void checkGameForNextTurn() {
        for (int i = 0; i < 15; i++) {
            checkFightTroops(game.getLeftSide()[i]);
        }
        for (int i = 0; i < 15; i++) {
            checkFightTroops(game.getRightSide()[i]);
        }
        for (int i = 0; i < 15; i++) {
            checkFightTroops(game.getMiddleSide()[i]);
        }
        for (int i = 0; i < 15; i++) {
            useHeal(game.getLeftSide()[i]);
        }
        for (int i = 0; i < 15; i++) {
            useHeal(game.getRightSide()[i]);
        }
        for (int i = 0; i < 15; i++) {
            useHeal(game.getMiddleSide()[i]);
        }
        checkFightHostTowers();
        checkFightOpponentTowers();
        checkTowersAfterFights();
        removeDeadTroops();
        checkTroopsForMoreUsualHealth();
    }

    public void removeDeadTroops() {
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < game.getLeftSide()[i].size(); j++) {
                if (game.getLeftSide()[i].get(j).getHitPoint() <= 0)
                    game.getLeftSide()[i].remove(j);
            }
        }
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < game.getRightSide()[i].size(); j++) {
                if (game.getRightSide()[i].get(j).getHitPoint() <= 0)
                    game.getRightSide()[i].remove(j);
            }
        }
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < game.getMiddleSide()[i].size(); j++) {
                if (game.getMiddleSide()[i].get(j).getHitPoint() <= 0)
                    game.getMiddleSide()[i].remove(j);
            }
        }
    }

    public void checkTroopsForMoreUsualHealth() {
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < game.getLeftSide()[i].size(); j++) {
                if (game.getLeftSide()[i].get(j).getName().equals("Baby Dragon") && game.getLeftSide()[i].get(j).getHitPoint() > 3300)
                    game.getLeftSide()[i].get(j).setHitPoint(3300);
                else if (game.getLeftSide()[i].get(j).getName().equals("Barbarian") && game.getLeftSide()[i].get(j).getHitPoint() > 2000)
                    game.getLeftSide()[i].get(j).setHitPoint(2000);
                else if (game.getLeftSide()[i].get(j).getName().equals("Ice Wizard") && game.getLeftSide()[i].get(j).getHitPoint() > 3500)
                    game.getLeftSide()[i].get(j).setHitPoint(3500);
            }
        }
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < game.getRightSide()[i].size(); j++) {
                if (game.getRightSide()[i].get(j).getName().equals("Baby Dragon") && game.getRightSide()[i].get(j).getHitPoint() > 3300)
                    game.getRightSide()[i].get(j).setHitPoint(3300);
                else if (game.getRightSide()[i].get(j).getName().equals("Barbarian") && game.getRightSide()[i].get(j).getHitPoint() > 2000)
                    game.getRightSide()[i].get(j).setHitPoint(2000);
                else if (game.getRightSide()[i].get(j).getName().equals("Ice Wizard") && game.getRightSide()[i].get(j).getHitPoint() > 3500)
                    game.getRightSide()[i].get(j).setHitPoint(3500);
            }
        }
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < game.getMiddleSide()[i].size(); j++) {
                if (game.getMiddleSide()[i].get(j).getName().equals("Baby Dragon") && game.getMiddleSide()[i].get(j).getHitPoint() > 3300)
                    game.getMiddleSide()[i].get(j).setHitPoint(3300);
                else if (game.getMiddleSide()[i].get(j).getName().equals("Barbarian") && game.getMiddleSide()[i].get(j).getHitPoint() > 2000)
                    game.getMiddleSide()[i].get(j).setHitPoint(2000);
                else if (game.getMiddleSide()[i].get(j).getName().equals("Ice Wizard") && game.getMiddleSide()[i].get(j).getHitPoint() > 3500)
                    game.getMiddleSide()[i].get(j).setHitPoint(3500);
            }
        }
    }

    public void checkTowersAfterFights() {
        if (game.getOpponentLeftHitPoint() < 0) game.setOpponentLeftHitPoint(0);
        if (game.getOpponentMiddleHitPoint() < 0) game.setOpponentMiddleHitPoint(0);
        if (game.getOpponentRightHitPoint() < 0) game.setOpponentRightHitPoint(0);
        if (game.getHostLeftHitPoint() < 0) game.setHostLeftHitPoint(0);
        if (game.getHostMiddleHitPoint() < 0) game.setHostMiddleHitPoint(0);
        if (game.getHostRightHitPoint() < 0) game.setHostRightHitPoint(0);
    }

    public void checkFightHostTowers() {
        int hostDamage = game.getHost().getLevel() * 500;
        if (game.getHostLeftHitPoint() > 0) {
            for (int i = 0; i < game.getLeftSide()[0].size(); i++) {
                if (!game.getLeftSide()[0].get(i).isInHostTeam()) {
                    if (game.getLeftSide()[0].get(i).getName().equals("Baby Dragon")) {
                        int hitPoint = game.getHostLeftHitPoint();
                        game.setHostLeftHitPoint(hitPoint - 1200);
                        game.getLeftSide()[0].get(i).addHitPoint((-1 * hostDamage));
                    } else if (game.getLeftSide()[0].get(i).getName().equals("Barbarian")) {
                        int hitPoint = game.getHostLeftHitPoint();
                        game.setHostLeftHitPoint(hitPoint - 900);
                        game.getLeftSide()[0].get(i).addHitPoint((-1 * hostDamage));
                    } else if (game.getLeftSide()[0].get(i).getName().equals("Ice Wizard")) {
                        int hitPoint = game.getHostLeftHitPoint();
                        game.setHostLeftHitPoint(hitPoint - 1500);
                        game.getLeftSide()[0].get(i).addHitPoint((-1 * hostDamage));
                    }
                }
            }
        }
        if (game.getHostMiddleHitPoint() > 0) {
            for (int i = 0; i < game.getMiddleSide()[0].size(); i++) {
                if (!game.getMiddleSide()[0].get(i).isInHostTeam()) {
                    if (game.getMiddleSide()[0].get(i).getName().equals("Baby Dragon")) {
                        int hitPoint = game.getHostMiddleHitPoint();
                        game.setHostMiddleHitPoint(hitPoint - 1200);
                        game.getMiddleSide()[0].get(i).addHitPoint((-1 * hostDamage));
                    } else if (game.getMiddleSide()[0].get(i).getName().equals("Barbarian")) {
                        int hitPoint = game.getHostMiddleHitPoint();
                        game.setHostMiddleHitPoint(hitPoint - 900);
                        game.getMiddleSide()[0].get(i).addHitPoint((-1 * hostDamage));
                    } else if (game.getMiddleSide()[0].get(i).getName().equals("Ice Wizard")) {
                        int hitPoint = game.getHostMiddleHitPoint();
                        game.setHostMiddleHitPoint(hitPoint - 1500);
                        game.getMiddleSide()[0].get(i).addHitPoint((-1 * hostDamage));
                    }
                }
            }
        }
        if (game.getHostRightHitPoint() > 0) {
            for (int i = 0; i < game.getRightSide()[0].size(); i++) {
                if (!game.getRightSide()[0].get(i).isInHostTeam()) {
                    if (game.getRightSide()[0].get(i).getName().equals("Baby Dragon")) {
                        int hitPoint = game.getHostRightHitPoint();
                        game.setHostRightHitPoint(hitPoint - 1200);
                        game.getRightSide()[0].get(i).addHitPoint((-1 * hostDamage));
                    } else if (game.getRightSide()[0].get(i).getName().equals("Barbarian")) {
                        int hitPoint = game.getHostRightHitPoint();
                        game.setHostRightHitPoint(hitPoint - 900);
                        game.getRightSide()[0].get(i).addHitPoint((-1 * hostDamage));
                    } else if (game.getRightSide()[0].get(i).getName().equals("Ice Wizard")) {
                        int hitPoint = game.getHostRightHitPoint();
                        game.setHostRightHitPoint(hitPoint - 1500);
                        game.getRightSide()[0].get(i).addHitPoint((-1 * hostDamage));
                    }
                }
            }
        }
    }

    public void checkFightOpponentTowers() {
        int opponentDamage = game.getOpponent().getLevel() * 500;
        if (game.getOpponentLeftHitPoint() > 0) {
            for (int i = 0; i < game.getLeftSide()[14].size(); i++) {
                if (game.getLeftSide()[14].get(i).isInHostTeam()) {
                    if (game.getLeftSide()[14].get(i).getName().equals("Baby Dragon")) {
                        int hitPoint = game.getOpponentLeftHitPoint();
                        game.setOpponentLeftHitPoint(hitPoint - 1200);
                        game.getLeftSide()[14].get(i).addHitPoint((-1 * opponentDamage));
                    } else if (game.getLeftSide()[14].get(i).getName().equals("Barbarian")) {
                        int hitPoint = game.getOpponentLeftHitPoint();
                        game.setOpponentLeftHitPoint(hitPoint - 900);
                        game.getLeftSide()[14].get(i).addHitPoint((-1 * opponentDamage));
                    } else if (game.getLeftSide()[14].get(i).getName().equals("Ice Wizard")) {
                        int hitPoint = game.getOpponentLeftHitPoint();
                        game.setOpponentLeftHitPoint(hitPoint - 1500);
                        game.getLeftSide()[14].get(i).addHitPoint((-1 * opponentDamage));
                    }
                }
            }
        }
        if (game.getOpponentMiddleHitPoint() > 0) {
            for (int i = 0; i < game.getMiddleSide()[14].size(); i++) {
                if (game.getMiddleSide()[14].get(i).isInHostTeam()) {
                    if (game.getMiddleSide()[14].get(i).getName().equals("Baby Dragon")) {
                        int hitPoint = game.getOpponentMiddleHitPoint();
                        game.setOpponentMiddleHitPoint(hitPoint - 1200);
                        game.getMiddleSide()[14].get(i).addHitPoint((-1 * opponentDamage));
                    } else if (game.getMiddleSide()[14].get(i).getName().equals("Barbarian")) {
                        int hitPoint = game.getOpponentMiddleHitPoint();
                        game.setOpponentMiddleHitPoint(hitPoint - 900);
                        game.getMiddleSide()[14].get(i).addHitPoint((-1 * opponentDamage));
                    } else if (game.getMiddleSide()[14].get(i).getName().equals("Ice Wizard")) {
                        int hitPoint = game.getOpponentMiddleHitPoint();
                        game.setOpponentMiddleHitPoint(hitPoint - 1500);
                        game.getMiddleSide()[14].get(i).addHitPoint((-1 * opponentDamage));
                    }
                }
            }
        }
        if (game.getOpponentRightHitPoint() > 0) {
            for (int i = 0; i < game.getRightSide()[14].size(); i++) {
                if (game.getRightSide()[14].get(i).isInHostTeam()) {
                    if (game.getRightSide()[14].get(i).getName().equals("Baby Dragon")) {
                        int hitPoint = game.getOpponentRightHitPoint();
                        game.setOpponentRightHitPoint(hitPoint - 1200);
                        game.getRightSide()[14].get(i).addHitPoint((-1 * opponentDamage));
                    } else if (game.getRightSide()[14].get(i).getName().equals("Barbarian")) {
                        int hitPoint = game.getOpponentRightHitPoint();
                        game.setOpponentRightHitPoint(hitPoint - 900);
                        game.getRightSide()[14].get(i).addHitPoint((-1 * opponentDamage));
                    } else if (game.getRightSide()[14].get(i).getName().equals("Ice Wizard")) {
                        int hitPoint = game.getOpponentRightHitPoint();
                        game.setOpponentRightHitPoint(hitPoint - 1500);
                        game.getRightSide()[14].get(i).addHitPoint((-1 * opponentDamage));
                    }
                }
            }
        }
    }

    public void useHeal(ArrayList<Troops> troops) {
        for (int i = 0; i < troops.size(); i++) {
            if (troops.get(i).getName().equals("Heal") && troops.get(i).isInHostTeam()) {//check Heal for Host team
                troops.get(i).addHitPoint(-1);//one time used this Heal
                for (int j = 0; j < troops.size(); j++) {
                    if (!troops.get(j).getName().equals("Heal") && troops.get(j).isInHostTeam()) {
                        troops.get(j).addHitPoint(1000);
                    }
                }
            }
        }
        for (int i = 0; i < troops.size(); i++) {
            if (troops.get(i).getName().equals("Heal") && !troops.get(i).isInHostTeam()) {//check Opponent for Host team
                troops.get(i).addHitPoint(-1);//one time used this Heal
                for (int j = 0; j < troops.size(); j++) {
                    if (!troops.get(j).getName().equals("Heal") && !troops.get(j).isInHostTeam()) {
                        troops.get(j).addHitPoint(1000);
                    }
                }
            }
        }
    }

    public void checkFightTroops(ArrayList<Troops> troops) {
        for (int i = 0; i < troops.size(); i++) {
            if (troops.get(i).isInHostTeam()) {
                for (int j = 0; j < troops.size(); j++) {
                    if (!troops.get(j).isInHostTeam()) {
                        if (troops.get(i).getName().equals("Baby Dragon") && troops.get(j).getName().equals("Barbarian")) {
                            troops.get(j).addHitPoint(-300);
                        } else if (troops.get(i).getName().equals("Baby Dragon") && troops.get(j).getName().equals("Ice Wizard")) {
                            troops.get(i).addHitPoint(-300);
                        } else if (troops.get(i).getName().equals("Barbarian") && troops.get(j).getName().equals("Ice Wizard")) {
                            troops.get(i).addHitPoint(-600);
                        } else if (troops.get(i).getName().equals("Barbarian") && troops.get(j).getName().equals("Baby Dragon")) {
                            troops.get(i).addHitPoint(-300);
                        } else if (troops.get(i).getName().equals("Ice Wizard") && troops.get(j).getName().equals("Baby Dragon")) {
                            troops.get(j).addHitPoint(-300);
                        } else if (troops.get(i).getName().equals("Ice Wizard") && troops.get(j).getName().equals("Barbarian")) {
                            troops.get(j).addHitPoint(-600);
                        }
                    }
                }
            }
        }
    }

    public void giveTheirGifts(User host, User opponent, int hostRightHitPoint, int hostLeftHitPoint, int hostMiddleHitPoint, int opponentRightHitPoint, int opponentLeftHitPoint, int opponentMiddleHitPoint) {
        int hostExp = hostLeftHitPoint + hostRightHitPoint + hostMiddleHitPoint + host.getExperience();
        int opponentExp = opponentRightHitPoint + opponentLeftHitPoint + opponentMiddleHitPoint + opponent.getExperience();
        if (hostLeftHitPoint == 0) opponent.addGold(25);
        if (hostMiddleHitPoint == 0) opponent.addGold(25);
        if (hostRightHitPoint == 0) opponent.addGold(25);
        if (opponentLeftHitPoint == 0) host.addGold(25);
        if (opponentMiddleHitPoint == 0) host.addGold(25);
        if (opponentRightHitPoint == 0) host.addGold(25);
        while (true) {
            if (hostExp >= host.getLevel() * host.getLevel() * 160) {
                hostExp -= host.getLevel() * host.getLevel() * 160;
                host.addLevel();
            } else break;
        }
        host.setExperience(hostExp);
        while (true) {
            if (opponentExp >= opponent.getLevel() * opponent.getLevel() * 160) {
                opponentExp -= opponent.getLevel() * opponent.getLevel() * 160;
                opponent.addLevel();
            } else break;
        }
        opponent.setExperience(opponentExp);
    }

    public void printScoreboard() {
        User[] users = new User[User.getUsers().size()];
        for (int i = 0; i < User.getUsers().size(); i++) {
            users[i] = User.getUsers().get(i);
        }
        for (int i = 0; i < users.length; i++) {
            for (int j = i + 1; j < users.length; j++) {
                if (users[i].getLevel() < users[j].getLevel()) {
                    User user = users[i];
                    users[i] = users[j];
                    users[j] = user;
                }
            }
        }
        for (int i = 0; i < users.length; i++) {
            for (int j = i + 1; j < users.length; j++) {
                if (users[i].getLevel() == users[j].getLevel() && users[i].getExperience() < users[j].getExperience()) {
                    User user = users[i];
                    users[i] = users[j];
                    users[j] = user;
                }
            }
        }
        for (int i = 0; i < users.length; i++) {
            for (int j = i + 1; j < users.length; j++) {
                int x = users[i].getUsername().compareTo(users[j].getUsername());
                if (users[i].getLevel() == users[j].getLevel() && users[i].getExperience() == users[j].getExperience() && x > 0) {
                    User user = users[i];
                    users[i] = users[j];
                    users[j] = user;
                }
            }
        }
        for (int i = 0; i < 5 && i < users.length; i++) {
            System.out.println((i + 1) + "- username: " + users[i].getUsername() + " level: " + users[i].getLevel() + " experience: " + users[i].getExperience());
        }
    }
}