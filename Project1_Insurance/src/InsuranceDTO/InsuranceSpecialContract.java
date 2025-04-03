package InsuranceDTO;

import java.util.*;

public class InsuranceSpecialContract {
	
	private String SC_id;				//특약 코드 고유키
	private String SC_name;				//특약 이름(명칭)
	private String SC_detail;			//특약 설명
	private String SC_type;				//특약유형(정기,종신)
	private String SC_guranteedtype;	//보장유형(입원,암,사망)
	private int SC_contractperiod;		//특약기간(월/년)
	private int SC_insurancefee;		//특약 보험료 	
	
	public String getSC_id() {
		return SC_id;
	}
	public void setSC_id(String sC_id) {
		this.SC_id = SC_id;
	}
	public String getSC_name() {
		return SC_name;
	}
	public void setSC_name(String sC_name) {
		SC_name = sC_name;
	}
	public String getSC_detail() {
		return SC_detail;
	}
	public void setSC_detail(String sC_detail) {
		SC_detail = sC_detail;
	}
	public String getSC_type() {
		return SC_type;
	}
	public void setSC_type(String sC_type) {
		SC_type = sC_type;
	}
	public String getSC_guranteedtype() {
		return SC_guranteedtype;
	}
	public void setSC_guranteedtype(String sC_guranteedtype) {
		SC_guranteedtype = sC_guranteedtype;
	}
	public int getSC_contractperiod() {
		return SC_contractperiod;
	}
	public void setSC_contractperiod(int sC_contractperiod) {
		SC_contractperiod = sC_contractperiod;
	}
	public int getSC_insurancefee() {
		return SC_insurancefee;
	}
	public void setSC_insurancefee(int sC_insurancefee) {
		SC_insurancefee = sC_insurancefee;
	}

}
