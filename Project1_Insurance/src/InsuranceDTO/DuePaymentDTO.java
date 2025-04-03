package InsuranceDTO;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class DuePaymentDTO {
	
	private String DP_id;
	private String IP_id;
	private String M_id;
	private int DP_payment;
	private int DP_totalamount;
	private String DP_duedateofpayment;
	private String DP_paymentstatus;
	
	private double DP_1Year;
	private double DP_2Year;
	private double DP_3Year;
	private double DP_5Year;
	private double DP_10Year;
	private double DP_15Year;
	private double DP_20Year;
	private double DP_30Year;
}
