package Player.Skill;

import Main.GameScreen;

public abstract class AreaEffectSkill {
    private int id;
    private String name;
    private double cooldown; // in seconds
    private double currentCooldown; // in seconds
    private double radius;

    protected AreaEffectSkill(int id, String name, double cooldown, double radius) {
        this.id = id;
        this.name = name;
        this.cooldown = cooldown;
        this.radius = radius;
        this.currentCooldown = 0;
    }

    public boolean isOffCooldown() {
        return currentCooldown <= 0;
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getCooldown() {
        return cooldown;
    }

    public double getCurrentCooldown() {
        return currentCooldown;
    }

    public double getRadius() {
        return radius;
    }

    public void updateCoolDown(double dt) {
        if (currentCooldown > 0) {
            currentCooldown -= dt;
            if (currentCooldown < 0) {
                currentCooldown = 0;
            }
        }

    }
}

