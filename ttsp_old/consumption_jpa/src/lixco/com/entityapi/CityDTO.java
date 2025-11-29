package lixco.com.entityapi;

import lixco.com.entity.City;
import lombok.Data;
@Data
public class CityDTO {
	private long id;
	private String city_code;
	private String city_name;
	private String en_name;
	private String countryCode;
	private String areaCode;

	public CityDTO(City city) {
		this.city_code = city.getCity_code();
		this.city_name = city.getCity_name();
		this.en_name = city.getEn_name();
		this.countryCode = city.getCountry()!=null?city.getCountry().getCountry_code():"";
		this.areaCode = city.getArea()!=null?city.getArea().getArea_code():"";
	}
}
