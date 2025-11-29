package lixco.com.commom_ejb;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MyMath {

	public static double round(double number) {
		if (number < 0) {
			number = Math.round(Math.abs(number)) * -1;
		} else {
			number = Math.round(number);
		}
		return number;
	}

	public static double roundCustom(double number, int sole) {
		BigDecimal bd = new BigDecimal(number + "");
		bd = bd.setScale(sole, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	public static void main(String[] args) {
		double a = 2.175;
		double roundedValue = roundCustom(a, 2);
		System.out.println("Giá trị sau khi làm tròn: " + roundedValue);
	}

	public static BigDecimal roundToTwoDecimalPlaces(String value) {
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(2, RoundingMode.HALF_UP);
		return bd;
	}
}
