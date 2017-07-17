package cn.itcast.bos.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.itcast.bos.domain.base.Area;

//<>内为domain类和主键类型
public interface AreaRepository extends JpaRepository<Area, String>, JpaSpecificationExecutor<Area> {

}
