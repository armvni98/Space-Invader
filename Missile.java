package sprites;

import static game.Commons.MISSILE_HEIGHT;
import static game.Commons.MISSILE_SPEED;
import static game.Commons.MISSILE_WIDTH;

public class Missile extends MovingObject {

    Missile(int x, int y) {
        super(x, y);
        loadImage("missile.png");
        width=MISSILE_WIDTH;
        height=MISSILE_HEIGHT;
        dy=-MISSILE_SPEED;      //player missile going upwards
        dx= 0;
    }

    @Override
    public void move() {
        if(y<=0) {
            this.die();     //missile visible false
        }

        super.move();
    }

}
