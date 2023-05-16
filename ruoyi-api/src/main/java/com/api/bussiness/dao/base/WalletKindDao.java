package com.api.bussiness.dao.base;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.api.bussiness.dao.table.base.MpWalletKind;

import org.apache.ibatis.annotations.Param;

@Repository
public interface WalletKindDao {
	
	@Select("select * from mp_wallet_kind where id = #{id}")
	MpWalletKind getWalletKindById(@Param("id")long id);
	
	@Select("select * from mp_wallet_kind where code = #{code}")
	MpWalletKind getWalletKindByCode(@Param("code")String code);
	
	

}
