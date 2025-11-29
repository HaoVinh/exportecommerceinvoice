/**
 * This class is made by Lam Quan Vu.
 * @Copyright 2013 by Lam Quan Vu. Email : LamQuanVu@gmail.com
 */
package trong.lixco.com.util;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import lixco.com.entity.Carrier;
import lixco.com.entity.City;
import lixco.com.entity.Contract;
import lixco.com.entity.Customer;
import lixco.com.entity.CustomerChannel;
import lixco.com.entity.CustomerTypes;
import lixco.com.entity.HarborCategory;
import lixco.com.entity.IECategories;
import lixco.com.entity.Product;
import lixco.com.entity.ProductBrand;
import lixco.com.entity.ProductCom;
import lixco.com.entity.ProductType;
import lixco.com.entity.Stocker;
import lixco.com.entity.Warehouse;
import lixco.com.interfaces.ICarrierService;
import lixco.com.interfaces.ICityService;
import lixco.com.interfaces.IContractService;
import lixco.com.interfaces.ICustomerChannelService;
import lixco.com.interfaces.ICustomerService;
import lixco.com.interfaces.ICustomerTypesService;
import lixco.com.interfaces.IHarborCategoryService;
import lixco.com.interfaces.IIECategoriesService;
import lixco.com.interfaces.IProductBrandService;
import lixco.com.interfaces.IProductComService;
import lixco.com.interfaces.IProductService;
import lixco.com.interfaces.IProductTypeService;
import lixco.com.interfaces.IStockerService;
import lixco.com.interfaces.IWarehouseService;
import lixco.com.service.CustomerChannelService;

@ManagedBean
public class AbstractAutoComplete {
	@Inject
	private IContractService contractService;
	@Inject
	private ICityService cityService;
	@Inject
	private ICustomerService customerService;
	@Inject
	private ICustomerTypesService customerTypesService;
	@Inject
	private IProductService productService;
	@Inject
	private IProductTypeService productTypeService;
	@Inject
	private IProductComService productComService;
	@Inject
	private IProductBrandService productBrandService;
	@Inject
	private ICarrierService carrierService;

	@PostConstruct
	public void init() {
	}

	@Inject
	private IIECategoriesService ieCategoriesService;
	@Inject
	ICustomerChannelService customerChannelService;

	// kênh khach hang
	public List<CustomerChannel> completeCustomerChannel(String containedStr) {
		try {
			List<CustomerChannel> list = new ArrayList<CustomerChannel>();
			String searchText = converViToEn(containedStr);
			if (searchText.contains("D") || searchText.contains("Đ") || searchText.contains("d")
					|| searchText.contains("đ")) {
				searchText = searchText.replace("D", "_");
				searchText = searchText.replace("Đ", "_");
				searchText = searchText.replace("d", "_");
				searchText = searchText.replace("đ", "_");
			}
			customerChannelService.findLike(searchText, list);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// loai nhap xuat
	public List<IECategories> completeIECategories(String containedStr) {
		try {
			List<IECategories> list = new ArrayList<IECategories>();
			String searchText = converViToEn(containedStr);
			if (searchText.contains("D") || searchText.contains("Đ") || searchText.contains("d")
					|| searchText.contains("đ")) {
				searchText = searchText.replace("D", "_");
				searchText = searchText.replace("Đ", "_");
				searchText = searchText.replace("d", "_");
				searchText = searchText.replace("đ", "_");
			}
			ieCategoriesService.complete(searchText, list);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// loai nhap xuat
	public List<IECategories> completeIECategoriesCode(String containedStr) {
		try {
			List<IECategories> list = new ArrayList<IECategories>();
			String searchText = converViToEn(containedStr);
			if (searchText.contains("D") || searchText.contains("Đ") || searchText.contains("d")
					|| searchText.contains("đ")) {
				searchText = searchText.replace("D", "_");
				searchText = searchText.replace("Đ", "_");
				searchText = searchText.replace("d", "_");
				searchText = searchText.replace("đ", "_");
			}
			ieCategoriesService.completeCode(searchText, list);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// hop dong
	public List<Contract> completeContract(String containedStr) {
		try {
			List<Contract> list = new ArrayList<Contract>();
			String searchText = converViToEn(containedStr);
			if (searchText.contains("D") || searchText.contains("Đ") || searchText.contains("d")
					|| searchText.contains("đ")) {
				searchText = searchText.replace("D", "_");
				searchText = searchText.replace("Đ", "_");
				searchText = searchText.replace("d", "_");
				searchText = searchText.replace("đ", "_");
			}
			FacesContext context = FacesContext.getCurrentInstance();
			Date date = (Date) UIComponent.getCurrentComponent(context).getAttributes().get("param");
			if (date != null) {
				contractService.completeDate(searchText, list, date);
			} else {
				contractService.complete(searchText, list);
			}

			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// kho
	@Inject
	IWarehouseService warehouseService;

	public List<Warehouse> completeWarehouse(String containedStr) {
		try {
			List<Warehouse> list = new ArrayList<Warehouse>();
			String searchText = converViToEn(containedStr);
			if (searchText.contains("D") || searchText.contains("Đ") || searchText.contains("d")
					|| searchText.contains("đ")) {
				searchText = searchText.replace("D", "_");
				searchText = searchText.replace("Đ", "_");
				searchText = searchText.replace("d", "_");
				searchText = searchText.replace("đ", "_");
			}
			warehouseService.complete(searchText, list);

			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Thu kho
	@Inject
	IStockerService stockerService;

	public List<Stocker> completeStocker(String containedStr) {
		try {
			List<Stocker> list = new ArrayList<Stocker>();
			String searchText = converViToEn(containedStr);
			if (searchText.contains("D") || searchText.contains("Đ") || searchText.contains("d")
					|| searchText.contains("đ")) {
				searchText = searchText.replace("D", "_");
				searchText = searchText.replace("Đ", "_");
				searchText = searchText.replace("d", "_");
				searchText = searchText.replace("đ", "_");
			}
			stockerService.complete(searchText, list);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// cang
	@Inject
	IHarborCategoryService harborCategoryService;

	public List<HarborCategory> completeHarborCategoryr(String containedStr) {
		try {
			List<HarborCategory> list = new ArrayList<HarborCategory>();
			String searchText = converViToEn(containedStr);
			if (searchText.contains("D") || searchText.contains("Đ") || searchText.contains("d")
					|| searchText.contains("đ")) {
				searchText = searchText.replace("D", "_");
				searchText = searchText.replace("Đ", "_");
				searchText = searchText.replace("d", "_");
				searchText = searchText.replace("đ", "_");
			}
			harborCategoryService.complete(searchText, list);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// thanh pho
	public List<City> completeCity(String containedStr) {
		try {
			List<City> list = new ArrayList<City>();
			String searchText = converViToEn(containedStr);
			if (searchText.contains("D") || searchText.contains("Đ") || searchText.contains("d")
					|| searchText.contains("đ")) {
				searchText = searchText.replace("D", "_");
				searchText = searchText.replace("Đ", "_");
				searchText = searchText.replace("d", "_");
				searchText = searchText.replace("đ", "_");
			}
			cityService.complete(searchText, list);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// khach hang
	public List<Customer> completeCustomer(String containedStr) {
		try {
			List<Customer> list = new ArrayList<Customer>();
			String searchText = converViToEn(containedStr);
			if (searchText.contains("D") || searchText.contains("Đ") || searchText.contains("d")
					|| searchText.contains("đ")) {
				searchText = searchText.replace("D", "_");
				searchText = searchText.replace("Đ", "_");
				searchText = searchText.replace("d", "_");
				searchText = searchText.replace("đ", "_");
			}
			customerService.complete(searchText, list);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// loai khach hang
	public List<CustomerTypes> completeCustomerTypes(String containedStr) {
		try {
			List<CustomerTypes> list = new ArrayList<CustomerTypes>();
			String searchText = converViToEn(containedStr);
			if (searchText.contains("D") || searchText.contains("Đ") || searchText.contains("d")
					|| searchText.contains("đ")) {
				searchText = searchText.replace("D", "_");
				searchText = searchText.replace("Đ", "_");
				searchText = searchText.replace("d", "_");
				searchText = searchText.replace("đ", "_");
			}
			customerTypesService.complete(searchText, list);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// san pham
	public List<Product> completeProduct(String containedStr) {
		try {
			List<Product> list = new ArrayList<Product>();
			String searchText = converViToEn(containedStr);
			if (searchText.contains("D") || searchText.contains("Đ") || searchText.contains("d")
					|| searchText.contains("đ")) {
				searchText = searchText.replace("D", "_");
				searchText = searchText.replace("Đ", "_");
				searchText = searchText.replace("d", "_");
				searchText = searchText.replace("đ", "_");
			}
			productService.complete(searchText, list);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Loai san pham
	public List<ProductType> completeProductTypes(String containedStr) {
		try {
			List<ProductType> list = new ArrayList<ProductType>();
			String searchText = converViToEn(containedStr);
			if (searchText.contains("D") || searchText.contains("Đ") || searchText.contains("d")
					|| searchText.contains("đ")) {
				searchText = searchText.replace("D", "_");
				searchText = searchText.replace("Đ", "_");
				searchText = searchText.replace("d", "_");
				searchText = searchText.replace("đ", "_");
			}
			productTypeService.complete(searchText, list);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// san pham com
	public List<ProductCom> completeProductComs(String containedStr) {
		try {
			List<ProductCom> list = new ArrayList<ProductCom>();
			String searchText = converViToEn(containedStr);
			if (searchText.contains("D") || searchText.contains("Đ") || searchText.contains("d")
					|| searchText.contains("đ")) {
				searchText = searchText.replace("D", "_");
				searchText = searchText.replace("Đ", "_");
				searchText = searchText.replace("d", "_");
				searchText = searchText.replace("đ", "_");
			}
			productComService.complete(searchText, list);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// nhom san pham brand
	public List<ProductBrand> completeProductBrand(String containedStr) {
		try {
			List<ProductBrand> list = new ArrayList<ProductBrand>();
			String searchText = converViToEn(containedStr);
			if (searchText.contains("D") || searchText.contains("Đ") || searchText.contains("d")
					|| searchText.contains("đ")) {
				searchText = searchText.replace("D", "_");
				searchText = searchText.replace("Đ", "_");
				searchText = searchText.replace("d", "_");
				searchText = searchText.replace("đ", "_");
			}
			productBrandService.complete(searchText, list);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Nguoi van chuyen
	public List<Carrier> completeCarrier(String containedStr) {
		try {
			List<Carrier> list = new ArrayList<Carrier>();
			String searchText = converViToEn(containedStr);
			if (searchText.contains("D") || searchText.contains("Đ") || searchText.contains("d")
					|| searchText.contains("đ")) {
				searchText = searchText.replace("D", "_");
				searchText = searchText.replace("Đ", "_");
				searchText = searchText.replace("d", "_");
				searchText = searchText.replace("đ", "_");
			}
			carrierService.complete(searchText, list);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String converViToEn(String s) {
		String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
		Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
		String result = pattern.matcher(temp).replaceAll("");
		return result;
	}
}
