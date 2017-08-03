package cn.itcast.bos.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.itcast.bos.domain.base.Courier;

//<>内为domain类和主键类型
public interface CourierRepository extends JpaRepository<Courier, Integer>, JpaSpecificationExecutor<Courier> {

	@Modifying
	@Query(value = "update Courier set deltag =1 where id =?")
	 public void updateDelTag(Integer id);

}
