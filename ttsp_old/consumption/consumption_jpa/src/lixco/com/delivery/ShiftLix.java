package lixco.com.delivery;

public class ShiftLix {
	private long id;
	private String name;
	private int shift_num;
	public ShiftLix() {
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
	public int getShift_num() {
		return shift_num;
	}
	public void setShift_num(int shift_num) {
		this.shift_num = shift_num;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ShiftLix other = (ShiftLix) obj;
		if (id != other.id)
			return false;
		return true;
	}
	

}
