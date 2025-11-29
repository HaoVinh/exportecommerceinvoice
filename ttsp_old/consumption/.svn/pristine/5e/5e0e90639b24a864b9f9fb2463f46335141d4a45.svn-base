package lixco.com.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

//danh muc binding với danh mục nhập xuất
@Entity
@Table(name="bindiecategories")
public class BindIECategories implements Serializable,Cloneable{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@ManyToOne(fetch=FetchType.LAZY)
	private IECategories ie_categories_source;
	@ManyToOne(fetch=FetchType.LAZY)
	private IECategories ie_categories_bind;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public IECategories getIe_categories_source() {
		return ie_categories_source;
	}
	public void setIe_categories_source(IECategories ie_categories_source) {
		this.ie_categories_source = ie_categories_source;
	}
	public IECategories getIe_categories_bind() {
		return ie_categories_bind;
	}
	public void setIe_categories_bind(IECategories ie_categories_bind) {
		this.ie_categories_bind = ie_categories_bind;
	}
	@Override
	public BindIECategories clone() throws CloneNotSupportedException {
		return (BindIECategories) super.clone();
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BindIECategories other = (BindIECategories) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
}
