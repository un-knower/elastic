package com.manji.elastic.dal.model.page;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.manji.elastic.common.manager.Manager;
import com.manji.elastic.common.mybatis.model.ModelExample;

/**
 * Created by xsm on 2014/12/16.
 */
public class Pagination<M, E extends ModelExample, ID> {

	/**
	 * 每次请求的request
	 */
	private HttpServletRequest request;

	/**
	 * 起始值
	 */
	private Integer start = 0;

	/**
	 * 当前页
	 */
	private Integer currentPage;

	/**
	 * 限制数(每页多少条)
	 */
	private Integer limit = 20;

	/**
	 * 总行数
	 */
	private Long rows;

	/**
	 * 总页数
	 */
	private Integer totalPages;

	/**
	 * 分页后的集合
	 */
	private List<M> list;

	public Pagination() {
		this.request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		if (currentPage == null) {
			currentPage = 1;
		}
		setStart(limit * (currentPage - 1));
		this.currentPage = currentPage;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		if (limit == null) {
			return;
		}
		if (currentPage != null) {
			setStart(limit * (currentPage - 1));
		}

		this.limit = limit;
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Long getRows() {
		return rows;
	}

	public void setRows(Long rows) {
		if (getRows() == null) {
			if (rows == null) {
				rows = 0L;
			}
			setTotalPages(rows % limit == 0 ? (int) (rows / limit) : (int) ((rows / limit) + 1));
			this.rows = rows;
		}
	}

	public Integer getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}

	public List<M> getList() {
		return list;
	}

	public void setList(List<M> list) {
		this.list = list;
	}

	public void setAttributeAll() {
		if (request == null)
			return;
		Iterator<Map.Entry<String, String[]>> iterator = request.getParameterMap().entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, String[]> entry = iterator.next();
			request.setAttribute(entry.getKey(), request.getParameter(entry.getKey()));
		}
		request.setAttribute("pagination", this);
	}

	public void setAttributeAll(List<M> list) {
		setList(list);
		setAttributeAll();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Page<M> page() {
		return new PageImpl(list, pageable(), getRows());
	}

	public Pageable pageable() {
		if (getCurrentPage() == null) {
			return new PageRequest(getStart() / getLimit(), getLimit());
		}
		return new PageRequest(getCurrentPage() - 1, getLimit());
	}

	public void setManager(Manager<M, E, ID> manager, E example) {
		this.rows = (long) manager.countByExample(example);
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
