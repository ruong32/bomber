import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

// lưu toạ độ của các vật thể brick
public class brick {
    protected int x, y;
    protected boolean isDestroyed = false;
    protected int timeAfterDestroyed = 18;
    protected Image image = new ImageIcon("data/image/brick.png").getImage();
    protected Sprite brick_exploded_1 = new Sprite("data/image/brick_exploded_1.png");
    protected Sprite brick_exploded_2 = new Sprite("data/image/brick_exploded_2.png");
    protected Sprite brick_exploded_3 = new Sprite("data/image/brick_exploded_3.png");
    protected Sprite brick_exploded_status = brick_exploded_1;

    public brick(int x, int y){
        this.x = x;
        this.y = y;
    }
}