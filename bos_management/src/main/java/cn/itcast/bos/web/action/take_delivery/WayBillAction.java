package cn.itcast.bos.web.action.take_delivery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.itcast.bos.domain.take_delivery.WayBill;
import cn.itcast.bos.service.take_delivery.WayBillService;
import cn.itcast.bos.web.action.common.BaseAction;

@Scope("prototype")
@Controller
@ParentPackage("json-default")
@Namespace("/")
public class WayBillAction extends BaseAction<WayBill> {
	private static final Logger LOGGER = Logger.getLogger(WayBillAction.class);

	@Autowired
	private WayBillService wayBillService;

	/**
	 * 保存运单的方法
	 * 
	 * @return
	 */

	@Action(value = "waybill_save", results = { @Result(name = "success", type = "json") })
	public String saveWayBill() {
		// 去除没有id的Order对象
		if (model.getOrder() != null && (model.getOrder().getId() == null || model.getOrder().getId() == 0)) {
			model.setOrder(null);
		}

		Map<String, Object> result = new HashMap<>();
		try {
			// 保存成功
			wayBillService.save(model);
			result.put("success", true);
			result.put("msg", "保存运单成功！！");
			LOGGER.info("运单保存成功" + model.getWayBillNum());
		} catch (Exception e) {
			// 保存失败了
			e.printStackTrace();
			result.put("success", false);
			result.put("msg", "保存运单失败了！！");
			LOGGER.error("运单失败" + model.getWayBillNum());
		}
		// 将结果压入值栈
		ActionContext.getContext().getValueStack().push(result);
		return SUCCESS;
	}

	/**
	 * 分页查询的方法
	 * 
	 * @return
	 */
	@Action(value = "waybill_pageQuery", results = { @Result(name = "success", type = "json") })
	public String page_Query() {
		Pageable pageable = new PageRequest(page - 1, rows, new Sort(new Sort.Order(Sort.Direction.DESC, "id")));
		
		 	/*Specification specifiction = new  Specification<WayBill>() {

				@Override
				public Predicate toPredicate(Root<WayBill> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
					// TODO Auto-generated method stub
					List<Predicate> list = new ArrayList<>();
					if (StringUtils.isNotBlank(model.getId())) {
						Predicate p1 = cb.equal(root.get("id").as(Integer.class), model.getId());
						list.add(p1);
					}
					return cb.and(list.toArray(new Predicate[0]));
				}
			};*/
		
		
		Page<WayBill> pageData = wayBillService.findByPage(model,pageable);
		pushPageDataToValueStack(pageData);
		return SUCCESS;
	}

	/**
	 * 通用过运单号查找运单的方法
	 * 
	 * @return
	 */
	@Action(value = "waybill_findByWayBillNum", results = { @Result(name = "success", type = "json") })
	public String findByWayBillNum() {
		Map<String, Object> result = new HashMap<>();
		WayBill wayBill = wayBillService.findByWayBillNum(model.getWayBillNum());
		if (wayBill == null) {
			result.put("success", false);
		} else {
			result.put("success", true);
			result.put("wayBillData", wayBill);
		}
		ActionContext.getContext().getValueStack().push(result);

		return SUCCESS;
	}

}
