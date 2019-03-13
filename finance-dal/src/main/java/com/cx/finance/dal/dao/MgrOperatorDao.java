package com.cx.finance.dal.dao;

import java.util.List;

import com.cx.finance.dal.domain.MgrOperatorDto;
import org.apache.ibatis.annotations.Param;

import com.cx.finance.dal.domain.MgrOperatorDo;
import com.cx.finance.dal.query.OperatorQuery;

/**
 * Dao
 * 
 * @author CodeGenerate
 * @version 1.0.0 初始化
 * @date 2018-09-17 15:22:24
 * Copyright 本内容仅限于杭州阿拉丁信息科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface MgrOperatorDao extends BaseDao<MgrOperatorDo, Long> {

    MgrOperatorDo getByUsername(@Param("username") String username);

    String getMenuIdsByOperatorId(Long operatorId);

    String getDivMenusIdByOperatorId(@Param("operatorId")Long operatorId,@Param("menuUrl")String menuUrl);


    List<MgrOperatorDto> pageQuery(OperatorQuery query);

    void clearMenus(Long roleId);
}
