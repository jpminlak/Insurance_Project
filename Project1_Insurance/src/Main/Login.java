package Main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import InsuranceDTO.InsuranceAdminDTO;

public class Login extends JFrame implements ActionListener {
	private DbDAO dbdao = DbDAO.getInstance();
	//ArrayList<InsuranceAdminDTO> admArr = new ArrayList<InsuranceAdminDTO>();
	
	private JLabel jl1 = new JLabel("로그인이 필요합니다.");
	private JButton btn1 = new JButton("로그인");
	private JButton btn2 = new JButton("관리자 등록");
	private JPanel jp1 = new JPanel();
	private Image bg;
	
	private JLabel id = new JLabel("ID");
	private JLabel password = new JLabel("암호");
	private DefaultListModel model = new DefaultListModel();
	// private JList jlist1 = new JList(model);
	private JTextField idField = new JTextField(10);
	private JPasswordField pwField = new JPasswordField(10);

	public Login() {
		// bg=Toolkit.getDefaultToolkit().getImage("C:/work/loginimage.jpg");
		bg = new ImageIcon("./src/img/loginimage.jpg").getImage();
		this.setLocation(0, 0);
		this.setSize(1250,750);
		
		this.setTitle("로그인");
		this.add("North",jl1);
	
		login();

		/*
		JPanel image = new JPanel() {public void paint(Graphics g) {
			super.paint(g);
			g.drawImage(bg,0,80,getWidth(),getHeight()-110,null);	// image,x,y,width,height,observer
			this.setOpaque(false);
			super.paintComponents(g);
			
			this.add(image);
		}};*/
		this.add(new JPanel() {public void paint(Graphics g) {
			super.paint(g);
			g.drawImage(bg,0,5,getWidth(),getHeight(),null);	// image,x,y,width,height,observer
			this.setOpaque(false);
			super.paintComponents(g);
		}});
		
		// this.add("East",jlist1);	// 출력 확인용
		this.setVisible(true);
		this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
		btn1.addActionListener(this); btn2.addActionListener(this);
	}

	private void login() {
		jp1.setLayout(null);
		jp1.setBounds(820,180,250,120);
		jp1.setBackground(Color.white);
		jp1.add(id); jp1.add(idField); jp1.add(password); jp1.add(pwField);
		jp1.add(btn1); jp1.add(btn2);
		this.add(jp1);
		id.setBounds(30,10,50,25); idField.setBounds(80,10,130,25);
		password.setBounds(30,45,50,25); pwField.setBounds(80,45,130,25);
		btn1.setBounds(20,80,80,25); btn2.setBounds(120,80,105,25);
	}

	@Override
	public void actionPerformed(ActionEvent a) {
		if(a.getSource() == btn1) {
			String id = idField.getText();
			String password = pwField.getText();
			String check = id+ " / " +password;	// 출력 확인용
			model.addElement(check);
			//loginCheck();
			if (dbdao.checkID(2, check)) {
				dbdao.logon(1, id);
				dispose(); new InfoDAO(id);
			} else {
				JOptionPane.showMessageDialog(this,"로그인 실패하셨습니다.");
			}
		} else if (a.getSource() == btn2) {
			Join join = new Join();
		}
	}

	class Join extends JFrame implements ActionListener {
		private JTextField jtf21 = new JTextField(10);
		private JPasswordField jpf22 = new JPasswordField(10);
		private JTextField jtf23 = new JTextField(10);
		private JButton btn21 = new JButton("중복확인");
		private JButton btn24 = new JButton("등록");
		
		public Join() {
			setLayout(null); setVisible(true);
			setBounds(500,200,350,250);
			setTitle("관리자 등록");
			JLabel jl21 = new JLabel("ID");
			JLabel jl22 = new JLabel("비밀번호");
			JLabel jl23 = new JLabel("이름");
			this.add(jl21); this.add(jtf21); this.add(jl22); this.add(jpf22);
			this.add(jl23); this.add(jtf23); this.add(btn21); this.add(btn24);
			btn21.addActionListener(this); btn24.addActionListener(this);
			jl21.setBounds(20,20,70,25); jtf21.setBounds(90,20,100,25);
			btn21.setBounds(210,20,100,25);
			jl22.setBounds(20,55,70,25); jpf22.setBounds(90,55,100,25);
			jl23.setBounds(20,90,70,25); jtf23.setBounds(90,90,100,25);
			btn24.setBounds(120,130,60,25);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			String newID = jtf21.getText();
			if (newID.length() < 4) {
				JOptionPane.showMessageDialog
				(null,"ID는 4자리 이상, (로마자)20자 이하를 입력해주세요.");
				jtf21.setText(""); return;
			}
			if (e.getSource() == btn21) {
				if(newID.equals("")) {
					JOptionPane.showMessageDialog(null,"사용하실 ID를 입력하세요.");
					return;
				}
				if (dbdao.checkID(1, newID)) {
					JOptionPane.showMessageDialog(this,"ID가 중복됩니다. 다른 ID를 사용해주세요.");
					return;
				} else {
					JOptionPane.showMessageDialog(this,"사용가능한 ID입니다.");
				}
			} else if (e.getSource() == btn24) {
				if (newID == null) {
					JOptionPane.showInputDialog("사용하실 ID를 입력하세요.");
					return;
				}
				if (dbdao.checkID(1, newID)) {
					JOptionPane.showMessageDialog(this,"ID가 중복됩니다. 다른 ID를 사용해주세요.");
					return;
				} else {
					// System.out.println("ID 사용 가능");
					String newPW = jpf22.getText();
					String newAdminName = jtf23.getText();
					if (newPW.length() < 4) {
						JOptionPane.showMessageDialog(null,"비밀번호는 4자리 이상 입력해주세요.");
						return;
					} else if (newAdminName.length() < 1) {
						JOptionPane.showMessageDialog(null,"이름을 입력해주세요.");
						return;
					} else {
						InsuranceAdminDTO admdto = new InsuranceAdminDTO();
						admdto.setAdmin_id(newID);
						admdto.setAdmin_password(newPW);
						admdto.setAdmin_name(newAdminName);
						dbdao.insertAdmin(admdto);
						idField.setText(newID);
						JOptionPane.showMessageDialog(null,"관리자 등록에 성공했습니다.");
					}
				}
			}
		}	
	}
		/* 관리자 전체 목록 확인용
		ArrayList<InsuranceAdminDTO> admlist = dbdao.selectInsuranceAdminDTO();
		for(InsuranceAdminDTO admdtoFor : admlist) {
			admdtoFor.prt();
		}*/
}