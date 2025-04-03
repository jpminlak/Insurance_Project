package InsuranceDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InsuranceProductDTO {
	private String IP_id;	// 보험상품 ID
	private String IP_name;	// 보험상품명
	private String M_id;
	private String MC_id;
	
	public void prt() {
		System.out.println(IP_id+ " / " +IP_name);
	}
}
