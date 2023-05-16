package com.api.bussiness.dao.base;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.api.bussiness.dao.table.base.MpBank;

@Repository
public interface BankDao {
	
	@Select("select * from mp_bank where code = #{code}")
	MpBank getBankKindByCode(@Param("code")String code);
	

}
