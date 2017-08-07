package cn.itcast.bos.service.transit.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.take_delivery.WayBillRepository;
import cn.itcast.bos.dao.transit.TransitInfoRepository;
import cn.itcast.bos.domain.take_delivery.WayBill;
import cn.itcast.bos.domain.transit.TransitInfo;
import cn.itcast.bos.index.WayBillIndexRepository;
import cn.itcast.bos.service.transit.TransitInofService;
@Service
@Transactional
public class TransitInfoServiceImpl implements TransitInofService {

	
	@Autowired
	private WayBillRepository wayBillRespostory;
	
	@Autowired
	private TransitInfoRepository transitInfoRepository;
	
	@Autowired
	private WayBillIndexRepository	wayBillIndexRespostory;
	
	@Override
	public void createTransits(String wayBillIds) {
		// TODO Auto-generated method stub
		if (StringUtils.isNotBlank(wayBillIds)) {
			for(String wayBillid :wayBillIds.split(",")){
				WayBill wayBill = wayBillRespostory.findOne(Integer.parseInt(wayBillid));
				if (wayBill.getSignStatus() == 1 ) {
					//代发货，生成transitInfo信息
					TransitInfo transitInfo = new TransitInfo();
					transitInfo.setWayBill(wayBill);
					transitInfo.setStatus("出入库中转");
					transitInfoRepository.save(transitInfo);
					wayBill.setSignStatus(2);
					//同步索引库
					wayBillIndexRespostory.save(wayBill);
					
				}
			}
		}
	}

	@Override
	public Page<TransitInfo> findByPage(Pageable pageable) {
		// TODO Auto-generated method stub
		return  transitInfoRepository.findAll(pageable);
	}

}
