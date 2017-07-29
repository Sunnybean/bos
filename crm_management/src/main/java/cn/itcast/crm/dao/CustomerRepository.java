package cn.itcast.crm.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.itcast.crm.domain.Customer;

public interface CustomerRepository  extends JpaRepository<Customer,Integer>{
	
	public List<Customer> findByFixedAreaIdIsNull();
	
	public List<Customer> findByFixedAreaId( String fixedAreaId);

	
	@Query("update Customer set fixedAreaId =? where id= ?")
	@Modifying
	public void updateFixedAreaId(String fixedAreaId, Integer id);

	@Query("update Customer set fixedAreaId = null where fixedAreaId =?")
	@Modifying //修改数据库的时候一定要加
	public void clearFixedAreaId(String fixedAreaId);

	public Customer findByTelephoneAndPassword(String telephone, String password);

	//根据地址查询定区编号的方法
	@Query("select fixedAreaId from Customer where address = ?")
	public String findFixedAreaIdByAddress(String address);

}
