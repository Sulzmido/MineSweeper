package main;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JFrame;
//import javax.swing.JOptionPane;

public class Game extends JFrame implements WindowListener{
    //private static Game game;
    //public static void main(String[] args){
        
        //game=new Game();
        
    //}
    
//    public static void newGame(){
//        game.dispose();
//        game=new Game();
//    }
    
    private GamePanel gp;
    public Game(String option){
        
        super("Mine Sweeper");
        //gp=new GamePanel(JOptionPane.showInputDialog("Enter \n1: Easy \n2: Medium \n3: Hard "));
        gp = new GamePanel(option) ;
        setContentPane(gp);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
        addWindowListener(this);
        setVisible(true);
        
    }

    @Override
    public void windowOpened(WindowEvent e) {}

    @Override
    public void windowClosing(WindowEvent e) {}

    @Override
    public void windowClosed(WindowEvent e) {}

    @Override
    public void windowIconified(WindowEvent e) {
       gp.pause();
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        gp.resume();
    }

    @Override
    public void windowActivated(WindowEvent e) {
        gp.resume();
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
       gp.pause();
    }
}
