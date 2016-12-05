import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

public class EscapeFromRectangulus extends JPanel implements KeyListener,
ActionListener {

  // DECLARE ALL INSTANCE VARIABLES HERE..

  private ArrayList<Rectangle> aliens = new ArrayList<Rectangle>();
  private int counter = 0;
  private int counter2 = 0;
  private Astronaut jon = new Astronaut();
  private Rectangle drop = null;
  private Rectangle drop2 = null;
  private Rectangle drop3 = null;
  private ArrayList<Rectangle> laser = new ArrayList<Rectangle>();
  public static final Rectangle BORDER = new Rectangle(0, 0, 800, 400);
  private int frameCount;// used for the score
  private int highscore = 0;
  private String title = "~~ Escape From Rectangulus ~~  .. (click here first) .. "
      + "CONTROLS: arrows = move, space = jump, r = restart, highscore = " + highscore;
  private Timer timer;// handles animation
  private static Image offScreenBuffer;// needed for double buffering graphics
  private Graphics offScreenGraphics;// needed for double buffering graphics

  /**
   * main() is needed to initialize the window.<br>
   * THIS METHOD SHOULD NOT BE MODIFIED! .. <br>
   * you should write all necessary initialization code in initRound()
   */
  public static void main(String[] args) {
    JFrame window = new JFrame("Escape From Rectangulus");
    window.setBounds(100, 100, 807, 428);
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    window.setResizable(false);

    EscapeFromRectangulus game = new EscapeFromRectangulus();
    game.setBackground(Color.WHITE);
    window.getContentPane().add(game);
    window.setVisible(true);
    game.init();
    window.addKeyListener(game);
  }

  /**
   * init method needed to initialize non-static fields<br>
   * THIS METHOD SHOULD NOT BE MODIFIED! ..
   */
  public void init() {
    offScreenBuffer = createImage(getWidth(), getHeight());// should be 800x400
    offScreenGraphics = offScreenBuffer.getGraphics();
    timer = new Timer(30, this);
    // timer fires every 30 milliseconds.. invokes method actionPerformed()

    initRound();
  }

  /**
   * initializes all fields needed for each round of play (i.e. restart)
   */
  public void initRound() {
    frameCount = 0;
    jon = new Astronaut();
    aliens = new ArrayList<Rectangle>();
    drop = null;
    drop2 = null;
    drop3 = null;
    counter = 0;
    counter2 = 0;
    laser = new ArrayList<Rectangle>();
  }

  /**
   * Called automatically after a repaint request<br>
   * THIS METHOD SHOULD NOT BE MODIFIED! ..
   */
  public void paint(Graphics g) {
    if(offScreenGraphics == null)
      return;

    draw((Graphics2D) offScreenGraphics);
    g.drawImage(offScreenBuffer, 0, 0, this);
  }

  /**
   * renders all objects to Graphics g
   */
  public void draw(Graphics2D g) {
    super.paint(g);// refresh the background
    g.setColor(Color.BLACK);
    g.drawString(title, 100, 20);// draw the title towards the top
    g.drawRect(BORDER.x, BORDER.y, BORDER.width - 1, BORDER.height - 1);
    g.drawString("score:  " + frameCount, 380, 100);// approximate middle
    jon.draw(g);
    for(int r = 0; r < aliens.size(); r++){
      g.draw(aliens.get(r));
    }
    
    if(drop != null){
      g.setColor(Color.RED);
      g.draw(drop);
      String dropNotice = "A fellow Astronaut!";
      g.setColor(Color.BLACK);
      g.drawString(dropNotice, 100, 40);
    }
    
    if(drop2 != null){
      g.setColor(Color.GREEN);
      g.draw(drop2);
      String dropNotice2 = "Time warp - Activate!";
      g.setColor(Color.BLACK);
      g.drawString(dropNotice2, 200, 40);
    }
    
    if(drop3 != null){
      g.setColor(Color.BLUE);
      g.draw(drop3);
      String dropNotice3 = "Laser, Activate!";
      g.setColor(Color.BLACK);
      g.drawString(dropNotice3, 400, 40);
    }
    
    for(int q = 0; q < laser.size(); q++){
      g.setColor(Color.BLUE);
      g.draw(laser.get(q));
    }
  }

  /**
   * Called automatically when the timer fires<br>
   * this is where all the game fields will be updated
   */
  public void actionPerformed(ActionEvent e) {

    jon.move();

    // Simple AI: Each frame there is a 5% chance of creating an alien Rectangle
    // with a random position and width. (note: its width is also its velocity)
    if (Math.random() < 0.05) {
      aliens.add(new Rectangle(BORDER.width, 360 - (int) (Math.random() * 40), (int) (Math.random() * 15 + 1), 20));
    }
    
    if(Math.random() < 0.001) {
      drop = new Rectangle(390, 360, 20, 20);
    }
    
    if(Math.random() < 0.003){
      drop2 = new Rectangle(350, 360, 20, 20);
    }
    
    if(Math.random() < 0.005){
      drop3 = new Rectangle(310, 360, 20, 20);
    }
    
    if(drop != null && jon.getBody().intersects(drop)){
      aliens.clear();
      drop = null;        
    }    
    
    if(drop2 != null && jon.getBody().intersects(drop2)){
      counter = 90;
      drop2 = null;
    }
    
    if(drop3 != null && jon.getBody().intersects(drop3)){
      counter2 = 150;
      drop3 = null;
    }
    
    if(counter > 0) {
      counter--;
    }
    
    if(counter2 > 0) {
      counter2--;
    }

    for(int x = 0; x < aliens.size(); x++){
      if(counter == 0){
        aliens.get(x).x -= aliens.get(x).width;
      } else {
        aliens.get(x).x -= aliens.get(x).width/2;
      }
      if(counter2 == 0){
        laser.clear();
      }
      if(aliens.get(0).x < 0){
        aliens.remove(0);
      } else if(jon.getBody().intersects(aliens.get(x))){
        timer.stop();
        if(frameCount > highscore){
          highscore = frameCount;
          title = "~~ Escape From Rectangulus ~~  .. (click here first) .. "
              + "CONTROLS: arrows = move, space = jump, r = restart, highscore = " + highscore;
        }
      }
    }

    frameCount++;// used for the score
    repaint();// needed to refresh the animation
  }
  

  /**
   * handles any key pressed events and updates the player's direction by
   * setting their direction to either 1 or -1 for right or left respectively
   * and updates their jumping status by invoking jump()
   */
  public void keyPressed(KeyEvent e) {
    int keyCode = e.getKeyCode();
    if (keyCode == KeyEvent.VK_LEFT) {
      jon.setDirection(-1);
    } else if (keyCode == KeyEvent.VK_RIGHT) {
      jon.setDirection(1);
    } else if (keyCode == KeyEvent.VK_SPACE) {
      jon.jump();
    } else if(keyCode == KeyEvent.VK_X && counter2 > 0){
      laser.add(new Rectangle (jon.getX(), jon.getY(), 20, 5));
      for(int s = 0; s < laser.size(); s++){
        if(counter2 == 0){
          laser.clear();
        }
        if(counter2 != 0 && laser.get(s).x < 708){
          laser.get(s).x  += laser.get(s).width;
          for(int d = 0; d < aliens.size(); d++){
            if(laser.get(s).intersects(aliens.get(d))){
              aliens.remove(d);
              laser.remove(s);
              break;
            }
          }
        }
      }
    }
  }

  /**
   * handles any key released events and updates the player's direction by
   * setting their direction to 0 if they need to stop and restarts the game if
   * the Timer is not running and user types 'r'
   */
  public void keyReleased(KeyEvent e) {
    int keyCode = e.getKeyCode();
    if (keyCode == KeyEvent.VK_LEFT  && jon.getDirection() == -1) {
      jon.setDirection(0);
    } else if (keyCode == KeyEvent.VK_RIGHT && jon.getDirection() == 1) {
      jon.setDirection(0);
    } else if (keyCode == KeyEvent.VK_R) {
      if (!timer.isRunning()) {
        timer.start();
        initRound();
      }
    }
  }

  /**
   * this method is needed for implementing interface KeyListener<br>
   * THIS METHOD SHOULD NOT BE MODIFIED! ..
   */
  public void keyTyped(KeyEvent e) {
  }

}// end class EscapeFromRectangulus
