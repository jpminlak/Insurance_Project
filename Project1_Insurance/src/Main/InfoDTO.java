package Main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import InsuranceDTO.DiseaseDTO;
import InsuranceDTO.DuePaymentDTO;

//public class InfoDTO extends JFrame implements ActionListener, MouseListener {
public class InfoDTO extends JFrame implements ActionListener{
	private DbDAO dbdao = DbDAO.getInstance();
	private JPanel jpD1 = new JPanel();		// 예상 보험료 패널
	private JPanel jpD2 = new JPanel();		// 예상 보험금 패널
	private JPanel jpD3 = new JPanel();		// 예상 해지환급금 패널
	private JPanel jpD4 = new JPanel();		// 예상 만기지급금 패널
	private JPanel jpD5 = new JPanel();		// 확인 패널
	private JButton btn5 = new JButton("확인");
	private JPanel jpD10 = new JPanel();	// 전체 패널
	private DefaultTableModel dtmD1, dtmD2, dtmD3, dtmD4, dtm10;
	private ArrayList<String[]> arr2 = new ArrayList<String[]>();
	private JTable jt20;
	private DefaultTableCellRenderer crRight = new DefaultTableCellRenderer();
	private int period;
	
	// DB에서 불러올 정보
	private String contractCode, name, insuranceCode, insuranceName;
	
	public InfoDTO(String[] dto, DefaultTableModel dtm10, ArrayList<String[]> arr2) {
		this.dtm10 = dtm10; this.arr2 = arr2;
		this.period = Integer.parseInt(dto[4].replaceAll("[^0-9]", ""));	// 가입기간		
		this.setBounds(200,200,720,450);
		this.setTitle("계약내용 확인");
		JLabel jlD0 = new JLabel("   계약코드: " +dto[0]+" / 피보험자: " +dto[1]+
				" / 상품코드: " +dto[2]+" / 상품명: " +dto[3]);
		jlD0.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		this.add("North",jlD0);
		jlD1();	jlD2();// jlD3(); jlD4();
		jpD1(); jpD2();/* jpD3(); jpD4();*/ jpD5();
		jpD10.add(jpD1); jpD10.add(jpD2); jpD10.add(jpD3); jpD10.add(jpD4); jpD10.add(jpD5);
		jpD10.setLayout(null);
		this.add(jpD10);
		this.setVisible(true);
		//this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);

		// MouseListener
		/*JButton a = new JButton();
		a.setOpaque(false);
		a.setBounds(10,10,890,390); //a.setBounds(10,10,1200,750);
		this.add(a);
		a.addMouseListener(this);*/
	}

	private void jlD1() {
		JLabel jlD1 = new JLabel("예상 보험료");
		jlD1.setFont(new Font("맑은 고딕", Font.BOLD, 14));
		jlD1.setBounds(20,10,120,20);
		jpD10.add(jlD1);
	}

	private void jpD1() {
	//public void jpD1(DefaultTableModel dtm10) {
		jpD1.setBounds(20,30,170,300);
		jpD1.setBackground(Color.lightGray);
		String[] headingsD1 = new String[] {"나이","금액"};
		dtmD1 = new DefaultTableModel(headingsD1,0);
		JTable jtD1 = new JTable(dtmD1);
		crRight.setHorizontalAlignment(JLabel.RIGHT);
		jtD1.getColumn("나이").setPreferredWidth(50);
		jtD1.getColumn("나이").setCellRenderer(crRight);
		jtD1.getColumn("금액").setPreferredWidth(100);
		jtD1.getColumn("금액").setCellRenderer(crRight);
		jtD1.setPreferredScrollableViewportSize(new Dimension(150, 250));
		jtD1.setFillsViewportHeight(true);
		JScrollPane jsD1 = new JScrollPane(jtD1);
		jpD1.add(new JScrollPane(jtD1));
		
		int amount = (int) dtm10.getValueAt(0, 0);	// 보험료
		int age = (int) dtm10.getValueAt(0, 1);	// 피보험자 연령
		int rateAge = 0;	// 피보험자 연령에 따른 조정 요율 *10000
		int rateSex = 0;	// 피보험자 성별에 따른 조정 요율 *10000
		if (dtm10.getValueAt(0, 2).equals("M")) rateSex = 25;	// 남성 +0.25%
		String ill = (String)dtm10.getValueAt(0, 3);	// 피보험자 병력
		int rateIll = dbdao.selectDisease(ill);
		// 피보험자 병력에 따른 조정 요율 *10000
		NumberFormat nf = NumberFormat.getInstance();	// 숫자 3자리마다 콤마 붙이기
		int ageEra;
		for (int i=age; i < 80; i+=10) {
			if (i > period+age) break;
			ageEra = (i/10)*10;	// 연령대(20대, 30대, 40대, ...)
			if (ageEra == (age/10)*10) {
				ageEra = age;
			}
			if (ageEra < 30) {
				rateAge = -200;	// 20대 이하 -2%
			} else if (ageEra >= 30 && ageEra < 40) {
				rateAge = -100;
			} else if (ageEra >= 40 && ageEra < 50) {
				rateAge = 0;
			} else if (ageEra >= 50 && ageEra < 60) {
				rateAge = 100;
			} else if (ageEra >= 60 && ageEra < 70) {
				rateAge = 200;
			}
			int amountCons = (amount +
					(amount * (rateAge + rateSex + rateIll) / 10000));
			String amountWon = nf.format(amountCons);
			Vector<Object> v = new Vector<>();
			v.add(ageEra+" 세");
			v.add(amountWon+" 원");
			dtmD1.addRow(v);
		}
	}

	private void jlD2() {
		JLabel jlD2 = new JLabel("예상 보험금");
		jlD2.setFont(new Font("맑은 고딕", Font.BOLD, 14));
		jlD2.setBounds(215,10,120,20);
		//jlD2.setBounds(20,350,120,20);
		jpD10.add(jlD2);
	}
	
	private void jpD2() {
		jpD2.setBounds(215,30,470,300);
		jpD2.setBackground(Color.lightGray);
		String[] headingsD2 = new String[] {"구분","계약코드","계약명","가입금액","가입기간"};
		dtmD2 = new DefaultTableModel(headingsD2,0);
		JTable jtD2 = new JTable(dtmD2);
		jtD2.getColumn("구분").setPreferredWidth(60);
		jtD2.getColumn("계약코드").setPreferredWidth(80);
		jtD2.getColumn("계약명").setPreferredWidth(150);
		crRight.setHorizontalAlignment(JLabel.RIGHT);
		jtD2.getColumn("가입금액").setPreferredWidth(80);
		jtD2.getColumn("가입금액").setCellRenderer(crRight);
		jtD2.getColumn("가입기간").setPreferredWidth(80);
		jtD2.getColumn("가입기간").setCellRenderer(crRight);
		jtD2.setPreferredScrollableViewportSize(new Dimension(450, 250));
		jtD2.setFillsViewportHeight(true);
		jpD2.add(new JScrollPane(jtD2));
		
    	for(int i=0; i<arr2.size(); i++) {
    		Vector<Object> v = new Vector<>();
    		for(int j=0; j<arr2.get(i).length; j++) {
    			v.add(arr2.get(i)[j]);
    		}
        	dtmD2.addRow(v);
    	}
	}
	
	/* 예상 해지환급금, 예상 만기지급금
	private void jlD3() {
		JLabel jlD3 = new JLabel("예상 해지환급금");
		jlD3.setFont(new Font("맑은 고딕", Font.BOLD, 14));
		jlD3.setBounds(650,10,120,20);
		jpD10.add(jlD3);
	}
	
	public void jpD3() {
		jpD3.setBounds(650,30,480,300);
		jpD3.setBackground(Color.lightGray);
		String[] headingsD3 = new String[] {"경과기간","납입보험료","해지환급금","환급률"};
		dtmD3 = new DefaultTableModel(headingsD3,0);
		JTable jtD3 = new JTable(dtmD3);
		jtD3.getColumn("경과기간").setPreferredWidth(80);
		crRight.setHorizontalAlignment(JLabel.RIGHT);
		jtD3.getColumn("납입보험료").setPreferredWidth(150);
		jtD3.getColumn("해지환급금").setPreferredWidth(150);
		jtD3.getColumn("환급률").setPreferredWidth(50);
		jtD3.setPreferredScrollableViewportSize(new Dimension(430, 250));
		jtD3.setFillsViewportHeight(true);
		jpD3.add(new JScrollPane(jtD3));
	}

	private void jlD4() {
		JLabel jlD4 = new JLabel("예상 만기지급금");
		jlD4.setFont(new Font("맑은 고딕", Font.BOLD, 14));
		jlD4.setBounds(650,350,120,20);
		jpD10.add(jlD4);		
	}
	
	private void jpD4() {
		jpD4.setBounds(650,370,480,300);
		jpD4.setBackground(Color.lightGray);
		String[] headingsD4 = new String[] {"경과기간","납입보험료","만기지급금","환급률"};
		dtmD4 = new DefaultTableModel(headingsD4,0);
		JTable jtD4 = new JTable(dtmD4);
		jtD4.getColumn("경과기간").setPreferredWidth(80);
		crRight.setHorizontalAlignment(JLabel.RIGHT);
		jtD4.getColumn("납입보험료").setPreferredWidth(150);
		jtD4.getColumn("만기지급금").setPreferredWidth(150);
		jtD4.getColumn("환급률").setPreferredWidth(50);
		jtD4.setPreferredScrollableViewportSize(new Dimension(430, 250));
		jtD4.setFillsViewportHeight(true);
		jpD4.add(new JScrollPane(jtD4));
		
		int age = (int) dtm10.getValueAt(0, 1);	// 피보험자 연령
		String conp = arr2.get(0)[4].replaceAll("[^0-9]", "");
		int conPeriod = Integer.parseInt(conp);	// 계약기간
		System.out.println(conPeriod);
    	int[] elapsedYear = {1,2,3,5,10,15,20,30};	// 1) 경과기간
    	int usePeriod = 0;	// 경과기간 가져올 횟수
    	for(int i=0; i<elapsedYear.length; i++) {
    		if(conPeriod - elapsedYear[i] >= 0) usePeriod++;
    		else break;
    	}    	
    	int amount = (int) dtm10.getValueAt(0, 0);	// 월 납입 보험료
    	int totalAmount = amount * (conPeriod * 12);	// 2) 만기시 총 납입 보험료
    	String conCode = arr2.get(0)[1];	// 상품코드
    	ArrayList<DuePaymentDTO> rateDue = dbdao.selectDuePayment(conCode, conPeriod);	// 환급률
    	//ArrayList<DuePaymentDTO> rateDue = dbdao.selectDuePayment(conCode);	// 환급률
		for(DuePaymentDTO dpFor : rateDue) {
			Vector<Object> v = new Vector<Object>();
			v.add(dpFor.getDP_1Year());
			if(conPeriod>=2) v.add(dpFor.getDP_2Year());
			if(conPeriod>=3) v.add(dpFor.getDP_3Year());
			if(conPeriod>=4) v.add(dpFor.getDP_5Year());
			if(conPeriod>=5) v.add(dpFor.getDP_10Year());
			if(conPeriod>=6) v.add(dpFor.getDP_15Year());
			if(conPeriod>=7) v.add(dpFor.getDP_20Year());
			if(conPeriod>=8) v.add(dpFor.getDP_30Year());
			dtmD4.addRow(v);
		}
    	/*Vector<Object> v = new Vector<Object>();
    	for (int rdFor : rateDue) {
    		v.add(rdFor);
    		dtmD4.addColumn(v);
    	}*/
//	}
	
	private void jpD5() {
		jpD5.setBounds(330,350,80,40);
		//jpD5.setBounds(600,700,80,40);
		jpD5.setBackground(Color.cyan);
		jpD5.add(btn5);
		btn5.addActionListener(this);
	}
	/*	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == btn5) {
			dispose(); return;
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		System.out.println("위치 :"+ e.getX()+" , "+ e.getY());
	}
	@Override
	public void mouseEntered(MouseEvent e) {
	}
	@Override
	public void mouseExited(MouseEvent e) {
	}
	@Override
	public void mousePressed(MouseEvent e) {
	}
	@Override
	public void mouseReleased(MouseEvent e) {
	}*/

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btn5) {
			dispose(); return;
		}
		
	}
}
