package kmobrevamp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name="KMR_SupportCenter")
public class SupportCenter {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="support_center_id")
	private int id;
	
	@Length(min=3,max=60,message="*Support Center name must have at least 3 characters & at most 60 characters")
	@NotEmpty(message="*Please provide the Support Center name")
	@Column(name="support_center_name",length=60)
	private String supportcentername;
	
	@Length(min=10,max=200,message="*Support Center address must have at least 10 characters & at most 200 characters")
	@NotEmpty(message="*Please provide the Support Center address")
	@Column(name="address",length=200)
	private String address;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSupportcentername() {
		return supportcentername;
	}

	public void setSupportcentername(String supportcentername) {
		this.supportcentername = supportcentername;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

		
}
