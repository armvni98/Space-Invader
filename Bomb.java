package sprites;

import static game.Commons.*;

public class Bomb extends MovingObject {

    Bomb(int x, int y) {
        super(x, y);
        loadImage("bomb.png");
        width=BOMB_WIDTH*2;
        height=BOMB_HEIGHT*2;
        dy=3;               //bomb coming downwards at rate of 3
    }

    @Override
    public void move() {
        if(y>GROUND-BOMB_HEIGHT)            //reaches bottom of screen && visible becomes false
            this.die();
        super.move();                       //gravity method begins
    }

}
