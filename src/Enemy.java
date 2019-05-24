import java.util.Random;

public class Enemy {
    protected int bX, bY; // toạ độ của nhân vật
    protected int  speed = 1; // tốc độ di chuyển
    protected int motionStatus = 0; //quyết định hình của nhân vật sẽ đc vẽ lên panel
    protected int direction = 1; // hướng di chuyển
    protected Random random = new Random();

    public Enemy(int x, int y){
        bX = x;
        bY = y;
    }
}