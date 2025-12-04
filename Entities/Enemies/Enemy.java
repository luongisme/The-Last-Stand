package Entities.Enemies;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.geometry.Rectangle2D;

public abstract class Enemy {

    private float x, y;
    private int enemyID;
    private int maxHealth;
    private int health;
    private int damage;
    private float speedX, speedY;
    private int enemyType;

    protected int frameW = 32;
    protected int frameH = 32;

    private int barWidth;
    private final int barLength = 5;

    // Directions
    public static final int DOWN = 0;
    public static final int LEFT = 1;
    public static final int RIGHT = 2;
    public static final int UP = 3;

    private int lastDir = RIGHT;

    // Patrol logic
    private float patrolTimer = 0;
    private float patrolInterval = 2000; 
    private int patrolStep = 0;

    private final int[] patrolDirections = {
        RIGHT, DOWN, LEFT, UP
    };

    // JavaFX hitbox
    private Rectangle2D bounds;

    // Animation (time-based)
    protected int animationIndex = 0;
    protected float animationTimer = 0f; // accumulated time in ms
    protected float animationSpeed = 100f; // ms per frame (default)
    protected int maxAnimationFrames = 3;

    public Enemy(float x, float y, int enemyType) {
        this.x = x;
        this.y = y;
        this.enemyType = enemyType;
        this.maxHealth = 50; 
        this.health = maxHealth;
        this.bounds = new Rectangle2D(x, y, 32, 32);
        
        updateBounds();
    }

    // Update the JavaFX hitbox
    public void updateBounds() {
        this.bounds = new Rectangle2D(x, y, 32, 32);
    }

    // ==================== Health Bar Drawing ====================
    public void drawHealthBar(GraphicsContext gc) {
        float maxHealth = getMaxHealth();   
        float currentHealth = this.health; 

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

        // hp bar outline
        gc.setStroke(Color.BLACK);
        gc.strokeRect(barX - 5, barY, fullWidth, 5);

        // hp bar
        gc.fillRect(barX - 5, barY, hpWidth, 5);
    }

    // ==================== Animation System ====================
    public void update(float dt) {
        updateAnimation(dt);
        
        float speed = 50.0f; 
        x += speed * (dt / 1000.0f);
        lastDir = 2; // default is right = 2
    }

    private void updateAnimation(float dt) {
        animationTimer += dt;
        if (animationTimer >= animationSpeed) {
            animationTimer = 0;
            animationIndex++;
            if (animationIndex >= maxAnimationFrames) {
                animationIndex = 0;
            }
        }
    }

    private void updateMove(float dt) {
        // Simple Patrol Logic
        patrolTimer += dt;
        if (patrolTimer >= patrolInterval) {
            patrolTimer = 0;
            patrolStep++;
            if (patrolStep >= patrolDirections.length) patrolStep = 0;
            lastDir = patrolDirections[patrolStep];
        }

        // SPEED: pixels per millisecond
        // 0.05f * 16ms ≈ 0.8 pixels per frame. 
        // 0.1f * 16ms ≈ 1.6 pixels per frame.
        float speed = 0.1f; 
        
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
    public float getEnemySpeedX() { return speedX; }
    public float getEnemySpeedY() { return speedY; }
    public Rectangle2D getBounds() { return bounds; }
    public int getEnemyType() { return enemyType; }
    public int getLastDir() { return lastDir; }
    public int getAnimationIndex() { return animationIndex; }

    // ==================== Setters ====================
    public void setEnemyID(int enemyID) { this.enemyID = enemyID; }
    public void setEnemyHealth(int health) { this.health = health; }
    public void setMaxHealth(int maxHealth) {this.maxHealth = maxHealth;}
    public void setEnemyDamage(int damage) { this.damage = damage; }
    public void setEnemySpeedX(float speedX) { this.speedX = speedX; }
    public void setEnemySpeedY(float speedY) { this.speedY = speedY; }
    public void setEnemyType(int enemyType) { this.enemyType = enemyType; }
    public void setLastDir(int lastDir) { this.lastDir = lastDir; }

}
