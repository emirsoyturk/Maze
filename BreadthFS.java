import java.awt.Point;
import java.util.LinkedList;

public class BreadthFS implements Runnable {

  private int table [][];
  private GamePanel gamePanel;
  private int BLACK;
  public void run(){
    table = GameFrame.gamePanel.table;
    BLACK = GameFrame.gamePanel.getBLACK();
    breadthFS();
  }


  private void breadthFS(){
    boolean visited[][] = new boolean[table.length][table[0].length];

    LinkedList<Point> queue = new LinkedList<>();
    Point curr = GameFrame.gamePanel.getCenter();
    visited[curr.x][curr.y] = true;

    queue.add(new Point(curr.x, curr.y));

    while(!queue.isEmpty()){
      curr = queue.poll();
      if(table[curr.x][curr.y] == 3){//FOUND
        return;
      }
      try{
        Thread.sleep(1);
      }catch(Exception ignored){}
      GameFrame.gamePanel.repaint();
      table[curr.x][curr.y] = 2;

      if(curr.x < table.length-1 && !visited[curr.x+1][curr.y] && table[curr.x+1][curr.y] != BLACK) {
        visited[curr.x+1][curr.y] = true;
        queue.add(new Point(curr.x+1, curr.y));
      }
      if(curr.x > 0 && !visited[curr.x-1][curr.y] && table[curr.x-1][curr.y] != BLACK) {
        visited[curr.x-1][curr.y] = true;
        queue.add(new Point(curr.x-1, curr.y));
      }
      if(curr.y < table[0].length-1 && !visited[curr.x][curr.y+1] && table[curr.x][curr.y+1] != BLACK) {
        visited[curr.x][curr.y+1] = true;
        queue.add(new Point(curr.x, curr.y+1));
      }
      if(curr.y > 0 && !visited[curr.x][curr.y-1] && table[curr.x][curr.y-1] != BLACK) {
        visited[curr.x][curr.y-1] = true;
        queue.add(new Point(curr.x, curr.y-1));
      }
    }
  }
}
