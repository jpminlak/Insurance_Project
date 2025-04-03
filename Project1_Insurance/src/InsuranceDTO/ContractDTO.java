package InsuranceDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContractDTO {
	private String C_ID;	// 계약 코드
	private String M_ID;	// 피보험자 코드
	private String P_ID;	// 상품 코드
	private int C_amount;	// 가입금액
	private int C_period;	// 가입기간
	private String MC_ID;	// 주계약 코드
	private String SC_ID1;	// 특약 코드
	private String SC_ID2;
	private String SC_ID3;
	private String SC_ID4;
	private String SC_ID5;
	private String SC_ID6;
	private String SC_ID7;
	private String SC_ID8;
	private String SC_ID9;
	
	public void prt() {
		
	}
}
