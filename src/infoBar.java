import javax.swing.*;
import java.awt.*;

public class infoBar extends JPanel {
    protected JLabel time, level;
    public infoBar(MainGame mainGame){
        setPreferredSize(new Dimension(0, 40));
        setVisible(true);
        setBackground(Color.BLACK);
        time = new JLabel("Time: " + mainGame.timeRemain/1000);
        time.setForeground(Color.white);
        time.setHorizontalAlignment(JLabel.CENTER);

        level = new JLabel("Level: " + mainGame.level );
        level.setForeground(Color.white);
        level.setHorizontalAlignment(JLabel.CENTER);

        setLayout(new GridLayout());

        add(time);
        add(level);
    }

    public void setTime(int time){
        this.time.setText("Time: " + time/1000);
    }

    public void setLevel(int lv){
        this.level.setText("Level: " + lv);
    }
}