import java.awt.*;
import javax.swing.*;

// Tạo ra khung để chưa panel (panel là nơi vẽ các ảnh của game)
public class GameFrame extends JFrame {

    public MainGame mg = new MainGame();
    public infoBar bar;

    public GameFrame() {
        initUI();
    }

    private void initUI() {
        bar = new infoBar(mg);
        mg.setInfoBar(bar);
        add(bar, BorderLayout.PAGE_START);
        add(mg);
        setTitle("Bomber");
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {

                new GameFrame();
            }
        });
    }
}