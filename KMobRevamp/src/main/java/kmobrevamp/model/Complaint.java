package kmobrevamp.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Past;

@Entity
@Table(name="KMR_Complaint")
public class Complaint {

	@Id
	@Column(name="workorderno")
	private Long workorderno;
	
	@Past(message="inDate can't be future date")
	@Column(name="indate")
	private Date indate;
	
	@Past(message="regnDate can't be future date")
	@Column(name="regndate")
	private Date regndate;
	
	@Past(message="Date can't be future date")
	@Column(name="date")
	private Date date;
	
	@Column(name="tokenno")
	private Long tokenno;
	
	@Column(name="claimno")
	private Long claimno;

	@Column(name="model")
	private String model;
	
	@Column(name="slno")
	private String slno;
	
	@Column(name="imeiorrsnno")
	private String imeiorrsnno;
	
	@Column(name="batteryno")
	private String batteryno;
	
	@Column(name="dateofpurchase")
	private Date dateofpurchase;
	
	@Column(name="status", columnDefinition="varchar(10) default 'AMC'")
	private String status;
	
	public Long getWorkorderno() {
		return workorderno;
	}

	public void setWorkorderno(Long workorderno) {
		this.workorderno = workorderno;
	}

	public Date getIndate() {
		return indate;
	}

	public void setIndate(Date indate) {
		this.indate = indate;
	}

	public Date getRegndate() {
		return regndate;
	}

	public void setRegndate(Date regndate) {
		this.regndate = regndate;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Long getTokenno() {
		return tokenno;
	}

	public void setTokenno(Long tokenno) {
		this.tokenno = tokenno;
	}

	public Long getClaimno() {
		return claimno;
	}

	public void setClaimno(Long claimno) {
		this.claimno = claimno;
	}
	
	    
	
}
