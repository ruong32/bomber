import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;

//Dữ liệu ảnh

public class Sprite {
    private String _path; //đường dẫn đến ảnh
    protected int[] _pixels; //mảng lưu các màu của điểm ảnh của ảnh
    protected int width; // chiều rộng của ảnh
    protected int height; // chiều cao của ảnh
    protected int offset_X, offset_Y;

    public Sprite(String path){
        _path = path;
        load();
    }

    //tải ảnh từ file và lưu vào mảng pixels
    private void load() {
        try {
            BufferedImage image = ImageIO.read(new FileInputStream(_path));
            this.width = image.getWidth();
            this.height = image.getHeight();
            offset_X = (48 - this.width)/2;
            offset_Y = (48 - this.height)/2;
            _pixels = new int[width*height];
            image.getRGB(0, 0, width, height, _pixels, 0, width);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
}