package cn.itcast.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.itcast.dao.ItemDao;
import cn.itcast.domain.Product;
import cn.itcast.domain.SearchResult;
@Repository
public class ItemDaoImpl implements ItemDao {

	@Autowired
	private SolrServer solrServer;
	// 链接地址：http://localhost:8983/solr
	
	@Override
	public SearchResult solrSearch(SolrQuery solrQuery) {
		SearchResult result = new SearchResult();
		List<Product> productList = new ArrayList<>();
		try {
			QueryResponse response = solrServer.query(solrQuery);
			SolrDocumentList results = response.getResults();
			// 获取总记录数
			Long numFound = results.getNumFound();
			result.setTotalCount(numFound.intValue());
			
			// 获取产品信息并封装
			for (SolrDocument sDoc : results) {
				Product pro = new Product();
				
				String pid = (String) sDoc.get("id");
				pro.setPid(pid);
				
				String product_name = (String) sDoc.get("product_name");
				
				Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
				if (highlighting != null && highlighting.size() > 0) {
					Map<String, List<String>> map = highlighting.get(pid);
					List<String> list = map.get("product_name");
					if (list != null && list.size() > 0) {
						product_name = list.get(0);
					}
				}
				pro.setName(product_name);
				
				String product_picture = (String)sDoc.get("product_picture");
				pro.setPicture(product_picture);
				
				Float product_price = (Float) sDoc.get("product_price");
				pro.setPrice(product_price);
				
				productList.add(pro);
			}
			result.setProductList(productList);
			
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}

}
