package Main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import InsuranceDTO.ContractDTO;
import InsuranceDTO.DiseaseDTO;
import InsuranceDTO.DuePaymentDTO;
import InsuranceDTO.InsuranceAdminDTO;
import InsuranceDTO.InsuranceMainContractDTO;
import InsuranceDTO.InsuranceProductDTO;
import InsuranceDTO.InsuranceSpecialContractDTO;
import InsuranceDTO.MemberDTO;
import lombok.Getter;

public class DbDAO {
	
	private String driver = "oracle.jdbc.driver.OracleDriver";
    private String url = "jdbc:oracle:thin:@localhost:1521:orcl";
    private String id = "system";
    private String pass = "1111";
    private static Connection conn = null;
    private static DbDAO dbdao = null;
	private ArrayList<DbDAO> mcID = new ArrayList<>();
	
	@Getter
	private String adminName;
    
	private DbDAO() {
		init();
	}
	
	private void init() {
		try {
			Class.forName(driver);
			System.out.println("클래스 load 성공");
		} catch (ClassNotFoundException e){
			System.out.println("클래스 load 실패");
			e.printStackTrace();
		}
	}
	
    public Connection getConnection() {
        try {
        	conn = DriverManager.getConnection(url, id, pass);
        	System.out.println("DB 연결 완료");
        } catch(Exception e) {
            System.out.println("DB 연결 실패");
            conn = null;
        }
        return conn;
    }

    public static DbDAO getInstance() {
        if (dbdao == null) {
            dbdao = new DbDAO();
        }
        return dbdao;
    }
    
	/* 피보험자 추가
    public void insertMember(MemberDTO mdto) {
		PreparedStatement pstmt = null;
		try {
			if(getConnection() != null) {
				String sql = "insert into TestMember values (?)";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, mdto.getM_registrationnumber());
				//pstmt.setString(2, mdto.getM_name());
				//pstmt.setString(3, mdto.getM_adress());
				pstmt.executeUpdate();
			}
		} catch (Exception e) {
		} finally {
			try {
				pstmt.close();
				conn.close();				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}*/
	
	public void insertAdmin(InsuranceAdminDTO admdto) {
		PreparedStatement pstmt = null;
		try {
			if(getConnection() != null) {
				String sql = "insert into Admin values (?,?,?)";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, admdto.getAdmin_id());
				pstmt.setString(2, admdto.getAdmin_name());
				pstmt.setString(3, admdto.getAdmin_password());
				pstmt.executeUpdate();
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,"관리자 등록에 실패했습니다. 다시 시도해주세요.");
		} finally {
			try {
				pstmt.close();
				conn.close();				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}		
	}
	
	public void insertMember(MemberDTO mdto) {
		PreparedStatement pstmt = null;
		try {
			if(getConnection() != null) {
				String sql = "insert into Member values (mem_seq.NEXTVAL,?,?,?,?)";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, mdto.getM_registrationnumber());
				pstmt.setString(2, mdto.getM_name());
				pstmt.setString(3, mdto.getM_addr());
				pstmt.setString(4, mdto.getM_diseasehistory());
				pstmt.executeUpdate();
			}
		} catch (Exception e) {
		} finally {
			try {
				pstmt.close();
				conn.close();				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}		
	}

	// 피보험자 ID를 먼저 찾은 뒤 계약정보에 피보험자 ID를 입력하기 위해 만듦 
	public String selectMemberID(String tempReg) {
		Statement stmt = null; ResultSet rs = null; String memberID = null;
		try {
			if(getConnection() != null) {
				String sql = "select M_ID from Member "
						+ "where M_RegNum = '"+tempReg+"'";
				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);
				rs.next();
				memberID = rs.getString("M_ID");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) rs.close();
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return memberID;
	}
	
	public void insertContract(ContractDTO cdto) {
		PreparedStatement pstmt = null;
		try {
			if(getConnection() != null) {
				String sql = "insert into Contract values "
						+ "(con_seq.NEXTVAL,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, cdto.getM_ID());
				pstmt.setString(2, cdto.getP_ID());
				pstmt.setInt(3, cdto.getC_amount());
				pstmt.setInt(4, cdto.getC_period());
				pstmt.setString(5, cdto.getMC_ID());
				pstmt.setString(6, cdto.getSC_ID1());
				pstmt.setString(7, cdto.getSC_ID2());
				pstmt.setString(8, cdto.getSC_ID3());
				pstmt.setString(9, cdto.getSC_ID4());
				pstmt.setString(10, cdto.getSC_ID5());
				pstmt.setString(11, cdto.getSC_ID6());
				pstmt.setString(12, cdto.getSC_ID7());
				pstmt.setString(13, cdto.getSC_ID8());
				pstmt.setString(14, cdto.getSC_ID9());
				pstmt.executeUpdate();
			}
			JOptionPane.showMessageDialog(null,"계약 등록에 성공하였습니다.");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,"계약 등록에 실패했습니다. 다시 시도해주세요.");
		} finally {
			try {
				pstmt.close();
				conn.close();				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}	
	}
	
	public ArrayList<ContractDTO> selectContract(int i, String code) {
		ArrayList<ContractDTO> contArr = new ArrayList<>(); String sql = null;
		PreparedStatement pstmt = null; ResultSet rs = null;
		try {
			if(getConnection() != null) {
				if(i == 1) {
					sql = "select * from contract " 
							+ "where M_ID = '"+code+"' order by P_ID ASC";
				} else if (i == 2) {
					sql = "select * from contract "
							+ "where C_ID = '"+code+"' order by P_ID ASC";
				}
				pstmt = conn.prepareStatement(sql);
				rs = pstmt.executeQuery();
			}
			while(rs.next()) {
				ContractDTO contdto = new ContractDTO();
				contdto.setC_ID(rs.getString("C_ID"));	// 계약 코드
				contdto.setM_ID(rs.getString("M_ID"));	// 피보험자
				contdto.setP_ID(rs.getString("P_ID"));	// 상품 코드
				contdto.setC_amount(rs.getInt("C_AMOUNT"));	// 가입금액
				contdto.setC_period(rs.getInt("C_PERIOD"));	// 가입기간
				contdto.setMC_ID(rs.getString("MC_ID"));	// 주계약 코드
				contdto.setSC_ID1(rs.getString("SC_ID1"));	// 특약 코드
				contdto.setSC_ID2(rs.getString("SC_ID2"));
				contdto.setSC_ID3(rs.getString("SC_ID3"));
				contdto.setSC_ID4(rs.getString("SC_ID4"));
				contdto.setSC_ID5(rs.getString("SC_ID5"));
				contdto.setSC_ID6(rs.getString("SC_ID6"));
				contdto.setSC_ID7(rs.getString("SC_ID7"));
				contdto.setSC_ID8(rs.getString("SC_ID8"));
				contdto.setSC_ID9(rs.getString("SC_ID9"));
				contArr.add(contdto);
			}
		} catch (Exception e) {
		} finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return contArr;
	}

	
	/*
	public String searchAdmin() {
		Statement stmt = null; ResultSet rs = null; String sql = null;
		try {
			if(getConnection() != null) {
				sql = "select * from InsuranceAdmin where Admin_name="+newID;
				//sql = "select Admin_id from InsuranceAdmin where Admin_id=?";
				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);
				}
			while(rs.next()) {
				InsuranceAdminDTO admdto = new InsuranceAdminDTO();
				admdto.setAdmin_id(rs.getString("Admin_id"));
				admdto.setAdmin_name(rs.getString("Admin_name"));
				admdto.setAdmin_password(rs.getString("Admin_password"));
				admArr.add(admdto);
			}
		} catch (Exception e) {
		} finally {
			try {
				rs.close(); stmt.close(); conn.close();				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}*/	

	public boolean checkID(int i, String call) {
		boolean idCheck = false;
		PreparedStatement pstmt = null; ResultSet rs = null; String sql = null;
		try {
			if(getConnection() != null) {
				if (i==1) {	// 관리자 등록시 ID 중복체크
					sql = "select ADMIN_ID from Admin";
				} else if (i==2) {	// 로그인 시 ID, 비밀번호 일치 확인
					sql = "select ADMIN_ID, ADMIN_PASSWORD, ADMIN_NAME from Admin";
				}
				pstmt = conn.prepareStatement(sql);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					if (i==1) {
						if(rs.getString("ADMIN_ID").equals(call)){
							idCheck = true;
						}
					} else if (i==2) {
						String bunch = rs.getString("ADMIN_ID")+ " / "
								+rs.getString("ADMIN_PASSWORD");
						if(bunch.equals(call)){
							adminName = rs.getString("ADMIN_NAME");
							idCheck = true;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return idCheck;
	}
	
	public void logon(int i, String id) {
		PreparedStatement pstmt = null; String sql = null;
		try {
			if(getConnection() != null) {
				if(i == 1) {
					sql = "insert into Logon values (?)";
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, id);
					pstmt.executeUpdate();
				} else if(i == 0) {
					sql = "delete from Logon where Admin_ID = '"+id+"'";
					pstmt = conn.prepareStatement(sql);
					pstmt.execute();
					JOptionPane.showMessageDialog(null,"로그아웃 되었습니다.");
				}
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,"로그아웃 하지 않은 계정이 있습니다. 나가실 때 로그아웃 해주세요.");
			e.printStackTrace();
		} finally {
			try {
				pstmt.close();
				conn.close();				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/*
	if(pstmt != null) pstmt.close();
	if(conn != null) conn.close();	
	 */
	
	// 관리자 이름 가져오그. statement로 가져오자
	/*} else if(i == 2) {
	sql = "select Admin_name from Admin where Admin_ID = '"+loginID+"'";
	pstmt = conn.prepareStatement(sql);
	rs = pstmt.executeQuery();
	while(rs.next()) {
		adminName = rs.getString("Admin_name");	
	}*/
	
	// 로그아웃
	/*
*/

	public ArrayList<InsuranceProductDTO>
	selectInsuranceProductDTO(String searchProd) {
		ArrayList<InsuranceProductDTO> prodArr = new ArrayList<>();
		PreparedStatement pstmt = null; ResultSet rs = null;
		try {
			if(getConnection() != null) {
				/* String sql = "select IP_id, IP_name from InsuranceProductTest
				 * where IP_name like '%"+searchProd+"%' and IP_name is not null";
				 * [like %]와 [not null]을 동시에 쓰는 건 의미가 없다. */
				String sql = "select P_ID, P_name "
						+ "from Product "
						+ "where P_name like '%"+searchProd+"%' "
						+ "order by P_ID ASC";
				pstmt = conn.prepareStatement(sql);
				rs = pstmt.executeQuery();
			}
			while(rs.next()) {
				InsuranceProductDTO proddto = new InsuranceProductDTO();
				proddto.setIP_id(rs.getString("P_ID"));
				proddto.setIP_name(rs.getString("P_name"));
				prodArr.add(proddto);
			}
		} catch (Exception e) {
		} finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return prodArr;
	}

	public String selectProduct(String prodCode) {
		Statement stmt = null; ResultSet rs = null; String prodName = null;
		try {
			if(getConnection() != null) {
				String sql = "select P_name from Product where P_ID = '"+prodCode+"'";
				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);
				rs.next();
				prodName = rs.getString("P_name");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) rs.close();
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return prodName;
	}
	
	public ArrayList<InsuranceMainContractDTO>
	selectInsuranceMainContractDTO(int i, String searchMC) {
		ArrayList<InsuranceMainContractDTO> mcArr = new ArrayList<>();
		PreparedStatement pstmt = null; ResultSet rs = null; String sql1 = null;
		try {
			if(getConnection() != null) {
				if (i == 1) {
					sql1 = "select MC_ID, MC_NAME, MC_min, MC_max, MC_FEE "
							+ "from MainContract "
							+ "where P_ID='"+searchMC+"'";	
				} else if (i == 2) {
					sql1 = "select MC_ID, MC_NAME, MC_min, MC_max, MC_FEE "
							+ "from MainContract "
							+ "where MC_ID='"+searchMC+"'";
				}
				pstmt = conn.prepareStatement(sql1);
				rs = pstmt.executeQuery();
			}
			while(rs.next()) {
				InsuranceMainContractDTO mcdto = new InsuranceMainContractDTO();
				mcdto.setMC_id(rs.getString("MC_ID"));
				mcdto.setMC_name(rs.getString("MC_NAME"));
				mcdto.setMC_min(rs.getInt("MC_min"));
				mcdto.setMC_max(rs.getInt("MC_max"));
				mcdto.setMC_fee(rs.getInt("MC_FEE"));
				mcArr.add(mcdto);
			}
		} catch (Exception e) {
		} finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return mcArr;
	}
	
	public ArrayList<InsuranceSpecialContractDTO>
	selectInsuranceSpecialContractDTO(int i, String sc) {
		ArrayList<InsuranceSpecialContractDTO> scArr = new ArrayList<>();
		PreparedStatement pstmt = null; ResultSet rs = null; String sql2 = null;
		try {
			if(getConnection() != null) {
				if (i == 1) {
					sql2 = "select SC_ID, SC_name, SC_pay, SC_fee "
							+ "from SpecialContract "
							+ "where MC_ID='"+sc+"'";
				} else if (i == 2) {
					sql2 = "select SC_ID, SC_name, SC_pay, SC_fee "
							+ "from SpecialContract "
							+ "where SC_ID='"+sc+"'";
				}
				pstmt = conn.prepareStatement(sql2);
				rs = pstmt.executeQuery();
			}
			while(rs.next()) {
				InsuranceSpecialContractDTO scdto
				= new InsuranceSpecialContractDTO();
				scdto.setSC_id(rs.getString("SC_ID"));
				scdto.setSC_name(rs.getString("SC_name"));
				scdto.setSC_pay(rs.getInt("SC_pay"));
				scdto.setSC_fee(rs.getInt("SC_fee"));
				scArr.add(scdto);
			}				
		} catch (Exception e) {
		} finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return scArr;					
	}

	public ArrayList<MemberDTO> memberselectAll(String searchRnumber) {
		ArrayList<MemberDTO> mlist = new ArrayList<>();
		String sql = "select M_RegNum, M_name, D_ID, M_addr, M_ID "
				+ "from Member "
				+ "where M_RegNum = '"+searchRnumber+"'";
				/* 검색어 일부만으로 조회할 때 
				"where M_RegNum like '%' || ? || '%'";*/
		try {
			Connection conn = getConnection();
			PreparedStatement stmt = conn.prepareStatement(sql);
			//stmt.setString(1, searchRnumber);
	        ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				MemberDTO mdto = new MemberDTO();	
				mdto.setM_registrationnumber(rs.getString("M_RegNum"));
			    mdto.setM_name(rs.getString("M_name"));
			    mdto.setM_diseasehistory(rs.getString("D_ID"));
			    mdto.setM_addr(rs.getString("M_addr"));
			    mdto.setM_ID(rs.getString("M_ID"));
				mlist.add(mdto);
			}
		} catch (Exception e) { 
			e.printStackTrace();
		}
		/*if (mlist.isEmpty()) {
			return null;
		}*/
		return mlist;
	}
	
	public ArrayList<MemberDTO> memberSelectAll2(String code) {
		ArrayList<MemberDTO> mlist2 = new ArrayList<>();
		String sql = "SELECT M_name, M_RegNum, D_ID, M_ID "
				+ "FROM Member "
				+ "WHERE M_RegNum = '" + code + "'";
		try {
			Connection conn = getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs  = stmt.executeQuery(sql);
			while(rs.next()) {
				MemberDTO mdto = new MemberDTO();
				mdto.setM_name(rs.getString("M_name"));
		        mdto.setM_registrationnumber(rs.getString("M_RegNum"));
			    mdto.setM_diseasehistory(rs.getString("D_ID"));
			    mdto.setM_ID(rs.getString("M_ID"));	// 피보험자 코드는 따로 표기 예정 
				mlist2.add(mdto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mlist2;
	}

	public ArrayList<DiseaseDTO> selectDiseaseDTO(String searchD) {
		PreparedStatement pstmt = null; ResultSet rs = null;
		ArrayList<DiseaseDTO> dlist = new ArrayList<>();
		try {
			if(getConnection() != null) {
				String sql = "select D_id, D_name "
						+ "from Disease "
						+ "where D_name like '%' || ? || '%' "
						+ "order by D_id ASC";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, searchD);
		        rs = pstmt.executeQuery();
			}
			while(rs.next()) {
				DiseaseDTO ddto = new DiseaseDTO();	
				ddto.setD_id(rs.getString("D_id"));
		        ddto.setD_name(rs.getString("D_name"));
		        dlist.add(ddto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return dlist;
	}

	public int selectDisease(String ill) {
		Statement stmt = null; ResultSet rs = null; int ill2 = 0; 
		try {
			if(getConnection() != null) {
				String sql = "select DR_rate " + 
						"from DiseaseRate dr right outer join Disease d " + 
						"on dr.D_type = d.D_type " + 
						"where d.D_id = '"+ill+"'";
				stmt = conn.createStatement();
		        rs = stmt.executeQuery(sql);
		        //rs.next();
		        while(rs.next()) {
		        	ill2 = rs.getInt("DR_rate");	
		        }
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) rs.close();
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return ill2;
	}

	public ArrayList<DuePaymentDTO> selectDuePayment(String conCode, int conPeriod) {
		ArrayList<DuePaymentDTO> dplist = new ArrayList<>();
		Statement stmt = null; ResultSet rs = null; double[] rateDue = null; 
		try {
			if(getConnection() != null) {
				String sql = "select DP_1Year, DP_2Year, DP_3Year, DP_5Year, "
						+ "DP_10Year, DP_15Year, DP_20Year, DP_30Year " 
						+ "from Payment "
						+ "where MC_id = '"+conCode+"'";
				stmt = conn.createStatement();
		        rs = stmt.executeQuery(sql);
		        rs.next();
		        DuePaymentDTO dpdto = new DuePaymentDTO();
		        dpdto.setDP_1Year(rs.getDouble("DP_1Year")*10000);
	        	if(conPeriod >= 2) dpdto.setDP_2Year(rs.getDouble("DP_2Year")*10000);
	        	if(conPeriod >= 3) dpdto.setDP_3Year(rs.getDouble("DP_3Year")*10000);
	        	if(conPeriod >= 4) dpdto.setDP_5Year(rs.getDouble("DP_5Year")*10000);
	        	if(conPeriod >= 5) dpdto.setDP_10Year(rs.getDouble("DP_10Year")*10000);
	        	if(conPeriod >= 6) dpdto.setDP_15Year(rs.getDouble("DP_15Year")*10000);
	        	if(conPeriod >= 7) dpdto.setDP_20Year(rs.getDouble("DP_20Year")*10000);
	        	if(conPeriod >= 8) dpdto.setDP_30Year(rs.getDouble("DP_30Year")*10000);
	        	dplist.add(dpdto);
		        // 소수점을 피하기 위해 10000을 곱하여 int 형태로 만든다. double로 하면 2진수의 한계가 기다림.
		        /*rateDue[0] = rs.getDouble("DP_1Year");
	        	if(conPeriod>=2) rateDue[1]=rs.getDouble("DP_2Year");
	        	else if(conPeriod>=3) rateDue[2]=rs.getDouble("DP_3Year");
	        	else if(conPeriod>=4) rateDue[3]=rs.getDouble("DP_5Year");
	        	else if(conPeriod>=5) rateDue[4]=rs.getDouble("DP_10Year");
	        	else if(conPeriod>=6) rateDue[5]=rs.getDouble("DP_15Year");
	        	else if(conPeriod>=7) rateDue[6]=rs.getDouble("DP_20Year");
	        	else if(conPeriod>=8) rateDue[7]=rs.getDouble("DP_30Year");
	        	for (int i=0; i<rateDue.length; i++) {
	        		System.out.println(rateDue[i]);
	        		
	        	}*/
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) rs.close();
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return dplist;
	}

	public void deleteContract(String prodCode) {
		PreparedStatement stmt = null;
		try {
			String sql = "delete from Contract where C_ID=?";
			if(getConnection() != null) {
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, prodCode);
				stmt.execute();
				JOptionPane.showMessageDialog(null, "선택하신 계약이 삭제되었습니다.");
			}
		} catch (Exception e) {
		} finally {
			try {
				stmt.close();
				conn.close();				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/*
	private int today(int age) {
		SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMDD");
		Calendar c1 = Calendar.getInstance();
		String strToday = sdf.format(c1.getTime());
		Integer.parseInt(strToday)
		age = ( - age) / 365;
		return age;
	}*/
	
	/*
	public boolean checkLogin(String check) {
		boolean idCheck = false;
		PreparedStatement pstmt = null; ResultSet rs = null; String sql = null;
		try {
			if(getConnection() != null) {
				sql = "select Admin_id from InsuranceAdmin";
				pstmt = conn.prepareStatement(sql);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					if(rs.getString("Admin_id").equals(newID)){
						idCheck = true;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return idCheck;
	}*/
	
	/* 피보험자 전체 목록 확인용
	public ArrayList<MemberDTO> selectMemberDTO() {
		ArrayList<MemberDTO> mdtoArr = new ArrayList<>();
		Statement stmt = null; ResultSet rs = null; String sql = null;
		try {
			if(getConnection() != null) {
				sql = "select registerNumber from TestMember";
				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);
				}
				while(rs.next()) {
					MemberDTO mdto = new MemberDTO();
					mdto.setM_registrationNumber(rs.getString("registerNumber"));
					mdtoArr.add(mdto);
				}
		} catch (Exception e) {
		} finally {
			try {
				rs.close(); stmt.close(); conn.close();				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return mdtoArr;
	}
	*/
	
	/* 관리자 정보 전체 검색
	public ArrayList<InsuranceAdminDTO> selectInsuranceAdminDTO(String newID) {
		ArrayList<InsuranceAdminDTO> admArr = new ArrayList<>();
		Statement stmt = null; ResultSet rs = null; String sql = null;
		try {
			if(getConnection() != null) {
				sql = "select * from InsuranceAdmin where Admin_id="+newID;
				//sql = "select Admin_id from InsuranceAdmin where Admin_id=?";
				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);
				}
			while(rs.next()) {
				InsuranceAdminDTO admdto = new InsuranceAdminDTO();
				admdto.setAdmin_id(rs.getString("Admin_id"));
				admdto.setAdmin_name(rs.getString("Admin_name"));
				admdto.setAdmin_password(rs.getString("Admin_password"));
				admArr.add(admdto);
			}
		} catch (Exception e) {
		} finally {
			try {
				rs.close(); stmt.close(); conn.close();				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return admArr;		
	}
	*/
	
    /*
    public static void main(String[] args) {
        // static 메소드에서 인스턴스 메소드 호출을 위해 객체 생성
        DbDAO dao = DbDAO.getInstance();
        dao.getConnection();
    }
    */
}
