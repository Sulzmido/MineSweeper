package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable{
    
    // boolean in while loop in run
    private boolean running=true;
    
    //GamePanel's width and height
    private int PHEIGHT;
    private int PWIDTH;
    
    private int gameWidth;
    private int gameHeight;
    
    private int wUnit;
    private int hUnit;
    
    private int noOfMines;
    
    private ImageManager im; 
   
    //panels constructor
    public GamePanel(String input) {
        
        super();
        im=new ImageManager();
        setLevelDetails(input);
        initRects();
        setMines();
        setNumbers();
        setPreferredSize(new Dimension(PWIDTH,PHEIGHT));
        setFocusable(true);
        requestFocus();
        setUpMouse();
        setUpKey();
              
    }
    
    private void setNumbers(){
        int numMines;
        
        for(int i=0; i< modRects.length;i++){
       
            numMines=0;
            if(modRects[i].isMine){
                modRects[i].addNumber(numMines); 
                continue;
            }
            if((i+1)%wUnit!=0){
                
                if(i+1>-1 && i+1 <wUnit*hUnit){
                    if(modRects[i+1].isMine) numMines++;
                }      
                if(i+wUnit+1>-1 && i+wUnit+1 <wUnit*hUnit){
                    if(modRects[i+wUnit+1].isMine) numMines++;
                }
                if(i-wUnit+1>-1 && i-wUnit+1 <wUnit*hUnit){
                    if(modRects[i-wUnit+1].isMine) numMines++;
                }
            }
            if(i%wUnit!=0){
                
                if(i-1>-1 && i-1 <wUnit*hUnit){
                    if(modRects[i-1].isMine) numMines++;
                }     
                if(i-wUnit-1>-1 && i-wUnit-1 <wUnit*hUnit){
                    if(modRects[i-wUnit-1].isMine) numMines++;
                }
                if(i+wUnit-1>-1 && i+wUnit-1 <wUnit*hUnit){
                    if(modRects[i+wUnit-1].isMine) numMines++;
                }
            }            
            if(i+wUnit>-1 && i+wUnit <wUnit*hUnit){
                if(modRects[i+wUnit].isMine) numMines++;
            }
            if(i-wUnit>-1 && i-wUnit <wUnit*hUnit){
                if(modRects[i-wUnit].isMine) numMines++;
            }
            
            modRects[i].addNumber(numMines);            
        }
        for (ModRects modRect : modRects) {
            //modRect.printNumbers();
        }
    }
    
    private void setMines(){
        
        int mineInsert=0;
        
        Random r =new Random();
        
        while(mineInsert<noOfMines){
            if(modRects[r.nextInt(wUnit*hUnit)].addMine()){
                mineInsert++;
            }
        }
        for (ModRects modRect : modRects) {
            //modRect.print();
        }
    }
    
    private ModRects[] modRects;
    
    private void initRects(){
        
        int xx=1;int yy=1;
        modRects=new ModRects[wUnit*hUnit];
        
        for (int i = 0; i < modRects.length; i++) {
            modRects[i]=new ModRects(new Point(xx,yy));
            if(++xx>wUnit){
                xx=1;
                yy++;
            }
            
        }
    }
    
    private void setLevelDetails(String input){
        try{
            int choice=Integer.parseInt(input);
            
            switch(choice){
                case 1:
                    wUnit=9;
                    hUnit=9;
                    noOfMines=10;
                break;
                
                case 2:
                    wUnit=16;
                    hUnit=10;
                    noOfMines=40;
                break;
                
                case 3:
                    wUnit=30;
                    hUnit=16;
                    noOfMines=99;
                break;
                
                default:
                    wUnit=9;
                    hUnit=9; 
                    noOfMines=10;
            }
            
        }catch(Exception e){
            
            JOptionPane.showMessageDialog(null,"Error Because of you! \n Press Ok to Close Application");
            System.exit(0);
        }
        
        gameWidth=wUnit*30;
        gameHeight=hUnit*30;
        PHEIGHT=gameHeight+20;
        PWIDTH=gameWidth+20;
        
    }
    
    //addNotify is called when panel is added to frame and visible
    private Thread thread;
    @Override
    public void addNotify(){
        super.addNotify();
        thread=new Thread(this);
        thread.start();
    }
    
    
    //run method in thread (Game Loop)
    @Override
    public void run() {
        while(running){
            //gameUpdate();
            gameRender();
            paintScreen();
            
            try{
                thread.sleep(100);
            }catch(Exception e){}
        }
        
    }
    
    private void gameUpdate() {
//        System.out.println("H");
        if(!isPaused){
            
            if(checkWin()){
                JOptionPane.showMessageDialog(null,"You WIN!!");
                running = false ;
                new HomeFrame().setVisible(true) ;   
                HomeFrame.game.dispose();
                //new HomeFrame() ;
                //System.exit(0);
            }                        
        }
    }
    
    //private boolean win;
    //private int antiCheat;
    private int rightClicks;
    private int leftClicks;
    private boolean checkWin(){
        
        rightClicks=0;
        leftClicks=0;
        for(ModRects modRect: modRects ){
            if(modRect.isMine && !modRect.rightClicked ){
                return false;
            }
            if(modRect.rightClicked){
                rightClicks++;
            }
            if(modRect.leftClicked){
                leftClicks++;
            }    
        }
        if(leftClicks == ((wUnit*hUnit)-noOfMines)){
            return rightClicks == noOfMines;
        }else{
            return false;
        }               
    }
    
    //gameRender uses img as buffer
    //paints into img
    private Image img;
    private void gameRender() {
//        System.out.println("He");

        img=createImage(PWIDTH,PHEIGHT);
        Graphics g=img.getGraphics();
        
        for(ModRects modRect : modRects){
            
            if(modRect.rightClicked){
                
                g.setColor(Color.green);
                g.fill3DRect((modRect.point.x-1)*30+10,(modRect.point.y-1)*30+10, 30, 30,true);
                g.setColor(Color.BLACK);
                g.drawRect((modRect.point.x-1)*30+10, (modRect.point.y-1)*30+10, 30, 30);
                g.setColor(Color.red);
                g.drawLine((modRect.point.x-1)*30+10, (modRect.point.y-1)*30+10,(modRect.point.x-1)*30+40, (modRect.point.y-1)*30+40);
                g.drawLine((modRect.point.x-1)*30+10, (modRect.point.y-1)*30+40,(modRect.point.x-1)*30+40, (modRect.point.y-1)*30+10);
                g.drawOval((modRect.point.x-1)*30+10, (modRect.point.y-1)*30+10,30,30);
                g.drawImage(im.Flag,(modRect.point.x-1)*30+10,(modRect.point.y-1)*30+10,null);
                
            }else if(modRect.leftClicked){
                g.setColor(Color.green);
                g.fill3DRect((modRect.point.x-1)*30+10,(modRect.point.y-1)*30+10, 30, 30,true);
                g.setColor(Color.BLACK);
                g.drawRect((modRect.point.x-1)*30+10, (modRect.point.y-1)*30+10, 30, 30);
                g.drawImage(im.getImage(modRect.number),(modRect.point.x-1)*30+11,(modRect.point.y-1)*30+11,null);
                
            }else{
                g.setColor(Color.green);
                g.fill3DRect((modRect.point.x-1)*30+10,(modRect.point.y-1)*30+10, 30, 30,true);
                g.setColor(Color.BLACK);
                g.drawRect((modRect.point.x-1)*30+10, (modRect.point.y-1)*30+10, 30, 30);
                
                //if(!modRect.isMine)g.drawImage(im.getImage(modRect.number),(modRect.point.x-1)*30+11,(modRect.point.y-1)*30+11,null);
            }  
        }

        fillBorders(g);
        
        g.dispose();
    }
    private void fillBorders(Graphics g){
        
        g.setColor(Color.black);       
        g.fill3DRect(0, 0, PWIDTH, 10,true);
        g.fill3DRect(0, 0, 10, PHEIGHT, true);
        g.fill3DRect(PWIDTH-10, 0, 10, PHEIGHT, true);
        g.fill3DRect(0, PHEIGHT-10, PWIDTH, 10, true);
    }
    
    //paintScreen paints buffer to screen
    //gets the Graphics of panel and paints the buffer image
    private void paintScreen() {
//        System.out.println("Hey");
        Graphics g=this.getGraphics();
        g.drawImage(img,0,0,null);        
        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }

    private volatile boolean isPaused=false;
    public void pause() {
        isPaused=true;
    }

    public void resume() {
        isPaused=false;
    }
    
    private int index;
    //method binds action to mouse input on JPanel
    private void setUpMouse() {
        
        addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                if(!isPaused){
                    
                    switch(e.getButton()){
                        
                        case MouseEvent.BUTTON1:
                                                        
                            //System.out.println("Left Click "+e.getX()+"  "+e.getY());
                            index=resolveClick(e.getX(),e.getY());
                            if(index>-1 && index < wUnit*hUnit){
                                int retVal=modRects[index].isLeftClicked();
                                if(retVal==1){
                                    doMagic(index);
                                }
                                if(retVal==2){
                                    JOptionPane.showMessageDialog(null,"You Clicked On A Mine!");
                                    running = false ;
                                    new HomeFrame().setVisible(true) ;   
                                    HomeFrame.game.dispose();
                                    //game Over
                                    //pause();
                                    //Game.newGame();
                                }
                            }                            
                            break;  
                        
                        case MouseEvent.BUTTON3:
                            
                            //System.out.println("Right Click "+e.getX()+"  "+e.getY());
                            index=resolveClick(e.getX(),e.getY());
                            if(index>-1 && index < wUnit*hUnit){                                
                                modRects[resolveClick(e.getX(),e.getY())].isRightClicked();
                            }                            
                            break;
                    }
                    gameUpdate();
                }                
            }
        });        
    }
    
    private void doMagic(int index){
        
        modRects[index].leftClicked=true;
        
         if((index+1)%wUnit!=0){
               
               if(index+1>-1 && index+1 <wUnit*hUnit){
                   if(!modRects[index+1].rightClicked){
                   if(modRects[index+1].number==0 && !modRects[index+1].leftClicked){
                       doMagic(index+1);
                   } 
                   else{
                       modRects[index+1].leftClicked=true;
                   }
                   }
               }      
               if(index+wUnit+1>-1 && index+wUnit+1 <wUnit*hUnit){
                   if(!modRects[index+wUnit+1].rightClicked){
                   if(modRects[index+wUnit+1].number==0 && !modRects[index+wUnit+1].leftClicked) {
                       doMagic(index+wUnit+1);
                   }
                   else{
                       modRects[index+wUnit+1].leftClicked=true;
                   }
                   }
               }
               if(index-wUnit+1>-1 && index-wUnit+1 <wUnit*hUnit){
                   if(!modRects[index-wUnit+1].rightClicked){
                   if(modRects[index-wUnit+1].number==0 && !modRects[index-wUnit+1].leftClicked) {
                       doMagic(index-wUnit+1);
                   }
                   else{
                       modRects[index-wUnit+1].leftClicked=true;
                   }
                   }
               }
       }
           if(index%wUnit!=0){
               
               if(index-1>-1 && index-1 <wUnit*hUnit){
                   if(!modRects[index-1].rightClicked){
                   if(modRects[index-1].number==0 && !modRects[index-1].leftClicked) {
                       doMagic(index-1);
                   }
                   else{
                       modRects[index-1].leftClicked=true;
                   }
                   }
               }
               
               if(index-wUnit-1>-1 && index-wUnit-1 <wUnit*hUnit){
                   if(!modRects[index-wUnit-1].rightClicked){
                   if(modRects[index-wUnit-1].number==0 && !modRects[index-wUnit-1].leftClicked) {
                       doMagic(index-wUnit-1);
                   }
                   else{
                       modRects[index-wUnit-1].leftClicked=true;
                   }
                   }
               }
               
               if(index+wUnit-1>-1 && index+wUnit-1 <wUnit*hUnit){
                   if(!modRects[index+wUnit-1].rightClicked){
                   if(modRects[index+wUnit-1].number==0 && !modRects[index+wUnit-1].leftClicked) {
                       doMagic(index+wUnit-1);
                   }
                   else{
                       modRects[index+wUnit-1].leftClicked=true;
                   }
                   }
               }
           }   
           
           if(index+wUnit>-1 && index+wUnit <wUnit*hUnit){
               if(!modRects[index+wUnit].rightClicked){
               if(modRects[index+wUnit].number==0 && !modRects[index+wUnit].leftClicked) {
                   doMagic(index+wUnit);
               }
               else{
                   modRects[index+wUnit].leftClicked=true;
               }
               }
           }
           if(index-wUnit>-1 && index-wUnit <wUnit*hUnit){
               if(!modRects[index-wUnit].rightClicked){
               if(modRects[index-wUnit].number==0 && !modRects[index-wUnit].leftClicked) {
                   doMagic(index-wUnit);
               }
               else{
                   modRects[index-wUnit].leftClicked=true;
               }
               }
           }        
    }
    
    private int resolveClick(int x,int y){
        
        return ((x-10)/30) + (((y-10)/30)*wUnit);
    }
    
    //method bind action to key input on JPanel
    private void setUpKey() {
        
        addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                if(e.getKeyChar()==KeyEvent.VK_SPACE) pause();
                if(e.getKeyChar()==KeyEvent.VK_ENTER) resume();
            }
        });
    }
    
}
