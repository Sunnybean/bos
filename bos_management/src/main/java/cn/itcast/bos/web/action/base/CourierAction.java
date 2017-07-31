package cn.itcast.bos.web.action.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.service.base.CourierService;

@Actions
@Controller
@Scope("prototype")
@ParentPackage("json-default")
@Namespace("/")
public class CourierAction extends ActionSupport implements ModelDriven<Courier> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Courier courier = new Courier();

	@Autowired
	private CourierService courierService;

	private int page;
	private int rows;
	private String ids;
	

	public void setIds(String ids) {
		this.ids = ids;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	@Override
	public Courier getModel() {
		// TODO Auto-generated method stub
		return courier;
	}

	@Action(value = "courier_save", results = {
			@Result(name = "success", location = "./pages/base/courier.html", type = "redirect"),
			@Result(name = "input", location = "./pages/base/courier.html", type = "redirect") })
	public String save() {
		courierService.save(courier);
		return SUCCESS;
	}

	@Action(value = "courier_pageQuery", results = { @Result(name = "success", type = "json") })
	public String pageQuery() {
		// 构成查询条件，Specfication条件查询对象，类似于Hibernate、的QBC查询
		Specification<Courier> specfication = new Specification<Courier>() {

			@Override
			public Predicate toPredicate(Root<Courier> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				/**
				 * 构造条件查询方阿飞，如果方法返回null 代表无条件查询   Root代表 参数 获取表达式， name=？ age=？
				 * CriteriaQuery 参数，构造简单查询条件返回，提供where方法
				 * CriteriaBuilder参数，构造Predicate对象，条件对象构造复杂条件查询
				 */
				List<Predicate> list = new ArrayList<>();
				if (StringUtils.isNotBlank(courier.getCourierNum())) {
					Predicate p1 = cb.equal(root.get("courierNum").as(String.class), courier.getCourierNum());
					list.add(p1);
				}
				if (StringUtils.isNotBlank(courier.getCompany())) {
					Predicate p2 = cb.like(root.get("Company").as(String.class), "%" + courier.getCompany() + "%");
					list.add(p2);
				}
				if (StringUtils.isNotBlank(courier.getType())) {
					Predicate p3 = cb.equal(root.get("type").as(String.class), courier.getType());
					list.add(p3);
				}
				  Join<Object, Object> standardRoot = root.join("standard",JoinType.INNER);
				if (courier.getStandard()!=null&& StringUtils.isNotBlank(courier.getStandard().getName())) {
					Predicate p4 = cb.like(standardRoot.get("name").as(String.class), "%"+courier.getStandard().getName()+"%");
					list.add(p4);
				}
				return cb.and(list.toArray(new Predicate[0]));
			}
		};

		Pageable pageable = new PageRequest(page - 1, rows);
		Page<Courier> pageData = courierService.findPageData(specfication,pageable);
		Map<String, Object> result = new HashMap<>();
		result.put("total", pageData.getTotalElements());
		result.put("rows", pageData.getContent());
		ActionContext.getContext().getValueStack().push(result);
		return SUCCESS;
	}
	//作废快递员的方法
	@Action(value="courier_delBatch",results={@Result(name="success" ,type="redirect",location="./pages/base/courier.html")})
	public String delBatch(){
		String[] idArray = ids.split(",");
		courierService.delBatch(idArray);
		return SUCCESS;
	}
//	定区关联快递员的方法
	@Action(value="courier_findnoassociation",results={@Result(name="success",type="json")})
	public String courierFindnoassociation(){
       List<Courier> list =	 courierService.findNoAssociation();
		ActionContext.getContext().getValueStack().push(list);
		
		return SUCCESS;
	}
	
	
	
	
	
	
	

}
