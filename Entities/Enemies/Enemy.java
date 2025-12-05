package Entities.Enemies;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public abstract class Enemy {

    private float x, y;
    private int enemyID;
    private int maxHealth;
    private int health;
    private int damage;
    private float speedX, speedY;
    private int enemyType;

    private boolean isHit;
    private boolean isAlive;

    protected int frameW = 32;
    protected int frameH = 32;

    private int barWidth;
    private final int barLength = 4;

    // Directions
    public static final int DOWN = 0;
    public static final int LEFT = 1;
    public static final int RIGHT = 2;
    public static final int UP = 3;

    private int lastDir = RIGHT;


    // JavaFX hitbox
    private Rectangle2D bounds;

    // Animation (time-based)
    protected int animationIndex = 0;
    protected float animationTimer = 5f; // accumulated time in ms
    protected float animationSpeed = 0.1f; // 0.1s = 100ms / frame
    protected int maxAnimationFrames = 3;

    public Enemy(float x, float y, int enemyType) {
        this.x = x;
        this.y = y;
        this.enemyType = enemyType;
        this.isHit=false;
        this.isAlive=true;
        this.maxHealth = 50; 
        this.health = maxHealth;
        this.bounds = new Rectangle2D(x, y, 32, 32);
        
        updateBounds();
    }
    
    public void setSpriteSize(int w, int h) {
        this.frameW = w;
        this.frameH = h;
        updateBounds();
    }

    public int getFrameW() { return frameW; }
    public int getFrameH() { return frameH; }

    // ==================== Getters ====================
    public float getX() { return x; }
    public float getY() { return y; }
    public int getEnemyId() { return enemyID; }
    public int getEnemyHealth() { return health; }
    public int getMaxHealth(){return maxHealth;}
    public int getEnemyDamage() { return damage; }
    public boolean getIsAlive() {return isAlive;}
    public float getEnemySpeedX() { return speedX; }
    public float getEnemySpeedY() { return speedY; }
    public Rectangle2D getBounds() { return bounds; }
    public int getEnemyType() { return enemyType; }
    public int getLastDir() { return lastDir; }
    public int getAnimationIndex() { return animationIndex; }
    public boolean isHit() {return isHit;}



    // ==================== Setters ====================
    public void setEnemyID(int enemyID) { this.enemyID = enemyID; }
    public void setEnemyHealth(int health) { this.health = health; }
    public void setMaxHealth(int maxHealth) {this.maxHealth = maxHealth;}
    public void setEnemyDamage(int damage) { this.damage = damage; }
    public void setEnemySpeedX(float speedX) { this.speedX = speedX; }
    public void setAlive(boolean alive) {isAlive = alive;}
    public void setHit(boolean hit) {isHit = hit;}
    public void setEnemySpeedY(float speedY) { this.speedY = speedY; }
    public void setEnemyType(int enemyType) { this.enemyType = enemyType; }
    public void setLastDir(int lastDir) { this.lastDir = lastDir; }

    // Update the JavaFX hitbox
    public void updateBounds() {
        this.bounds = new Rectangle2D(x, y, 32, 32);
    }

    // ==================== Health Bar Drawing ====================
    public void drawHealthBar(GraphicsContext gc) {
        float maxHealth = getMaxHealth();   // Add getter or store in class
        float currentHealth = this.health;  // your existing variable

        if (currentHealth <= 0) return;

        int barX = (int) x;
        int barY = (int) y - 6;

        // Full health bar width
        int fullWidth = 50;

        // Scale width according to HP
        double hpRatio = currentHealth / maxHealth;
        double hpWidth = fullWidth * hpRatio;

        // Pick color
        if (hpRatio > 0.6) {
            gc.setFill(Color.GREEN);
        } 
        else if (hpRatio > 0.3) {
            gc.setFill(Color.YELLOW);
        } 
        else if (hpRatio > 0.15) {
            gc.setFill(Color.ORANGE);
        } 
        else {
            gc.setFill(Color.RED);
        }

        // Draw outline (optional)
        gc.setStroke(Color.BLACK);
        gc.strokeRect(barX - 5, barY, fullWidth, 5);

        // Draw filled HP bar
        gc.fillRect(barX - 5, barY, hpWidth, 5);
    }

    // ==================== Animation System ====================
    public void update(float dt) {
        // 1. Update Animation
        updateAnimation(dt);
        
        // 2. Update Movement Logic
        updateMove(dt);
    }

    private void updateAnimation(float dt) {
        animationTimer += dt;
        if (animationTimer >= animationSpeed) {
            animationTimer = 0; // or -= animationSpeed
            animationIndex++;
            if (animationIndex >= maxAnimationFrames) {
                animationIndex = 0;
            }
        }
    }

    private void updateMove(float dt) {


        float speed = 100f;
        
        float distance = speed * dt;

        float dx = 0, dy = 0;
        switch (lastDir) {
            case RIGHT -> dx = distance;
            case LEFT  -> dx = -distance;
            case UP    -> dy = -distance;
            case DOWN  -> dy = distance;
        }
        
        // Apply movement
        this.x += dx;
        this.y += dy;
        updateBounds();

    }

    // ==================== Attack Logic ====================
    public boolean canAttack() {
        return true;
    }

    // ==================== Take Damage Logic ====================

    //handle damage taken and death
    public void takeDamage(int damage){
        this.isHit=true;
        this.health-=damage;
        if(this.health<=0) {
            this.isAlive = false;
            this.health = 0;
        }
    }

}
