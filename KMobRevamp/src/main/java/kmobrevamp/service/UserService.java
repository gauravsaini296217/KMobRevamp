package kmobrevamp.service;

import kmobrevamp.model.User;

public interface UserService {

	public User findUserByEmail(String email);
	public void saveUser(User user);
}
