package cn.itcast.bos.web.service.take_delivery.impl;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.take_delivery.PromotionRepository;
import cn.itcast.bos.domain.page.PageBean;
import cn.itcast.bos.domain.take_delivery.Promotion;
import cn.itcast.bos.web.service.take_delivery.PromotionService;

@Service
@Transactional
public class PromotionServiceImpl implements PromotionService {

	@Autowired
	private PromotionRepository promotionRepository;

	@Override
	public void save(Promotion model) {
		promotionRepository.save(model);
	}

	@Override
	public Page<Promotion> findData(Pageable pageable) {
		// TODO Auto-generated method stub
		return promotionRepository.findAll(pageable);
	}

	@Override
	public PageBean<Promotion> findPageData(int page, int rows) {
		// TODO Auto-generated method stub
		Pageable pageable = new PageRequest(page-1,rows);
		Page<Promotion> pageData = promotionRepository.findAll(pageable);
		
		PageBean<Promotion> pageBean = new PageBean<Promotion>();
		pageBean.setPageData(pageData.getContent());
		pageBean.setTotalCount(pageData.getTotalElements());
		return pageBean;
	}
}
