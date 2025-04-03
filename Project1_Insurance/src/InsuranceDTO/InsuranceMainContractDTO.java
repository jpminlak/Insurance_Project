package InsuranceDTO;

import java.util.*;
import lombok.*;

@Getter
@Setter
public class InsuranceMainContractDTO {
	private String MC_id;		// 주계약 코드
	private String P_ID;		// 상품코드
	private String MC_name;		// 주계약 이름 
	private int MC_min;		// 주계약 최소 가입금액
	private int MC_max;		// 주계약 최대 가입금액
	private int MC_fee;		// 주계약 보험료
	
	public void prt() {
		System.out.print(MC_id+" / ");
		System.out.print(MC_name+" / ");
		System.out.print(MC_min+" / ");
		System.out.print(MC_max+" / ");
		System.out.println(MC_fee+" / ");
	}
}
