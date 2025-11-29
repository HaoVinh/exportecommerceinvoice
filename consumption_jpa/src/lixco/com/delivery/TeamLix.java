package lixco.com.delivery;

public class TeamLix {
	private long id;
	private String name;
	private WorkshopLix workshopLix;
	
	public TeamLix() {
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
	public WorkshopLix getWorkshopLix() {
		return workshopLix;
	}
	public void setWorkshopLix(WorkshopLix workshopLix) {
		this.workshopLix = workshopLix;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TeamLix other = (TeamLix) obj;
		if (id != other.id)
			return false;
		return true;
	}
	

}
