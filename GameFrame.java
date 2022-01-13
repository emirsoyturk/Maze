import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame{
  public static GamePanel gamePanel;
  GameFrame(){
    gamePanel = new GamePanel();
    add(gamePanel);
    setTitle("Maze");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setResizable(false);
    pack();
    setVisible(true);
    setLocationRelativeTo(null);
  }
}
