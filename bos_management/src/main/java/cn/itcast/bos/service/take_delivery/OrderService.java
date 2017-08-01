package cn.itcast.bos.service.take_delivery;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import cn.itcast.bos.domain.take_delivery.Order;

/**
 * webService 服务器端
 * @version 1.0
 * @author jhl
 * @description 订单服务接口	
 */
public interface OrderService {
	
	@Path("/order")
	@POST //保存
	@Consumes({"application/xml","application/json"})
	public void order_save(Order order);

	public Order findByOrderNumber(String orderNum);
}
