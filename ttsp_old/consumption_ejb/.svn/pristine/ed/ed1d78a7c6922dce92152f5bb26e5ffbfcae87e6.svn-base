package lixco.com.interfaces;

import java.util.List;

import lixco.com.entity.Inventory;
import lixco.com.reportInfo.HangChamLuanChuyen;
import lixco.com.reportInfo.TonKhoThang;
import lixco.com.reqInfo.InfoInventory;

public interface IInventoryService {
	public int search(String json, List<Inventory> list);

	public int saveOrUpdateInventory(int month, int year, String memberName, List<TonKhoThang> list);

	public int infoInventory(int month, int year, InfoInventory infoInventory);

	public int getListInventory(String json, List<TonKhoThang> list);

	public double getInventoryCurrentOfProduct(long productId);

	public int getListExpectedInventory(String json, List<Object[]> list,boolean bonoibo);
	public List<Object[]> getListExpectedInventory2(String json, boolean bonoibo);
	public List<Object[]> getListExpectedOrder(String json, boolean bonoibo);
	public List<Object[]> getListExpectedOrderKenhKH(String json, boolean bonoibo);

	public Inventory selectByIdProduct(long id, int month, int year);
	public Inventory create(Inventory entity);
	public Inventory update(Inventory account);
	public int getListExpectedInventoryDetail(String json, List<Object[]> list,boolean bonoibo);
	public int getListExpectedInventoryDetail2(String json, List<Object[]> list,boolean bonoibo);
	public int getSPChuaGiao(String json, List<Object[]> list);
	public List<TonKhoThang> tonkhothang(String json);
}
