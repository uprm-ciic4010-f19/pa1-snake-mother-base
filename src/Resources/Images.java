package Resources;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by AlexVR on 7/1/2018.
 */
public class Images {


    public static BufferedImage[] butstart;
    public static BufferedImage title;
    public static BufferedImage Pause;
    public static BufferedImage[] Resume;
    public static BufferedImage[] BTitle;
    public static BufferedImage[] Options;
    public static ImageIcon icon;
    public static BufferedImage GameOver;
    public static BufferedImage Continue;
    public static BufferedImage Exit;		
    
    public Images() {

        butstart = new BufferedImage[3];
        Resume = new BufferedImage[2];
        BTitle = new BufferedImage[2];
        Options = new BufferedImage[2];
 

        try {

            title = ImageIO.read(getClass().getResourceAsStream("/Sheets/Title.png"));
            Pause = ImageIO.read(getClass().getResourceAsStream("/Buttons/Pause.png"));
            GameOver = ImageIO.read(getClass().getResourceAsStream("/Sheets/Game Over Screen.jpg"));
            //Continue[0] = ImageIO.read(getClass().getResourceAsStream("/Buttons/Continue Button.jpg"));
            //Exit = ImageIO.read(getClass().getResourceAsStream("/Buttons/Exit Button.jpg"));
            Resume[0] = ImageIO.read(getClass().getResourceAsStream("/Buttons/Continue Button.jpg"));
            Resume[1] = ImageIO.read(getClass().getResourceAsStream("/Buttons/Continue Button.jpg"));
            BTitle[0] = ImageIO.read(getClass().getResourceAsStream("/Buttons/Exit Button.jpg"));
            BTitle[1] = ImageIO.read(getClass().getResourceAsStream("/Buttons/Exit Button.jpg"));
            //Options[0] = ImageIO.read(getClass().getResourceAsStream("/Buttons/Options.png"));
            //Options[1] = ImageIO.read(getClass().getResourceAsStream("/Buttons/OptionsP.png"));
            butstart[0]= ImageIO.read(getClass().getResourceAsStream("/Buttons/NormBut.png"));//normbut
            butstart[1]= ImageIO.read(getClass().getResourceAsStream("/Buttons/HoverBut.png"));//hoverbut
            butstart[2]= ImageIO.read(getClass().getResourceAsStream("/Buttons/ClickedBut.png"));//clickbut

            icon =  new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/Sheets/icon.png")));


        }catch (IOException e) {
        e.printStackTrace();
    }


    }

    public static BufferedImage loadImage(String path) {
        try {
            return ImageIO.read(Images.class.getResourceAsStream(path));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }

}
