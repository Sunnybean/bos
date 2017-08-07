package cn.itcast.bos.service.take_delivery;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.itcast.bos.domain.take_delivery.WayBill;


public interface WayBillService {

	void save(WayBill model);

	Page<WayBill> findByPage(WayBill model, Pageable pageable);

	WayBill findByWayBillNum(String wayBillNum);
	
	
	//同步索引到数据库
	public void syncIndex();

	
	//报表查询
	List<WayBill> findWayBills(WayBill model);

}
