package kmobrevamp.service;

import java.util.List;

import kmobrevamp.model.Factory;
import kmobrevamp.model.ServiceCenter;
import kmobrevamp.model.User;

public interface UserService {

	public User findUserByEmail(String email);
	public void saveFactoryUser(User user);
	public void saveServiceCenterUser(User user);
	public void saveSupportCenterUser(User user);
	public List<Factory> getFactories();
	public Factory getFactoryById(int id);
	public List<ServiceCenter> getServices();
	public ServiceCenter getServiceById(int id);
}
