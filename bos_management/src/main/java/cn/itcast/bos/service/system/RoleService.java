package cn.itcast.bos.service.system;

import java.util.List;

import cn.itcast.bos.domain.system.Role;
import cn.itcast.bos.domain.system.User;

public interface RoleService {
		List<Role> findByUser(User user);

		void save(Role model, String[] permissionIds, String menuIds);

		List<Role> findAll();
}
