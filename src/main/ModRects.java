package main;

import java.awt.Point;

public class ModRects {
    
    public boolean isMine=false;
    public Point point;
    
    public boolean leftClicked=false;
    public boolean rightClicked=false;
    
    public int number;
    
    
    public ModRects(Point pos){
        
        point=pos;
    }
    
    public void isRightClicked(){
        
        if(leftClicked) return;
        rightClicked=!rightClicked;
    }
    
    public int isLeftClicked(){
        if(leftClicked) return 0;
        if(rightClicked) return 0;
        if(isMine){
            
            return 2;
        }
        if(number!=0){
            leftClicked=true;
            return 0;
        }else{
            return 1;
        }
        
    }
    
    public void printNumbers(){
        System.out.println(point.toString()+" Number: "+number);
    }
    public void print(){
        
        System.out.println(point.toString()+"   "+isMine);
    }
    public boolean addMine(){
        if(!isMine){
            isMine=true;
            return true;
        }
        return false;
    }
    
    public void addNumber(int num){
        number=num;
    }
    
}

