package cn.itcast.bos.web.service.take_delivery;



import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.itcast.bos.domain.page.PageBean;
import cn.itcast.bos.domain.take_delivery.Promotion;

public interface PromotionService {

	void save(Promotion model);

	Page<Promotion> findData(Pageable pageable);
	
	@Path("/pageQuery")
	@GET
	@Produces({"application/xml","application/json"})
	PageBean<Promotion> findPageData(@QueryParam("page") int page,
			@QueryParam("rows") int rows);
	

}
