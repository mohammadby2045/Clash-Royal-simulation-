package Model;

import java.util.ArrayList;

public class Game {
    private int turnNumber;
    private User host;
    private User opponent;
    private ArrayList<Troops>[] leftSide;
    private ArrayList<Troops>[] rightSide;
    private ArrayList<Troops>[] middleSide;
    private int hostLeftHitPoint;
    private int hostRightHitPoint;
    private int hostMiddleHitPoint;
    private int opponentLeftHitPoint;
    private int opponentRightHitPoint;
    private int opponentMiddleHitPoint;
    private boolean isHostPlayedCard;
    private boolean isOpponentPlayedCard;
    private int hostNumberOfMoves;
    private int opponentNumberOfMoves;

    public Game(int turnNumber, User host, User opponent, int hostLeftHitPoint, int hostRightHitPoint, int hostMiddleHitPoint, int opponentLeftHitPoint, int opponentRightHitPoint, int opponentMiddleHitPoint) {
        this.host = host;
        this.opponent = opponent;
        this.leftSide = new ArrayList[15];
        for (int i = 0; i < 15; i++) {
            this.leftSide[i] = new ArrayList<>();
        }
        this.rightSide = new ArrayList[15];
        for (int i = 0; i < 15; i++) {
            this.rightSide[i] = new ArrayList<>();
        }
        this.middleSide = new ArrayList[15];
        for (int i = 0; i < 15; i++) {
            this.middleSide[i] = new ArrayList<>();
        }
        this.hostLeftHitPoint = hostLeftHitPoint;
        this.hostRightHitPoint = hostRightHitPoint;
        this.hostMiddleHitPoint = hostMiddleHitPoint;
        this.opponentLeftHitPoint = opponentLeftHitPoint;
        this.opponentRightHitPoint = opponentRightHitPoint;
        this.opponentMiddleHitPoint = opponentMiddleHitPoint;
        this.turnNumber = turnNumber;
        this.isHostPlayedCard = false;
        this.isOpponentPlayedCard = false;
        this.hostNumberOfMoves = 3;
        this.opponentNumberOfMoves = 3;
    }

    public int getHostNumberOfMoves() {
        return hostNumberOfMoves;
    }

    public int getOpponentNumberOfMoves() {
        return opponentNumberOfMoves;
    }

    public ArrayList<Troops>[] getLeftSide() {
        return leftSide;
    }

    public ArrayList<Troops>[] getRightSide() {
        return rightSide;
    }

    public ArrayList<Troops>[] getMiddleSide() {
        return middleSide;
    }

    public boolean isHostPlayedCard() {
        return isHostPlayedCard;
    }

    public boolean isOpponentPlayedCard() {
        return isOpponentPlayedCard;
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    public User getHost() {
        return host;
    }

    public User getOpponent() {
        return opponent;
    }

    public int getHostLeftHitPoint() {
        return hostLeftHitPoint;
    }

    public int getHostRightHitPoint() {
        return hostRightHitPoint;
    }

    public int getHostMiddleHitPoint() {
        return hostMiddleHitPoint;
    }

    public int getOpponentLeftHitPoint() {
        return opponentLeftHitPoint;
    }

    public int getOpponentRightHitPoint() {
        return opponentRightHitPoint;
    }

    public int getOpponentMiddleHitPoint() {
        return opponentMiddleHitPoint;
    }

    public void setHostPlayedCard(boolean hostPlayedCard) {
        isHostPlayedCard = hostPlayedCard;
    }

    public void setOpponentPlayedCard(boolean opponentPlayedCard) {
        isOpponentPlayedCard = opponentPlayedCard;
    }

    public void setHostNumberOfMoves(int hostNumberOfMoves) {
        this.hostNumberOfMoves = hostNumberOfMoves;
    }

    public void setOpponentNumberOfMoves(int opponentNumberOfMoves) {
        this.opponentNumberOfMoves = opponentNumberOfMoves;
    }

    public void setHostLeftHitPoint(int hostLeftHitPoint) {
        this.hostLeftHitPoint = hostLeftHitPoint;
    }

    public void setHostRightHitPoint(int hostRightHitPoint) {
        this.hostRightHitPoint = hostRightHitPoint;
    }

    public void setHostMiddleHitPoint(int hostMiddleHitPoint) {
        this.hostMiddleHitPoint = hostMiddleHitPoint;
    }

    public void setOpponentLeftHitPoint(int opponentLeftHitPoint) {
        this.opponentLeftHitPoint = opponentLeftHitPoint;
    }

    public void setOpponentRightHitPoint(int opponentRightHitPoint) {
        this.opponentRightHitPoint = opponentRightHitPoint;
    }

    public void setOpponentMiddleHitPoint(int opponentMiddleHitPoint) {
        this.opponentMiddleHitPoint = opponentMiddleHitPoint;
    }

    public void setTurnNumber(int turnNumber) {
        this.turnNumber = turnNumber;
    }
}
