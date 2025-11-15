package Entities.Enemies;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class Enemy {
    private float x, y;
    private int enemyID;
    private int health;
    private int damage;
    private float speedX, speedY;
    private int enemyType;
    private int barWidth;
    private int barLength = 5;

    // 0 = Up, 1 = Right, 2 = Down, 3 = Left
    public static final int DOWN = 0;
    public static final int LEFT = 1;
    public static final int RIGHT = 2;
    public static final int UP = 3;

    // override lastDir default
    private int lastDir = DOWN;  // default
    // Patrol logic
    private float patrolTimer = 0;
    private float patrolInterval = 2000; // milliseconds per direction
    private int patrolStep = 0;

    // Patrol order (change this to your liking)
    private final int[] patrolDirections = {
        RIGHT, DOWN, LEFT, UP
    };



    private Rectangle bounds;

    protected int animationIndex = 0;
    protected int animationTick = 0;
    protected int animationSpeed = 10; // Lower is faster
    protected int maxAnimationFrames = 3; // Set this to the number of frames per enemy

    public Enemy(float x, float y, int enemyType) {
        this.x = x;
        this.y = y;
        // this.enemyID = enemyID;
        // this.health = health;
        // this.damage = damage;
        // this.speedX = speedX;
        // this.speedY = speedY;
        this.enemyType = enemyType;
        this.barWidth = 50; // default health bar width
        this.bounds = new Rectangle((int)x, (int)y, 32, 32); // Example hitbox size
        this.lastDir = lastDir; // No direction
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getEnemyId() {
        return enemyID;
    }

    public int getEnemyHealth() {
        return health;
    }

    public int getEnemyDamage() {
        return damage;
    }

    public float getEnemySpeedX() {
        return speedX;
    }

    public float getEnemySpeedY() {
        return speedY;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public int getEnemyType() {
        return enemyType;
    }

    public int getLastDir() {
        return lastDir;
    }

    public void setEnemyID(int enemyID) {
        this.enemyID = enemyID;
    }

    public void setEnemyHealth(int health) {
        this.health = health;
    }

    public void setEnemyDamage(int damage) {
        this.damage = damage;
    }

    public void setEnemySpeedX(float speedX) {
        this.speedX = speedX;
    }

    public void setEnemySpeedY(float speedY) {
        this.speedY = speedY;
    }

    public void setEnemyType(int enemyType) {
        this.enemyType = enemyType;
    }

    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }

    public void setLastDir(int lastDir) {
        this.lastDir = lastDir;
    }

    public void drawHealthBar(Graphics g){//draw health bar method
        int barX=(int) x;
        int barY=(int) y-10;
        barWidth=50;

        
        if(barWidth <= 30 && barWidth >= 20){
            g.setColor(Color.YELLOW);
        }
        else if(barWidth <= 20 && barWidth >= 10){
            g.setColor(Color.ORANGE);
        }
        else if(barWidth <= 10 && barWidth>=0){
            g.setColor(Color.RED);
        }
        else{
            g.setColor(Color.GREEN);
        }
        g.fillRect(barX-8, barY+5,barWidth,barLength);
    }


    public void updateAnimation() {
        animationTick++;
        if (animationTick >= animationSpeed) {
            animationTick = 0;
            animationIndex++;
            if (animationIndex >= maxAnimationFrames) {
                animationIndex = 0;
            }
        }
    }

    public void update(float dt) {
        patrolTimer += dt;

        // When time exceeds patrol interval → change direction
        if (patrolTimer >= patrolInterval) {
            patrolTimer = 0;

            patrolStep++;
            if (patrolStep >= patrolDirections.length)
                patrolStep = 0;

            lastDir = patrolDirections[patrolStep]; 
        }

        // Move according to current direction
        float speed = 0.5f;
        float dx = 0;
        float dy = 0;

        switch (lastDir) {
            case RIGHT -> dx = speed;
            case LEFT  -> dx = -speed;
            case UP    -> dy = -speed;
            case DOWN  -> dy = speed;
        }

        move(dx, dy);
        updateAnimation();
    }

    // Expose current animation frame for external renderers
    public int getAnimationIndex() {
        return animationIndex;
    }
    
    public void move(float mx, float my) {
        this.x += mx;
        this.y += my;

        // Set direction based on movement
        if (Math.abs(mx) > Math.abs(my)) {
            if (mx > 0){ lastDir = RIGHT; }
            else if (mx < 0) { lastDir = LEFT; }
        } else {
            if (my > 0) { lastDir = DOWN; }
            else if (my < 0) { lastDir = UP; }
        }
        this.bounds.setLocation((int)this.x, (int)this.y);
    }

    public boolean canAttack() {
        // Determine if the enemy can attack
        return true;
    }

    public void render(Graphics g){
        // Render enemy on screen
    }
}
