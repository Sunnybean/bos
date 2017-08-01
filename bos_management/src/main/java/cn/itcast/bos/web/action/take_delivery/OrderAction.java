package cn.itcast.bos.web.action.take_delivery;

import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.itcast.bos.domain.take_delivery.Order;
import cn.itcast.bos.service.take_delivery.OrderService;
import cn.itcast.bos.web.action.common.BaseAction;

@Scope("prototype")
@Controller
@ParentPackage("json-default")
@Namespace("/")
public class OrderAction extends BaseAction<Order> {
	@Autowired
	private OrderService orderService;

	@Action(value = "order_findByOrderNumber", results = { @Result(name = "success", type = "json") })
	public String findByOrderNum() {
		Order order = orderService.findByOrderNumber(model.getOrderNum());
		Map<String, Object> result = new HashMap<>();
		if (order == null) {
			result.put("success", false);
		} else {
			if (order.getWayBill()!= null) {
				result.put("success", false);
				result.put("msg", "已经生成订单信息！");
			}else{
			result.put("success", true);
			result.put("orderData", order);
			}
		}
		ActionContext.getContext().getValueStack().push(result);
		return SUCCESS;
	}
}
