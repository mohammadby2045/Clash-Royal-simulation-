package Model;

import java.util.ArrayList;

public class User {
    private static final ArrayList<User> users = new ArrayList<>();
    private String password;
    private String username;
    private int gold;
    private int level;
    private int experience;
    private ArrayList<String> allCards;
    private ArrayList<String> battleDeck;

    public User(String password, String username, int gold, int level, int experience) {
        this.password = password;
        this.username = username;
        this.gold = gold;
        this.experience = experience;
        this.level = level;
        this.allCards = new ArrayList<>();
        this.battleDeck = new ArrayList<>();
    }

    public static User getUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username))
                return user;
        }
        return null;
    }

    public void addGold(int gold) {
        this.gold += gold;
    }

    public void addLevel() {
        this.level++;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public int getLevel() {
        return level;
    }

    public int getExperience() {
        return experience;
    }

    public static ArrayList<User> getUsers() {
        return users;
    }

    public static void addUser(String username, String password) {
        users.add(new User(password, username, 100, 1, 0));
        getUserByUsername(username).addCard("Fireball");
        getUserByUsername(username).addCard("Barbarian");
        getUserByUsername(username).addBattleDeck("Fireball");
        getUserByUsername(username).addBattleDeck("Barbarian");
    }

    public void addCard(String cardName) {
        this.allCards.add(cardName);
    }

    public void addBattleDeck(String cardName) {
        this.battleDeck.add(cardName);
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public String getUsername() {
        return username;
    }

    public int getGold() {
        return gold;
    }

    public ArrayList<String> getAllCards() {
        return allCards;
    }

    public ArrayList<String> getBattleDeck() {
        return battleDeck;
    }

    public boolean isPasswordCorrect(String password) {
        return this.getPassword().equals(password);
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public String getPassword() {
        return password;
    }
}
