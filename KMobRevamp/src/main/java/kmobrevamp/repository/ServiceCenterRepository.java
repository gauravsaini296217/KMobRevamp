package kmobrevamp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kmobrevamp.model.ServiceCenter;

@Repository("serviceCenterRepository")
public interface ServiceCenterRepository extends JpaRepository<ServiceCenter, Integer> {

    public List<ServiceCenter> findAll();
	
	public ServiceCenter findById(int factory_id);
	
	
}
