package cn.itcast.bos.dao.base;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.itcast.bos.domain.base.Standard;

public interface StandardRepository extends JpaRepository<Standard, Integer> {

	public List<Standard> findByName(String name);

	@Query(value = "from Standard where name= ?", nativeQuery = false)
	public List<Standard> findByqueryName(String name);

	@Query(value = "update Standard set minLength =?2 where id = ?1")
	@Modifying
	public void update(Integer id, Integer minLength);

	/*
	 * public List<Standard> findByNameLike(String name);
	 * 
	 * @Query(value="from Standard where name= ?" ,nativeQuery=false) public
	 * List<Standard> queryName(String name);
	 * 
	 * @Query("update Standard set minLength=?2 where id=?1")
	 * 
	 * @Modifying public void updateMinLength(Integer id,Integer minLength);
	 */

}
