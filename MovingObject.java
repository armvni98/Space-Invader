package sprites;

import game.Board;
import javax.swing.*;
import java.awt.*;

public class MovingObject {

    private Image image;
    public int x;
    public int y;
    int dx;     //velocity OX
    int dy;     //velocity OY
    int width;
    int height;
    boolean dying;
    boolean visible;

    MovingObject(int x, int y) {
        this.x=x;
        this.y=y;
        visible=true;
        dying=false;
    }

    void loadImage(String pngFile) {            //loads the images onto the board
        ImageIcon ii = new ImageIcon(pngFile);
        image = ii.getImage();
    }

    public void explosion() {
        loadImage("explosion.png");
        setDying(true);
    }



    public boolean collisionWith(MovingObject o) {                  //determines collision

        int tx = this.x;
        int ty = this.y;
        int rx = o.x;
        int ry = o.y;

        int rw = o.width + rx;
        int rh =  o.height + ry;
        int tw = this.width + tx;
        int th = this.height + ty;

        return ((rw > tx) &&
                (rh > ty) &&
                (tw > rx) &&
                (th > ry));
    }



    public void draw(Graphics g, Board board) {

        g.drawImage(image, x, y, width, height, board);
    }

    public void die() {

        visible=false;
    }

    public boolean isVisible() {

        return visible;
    }

    void setDying(boolean b) {

        dying=b;
    }

    public boolean isDying() {

        return dying;
    }

    public void move() {        //main move method updating movement on all other sprites (player/enemy/missile)
        x+=dx;
        y+=dy;
    }

}
