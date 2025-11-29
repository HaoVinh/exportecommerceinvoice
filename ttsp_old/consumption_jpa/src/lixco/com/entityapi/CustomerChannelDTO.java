package lixco.com.entityapi;

import lixco.com.entity.CustomerChannel;
import lombok.Data;

@Data
public class CustomerChannelDTO{
	private String name;
	public CustomerChannelDTO(CustomerChannel customerChannel) {
		this.name=customerChannel.getName();
	}
	
	

}
