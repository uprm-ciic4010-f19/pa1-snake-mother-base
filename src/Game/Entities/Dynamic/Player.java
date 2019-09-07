package Game.Entities.Dynamic;

import Main.Handler;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

import Game.GameStates.State;

/**
 * Created by AlexVR on 7/2/2018.
 */
public class Player {

    public int lenght;
    public boolean justAte;
    private Handler handler;

    public int xCoord;
    public int yCoord;
    public static double currScore = 0;
    
    public int moveCounter;

    public String direction;//is your first name one?

    public Player(Handler handler){
        this.handler = handler;
        xCoord = 0;
        yCoord = 0;
        moveCounter = 0;
        direction= "Right";
        justAte = false;
        lenght= 1;

    }

    public void tick(){
        moveCounter++;
        if(moveCounter>=5) {
            checkCollisionAndMove();
            moveCounter=0;
        }
        if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_UP) && direction != "Down") {
			direction = "Up";
		}
		if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_DOWN) && direction != "Up") {
			direction = "Down";
		}
		if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_LEFT) && direction != "Right") {
			direction = "Left";
		}
		if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_RIGHT) && direction != "Left") {
				direction = "Right";
        }
         if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_N)){
        	 Eat();
        	 handler.getWorld().appleOnBoard=true;
         }
         if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_ESCAPE)) {
        	 State.setState(handler.getGame().pauseState);
         }
    }

    public void checkCollisionAndMove(){
        handler.getWorld().playerLocation[xCoord][yCoord]=false;
        int x = xCoord;
        int y = yCoord;
        switch (direction){
            case "Left":
                if(xCoord==0){
                    kill();
                }else{
                    xCoord--;
                }
                break;
            case "Right":
                if(xCoord==handler.getWorld().GridWidthHeightPixelCount-1){
                    kill();
                }else{
                    xCoord++;
                }
                break;
            case "Up":
                if(yCoord==0){
                    kill();
                }else{
                    yCoord--;
                }
                break;
            case "Down":
                if(yCoord==handler.getWorld().GridWidthHeightPixelCount-1){
                    kill();
                }else{
                    yCoord++;
                }
                break;
        }
        handler.getWorld().playerLocation[xCoord][yCoord]=true;


        if(handler.getWorld().appleLocation[xCoord][yCoord]){
            Eat();
            currScore = Math.sqrt(2 * currScore + 1);
        }

        if(!handler.getWorld().body.isEmpty()) {
            handler.getWorld().playerLocation[handler.getWorld().body.getLast().x][handler.getWorld().body.getLast().y] = false;
            handler.getWorld().body.removeLast();
            handler.getWorld().body.addFirst(new Tail(x, y,handler));
        }

    }

    public void render(Graphics g,Boolean[][] playeLocation){
        Random r = new Random();
        int colorCode = 0;
        for (int i = 0; i < handler.getWorld().GridWidthHeightPixelCount; i++) {
        	if (colorCode == 0) {
        		g.setColor(Color.green);
        		colorCode = colorCode + 1 ;
        	} else if (colorCode == 1) {
        		g.setColor(Color.blue);
        		colorCode = colorCode + 1 ;
        	} else if (colorCode == 2) {
             	g.setColor(Color.cyan);
             	colorCode = colorCode + 1 ;
        	} else if (colorCode == 3) {
                g.setColor(Color.pink);
                colorCode = colorCode + 1 ;
            } else if (colorCode == 4) {
                g.setColor(Color.magenta);
                colorCode = colorCode + 1 ;
            } else if (colorCode == 5) {
                g.setColor(Color.red);
                colorCode = colorCode + 1 ;
            } else if (colorCode == 6) {
                g.setColor(Color.orange);
                colorCode = colorCode + 1 ;
            } else if (colorCode == 7) {
                g.setColor(Color.yellow);
                colorCode = colorCode - 7 ;
            }
        	for (int j = 0; j < handler.getWorld().GridWidthHeightPixelCount; j++) {
        		if (colorCode == 0) {
            		g.setColor(Color.green);
            		colorCode = colorCode + 1 ;
            	} else if (colorCode == 1) {
            		g.setColor(Color.blue);
            		colorCode = colorCode + 1 ;
            	} else if (colorCode == 2) {
                 	g.setColor(Color.cyan);
                 	colorCode = colorCode + 1 ;
            	} else if (colorCode == 3) {
                    g.setColor(Color.pink);
                    colorCode = colorCode + 1 ;
                } else if (colorCode == 4) {
                    g.setColor(Color.magenta);
                    colorCode = colorCode + 1 ;
                } else if (colorCode == 5) {
                    g.setColor(Color.red);
                    colorCode = colorCode + 1 ;
                } else if (colorCode == 6) {
                    g.setColor(Color.orange);
                    colorCode = colorCode + 1 ;
                } else if (colorCode == 7) {
                    g.setColor(Color.yellow);
                    colorCode = colorCode - 7 ;
                }
        		//g.setColor(Color.green);

                if(playeLocation[i][j]||handler.getWorld().appleLocation[i][j]){
                    g.fillRect((i*handler.getWorld().GridPixelsize),
                            (j*handler.getWorld().GridPixelsize),
                            handler.getWorld().GridPixelsize,
                            handler.getWorld().GridPixelsize);
                    
           
        		   
                }

            }
        }

        g.drawString(currScore+"", 1, 10);
    }

    public void Eat(){
        lenght++;
        Tail tail= null;
        handler.getWorld().appleLocation[xCoord][yCoord]=false;
        handler.getWorld().appleOnBoard=false;
        switch (direction){
            case "Left":
                if( handler.getWorld().body.isEmpty()){
                    if(this.xCoord!=handler.getWorld().GridWidthHeightPixelCount-1){
                        tail = new Tail(this.xCoord+1,this.yCoord,handler);
                    }else{
                        if(this.yCoord!=0){
                            tail = new Tail(this.xCoord,this.yCoord-1,handler);
                        }else{
                            tail =new Tail(this.xCoord,this.yCoord+1,handler);
                        }
                    }
                }else{
                    if(handler.getWorld().body.getLast().x!=handler.getWorld().GridWidthHeightPixelCount-1){
                        tail=new Tail(handler.getWorld().body.getLast().x+1,this.yCoord,handler);
                    }else{
                        if(handler.getWorld().body.getLast().y!=0){
                            tail=new Tail(handler.getWorld().body.getLast().x,this.yCoord-1,handler);
                        }else{
                            tail=new Tail(handler.getWorld().body.getLast().x,this.yCoord+1,handler);

                        }
                    }

                }
                break;
            case "Right":
                if( handler.getWorld().body.isEmpty()){
                    if(this.xCoord!=0){
                        tail=new Tail(this.xCoord-1,this.yCoord,handler);
                    }else{
                        if(this.yCoord!=0){
                            tail=new Tail(this.xCoord,this.yCoord-1,handler);
                        }else{
                            tail=new Tail(this.xCoord,this.yCoord+1,handler);
                        }
                    }
                }else{
                    if(handler.getWorld().body.getLast().x!=0){
                        tail=(new Tail(handler.getWorld().body.getLast().x-1,this.yCoord,handler));
                    }else{
                        if(handler.getWorld().body.getLast().y!=0){
                            tail=(new Tail(handler.getWorld().body.getLast().x,this.yCoord-1,handler));
                        }else{
                            tail=(new Tail(handler.getWorld().body.getLast().x,this.yCoord+1,handler));
                        }
                    }

                }
                break;
            case "Up":
                if( handler.getWorld().body.isEmpty()){
                    if(this.yCoord!=handler.getWorld().GridWidthHeightPixelCount-1){
                        tail=(new Tail(this.xCoord,this.yCoord+1,handler));
                    }else{
                        if(this.xCoord!=0){
                            tail=(new Tail(this.xCoord-1,this.yCoord,handler));
                        }else{
                            tail=(new Tail(this.xCoord+1,this.yCoord,handler));
                        }
                    }
                }else{
                    if(handler.getWorld().body.getLast().y!=handler.getWorld().GridWidthHeightPixelCount-1){
                        tail=(new Tail(handler.getWorld().body.getLast().x,this.yCoord+1,handler));
                    }else{
                        if(handler.getWorld().body.getLast().x!=0){
                            tail=(new Tail(handler.getWorld().body.getLast().x-1,this.yCoord,handler));
                        }else{
                            tail=(new Tail(handler.getWorld().body.getLast().x+1,this.yCoord,handler));
                        }
                    }

                }
                break;
            case "Down":
                if( handler.getWorld().body.isEmpty()){
                    if(this.yCoord!=0){
                        tail=(new Tail(this.xCoord,this.yCoord-1,handler));
                    }else{
                        if(this.xCoord!=0){
                            tail=(new Tail(this.xCoord-1,this.yCoord,handler));
                        }else{
                            tail=(new Tail(this.xCoord+1,this.yCoord,handler));
                        } System.out.println("Tu biscochito");
                    }
                }else{
                    if(handler.getWorld().body.getLast().y!=0){
                        tail=(new Tail(handler.getWorld().body.getLast().x,this.yCoord-1,handler));
                    }else{
                        if(handler.getWorld().body.getLast().x!=0){
                            tail=(new Tail(handler.getWorld().body.getLast().x-1,this.yCoord,handler));
                        }else{
                            tail=(new Tail(handler.getWorld().body.getLast().x+1,this.yCoord,handler));
                        }
                    }

                }
                break;
        }
        handler.getWorld().body.addLast(tail);
        handler.getWorld().playerLocation[tail.x][tail.y] = true;
        
        

        }
        
    
    

    public void kill(){
        lenght = 0;
        for (int i = 0; i < handler.getWorld().GridWidthHeightPixelCount; i++) {
            for (int j = 0; j < handler.getWorld().GridWidthHeightPixelCount; j++) {

                handler.getWorld().playerLocation[i][j]=false;

            }
        }
    }

    public boolean isJustAte() {
        return justAte;
    }

    public void setJustAte(boolean justAte) {
        this.justAte = justAte;
    }
}
