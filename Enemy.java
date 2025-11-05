import java.awt.Graphics;
import java.awt.Rectangle;

public class Enemy {
    private float x, y;
    private int enemyID;
    private int health;
    private int damage;
    private float speedX, speedY;
    private int enemyType;
    private int lastDir; // 0: up, 1: right, 2: down, 3: left
    private Rectangle bounds;

    public Enemy(float x, float y, int enemyID, int health, int damage, float speedX, float speedY, int enemyType) {
        this.x = x;
        this.y = y;
        this.enemyID = enemyID;
        this.health = health;
        this.damage = damage;
        this.speedX = speedX;
        this.speedY = speedY;
        this.enemyType = enemyType;
        this.bounds = new Rectangle((int)x, (int)y, 32, 32); // Example hitbox size
        this.lastDir = -1; // No direction
    }

    public void update(float dt) {
        // Update enemy position and state
    }
    
    public void move(float x, float y) {
        this.x += x;
        this.y += y;
        this.bounds.setLocation((int)this.x, (int)this.y);
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

    public boolean canAttack() {
        // Determine if the enemy can attack
        return true;
    }

    public void render(Graphics g){
        // Render enemy on screen
    }
}
