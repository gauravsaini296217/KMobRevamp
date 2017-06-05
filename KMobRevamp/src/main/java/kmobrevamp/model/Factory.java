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
@Table(name="KMR_FactoryMaster")
public class Factory {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="factory_id")
	private int id;
	
	@Length(min=3,max=60,message="*Factory name must have at least 3 characters & at most 60 characters")
	@NotEmpty(message="*Please provide the Factory name")
	@Column(name="factoryname", length=60)
	private String name;
	
	@Length(min=10,max=200,message="*Factory address must have at least 10 characters & at most 200 characters")
	@NotEmpty(message="*Please provide the Factory address")
	@Column(name="address", length=200)
	private String address;

	@OneToMany(cascade=CascadeType.ALL)
	@JoinTable(name="KMR_factory_service", joinColumns=@JoinColumn(name="factory_id"), inverseJoinColumns=@JoinColumn(name="service_center_id"))
	private Set<ServiceCenter> serviceCenters;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Set<ServiceCenter> getServiceCenters() {
		return serviceCenters;
	}

	public void setServiceCenters(Set<ServiceCenter> serviceCenters) {
		this.serviceCenters = serviceCenters;
	}

	@Override
	public String toString() {
		return "Factory [id=" + id + ", name=" + name + ", address=" + address + ", serviceCenters=" + serviceCenters
				+ "]";
	}

    
	
}
