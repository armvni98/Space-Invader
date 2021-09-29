package sprites;



import java.util.Random;
import static game.Commons.ENEMY_HEIGHT;
import static game.Commons.ENEMY_WIDTH;

public class Boss extends MovingObject {

    public TargetedBomb bomb;


    boolean almostDied;     //for proper projection of explosion
    private Random rand;



    public TargetedBomb getBomb() {
        return bomb;
    }

    Boss(int x, int y) {
        super(x, y);
        rand = new Random();
        loadImage("Boss.png");
        width=56;
        height=56;
        dx=8;
        almostDied=false;
        bomb =  new TargetedBomb(0,0);    //new Bomb(0, 0);
        bomb.loadImage("bb3.png");
        bomb.width = 40;
        bomb.height = 40;
//

        bomb.die();

    }

    void tryToShoot() {

        int random = rand.nextInt()%10; // Randomly shooting by the enemy
        if(random==1 && !this.bomb.visible && this.visible){ //!this.bomb.visible
            this.bomb.x=this.x+ENEMY_WIDTH/2;
            this.bomb.y=this.y+ENEMY_HEIGHT;
            this.bomb.visible=true;
        }
    }




    void setAlmostDied(boolean almostDied) {
        this.almostDied = almostDied;
    }

    @Override
    public void move() {
        super.move();
    }

}
