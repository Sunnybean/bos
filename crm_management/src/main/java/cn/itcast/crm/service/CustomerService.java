package cn.itcast.crm.service;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import cn.itcast.crm.domain.Customer;

public interface CustomerService {
	// 查询所有未关联客户列表
	@Path("/noassociationcustomers")
	@GET
	@Produces({"application/xml","applicaton/json"})
	public List<Customer> findNoAssociationCustomers();

	// 查询已经关联的客户列表
	@Path("/associationfixedareacustomers/{fiexdareaid}")
	@GET  //查询
	@Produces({"application/xml","applicaton/json"})
	public List<Customer> findHasAssociationFixedAreaCustomer(@PathParam("fixedareaid") String fixedAreaId);

	// 将客户关联到定区上
	@Path("/associationcustomertofixedarea/")
	@PUT //修改
	public void associationCustomerToFixedArea(
			@QueryParam("customerIdstr")  String customerIdstr, 
			@QueryParam("fixedAreaId")  String fixedAreaId);
}
