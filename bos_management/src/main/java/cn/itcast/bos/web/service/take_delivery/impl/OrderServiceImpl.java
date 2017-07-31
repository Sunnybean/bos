package cn.itcast.bos.web.service.take_delivery.impl;

import java.util.Date;
import java.util.Iterator;
import java.util.UUID;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.AreaRepository;
import cn.itcast.bos.dao.base.FixedAreaRepository;
import cn.itcast.bos.dao.take_delivery.OrderRepository;
import cn.itcast.bos.dao.take_delivery.WorkBillRepository;
import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.domain.base.SubArea;
import cn.itcast.bos.domain.constant.Contants;
import cn.itcast.bos.domain.take_delivery.Order;
import cn.itcast.bos.domain.take_delivery.WorkBill;
import cn.itcast.bos.web.service.take_delivery.OrderService;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private FixedAreaRepository fixedAreaRepository;
	@Autowired
	private AreaRepository areaRepository;
	@Autowired
	private WorkBillRepository workBillRepository;
	@Autowired
	@Qualifier("jmsQueueTemplate")
	private JmsTemplate jmsTemplate;

	@Override
	public void order_save(Order order) {
		order.setOrderNum(UUID.randomUUID().toString());
		order.setOrderTime(new Date());
		order.setStatus("1");// 取单状态
		// TODO Auto-generated method stub
		
		// 自动分单逻辑，基于CRM地址库完全匹配，获取定区，匹配快递员
		String fixedAreaId = WebClient.create(Contants.CRM_MANAGEMENT_URL
				+"/crm_management/services/customerService/customer/findFixedAreaIdByAddress?address=" + order.getSendAddress())
				.accept(MediaType.APPLICATION_JSON).get(String.class);
		if (fixedAreaId != null) {
			// 根据定区id获得定区，再获得快递员
			FixedArea fixedArea = fixedAreaRepository.findOne(fixedAreaId);
			Courier courier = fixedArea.getCouriers().iterator().next();
			if (courier != null) {
				System.out.println("自动分单成功");
				saveOrder(order, courier);
				generateWorkBill(order);
				return;
			}
		}
		// 自动分单逻辑，通过省市区，查询分区关键字，匹配 地址 ，基于分区实现自动分单
		// 寄件人地区
		Area area = order.getSendArea();
		Area persistArea = areaRepository.findByProvinceAndCityAndDistrict(area.getProvince(), area.getCity(),
				area.getDistrict());
		// 收件人地区
		Area recArea = order.getRecArea();
		Area persistRecArea = areaRepository.findByProvinceAndCityAndDistrict(area.getProvince(), area.getCity(),
				area.getDistrict());

		order.setSendArea(persistArea);
		order.setRecArea(persistRecArea);
		// 遍历persistArea 区域，查找所有分区，，
		for (SubArea subArea : persistArea.getSubareas()) {
			// 当前客户下单地址是否包含分区的关键字
			// 包含就是找到了定区
			if (order.getSendAddress().contains(subArea.getKeyWords())||order.getSendAddress().contains(subArea.getAssistKeyWords())) {
				Iterator<Courier> itt = subArea.getFixedArea().getCouriers().iterator();
				if (itt.hasNext()) {
					Courier courier = itt.next();
					// 找到快递员
					if (courier != null) {
						System.out.println("自动分单成功");
						saveOrder(order, courier);
						// 生成工单发送短信
						generateWorkBill(order);
						return;
					}
				}
			}
		}
		//进入人工分单
		order.setOrderType("2");
		orderRepository.save(order);
	}

	/**
	 * 
	 * 保存订单的方法
	 * 
	 * @param order
	 * @param courier
	 */
	private void saveOrder(Order order, Courier courier) {
		order.setCourier(courier);
		order.setOrderType("1");
		orderRepository.save(order);
	}

	/**
	 * 
	 * 生成工单的方法，发送短信给快递员
	 * 
	 * @param order
	 *            订单
	 */
	private void generateWorkBill(final Order order) {
		WorkBill workBill = new WorkBill();
		workBill.setType("新");
		workBill.setPickstate("新单");
		workBill.setBuildtime(new Date());
		workBill.setRemark(order.getRemark());
		final String smsNumber = RandomStringUtils.randomNumeric(4);
		workBill.setSmsNumber(smsNumber);// 生成短信序号
		workBill.setOrder(order);
		workBill.setCourier(order.getCourier());
		workBillRepository.save(workBill);
		// 发送短信
/*
		jmsTemplate.send("bos_sms", new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				MapMessage mapMessage = session.createMapMessage();
				mapMessage.setString("telephone", order.getCourier().getTelephone());
				mapMessage.setString("msg", "短信序号：" + smsNumber + ",取件地址：" + order.getSendAddress() + ",联系人："
						+ order.getSendName() + "手机号" + order.getSendMobile());
				return mapMessage;
			}
		});*/
		workBill.setPickstate("已通知");

	}

	@Override
	public Order findByOrderNumber(String orderNum) {
		// TODO Auto-generated method stub
		return orderRepository.findByOrderNum(orderNum);
	}

}
