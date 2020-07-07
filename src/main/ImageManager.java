
package main;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;


public class ImageManager {
    
   // private final String IMAGE_DIR="Images/";
    
    public BufferedImage Zero;
    public BufferedImage One;
    public BufferedImage Two;
    public BufferedImage Three;
    public BufferedImage Four;
    public BufferedImage Five;
    public BufferedImage Six;
    public BufferedImage Seven;
    public BufferedImage Eight;
    
    public BufferedImage Mine;
    public BufferedImage Flag;    
    
    public ImageManager(){
        loadImages();
    }
    
    private GraphicsConfiguration gc;
    
    private void loadImages(){
        
        GraphicsEnvironment ge=GraphicsEnvironment.getLocalGraphicsEnvironment();
        gc=ge.getDefaultScreenDevice().getDefaultConfiguration();
        
        
        Zero=loadImage("Zero.gif");
        One=loadImage("One.gif");

        Two=loadImage("Two.gif");
        
        Three=loadImage("Three.gif");
        Four=loadImage("Four.gif");
        Five=loadImage("Five.gif");
        Six=loadImage("Six.gif");
        Seven=loadImage("Seven.gif");
        Eight=loadImage("Eight.gif");
        
        //Mine=loadImage("Mine.gif");
        Flag=loadImage("Flag.gif");
              
    }
    
    private BufferedImage loadImage(String fnm){    
        //load an image compatible with underlying graphics device.
        try {
            BufferedImage im=ImageIO.read(getClass().getResource(fnm));
            
            int transparency=im.getColorModel().getTransparency();
            
            BufferedImage copy=gc.createCompatibleImage(im.getWidth(),im.getHeight(),transparency);
            
            Graphics2D g2d=copy.createGraphics();
            g2d.drawImage(im,0,0,null);
            g2d.dispose();
            return copy;
            
        } catch (IOException ex) {return null;}     
    }
    
    public BufferedImage getImage(int num){
        
        switch(num){
            case 0:
                return Zero;
            case 1:
                return One;
            case 2:
                return Two;
            case 3:
                return Three;
            case 4:
                return Four;
            case 5:
                return Five;
            case 6:
                return Six;
            case 7:
                return Seven;
            case 8:
                return Eight;
            default: return null;
                
        }      
    }    
}
