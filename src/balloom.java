import java.awt.*;

public class balloom extends Enemy {
    //up == left
    protected Sprite left_1 = new Sprite("data/image/balloom_left1.png");
    protected Sprite left_2 = new Sprite("data/image/balloom_left2.png");
    protected Sprite left_3 = new Sprite("data/image/balloom_left3.png");
    //down == right
    protected Sprite right_1 = new Sprite("data/image/balloom_right1.png");
    protected Sprite right_2 = new Sprite("data/image/balloom_right2.png");
    protected Sprite right_3 = new Sprite("data/image/balloom_right3.png");

    protected Sprite dead = new Sprite("data/image/balloom_dead.png");
    //lưu trạng thái balloom
    protected Sprite balloomStatus = left_1;
    protected boolean isDead = false;
    public balloom(int x, int y){
        super(x,y);
    }

    public void move(Map map){
        int x;
        if (!staticEntitiesCollision(map)) {
            x = random.nextInt();
            if (x % 4 == 0) {
                x = random.nextInt() % 3;
                if (x < 0) x = -x;
                x++;
                speed = x;
            }
            motionStatus++;
            if (direction == 1) {
                bY -= speed;
                if (staticEntitiesCollision(map)) {
                    bY += speed;
                    motionStatus = 0;
                    do {
                        x = random.nextInt() % 4;
                        if (x < 0) x = -x;
                        x++;
                    } while (staticEntitiesCollision(map));
                    direction = x;
                }

            } else if (direction == 2) {
                bY += speed;
                if (staticEntitiesCollision(map)) {
                    bY -= speed;
                    motionStatus = 0;
                    do {
                        x = random.nextInt() % 4;
                        if (x < 0) x = -x;
                        x++;
                    } while (staticEntitiesCollision(map));
                    direction = x;
                }
            } else if (direction == 3) {
                bX -= speed;
                if (staticEntitiesCollision(map)) {
                    bX += speed;
                    motionStatus = 0;
                    do {
                        x = random.nextInt() % 4;
                        if (x < 0) x = -x;
                        x++;
                    } while (staticEntitiesCollision(map));
                    direction = x;
                }
            } else if (direction == 4) {
                bX += speed;
                if (staticEntitiesCollision(map)) {
                    bX -= speed;
                    motionStatus = 0;
                    do {
                        x = random.nextInt() % 4;
                        if (x < 0) x = -x;
                        x++;
                    } while (staticEntitiesCollision(map));
                    direction = x;
                }
            }
        }
    }

    public boolean  staticEntitiesCollision(Map map){

        Rectangle balloomBound = new Rectangle(bX, bY, 48, 48);
            if (direction == 1){
                int index = (bY/48)*map.width + bX/48;
                if (map.data.get(index).type == '#' || map.data.get(index).type == '*' || map.data.get(index).type == 'b'){
                    if (balloomBound.intersects(new Rectangle(map.data.get(index).x, map.data.get(index).y, 48, 48))) return true;
                }
                index++;
                if (map.data.get(index).type == '#' || map.data.get(index).type == '*' || map.data.get(index).type == 'b'){
                    if (balloomBound.intersects(new Rectangle(map.data.get(index).x, map.data.get(index).y, 48, 48))) return true;
                }
            }

            if (direction == 3){
                int index = (bY/48)*map.width + bX/48;
                if (map.data.get(index).type == '#' || map.data.get(index).type == '*' || map.data.get(index).type == 'b'){
                    if (balloomBound.intersects(new Rectangle(map.data.get(index).x, map.data.get(index).y, 48, 48))) return true;
                }
                index += map.width;
                if (map.data.get(index).type == '#' || map.data.get(index).type == '*' || map.data.get(index).type == 'b'){
                    if (balloomBound.intersects(new Rectangle(map.data.get(index).x, map.data.get(index).y, 48, 48))) return true;
                }
            }



            if (direction == 2){
                int index = (bY/48 + 1)*map.width + bX/48;
                if (map.data.get(index).type == '#' || map.data.get(index).type == '*' || map.data.get(index).type == 'b'){
                    if (balloomBound.intersects(new Rectangle(map.data.get(index).x, map.data.get(index).y, 48, 48))) return true;
                }
                index++;
                if (map.data.get(index).type == '#' || map.data.get(index).type == '*' || map.data.get(index).type == 'b'){
                    if (balloomBound.intersects(new Rectangle(map.data.get(index).x, map.data.get(index).y, 48, 48))) return true;
                }
            }

            if (direction == 4){
                int index = (bY/48)*map.width + bX/48 + 1;
                if (map.data.get(index).type == '#' || map.data.get(index).type == '*' || map.data.get(index).type == 'b'){
                    if (balloomBound.intersects(new Rectangle(map.data.get(index).x, map.data.get(index).y, 48, 48))) return true;
                }
                index += map.width;
                if (map.data.get(index).type == '#' || map.data.get(index).type == '*' || map.data.get(index).type == 'b'){
                    if (balloomBound.intersects(new Rectangle(map.data.get(index).x, map.data.get(index).y, 48, 48))) return true;
                }
            }

        return false;
    }
}