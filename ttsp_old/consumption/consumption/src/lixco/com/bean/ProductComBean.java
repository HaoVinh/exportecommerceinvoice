package lixco.com.bean;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import lixco.com.commom_ejb.PagingInfo;
import lixco.com.common.FormatHandler;
import lixco.com.common.ImageHandler;
import lixco.com.common.JsonParserUtil;
import lixco.com.common.SessionHelper;
import lixco.com.entity.ProductBrand;
import lixco.com.entity.ProductCom;
import lixco.com.entity.ProductNorm;
import lixco.com.interfaces.IProductBrandService;
import lixco.com.interfaces.IProductComService;
import lixco.com.reqInfo.ProductBrandReqInfo;
import lixco.com.reqInfo.ProductComReqInfo;
import lombok.Getter;
import lombok.Setter;

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
import trong.lixco.com.bean.AbstractBean;
import trong.lixco.com.convertfont.FontType;
import trong.lixco.com.convertfont.MyConvertFontFactory;
import trong.lixco.com.util.MyUtilExcel;
import trong.lixco.com.util.Notify;
import trong.lixco.com.util.ToolReport;

import com.google.gson.JsonObject;

@Named
@ViewScoped
public class ProductComBean extends AbstractBean {
	private static final long serialVersionUID = -7261573870814228140L;
	@Inject
	private Logger logger;
	@Inject
	private IProductBrandService productBrandService;
	@Inject
	private IProductComService productComService;
	private ProductCom productComCrud;
	private ProductCom productComSelect;
	private List<ProductCom> listProductCom;
	@Getter
	@Setter
	private List<ProductCom> listProductComFilter;
	private String pComCode;
	private String pComName;
	private ProductBrand productBrand;
	private Account account;

	@Override
	protected void initItem() {
		try {
			search();
			FacesContext facesContext = FacesContext.getCurrentInstance();
			HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
			account = SessionHelper.getInstance().getSession("account", request, Account.class);
			productComCrud = new ProductCom();
		} catch (Exception e) {
			logger.error("ProductComBean.initItem:" + e.getMessage(), e);
		}
	}

	@Override
	protected Logger getLogger() {
		return logger;
	}

	public void search() {
		try {
			listProductCom = new ArrayList<ProductCom>();
			JsonObject jsonInfo = new JsonObject();
			jsonInfo.addProperty("pcom_code", FormatHandler.getInstance().converViToEn(pComCode));
			jsonInfo.addProperty("pcom_name", FormatHandler.getInstance().converViToEn(pComName));
			jsonInfo.addProperty("product_brand_id", productBrand != null ? productBrand.getId() : 0);
			JsonObject json = new JsonObject();
			json.add("pcom_info", jsonInfo);
			productComService.search(JsonParserUtil.getGson().toJson(json), listProductCom);
		} catch (Exception e) {
			logger.error("ProductComBean.search:" + e.getMessage(), e);
		}
	}

	public void exportExcel() {
		try {
			if (listProductCom.size() > 0) {
				List<Object[]> results = new ArrayList<Object[]>();
				Object[] title = { "id", "ngaytao", "ngaycn", "ma", "ten", "dvt", "mabrand", "tenbrand", "maspdinhmuc",
						"tenspdinhmuc" };
				results.add(title);
				for (ProductCom p : listProductCom) {
					ProductBrand pb = p.getProduct_brand();
					ProductNorm pn = p.getProduct_norm();
					Object row[] = { p.getId(), p.getCreated_date(), p.getLast_modifed_date(), p.getPcom_code(),
							p.getPcom_name(), p.getUnit(), pb == null ? "" : pb.getPbrand_code(),
							pb == null ? "" : pb.getPbrand_name(), pn == null ? "" : pn.getPnorm_code(),
							pn == null ? "" : pn.getPnorm_name() };
					results.add(row);
				}
				String titleEx = "dmsp_brand";
				ToolReport.printReportExcelRaw(results, titleEx);
			}
		} catch (Exception e) {
			logger.error("ProductBean.exportExcel:" + e.getMessage(), e);
		}
	}

	public void saveOrUpdate() {
		PrimeFaces current = PrimeFaces.current();
		try {
			if (productComCrud != null) {
				String pComCode = productComCrud.getPcom_code();
				String pComName = productComCrud.getPcom_name();
				ProductBrand productBrand = productComCrud.getProduct_brand();
				if (pComCode != null && pComCode != "" && pComName != null && pComName != "" && productBrand != null) {
					ProductComReqInfo t = new ProductComReqInfo(productComCrud);
					if (productComCrud.getId() == 0) {
						// check code đã tồn tại chưa
						if (allowSave(new Date())) {
							if (productComService.checkProductComCode(pComCode, 0) == 0) {
								productComCrud.setCreated_date(new Date());
								productComCrud.setCreated_by(account.getMember().getName());
								if (productComService.insert(t) != -1) {
									current.executeScript("swaldesigntimer('Thành công!', 'Thêm thành công!','success',2000);");
									listProductCom.add(0, productComCrud);
									if (listProductComFilter != null) {
										listProductComFilter.add(0, productComCrud);
									}
								} else {
									current.executeScript("swaldesigntimer('Xảy ra lỗi!', 'Lỗi hệ thống!','error',2000);");
								}
							} else {
								current.executeScript("swaldesigntimer('Cảnh báo', 'Mã đã tồn tại!','warning',2000);");
							}
						} else {
							current.executeScript("swaldesigntimer('Cảnh báo!', 'Tài khoản này không có quyền thực hiện hoặc tháng đã khoá!','error',2000);");
						}
					} else {
						// check code update đã tồn tại chưa
						if (allowUpdate(new Date())) {
							if (productComService.checkProductComCode(pComCode, productComCrud.getId()) == 0) {
								productComCrud.setLast_modifed_by(account.getMember().getName());
								productComCrud.setLast_modifed_date(new Date());
								if (productComService.update(t) != -1) {
									current.executeScript("swaldesigntimer('Thành công!', 'Cập nhật thành công!','success',2000);");
									listProductCom.set(listProductCom.indexOf(productComCrud), productComCrud);
									if (listProductComFilter != null) {
										listProductComFilter.set(listProductCom.indexOf(productComCrud), productComCrud);
									}
								} else {
									current.executeScript("swaldesigntimer('Xảy ra lỗi!', 'Lỗi hệ thống!','error',2000);");
								}
							} else {
								current.executeScript("swaldesigntimer('Cảnh báo', 'Mã đã tồn tại!','warning',2000);");
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
			logger.error("ProductComBean.saveOrUpdate:" + e.getMessage(), e);
		}
	}

	public void showEdit() {
		try {
			productComCrud = productComSelect;
		} catch (Exception e) {
			logger.error("ProductComBean.showEdit:" + e.getMessage(), e);
		}
	}

	public void createNew() {
		productComCrud = new ProductCom();
	}

	public void delete() {
		PrimeFaces current = PrimeFaces.current();
		try {
			if (productComSelect != null) {
				if (allowDelete(new Date())) {
					if (productComService.deleteById(productComSelect.getId()) != -1) {
						current.executeScript("swaldesigntimer('Thành công!', 'Xóa thành công!','success',2000);");
						listProductCom.remove(productComSelect);
						if (listProductComFilter != null) {
							listProductComFilter.remove(productComSelect);
						}
					} else {
						current.executeScript("swaldesigntimer('Xảy ra lỗi!', 'Lỗi hệ thống!','error',2000);");
					}
				} else {
					current.executeScript("swaldesigntimer('Cảnh báo!', 'Tài khoản này không có quyền thực hiện hoặc tháng đã khoá!','error',2000);");
				}
			} else {
				current.executeScript("swaldesigntimer('Cảnh báo!', 'Chưa chọn dòng để xóa!','warning',2000);");
			}
		} catch (Exception e) {
			logger.error("ProductComBean.delete:" + e.getMessage(), e);
		}
	}

	public List<ProductBrand> autoCompleteProductBrand(String text) {
		try {
			List<ProductBrand> list = new ArrayList<ProductBrand>();
			productBrandService.findLike(FormatHandler.getInstance().converViToEn(text), 120, list);
			return list;
		} catch (Exception e) {
			logger.error("ProductComBean.autoCompleteProductBrand:" + e.getMessage(), e);
		}
		return null;
	}

	public void showDialogUpload() {
		try {
			PrimeFaces current = PrimeFaces.current();
			current.executeScript("PF('uploadpdffile').show();");
		} catch (Exception e) {
			logger.error("ProductComBean.showDialogUpload:" + e.getMessage(), e);
		}
	}

	private Workbook getWorkbook(FileInputStream inputStream, String excelFilePath) throws IOException {
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
	@Getter
	@Setter
	boolean fontvni = false;
	public void loadExcel(FileUploadEvent event) {
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		OutputStream outStream = null;
		FileInputStream inputStream = null;
		try {
			if (event.getFile() != null) {
				UploadedFile part = event.getFile();
				byte[] byteFile = event.getFile().getContent();
				String filename = part.getFileName();
				int index = filename.lastIndexOf(".");
				String type = filename.substring(index);
				String url = FacesContext.getCurrentInstance().getExternalContext()
						.getRealPath("/resources/upload/productcom");
				String nameNewFile = ImageHandler.getInstance().generateUniqueFileName(type, url);
				File fileSave = new File(url, nameNewFile + "." + type);
				outStream = new FileOutputStream(fileSave);
				outStream.write(byteFile);
				List<ProductCom> listProductCom = new ArrayList<ProductCom>();
				inputStream = new FileInputStream(fileSave);
				Workbook workBook = getWorkbook(inputStream, "/resources/upload/productcom/" + nameNewFile + "." + type);
				Sheet firstSheet = workBook.getSheetAt(0);
				Iterator<Row> rows = firstSheet.iterator();
				ProductCom lix;
				while (rows.hasNext()) {
					rows.next();
					rows.remove();
					break;
				}
				while (rows.hasNext()) {
					Row row = rows.next();
					Iterator<Cell> cells = row.cellIterator();
					lix = new ProductCom();
					lv2: while (cells.hasNext()) {
						Cell cell = cells.next();
						String cellvalue = Objects.toString(MyUtilExcel.getCellValue(cell), null);
						if (fontvni)
							cellvalue = MyConvertFontFactory.convert(FontType.VNI_WINDOWS, FontType.UNICODE, cellvalue);
						int columnIndex = cell.getColumnIndex();

						switch (columnIndex) {
						case 0:
							try {
								lix.setPcom_code(cellvalue);
							} catch (Exception e) {
							}
							break;
						case 1:
							try {
								lix.setPcom_name(cellvalue);
							} catch (Exception e) {
							}
							break;
						case 2:
							try {
								ProductBrandReqInfo pb = new ProductBrandReqInfo();
								productBrandService.selectByCode(cellvalue, pb);
								if (pb.getProduct_brand() == null) {
									break lv2;
								}
								lix.setProduct_brand(pb.getProduct_brand());
							} catch (Exception e) {
							}
							break;
						case 3:
							try {
								lix.setUnit(cellvalue);
							} catch (Exception e) {
							}
							break;
						// case 4:
						// try{
						// String madm=(String)MyUtilExcel.getCellValue(cell);
						// // ProductNorm
						// pnorm=productNormService.getProductNormByCode(madm);
						// // if(pnorm ==null){
						// // break lv2;
						// // }
						// // lix.setProductNorm(pnorm);
						// }catch(Exception e){
						// }
						// break;
						}
					}
					listProductCom.add(lix);
				}
				workBook = null;
				for (ProductCom it : listProductCom) {
					ProductComReqInfo t = new ProductComReqInfo();
					productComService.selectByCode(it.getPcom_code(), t);
					ProductCom pc = t.getProduct_com();
					t.setProduct_com(it);
					if (pc != null) {
						it.setId(pc.getId());
						it.setLast_modifed_date(new Date());
						it.setCreated_by(account.getMember().getName());
						productComService.update(t);
					} else {
						it.setCreated_date(new Date());
						it.setCreated_by(account.getMember().getName());
						productComService.insert(t);
					}
				}
				search();
				notify.success();

			}
		} catch (Exception e) {
			logger.error("ProductBrandBean.loadExcel:" + e.getMessage(), e);
			notify.error();
		} finally {
			try {
				if (outStream != null) {
					outStream.close();
				}
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public ProductCom getProductComCrud() {
		return productComCrud;
	}

	public void setProductComCrud(ProductCom productComCrud) {
		this.productComCrud = productComCrud;
	}

	public ProductCom getProductComSelect() {
		return productComSelect;
	}

	public void setProductComSelect(ProductCom productComSelect) {
		this.productComSelect = productComSelect;
	}

	public List<ProductCom> getListProductCom() {
		return listProductCom;
	}

	public void setListProductCom(List<ProductCom> listProductCom) {
		this.listProductCom = listProductCom;
	}

	public String getpComCode() {
		return pComCode;
	}

	public void setpComCode(String pComCode) {
		this.pComCode = pComCode;
	}

	public String getpComName() {
		return pComName;
	}

	public void setpComName(String pComName) {
		this.pComName = pComName;
	}

	public ProductBrand getProductBrand() {
		return productBrand;
	}

	public void setProductBrand(ProductBrand productBrand) {
		this.productBrand = productBrand;
	}
}
