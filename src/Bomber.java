import java.awt.*;
import java.awt.event.KeyEvent;
import java.net.ResponseCache;
import java.util.ArrayList;

// Nhân vật bomber
public class Bomber{
    protected int bX, bY, dX, dY; // toạ độ của nhân vật
    protected int  speed = 3; // tốc độ di chuyển
    protected int motionStatus = 0; //quyết định hình của nhân vật sẽ đc vẽ lên panel
    protected int direction = 2; // hướng di chuyển
    //các hình ảnh về chuyển động của nhân vật
    //up: direction = 1;
    public Sprite up_1 = new Sprite("data/image/player_up_1.png");
    public Sprite up_2 = new Sprite("data/image/player_up_2.png");
    public Sprite up_3 = new Sprite("data/image/player_up_3.png");
    //down: direction = 2;
    public Sprite down_1 = new Sprite("data/image/player_down_1.png");
    public Sprite down_2 = new Sprite("data/image/player_down_2.png");
    public Sprite down_3 = new Sprite("data/image/player_down_3.png");
    //left: direction = 3;
    public Sprite left_1 = new Sprite("data/image/player_left_1.png");
    public Sprite left_2 = new Sprite("data/image/player_left_2.png");
    public Sprite left_3 = new Sprite("data/image/player_left_3.png");
    //right: direction = 4;
    public Sprite right_1 = new Sprite("data/image/player_right_1.png");
    public Sprite right_2 = new Sprite("data/image/player_right_2.png");
    public Sprite right_3 = new Sprite("data/image/player_right_3.png");
    //dead
    public Sprite dead_1 = new Sprite("data/image/player_dead_1.png");
    public Sprite dead_2 = new Sprite("data/image/player_dead_2.png");
    public Sprite dead_3 = new Sprite("data/image/player_dead_3.png");
    //lưu trạng thái nhân vật
    public Sprite bomberStatus = down_1;
    public ArrayList<Bomb> bombs = new ArrayList<Bomb>();
    public int bombQuantity = 1;
    protected int bombEffectRange = 1;
    public boolean isDead = false;
    private boolean isMoving = false;
    private boolean throughBomb = false;
    private boolean initBomb = false;

    public Bomber(int x, int y){
        bX = x;
        bY = y;
    }

    //kiểm tra va chạm với các vật thế tĩnh: wall, brick
    public boolean staticEntitiesCollision(Map map, int motionDelay){
        int motion = motionStatus % motionDelay;
        Rectangle bomberBound = new Rectangle();
        if (direction == 1){
            if (motion < motionDelay/2){
                bomberBound = new Rectangle(bX, bY, up_2.width, up_2.height);
            }
            else if (motion >= motionDelay/2){
                bomberBound = new Rectangle(bX, bY, up_3.width, up_3.height);
            }
            int index = (bY/48)*map.width + bX/48;
            if (map.data.get(index).type == '#' || map.data.get(index).type == '*' || (map.data.get(index).type == 'b' && !throughBomb)){
                if (bomberBound.intersects(new Rectangle(map.data.get(index).x, map.data.get(index).y, 48, 48))) {
                    if (map.data.get(index+1).type != '#' && map.data.get(index+1).type != '*' && map.data.get(index).type != 'b' && bX % 48 >= 35)
                    bX += speed;
                    return true;
                }
            }
            index++;
            if (map.data.get(index).type == '#' || map.data.get(index).type == '*' || (map.data.get(index).type == 'b' && !throughBomb)){
                if (bomberBound.intersects(new Rectangle(map.data.get(index).x, map.data.get(index).y, 48, 48))) {
                    if (map.data.get(index-1).type != '#' && map.data.get(index-1).type != '*' && bX % 48 <= 35)
                    bX -= speed;
                    return true;
                }
            }
        }

        if (direction == 2){
            if (motion < motionDelay/2){
                bomberBound = new Rectangle(bX, bY, down_2.width, down_2.height);
            }
            else if (motion >= motionDelay/2){
                bomberBound = new Rectangle(bX, bY, down_3.width, down_3.height);
            }
            int index = (bY/48 + 1)*map.width + bX/48;
            if (map.data.get(index).type == '#' || map.data.get(index).type == '*' || (map.data.get(index).type == 'b' && !throughBomb)){
                if (bomberBound.intersects(new Rectangle(map.data.get(index).x, map.data.get(index).y, 48, 48))) {
                    if (map.data.get(index+1).type != '#' && map.data.get(index+1).type != '*' && map.data.get(index).type != 'b' && bX % 48 >= 35)
                    bX += speed;
                    return true;
                }
            }
            index++;
            if (map.data.get(index).type == '#' || map.data.get(index).type == '*' || (map.data.get(index).type == 'b' && !throughBomb)){
                if (bomberBound.intersects(new Rectangle(map.data.get(index).x, map.data.get(index).y, 48, 48))) {
                    if (map.data.get(index-1).type != '#' && map.data.get(index-1).type != '*' && bX % 48 <= 35)
                    bX -= speed;
                    return true;
                }
            }
        }

        if (direction == 3){
            if (motion <= motionDelay/4){
                bomberBound = new Rectangle(bX, bY, left_2.width, left_2.height);
            }
            else if (motionDelay/4 < motion && motion <= 2*motionDelay/4){
                bomberBound = new Rectangle(bX, bY, left_1.width, left_1.height);
            }
            else if (2*motionDelay/4 < motion && motion <= 3*motionDelay/4){
                bomberBound = new Rectangle(bX, bY, left_3.width, left_3.height);
            }
            else if (motion > 3*motionDelay/4){
                bomberBound = new Rectangle(bX, bY, left_1.width, left_1.height);
            }
            int index = (bY/48)*map.width + bX/48;
            if (map.data.get(index).type == '#' || map.data.get(index).type == '*' || (map.data.get(index).type == 'b' && !throughBomb)){
                if (bomberBound.intersects(new Rectangle(map.data.get(index).x, map.data.get(index).y, 48, 48))) {
                    if (map.data.get(index+map.width).type != '#' && map.data.get(index+map.width).type != '*' && map.data.get(index).type != 'b' && bY % 48 >= 35)
                    bY += speed;
                    return true;
                }
            }
            index += map.width;
            if (map.data.get(index).type == '#' || map.data.get(index).type == '*' || (map.data.get(index).type == 'b' && !throughBomb)){
                if (bomberBound.intersects(new Rectangle(map.data.get(index).x, map.data.get(index).y, 48, 48))) {
                    if (map.data.get(index-map.width).type != '#' && map.data.get(index-map.width).type != '*' && bY % 48 <= 18)
                    bY -= speed;
                    return true;
                }
            }
        }

        if (direction == 4){
            if (motion <= motionDelay/4){
                bomberBound = new Rectangle(bX, bY, right_2.width, right_2.height);
            }
            else if (motionDelay/4 < motion && motion <= 2*motionDelay/4){
                bomberBound = new Rectangle(bX, bY, right_1.width, right_1.height);
            }
            else if (2*motionDelay/4 < motion && motion <= 3*motionDelay/4){
                bomberBound = new Rectangle(bX, bY, right_3.width, right_3.height);
            }
            else if (motion > 3*motionDelay/4){
                bomberBound = new Rectangle(bX, bY, right_1.width, right_1.height);
            }
            int index = (bY/48)*map.width + bX/48 + 1;
            if (map.data.get(index).type == '#' || map.data.get(index).type == '*' || (map.data.get(index).type == 'b' && !throughBomb)){
                if (bomberBound.intersects(new Rectangle(map.data.get(index).x, map.data.get(index).y, 48, 48))) {
                    if (map.data.get(index+map.width).type != '#' && map.data.get(index+map.width).type != '*' && map.data.get(index).type != 'b' && bY % 48 >= 35)
                    bY += speed;
                    return true;
                }
            }
            index += map.width;
            if (map.data.get(index).type == '#' || map.data.get(index).type == '*' || (map.data.get(index).type == 'b' && !throughBomb)){
                if (bomberBound.intersects(new Rectangle(map.data.get(index).x, map.data.get(index).y, 48, 48))) {
                    if (map.data.get(index-map.width).type != '#' && map.data.get(index-map.width).type != '*' && bY % 48 <= 18)
                    bY -= speed;
                    return true;
                }
            }
        }
        return false;
    }
    public void keyPressed(KeyEvent e, Map map, int MOTION_DELAY) {
        int key = e.getKeyCode();
        isMoving = true;
        switch (key) {
            case KeyEvent.VK_UP:
                if (direction == 4){
                    bX += speed;
                    if (staticEntitiesCollision(map, MOTION_DELAY)){
                       bX -= 4+speed;
                    }else{
                        bX -= speed;
                    }
                }
                direction = 1;
                dY = -speed;
                dX = 0;
                break;
            case KeyEvent.VK_DOWN:
                if (direction == 4){
                    bX += speed;
                    if (staticEntitiesCollision(map, MOTION_DELAY)){
                        bX -= 4+speed;
                    }else{
                        bX -= speed;
                    }
                }
                direction = 2;
                dY = speed;
                dX = 0;
                break;
            case KeyEvent.VK_LEFT:
                direction = 3;
                dX = -speed;
                dY = 0;
                break;
            case KeyEvent.VK_RIGHT:
                direction = 4;
                dX = speed;
                dY = 0;
                break;
            case KeyEvent.VK_SPACE:
                isMoving = false;
                initBomb = true;
                break;
        }
    }
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        isMoving = false;
        motionStatus = 0;
        switch (key) {
            case KeyEvent.VK_UP:
                dY = 0;
                break;
            case KeyEvent.VK_DOWN:
                dY = 0;
                break;
            case KeyEvent.VK_LEFT:
                dX = 0;
                break;
            case KeyEvent.VK_RIGHT:
                dX = 0;
                break;
        }
    }

    public void move(Map map, int MOTION_DELAY){
        if (!isDead){
            if (isMoving){
                motionStatus++;
            }
            if (initBomb){
                if (bombQuantity > 0){
                    int x = bX + bomberStatus.width/2;
                    int y = bY + bomberStatus.height/2;
                    x -= x%48;
                    y -= y%48;
                    if (map.data.get(map.width*(y/48) + x/48).type != 'b'){
                        bombs.add(new Bomb(x, y));
                        map.data.get(map.width*(y/48) + x/48).type = 'b';
                        bombQuantity--;
                        throughBomb = true;
                    }
                }
                initBomb = false;
            }
            if (!staticEntitiesCollision(map, MOTION_DELAY)) {
                bX += dX;
                bY += dY;
                int n = bombs.size();
                if (n > 0){
                    if (bombs.get(n-1).x/48 != bX / 48 || bombs.get(n-1).y/48 != bY / 48)
                        throughBomb = false;
                }
                if (staticEntitiesCollision(map, MOTION_DELAY)){
                    bX -= dX;
                    bY -= dY;
                }
            }
        }
    }

    public void takeBuffItems(Buff buff){
        int n = buff.listItems.size();
        Rectangle bomberBound = new Rectangle(bX, bY, bomberStatus.width, bomberStatus.height);
        for(int i = 0; i < n; i++){
            Rectangle itemBound = new Rectangle(buff.listItems.get(i).x, buff.listItems.get(i).y, 46, 46);
            if (bomberBound.intersects(itemBound)){
                if (buff.listItems.get(i).type == 's'){
                    speed++;
                    buff.listItems.remove(i);
                    i--;
                    n--;
                }else if (buff.listItems.get(i).type == 'f'){
                    bombEffectRange++;
                    buff.listItems.remove(i);
                    i--;
                    n--;
                }else if (buff.listItems.get(i).type == 'b'){
                    bombQuantity++;
                    buff.listItems.remove(i);
                    i--;
                    n--;
                }
            }
        }
    }
}