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

import lixco.com.common.FormatHandler;
import lixco.com.common.ImageHandler;
import lixco.com.common.JsonParserUtil;
import lixco.com.common.SessionHelper;
import lixco.com.entity.ProductBrand;
import lixco.com.interfaces.IProductBrandService;
import lixco.com.reqInfo.ProductBrandReqInfo;
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
public class ProductBrandBean extends AbstractBean {
	private static final long serialVersionUID = 7751390318581104133L;
	@Inject
	private Logger logger;
	@Inject
	private IProductBrandService productBrandService;
	private ProductBrand productBrandCrud;
	private ProductBrand productBrandSelect;
	private List<ProductBrand> listProductBrand;
	@Getter
	@Setter
	private List<ProductBrand> listProductBrandFilter;
	private String pBrandCode;
	private String pBrandName;
	private Account account;

	@Override
	protected void initItem() {
		try {
			search();
			FacesContext facesContext = FacesContext.getCurrentInstance();
			HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
			account = SessionHelper.getInstance().getSession("account", request, Account.class);
			productBrandCrud = new ProductBrand();
		} catch (Exception e) {
			logger.error("ProductBrandBean.initItem:" + e.getMessage(), e);
		}
	}

	@Override
	protected Logger getLogger() {
		return logger;
	}

	public void search() {
		try {
			listProductBrand = new ArrayList<ProductBrand>();
			JsonObject jsonInfo = new JsonObject();
			jsonInfo.addProperty("pbrand_code", FormatHandler.getInstance().converViToEn(pBrandCode));
			jsonInfo.addProperty("pbrand_name", FormatHandler.getInstance().converViToEn(pBrandName));
			JsonObject jsonPage = new JsonObject();
			JsonObject json = new JsonObject();
			json.add("pbrand_info", jsonInfo);
			json.add("page", jsonPage);
			productBrandService.search(JsonParserUtil.getGson().toJson(json), listProductBrand);
		} catch (Exception e) {
			logger.error("ProductBrandBean.search:" + e.getMessage(), e);
		}
	}

	public void exportExcel() {
		try {
			if (listProductBrand.size() > 0) {
				List<Object[]> results = new ArrayList<Object[]>();
				Object[] title = { "id", "ngaytao", "ngaycn", "ma", "ten", "dvt" };
				results.add(title);
				for (ProductBrand p : listProductBrand) {
					Object row[] = { p.getId(), p.getCreated_date(), p.getLast_modifed_date(), p.getPbrand_code(),
							p.getPbrand_name(), p.getUnit() };
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
			if (productBrandCrud != null) {
				String pBrandCode = productBrandCrud.getPbrand_code();
				String pBrandName = productBrandCrud.getPbrand_name();
				if (pBrandCode != null && pBrandCode != "" && pBrandName != null && pBrandName != "") {
					ProductBrandReqInfo t = new ProductBrandReqInfo(productBrandCrud);
					if (productBrandCrud.getId() == 0) {
						// check code đã tồn tại chưa
						if (allowSave(new Date())) {
							if (productBrandService.checkProductBrandCode(pBrandCode, 0) == 0) {
								productBrandCrud.setCreated_date(new Date());
								productBrandCrud.setCreated_by(account.getMember().getName());
								if (productBrandService.insert(t) != -1) {
									current.executeScript("swaldesigntimer('Thành công!', 'Thêm thành công!','success',2000);");
									listProductBrand.add(0, productBrandCrud);
									if (listProductBrandFilter != null) {
										listProductBrandFilter.add(0, productBrandCrud);
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
							if (productBrandService.checkProductBrandCode(pBrandCode, productBrandCrud.getId()) == 0) {
								productBrandCrud.setLast_modifed_by(account.getMember().getName());
								productBrandCrud.setLast_modifed_date(new Date());
								if (productBrandService.update(t) != -1) {
									current.executeScript("swaldesigntimer('Thành công!', 'Cập nhật thành công!','success',2000);");
									listProductBrand.set(listProductBrand.indexOf(productBrandCrud), productBrandCrud);
									if (listProductBrandFilter != null) {
										listProductBrandFilter.set(listProductBrandFilter.indexOf(productBrandCrud),
												productBrandCrud);
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
			logger.error("ProductBrandBean.saveOrUpdate:" + e.getMessage(), e);
		}
	}

	public void showEdit() {
		try {
			productBrandCrud = productBrandSelect;
		} catch (Exception e) {
			logger.error("ProductBrandBean.showEdit:" + e.getMessage(), e);
		}
	}

	public void createNew() {
		productBrandCrud = new ProductBrand();
	}

	public void delete() {
		PrimeFaces current = PrimeFaces.current();
		try {
			if (productBrandSelect != null) {
				if (allowDelete(new Date())) {
					if (productBrandService.deleteById(productBrandSelect.getId()) != -1) {
						current.executeScript("swaldesigntimer('Thành công!', 'Xóa thành công!','success',2000);");
						listProductBrand.remove(productBrandSelect);
						if (listProductBrandFilter != null) {
							listProductBrandFilter.remove(productBrandSelect);
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
			logger.error("ProductBrandBean.delete:" + e.getMessage(), e);
		}
	}

	public void showDialogUpload() {
		try {
			PrimeFaces current = PrimeFaces.current();
			current.executeScript("PF('uploadpdffile').show();");
		} catch (Exception e) {
			logger.error("ProductBrandBean.showDialogUpload:" + e.getMessage(), e);
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
						.getRealPath("/resources/upload/productbrand");
				String nameNewFile = ImageHandler.getInstance().generateUniqueFileName(type, url);
				File fileSave = new File(url, nameNewFile + "." + type);
				outStream = new FileOutputStream(fileSave);
				outStream.write(byteFile);
				List<ProductBrand> listProductBrand = new ArrayList<ProductBrand>();
				inputStream = new FileInputStream(fileSave);
				Workbook workBook = getWorkbook(inputStream, "/resources/upload/productbrand" + "/" + nameNewFile + "."
						+ type);
				Sheet firstSheet = workBook.getSheetAt(0);
				Iterator<Row> rows = firstSheet.iterator();
				ProductBrand lix;
				while (rows.hasNext()) {
					rows.next();
					rows.remove();
					break;
				}
				while (rows.hasNext()) {
					Row row = rows.next();
					Iterator<Cell> cells = row.cellIterator();
					lix = new ProductBrand();
					while (cells.hasNext()) {
						Cell cell = cells.next();
						int columnIndex = cell.getColumnIndex();
						String cellvalue = Objects.toString(MyUtilExcel.getCellValue(cell), null);
						if (fontvni)
							cellvalue = MyConvertFontFactory.convert(FontType.VNI_WINDOWS, FontType.UNICODE, cellvalue);
						switch (columnIndex) {
						case 0:
							try {
								lix.setPbrand_code(cellvalue);
							} catch (Exception e) {
							}
							break;
						case 1:
							try {
								lix.setPbrand_name(cellvalue);
							} catch (Exception e) {
							}
							break;
						case 2:
							try {
								lix.setUnit(cellvalue);
							} catch (Exception e) {
							}
							break;
						}
					}
					listProductBrand.add(lix);
				}
				workBook = null;
				for (ProductBrand it : listProductBrand) {
					ProductBrandReqInfo p = new ProductBrandReqInfo();
					productBrandService.selectByCode(it.getPbrand_code(), p);
					ProductBrand temp = p.getProduct_brand();
					p.setProduct_brand(it);
					if (temp != null) {
						it.setId(temp.getId());
						it.setLast_modifed_date(new Date());
						it.setCreated_by(account.getMember().getName());
						productBrandService.update(p);
					} else {
						it.setCreated_date(new Date());
						it.setCreated_by(account.getMember().getName());
						productBrandService.insert(p);
					}
				}

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

	public ProductBrand getProductBrandCrud() {
		return productBrandCrud;
	}

	public void setProductBrandCrud(ProductBrand productBrandCrud) {
		this.productBrandCrud = productBrandCrud;
	}

	public ProductBrand getProductBrandSelect() {
		return productBrandSelect;
	}

	public void setProductBrandSelect(ProductBrand productBrandSelect) {
		this.productBrandSelect = productBrandSelect;
	}

	public List<ProductBrand> getListProductBrand() {
		return listProductBrand;
	}

	public void setListProductBrand(List<ProductBrand> listProductBrand) {
		this.listProductBrand = listProductBrand;
	}

	public String getpBrandCode() {
		return pBrandCode;
	}

	public void setpBrandCode(String pBrandCode) {
		this.pBrandCode = pBrandCode;
	}

	public String getpBrandName() {
		return pBrandName;
	}

	public void setpBrandName(String pBrandName) {
		this.pBrandName = pBrandName;
	}

}
