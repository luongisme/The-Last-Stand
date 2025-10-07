package Input;


import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardListener implements KeyListener {
    @Override
    public void keyTyped(KeyEvent e) {
        switch(e.getKeyChar()){
            // case 'w':
            //     System.out.println("W key typed");
            //     break;
            // case 'a':
            //     System.out.println("A key typed");
            //     break;
            // case 's':
            //     System.out.println("S key typed");
            //     break;
            // case 'd':
            //     System.out.println("D key typed");
            //     break;
            // default:
            //     break;
        }
    }
    @Override
    public void keyPressed(KeyEvent e) {
        
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
    }
}
