import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.ArrayList;
import java.awt.event.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.concurrent.TimeUnit;

public class GamePanel extends JPanel implements MouseListener{

  public final int WIDTH = 900;
  public final int HEIGHT = 900;
  private static final int cellSize = 10;
  public int table [][] = new int[HEIGHT/cellSize][WIDTH/cellSize];
  private Point center = new Point(HEIGHT/(2*cellSize), WIDTH/(2*cellSize));
  private final int WHITE = 0;
  private final int BLACK = 1;
  private final int GREEN = 2;
  private final int RED = 3;
  private final int BLUE = 4;
  GamePanel(){
    addMouseListener(this);
    setFocusable(true);
    requestFocusInWindow();
    setPreferredSize(new Dimension(HEIGHT, WIDTH));
    newGame();
  }

  public static int getCellSize(){ return cellSize; }
  public Point getCenter(){ return center; }
  public int getBLACK(){ return BLACK; }

  @Override
  protected void paintComponent(Graphics g){
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    for(int x=0; x<table.length; x++){
      for(int y=0; y<table[0].length; y++){
        int cell = table[x][y];
        switch(cell){
          case WHITE: g2d.setColor(Color.WHITE); break;
          case BLACK: g2d.setColor(Color.BLACK); break;
          case GREEN: g2d.setColor(Color.GREEN); break;
          case RED: g2d.setColor(Color.RED); break;
          case BLUE: g2d.setColor(Color.BLUE); break;
          default : continue;
        }
        g2d.fillRect(x*cellSize, y*cellSize, cellSize, cellSize);
      }
    }
  }

  private void createTable(){
    for(int x=0; x<table.length; x++){
      for(int y=0; y<table[0].length; y++){
        table[x][y] = BLACK;
      }
    }
  }
  public void newGame(){
    createTable();
    createMazeNew();
    new Thread(new BreadthFS()).start();
    repaint();
  }

  public void createMazeNew(){
    long start = System.currentTimeMillis();
    LinkedList<Point> shuffled = shuffleOrder();
    ArrayList<Point> paths = new ArrayList<>();
    boolean placed [][] = new boolean[table.length][table[0].length];
    int currentWallNumber = 0;
    int totalWallNumber = 5000;
    for(int i=0; i<totalWallNumber; i++){
      Point curr = shuffled.poll();
      shuffled.add(curr);
      currentWallNumber++;
      placed[curr.x][curr.y] = true;
      table[curr.x][curr.y] = BLACK;

      if(curr == center || !mapIsValidControl(placed, currentWallNumber)){
        currentWallNumber--;
        placed[curr.x][curr.y] = false;
        table[curr.x][curr.y] = WHITE;
        paths.add(curr);
        i--;
      }
    }
    Point randomLoc = paths.get((int)(Math.random()*paths.size()));
    Point escape = randomLoc;
    table[escape.x][escape.y] = RED;

  }
  public LinkedList<Point> shuffleOrder(){
    LinkedList<Point> shuffled = new LinkedList<>();

    for(int i=0; i<table.length; i++){
      for(int j=0; j<table[0].length; j++){
        shuffled.add(new Point(i, j));
      }
    }
    for(int i=0; i<shuffled.size(); i++){
      int randomNum = (int)(Math.random()*(shuffled.size()-i));
      Point temp = shuffled.get(randomNum+i);
      shuffled.set(randomNum+i, shuffled.get(i));
      shuffled.set(i, temp);
    }

    return shuffled;
  }
  public boolean mapIsValidControl(boolean placed[][], int wallNumber){
    boolean [][] visited = new boolean[table.length][table[0].length];
    LinkedList<Point> queue = new LinkedList<>();

    queue.add(center);
    int currentPathNumber = 0;
    while(!queue.isEmpty()){
      Point curr = queue.poll();

      if(curr.x != table.length-1 && !visited[curr.x+1][curr.y] && !placed[curr.x+1][curr.y] && table[curr.x+1][curr.y] != BLACK) {
        visited[curr.x+1][curr.y] = true;
        queue.add(new Point(curr.x+1, curr.y));
        currentPathNumber++;
      }
      if(curr.x != 0 && !visited[curr.x-1][curr.y] && !placed[curr.x-1][curr.y] && table[curr.x-1][curr.y] != BLACK) {
        visited[curr.x-1][curr.y] = true;
        queue.add(new Point(curr.x-1, curr.y));
        currentPathNumber++;
      }
      if(curr.y != table[0].length-1 && !visited[curr.x][curr.y+1] && !placed[curr.x][curr.y+1] && table[curr.x][curr.y+1] != BLACK) {
        visited[curr.x][curr.y+1] = true;
        queue.add(new Point(curr.x, curr.y+1));
        currentPathNumber++;
      }
      if(curr.y != 0 && !visited[curr.x][curr.y-1] && !placed[curr.x][curr.y-1] && table[curr.x][curr.y-1] != BLACK) {
        visited[curr.x][curr.y-1] = true;
        queue.add(new Point(curr.x, curr.y-1));
        currentPathNumber++;
      }
    }
    return wallNumber == table.length * table[0].length - currentPathNumber;
  }
  public void mouseClicked(MouseEvent e) {
    newGame();
  }
  public void mouseEntered(MouseEvent e) { }
  public void mouseExited(MouseEvent e) { }
  public void mousePressed(MouseEvent e) { }
  public void mouseReleased(MouseEvent e) { }
}
