package cn.itcast.bos.web.action.base;

import java.util.HashMap;

import java.util.List;
import java.util.Map;

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
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import cn.itcast.bos.domain.base.Standard;
import cn.itcast.bos.service.base.StandardService;

@Actions
@Scope("prototype")
@Controller
@Namespace("/")
@ParentPackage("json-default")
public class StandardAction extends ActionSupport implements ModelDriven<Standard> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Standard standard = new Standard();
	private int page;
	private int rows;

	public void setPage(int page) {
		this.page = page;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	@Autowired
	private StandardService standardService;

	@Override
	public Standard getModel() {
		// TODO Auto-generated method stub
		return standard;
	}

	// 添加
	@Action(value = "standard_save", results = {
			@Result(name = "success", location = "./pages/base/standard.html") })
	public String save() {
		standardService.save(standard);
		System.out.println("添加收派标准");
		return SUCCESS;
	}

	// 分页查询
	@Action(value = "standard_pageQuery", results = { @Result(name = "success", type = "json") })
	public String pageQuery() {
		Pageable pageable = new PageRequest(page - 1, rows);
		Page<Standard> pageData = standardService.findPageData(pageable);
		
		Map<String, Object> result = new HashMap<>();
		result.put("total", pageData.getTotalElements());
		result.put("rows", pageData.getContent());
		ActionContext.getContext().getValueStack().push(result);
		return SUCCESS;
	}

	// 查询所有
	@Action(value = "standard_findAll", results = { @Result(name = "success", type = "json") })
	public String findAll() {
		List<Standard> standards = standardService.findAll();
		ActionContext.getContext().getValueStack().push(standards);
		return SUCCESS;
	}
	
}

