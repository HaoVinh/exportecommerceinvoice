package trong.lixco.com.bean;

/**
 * Danh muc menu
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;

import trong.lixco.com.account.servicepublics.Menu;
import trong.lixco.com.account.servicepublics.MenuServicePublic;
import trong.lixco.com.account.servicepublics.MenuServicePublicProxy;
import trong.lixco.com.account.servicepublics.Program;
import trong.lixco.com.account.servicepublics.ProgramServicePublic;
import trong.lixco.com.account.servicepublics.ProgramServicePublicProxy;
import trong.lixco.com.util.NameSytem;

@ManagedBean(name = "menuBean2")
@SessionScoped
public class MenuBean2 extends AbstractBean {
	private static final long serialVersionUID = 1L;
	// private List<Menu> menus;
	private Menu menu;
	private ProgramServicePublic programService;
	private MenuServicePublic menuService;
	@Inject
	private Logger logger;
	private String menuStr;

	@Override
	protected Logger getLogger() {
		return logger;
	}

	@Override
	public void initItem() {
		try {
			menu = new Menu();
			programService = new ProgramServicePublicProxy();
			menuService = new MenuServicePublicProxy();
			List<Menu> menus = new ArrayList<>();
			setupMenuPreview(menus);
			StringBuilder builder = new StringBuilder();
			builder.append("<ul class='sidebar-menu' data-widget='tree'> ");
			builder.append("<li class='header' style='font-weight: bold; color: #ccd8de; font-size: larger;'>TTSP ("
					+ getDatabase() + ")</li>");
			generMenu(menus, builder, 0, false);
			builder.append("</ul>");
			this.menuStr = builder.toString();
			builder = null;
		} catch (Exception e) {
			logger.error("MenuBean2.initItem:" + e.getMessage(), e);
		}
	}

	public boolean generMenu(List<Menu> listMenu, StringBuilder buider, long parent_id, boolean courl) {
		try {
			List<Menu> listMenuTemp = new ArrayList<Menu>();
			Iterator<Menu> itr = listMenu.iterator();
			while (itr.hasNext()) {
				Menu item = itr.next();
				Menu p = item.getParent();
				if (parent_id == 0 && p == null) {
					listMenuTemp.add(item);
					itr.remove();
				} else if (parent_id != 0 && p != null && p.getId() == parent_id) {
					listMenuTemp.add(item);
					itr.remove();
				}
			}
			if (listMenuTemp.size() != 0) {
				for (Menu h : listMenuTemp) {
					String url = h.getUrl();
					boolean courlpro = false;
					if (url == null || (url != null && "".equals(url))) {
						boolean status = false;
						for (int i = 0; i < listMenu.size(); i++) {
							if (h.equals(listMenu.get(i).getParent())) {
								status = true;
								break;
							}
						}
						if (status == false) {
						} else {
							buider.append("<li class='treeview'>");
							buider.append("<a href='javascript: void(0)'>");
							buider.append("<i class='" + h.getIcon() + "'></i>" + h.getTenHienThi());
							buider.append("<span class='pull-right-container'><i class='fas fa-angle-left pull-right'></i></span></a>");
							buider.append("<ul class='treeview-menu'>");
						}

					} else {
						buider.append("<li  class='singleNodeNav'><a href='/"+NameSytem.NAMEPROGRAM + url + "'><i class='"
								+ h.getIcon() + "'></i> <span>" + h.getTenHienThi() + "</span></a>");
						courlpro = true;
					}
					generMenu(listMenu, buider, h.getId(), courlpro);
					buider.append("</li>");
				}
				buider.append("</ul>");
				return true;
			}
		} catch (Exception e) {
			logger.error("MenuBean.generMenu:" + e.getMessage(), e);
		}
		return false;
	}

	/*
	 * Cai dat danh sach menu preview
	 */
	public void setupMenuPreview(List<Menu> menus) {
		try {
			Program program = programService.findByName(NameSytem.NAMEPROGRAM);
			Menu temps[] = menuService.find_Program(program);
			if (temps != null) {
				for (int j = 0; j < temps.length; j++) {
					if ("".equals(temps[j].getUrl())) {
						menus.add(temps[j]);
					} else {
						if (getUrlPermissions().size() == 0)
							getAuthorizationManager().isAllowed(getAccount());
						for (int i = 0; i < getUrlPermissions().size(); i++) {
							if (getUrlPermissions().get(i).getUrl().contains(temps[j].getUrl())) {
								menus.add(temps[j]);
								break;
							}
						}
					}
				}
			}
			// model = new DefaultMenuModel();
			// createMenu(menus);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	public String getMenuStr() {
		return menuStr;
	}

	public void setMenuStr(String menuStr) {
		this.menuStr = menuStr;
	}

}
