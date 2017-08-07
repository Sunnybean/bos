package cn.itcast.bos.service.base.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.StandardRepository;
import cn.itcast.bos.domain.base.Standard;
import cn.itcast.bos.service.base.StandardService;


@Service
@Transactional
public class StandardServiceImpl implements StandardService {
	
	@Autowired
	private StandardRepository standardRepository;

	@Override
	//保存修改的时候清除缓存
	@CacheEvict(value="standard" ,allEntries=true)
	public void save(Standard standard) {
		 standardRepository.save(standard);
	}

	@Override
	
	@Cacheable(value="standard",key="#pageable.pageNumber+'_'+#pageable.pageSize")
	public Page<Standard> findPageData(Pageable pageable) {
		// TODO Auto-generated method stub
		return standardRepository.findAll(pageable);
	}

	@Override
	//一般查询的方法上加上缓存
	@Cacheable("standard")
	public List<Standard> findAll() {
		// TODO Auto-generated method stub
		return standardRepository.findAll();
	}
}