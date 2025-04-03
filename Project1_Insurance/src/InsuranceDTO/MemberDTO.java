package InsuranceDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberDTO {
	private String M_ID;		// 피보험자ID(기본키,고유번호)
	private String M_name;		// 피보험자이름
	private String M_registrationnumber;	// 피보험자 주민번호
	private String M_addr;	// 피보험자 주소 
	private String M_diseasehistory;	// 피보험자 질병이력 

	public void prt() {
		System.out.print(M_name);
		System.out.print(" / "+M_registrationnumber);
		System.out.print(" / "+M_diseasehistory);
		System.out.println(" / "+M_addr);
	}
		
	public void prt2() {
		System.out.print(M_name);
		System.out.println(" / "+M_diseasehistory);
	}
}
