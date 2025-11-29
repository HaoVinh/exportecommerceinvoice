package lixco.com.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class KeHoachSL_SanXuat implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private int year;
	private String kenh;
	private int kenhLever;
	private String kenhDT;
	private String thuockenh;
	
	private double sl_1;
	private double sl_2;
	private double sl_3;
	private double sl_4;
	private double sl_5;
	private double sl_6;
	private double sl_7;
	private double sl_8;
	private double sl_9;
	private double sl_10;
	private double sl_11;
	private double sl_12;

}
