package cn.itcast.bos.service.base.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.CourierRepository;
import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.service.base.CourierService;

@Service
@Transactional
public class CourierServiceImpl implements CourierService {
	@Autowired
	private CourierRepository courierRepository;

	@Override
	public void save(Courier courier) {

		courierRepository.save(courier);
	}


	@Override
	public Page<Courier> findPageData(Specification<Courier> specfication, Pageable pageable) {
		// TODO Auto-generated method stub
		return courierRepository.findAll(specfication,pageable);
	}


	@Override
	public void delBatch(String[] idArray) {
		// TODO Auto-generated method stub
		for (String i :idArray) {
			Integer id = Integer.parseInt(i);
			courierRepository.updateDelTag(id);
		}
	}
	
	/*@Override
	public Page<Courier> findPageData(Specification<T> , Pageable pageable) {
		// TODO Auto-generated method stub
		return courierRepository.findAll(pageable);
	}*/

}
