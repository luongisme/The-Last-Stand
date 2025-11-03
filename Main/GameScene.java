package Main;
import java.awt.image.BufferedImage;
public class GameScene {
    protected Game game;
    protected int animationIndex;
    protected int ANIMATION_SPEED;
    protected int tick;

    
    public GameScene(Game game){
        this.game=game;
    }

    public Game getgame(){
        return game;
    }

    protected boolean isAnimation(int spriteID){
        return false;
    }

    public void updateTick(){

    }

    
}
