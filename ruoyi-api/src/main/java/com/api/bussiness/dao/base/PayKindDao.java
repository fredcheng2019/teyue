package com.api.bussiness.dao.base;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.api.bussiness.dao.table.base.MpPayKind;

import org.apache.ibatis.annotations.Param;

@Repository
public interface PayKindDao {
	
	@Select("select * from mp_pay_kind where code = #{code}")
	MpPayKind getPayKindByCode(@Param("code")String code);
	

}
