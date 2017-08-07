package cn.itcast.bos.service.transit.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.transit.SignInfoRepository;
import cn.itcast.bos.dao.transit.TransitInfoRepository;
import cn.itcast.bos.domain.transit.SignInfo;
import cn.itcast.bos.domain.transit.TransitInfo;
import cn.itcast.bos.index.WayBillIndexRepository;
import cn.itcast.bos.service.transit.SignInfoService;

@Service
@Transactional
public class SignInfoServiceImpl implements SignInfoService {
	@Autowired
	private SignInfoRepository signInfoRepository;

	
	@Autowired 
	private TransitInfoRepository transitInfoRepository;
	
	private WayBillIndexRepository wayBillIndexRepository;

	@Override
	public void save(String transInfoId, SignInfo signInfo) {
		// TODO Auto-generated method stub
		//保存签收录入信息
		signInfoRepository.save(signInfo);
		//关联运输流程
		TransitInfo transitInfo = transitInfoRepository.findOne(Integer.parseInt(transInfoId));
		transitInfo.setSignInfo(signInfo);
		if (signInfo.getSignType().equals("正常")) {
			transitInfo.setStatus("正常签收");
			transitInfo.getWayBill().setSignStatus(3);
			wayBillIndexRepository.save(transitInfo.getWayBill());
			
		}else{
			transitInfo.setStatus("异常");
			transitInfo.getWayBill().setSignStatus(4);
			wayBillIndexRepository.save(transitInfo.getWayBill());
		}
		
		
	}

}
