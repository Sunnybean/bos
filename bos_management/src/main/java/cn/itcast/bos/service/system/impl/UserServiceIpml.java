package cn.itcast.bos.service.system.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.system.UserRepository;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.system.UserService;
@Service
@Transactional
public class UserServiceIpml implements UserService {
	
	@Autowired
	private UserRepository userRepository;
	

	@Override
	public User findByUsername(String username) {
		// TODO Auto-generated method stub
		return userRepository.findByUsername(username);
	}

}
