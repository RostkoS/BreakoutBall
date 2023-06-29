import javax.swing.*;
import java.awt.*;

public class Main extends JFrame{
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            Main ex = new Main();
            ex.setVisible(true);
        });

    }
    public Main() {
        initUI(); //initializing UI
    }

    private void initUI() {
        GamePlay gamePlay = new GamePlay();
        setBounds(130, 120, 700, 600);
        setTitle("Breakout Ball");
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(gamePlay);
    }
}