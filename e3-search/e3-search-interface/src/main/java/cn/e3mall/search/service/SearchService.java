package cn.e3mall.search.service;

import cn.e3mall.common.pojo.SearchResult;

public interface SearchService {

	public SearchResult search(String keyword, Integer page, Integer rows) throws Exception;
}