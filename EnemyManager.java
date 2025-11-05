import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class EnemyManager {
    private ArrayList<Enemy> enemies = new ArrayList<>();
    private float directionX, directionY;
    private BufferedImage[] enemyImgs; // Array to hold enemy images

    public EnemyManager() {
        enemies = new ArrayList<>();
        directionX = 0;
        directionY = 0;
        enemyImgs = new BufferedImage[5]; // Assuming 5 different enemy types
        addEnemy(3*32, 9*32);
    }

    // private void loadEnemyImgs() {
    //     BuffeeredImage atlas = LoadSave.getSpriteAtlas();
    //     enemyImgs[0] = LoadSave.getSpriteAtlas().getSubimage(0, 32, 32, 32);
    //     enemyImgs[1] = LoadSave.getSpriteAtlas().getSubimage(32, 32, 32, 32);
    //     enemyImgs[2] = LoadSave.getSpriteAtlas().getSubimage(64, 32, 32, 32);
    //     enemyImgs[3] = LoadSave.getSpriteAtlas().getSubimage(96, 32, 32, 32
    // }

    public void update(){
        for(Enemy e : enemies){
            e.move(0.5f, 0);
        }
    }

    public void drawHealthBar(Graphics g){
        for(Enemy enemy : enemies){
            // Draw health bar for each enemy
        }
    }

    public void addEnemy(int x, int y){
        enemies.add(new Enemy(x, y, 1, 100, 10, 2, 2, 1)); // Example enemy
    }

    public void draw(Graphics g){
        for(Enemy e : enemies){
            drawEnemy(e, g);
        }
    }

    public void drawEnemy(Enemy e, Graphics g){
        g.drawImage(enemyImgs[0], (int)e.getX(), (int)e.getY(), null);
    }

    public void isNextTileRoad(float nextX, float nextY){
        // Check if the next tile is road
    }

    public float getDirectionX() {
        return directionX;
    }
    public float getDirectionY() {
        return directionY;
    }
    public void setDirectionX(float directionX) {
        this.directionX = directionX;
    }
    public void setDirectionY(float directionY) {
        this.directionY = directionY;
    }
}
