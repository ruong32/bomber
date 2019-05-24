import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

//nơi xử lý và vẽ các khung hình của game
public class MainGame extends JPanel implements ActionListener{

    private final int DELAY = 20; //thời gian nghỉ sau khi vẽ xong khung hình
    private Timer timer = new Timer(DELAY, this);
    protected Bomber player;
    private final int MOTION_DELAY = 24;
    protected Image wall = new ImageIcon("data/image/wall.png").getImage();
    protected Image grass = new ImageIcon("data/image/grass.png").getImage();
    protected ArrayList<brick> bricks = new ArrayList<brick>();
    protected ArrayList<balloom> ballooms = new ArrayList<balloom>();
    protected ArrayList<oneal> oneals = new ArrayList<oneal>();
    protected Buff buff = new Buff();
    protected boolean GameOver = false;
    protected Map map = new Map();
    protected Portal portal = new Portal();
    protected int level = 1;
    protected int timeRemain = 200000;
    protected infoBar ifBar;
    protected boolean levelPassed = false;
    protected int levelScreenTime = 1000;

    public MainGame(){
        setFocusable(true);
        loadLevel(level);
        //thêm sự kiện bàn phím
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                //nguyên tắc là giảm hoặc tăng toạ độ của nhân vật
                if (!GameOver){
                    player.keyPressed(e, map, MOTION_DELAY);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                //đặt lại trạng thái chuyển độ = 0 nghĩa là sẽ vẽ hình nhân vật đứng im
                if (!GameOver){
                player.keyReleased(e);
                }
            }
        });
        timer.start();
    }

    //tải các vật thế theo file lv(x).txt
    public void loadLevel(int level){
        try {
            BufferedReader bfr = new BufferedReader(new InputStreamReader(new FileInputStream("data/levels/lv" + level + ".txt"), "UTF-8"));
            String temporary;
            int n = 0;
            int k = 0;
            map.data.clear();
            ballooms.clear();
            oneals.clear();
            bricks.clear();
            buff.listItems.clear();
            if (player != null) {
                /*int x = player.bombs.size();
                player.bombQuantity += x;
                player.bombs.clear();*/

            }
            portal.clear();
            //lần lượt đọc từng dòng của file lv
            while ((temporary = bfr.readLine()) != null){
                n = temporary.length();
                for(int i = 0;i < n; i++){
                    if (temporary.charAt(i) == ' ') map.data.add(new xOy(i*48,k*48, ' ')); //thêm toạ độ của grass
                    if (temporary.charAt(i) == '*') {
                        map.data.add(new xOy(i*48,k*48, '*')); //thêm toạ độ của brick
                        bricks.add(new brick(i,k));
                    }
                    if (temporary.charAt(i) == '#') map.data.add(new xOy(i*48,k*48, '#')); //thêm toạ độ của wall
                    if (temporary.charAt(i) == 'p') {
                        if (player == null){
                            player = new Bomber(i*48 + 12,k*48 + 3); //thêm toạ độ của nhân vật
                        }else{
                            player.bX = i*48 + 12;
                            player.bY = k*48 + 3;
                            player.direction = 2;
                            player.bombEffectRange = 1;
                            player.bombQuantity = 1;
                            player.speed = 3;
                        }
                        map.data.add(new xOy(i*48,k*48, ' ')); //thêm toạ độ của grass
                    }
                    if (temporary.charAt(i) == '1'){
                        ballooms.add(new balloom(i*48,k*48));
                        map.data.add(new xOy(i*48,k*48, ' '));
                    }
                    if (temporary.charAt(i) == '2'){
                        oneals.add(new oneal(i*48,k*48));
                        map.data.add(new xOy(i*48,k*48, ' '));
                    }
                    if (temporary.charAt(i) == 's'){
                        buff.listItems.add(new xOy(i*48 + 1, k*48 + 1, 's'));
                        map.data.add(new xOy(i*48,k*48, '*'));
                        bricks.add(new brick(i,k));
                    }
                    if (temporary.charAt(i) == 'b'){
                        buff.listItems.add(new xOy(i*48 + 1, k*48 + 1, 'b'));
                        map.data.add(new xOy(i*48,k*48, '*'));
                        bricks.add(new brick(i,k));
                    }
                    if (temporary.charAt(i) == 'f'){
                        buff.listItems.add(new xOy(i*48 + 1, k*48 + 1, 'f'));
                        map.data.add(new xOy(i*48,k*48, '*'));
                        bricks.add(new brick(i,k));
                    }
                    if (temporary.charAt(i) == 'x'){
                        portal.setXY(i*48 + 1, k*48 + 1);
                        map.data.add(new xOy(i*48,k*48, '*'));
                        bricks.add(new brick(i,k));
                    }
                }
                k++;
            }
            //tính chiều rộng và dài của panel dựa vào kích thước của bản đồ
            map.width = n;
            map.height = k;
            this.setSize(map.width*48, map.height*48);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void checkGameOver(){
        Rectangle bomberBound = new Rectangle(player.bX, player.bY, player.bomberStatus.width, player.bomberStatus.height);
        for(balloom bl : ballooms){
            if (!bl.isDead && bomberBound.intersects(new Rectangle(bl.bX, bl.bY, bl.left_1.width, bl.left_1.height))) {
                player.motionStatus = 0;
                player.isDead = true;
            }
        }
        for(oneal ol : oneals){
            if (!ol.isDead && bomberBound.intersects(new Rectangle(ol.bX, ol.bY, ol.left_1.width, ol.left_1.height))) {
                player.motionStatus = 0;
                player.isDead = true;
            }
        }
    }

    private boolean isClearEnermy(){
        return ballooms.size() == 0 && oneals.size() == 0;
    }

    // vẽ 1 vật thể và loại bỏ màu nền
    private void draw(Graphics g, Sprite sprite, int x, int y){
        for(int i = 0; i < sprite.width; i++) {
            for (int j = 0; j < sprite.height; j++) {
                if (sprite._pixels[j * sprite.width + i] != -65281) {
                    g.setColor(new Color(sprite._pixels[j * sprite.width + i]));
                    g.drawLine(i+x, j+y, i+x, j+y);
                }
            }
        }
    }

    private void drawPlayer(Graphics g){
        int motion;
        if (!player.isDead){
            motion = player.motionStatus % MOTION_DELAY;
            if (player.direction == 1){
                if (player.motionStatus == 0) {
                    player.bomberStatus = player.up_1;
                }
                else if (motion < MOTION_DELAY/2) {
                    player.bomberStatus = player.up_2;
                }
                else if (motion >= MOTION_DELAY/2) {
                    player.bomberStatus = player.up_3;
                }
            }else if (player.direction == 2){
                if (player.motionStatus == 0) {
                    player.bomberStatus = player.down_1;
                }
                else if (motion < MOTION_DELAY/2) {
                    player.bomberStatus = player.down_2;
                }
                else if (motion >= MOTION_DELAY/2) {
                    player.bomberStatus = player.down_3;
                }
            }else if (player.direction == 3){
                if (player.motionStatus == 0) {
                    player.bomberStatus = player.left_1;
                }
                else if (motion <= MOTION_DELAY/4) {
                    player.bomberStatus = player.left_2;
                }
                else if (MOTION_DELAY/4 < motion && motion <= 2*MOTION_DELAY/4) {
                    player.bomberStatus = player.left_1;
                }
                else if (2*MOTION_DELAY/4 < motion && motion <= 3*MOTION_DELAY/4) {
                    player.bomberStatus = player.left_3;
                }
                else if (motion > 3*MOTION_DELAY/4) {
                    player.bomberStatus = player.left_1;
                }
            }else if (player.direction == 4){
                if (player.motionStatus == 0) {
                    player.bomberStatus = player.right_1;
                }
                else if (motion <= MOTION_DELAY/4) {
                    player.bomberStatus = player.right_2;
                }
                else if (MOTION_DELAY/4 < motion && motion <= 2*MOTION_DELAY/4) {
                    player.bomberStatus = player.right_1;
                }
                else if (2*MOTION_DELAY/4 < motion && motion <= 3*MOTION_DELAY/4) {
                    player.bomberStatus = player.right_3;
                }
                else if (motion > 3*MOTION_DELAY/4) {
                    player.bomberStatus = player.right_1;
                }
            }
            draw(g, player.bomberStatus, player.bX, player.bY);
        }else{
            if (player.motionStatus == MOTION_DELAY) {
                drawGameOver(g);
                timer.stop();
            }
            if (player.motionStatus <= MOTION_DELAY/4){
                draw(g, player.dead_1, player.bX, player.bY);
            }
            else if (player.motionStatus/4 < MOTION_DELAY && player.motionStatus <= 2*MOTION_DELAY/4){
                draw(g, player.dead_2, player.bX, player.bY);
            }
            else if (2*MOTION_DELAY/4 < player.motionStatus && player.motionStatus <= 3*MOTION_DELAY/4){
                draw(g, player.dead_3, player.bX, player.bY);
            }
            player.motionStatus++;
        }
    }

    private void drawBomb(Graphics g){
        int n = player.bombs.size();
        int range = player.bombEffectRange;
        for(int i = 0; i < n; i++){
            int x = player.bombs.get(i).x, y = player.bombs.get(i).y;
            int time = player.bombs.get(i).timeCountingDown;
            if (time == 0) player.bombs.get(i).exploded = true;
            if (!player.bombs.get(i).exploded){
            if ((100 < time && time <= 120) || (40 < time && time <= 60)){
                player.bombs.get(i).bombStatus = player.bombs.get(i).bomb_1;
            }else if ((80 < time && time <= 100) || (20 < time && time <= 40)){
                player.bombs.get(i).bombStatus = player.bombs.get(i).bomb_2;
            }if ((60 < time && time <= 80) || (0 < time && time <= 20)){
                    player.bombs.get(i).bombStatus = player.bombs.get(i).bomb_3;
            }
            draw(g, player.bombs.get(i).bombStatus, x + player.bombs.get(i).bombStatus.offset_X, y + player.bombs.get(i).bombStatus.offset_Y);
                player.bombs.get(i).timeCountingDown--;
            }else{
                int time1 = player.bombs.get(i).effectTimeOfFlame;
                if (time1 == 0){
                    player.bombs.remove(i);
                    map.data.get(map.width*(y/48) + (x/48)).type = ' ';
                    i--;
                    n--;
                    player.bombQuantity++;
                }else{
                    if (0 < time1 && time1 <= 2){
                        player.bombs.get(i).bomb_exploded_status = player.bombs.get(i).bomb_exploded_3;
                        player.bombs.get(i).explosion_horizontal_status = player.bombs.get(i).explosion_horizontal_3;
                        player.bombs.get(i).explosion_vertical_status = player.bombs.get(i).explosion_vertical_3;
                        player.bombs.get(i).explosion_horizontal_left_last_status = player.bombs.get(i).explosion_horizontal_left_last_3;
                        player.bombs.get(i).explosion_horizontal_right_last_status = player.bombs.get(i).explosion_horizontal_right_last_3;
                        player.bombs.get(i).explosion_vertical_top_last_status = player.bombs.get(i).explosion_vertical_top_last_3;
                        player.bombs.get(i).explosion_vertical_down_last_status = player.bombs.get(i).explosion_vertical_down_last_3;
                    }else if (2 < time1 && time1 <= 6){
                        player.bombs.get(i).bomb_exploded_status = player.bombs.get(i).bomb_exploded_2;
                        player.bombs.get(i).explosion_horizontal_status = player.bombs.get(i).explosion_horizontal_2;
                        player.bombs.get(i).explosion_vertical_status = player.bombs.get(i).explosion_vertical_2;
                        player.bombs.get(i).explosion_horizontal_left_last_status = player.bombs.get(i).explosion_horizontal_left_last_2;
                        player.bombs.get(i).explosion_horizontal_right_last_status = player.bombs.get(i).explosion_horizontal_right_last_2;
                        player.bombs.get(i).explosion_vertical_top_last_status = player.bombs.get(i).explosion_vertical_top_last_2;
                        player.bombs.get(i).explosion_vertical_down_last_status = player.bombs.get(i).explosion_vertical_down_last_2;
                    }else if (6 < time1 && time1 <= 20){
                        player.bombs.get(i).bomb_exploded_status = player.bombs.get(i).bomb_exploded_1;
                        player.bombs.get(i).explosion_horizontal_status = player.bombs.get(i).explosion_horizontal_1;
                        player.bombs.get(i).explosion_vertical_status = player.bombs.get(i).explosion_vertical_1;
                        player.bombs.get(i).explosion_horizontal_left_last_status = player.bombs.get(i).explosion_horizontal_left_last_1;
                        player.bombs.get(i).explosion_horizontal_right_last_status = player.bombs.get(i).explosion_horizontal_right_last_1;
                        player.bombs.get(i).explosion_vertical_top_last_status = player.bombs.get(i).explosion_vertical_top_last_1;
                        player.bombs.get(i).explosion_vertical_down_last_status = player.bombs.get(i).explosion_vertical_down_last_1;
                    }
                    //int east, west, north, south;
                    draw(g, player.bombs.get(i).bomb_exploded_status, x, y);
                    for(player.bombs.get(i).east = 1; player.bombs.get(i).east <= range-1; player.bombs.get(i).east++){
                        if (map.data.get(map.width*(y/48) + (x/48) + player.bombs.get(i).east).type == '#' || map.data.get(map.width*(y/48) + (x/48) + player.bombs.get(i).east).type == '*') break;
                        draw(g, player.bombs.get(i).explosion_horizontal_status, x + player.bombs.get(i).east*48, y);
                    }
                    for(player.bombs.get(i).west = 1; player.bombs.get(i).west <= range-1; player.bombs.get(i).west++){
                        if (map.data.get(map.width*(y/48) + (x/48) - player.bombs.get(i).west).type == '#' || map.data.get(map.width*(y/48) + (x/48) - player.bombs.get(i).west).type == '*') break;
                        draw(g, player.bombs.get(i).explosion_horizontal_status, x - player.bombs.get(i).west*48, y);
                    }
                    for(player.bombs.get(i).north = 1; player.bombs.get(i).north <= range-1; player.bombs.get(i).north++){
                        if (map.data.get(map.width*(y/48 - player.bombs.get(i).north) + (x/48)).type == '#' || map.data.get(map.width*(y/48 - player.bombs.get(i).north) + (x/48)).type == '*') break;
                        draw(g, player.bombs.get(i).explosion_vertical_status, x, y - player.bombs.get(i).north*48);
                    }
                    for(player.bombs.get(i).south = 1; player.bombs.get(i).south <= range-1; player.bombs.get(i).south++){
                        if (map.data.get(map.width*(y/48 + player.bombs.get(i).south) + (x/48)).type == '#' || map.data.get(map.width*(y/48 + player.bombs.get(i).south) + (x/48)).type == '*') break;
                        draw(g, player.bombs.get(i).explosion_vertical_status, x, y + player.bombs.get(i).south*48);
                    }
                    if (map.data.get(map.width*(y/48) + (x/48) + player.bombs.get(i).east).type != '#'){
                        draw(g, player.bombs.get(i).explosion_horizontal_right_last_status, x + player.bombs.get(i).east*48, y);
                        if (map.data.get(map.width*(y/48) + (x/48) + player.bombs.get(i).east).type == '*' && time1 == 3){
                            for(brick bks : bricks){
                                if (bks.x == (x/48) + player.bombs.get(i).east && bks.y == y/48){
                                    bks.isDestroyed = true;
                                    break;
                                }
                            }
                        }
                    }
                    if (map.data.get(map.width*(y/48) + (x/48) - player.bombs.get(i).west).type != '#'){
                        draw(g, player.bombs.get(i).explosion_horizontal_left_last_status, x - player.bombs.get(i).west*48, y);
                        if (map.data.get(map.width*(y/48) + (x/48) - player.bombs.get(i).west).type == '*' && time1 == 3){
                            for(brick bks : bricks){
                                if (bks.x == (x/48) - player.bombs.get(i).west && bks.y == y/48){
                                    bks.isDestroyed = true;
                                    break;
                                }
                            }
                        }
                    }
                    if (map.data.get(map.width*(y/48 + player.bombs.get(i).south) + (x/48)).type != '#'){
                        draw(g, player.bombs.get(i).explosion_vertical_down_last_status, x, y + player.bombs.get(i).south*48);
                        if (map.data.get(map.width*(y/48 + player.bombs.get(i).south) + (x/48)).type == '*' && time1 == 3){
                            for(brick bks : bricks){
                                if (bks.x == (x/48) && bks.y == y/48 + player.bombs.get(i).south){
                                    bks.isDestroyed = true;
                                    break;
                                }
                            }
                        }
                    }
                    if (map.data.get(map.width*(y/48 - player.bombs.get(i).north) + (x/48)).type != '#'){
                        draw(g, player.bombs.get(i).explosion_vertical_top_last_status, x, y - player.bombs.get(i).north*48);
                        if (map.data.get(map.width*(y/48 - player.bombs.get(i).north) + (x/48)).type == '*' && time1 == 3){
                            for(brick bks : bricks){
                                if (bks.x == (x/48) && bks.y == y/48 - player.bombs.get(i).north){
                                    bks.isDestroyed = true;
                                    break;
                                }
                            }
                        }
                    }
                    player.bombs.get(i).effectTimeOfFlame--;
                    player.bombs.get(i).flameCollision(this);
                }
            }
        }
    }

    private void drawBalloom(Graphics g){
        int motion;
        int n = ballooms.size();
        for(int i = 0; i < n; i++){
            if (!ballooms.get(i).isDead){
            motion = ballooms.get(i).motionStatus % MOTION_DELAY;
            if (ballooms.get(i).direction == 1 || ballooms.get(i).direction == 3){
                if (motion <= MOTION_DELAY/3){
                    ballooms.get(i).balloomStatus = ballooms.get(i).left_1;
                }
                else if (MOTION_DELAY/3 < motion && motion <= 2*MOTION_DELAY/3){
                    ballooms.get(i).balloomStatus = ballooms.get(i).left_2;
                }
                else if (motion > 2*MOTION_DELAY/3){
                    ballooms.get(i).balloomStatus = ballooms.get(i).left_3;
                }
            }

            if (ballooms.get(i).direction == 2 || ballooms.get(i).direction == 4){
                if (motion <= MOTION_DELAY/3){
                    ballooms.get(i).balloomStatus = ballooms.get(i).right_1;
                }
                else if (MOTION_DELAY/3 < motion && motion <= 2*MOTION_DELAY/3){
                    ballooms.get(i).balloomStatus = ballooms.get(i).right_2;
                }
                else if (motion > 2*MOTION_DELAY/3){
                    ballooms.get(i).balloomStatus = ballooms.get(i).right_3;
                }
            }
            draw(g, ballooms.get(i).balloomStatus, ballooms.get(i).bX + ballooms.get(i).balloomStatus.offset_X, ballooms.get(i).bY + ballooms.get(i).balloomStatus.offset_Y);
        }else{
                if (ballooms.get(i).motionStatus == MOTION_DELAY){
                    ballooms.remove(i);
                    i--;
                    n--;
                }else{
                    draw(g, ballooms.get(i).dead, ballooms.get(i).bX + ballooms.get(i).dead.offset_X, ballooms.get(i).bY + ballooms.get(i).dead.offset_Y);
                    ballooms.get(i).motionStatus++;
                }
            }
        }
    }

    private void drawOneal(Graphics g){
        int motion;
        int n = oneals.size();
        for(int i = 0; i < n; i++){
            if (!oneals.get(i).isDead){
            motion = oneals.get(i).motionStatus % MOTION_DELAY;
            if (oneals.get(i).direction == 1 || oneals.get(i).direction == 3){
                if (motion <= MOTION_DELAY/3){
                    oneals.get(i).onealStatus = oneals.get(i).left_1;
                }
                else if (MOTION_DELAY/3 < motion && motion <= 2*MOTION_DELAY/3){
                    oneals.get(i).onealStatus = oneals.get(i).left_2;
                }
                else if (motion > 2*MOTION_DELAY/3){
                    oneals.get(i).onealStatus = oneals.get(i).left_3;
                }
            }

            if (oneals.get(i).direction == 2 || oneals.get(i).direction == 4){
                if (motion <= MOTION_DELAY/3){
                    oneals.get(i).onealStatus = oneals.get(i).right_1;
                }
                else if (MOTION_DELAY/3 < motion && motion <= 2*MOTION_DELAY/3){
                    oneals.get(i).onealStatus = oneals.get(i).right_2;
                }
                else if (motion > 2*MOTION_DELAY/3){
                    oneals.get(i).onealStatus = oneals.get(i).right_3;
                }
            }
            draw(g, oneals.get(i).onealStatus, oneals.get(i).bX + oneals.get(i).onealStatus.offset_X, oneals.get(i).bY + oneals.get(i).onealStatus.offset_Y);
        }else{
                if (oneals.get(i).motionStatus == MOTION_DELAY){
                    oneals.remove(i);
                    i--;
                    n--;
                }else{
                    draw(g, oneals.get(i).dead, oneals.get(i).bX + oneals.get(i).dead.offset_X, oneals.get(i).bY + oneals.get(i).dead.offset_Y);
                    oneals.get(i).motionStatus++;
                }
            }
        }
    }

    private void drawBrick(Graphics g){
        int n = bricks.size();
        for(int i = 0; i < n; i++){
            int x = bricks.get(i).x, y = bricks.get(i).y;
            if (!bricks.get(i).isDestroyed){
                g.drawImage(bricks.get(i).image, x*48, y*48, this);
            }else{
                int time = bricks.get(i).timeAfterDestroyed;
                if (time == 0){
                    map.data.get(map.width*y + x).type = ' ';
                    bricks.remove(i);
                    i--;
                    n--;
                }else{
                    if (0 < time && time <= 6) bricks.get(i).brick_exploded_status = bricks.get(i).brick_exploded_3;
                    else if (6 < time && time <= 12) bricks.get(i).brick_exploded_status = bricks.get(i).brick_exploded_2;
                    else if (12 < time && time <= 18) bricks.get(i).brick_exploded_status = bricks.get(i).brick_exploded_1;
                    draw(g, bricks.get(i).brick_exploded_status, x*48, y*48);
                    bricks.get(i).timeAfterDestroyed--;
                }
            }
        }
    }

    private void drawBuffItems(Graphics g){
        int n = buff.listItems.size();
        for (int i = 0; i < n; i++){
            if (buff.listItems.get(i).type == 's'){
                g.drawImage(buff.speed, buff.listItems.get(i).x, buff.listItems.get(i).y, this);
            }else if (buff.listItems.get(i).type == 'f'){
                g.drawImage(buff.powerup_flames, buff.listItems.get(i).x, buff.listItems.get(i).y, this);
            }else if (buff.listItems.get(i).type == 'b'){
                g.drawImage(buff.powerup_bombs, buff.listItems.get(i).x, buff.listItems.get(i).y, this);
            }
        }
    }

    public void drawCenteredString(String s, int w, int h, Graphics g) {
        FontMetrics fm = g.getFontMetrics();
        int x = (w - fm.stringWidth(s)) / 2;
        int y = (fm.getAscent() + (h - (fm.getAscent() + fm.getDescent())) / 2);
        g.drawString(s, x, y);
    }

    public void drawChangeLevel(Graphics g, int level) {
        g.setColor(Color.black);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        Font font = new Font("Arial", Font.PLAIN, 30);
        g.setFont(font);
        g.setColor(Color.white);
        drawCenteredString("LEVEL " + level, this.getWidth(), this.getHeight(), g);
    }

    public void drawGameOver(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        Font font = new Font("Arial", Font.PLAIN, 30);
        g.setFont(font);
        g.setColor(Color.white);
        drawCenteredString("GAME OVER", this.getWidth(), this.getHeight(), g);
    }

    public void setInfoBar(infoBar bar){
        this.ifBar = bar;
    }

    public void checkLevelPassed(){
        levelPassed = portal.getBound().intersects(new Rectangle(player.bX, player.bY, player.bomberStatus.width, player.bomberStatus.height)) && isClearEnermy();
    }

    //vẽ toàn bộ các vật thể
    private void drawGame(Graphics g){
        for(xOy xoy : map.data){
            if (xoy.type == '#') g.drawImage(wall, xoy.x, xoy.y, this);
             else if (xoy.type == ' ' || xoy.type == '*' || xoy.type == 'b') g.drawImage(grass, xoy.x, xoy.y, this);
        }
        g.drawImage(portal.image, portal.x, portal.y, this);
        drawBuffItems(g);
        drawBrick(g);
        drawBomb(g);
        drawBalloom(g);
        drawOneal(g);
        drawPlayer(g);
    }

    @Override
    protected void paintComponent(Graphics g){
        if (levelScreenTime > 0){
            drawChangeLevel(g, level);
            levelScreenTime -= 30;
        }else{
            timeRemain -= 30;
            if (timeRemain < 0){
                drawGameOver(g);
                timer.stop();
            }else {
                ifBar.setTime(timeRemain);
                if (!levelPassed) {
                    drawGame(g);
                    checkLevelPassed();
                    if (levelPassed) {
                        levelScreenTime = 1000;
                        level++;
                    }
                }else{
                    loadLevel(level);
                    timeRemain = 200000;
                    ifBar.setLevel(level);
                    levelPassed = false;
                }
            }
        }
    }
    @Override
    public void actionPerformed(ActionEvent e){
        if (!player.isDead) checkGameOver();
        if (!player.isDead){
            player.move(map, MOTION_DELAY);
            player.takeBuffItems(buff);
            for(balloom bl : ballooms){
                bl.move(map);
            }
            for(oneal ol : oneals){
                ol.move(player, map);
            }
        }
        repaint();
    }
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(this.getWidth(), this.getHeight());
    }
}