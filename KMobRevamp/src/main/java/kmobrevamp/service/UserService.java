package kmobrevamp.service;

import kmobrevamp.model.User;

public interface UserService {

	public User findUserByEmail(String email);
	public void saveFactoryUser(User user);
	public void saveServiceCenterUser(User user);
	public void saveSupportCenterUser(User user);
}
