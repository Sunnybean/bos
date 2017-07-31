package cn.itcast.bos.web.service.take_delivery;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.itcast.bos.domain.take_delivery.WayBill;


public interface WayBillService {

	void save(WayBill model);

	Page<WayBill> findByPage(Pageable pageable);

	WayBill findByWayBillNum(String wayBillNum);

}
