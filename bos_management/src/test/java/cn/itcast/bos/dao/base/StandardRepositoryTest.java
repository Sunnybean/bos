package cn.itcast.bos.dao.base;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.domain.base.Standard;
@ContextConfiguration(locations="classpath:applicationContext.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class StandardRepositoryTest {
	@Autowired
	private StandardRepository standardRepository;
	@Test
	@Transactional
	@Rollback(false)
	public void test11(){
		/*System.out.println(standardRepository.findByName("10-20"));
		System.out.println(standardRepository.findByqueryName("33"));*/
		standardRepository.update(2,963);
	}

}
