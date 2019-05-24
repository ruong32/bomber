import java.awt.*;
import java.util.ArrayList;

public class Bomb {
    protected Sprite bomb_1 = new Sprite("data/image/bomb_1.png");
    protected Sprite bomb_2 = new Sprite("data/image/bomb_2.png");
    protected Sprite bomb_3 = new Sprite("data/image/bomb_3.png");
    protected Sprite bombStatus = bomb_1;

    protected Sprite bomb_exploded_1 = new Sprite("data/image/bomb_exploded_1.png");
    protected Sprite bomb_exploded_2 = new Sprite("data/image/bomb_exploded_2.png");
    protected Sprite bomb_exploded_3 = new Sprite("data/image/bomb_exploded_3.png");
    protected Sprite bomb_exploded_status = bomb_exploded_1;

    protected Sprite explosion_horizontal_1 = new Sprite("data/image/explosion_horizontal_1.png");
    protected Sprite explosion_horizontal_2 = new Sprite("data/image/explosion_horizontal_2.png");
    protected Sprite explosion_horizontal_3 = new Sprite("data/image/explosion_horizontal_3.png");
    protected Sprite explosion_horizontal_status = explosion_horizontal_1;

    protected Sprite explosion_vertical_1 = new Sprite("data/image/explosion_vertical_1.png");
    protected Sprite explosion_vertical_2 = new Sprite("data/image/explosion_vertical_2.png");
    protected Sprite explosion_vertical_3 = new Sprite("data/image/explosion_vertical_3.png");
    protected Sprite explosion_vertical_status = explosion_vertical_1;

    protected Sprite explosion_horizontal_left_last_1 = new Sprite("data/image/explosion_horizontal_left_last_1.png");
    protected Sprite explosion_horizontal_left_last_2 = new Sprite("data/image/explosion_horizontal_left_last_2.png");
    protected Sprite explosion_horizontal_left_last_3 = new Sprite("data/image/explosion_horizontal_left_last_3.png");
    protected Sprite explosion_horizontal_left_last_status = explosion_horizontal_left_last_1;

    protected Sprite explosion_horizontal_right_last_1 = new Sprite("data/image/explosion_horizontal_right_last_1.png");
    protected Sprite explosion_horizontal_right_last_2 = new Sprite("data/image/explosion_horizontal_right_last_2.png");
    protected Sprite explosion_horizontal_right_last_3 = new Sprite("data/image/explosion_horizontal_right_last_3.png");
    protected Sprite explosion_horizontal_right_last_status = explosion_horizontal_right_last_1;

    protected Sprite explosion_vertical_top_last_1 = new Sprite("data/image/explosion_vertical_top_last_1.png");
    protected Sprite explosion_vertical_top_last_2 = new Sprite("data/image/explosion_vertical_top_last_2.png");
    protected Sprite explosion_vertical_top_last_3 = new Sprite("data/image/explosion_vertical_top_last_3.png");
    protected Sprite explosion_vertical_top_last_status = explosion_vertical_top_last_1;

    protected Sprite explosion_vertical_down_last_1 = new Sprite("data/image/explosion_vertical_down_last_1.png");
    protected Sprite explosion_vertical_down_last_2 = new Sprite("data/image/explosion_vertical_down_last_2.png");
    protected Sprite explosion_vertical_down_last_3 = new Sprite("data/image/explosion_vertical_down_last_3.png");
    protected Sprite explosion_vertical_down_last_status = explosion_vertical_down_last_1;

    protected boolean exploded = false;
    protected int timeCountingDown = 120;
    protected int effectTimeOfFlame = 20;
    protected int east, west, south, north;
    protected int x, y;

    public Bomb(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void flameCollision(MainGame mainGame){
        ArrayList<Rectangle> listRecangle = new ArrayList<Rectangle>();
        listRecangle.add(new Rectangle(x, y, 48,48));
        for(int i = 1; i <= east; i++){
            listRecangle.add(new Rectangle(x+i*48, y, 48,48));
        }
        for(int i = 1; i <= west; i++){
            listRecangle.add(new Rectangle(x-i*48, y, 48,48));
        }
        for(int i = 1; i <= south; i++){
            listRecangle.add(new Rectangle(x, y+i*48, 48,48));
        }
        for(int i = 1; i <= north; i++){
            listRecangle.add(new Rectangle(x, y-i*48, 48,48));
        }
        for(Rectangle rectangle : listRecangle){
            if (rectangle.intersects(new Rectangle(mainGame.player.bX, mainGame.player.bY, mainGame.player.bomberStatus.width, mainGame.player.bomberStatus.height))){
                mainGame.player.motionStatus = 0;
                mainGame.player.isDead = true;
            }
            for (balloom bl : mainGame.ballooms){
                if (rectangle.intersects(new Rectangle(bl.bX, bl.bY, bl.balloomStatus.width, bl.balloomStatus.height))){
                    bl.motionStatus = 0;
                    bl.isDead = true;
                }
            }
            for (oneal ol : mainGame.oneals){
                if (rectangle.intersects(new Rectangle(ol.bX, ol.bY, ol.onealStatus.width, ol.onealStatus.height))){
                    ol.motionStatus = 0;
                    ol.isDead = true;
                }
            }
            for (Bomb bomb : mainGame.player.bombs){
                if (bomb.timeCountingDown > 0){
                    if (rectangle.intersects(new Rectangle(bomb.x, bomb.y, bomb.bombStatus.width, bomb.bombStatus.height))){
                        bomb.timeCountingDown = 0;
                    }
                }
            }
        }
    }
}