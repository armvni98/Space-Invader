package sprites;

import static game.Commons.*;

public class TargetedBomb extends MovingObject {
    TargetedBomb(int x, int y) {
        super(x, y);
        loadImage("bomb.png");
        width=BOMB_WIDTH;
        height=BOMB_HEIGHT;
        dy=3;               //bomb coming downwards at rate of 3
    }

    @Override
    public void move() {
        if(y>GROUND-BOMB_HEIGHT)            //reaches bottom of screen && visible becomes false
            this.die();
        super.move();                       //gravity method begins
    }
}
