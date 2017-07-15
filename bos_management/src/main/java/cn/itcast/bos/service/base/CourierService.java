package cn.itcast.bos.service.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.itcast.bos.domain.base.Courier;

public interface CourierService {

	void save(Courier courier);

	Page<Courier> findPageData(Pageable pageable);

}
