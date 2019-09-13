package Game.Entities.Dynamic;

import Main.Handler;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import Game.Entities.Static.Apple;
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
    public static double currScore = 0.0;
    public String Score;
    public int moveCounter;
    public boolean isGood;
    public int steps;
    
    private InputStream GameOverMusic;
    private AudioInputStream audioStream;
    private AudioFormat format;
    private DataLine.Info info;
    private Clip audioClip;
   
    public String direction;//is your first name one?

    public Player(Handler handler){
        this.handler = handler;
        xCoord = 0;
        yCoord = 0;
        moveCounter = 0;
        direction= "Right";
        justAte = false;
        lenght= 1;
        isGood = Apple.isGood();
        steps = 0;

    }
    int pace = 5;
    public void tick(){
        moveCounter++;
        //Phase 4: Makes good Apples Become Rotten After X steps(X = Grid Width)
        if (steps == handler.getWorld().GridWidthHeightPixelCount || isGood == false) {
			steps = 0;
			isGood = false;
		}
        if(moveCounter>=pace) {
            checkCollisionAndMove();
            moveCounter=0;
            steps++;
        }
        //Phase 3: Prevent Backtracking
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
		//Phase 2: + & - Buttons To Change Snake Speed
		if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_MINUS)) {
			pace+=4;
		}
		if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_EQUALS)) {
			pace-=4;
		}
		//Phase 2: N Button To Add A Piece Of Tail
         if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_N)){
        	 Eat();
        	 handler.getWorld().appleOnBoard=true;
         }
         //Phase 3: ESC Button To Trigger Pause State 
         if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_ESCAPE)) {
        	 State.setState(handler.getGame().pauseState);
         }
         //Added COMMA Button To Test Game Over State
         if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_COMMA)) {
        	 State.setState(handler.getGame().gameoverState);
        	 
        	 try {
             	
             	GameOverMusic = getClass().getResourceAsStream("/music/Game Over - Metal Gear Solid.wav");
                 audioStream = AudioSystem.getAudioInputStream(GameOverMusic);
                 format = audioStream.getFormat();
                 info = new DataLine.Info(Clip.class, format);
                 audioClip = (Clip) AudioSystem.getLine(info);
                 audioClip.open(audioStream);
                 audioClip.loop(0);


             } catch (UnsupportedAudioFileException e) {
                 e.printStackTrace();
             } catch (IOException e) {
                 e.printStackTrace();
             } catch (LineUnavailableException e) {
                 e.printStackTrace();
             }
         }

         }


    public void checkCollisionAndMove(){
        handler.getWorld().playerLocation[xCoord][yCoord]=false;
        int x = xCoord;
        int y = yCoord;
        //Phase 3: Make Snake Teleport When It Collides With Screen Edges
        switch (direction) {
    	case "Left":
    		if (xCoord == 0) {
    			xCoord = handler.getWorld().GridWidthHeightPixelCount - 1;
    		} else {
    			xCoord--;
    		}
    		break;
    	case "Right":
    		if (xCoord == handler.getWorld().GridWidthHeightPixelCount - 1) {
    			xCoord = 0;
    		} else {
    			xCoord++;
    		}
    		break;
    	case "Up":
    		if (yCoord == 0) {
    			yCoord = handler.getWorld().GridWidthHeightPixelCount - 1;
    			;
    		} else {
    			yCoord--;
    		}
    		break;
    	case "Down":
    		if (yCoord == handler.getWorld().GridWidthHeightPixelCount - 1) {
    			yCoord = 0;
    		} else {
    			yCoord++;
    		}
    		break;
    	}
        handler.getWorld().playerLocation[xCoord][yCoord]=true;


        if(handler.getWorld().appleLocation[xCoord][yCoord]){
        	 //Phase 1: Change Speed Of snake When It Eats
        	 //Phase 3: Make Score Appear When Snake Eats
        	 //Phase 4: Decrease The Score When Snake Eats A Bad Apple
        	 if (isGood == false) {
             	if (currScore > 0) {
             		pace++;
             		currScore -= Math.sqrt(2 * currScore + 1);
             		if (currScore < 0) {
             			currScore = 0;
             		}
             	} else {
             		Eat();
             	}
             	foodPoisoning();
            	isGood = true;
            } else {
            	Eat();
            	pace-=8;
            	currScore += Math.sqrt(2 * currScore + 1);
            }
            steps = 0;
            DecimalFormat numberFormat = new DecimalFormat("#.00");
            Score=numberFormat.format(currScore);
        }
        

        if(!handler.getWorld().body.isEmpty()) {
            handler.getWorld().playerLocation[handler.getWorld().body.getLast().x][handler.getWorld().body.getLast().y] = false;
            handler.getWorld().body.removeLast();
            handler.getWorld().body.addFirst(new Tail(x, y,handler));}
        //Phase 3: Display Game Over Screen When Snake Collides With Itself    
        for(int i = 0; i < handler.getWorld().body.size(); i++) {
        	if(xCoord == handler.getWorld().body.get(i).x && yCoord == handler.getWorld().body.get(i).y) {
        		State.setState(handler.getGame().gameoverState);
        		
        		try {
                 	
                 	GameOverMusic = getClass().getResourceAsStream("/music/Game Over - Metal Gear Solid.wav");
                     audioStream = AudioSystem.getAudioInputStream(GameOverMusic);
                     format = audioStream.getFormat();
                     info = new DataLine.Info(Clip.class, format);
                     audioClip = (Clip) AudioSystem.getLine(info);
                     audioClip.open(audioStream);
                     audioClip.loop(0);


                 } catch (UnsupportedAudioFileException e) {
                     e.printStackTrace();
                 } catch (IOException e) {
                     e.printStackTrace();
                 } catch (LineUnavailableException e) {
                     e.printStackTrace();
                 }
        		
        	kill();
        	}
        }
        }
    
        
    
        

  
    public void render(Graphics g,Boolean[][] playeLocation){
        Color [] colors = {Color.green, Color.blue, Color.cyan, Color.pink, Color.magenta, Color.red, Color.orange, Color.yellow};
        int colorCode = 0;
        for (int i = 0; i < handler.getWorld().GridWidthHeightPixelCount; i++) {
        	if (colorCode < 7) {
        		colorCode++;
        	} else if (colorCode == 7) {
        		colorCode = colorCode - 7;
        	}
        	for (int j = 0; j < handler.getWorld().GridWidthHeightPixelCount; j++) {
        		if (playeLocation[i][j]) {
        			if (colorCode < 7) {
                		g.setColor(colors[colorCode]);
                        g.fillRect((i*handler.getWorld().GridPixelsize),
                                (j*handler.getWorld().GridPixelsize),
                                handler.getWorld().GridPixelsize,
                                handler.getWorld().GridPixelsize);  
                		colorCode++;
                	} else if (colorCode == 7) {
                		g.setColor(colors[colorCode]);
                        g.fillRect((i*handler.getWorld().GridPixelsize),
                                (j*handler.getWorld().GridPixelsize),
                                handler.getWorld().GridPixelsize,
                                handler.getWorld().GridPixelsize);
                		colorCode = colorCode - 7;
                    }
        		
        		//Phase 1: Multicolored snake ^^^

      		   
                } else if (isGood == false){
                	if (handler.getWorld().appleLocation[i][j]){
                		g.setColor(Color.darkGray);
                		g.fillRect((i*handler.getWorld().GridPixelsize),
                				(j*handler.getWorld().GridPixelsize),
                				handler.getWorld().GridPixelsize,
                				handler.getWorld().GridPixelsize);
                	}
                } else if (handler.getWorld().appleLocation[i][j]){
            		g.setColor(Color.green);
            		g.fillRect((i*handler.getWorld().GridPixelsize),
            				(j*handler.getWorld().GridPixelsize),
            				handler.getWorld().GridPixelsize,
            				handler.getWorld().GridPixelsize);
            	}
        		//Phase 4: Apple colors  ^^^

            }
        }
        //Phase 3: Score printing vvv
        g.setColor(Color.black);
        if (Score == null) {
        	g.drawString("Score: 0", 1, 10);
        } else {
            g.drawString("Score: " + Score, 1, 10);
        }
        g.drawString("Steps: " + steps, 5, 20);
    }

    public void Eat(){
    	lenght++;
        Tail tail= null;
        handler.getWorld().appleLocation[xCoord][yCoord]=false;
        handler.getWorld().appleOnBoard=false;
        switch (direction){
            case "Left":
                if(handler.getWorld().body.isEmpty()){
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
                        } System.out.println("The cake is a lie");
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
    
    public void foodPoisoning() {
    	handler.getWorld().playerLocation[handler.getWorld().body.getLast().x][handler.getWorld().body.getLast().y] = false;
    	handler.getWorld().appleLocation[xCoord][yCoord] = false;
    	handler.getWorld().body.removeLast();
    	handler.getWorld().appleOnBoard = false;
    	
//    	Phase 4: method to reduce snake length ^^^
    }
    
}
