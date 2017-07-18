package cn.itcast.crm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.crm.dao.CustomerRepository;
import cn.itcast.crm.domain.Customer;
import cn.itcast.crm.service.CustomerService;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {
	
	@Autowired
	private CustomerRepository customerRepository;

	@Override
	public List<Customer> findNoAssociationCustomers() {
		// TODO Auto-generated method stub
		//就是定区id为空
		return customerRepository.findByFixedAreaIdIsNull();
	}

	@Override
	public List<Customer> findHasAssociationFixedAreaCustomer(String fixedAreaId) {
		// TODO Auto-generated method stub
		return customerRepository.findByFixedAreaId(fixedAreaId);
	}

	@Override
	public void associationCustomerToFixedArea(String customerIdstr, String fixedAreaId) {
		// TODO Auto-generated method stub
		String[] array = customerIdstr.split(",");
		for (String idStr : array) {
			Integer id = Integer.parseInt(idStr);
			customerRepository.updateFixedAreaId(fixedAreaId,id);
		}
		

	}

}
