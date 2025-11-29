package trong.lixco.com.entity;
/**
 * @author vantrong
 *
 * 01-08-2017
 */
import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;
@Data
@Entity
public class TonKhoThucTe{
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;
	private Long idsp;
	private String masp;
	private String tensp;
	private double ton;

	public TonKhoThucTe() {
	}
	public TonKhoThucTe(Long idsp, String masp, String tensp, double ton) {
		super();
		this.idsp = idsp;
		this.masp = masp;
		this.tensp = tensp;
		this.ton = ton;
	}
	
}
