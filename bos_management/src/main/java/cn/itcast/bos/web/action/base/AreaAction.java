package cn.itcast.bos.web.action.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
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

import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.service.base.AreaService;
import cn.itcast.bos.utils.PinYin4jUtils;
import cn.itcast.bos.web.action.common.BaseAction;

@Controller
@Scope("prototype")
@ParentPackage("json-default")
@Namespace("/")
public class AreaAction extends BaseAction<Area> {
	private File file;

	public void setFile(File file) {
		this.file = file;
	}

	@Autowired
	private AreaService areaService;

	// 文件导入方法
	@Action(value = "area_batchImport")
	public String batchImport() throws IOException {
		List<Area> areas = new ArrayList<>();
		// 加载excel对象
		@SuppressWarnings("resource")
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(new FileInputStream(file));
		// 读入每个sheet
		HSSFSheet sheet = hssfWorkbook.getSheetAt(0);
		for (Row row : sheet) {
			if (row.getRowNum() == 0) {
				continue;
			}
			if (row.getCell(0) == null || StringUtils.isBlank(row.getCell(0).getStringCellValue())) {
				continue;
			}
			Area area = new Area();
			area.setId(row.getCell(0).getStringCellValue());
			area.setProvince(row.getCell(1).getStringCellValue());
			area.setCity(row.getCell(2).getStringCellValue());
			area.setDistrict(row.getCell(3).getStringCellValue());
			area.setPostcode(row.getCell(4).getStringCellValue());
			
			// 基于pinyin4j生成城市编码和简码
			String province = area.getProvince();
			String city = area.getCity();
			String district = area.getDistrict();
			province = province.substring(0, province.length() - 1);
			city = city.substring(0, city.length() - 1);
			district = district.substring(0, district.length() - 1);
			// 简码
			String[] headArray = PinYin4jUtils.getHeadByString(province + city
					+ district);
			StringBuffer buffer = new StringBuffer();
			for (String headStr : headArray) {
				buffer.append(headStr);
			}
			String shortcode = buffer.toString();
			area.setShortcode(shortcode);
			// 城市编码
			String citycode = PinYin4jUtils.hanziToPinyin(city, "");
			area.setCitycode(citycode);
			areas.add(area);
		}
	
		areaService.saveBatch(areas);

		return NONE;
	}

	// 分页查询所有的方法
	@Action(value = "area_pageQuery",results={@Result(name="success",type="json")})
	public String pageQuery() {
		Pageable pageable = new PageRequest(page - 1, rows);
		// Sepcification条件查询
		Specification<Area> sepcification = new Specification<Area>() {

			@Override
			public Predicate toPredicate(Root<Area> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				List<Predicate> list = new ArrayList<>();
				if (StringUtils.isNotBlank(model.getProvince())) {
					Predicate p1 = cb.like(root.get("province").as(String.class), "%" + model.getProvince() + "%");
					list.add(p1);
				}
				if (StringUtils.isNotBlank(model.getCity())) {
					Predicate p2 = cb.like(root.get("city").as(String.class), "%"+model.getCity()+"%");
					list.add(p2);
				}
				if (StringUtils.isNotBlank(model.getDistrict())) {
					Predicate p3 = cb.like(root.get("district").as(String.class), "%"+model.getDistrict()+"%");
					list.add(p3);
				}
				return cb.and(list.toArray(new Predicate[0]));
			}
		};

		Page<Area> pageData = areaService.findByPage(sepcification,pageable);
		pushPageDataToValueStack(pageData);
		return SUCCESS;
	}

}
