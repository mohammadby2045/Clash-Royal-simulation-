package Model;

public class Troops {
    private final String name;
    private int hitPoint;
    private final boolean isInHostTeam;

    public Troops(String name, int hitPoint, boolean isInHostTeam) {
        this.name = name;
        this.hitPoint = hitPoint;
        this.isInHostTeam = isInHostTeam;
    }

    public String getName() {
        return name;
    }

    public int getHitPoint() {
        return hitPoint;
    }

    public boolean isInHostTeam() {
        return isInHostTeam;
    }

    public void setHitPoint(int hitPoint) {
        this.hitPoint = hitPoint;
    }

    public void addHitPoint(int hitPoint) {
        this.hitPoint += hitPoint;
    }
}
