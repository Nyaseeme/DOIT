package cn.itcast.service;

import cn.itcast.domain.SearchResult;

public interface ItemService {

	public SearchResult solrSearch(String queryString,String catalog_name,String price,Integer page,Integer rows,String sort);
}
