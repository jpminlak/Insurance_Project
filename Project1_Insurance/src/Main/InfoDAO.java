package Main;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import InsuranceDTO.ContractDTO;
import InsuranceDTO.DiseaseDTO;
import InsuranceDTO.InsuranceMainContractDTO;
import InsuranceDTO.InsuranceProductDTO;
import InsuranceDTO.InsuranceSpecialContractDTO;
import InsuranceDTO.MemberDTO;

public class InfoDAO extends JFrame implements ActionListener {
//public class InfoDAO extends JFrame implements ActionListener, MouseListener {
	private DbDAO dbdao = DbDAO.getInstance();
	private JPanel jp1 = new JPanel();	// 피보험자 주민번호 검색 패널
	private JButton btn1, btn70, btn90, btn91, btn92;
	private JTable jt2, jt3, jt5, jt6, jt7, jt70;
	private JTextField jtf1 = new JTextField(10);
	private JPanel jp2 = new JPanel();	// 피보험자 개인정보 등록 패널
	private JButton btn2 = new JButton("등록");
	private JPanel jp3 = new JPanel(new FlowLayout(FlowLayout.LEFT));	// 상품 조회 패널
	private JTextField jtf31 = new JTextField(17);
	private JButton btn31 = new JButton("조회");
	private JButton btn32 = new JButton("등록");
	private JTextField jtf32 = new JTextField(15);
	private JTextField jtf33 = new JTextField(15);
	private JPanel jp5 = new JPanel();	// 주계약 특약 선택 패널
	private JButton btn5 = new JButton("추가");
	private JPanel jp6 = new JPanel();	// 등록된 피보험자 개인정보 출력 패널
	private JPanel jp7 = new JPanel();	// 등록한 상품, 가입금액 출력 패널
	private JButton btn7 = new JButton("선택");
	private JButton btn7a = new JButton("취소");
	private JPanel jp70 = new JPanel();	// 세부계약(주계약,특약) 표시
	private JPanel jp8 = new JPanel();	// 예상 보험료 패널
	private JLabel jl82;
	private JPanel jp9 = new JPanel();	// 최종 등록 / 취소
	private JPanel jp10 = new JPanel();	// 전체 패널
	//private DefaultTableCellRenderer dtcr23 = new DefaultTableCellRenderer();
	//private Container container = getContentPane();
	private JButton logout = new JButton("로그아웃");
	private JButton ml = new JButton();	// MouseListener용 거대 버튼
	//JButton btn21 = new JButton("등록"); JButton btn22 = new JButton("취소");
	private String memberInfo;	// 신규 피보험자 임시 정보 
	private int memberAge = 0; String memberSex = null;
	private DefaultTableModel dtm2,dtm3,dtm5,dtm6,dtm7,dtm70,dtm10;
	private ArrayList<String[]> arr2 = new ArrayList<String[]>();
	private DefaultTableCellRenderer dtcr;
	private int fee, prodPrice; 
	//private JCheckBox box = new JCheckBox();
	private String memberID, tempReg, adminID, adminName;
	private String beCode = "X";		// 기존 계약(X), 신규계약(O)
	private String[] msCont = new String[10];	// 주계약/특약 코드 임시 저장
	private String[] contID = new String[10];	// 피보험자 한 명이 계약 가능한 수 10개
	private String[] dto = new String[5];	// 예상 패널에서 출력할 계약 정보
	
	public InfoDAO(String id) {
		this.setBounds(0,0,1250,850);
		this.setTitle("계약 등록/조회");

		this.adminID = id;
		adminName = dbdao.getAdminName();
		JLabel title = new JLabel(adminName+" 님 환영합니다.");
		title.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		JPanel jp0 = new JPanel();
		jp0.add(title); jp0.add(logout);
		this.add("North",jp0);
		logout.addActionListener(this);

		jp1(); jp2(); jp3(); jp5(); jp6(); jp7(); jp70(); jp8(); jp9();
		jp10.setLayout(null);
		jp10.add(jp1); jp10.add(jp2); jp10.add(jp3); jp10.add(jp5);
		jp10.add(jp6); jp10.add(jp7); jp10.add(jp70); jp10.add(jp8); jp10.add(jp9);
		this.add(jp10);
		this.setVisible(true);
		this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
				
		dtcr = new DefaultTableCellRenderer() {
			public Component getTableCellRendererComponent
					(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				JCheckBox box= new JCheckBox();
				box.setSelected(((Boolean)value).booleanValue());
				box.setHorizontalAlignment(JLabel.CENTER);
				return box;
			}
		};
		
		/* 전체 보험 목록 출력
		ArrayList<InsuranceProductDTO> prodlist = dbdao.selectInsuranceProductDTO();
		for(InsuranceProductDTO prodFor : prodlist) {
			prodFor.prt();
		}*/
		
		/* MouseListener
		ml.setOpaque(false);
		ml.setBounds(10, 10, 1200,840);
		this.add(ml);
		ml.addMouseListener(this);*/
	}

	private void jp1() {
		jp1.setBounds(10,10,300,40);
		jp1.setBackground(Color.yellow);
		JLabel jl1 = new JLabel("주민등록번호"); btn1 = new JButton("조회");
		btn1.addActionListener(this);
		jp1.add(jl1); jp1.add(jtf1); jp1.add(btn1);
	}
		
	private void jp2() {
		jp2.setBounds(10,60,570,80);
		jp2.setBackground(Color.yellow);
		String[] headings2 = new String[] {"주민등록번호","피보험자","병력","주소"};
		dtm2 = new DefaultTableModel(headings2,0);
		jt2 = new JTable(dtm2);
		jt2.getColumn("주민등록번호").setPreferredWidth(120);
		jt2.getColumn("피보험자").setPreferredWidth(80);
		jt2.getColumn("병력").setPreferredWidth(80);
		jt2.getColumn("주소").setPreferredWidth(280);
		jt2.setPreferredScrollableViewportSize(new Dimension(560, 17));
		//jt2.setRowHeight(20); 셀 높이 설정 가능할 듯
		jt2.setFillsViewportHeight(true);
		jp2.add(new JScrollPane(jt2));
		/* 피보험자 전체 목록 확인용
		ArrayList<MemberDTO> mlist = dbdao.selectMemberDTO();
		for(MemberDTO mdtoFor : mlist) {
			mdtoFor.prt();
		}
		*/
		jp2.add(btn2); btn2.addActionListener(this);
	}

	private void jp3() {
		jp3.setLayout(null);
		jp3.setBounds(650,10,350,320);
		jp3.setBackground(Color.yellow);
		
		String[] headings3 = new String[] {"상품코드","상품명"};
		dtm3 = new DefaultTableModel(headings3, 0);
		jt3 = new JTable(dtm3);
		jt3.getColumn("상품코드").setPreferredWidth(80);
		jt3.getColumn("상품명").setPreferredWidth(250);
		jt3.setPreferredScrollableViewportSize(new Dimension(330, 200));
		jt3.setFillsViewportHeight(true);
		jt3.getTableHeader().setReorderingAllowed(false);
		jt3.getTableHeader().setResizingAllowed(false);
		JScrollPane js3 = new JScrollPane(jt3);	jp3.add(js3);
		jt3.setBounds(10,10,330,200); js3.setBounds(5,5,340,210);
		
		JLabel jl31 = new JLabel("상품명");
		jp3.add(jl31); jp3.add(jtf31); jp3.add(btn31);
		jl31.setBounds(10,225,60,25); jtf31.setBounds(80,225,185,25);
		btn31.setBounds(280,225,60,25); btn31.addActionListener(this);
		
		JLabel jl32 = new JLabel("가입금액");
		JLabel jl33 = new JLabel("만원");
		jp3.add(jl32); jp3.add(jtf32); jp3.add(jl33); jp3.add(btn32);
		jl32.setBounds(10,255,60,25); jtf32.setBounds(80,255,150,25);
		jl33.setBounds(240,255,30,25);
		//btn32.setBounds(280,290,60,25); btn32.addActionListener(this);
		
		JLabel jl34 = new JLabel("가입기간");
		JLabel jl35 = new JLabel("년");
		jp3.add(jl34); jp3.add(jtf33); jp3.add(jl35); jp3.add(btn32);
		jl34.setBounds(10,285,60,25); jtf33.setBounds(80,285,150,25);
		jl35.setBounds(253,285,30,25);
		btn32.setBounds(280,285,60,25); btn32.addActionListener(this);
	}
	
	private void jp5() {
		String[] headings5 = new String[] {"v","구분","계약코드","계약명","가입금액","보험료"};
		dtm5 = new DefaultTableModel(headings5, 0) {
			public Class<?> getColumnClass(int column) {
				switch(column) {
					case 0:	return Boolean.class;
					case 1: return String.class;
					case 2: return String.class;
					case 3: return String.class;
					case 4: return String.class;
					case 5: return String.class;
					default: return String.class;
				}
			}
		};
		jp5.setBounds(650,340,560,380);
		jp5.setBackground(Color.yellow);
		jt5 = new JTable(dtm5);
		jt5.getColumn("v").setCellRenderer(dtcr);
		JCheckBox box = new JCheckBox();
		box.setHorizontalAlignment(JLabel.CENTER);
		jt5.getColumn("v").setCellEditor(new DefaultCellEditor(box));
		jt5.getColumn("v").setPreferredWidth(30);
		jt5.getColumn("구분").setPreferredWidth(50);
		jt5.getColumn("계약코드").setPreferredWidth(100);
		jt5.getColumn("계약명").setPreferredWidth(200);
		jt5.getColumn("가입금액").setPreferredWidth(80);
		jt5.getColumn("보험료").setPreferredWidth(80);
		DefaultTableCellRenderer crRight = new DefaultTableCellRenderer();		
		crRight.setHorizontalAlignment(JLabel.RIGHT);
		jt5.getColumn("가입금액").setCellRenderer(crRight);
		jt5.getColumn("보험료").setCellRenderer(crRight);
		jt5.setPreferredScrollableViewportSize(new Dimension(540, 310));
		jt5.setFillsViewportHeight(true);
		JScrollPane js5 = new JScrollPane(jt5);
		getContentPane().add(js5);
		//jp5.add(getContentPane().add(js5));
		jp5.add(js5); jp5.add(btn5);
		jt5.setBounds(10,10,540,310); js5.setBounds(5,5,550,320);
		//btn5.setBounds(5,5,65,25);
		btn5.addActionListener(this);
	}
	
	private void jp6() {
		jp6.setBounds(10,150,320,50);
		jp6.setBackground(Color.lightGray);
		String[] headings6 = new String[] {"피보험자","연령","성별","병력"};
		dtm6 = new DefaultTableModel(headings6,0);
		jt6 = new JTable(dtm6);
		jt6.getColumn("피보험자").setPreferredWidth(100);
		jt6.getColumn("연령").setPreferredWidth(50);
		jt6.getColumn("성별").setPreferredWidth(50);
		jt6.getColumn("병력").setPreferredWidth(100);
		jt6.setPreferredScrollableViewportSize(new Dimension(300, 17));
		jt6.setFillsViewportHeight(true);
		jp6.add(new JScrollPane(jt6));
	}
	
	private void jp7() {
		jp7.setLayout(null);
		jp7.setBounds(10,210,570,120);
		jp7.setBackground(Color.lightGray);
		String[] headings7 = new String[]
				{"계약코드","상품코드","상품명","가입금액","가입기간","신규"};
		dtm7 = new DefaultTableModel(headings7, 0);		// 신규 고객 입력 정보
		jt7 = new JTable(dtm7);
		jt7.getColumn("계약코드").setPreferredWidth(70);
		jt7.getColumn("상품코드").setPreferredWidth(70);
		jt7.getColumn("상품명").setPreferredWidth(150);
		jt7.getColumn("가입금액").setPreferredWidth(80);
		jt7.getColumn("가입기간").setPreferredWidth(70);
		jt7.getColumn("신규").setPreferredWidth(30);
		jt7.getTableHeader().setReorderingAllowed(false);
		jt7.getTableHeader().setResizingAllowed(false);
		DefaultTableCellRenderer crRight = new DefaultTableCellRenderer();
		DefaultTableCellRenderer crCentre = new DefaultTableCellRenderer();
		crRight.setHorizontalAlignment(JLabel.RIGHT);
		crCentre.setHorizontalAlignment(JLabel.CENTER);
		jt7.getColumn("가입금액").setCellRenderer(crRight);
		jt7.getColumn("가입기간").setCellRenderer(crRight);
		jt7.getColumn("신규").setCellRenderer(crCentre);
		jt7.setPreferredScrollableViewportSize(new Dimension(470, 90));
		jt7.setFillsViewportHeight(true);
		JScrollPane js7 = new JScrollPane(jt7);
		jp7.add(js7); jp7.add(btn7); jp7.add(btn7a);
		jt7.setBounds(10,10,470,90); js7.setBounds(5,5,480,100);
		btn7.setBounds(495,30,60,25);
		btn7a.setBounds(495,65,60,25);
		btn7.addActionListener(this);
		btn7a.addActionListener(this);
	}

	private void jp70() {
		jp70.setBounds(10,340,440,380);
		jp70.setBackground(Color.lightGray);
		String[] headings70 = new String[] {"계약코드","계약명","가입금액","보험료","신규"};
		dtm70 = new DefaultTableModel(headings70, 0);
		jt70 = new JTable(dtm70);
		jt70.getColumn("계약코드").setPreferredWidth(80);
		jt70.getColumn("계약명").setPreferredWidth(150);
		jt70.getColumn("가입금액").setPreferredWidth(80);
		jt70.getColumn("보험료").setPreferredWidth(80);
		jt70.getColumn("신규").setPreferredWidth(30);
		DefaultTableCellRenderer crRight = new DefaultTableCellRenderer();
		DefaultTableCellRenderer crCentre = new DefaultTableCellRenderer();
		crRight.setHorizontalAlignment(JLabel.RIGHT);
		crCentre.setHorizontalAlignment(JLabel.CENTER);
		jt70.getColumn("가입금액").setCellRenderer(crRight);
		jt70.getColumn("보험료").setCellRenderer(crRight);
		jt70.getColumn("신규").setCellRenderer(crCentre);
		jt70.setPreferredScrollableViewportSize(new Dimension(430, 310));
		jt70.setFillsViewportHeight(true);
		jt70.getTableHeader().setReorderingAllowed(false);
		jt70.getTableHeader().setResizingAllowed(false);
		jp70.add(new JScrollPane(jt70));
		btn70 = new JButton("취소"); jp70.add(btn70);
		btn70.addActionListener(this);
	}
	
	private void jp8() {
		jp8.setBounds(10,730,400,35);
		jp8.setBackground(new Color(175,255,47));
		jp8.setLayout(null);
		JLabel jl81 = new JLabel("예상 월 보험료");
		jl81.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		jl82 = new JLabel("0");
		// JLabel에는 String 형태만 가능하여 이런 방식을 사용
		jl82.setHorizontalAlignment(JLabel.RIGHT); 
		JLabel jl83 = new JLabel("원");
		jl81.setBounds(10,5,120,25); jl82.setBounds(140,5,200,25);
		jl83.setBounds(350,5,30,25);
		jp8.add(jl81); jp8.add(jl82); jp8.add(jl83);
	}
	
	private void calculatefee() {
		fee = 0; int age; int rateAge = 0; int rateSex = 0; String ill = null;
		for(int i = 0; i<dtm70.getRowCount(); i++) {
			int tempFee = Integer.parseInt(dtm70.getValueAt(i,3).toString());
			fee += tempFee;
		}
		if (dtm6.getRowCount() == 0) {
			JOptionPane.showMessageDialog
			(null, "피보험자 정보가 없으면 40세 남성을 기준으로 월 보험료를 계산합니다.");
			age = 40; rateSex = 25;
		} else {
			age = (int) dtm6.getValueAt(0,1);
			if (dtm6.getValueAt(0,2).equals("M")) rateSex = 25;
			ill = dtm6.getValueAt(0,3).toString();	// 피보험자 병력
		}
		int rateIll = dbdao.selectDisease(ill);
		NumberFormat nf = NumberFormat.getInstance();	// 숫자 3자리마다 콤마 붙이기
		int ageEra = (age/10)*10;	// 연령대(20대, 30대, 40대, ...)
		if (ageEra < 30) rateAge = -200;
		else if (ageEra >= 30 && ageEra < 40) rateAge = -100;
		else if (ageEra >= 40 && ageEra < 50) rateAge = 0;
		else if (ageEra >= 50 && ageEra < 60) rateAge = 100;
		else if (ageEra >= 60 && ageEra < 70) rateAge = 200;
		int amountCons = (fee +
				(fee * (rateAge + rateSex + rateIll) / 10000));
		String amountWon = nf.format(amountCons);
		jl82.setText(amountWon);
	}
	
	private void jp9() {
		jp9.setBounds(510,730,230,40); jp9.setBackground(Color.cyan);
		btn90 = new JButton("예상");	btn91 = new JButton("등록");
		btn92 = new JButton("삭제");
		jp9.add(btn90); jp9.add(btn91); jp9.add(btn92);
		btn90.addActionListener(this); btn91.addActionListener(this);
		btn92.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == btn1) {
			btn1();
		} else if(e.getSource() == btn2) {
			if(jt2.getSelectedRow() == -1) {
				 JOptionPane.showMessageDialog(null, "먼저 주민등록번호를 선택해주세요.");
			} else {
				btn2();
			}
		} else if (e.getSource() == btn31) {
			btn31();
		} else if (e.getSource() == btn32) {
			btn32(); btn32a();
		} else if (e.getSource() == btn5) {
			btn5();
		} else if (e.getSource() == btn7) {
			if(jt7.getSelectedRow() == -1) {
				 JOptionPane.showMessageDialog(null, "조회하실 계약을 선택해주세요.");
			} else {
				btn7();
			}
		} else if (e.getSource() == btn7a) {
			if(jt7.getSelectedRow() == -1 ||
					jt7.getValueAt(jt7.getSelectedRow(),5).equals("X")) {
				JOptionPane.showMessageDialog(null, "신규계약만 취소 가능합니다.");
				return;
			} else {
				int[] sr = jt7.getSelectedRows();
				for(int i : sr) {
					dtm7.removeRow(jt7.getSelectedRow());
				}
			}
		} else if(e.getSource() == btn70) {
			System.out.println("btn70");
			if(jt70.getSelectedRow() == -1) {
				return;
			} else {
				int[] sc = jt70.getSelectedRows();
				for(int i : sc) {
					dtm70.removeRow(jt70.getSelectedRow());
					calculatefee();
				}
			}
		} else if (e.getSource() == btn90) {
			if (jt6.getRowCount()==0 || jt7.getRowCount()==0 ||
					jt70.getRowCount()==0) {
				JOptionPane.showMessageDialog(null,"정보를 더 입력하세요.");
				return;
			}
			btn90(); btn90a(); btn90b();
			InfoDTO infdto = new InfoDTO(dto, dtm10, arr2);
		} else if (e.getSource() == btn91) {
			if (jt7.getSelectedRow() == -1) {
				JOptionPane.showMessageDialog(null, "등록하실 계약을 선택해주세요.");
				return;
			}
			if (jt7.getValueAt(jt7.getSelectedRow(),5).equals("X")) {
				JOptionPane.showMessageDialog(null, "이미 등록된 계약입니다.");
				return;
			}
			int yesno = JOptionPane.showConfirmDialog
					(this, "신규계약을 등록하시겠습니까?","confirm",JOptionPane.YES_NO_OPTION);
			if (yesno == 0) {	// 예(0), 아니오(1)
				btn91a(); btn91b();			
			} else return;
			
		} else if (e.getSource() == btn92) {
			btn92();
		} else if(e.getSource() == logout) {
			dbdao.logon(0, adminID);
			dispose(); new Login();
		}
	}

	private void btn1() {
		String searchRnumber = jtf1.getText();
		for(int i=0; i<dtm2.getRowCount(); i++){
			dtm2.removeRow(i); i=-1;
		}
		if(searchRnumber.equals("")) {
			JOptionPane.showMessageDialog(null,"주민등록번호를 입력해주세요.");
		} else if (searchRnumber.length() < 7 ||
				searchRnumber.charAt(7) < 49 || searchRnumber.charAt(7) > 52) {
			JOptionPane.showMessageDialog(null,"주민등록번호 입력 오류입니다.");
			return;
		} else {
			ArrayList<MemberDTO> memlist = dbdao.memberselectAll(searchRnumber);
			Vector<Object> v = new Vector<Object>();
			if (memlist.isEmpty()) {
				JP20 jp20 = new JP20(searchRnumber);
			} else {
				for(MemberDTO memFor : memlist) {	
					v.add(memFor.getM_registrationnumber());
					v.add(memFor.getM_name());
					v.add(memFor.getM_diseasehistory());
					v.add(memFor.getM_addr());
					dtm2.addRow(v);
					//memFor.prt();
				}
			}
		}
		jtf1.setText("");
		/* 등록
		JOptionPane.showMessageDialog(null,"팝업 메시지");
		MemberDTO mdto = new MemberDTO();
		mdto.setM_registrationNumber(jtf1.getText());
		dbdao.insertMember(mdto);
		//dlm1.addElement(regNum);
		dtm1.setValueAt(jtf1.getText(), 0, 0);
		jtf1.setText("");*/
	}

	private void btn2() {
		for(int i=0; i<dtm6.getRowCount(); i++){
			dtm6.removeRow(i); i=-1;
		}
		String code = (String)jt2.getValueAt(jt2.getSelectedRow(), 0);	// 주민등록번호
		ArrayList<MemberDTO> memlist2 = dbdao.memberSelectAll2(code);
		String memberCode = null;	// 피보험자 코드
		Vector<Object> v = new Vector<Object>();
		if (memlist2.isEmpty()) {
			calculateAge();
			if (memberAge == -1) {
		    	JOptionPane.showMessageDialog(null,"주민등록번호 입력 오류입니다.");
		    	return;
			} else {
				v.add(dtm2.getValueAt(0,1)); v.add(memberAge);
				v.add(memberSex); v.add(dtm2.getValueAt(0,2));
				dtm6.addRow(v);	
			}
		} else {
			for(MemberDTO memFor2 : memlist2) {
				calculateAge();
				v.add(memFor2.getM_name()); v.add(memberAge); v.add(memberSex);
				if(memFor2.getM_diseasehistory() != null) {
					v.add(memFor2.getM_diseasehistory());
				} else {
					v.add("없음");
				}
				dtm6.addRow(v);
				memberCode = memFor2.getM_ID();
				//memFor2.prt2();
			}
		}
		memberAge=0; memberSex=null;
		btn21(memberCode);
	}
	
	// 기존 고객이 가지고 있는 보험계약 정보 불러오기
	private void btn21(String memberCode) {
		for(int i=0; i<dtm7.getRowCount(); i++){
			dtm7.removeRow(i); i=-1;
		}
		for(int i=0; i<dtm70.getRowCount(); i++){
			dtm70.removeRow(i); i=-1;
		}
		ArrayList<ContractDTO> cdto = dbdao.selectContract(1, memberCode);
		beCode = "X"; int cdtoCount = 0; int j = 0;
		for(ContractDTO cdtoFor : cdto) {
			String prodCode = cdtoFor.getP_ID();	// 상품 코드
			String prodName = dbdao.selectProduct(prodCode);	// 상품명 검색
			Vector<Object> v2 = new Vector<Object>();	// 상품 등록 패널(jp7)
			prodPrice = cdtoFor.getC_amount();		// 가입금액
			v2.add(cdtoFor.getC_ID()); v2.add(cdtoFor.getP_ID()); v2.add(prodName);
			v2.add(cdtoFor.getC_amount()+" 만원"); v2.add(cdtoFor.getC_period()+" 년");
			v2.add(beCode);
			dtm7.addRow(v2);
			msCont[0] = cdtoFor.getMC_ID(); msCont[1] = cdtoFor.getSC_ID1();
			msCont[2] = cdtoFor.getSC_ID2(); msCont[3] = cdtoFor.getSC_ID3();
			msCont[4] = cdtoFor.getSC_ID4(); msCont[5] = cdtoFor.getSC_ID5();
			msCont[6] = cdtoFor.getSC_ID6(); msCont[7] = cdtoFor.getSC_ID7();
			msCont[8] = cdtoFor.getSC_ID8(); msCont[9] = cdtoFor.getSC_ID9();
		}
		/*
		for (int i=0; i<msCont.length; i++) {
			Vector<Object> v70 = new Vector<Object>();	// 주계약/특약
			if(msCont[i] != null) {
				v70.add(msCont[i]);	// v70 - 0번 인덱스 - 주계약/특약 코드
				System.out.print(msCont[i]+" / ");
				if(i == 0) {	// 주계약
					ArrayList<InsuranceMainContractDTO> mclist
					= dbdao.selectInsuranceMainContractDTO(2, msCont[i]);
					for(InsuranceMainContractDTO mcFor : mclist) {
						v70.add(mcFor.getMC_name());	// v70 - 1~3번 인덱스
						v70.add(prodPrice+" 만원");
						v70.add(mcFor.getMC_fee());
						v70.add(beCode);	// v70 - 4번 인덱스
						System.out.print(mcFor.getMC_name()+" / ");
						System.out.print(prodPrice+" / ");
						System.out.println(mcFor.getMC_fee());
					}
				} else {	// 특약
					ArrayList<InsuranceSpecialContractDTO> sclist
					= dbdao.selectInsuranceSpecialContractDTO(2, msCont[i]);
					for(InsuranceSpecialContractDTO scFor : sclist) {
						v70.add(scFor.getSC_name());	// v70 - 1~3번 인덱스
						v70.add(scFor.getSC_pay()+" 만원");
						v70.add(scFor.getSC_fee());
						v70.add(beCode);	// v70 - 4번 인덱스
						System.out.print(scFor.getSC_name()+" / ");
						System.out.print(scFor.getSC_pay()+" / ");
						System.out.println(scFor.getSC_fee());
					}
				}
				dtm70.addRow(v70);
			}
		}*/
		calculatefee();
	}

	// 나이, 성별 구하기
	private void calculateAge() {
	    tempReg = (String) jt2.getValueAt(0, 0);
        int year = Integer.parseInt(tempReg.substring(0,2));
        int month = Integer.parseInt(tempReg.substring(2,4));
        int day = Integer.parseInt(tempReg.substring(4,6));
	    if (tempReg.charAt(7) == '1') {
	    	year += 1900; memberSex = "M";
	    } else if (tempReg.charAt(7) == '2') {
	    	year += 1900; memberSex = "F";
	    } else if (tempReg.charAt(7) == '3') {
	    	year += 2000; memberSex = "M";
	    } else if (tempReg.charAt(7) == '4') {
	    	year += 2000; memberSex = "F";
	    } else {
	    	memberAge = -1; return;
	    }
		// 오늘 날짜
		LocalDate today = LocalDate.now();
        int todayYear = today.getYear();
        int todayMonth = today.getMonthValue();
        int todayDay = today.getDayOfMonth();
        // 생일에 따른 나이 구하기
       	memberAge = todayYear - year;	
        if(month > todayMonth) {
        	memberAge--;
        } else if(month == todayMonth) {
        	if(day > todayDay) {
        		memberAge--;
            }
        }
	}
	
	private void btn31() {
		for(int i=0; i<dtm3.getRowCount(); i++){
			dtm3.removeRow(i);
			i=-1;	// 기존에 잔존하던 테이블 값을 완전히 지우기 위해 일부러 i값을 0으로 맞춤. 이렇게 해야 완전히 없어짐.
		}
		String searchProd = jtf31.getText();
		if(searchProd.equals("")) {
			JOptionPane.showMessageDialog(null,"상품을 입력해주세요.");
		} else {
			ArrayList<InsuranceProductDTO> prodlist
			= dbdao.selectInsuranceProductDTO(searchProd);
			for(InsuranceProductDTO prodFor : prodlist) {
				Vector<Object> v = new Vector<Object>();
				v.add(prodFor.getIP_id());
				v.add(prodFor.getIP_name());
				dtm3.addRow(v);
				// prodFor.prt();
			}
		}
	}

	private void btn32() {
		for(int i=0; i<dtm7.getRowCount(); i++){
			if(dtm7.getValueAt(i,4).equals("O")) {
				dtm7.removeRow(i); i=-1;
			}
		}
		for(int i=0; i<dtm70.getRowCount(); i++){
			//if(dtm7.getValueAt(i,4).equals("O")) {
				dtm70.removeRow(i); i=-1;
			//}
			calculatefee();
		}
		if(jt3.getSelectedRow() == -1) {
			return;
		}
		//String price = jtf32.getText().trim();
		//String period = jtf33.getText().trim();
		int price = Integer.parseInt(jtf32.getText().trim());
		int period = Integer.parseInt(jtf33.getText().trim());
		if (jtf32.getText().trim().isEmpty() || jtf33.getText().trim().isEmpty()) {
		    JOptionPane.showMessageDialog
		    	(null, "가격과 기간을 입력하세요.", "입력 오류", JOptionPane.WARNING_MESSAGE);
		    return;
		}
		if (period > 30 || period < 1) {
			 JOptionPane.showMessageDialog(null, "가입기간은 1년 이상, 30년 이하만 가능합니다.");
			 return;
		}
		String code = (String)jt3.getValueAt(jt3.getSelectedRow(),0);
		String prod = (String)jt3.getValueAt(jt3.getSelectedRow(),1);
		beCode = "O";
		Vector<Object> v = new Vector<Object>();
		v.add("신규"); v.add(code); v.add(prod); 
		v.add(jtf32.getText().trim() + " 만원");	v.add(jtf33.getText().trim() + " 년");
		v.add(beCode);
		dtm7.addRow(v);
	}
	
	private void btn32a() {
		for(int i=0; i<dtm5.getRowCount(); i++){
			dtm5.removeRow(i); i=-1;
		}
		String searchMC = (String)jt3.getValueAt(jt3.getSelectedRow(),0);
		int price = Integer.parseInt(jtf32.getText().trim());
		if (searchMC == null) {
			JOptionPane.showMessageDialog(null,"상품을 선택해주세요.");
			return;
		} // 상품을 선택하지 않았을 시 코드 다듬기 필요
		ArrayList<String> mcID = new ArrayList<>();
		if(searchMC.equals("")) {
			JOptionPane.showMessageDialog(null,"가입금액을 입력해주세요.");
		} else {
			ArrayList<InsuranceMainContractDTO> mclist
			= dbdao.selectInsuranceMainContractDTO(1, searchMC);
			for(InsuranceMainContractDTO mcFor : mclist) {	// 주계약
				if(mcFor.getMC_min() > price || mcFor.getMC_max() < price) {
					JOptionPane.showMessageDialog(null,
							"해당 주계약의 최소 가입금액은 "+mcFor.getMC_min()+"만원, "
							+ "최대 가입금액은 "+mcFor.getMC_max()+"만원입니다.");
					continue; //return;
				}
				Vector<Object> v = new Vector<Object>();
				v.add(false); v.add("주계약"); v.add(mcFor.getMC_id());
				v.add(mcFor.getMC_name()); v.add(price+" 만원");
				v.add(mcFor.getMC_fee());
				dtm5.addRow(v);
				mcID.add(mcFor.getMC_id());
				//mcFor.prt();
			}
			for(int i=0; i<mcID.size(); i++) {	// 특약
				String sc = mcID.get(i);
				ArrayList<InsuranceSpecialContractDTO> sclist 
				= dbdao.selectInsuranceSpecialContractDTO(1, sc);
				for(InsuranceSpecialContractDTO scFor : sclist) {
					Vector<Object> v = new Vector<Object>();
					v.add(false); v.add("특약"); 	v.add(scFor.getSC_id());
					v.add(scFor.getSC_name());
					if(scFor.getSC_pay() > price) {
						v.add(price+" 만원");
					} else {
						v.add(scFor.getSC_pay()+" 만원");
					}
					v.add(scFor.getSC_fee());
					dtm5.addRow(v);
				}
			}
		}
	}
	
	private void btn5() {
	    /*if (jt5.getSelectedRow() == -1) {
	        JOptionPane.showMessageDialog(null, "주계약/특약을 선택하세요.");
	        return;
	    }*/
	    Vector<Object> selectRow = new Vector<>();
	    int mainContract = 0;
	    for(int i=0; i<jt5.getRowCount(); i++) {
        	Boolean checked = Boolean.valueOf(jt5.getValueAt(i, 0).toString());
        	if(checked) {
        		selectRow.add(i);
        		if (jt5.getValueAt(i, 1).toString().equals("주계약")) {
        			mainContract++;	
        		}
        	}
	    }
	    if (mainContract != 1) {
	        JOptionPane.showMessageDialog(null, "주계약 1개를 반드시 선택해주세요.");
	        return;
	    }
	    for (int j=0; j<dtm70.getRowCount(); j++) {
	        String existCode = dtm70.getValueAt(j,0).toString(); // 0번 열이 계약코드
	        for (int k=0; k<selectRow.size(); k++) {
	        	if (existCode.equals
	        			(jt5.getValueAt((int) selectRow.get(k),2).toString())) {
		        	JOptionPane.showMessageDialog(null, "이미 추가된 계약입니다.");
	    	        return;	
	        	}
	        }
	    }
		//if (dtm6.getValueAt(0,1) == null || dtm6.getValueAt(0,2) == null) {
		//if (jt6.getValueAt(0,1) == "" || jt6.getValueAt(0,2) == "") {
	    if (jt70.getRowCount() > 0) {
	    	if (jt70.getValueAt(0,0).toString().charAt(0) == 'E') {
		        JOptionPane.showMessageDialog(null, "이미 주계약 1개를 선택하셨습니다.");
		        return;
		    }	
	    }
	    for(int i=0; i<jt5.getRowCount(); i++) {
        	Boolean checked = Boolean.valueOf(jt5.getValueAt(i, 0).toString());
        	beCode = "O";
	        if(checked) {
	    	    Vector<Object> row = new Vector<Object>();
	    	    row.add(jt5.getValueAt(i, 2).toString());
	    	    row.add(jt5.getValueAt(i, 3).toString());
	    	    row.add(jt5.getValueAt(i, 4).toString());
	    	    row.add(jt5.getValueAt(i, 5).toString());
	    	    row.add(beCode);
	    	    dtm70.addRow(row);
	        }
	    }
	    calculatefee();
	}

	private void btn7() {
		for(int i=0; i<dtm70.getRowCount(); i++){
			dtm70.removeRow(i); i=-1;
		}
		if(jt7.getSelectedRow() == -1) {
			return;
		} else {
			String nowContID = jt7.getValueAt(jt7.getSelectedRow(),0).toString();
			ArrayList<ContractDTO> cdto = dbdao.selectContract(2, nowContID);
			String[] nowMS = new String[10];	// 현재 주/특약 코드
			int n = 1; // 특약 개수 -1
			for(ContractDTO cdtoFor : cdto) {	// 해당 계약번호의 주계약/특약 검색
				nowMS[0] = cdtoFor.getMC_ID();
				if (cdtoFor.getSC_ID1() != null) nowMS[1] = cdtoFor.getSC_ID1(); n++;
				if (cdtoFor.getSC_ID2() != null) nowMS[2] = cdtoFor.getSC_ID2(); n++;
				if (cdtoFor.getSC_ID3() != null) nowMS[3] = cdtoFor.getSC_ID3(); n++;
				if (cdtoFor.getSC_ID4() != null) nowMS[4] = cdtoFor.getSC_ID4(); n++;
				if (cdtoFor.getSC_ID5() != null) nowMS[5] = cdtoFor.getSC_ID5(); n++;
				if (cdtoFor.getSC_ID6() != null) nowMS[6] = cdtoFor.getSC_ID6(); n++;
				if (cdtoFor.getSC_ID7() != null) nowMS[7] = cdtoFor.getSC_ID7(); n++;
				if (cdtoFor.getSC_ID8() != null) nowMS[8] = cdtoFor.getSC_ID8(); n++;
				if (cdtoFor.getSC_ID9() != null) nowMS[9] = cdtoFor.getSC_ID9(); n++;
			}
			for (int i=0; i<n; i++) {
				Vector<Object> v70 = new Vector<Object>();	// 주계약/특약
				if(nowMS[i] != null) {
					v70.add(nowMS[i]);	// v70 - 1번 인덱스 - 주계약/특약 코드
					if(i == 0) {	// 주계약
						ArrayList<InsuranceMainContractDTO> mclist
						= dbdao.selectInsuranceMainContractDTO(2, nowMS[i]);
						for(InsuranceMainContractDTO mcFor : mclist) {
							v70.add(mcFor.getMC_name());	// v70 - 1~3번 인덱스

							v70.add(prodPrice+" 만원");
							v70.add(mcFor.getMC_fee());
							v70.add(beCode);	// v70 - 4번 인덱스
						}
					} else {	// 특약
						ArrayList<InsuranceSpecialContractDTO> sclist
						= dbdao.selectInsuranceSpecialContractDTO(2, nowMS[i]);
						for(InsuranceSpecialContractDTO scFor : sclist) {
							int tempSCprice = 0;	
							v70.add(scFor.getSC_name());	// v70 - 1~3번 인덱스
							// 주계약 가입금액보다 특약 가입금액이 클 때
							if (prodPrice < scFor.getSC_pay()) {
								v70.add(prodPrice+" 만원");
							} else {
								v70.add(scFor.getSC_pay()+" 만원");	
							}							
							v70.add(scFor.getSC_fee());
							v70.add(beCode);	// v70 - 4번 인덱스
						}
					}
					dtm70.addRow(v70);
				}
			}
		}
		calculatefee();
	}
	
	private void btn90() {
		dto[0] = jt7.getValueAt(jt7.getSelectedRow(),0).toString();	// 계약코드
		dto[1] = jt6.getValueAt(0,0).toString();	// 피보험자
		dto[2] = jt7.getValueAt(jt7.getSelectedRow(),1).toString();	// 상품코드
		dto[3] = jt7.getValueAt(jt7.getSelectedRow(),2).toString();	// 상품명
		dto[4] = jt7.getValueAt(jt7.getSelectedRow(),4).toString();	// 가입기간
	}
	
	private void btn90a() {
		String[] headings10 = new String[] {"보험료","연령","성별","병력"};
		dtm10 = new DefaultTableModel(headings10, 0);
		dtm10.addRow(new Object[0]);
		dtm10.setValueAt(fee,0,0);
		dtm10.setValueAt(jt6.getValueAt(0,1),0,1);
		dtm10.setValueAt(jt6.getValueAt(0,2),0,2);
		dtm10.setValueAt(jt6.getValueAt(0,3),0,3);
	}
	
	private void btn90b() {
		arr2.removeAll(arr2);	// 배열의 모든 데이터를 삭제
		for(int i=0 ; i<jt70.getRowCount(); i++) {
			String conType;		// 구분
			String conCode = jt70.getValueAt(i,0).toString();	// 계약코드
			if(conCode.charAt(0) == 'E') {
				conType = "주계약";
			} else {
				conType = "특약";
			}
			String conName = jt70.getValueAt(i,1).toString();	// 계약명
			String conPrice = jt70.getValueAt(i,2).toString();	// 가입금액
			// 가입기간
			String conPeriod = jt7.getValueAt(jt7.getSelectedRow(),4).toString();
			arr2.add(new String[] {conType, conCode, conName, conPrice, conPeriod});
		}
	}
	
	// 회원을 먼저 등록하자.
	private void btn91a() {
		MemberDTO mdto = new MemberDTO();
		mdto.setM_registrationnumber(jt2.getValueAt(0,0).toString());
		mdto.setM_name(jt2.getValueAt(0,1).toString());
		mdto.setM_diseasehistory(jt2.getValueAt(0,2).toString());
		mdto.setM_addr(jt2.getValueAt(0,3).toString());
		dbdao.insertMember(mdto);
		memberID = dbdao.selectMemberID(tempReg);
		// 피보험자 ID를 먼저 찾은 뒤 계약정보에 피보험자 ID를 입력하기 위해 만듦
	}

	// 회원 등록 후 계약정보 등록하자.
	private void btn91b() {
		int spcLength = 0;
		String amountInt = jt7.getValueAt
				(jt7.getSelectedRow(),3).toString().replaceAll("[^0-9]", "");
		String periodInt = jt7.getValueAt
				(jt7.getSelectedRow(),4).toString().replaceAll("[^0-9]", "");
		int C_amount = Integer.parseInt(amountInt);
		int C_period = Integer.parseInt(periodInt);
		ContractDTO cdto = new ContractDTO();
		cdto.setM_ID(memberID);
		cdto.setP_ID(jt7.getValueAt(jt7.getSelectedRow(),1).toString());
		cdto.setC_amount(C_amount);
		cdto.setC_period(C_period);
		cdto.setMC_ID(jt70.getValueAt(0,0).toString());
		for(int i=0; i<jt70.getRowCount(); i++) {
			if(jt70.getValueAt(i,0).toString().charAt(0) == 'F') {
				spcLength++;	// 특약 길이
			}
		}
		if (spcLength >= 1) cdto.setSC_ID1(jt70.getValueAt(1,0).toString()); 
		if (spcLength >= 2) cdto.setSC_ID2(jt70.getValueAt(2,0).toString());
		if (spcLength >= 3) cdto.setSC_ID3(jt70.getValueAt(3,0).toString());
		if (spcLength >= 4) cdto.setSC_ID4(jt70.getValueAt(4,0).toString());
		if (spcLength >= 5) cdto.setSC_ID5(jt70.getValueAt(5,0).toString());
		if (spcLength >= 6) cdto.setSC_ID6(jt70.getValueAt(6,0).toString());
		if (spcLength >= 7) cdto.setSC_ID7(jt70.getValueAt(7,0).toString());
		if (spcLength >= 8) cdto.setSC_ID8(jt70.getValueAt(8,0).toString());
		if (spcLength >= 9) cdto.setSC_ID9(jt70.getValueAt(9,0).toString());
		dbdao.insertContract(cdto);
		/*System.out.print(memberID+" / ");
		System.out.print(jt7.getValueAt(jt7.getSelectedRow(),1).toString()+" / ");
		System.out.print(C_amount+" / ");
		System.out.print(C_period+" / ");
		System.out.print(jt70.getValueAt(0,0).toString()+" / ");
		System.out.print(jt70.getValueAt(1,0).toString()+" / ");
		System.out.print(jt70.getValueAt(2,0).toString()+" / ");
		System.out.print(jt70.getValueAt(3,0).toString()+" / ");
		System.out.print(jt70.getValueAt(4,0).toString()+" / ");
		System.out.println(jt70.getValueAt(5,0).toString());*/
	}
	
	private void btn92() {
		if (jt7.getSelectedRow() == -1) {
			JOptionPane.showMessageDialog(null, "조회하실 계약을 선택해주세요.");
			return;
		}
		int yesno = JOptionPane.showConfirmDialog
		(this, "삭제하시겠습니까?","confirm",JOptionPane.YES_NO_OPTION);	// 예(0), 아니오(1)
		if (yesno == 0) {
			// 계약코드
			String prodCode = jt7.getValueAt(jt7.getSelectedRow(),0).toString();
			dbdao.deleteContract(prodCode);
			int[] sr = jt7.getSelectedRows(); int[] sc = jt70.getSelectedRows();
			for(int i : sr) dtm7.removeRow(jt7.getSelectedRow());
			for(int i : sc) {
				dtm70.removeRow(jt70.getSelectedRow());
				calculatefee();
			}
		} else if (yesno == 1) return;
	}
	
	/* MouseListener
	@Override
	public void mouseClicked(MouseEvent e) {
		// 클릭한 좌표 콘솔에 출력
		System.out.println("위치 :"+ e.getX()+" , "+ e.getY());
		
		/* MouseListener ml;
		JLabel text = null;
		System.out.println("click count" +e.getClickCount());
		if(e.getButton()==MouseEvent.BUTTON1) {
			text.setLocation(e.getX(), e.getY());
		} */
	/*}
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
	
	class JP20 extends JFrame implements ActionListener {
		private JTable jt23;
		private DefaultTableModel dtm23;
		private JButton btn21, btn22, btn23, btn24;
		//private JTextField jtf21, jtf22, jtf23, jtf24;
		private JTextField jtf21 = new JTextField(10);
		private JTextField jtf22 = new JTextField(10);
		private JTextField jtf23 = new JTextField(10);
		private JTextField jtf24 = new JTextField(10);
		private DefaultTableCellRenderer dtcr;
		private String code;
		
		public JP20(String searchRnumber) {
			setLayout(null);
			setBounds(500,200,420,600);
			setTitle("피보험자 등록");
			JLabel jl21 = new JLabel("주민등록번호"); JLabel jl22 = new JLabel("피보험자");
			JLabel jl23 = new JLabel("병력"); JLabel jl24 = new JLabel("주소");
			btn23 = new JButton("검색");
			btn21 = new JButton("등록"); btn22 = new JButton("취소");
			this.add(jl21); this.add(jtf21); this.add(jl22); this.add(jtf22);
			this.add(jl23); this.add(jtf23); this.add(jl24); this.add(jtf24);
			this.add(btn23); this.add(btn21); this.add(btn22);
			btn23.addActionListener(this);
			btn21.addActionListener(this); btn22.addActionListener(this);
			jtf21.setText(searchRnumber);
			jl21.setBounds(10,10,100,25); jtf21.setBounds(120,10,180,25);
			jl22.setBounds(10,50,100,25); jtf22.setBounds(120,50,180,25);
			jl23.setBounds(10,90,100,25); jtf23.setBounds(120,90,180,25);
			btn23.setBounds(320,90,60,25);
			jl24.setBounds(10,130,100,25); jtf24.setBounds(120,130,180,25);
			btn21.setBounds(140,180,60,25); btn22.setBounds(210,180,60,25);
			jp23();
			btn24 = new JButton("선택");
			this.add(btn24); btn24.setBounds(170,500,60,25);
			btn24.addActionListener(this);
			setVisible(true);
			
			dtcr = new DefaultTableCellRenderer() {
				public Component getTableCellRendererComponent
						(JTable table, Object value, boolean isSelected,
						boolean hasFocus, int row, int column) {
					JCheckBox box= new JCheckBox();
					box.setSelected(((Boolean)value).booleanValue());
					box.setHorizontalAlignment(JLabel.CENTER);
					return box;
				}
			};
			
			return;
		}
		
		private void jp23() {
			String[] headings23 = new String[] {"v","질병코드","질병명"};
			dtm23 = new DefaultTableModel(headings23, 0) {
				public Class<?> getColumnClass(int column) {
					switch(column) {
						case 0:	return Boolean.class;
						case 1: return String.class;
						case 2: return String.class;
						default: return String.class;
					}
				}
			};
		
			jt23 = new JTable(dtm23);
			jt23.getColumn("v").setCellRenderer(dtcr);
			JCheckBox box = new JCheckBox();
			box.setHorizontalAlignment(JLabel.CENTER);
			jt23.getColumn("v").setCellEditor(new DefaultCellEditor(box));
			jt23.getColumn("v").setPreferredWidth(50);
			jt23.getColumn("질병코드").setPreferredWidth(80);
			jt23.getColumn("질병명").setPreferredWidth(200);
			jt23.setPreferredScrollableViewportSize(new Dimension(330, 240));
			jt23.setFillsViewportHeight(true);
			JScrollPane js23 = new JScrollPane(jt23);
			getContentPane().add(js23);
			js23.setVisible(true);
			js23.setViewportView(jt23);
			jt23.setBounds(30,240,330,240); js23.setBounds(25,235,340,250);
			this.add(js23);
			//jtf23.setText((String) jt23.getValueAt(jt23.getSelectedRow(), 0));
		}
				
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == btn21) {
				for(int i=0; i<dtm2.getRowCount(); i++){
					dtm2.removeRow(i); i=-1;
				}
				String[] memberInfo = new String[4];
				memberInfo[0] = jtf21.getText(); memberInfo[1] = jtf22.getText();
				memberInfo[2] = jtf23.getText(); memberInfo[3] = jtf24.getText();
				dtm2.addRow(memberInfo); memberInfo = null;				
				dispose(); return;
			} else if(e.getSource() == btn22) {
				dispose();
			} else if (e.getSource() == btn23) {
				for(int i=0; i<dtm23.getRowCount(); i++){
					dtm23.removeRow(i);	i=-1;
				}
				String searchD = jtf23.getText();
				ArrayList<DiseaseDTO> dlist					
					= dbdao.selectDiseaseDTO(searchD);
				for(DiseaseDTO dFor : dlist) {
					Vector<Object> v = new Vector<Object>();
					v.add(false); v.add(dFor.getD_id()); v.add(dFor.getD_name());
					dtm23.addRow(v);
				}
				return;
			} else if (e.getSource() == btn24) {
				Boolean checked = Boolean.valueOf
						(jt23.getValueAt(jt23.getSelectedRow(),0).toString());
				if(checked) 
					jtf23.setText(jt23.getValueAt(jt23.getSelectedRow(),1).toString());
				// 복수의 것을 선택할 때
		        /*for(int i=0;i<jt23.getRowCount();i++) {
		        	Boolean checked = Boolean.valueOf(jt23.getValueAt(i, 0).toString());
			        String col = jt23.getValueAt(i, 1).toString();
			        if(checked) JOptionPane.showMessageDialog(null, col);
		        }*/
			}
		}
	}
}