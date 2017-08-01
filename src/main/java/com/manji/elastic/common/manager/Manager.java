package com.manji.elastic.common.manager;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.manji.elastic.common.mybatis.model.ModelExample;

public interface Manager<M , E extends ModelExample, ID> {

	int countByExample(E example);

	int deleteByExample(E example);

	int deleteByPrimaryKey(ID id);

	int insert(M model);

	//int insertSelective(M model);

	List<M> selectByExample(E example);

	M selectByPrimaryKey(ID id);

	M selectOneByExample(E example);

	Page<M> selectPageByExample(Pageable pageable, E example);

	Page<M> selectPageByExample(Pageable pageable,long rows,E example);

	int updateByExampleSelective(M model, E example);

	//int updateByExample(M model, E example);

	int updateByPrimaryKeySelective(M model);

	//int updateByPrimaryKey(M model);

}
