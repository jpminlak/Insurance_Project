package InsuranceDTO;

import lombok.*;

@Getter
@Setter
public class InsuranceAdminDTO {
	private String Admin_id;//������ID
	private String Admin_name;//�������̸�
	private String Admin_password;//������ ��й�ȣ
	
	public void prt() {
		System.out.println("Admin_id : "+this.Admin_id);
		System.out.println("Admin_name : "+this.Admin_name);
		System.out.println("Admin_password : "+this.Admin_password);
	}
}
	
	