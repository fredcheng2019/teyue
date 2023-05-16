package com.api.bussiness.dao.wallet;

import java.math.BigDecimal;

import com.api.bussiness.dao.table.merchant.FinancialWallet;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;


@Repository
public interface FinancialWalletDao {
	
	/**
	 * 根据钱包id获取钱够的钱包
	 * @param id
	 * @return
	 */
	@Select("select * from mp_financial_wallet where id = #{id} and and (account_balance - frozen_balance) >= #{total_fee}")
    FinancialWallet getWalletByIdAndEnoughMoney(@Param("id")long id, @Param("total_fee")BigDecimal total_fee);
	
	/**
	 * 根据渠道id获取钱够的钱包
	 * @param id
	 * @return
	 */
	@Select("select * from mp_financial_wallet where channel_id = #{channel_id} and (account_balance - frozen_balance) >= #{total_fee}")
	FinancialWallet getWalletByChIdAndEnoughMoney(@Param("channel_id")long channel_id,@Param("total_fee")BigDecimal total_fee);
	
	
	/**
	 * 根据渠道id冻结平台钱包金额
	 * @param account_balance
	 * @param frozen_balance
	 * @param id
	 * @return
	 */
	@Update("update mp_financial_wallet set frozen_balance = frozen_balance + ${frozen_balance}"
			+ " where channel_id = #{channel_id}")
	int frozenBalanceByChId(@Param("frozen_balance")BigDecimal frozen_balance,@Param("channel_id")long channel_id);
	
	/**
	 * 根据渠道id解冻
	 * @param frozen_balance
	 * @param id
	 * @return
	 */
	@Update("update mp_financial_wallet set account_balance = account_balance + #{frozen_balance},"
			+ "frozen_balance = frozen_balance - #{frozen_balance}"
			+ "where channel_id = #{channel_id}")
	int recoveryBalanceById(@Param("frozen_balance")BigDecimal frozen_balance,@Param("channel_id")long channel_id);

}
