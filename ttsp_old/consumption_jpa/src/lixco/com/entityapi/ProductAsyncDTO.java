package lixco.com.entityapi;

import java.util.List;

import lombok.Data;
@Data
public class ProductAsyncDTO {
	
	List<ProductBrandDTO> productBrandDTOs;
	List<ProductComDTO> productComDTOs;
	List<ProductGroupDTO> productGroupDTOs;
	List<ProductTypeDTO> productTypeDTOs;
	List<ProductDTO> productDTOs;
	List<PromotionProductGroupDTO> promotionProductGroupDTOs;
	public ProductAsyncDTO(List<ProductBrandDTO> productBrandDTOs, List<ProductComDTO> productComDTOs,
			List<ProductGroupDTO> productGroupDTOs, List<ProductTypeDTO> productTypeDTOs, List<ProductDTO> productDTOs,
			List<PromotionProductGroupDTO> promotionProductGroupDTOs) {
		super();
		this.productBrandDTOs = productBrandDTOs;
		this.productComDTOs = productComDTOs;
		this.productGroupDTOs = productGroupDTOs;
		this.productTypeDTOs = productTypeDTOs;
		this.productDTOs = productDTOs;
		this.promotionProductGroupDTOs = promotionProductGroupDTOs;
	}
	
	

}
