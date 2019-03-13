package com.cx.finance.dal.dao;

import com.cx.finance.dal.domain.FinResourceDo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Dao
 * 
 * @author CodeGenerate
 * @version 1.0.0 初始化
 * @date 2018-11-29 13:37:52
 * Copyright 本内容仅限于杭州阿拉丁信息科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface FinResourceDao extends BaseDao<FinResourceDo, Long> {

    List<FinResourceDo> getFinResourceListByTypeAndSecType(@Param("type") String type, @Param("SecType")String SecType);

    FinResourceDo getFinResourceListByTypeAndSecTypeAndValue(@Param("type") String type,@Param("SecType")String SecType, @Param("value") String value);
}
