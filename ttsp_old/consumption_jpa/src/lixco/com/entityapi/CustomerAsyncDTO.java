package lixco.com.entityapi;

import java.util.List;

import lombok.Data;

@Data
public class CustomerAsyncDTO {

	List<CityDTO> cityDTOs;
	List<CustomerChannelDTO> customerChannelDTOs;
	List<CustomerTypesDTO> customerTypesDTOs;
	List<CustomerDTO> customerDTOs;

	public CustomerAsyncDTO(List<CityDTO> cityDTOs, List<CustomerChannelDTO> customerChannelDTOs,
			List<CustomerTypesDTO> customerTypesDTOs, List<CustomerDTO> customerDTOs) {
		super();
		this.cityDTOs = cityDTOs;
		this.customerChannelDTOs = customerChannelDTOs;
		this.customerTypesDTOs = customerTypesDTOs;
		this.customerDTOs = customerDTOs;
	}

}
