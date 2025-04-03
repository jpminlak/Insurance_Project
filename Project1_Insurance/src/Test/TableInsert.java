package Test;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
 
public class TableInsert extends JFrame {
 
    public TableInsert(){
        
        String []a = {"a","b","c"};
        String [][]b = {{"a1","a2","a3"},
                        {"b1","b2","b3"},
                        {"c1","c2","c3"}};
        
        //1. 모델과 데이터를 연결
        DefaultTableModel model = new DefaultTableModel(b,a);
        
        //2. Model을 매개변수로 설정, new JTable(b,a)도 가능하지만 
        //삽입 삭제를 하기 위해 해당 방법을 사용합니다
        JTable table = new JTable(model); //
        
        //3. 결과적으로는 JScrollPane를 추가합니다.
        JScrollPane sc = new JScrollPane(table);
        
        //4. 컴포넌트에  Table 추가
        add(sc);
        
        //테이블에 데이터 추가하기
        //원본데이터를 건들지 않고 table의 매개변수인 model에 있는 데이터를 변경합니다
        DefaultTableModel m =
                (DefaultTableModel)table.getModel();
        //모델에 데이터 추가 , 1번째 출에 새로운 데이터를 추가합니다
        m.insertRow(1, new Object[]{"d1","d2","d3"});
        //추가를 마치고 데이터 갱신을 알립니다.
        table.updateUI();
    
        
        
        //------- 그 외 메소드들 ----------
        
        //테이블의 데이터를 가져오는 메소드
        System.out.println(table.getValueAt(1,1));
        //테이블의 데이터를 바꾸는 메소드
        table.setValueAt("picachu",2,2);
        //테이블 row 갯수 가져오기
        System.out.println(table.getRowCount());
        //테이블 colum 갯수 가져오기
        System.out.println(table.getColumnCount());
        //테이블 Colum 이름 가져오기
        System.out.println(table.getColumnName(0));
        
        setBounds(0, 0, 300, 150);
        setVisible(true);
        
    }

}
