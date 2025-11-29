package lixco.com.delivery;

public class WorkshopLix {
	private long id;
	private String name;
	public WorkshopLix() {
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WorkshopLix other = (WorkshopLix) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
}
