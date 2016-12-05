import java.awt.*;
public class Astronaut{
  private int xVelocity = 0;
  private int yVelocity = 0;
  private int direction = 0;
  private Rectangle body;

  public Astronaut(){
    body = new Rectangle();
    body.x = 390;
    body.y = 360;
    body.width = 20;
    body.height = 40;
  }
  
  public Astronaut(int x1, int y1, int w, int h){
    body = new Rectangle();
    body.x = x1;
    body.y = y1;
    body.width = w;
    body.height = h;
  }

  public void draw(Graphics2D g){
    g.fill(body);
  }

  public int getDirection(){
    return direction;
  }

  public Rectangle getBody(){
    return body;
  }

  public void setDirection(int d){
    direction = d;
  }

  public void move(){
    if(direction == 1){
      if(xVelocity < 8){
        xVelocity++;
      }
    } 

    if(direction == -1){
      if(xVelocity > -8){
        xVelocity--;
      }
    } 
    if(direction == 0){
      if(xVelocity < 0){
        xVelocity++;
      } else if(xVelocity > 0){
        xVelocity--;
      }
    }
    body.x += xVelocity;
    if(body.y < 360){
      body.y -= yVelocity;
      yVelocity--;
    }

    if(body.x < 0){
      body.x = 0;
    } else if(body.x > 780){
      body.x = 780;
    }

    if(body.y > 360){
      body.y = 360;
    }
  }

  public void jump(){
    if(body.y == 360){
      yVelocity = 15;
      body.y -= 15;
    }
  }

  public int getY()
  {
    // TODO Auto-generated method stub
    return body.y;
  }
  
  public int getX(){
    return body.x;
  }

}
