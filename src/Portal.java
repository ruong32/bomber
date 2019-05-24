import javax.swing.*;
import java.awt.*;

public class Portal {
    protected int x = -99, y = -99;
    protected Image image = new ImageIcon("data/image/portal.png").getImage();
    public void setXY(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void clear(){
        this.x = -99;
        this.y = -99;
    }

    public Rectangle getBound(){
        return new Rectangle(x, y, 46, 46);
    }
}