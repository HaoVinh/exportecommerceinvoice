package lixco.com.bean;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import lixco.com.commom_ejb.MyUtilEJB;
import lixco.com.common.ApiCallClient;
import lixco.com.common.FormatHandler;
import lixco.com.common.JsonParserUtil;
import lixco.com.common.SessionHelper;
import lixco.com.entity.DongBo;
import lixco.com.entity.Product;
import lixco.com.entity.ProductBrand;
import lixco.com.entity.ProductCom;
import lixco.com.entity.ProductGroup;
import lixco.com.entity.ProductKM;
import lixco.com.entity.ProductType;
import lixco.com.entity.PromotionProductGroup;
import lixco.com.entityapi.DataAPI;
import lixco.com.entityapi.ProductAsyncDTO;
import lixco.com.entityapi.ProductBrandDTO;
import lixco.com.entityapi.ProductComDTO;
import lixco.com.entityapi.ProductDTO;
import lixco.com.entityapi.ProductGroupDTO;
import lixco.com.entityapi.ProductKMDTO;
import lixco.com.entityapi.ProductTypeDTO;
import lixco.com.entityapi.PromotionProductGroupDTO;
import lixco.com.interfaces.IProductBrandService;
import lixco.com.interfaces.IProductComService;
import lixco.com.interfaces.IProductGroupService;
import lixco.com.interfaces.IProductService;
import lixco.com.interfaces.IProductTypeService;
import lixco.com.interfaces.IPromotionProductGroupService;
import lixco.com.reqInfo.ProductComReqInfo;
import lixco.com.reqInfo.ProductDataReqInfo;
import lixco.com.reqInfo.ProductGroupReqInfo;
import lixco.com.reqInfo.ProductReqInfo;
import lixco.com.reqInfo.ProductTypeReqInfo;
import lixco.com.reqInfo.PromotionProductGroupReqInfo;
import lixco.com.service.ProductBrandService;
import lixco.com.service.ProductGroupService;
import lixco.com.service.ProductKMService;
import lixco.com.service.ProductTypeService;
import lombok.Getter;
import lombok.Setter;
import okhttp3.Call;
import okhttp3.Response;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jboss.logging.Logger;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;

import trong.lixco.com.account.servicepublics.Account;
import trong.lixco.com.api.SecurityConfig;
import trong.lixco.com.bean.AbstractBean;
import trong.lixco.com.entity.AccountAPI;
import trong.lixco.com.entity.AccountDatabase;
import trong.lixco.com.service.AccountAPIService;
import trong.lixco.com.service.AccountDatabaseService;
import trong.lixco.com.service.DongBoService;
import trong.lixco.com.util.MyStringUtil;
import trong.lixco.com.util.MyUtilExcel;
import trong.lixco.com.util.Notify;
import trong.lixco.com.util.StaticPath;
import trong.lixco.com.util.ToolReport;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

@Named
@ViewScoped
public class ProductBean extends AbstractBean {
	private static final long serialVersionUID = -7927381135900704460L;
	@Inject
	private Logger logger;
	@Inject
	private IProductService productService;
	@Inject
	private IProductComService productComService;
	@Inject
	private IProductTypeService productTypeService;
	@Inject
	private IProductGroupService productGroupService;
	@Inject
	private IPromotionProductGroupService promotionProductGroupService;
	@Inject
	private AccountDatabaseService accountDatabaseService;
	@Inject
	ProductKMService productKMService;
	private Product productCrud;
	private Product productSelect;
	private List<Product> listProduct;
	@Getter
	@Setter
	private List<Product> listProductFilters;
	private List<ProductType> listProductType;
	private List<ProductGroup> listProductGroup;
	@Getter
	private List<ProductKM> productKMs;
	@Getter
	@Setter
	private ProductCom productCom;
	@Getter
	@Setter
	private ProductKM productKM;
	@Getter
	@Setter
	private ProductType productType;
	private Account account;

	private final String NAMESYNC = "sanpham";
	@Getter
	DongBo dongBo;

	@Override
	protected void initItem() {
		try {
			dongBo = dongBoService.findName(NAMESYNC);
			if (dongBo == null) {
				dongBo = new DongBo();
				dongBo.setName(NAMESYNC);
			}
			search();
			FacesContext facesContext = FacesContext.getCurrentInstance();
			HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
			account = SessionHelper.getInstance().getSession("account", request, Account.class);
			productCrud = new Product();
			productKM = new ProductKM();
			listProductType = new ArrayList<>();
			productTypeService.selectAllOrderByName(listProductType);
			listProductGroup = new ArrayList<>();
			productGroupService.selectAllOrderByName(listProductGroup);
			productKMs = new ArrayList<>();
		} catch (Exception e) {
			logger.error("ProductBean.initItem:" + e.getMessage(), e);
		}
	}

	@Override
	protected Logger getLogger() {
		return logger;
	}

	@Getter
	@Setter
	private String stextStr;

	public void search() {
		try {
			listProduct = new ArrayList<Product>();
			JsonObject jsonInfo = new JsonObject();
			jsonInfo.addProperty("stextStr", MyStringUtil.replaceD(stextStr));
			jsonInfo.addProperty("product_com_id", productCom != null ? productCom.getId() : 0);
			jsonInfo.addProperty("product_type_id", productType != null ? productType.getId() : 0);
			JsonObject json = new JsonObject();
			json.add("product_info", jsonInfo);
			productService.search(JsonParserUtil.getGson().toJson(json), listProduct);
		} catch (Exception e) {
			logger.error("ProductBean.search:" + e.getMessage(), e);
		}
	}

	public void capnhatSPKM() {
		try {
			int dadongbo = 0;
			for (int i = 0; i < listProduct.size(); i++) {
				List<ProductKM> productKMs = new ArrayList<ProductKM>();
				if (listProduct.get(i).getMaspchinh() != null) {
					Product spchinh = productService.selectByCode(listProduct.get(i).getMaspchinh());
					if (spchinh != null) {
						ProductKM pKM = new ProductKM();
						pKM.setCreated_by("Cập nhật admin");
						pKM.setCreated_date(new Date());
						pKM.setPromotion_product(spchinh);
						pKM.setQuantity(1);
						pKM.setSpchinh(true);
						productKMs.add(pKM);
					}
				}
				if (listProduct.get(i).getPromotion_product() != null) {
					ProductKM pKM = new ProductKM();
					pKM.setCreated_by("Cập nhật admin");
					pKM.setCreated_date(new Date());
					pKM.setPromotion_product(listProduct.get(i).getPromotion_product());
					pKM.setQuantity(1);
					productKMs.add(pKM);
				}
				if (productKMs.size() != 0) {
					listProduct.get(i).setProductKMs(productKMs);
					productService.update(listProduct.get(i));
					dadongbo++;
				}
			}
			notice("Đã đồng bộ " + dadongbo + "/" + listProduct.size());
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Inject
	AccountAPIService accountAPIService;
	@Inject
	IProductBrandService productBrandService;
	@Inject
	DongBoService dongBoService;

	public void dongbo() {
		try {
			Gson gson = JsonParserUtil.getGson();
			String[] chinhanhs = { "BD", "BN" };
			ProductAsyncDTO productAsyncDTO = dongbodata();
			StringBuilder message = new StringBuilder();
			StringBuilder notice = new StringBuilder();
			StringBuilder errorCN = new StringBuilder();
			for (int i = 0; i < chinhanhs.length; i++) {
				String token = "";
				AccountAPI accountAPI = SecurityConfig.getTokenTime(accountAPIService, chinhanhs[i]);
				if (accountAPI == null) {
					noticeError("Không có tài khoản đăng nhập API.");
					return;
				}
				String path = StaticPath.getPathBD();
				if ("BD".equals(chinhanhs[i])) {
					path = StaticPath.getPathBD();
				} else if ("BN".equals(chinhanhs[i])) {
					path = StaticPath.getPathBN();
				}

				String[] tokentime = new String[] { accountAPI.getToken(), accountAPI.getTimetoken() + "" };
				if (tokentime != null
						&& (tokentime[1] != null && new Date().getTime() < Long.parseLong(tokentime[1]) && tokentime[0] != null)) {
					token = tokentime[0];
				} else {
					dangnhapAPIdongbo(gson, path, chinhanhs[i]);
				}
				String datajson = gson.toJson(productAsyncDTO);
				DataAPI dataAPI = new DataAPI(datajson);

				// Goi ham dong bo BD
				Call call = trong.lixco.com.api.CallAPI.getInstance(token).getMethodPost(
						path + "/api/data/dongbo/sanpham", gson.toJson(dataAPI));
				Response response = call.execute();

				String data = response.body().string();
				JsonObject jdata = gson.fromJson(data, JsonObject.class);
				if (jdata != null) {
					String msg = jdata.get("msg").getAsString();
					message.append(msg);
					if (response.isSuccessful()) {
						notice.append(chinhanhs[i]);
						if (i == 0)
							notice.append(", ");
						info("Đồng bộ " + chinhanhs[i] + " thành công.");
					} else {
						if (response.code() == 401) {
							dangnhapAPIdongbo(gson, path, chinhanhs[i]);
						} else {
							errorCN.append(msg + chinhanhs[i]);
							error(msg + chinhanhs[i]);
						}
					}
				} else {
					warning("Không có kết nối đến chi nhánh " + chinhanhs[i]);
					errorCN.append("Không có kết nối đến chi nhánh " + chinhanhs[i]);
				}
			}
			if (!"".equals(notice.toString())) {
				if ("".equals(errorCN.toString())) {
					List<Long> ids = new ArrayList<Long>();
					for (int i = 0; i < productAsyncDTO.getProductDTOs().size(); i++) {
						ids.add(productAsyncDTO.getProductDTOs().get(i).getId());
					}
					productService.updateCapNhat(ids);
				}
			} else {
				noticeError("Kiểm tra lại dữ liệu chi nhánh.");
			}
			dongBo.setTime("Lần cuối: " + MyUtilEJB.chuyensangStrHH(new Date()));
			dongBo.setUserSync(getAccount().getUserName());
			dongBo.setMessages(message.toString());
			dongBoService.saveOrUpdate(dongBo);
		} catch (Exception e) {
			e.printStackTrace();
			error(e.getLocalizedMessage());
		}

	}

	private void dangnhapAPIdongbo(Gson gson, String path, String chinhanh) throws IOException {
		AccountAPI accountAPI = SecurityConfig.getTokenTime(accountAPIService, chinhanh);
		String[] tokentime = new String[2];
		Call call = trong.lixco.com.api.CallAPI.getInstance("").getMethodPost(path + "/api/account/login",
				gson.toJson(accountAPI));
		Response response = call.execute();
		if (response.isSuccessful()) {
			String data = response.body().string();
			JsonObject jsonObject = gson.fromJson(data, JsonObject.class);
			tokentime[0] = jsonObject.get("access_token").getAsString();
			tokentime[1] = jsonObject.get("expires_in").getAsString();
			SecurityConfig.saveToken(accountAPIService, tokentime[0], Long.parseLong(tokentime[1]), chinhanh);
			notice("Đã liên kết đến hệ thống " + response.toString() + ". Vui lòng thực hiện chuyển lại");
		} else {
			noticeError("Tài khoản đăng nhập API " + chinhanh + " không đúng hoặc lỗi " + response.toString());
			return;
		}
	}

	private ProductAsyncDTO dongbodata() {
		// san pham brand
		List<ProductBrand> productBrands = new ArrayList<ProductBrand>();
		productBrandService.selectAll(productBrands);
		List<ProductBrandDTO> productBrandDTOs = new ArrayList<ProductBrandDTO>();
		for (int i = 0; i < productBrands.size(); i++) {
			ProductBrandDTO pdbDTO = new ProductBrandDTO(productBrands.get(i));
			productBrandDTOs.add(pdbDTO);
		}
		// san pham com
		List<ProductCom> productComs = new ArrayList<ProductCom>();
		productComService.selectAll(productComs);
		List<ProductComDTO> productComDTOs = new ArrayList<ProductComDTO>();
		for (int i = 0; i < productComs.size(); i++) {
			ProductComDTO pdComDTO = new ProductComDTO(productComs.get(i));
			productComDTOs.add(pdComDTO);
		}

		// Nhom san pham
		List<ProductGroup> productGroups = new ArrayList<ProductGroup>();
		productGroupService.selectAll(productGroups);
		List<ProductGroupDTO> productGroupDTOs = new ArrayList<ProductGroupDTO>();
		for (int i = 0; i < productGroups.size(); i++) {
			ProductGroupDTO pdgDTO = new ProductGroupDTO(productGroups.get(i));
			productGroupDTOs.add(pdgDTO);
		}
		// Loai san pham
		List<ProductType> productTypes = new ArrayList<ProductType>();
		productTypeService.selectAll(productTypes);
		List<ProductTypeDTO> productTypeDTOs = new ArrayList<ProductTypeDTO>();
		for (int i = 0; i < productTypes.size(); i++) {
			ProductTypeDTO pdTDTO = new ProductTypeDTO(productTypes.get(i));
			productTypeDTOs.add(pdTDTO);
		}
		// nhom san pham km
		List<PromotionProductGroup> promotionProductGroups = new ArrayList<PromotionProductGroup>();
		promotionProductGroupService.selectAll(promotionProductGroups);
		List<PromotionProductGroupDTO> promotionProductGroupDTOs = new ArrayList<PromotionProductGroupDTO>();
		for (int i = 0; i < promotionProductGroups.size(); i++) {
			PromotionProductGroupDTO pdTDTO = new PromotionProductGroupDTO(promotionProductGroups.get(i));
			promotionProductGroupDTOs.add(pdTDTO);
		}

		// San pham
		List<Product> products = productService.findNotSync();
		List<ProductDTO> productDTOs = new ArrayList<ProductDTO>();
		for (int i = 0; i < products.size(); i++) {
			ProductDTO pdDTO = new ProductDTO(products.get(i));
			List<ProductKMDTO> productKMDTOs = new ArrayList<ProductKMDTO>();
			List<ProductKM> productKMs = productKMService.findByIdProductMain(products.get(i).getId());
			for (int j = 0; j < productKMs.size(); j++) {
				productKMDTOs.add(new ProductKMDTO(productKMs.get(j)));
			}
			pdDTO.setProductKMDTOs(productKMDTOs);
			productDTOs.add(pdDTO);
		}
		ProductAsyncDTO productAsyncDTO = new ProductAsyncDTO(productBrandDTOs, productComDTOs, productGroupDTOs,
				productTypeDTOs, productDTOs, promotionProductGroupDTOs);
		return productAsyncDTO;
	}

	public void exportExcel() {
		try {
			if (listProduct.size() > 0) {
				List<Object[]> results = new ArrayList<Object[]>();
				Object[] title = { "id", "ngaytao", "ngaycn", "masp", "tensp", "tensp(tienganh)", "tenhaiquan",
						"thongtinsp", "donvitinh", "malever", "quicachdonggoi", "slthung/pallet", "hesochuyendoi",
						"trongluongbbthucte", "donvidonggoi", "xuatkhau", "makhac", "sldutru", "masp_com", "tensp_com",
						"maspkhuyenmai", "tenspkm", "khongsd", "maloaisp", "tenloaisp", "manhomsp", "tennhomsp",
						"manhomspkm", "tennhomspkm", "maspchinh", "maspcu", "maspbrand", "tenspbrand", "xuatkhau" };
				results.add(title);
				for (Product p : listProduct) {
					ProductCom pcom = p.getProduct_com();
					ProductBrand pbranf = (pcom == null ? null : pcom.getProduct_brand());
					Product ppr = p.getPromotion_product();
					ProductType pt = p.getProduct_type();
					ProductGroup pg = p.getProduct_group();
					PromotionProductGroup prg = p.getPromotion_product_group();
					Object row[] = { p.getId(), p.getCreated_date(), p.getLast_modifed_date(), p.getProduct_code(),
							p.getProduct_name(), p.getEn_name(), p.getCustoms_name(), p.getProduct_info(), p.getUnit(),
							p.getLever_code(), p.getSpecification(), p.getBox_quantity(), p.getFactor(), p.getTare(),
							p.getPacking_unit(), p.isTypep(), p.getOther_code(), p.getReserve_quantity(),
							pcom == null ? "" : pcom.getPcom_code(), pcom == null ? "" : pcom.getPcom_name(),
							ppr == null ? "" : ppr.getProduct_code(), ppr == null ? "" : ppr.getProduct_name(),
							p.isDisable(), pt == null ? "" : pt.getCode(), pt == null ? "" : pt.getName(),
							pg == null ? "" : pg.getCode(), pg == null ? "" : pg.getName(),
							prg == null ? "" : prg.getCode(), prg == null ? "" : prg.getName(), p.getMaspchinh(),
							p.getMaspcu(), pbranf == null ? "" : pbranf.getPbrand_code(),
							pbranf == null ? "" : pbranf.getPbrand_name(), p.isTypep() };
					results.add(row);
				}
				String titleEx = "dmsp";
				ToolReport.printReportExcelRaw(results, titleEx);
			}
		} catch (Exception e) {
			logger.error("ProductBean.exportExcel:" + e.getMessage(), e);
		}
	}

	private void callApiProductFoxPro(long productId) {
		PrimeFaces current = PrimeFaces.current();
		try {
			lixco.com.reqfox.Product product = productService.getProductFoxPro(productId);
			AccountDatabase accountDatabase = accountDatabaseService.findByName("foxproapi");
			if (product != null) {
				try {
					Call call = ApiCallClient.getListObjectWithParam(accountDatabase.getAddressPublic(), "product",
							"sa", JsonParserUtil.getGson().toJson(product));
					Response response = call.execute();
					String body = response.body().string();
					JsonObject result = JsonParserUtil.getGson().fromJson(body, JsonObject.class);
					if (response.isSuccessful() && result.get("err").getAsInt() == 0) {
						lixco.com.reqfox.Product productResult = JsonParserUtil.getGson().fromJson(result.get("dt"),
								lixco.com.reqfox.Product.class);

					} else {
						String mesages = result.get("msg").getAsString();
						current.executeScript("swaldesignclose2('Cảnh báo!', '" + mesages + "','warning');");
					}
				} catch (Exception e) {
					e.printStackTrace();
					current.executeScript("swaldesignclose2('Cảnh báo!', 'Không thực hiện liên kết dữ liệu foxpro','warning');");
				}
			}
		} catch (Exception e) {
			logger.error("ProductBean.callApiProductFoxPro:foxpro");
		}
	}

	private void callApiDelivery(long id) {
		PrimeFaces current = PrimeFaces.current();
		try {
			AccountDatabase accountDatabase = accountDatabaseService.findByName("giaonhan");
			Product product = productService.selectForDelivery(id);
			if (product != null) {
				ProductDataReqInfo t = new ProductDataReqInfo();
				ProductDataReqInfo.Product p = new ProductDataReqInfo.Product(product.getProduct_code(),
						product.getProduct_name(), product.getLever_code(), product.getBox_quantity(),
						product.getSpecification(), 0, product.getUnit(), product.getFactor(), product.isTypep() ? "XK"
								: "ND", product.isDisable());
				t.setProduct(p);
				ProductCom productCom = product.getProduct_com();
				if (productCom != null) {
					ProductDataReqInfo.ProductCom pc = new ProductDataReqInfo.ProductCom(productCom.getPcom_code(),
							productCom.getPcom_name(), productCom.getUnit());
					t.setProduct_com(pc);
					ProductBrand productBrand = productCom.getProduct_brand();
					if (productBrand != null) {
						ProductDataReqInfo.ProductBrand pb = new ProductDataReqInfo.ProductBrand(
								productBrand.getPbrand_code(), productBrand.getPbrand_name());
						t.setProduct_brand(pb);
					}
				}
				// {authorization:'',content_type:''}
				JsonObject headerJson = new JsonObject();
				headerJson.addProperty("authorization", "");
				headerJson.addProperty("content_type", "application/x-www-form-urlencoded; charset=utf-8");
				Call call = ApiCallClient.apiCommand(accountDatabase.getAddressPublic(), "productlix", "save",
						JsonParserUtil.getGson().toJson(t), JsonParserUtil.getGson().toJson(headerJson));
				Response response = call.execute();
				if (response.isSuccessful()) {
					String body = response.body().string();
					JsonObject jbody = JsonParserUtil.getGson().fromJson(body, JsonObject.class);
					if (jbody.get("err").getAsInt() != 0) {
						current.executeScript("swaldesignclose2('Xảy ra lỗi','Không cập nhật được sản phẩm chương trình giao nhận"
								+ jbody + "','warning')");
					}
				} else {
					current.executeScript("swaldesignclose2('Xảy ra lỗi','Không cập nhật được sản phẩm chương trình giao nhận','warning')");
				}

			}
		} catch (Exception e) {
			logger.error("ProductBean.callApiDelivery:" + e.getMessage(), e);
		}
	}

	public void saveOrUpdate() {
		PrimeFaces current = PrimeFaces.current();
		try {
			if (productCrud != null) {
				String productCode = productCrud.getProduct_code();
				String productName = productCrud.getProduct_name();
				ProductCom productCom = productCrud.getProduct_com();
				if (productCode != null && productCode != "" && productName != null && productName != ""
						&& productCom != null) {
					ProductReqInfo t = new ProductReqInfo(productCrud);
					if (productCrud.getId() == 0) {
						// check code đã tồn tại chưa
						if (allowSave(new Date())) {
							if (productService.checkProductCode(productCode, 0) == 0) {
								if (productService.checkProductName(productName, 0) == 0) {
									productCrud.setCreated_date(new Date());
									productCrud.setCreated_by(account.getMember().getName());
									productCrud.setProductKMs(productKMs);
									if (productService.insert(t) != -1) {
										success();
										listProduct.add(0, productCrud);
										if (listProductFilters != null) {
											listProductFilters.add(0, productCrud);
										}

										// call api delivery
										callApiDelivery(productCrud.getId());
										// call api foxpro
										// callApiProductFoxPro(productCrud.getId());
										createNew();
										// executeScript("PF('tablesp').clearFilters();");
									} else {
										current.executeScript("swaldesigntimer('Xảy ra lỗi!', 'Lỗi hệ thống!','error',2000);");
									}
								} else {
									noticeError("Tên sản phẩm đã có.");
								}
							} else {
								noticeError("Mã sản phẩm đã có.");
							}
						} else {
							current.executeScript("swaldesigntimer('Cảnh báo!', 'Tài khoản này không có quyền thực hiện hoặc tháng đã khoá!','error',2000);");
						}
					} else {
						// check code update đã tồn tại chưa
						if (allowUpdate(new Date())) {
							if (productService.checkProductCode(productCode, productCrud.getId()) == 0) {
								productCrud.setLast_modifed_by(account.getMember().getName());
								productCrud.setLast_modifed_date(new Date());
								productCrud.setProductKMs(productKMs);
								if (productService.update(t) != -1) {
									success();
									listProduct.set(listProduct.indexOf(productCrud), productCrud);
									if (listProductFilters != null) {
										listProductFilters.set(listProductFilters.indexOf(productCrud), productCrud);
									}
									callApiDelivery(productCrud.getId());
									// executeScript("PF('tablesp').clearFilters();");
									// call api foxpro
									// callApiProductFoxPro(productCrud.getId());
								} else {
									current.executeScript("swaldesigntimer('Xảy ra lỗi!', 'Lỗi hệ thống!','error',2000);");
								}
							} else {
								current.executeScript("swaldesigntimer('Cảnh báo', 'Mã sản phẩm đã tồn tại!','warning',2000);");
							}
						} else {
							current.executeScript("swaldesigntimer('Cảnh báo!', 'Tài khoản này không có quyền thực hiện hoặc tháng đã khoá!','error',2000);");
						}
					}
				} else {
					current.executeScript("swaldesigntimer('Cảnh báo', 'Thông tin không đầy đủ, điền đủ thông tin chứa(*)!','warning',2000);");
				}
			}
		} catch (Exception e) {
			logger.error("ProductBean.saveOrUpdate:" + e.getMessage(), e);
		}
	}

	public void addProductKM() {
		try {
			if (productKMs == null)
				productKMs = new ArrayList<ProductKM>();
			productKMs.add(productKM);
			productKM = new ProductKM();
		} catch (Exception e) {
			logger.error("ProductBean.saveOrUpdate:" + e.getMessage(), e);
		}
	}

	public void deleteProductKM(int index) {
		try {
			ProductKM productKM = productKMs.get(index);
			if (productKM.getId() != 0) {
				productKMService.deleteById(productKM.getId());
			}
			productKMs.remove(index);
		} catch (Exception e) {
			logger.error("ProductBean.saveOrUpdate:" + e.getMessage(), e);
		}
	}

	public void copy() {
		PrimeFaces current = PrimeFaces.current();
		try {
			if (productCrud != null) {
				productCrud = productCrud.clone();
				productCrud.setId(0);
				productCrud.setLast_modifed_by(null);
				productCrud.setLast_modifed_date(null);
				productCrud.setProduct_code(productCrud.getProduct_code() + "_COPY");
				productCrud.setMaspchinh("");
				productCrud.setPromotion_product(null);
				if (productKMs != null) {
					for (int i = 0; i < productKMs.size(); i++) {
						productKMs.get(i).setId(0);
					}
				} else {
					productKMs = new ArrayList<ProductKM>();
				}
			} else {
				current.executeScript("swaldesigntimer('Cảnh báo', 'Không có dữ liệu copy','warning',2000);");
			}

		} catch (Exception e) {
			logger.error("ProductBean.saveOrUpdate:" + e.getMessage(), e);
		}
	}

	public void showDialogEdit() {
		try {
			if (productSelect != null) {
				productCrud = productService.findById(productSelect.getId());
				productKMs = productKMService.findByIdProductMain(productCrud.getId());
			} else {
				warning("Chọn dòng để chỉnh sửa!");
			}
		} catch (Exception e) {
			logger.error("ProductBean.showDialogEdit:" + e.getMessage(), e);
		}
	}

	public void showDialog() {
		PrimeFaces current = PrimeFaces.current();
		try {
			productCrud = new Product();
			productKMs = new ArrayList<ProductKM>();
			current.executeScript("PF('dlg1').show();");
		} catch (Exception e) {
			logger.error("ProductBean.showDialog:" + e.getMessage(), e);
		}
	}

	public void createNew() {
		productCrud = new Product();
		if (productKMs != null) {
			for (int i = 0; i < productKMs.size(); i++) {
				productKMs.get(i).setId(0);
			}
		} else {
			productKMs = new ArrayList<ProductKM>();
		}
	}

	public void delete() {
		PrimeFaces current = PrimeFaces.current();
		try {
			if (productSelect != null) {
				if (allowDelete(new Date())) {
					StringBuilder error = new StringBuilder();
					if (productService.deleteById(productSelect.getId(), error) != -1) {
						success();
						listProduct.remove(productSelect);
						if (listProductFilters != null) {
							listProductFilters.remove(productSelect);
						}
					} else {
						noticeError(error.toString());
					}
				} else {
					current.executeScript("swaldesigntimer('Cảnh báo!', 'Tài khoản này không có quyền thực hiện hoặc tháng đã khoá!','error',2000);");
				}
			} else {
				current.executeScript("swaldesigntimer('Cảnh báo!', 'Chưa chọn dòng để xóa!','warning',2000);");
			}
		} catch (Exception e) {
			logger.error("ProductBean.delete:" + e.getMessage(), e);
		}
	}

	public List<ProductCom> autoCompleteProductCom(String text) {
		try {
			List<ProductCom> list = new ArrayList<ProductCom>();
			productComService.findLike(FormatHandler.getInstance().converViToEn(text), 120, list);
			for (ProductCom p : list) {
				p.setProduct_brand(null);
				p.setProduct_norm(null);
			}
			return list;
		} catch (Exception e) {
			logger.error("ProductBean.autoCompleteProductCom:" + e.getMessage(), e);
		}
		return null;
	}

	public List<Product> completeProduct(String text) {
		try {
			List<Product> list = new ArrayList<Product>();
			productService.findLike(FormatHandler.getInstance().converViToEn(text), 120, list);
			return list;
		} catch (Exception e) {
			logger.error("ProductBean.completeProduct:" + e.getMessage(), e);
		}
		return null;
	}

	public List<PromotionProductGroup> completePromotionProductGroup(String text) {
		try {
			Object size = UIComponent.getCurrentComponent(FacesContext.getCurrentInstance()).getAttributes()
					.get("sizep");
			List<PromotionProductGroup> list = new ArrayList<PromotionProductGroup>();
			promotionProductGroupService.complete(FormatHandler.getInstance().converViToEn(text),
					Integer.parseInt(Objects.toString(size, "0")), list);
			return list;
		} catch (Exception e) {
			logger.error("ProductBean.completePromotionProductGroup:" + e.getMessage(), e);
		}
		return null;
	}

	public void showDialogUpload() {
		try {
			PrimeFaces current = PrimeFaces.current();
			current.executeScript("PF('uploadpdffile').show();");
		} catch (Exception e) {
			logger.error("ProductBean.showDialogUpload:" + e.getMessage(), e);
		}
	}

	public void loadExcel(FileUploadEvent event) {
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		try {
			if (event.getFile() != null) {
				UploadedFile part = event.getFile();
				byte[] byteFile = event.getFile().getContent();
				List<Product> listProductTemp = new ArrayList<Product>();
				Workbook workBook = getWorkbook(new ByteArrayInputStream(byteFile), part.getFileName());
				Sheet firstSheet = workBook.getSheetAt(0);
				Iterator<Row> rows = firstSheet.iterator();
				while (rows.hasNext()) {
					rows.next();
					rows.remove();
					break;
				}
				while (rows.hasNext()) {
					Row row = rows.next();
					Iterator<Cell> cells = row.cellIterator();
					Product lix = new Product();
					lv2: while (cells.hasNext()) {
						Cell cell = cells.next();
						int columnIndex = cell.getColumnIndex();

						switch (columnIndex) {
						case 0:
							try {
								// tên sản phẩm
								String masp = Objects.toString(MyUtilExcel.getCellValue(cell), null);
								try {
									double maspIsNumber = Double.parseDouble(masp);
									masp = ((int) maspIsNumber) + "";
								} catch (Exception e) {
								}
								if (masp != null && !"".equals(masp)) {
									lix.setProduct_code(masp);
								} else {
									break lv2;
								}
							} catch (Exception e) {
							}
							break;
						case 1:
							try {
								// tên sản phẩm tiếng việt
								String name = Objects.toString(MyUtilExcel.getCellValue(cell), null);
								lix.setProduct_name(name);
							} catch (Exception e) {
							}
							break;
						case 2:
							try {
								// tên sản phẩm tiếng anh
								String name_en = Objects.toString(MyUtilExcel.getCellValue(cell), null);
								lix.setEn_name(name_en);
							} catch (Exception e) {
							}
							break;
						case 3:
							try {
								// tên khai báo hải quan
								String customs_name = Objects.toString(MyUtilExcel.getCellValue(cell), null);
								lix.setCustoms_name(customs_name);
							} catch (Exception e) {
							}
							break;
						case 4:
							try {
								String manhomsp = Objects.toString(MyUtilExcel.getCellValue(cell), null);
								if (manhomsp != null) {
									ProductGroupReqInfo pg = new ProductGroupReqInfo();
									productGroupService.selectByCode(manhomsp, pg);
									lix.setProduct_group(pg.getProduct_Group());
								}
							} catch (Exception e) {
							}
							break;
						case 5:

							try {
								String manloaisp = Objects.toString(MyUtilExcel.getCellValue(cell), null);
								if (manloaisp != null && !"".equals(manloaisp)) {
									ProductTypeReqInfo pt = new ProductTypeReqInfo();
									productTypeService.selectByCode(manloaisp, pt);
									lix.setProduct_type(pt.getProduct_type());
								}
							} catch (Exception e) {
							}
							break;
						case 6:
							try {
								// đơn vị tính
								String dvt = Objects.toString(MyUtilExcel.getCellValue(cell), null);
								lix.setUnit(dvt);
							} catch (Exception e) {
							}
							break;
						case 7:
							try {
								// hệ số quy đổi
								String hsqd = Objects.toString(MyUtilExcel.getCellValue(cell), "0");
								lix.setFactor(Double.parseDouble(hsqd));
							} catch (Exception e) {
							}
							break;
						case 9:
							try {
								// sản phẩm com
								String mspcom = Objects.toString(MyUtilExcel.getCellValue(cell), null);
								if (mspcom != null && !"".equals(mspcom)) {
									ProductComReqInfo com = new ProductComReqInfo();
									productComService.selectByCode(mspcom, com);
									lix.setProduct_com(com.getProduct_com());
								}
							} catch (Exception e) {
							}
							break;
						case 10:
							try {
								// qui cách đóng gói
								String slsp = Objects.toString(MyUtilExcel.getCellValue(cell), "0");
								lix.setSpecification(Double.parseDouble(slsp));
							} catch (Exception e) {
							}
							break;
						case 11:
							try {
								// đơn vị bao bì
								String dvtbb = Objects.toString(MyUtilExcel.getCellValue(cell), null);
								lix.setPacking_unit(dvtbb);
							} catch (Exception e) {
							}
							break;
						case 12:
							try {
								// trọng lượng thùng
								String tlthung = Objects.toString(MyUtilExcel.getCellValue(cell), "0");
								lix.setTare(Double.parseDouble(tlthung));
							} catch (Exception e) {
							}
							break;
						case 13:
							try {
								// nhóm sản phẩm khuyến mãi
								String manspkm = Objects.toString(MyUtilExcel.getCellValue(cell), null);
								if (manspkm != null && !"".equals(manspkm)) {
									PromotionProductGroupReqInfo km = new PromotionProductGroupReqInfo();
									promotionProductGroupService.selectByCode(manspkm, km);
									lix.setPromotion_product_group(km.getPromotion_product_group());
								}
							} catch (Exception e) {
							}
							break;
						case 14:
							try {
								// số lượng dự trữ
								String sldutru = Objects.toString(MyUtilExcel.getCellValue(cell), "0");
								lix.setReserve_quantity(Double.parseDouble(sldutru));
							} catch (Exception e) {
							}
							break;
						case 15:
							try {
								// mã sản phẩm lever
								String levercode = Objects.toString(MyUtilExcel.getCellValue(cell), null);
								lix.setLever_code(levercode);
							} catch (Exception e) {
							}
							break;
						case 16:
							try {
								// không còn sử dụng
								String disable = Objects.toString(MyUtilExcel.getCellValue(cell), "0");
								lix.setDisable(Boolean.parseBoolean(disable));
							} catch (Exception e) {
							}
							break;
						case 17:
							try {
								// xuất khẩu
								String xk = Objects.toString(MyUtilExcel.getCellValue(cell), "0");
								lix.setTypep(Boolean.parseBoolean(xk));
							} catch (Exception e) {
							}
							break;
						case 18:
							try {
								// thông tin ghi trên packaing list
								String info = Objects.toString(MyUtilExcel.getCellValue(cell), null);
								lix.setProduct_info(info);
							} catch (Exception e) {
							}
							break;
						case 19:
							try {
								// sản phẩm khuyến mãi đính kèm trực tiếp trên
								// sản phẩm.
								String maspkm = Objects.toString(MyUtilExcel.getCellValue(cell), null);
								ProductReqInfo prop = new ProductReqInfo();
								productService.selectByCode(maspkm, prop);
								lix.setPromotion_product(prop.getProduct());
							} catch (Exception e) {
							}
							break;
						case 20:
							try {
								// số lượng thùng trên pallet
								String box = Objects.toString(MyUtilExcel.getCellValue(cell), "0");
								lix.setBox_quantity(Double.parseDouble(box));
							} catch (Exception e) {
							}
							break;
						}
					}
					listProductTemp.add(lix);
				}
				workBook = null;// free
				for (Product it : listProductTemp) {
					ProductReqInfo t = new ProductReqInfo();
					productService.selectByCode(it.getProduct_code(), t);
					Product p = t.getProduct();
					t.setProduct(it);
					if (p != null) {
						it.setId(p.getId());
						it.setLast_modifed_date(new Date());
						it.setCreated_by(account.getMember().getName());
						productService.update(t);
					} else {
						it.setCreated_date(new Date());
						it.setCreated_by(account.getMember().getName());
						productService.insert(t);
					}
				}
				search();
				notify.success();

			}
		} catch (Exception e) {
			logger.error("ProductBean.loadExcel:" + e.getMessage(), e);
		}
	}

	private Workbook getWorkbook(InputStream inputStream, String excelFilePath) throws IOException {
		Workbook workbook = null;
		if (excelFilePath.endsWith("xlsx")) {
			workbook = new XSSFWorkbook(inputStream);
		} else if (excelFilePath.endsWith("xls")) {
			workbook = new HSSFWorkbook(inputStream);
		} else {
			throw new IllegalArgumentException("The specified file is not Excel file");
		}

		return workbook;
	}

	public Product getProductCrud() {
		return productCrud;
	}

	public void setProductCrud(Product productCrud) {
		this.productCrud = productCrud;
	}

	public Product getProductSelect() {
		return productSelect;
	}

	public void setProductSelect(Product productSelect) {
		this.productSelect = productSelect;
	}

	public List<Product> getListProduct() {
		return listProduct;
	}

	public void setListProduct(List<Product> listProduct) {
		this.listProduct = listProduct;
	}

	public List<ProductType> getListProductType() {
		return listProductType;
	}

	public void setListProductType(List<ProductType> listProductType) {
		this.listProductType = listProductType;
	}

	public List<ProductGroup> getListProductGroup() {
		return listProductGroup;
	}

	public void setListProductGroup(List<ProductGroup> listProductGroup) {
		this.listProductGroup = listProductGroup;
	}

}
