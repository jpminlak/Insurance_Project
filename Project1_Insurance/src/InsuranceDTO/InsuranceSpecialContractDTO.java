package InsuranceDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InsuranceSpecialContractDTO {
	private String SC_id;		// 특약 코드
	private String SC_name;		// 특약 이름
	private int SC_pay;		// 특약 가입금액
	private int SC_fee;		// 특약 기준 보험료 

	private void prt() {
		System.out.println(SC_id+ " / " +SC_name+ " / " +SC_pay+ " / " +SC_fee);
	}
}
