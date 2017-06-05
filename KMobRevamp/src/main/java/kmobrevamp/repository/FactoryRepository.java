package kmobrevamp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kmobrevamp.model.Factory;

@Repository("factoryRepository")
public interface FactoryRepository extends JpaRepository<Factory, Integer>{

	public List<Factory> findAll();
	
	public Factory findById(int factory_id);
	
}
