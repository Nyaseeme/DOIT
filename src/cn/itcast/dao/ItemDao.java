package cn.itcast.dao;

import org.apache.solr.client.solrj.SolrQuery;

import cn.itcast.domain.SearchResult;

public interface ItemDao {

	// 查询索引库，得到Pojo分页对象
	public SearchResult solrSearch(SolrQuery solrQuery);
}
