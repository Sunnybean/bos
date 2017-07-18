package cn.itcast.crm.service;

import static org.junit.Assert.*;

import org.hibernate.engine.transaction.jta.platform.internal.SynchronizationRegistryBasedSynchronizationStrategy;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration(locations="classpath:applicationContext.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class CustomerServiceTest {
	
	@Autowired
	private CustomerService custoemrService;

	@Test
	public void testFindNoAssociationCustomers() {
		System.out.println(custoemrService.findNoAssociationCustomers());
	}

	@Test
	public void testFindHasAssociationFixedAreaCustomer() {
		System.out.println(custoemrService.findHasAssociationFixedAreaCustomer("dq001"));
	}

	@Test
	public void testAssociationCustomerToFixedArea() {
		custoemrService.associationCustomerToFixedArea("1,2", "dq001");
	}

}
