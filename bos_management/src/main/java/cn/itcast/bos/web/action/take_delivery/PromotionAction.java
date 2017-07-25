package cn.itcast.bos.web.action.take_delivery;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
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

import cn.itcast.bos.domain.take_delivery.Promotion;
import cn.itcast.bos.web.action.common.BaseAction;
import cn.itcast.bos.web.service.take_delivery.PromotionService;

@Scope("prototype")
@Controller
@ParentPackage("json-default")
@Namespace("/")
public class PromotionAction extends BaseAction<Promotion> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2350583251606040826L;
	private File titleImgFile;
	private String titleImgFileFileName;

	public void setTitleImgFileFileName(String titleImgFileFileName) {
		this.titleImgFileFileName = titleImgFileFileName;
	}

	public void setTitleImgFile(File titleImgFile) {
		this.titleImgFile = titleImgFile;
	}

	@Autowired
	private PromotionService promotionService;

	@Action(value = "promotion_save", results = {
			@Result(name = "success", type = "redirect", location = "./pages/take_delivery/promotion.html") })
	public String save() throws IOException {
		// 宣传图上传保存路径 ，，，在数据 表保存宣传图路径
		String savePath = ServletActionContext.getServletContext().getRealPath("/upload/");
		String saveUrl = ServletActionContext.getRequest().getContextPath() + "/upload/";
		System.out.println(savePath);
		System.out.println(saveUrl);

		// 生成随机图片名
		UUID uuid = UUID.randomUUID();
		String ext = titleImgFileFileName.substring(titleImgFileFileName.lastIndexOf("."));
		String randomFileName = uuid + ext;
		// 保存图片，，，绝对路径
		File destFile = new File(savePath + "/" + randomFileName);
		FileUtils.copyFile(titleImgFile, destFile);

		model.setTitleImg(ServletActionContext.getRequest().getContextPath() + "/upload/" + randomFileName);

		promotionService.save(model);
		return SUCCESS;
	}

	// 宣传活动的分页查询方法
	@Action(value = "promotion_pageQuery", results = { @Result(name = "success", type = "json") })
	public String pageQuery() {
		

		Pageable pageable = new PageRequest(page - 1, rows);
		
		//利用webService
		

		// 条件查询
		@SuppressWarnings("unused")
		Specification<Promotion> specification = new Specification<Promotion>() {

			@Override
			public Predicate toPredicate(Root<Promotion> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				List<Predicate> list = new ArrayList<Predicate>();

				if (StringUtils.isNotBlank(model.getDescription())) {
					Predicate p1 = cb.like(root.get("description").as(String.class), model.getDescription());
					list.add(p1);
				}

				return cb.and(list.toArray(new Predicate[0]));
			}
		};

		Page<Promotion> pageData = promotionService.findData(pageable);
		pushPageDataToValueStack(pageData);
		return SUCCESS;
	}

}
