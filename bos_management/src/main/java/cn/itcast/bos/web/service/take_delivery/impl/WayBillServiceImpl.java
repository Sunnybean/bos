package cn.itcast.bos.web.service.take_delivery.impl;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.take_delivery.WayBillRepository;
import cn.itcast.bos.domain.take_delivery.WayBill;
import cn.itcast.bos.web.service.take_delivery.WayBillService;

@Service
@Transactional
public class WayBillServiceImpl implements WayBillService {
	
	@Autowired
	private WayBillRepository wayBillRepository;
	@Override
	public void save(WayBill model) {
		// TODO Auto-generated method stub
		WayBill persistWayBill = wayBillRepository.findByWayBillNum(model.getWayBillNum());
		if (persistWayBill.getId() == null) {
			wayBillRepository.save(model);
		}else{
			Integer id =persistWayBill.getId();
			try {
				BeanUtils.copyProperties(persistWayBill, model);
				persistWayBill.setId(id);
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	
	}
	@Override
	public Page<WayBill> findByPage(Pageable pageable) {
		
		return wayBillRepository.findAll(pageable);
	}
	@Override
	public WayBill findByWayBillNum(String wayBillNum) {
		// TODO Auto-generated method stub
		return wayBillRepository.findByWayBillNum(wayBillNum);
	}

}
