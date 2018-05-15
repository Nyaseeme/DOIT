package cn.itcast.service.impl;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.itcast.dao.ItemDao;
import cn.itcast.domain.SearchResult;
import cn.itcast.service.ItemService;

@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private ItemDao itemDao;

	@Override
	public SearchResult solrSearch(String queryString, String catalog_name, String price, Integer page, Integer rows,
			String sort) {
		SolrQuery solrQuery = new SolrQuery();
		
		if (queryString != null && queryString != "") {
			solrQuery.setQuery(queryString);
		} else {
			// 如果为空那么就要查询所有，因为该项是主查询，其余设置不具备查询能力
			solrQuery.setQuery("*:*");
		}
		
		// 分类过滤查询
		if (catalog_name != null && catalog_name != "") {
			solrQuery.addFilterQuery("product_catalog_name:" + catalog_name);
		}
		
		// 价格过滤查询
		if (price != null && !"".equals(price)) {
			String[] prices = price.split("-");
			solrQuery.addFilterQuery("product_price:[" + prices[0] + " TO " + prices[1] + "]");
		}
		
		// 设置排序
		if ("1".equals(sort)) {
			solrQuery.setSort("product_price", ORDER.asc);
		} else {
			solrQuery.setSort("product_price",ORDER.desc);
		}
		
		// 设置高亮
		// 开启高亮
		solrQuery.setHighlight(true);
		// 设置高亮字段
		solrQuery.addHighlightField("product_name");
		// 设置前缀
		solrQuery.setHighlightSimplePre("<font color='red'>");
		// 设置后缀
		solrQuery.setHighlightSimplePost("</font>");
		
		// 设置默认查询
		solrQuery.set("df", "product_keywords");
		
		// 设置分页查询
		int startNo = (page - 1)*rows; // 分页起始索引
		solrQuery.setStart(startNo);
		solrQuery.setRows(rows); // 每页显示条数
		
		SearchResult result = itemDao.solrSearch(solrQuery);
		
		result.setCurrPage(page);
		
		Integer totalCount = result.getTotalCount();
		double ceil = Math.ceil(totalCount*1.0/rows);
		int totalPages = (int) ceil;
		result.setTotalPages(totalPages);
		return result;
	}

}
