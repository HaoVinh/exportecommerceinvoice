package trong.lixco.com.info;

import lombok.Data;

@Data
public class TheoDoiSLBH_SX_Data {
	
	private String kenh;
	private String kenhCSS;
	private int kenhLever;
	private double dukienSLban;
	private double thucxuat;
	private double khconlai;
	private double uocconlai;
	private double tonkhodauthang;
	private double tonkhocuoi;
	private double kehoachsx;
	private double dasx;
	private double kehoachsxconlai;
	
	private String kenhDT;
	private String thuockenh;
	
	private boolean duocnhap;
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TheoDoiSLBH_SX_Data other = (TheoDoiSLBH_SX_Data) obj;
		if (kenh == null) {
			if (other.kenh != null)
				return false;
		} else if (!kenh.equals(other.kenh))
			return false;
		if (kenhCSS == null) {
			if (other.kenhCSS != null)
				return false;
		} else if (!kenhCSS.equals(other.kenhCSS))
			return false;
		if (kenhDT == null) {
			if (other.kenhDT != null)
				return false;
		} else if (!kenhDT.equals(other.kenhDT))
			return false;
		if (kenhLever != other.kenhLever)
			return false;
		return true;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((kenh == null) ? 0 : kenh.hashCode());
		result = prime * result + ((kenhCSS == null) ? 0 : kenhCSS.hashCode());
		result = prime * result + ((kenhDT == null) ? 0 : kenhDT.hashCode());
		result = prime * result + kenhLever;
		return result;
	}
	
	

}
