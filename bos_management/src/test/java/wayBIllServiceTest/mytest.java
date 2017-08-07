package wayBIllServiceTest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.itcast.bos.service.take_delivery.WayBillService;
@ContextConfiguration(locations="classpath:applicationContext.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class mytest {
	
	
	@Autowired
	private WayBillService wayBillService;
	
	@Test
	public void te(){
		wayBillService.syncIndex();
		
	}
	
}
