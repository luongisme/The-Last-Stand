package Player.Skill;

public abstract class AreaEffectSkill {
    private final int id;
    private final String name;
    private final double cooldown; // in seconds
    private double currentCooldown; // in seconds
    private final double radius;
    private final int damage; // damage dealt to enemies

    protected AreaEffectSkill(int id, String name, double cooldown, double radius, int damage) {
        this.id = id;
        this.name = name;
        this.cooldown = cooldown;
        this.radius = radius;
        this.damage = damage;
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

    public int getDamage() {
        return damage;
    }

    public void updateCoolDown(double dt) {
        if (currentCooldown > 0) {
            currentCooldown -= dt;
            if (currentCooldown < 0) {
                currentCooldown = 0;
            }
        }

    }

    public void useSkill() {
        if (isOffCooldown()) {
            currentCooldown = cooldown;
        }
    }


}

