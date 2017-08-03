package cn.itcast.bos.service.system.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import cn.itcast.bos.dao.system.MenuRepository;
import cn.itcast.bos.domain.system.Menu;
import cn.itcast.bos.service.system.MenuService;

public class MenuServiceImpl implements MenuService {
	
	@Autowired
	private MenuRepository menuRepository;
	@Override
	public List<Menu> findAll() {
		// TODO Auto-generated method stub
		return menuRepository.findAll();
	}
	@Override
	public void save(Menu menu) {
		// TODO Auto-generated method stub
		//防止用于没有选中
		if (menu.getParentMenu()!= null && menu.getParentMenu().getId()==0) {
			menu.setParentMenu(null);
		}
		menuRepository.save(menu);
	}

}
