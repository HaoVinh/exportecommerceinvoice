package trong.lixco.com.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class MyUtil {
	static DecimalFormat myFormatter;
	static DecimalFormat myFormatterle;
	static SimpleDateFormat ddMMyyyy;
	static SimpleDateFormat ddMMyyyyapi;
	static SimpleDateFormat ddMMyyyyStr;
	static SimpleDateFormat ddMMyy;
	static SimpleDateFormat ddMMyyyyhh;
	static SimpleDateFormat dd;
	static SimpleDateFormat hhmm;

	static {
		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
		otherSymbols.setDecimalSeparator(',');
		otherSymbols.setGroupingSeparator('.');
		myFormatter = new DecimalFormat("###,###,###,###,###", otherSymbols);
		myFormatterle = new DecimalFormat("###,###,###,###,###.##", otherSymbols);
		ddMMyyyy = new SimpleDateFormat("dd/MM/yyyy");
		ddMMyyyyapi = new SimpleDateFormat("dd-MM-yyyy");
		ddMMyyyyStr = new SimpleDateFormat("ddMMyyyy");
		ddMMyy = new SimpleDateFormat("ddMMyy");
		ddMMyyyyhh = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		hhmm = new SimpleDateFormat("HH:mm");
		dd = new SimpleDateFormat("dd");
	}

	public static String dinhdangso(int so) {
		try {
			return myFormatter.format(so);
		} catch (Exception e) {
			return so + "";
		}
	}

	public static String dinhdangso(double quantity) {
		try {
			return myFormatter.format(quantity);
		} catch (Exception e) {
			return quantity + "";
		}
	}

	public static String dinhdangso2sole(double quantity) {
		try {
			return myFormatterle.format(quantity);
		} catch (Exception e) {
			return quantity + "";
		}
	}
	
	public static int chuyensangso(String so) {
		try {
			return myFormatter.parse(so).intValue();
		} catch (Exception e) {
			return 0;
		}
	}

	public static Date thembotngay(Date date, int number) {
		if (date == null)
			return null;
		Calendar sd = Calendar.getInstance();
		sd.setTime(date);
		sd.add(Calendar.DATE, number);
		return sd.getTime();
	}

	public static Date thembotthang(Date date, int number) {
		if (date == null)
			return null;
		Calendar sd = Calendar.getInstance();
		sd.setTime(date);
		sd.add(Calendar.MONTH, number);
		return sd.getTime();
	}
	public static boolean ngaygiuahaingay(Date dmain,Date date1, Date date2) {
		if((dmain.after(date1)||dmain.equals(date1))&&(dmain.before(date2)||dmain.equals(date2)))
			return true;
		return false;
	}
	

	public static Date loaibogio(Date date) {
		try {
			return ddMMyyyy.parse(ddMMyyyy.format(date));
		} catch (Exception e) {
			return null;
		}
	}
	public static double trulaygio(Date date1,Date date2) {
		try {
			 double diff = date1.getTime() - date2.getTime();
			 return  diff / (60 * 60 * 1000);
		} catch (Exception e) {
			return 0.0;
		}
	}
	public static double trulayphut(Date date1,Date date2) {
		try {
			 double diff = date1.getTime() - date2.getTime();
			 return  diff / (60  * 1000);
		} catch (Exception e) {
			return 0.0;
		}
	}

	public static String chuyensangStrCode(Date date) {
		try {
			return ddMMyyyyStr.format(date);
		} catch (Exception e) {
			return "";
		}
	}
	public static String chuyensangStr(Date date) {
		try {
			return ddMMyyyy.format(date);
		} catch (Exception e) {
			return "";
		}
	}
	public static String chuyensangStrApi(Date date) {
		try {
			return ddMMyyyyapi.format(date);
		} catch (Exception e) {
			return "";
		}
	}
	public static Date chuyensangStrApiDate(String dateStr) {
		try {
			return ddMMyyyyapi.parse(dateStr);
		} catch (Exception e) {
			return null;
		}
	}
	public static String chuyensangddMMyyStr(Date date) {
		try {
			return ddMMyy.format(date);
		} catch (Exception e) {
			return "";
		}
	}
	public static String chuyensangStrHH(Date date) {
		try {
			return ddMMyyyyhh.format(date);
		} catch (Exception e) {
			return "";
		}
	}
	public static String chuyensangStrHHmm(Date date) {
		try {
			return hhmm.format(date);
		} catch (Exception e) {
			return "";
		}
	}
	public static String chuyensangDay(Date date) {
		try {
			return dd.format(date);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	public static String hashSetToString(Set<String> set, String separator) {
        StringBuilder sb = new StringBuilder();
        Iterator<String> iterator = set.iterator();
        while (iterator.hasNext()) {
            sb.append(iterator.next());
            if (iterator.hasNext()) {
                sb.append(separator);
            }
        }
        return sb.toString();
    }
}
