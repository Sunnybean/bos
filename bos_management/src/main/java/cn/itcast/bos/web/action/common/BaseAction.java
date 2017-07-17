package cn.itcast.bos.web.action.common;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

public abstract class BaseAction<T> extends ActionSupport implements ModelDriven<T> {

	// 模型驱动
	protected T model;

	@Override
	public T getModel() {
		return model;
	}
	//构造器完成model实例化
	public BaseAction() {
		// 构造子类Action对象 ，获取继承父类型的泛型
				// AreaAction extends BaseAction<Area>
				// type ----BaseAction<Area>
		Type genericSuperclass = this.getClass().getGenericSuperclass();
		ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
		Class<T> modelClass = (Class<T>) parameterizedType.getActualTypeArguments()[0];

		try {
			model = modelClass.newInstance();
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	protected int page;
	protected int rows;

	public void setPage(int page) {
		this.page = page;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}
	// 将分页查询结果数据，压入值栈的方法
	protected void pushPageDataToValueStack(Page<T> pageData) {
		Map<String, Object> result = new HashMap<>();
		result.put("total", pageData.getTotalElements());
		result.put("rows", pageData.getContent());
		ActionContext.getContext().getValueStack().push(result);
	}

}
