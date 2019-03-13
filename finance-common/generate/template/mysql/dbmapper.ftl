<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<!-- ${tableName}表 -->
<mapper namespace="${packageName}.dal.dao.${ClassName}Dao">
    
    <!--基本的sql查询字段 公共引用...-->
    <sql id="queryFields">
        <#list list as column><#if column.columnName == "id">${column.typeName} rid<#else><#if column.columnName == "isDelete"><#else>${column.typeName}</#if></#if><#if column.columnName == "isDelete"><#else><#if column_index+1 != listSize>,<#else> </#if></#if></#list>
    </sql>
    
    <!-- 基本的sql查询条件公共引用 -->
    <sql id="commonCondition">
    	WHERE is_delete = 0
    <#list list as column>
        <#if column.columnName == "id">
        <if test="rid !=null">
            AND id  = #${leftBraces}rid,jdbcType=${column.jdbcType}${rightBraces}
        </if>
        <#elseif column.columnName == "isDelete"><#elseif column.dataType == "Date">
        <if test="${column.columnName} !=null">
            AND ${column.typeName} = #${leftBraces}${column.columnName},jdbcType=${column.jdbcType}${rightBraces}
        </if>
        <#else >
        <if test="${column.columnName} != null">
            AND ${column.typeName} = #${leftBraces}${column.columnName},jdbcType=${column.jdbcType}${rightBraces}
        </if>
        </#if >
    </#list>
    </sql>

    
    <insert id="saveRecord" parameterType="${ClassName}Do" keyProperty="rid" useGeneratedKeys="true">
        INSERT INTO ${tableName}
        <trim prefix="(" suffix=")" suffixOverrides="," >
        <#list list as column>
	        <#if column.columnName == "isDelete">
	        <#elseif column.columnName != "id">
	        <if test="${column.columnName} != null">        
	            ${column.typeName},
	        </if>
	        </#if>
	     </#list>
        </trim>
        
        <trim prefix="values (" suffix=")" suffixOverrides="," >
      	<#list list as column>
	        <#if column.columnName == "isDelete">
	        <#elseif column.columnName != "id">
		    <if test="${column.columnName} != null" >
		       #${leftBraces}${column.columnName},jdbcType=${column.jdbcType}${rightBraces},
		    </if>
	        </#if >
        </#list>
        </trim>
    </insert>
    
    <update id="updateById"  parameterType="${ClassName}Do">
        UPDATE ${tableName}
          <set>
            <#list list as column>
            <#if column.columnName == "id">
            <#elseif column.columnName == "isDelete">
            <#elseif column.columnName == "gmtCreate">
            <#elseif column.columnName == "gmtModified">
            <#else >
            <if test="${column.columnName} != null">        
                ${column.typeName} = #${leftBraces}${column.columnName},jdbcType=${column.jdbcType}${rightBraces},
            </if>
            </#if>
            </#list>
            gmt_modified = NOW()
        </set>    
        WHERE is_delete = 0 AND id = #${leftBraces}rid ,jdbcType=BIGINT${rightBraces}
    </update>

	<select id="getById" resultType="${ClassName}Do">
        SELECT
        <include refid="queryFields" />
        FROM ${tableName}
        WHERE is_delete = 0 AND id=#${leftBraces}rid ,jdbcType=BIGINT${rightBraces}
        LIMIT 0,1
    </select>

	<select id="getByCommonCondition" resultType="${ClassName}Do" parameterType="${ClassName}Do">
        SELECT
        <include refid="queryFields" />
        FROM ${tableName}
        <include refid="commonCondition"/> 
        limit 0,1
    </select>
               
    <select id="getListByCommonCondition" resultType="${ClassName}Do" parameterType="${ClassName}Do">
        SELECT
        <include refid="queryFields" />
        FROM ${tableName}
        <include refid="commonCondition"/>
    </select>
    
</mapper>
