package kmobrevamp.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name="KMR_ServiceCenter")
public class ServiceCenter {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="service_center_id")
	private int id;
	
	@Length(min=3,max=60,message="*Service Center name must have at least 3 characters & at most 60 characters")
	@NotEmpty(message="*Please provide the Service Center name")
	@Column(name="service_center_name",length=60)
	private String servicecentername;
	
	@Length(min=10,max=200,message="*Service Center address must have at least 10 characters & at most 200 characters")
	@NotEmpty(message="*Please provide the Service Center address")
	@Column(name="address",length=200)
	private String address;

	@OneToMany(cascade=CascadeType.ALL)
	@JoinTable(name="KMR_service_support", joinColumns= @JoinColumn(name="service_center_id"), inverseJoinColumns=@JoinColumn(name="support_center_id") )
	private Set<SupportCenter> supportCenters;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getServicecentername() {
		return servicecentername;
	}

	public void setServicecentername(String servicecentername) {
		this.servicecentername = servicecentername;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Set<SupportCenter> getSupportCenters() {
		return supportCenters;
	}

	public void setSupportCenters(Set<SupportCenter> supportCenters) {
		this.supportCenters = supportCenters;
	}

	@Override
	public String toString() {
		return "ServiceCenter [id=" + id + ", servicecentername=" + servicecentername + ", address=" + address
				+ ", supportCenters=" + supportCenters + "]";
	}
	
	
	
}
