package sprites;

import game.Board;

import java.awt.event.KeyEvent;
import static game.Commons.*;

public class Player extends MovingObject {

    private Missile m;

    public Player (int x, int y) {
        super(x, y);
        loadImage("SpaceShip.png");

        width=PLAYER_WIDTH;
        height=PLAYER_HEIGHT;
        m = new Missile(0, 0);
        m.die();

    }

    public Missile getM() {

        return m;
    }

    public void revive() {
        loadImage("SpaceShip.png");
        setDying(false);
        x=500;
    }

    public void missleMove() {
        if(m.isVisible()) {
            m.move();
        }
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            dx = -PLAYER_SPEED;
        }
        if (key == KeyEvent.VK_RIGHT) {
            dx = PLAYER_SPEED;
        }


        if (key == KeyEvent.VK_SPACE) {
            if(!m.visible) {
                Board.play("missile.wav");
                m.visible=true;
                m.x=this.x + PLAYER_WIDTH/2;    //center of player location
                m.y=this.y;


            }
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if(key==KeyEvent.VK_LEFT) {
            dx=0;
        }
        if(key==KeyEvent.VK_R) {
            new game.SpaceInvaders();
        }
        if(key==KeyEvent.VK_RIGHT) {
            dx=0;
        }


    }

    @Override
    public void move() {
        if(x>BOARD_WIDTH-PLAYER_WIDTH)
            x=BOARD_WIDTH-PLAYER_WIDTH;
        else if(x<0)

            x=0;
        else if(y>BOARD_HEIGHT-65)
            y = BOARD_HEIGHT-65;
        else
            super.move();
    }

}
