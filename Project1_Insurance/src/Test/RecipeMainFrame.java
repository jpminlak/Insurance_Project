package Test;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*; 
 
public class RecipeMainFrame extends JFrame implements ActionListener{
    //배치
    CardLayout card=new CardLayout();
    Login login=new Login();
    JoinForm join=new JoinForm();
    public RecipeMainFrame()
    {
        //GUI 화면 디자인, 변수 초기화, 데이터베이스 연결, 서버연결, 쓰레드 동작
        //Layout => 화면배치
        setLayout(card);
        
        //화면추가
        add("JOIN",join);
        add("LOGIN",login);
        
        setSize(1300, 960);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE); 
        
        login.b2.addActionListener(this);
        join.b3.addActionListener(this);
    }
 
    public static void main(String[] args) throws Exception{
        //UIManager.setLookAndFeel("com.jtattoo.plaf.mcwin.McWinLookAndFeel"); 
        new RecipeMainFrame();    
    }
 
    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        if(e.getSource()==login.b2)
        {
            card.show(getContentPane(), "JOIN");
        }
        else if(e.getSource()==join.b3)
        {
            card.show(getContentPane(), "LOGIN");
        }
    }
 
}
