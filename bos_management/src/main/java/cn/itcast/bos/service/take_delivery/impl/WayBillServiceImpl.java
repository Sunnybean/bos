package cn.itcast.bos.service.take_delivery.impl;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.index.query.QueryStringQueryBuilder.Operator;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.query.WildcardQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.take_delivery.WayBillRepository;
import cn.itcast.bos.domain.take_delivery.WayBill;
import cn.itcast.bos.index.WayBillIndexRepository;
import cn.itcast.bos.service.take_delivery.WayBillService;

@Service
@Transactional
public class WayBillServiceImpl implements WayBillService {
	
	@Autowired
	private WayBillRepository wayBillRepository;
	
	@Autowired
	private WayBillIndexRepository wayBillIndexRepository;
	@Override
	public void save(WayBill model) {
		// TODO Auto-generated method stub
		
		//BUG,数据库认为是插入操作，回显后再保存id冲突，
		WayBill persistWayBill = wayBillRepository.findByWayBillNum(model.getWayBillNum());
		if (persistWayBill ==null ||persistWayBill.getId() == null ) {
			wayBillRepository.save(model);
			wayBillIndexRepository.save(model);
		}else{
			try {
				//查出来原来的id，单独保存
				Integer id =persistWayBill.getId();
				//复制内容到原来的，变相的更新，再把id改回来
				BeanUtils.copyProperties(persistWayBill, model);
				persistWayBill.setId(id);
				wayBillIndexRepository.save(model);
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	
	}
	@Override
	public Page<WayBill> findByPage(WayBill wayBill,Pageable pageable) {
		//判断waybill中条件是否存在  
		if(StringUtils.isBlank(wayBill.getWayBillNum())	
			&&StringUtils.isBlank(wayBill.getRecAddress())
			&&StringUtils.isBlank(wayBill.getSendAddress())
			&&StringUtils.isBlank(wayBill.getSendProNum())
			&&(wayBill.getSignStatus() == null || wayBill.getSignStatus()==0)){
			return wayBillIndexRepository.findAll(pageable);
		}else{
			//布尔查询，多条件查询
			BoolQueryBuilder query = new BoolQueryBuilder(); 
			//运单号查询
			if (StringUtils.isNoneBlank(wayBill.getWayBillNum())) {
				QueryBuilder temQuery = new TermQueryBuilder("wayBillNum",wayBill.getWayBillNum());
				query.must(temQuery);
			}
			//发货地，模糊查询
			if (StringUtils.isNoneBlank(wayBill.getSendAddress())) {
				//情况一，输入北，查询词条的一部分，使用模糊条件匹配查询
				QueryBuilder wildcardQuery = new WildcardQueryBuilder("sendAddress","*"+wayBill.getSendAddress()+"*");
				//情况二，输入北京市海淀区   是多个词条的组合，进行分词后，没个词条 匹配查询
				QueryBuilder queryStringQueryBuilder = new QueryStringQueryBuilder
						(wayBill.getSendAddress()).field("sendAddress").defaultOperator(Operator.AND);
				BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
				boolQueryBuilder.should(wildcardQuery);
				boolQueryBuilder.should(queryStringQueryBuilder);
				query.must(boolQueryBuilder);
			}
			//收货地，模糊查询
			if (StringUtils.isNoneBlank(wayBill.getRecAddress())) {
				QueryBuilder wildcardQuery = new WildcardQueryBuilder("recAddress","*"+wayBill.getRecAddress()+"*");
				query.must(wildcardQuery);
			}
			//速运类型 ，等值查询
			if (StringUtils.isNoneBlank(wayBill.getSendProNum())) {
				QueryBuilder  termQuery = new TermQueryBuilder("sendProNum",wayBill.getSendProNum());
				query.must(termQuery);
			}
			SearchQuery searchQuery = new NativeSearchQuery(query);
			searchQuery.setPageable(pageable);
			//有条件的查询，查询索引库
			return wayBillIndexRepository.search(searchQuery);
			
		}
	}
	@Override
	public WayBill findByWayBillNum(String wayBillNum) {
		// TODO Auto-generated method stub
		return wayBillRepository.findByWayBillNum(wayBillNum);
	}
	

}
