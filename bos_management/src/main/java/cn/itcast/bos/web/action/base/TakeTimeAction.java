package cn.itcast.bos.web.action.base;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.itcast.bos.domain.base.TakeTime;
import cn.itcast.bos.service.base.TakeTimeService;
import cn.itcast.bos.web.action.common.BaseAction;

@Scope("prototype")
@ParentPackage("json-default")
@Namespace("/")
@Controller
public class TakeTimeAction extends BaseAction<TakeTime>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Autowired
	private TakeTimeService takeTimeService;
	
	@Action(value="taketime_findAll", results={@Result(name="success",type="json")})
	public String findAll(){
		List<TakeTime> takeTimes = takeTimeService.findAll();
		ActionContext.getContext().getValueStack().push(takeTimes);
		return SUCCESS;
	}
 
	
}
