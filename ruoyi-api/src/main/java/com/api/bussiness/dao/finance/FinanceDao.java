package com.api.bussiness.dao.finance;

import java.math.BigDecimal;

import com.api.bussiness.dao.table.finance.FinancialSetting;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import org.springframework.stereotype.Repository;

@Repository
public interface FinanceDao {
	
	
	/**
	 * 商户支付和代付、渠道支付产生的利润
	 * @param account_balance
	 * @param channel_id
	 * @param
	 * @return
	 */
	@Update("update mp_financial_wallet set account_balance = account_balance + #{account_balance} where channel_id = #{channel_id}")
	int updateFinance(@Param("account_balance")BigDecimal account_balance,@Param("channel_id")long channel_id);
	
	/**
	 * 平台代付自己失败时调用
	 * @param
	 * @param channel_id
	 * @return
	 */
	@Update("update mp_financial_wallet set frozen_balance = frozen_balance - #{frozen_balance} where channel_id = #{channel_id}")
	int updateFinanceWhenPlatCallBackFail(@Param("frozen_balance")BigDecimal frozen_balance,@Param("channel_id")long channel_id);
	

	/**
	 * 平台代付自己成功时调用
	 * @param frozen_balance
	 * @param channel_id
	 * @return
	 */
	@Update("update mp_financial_wallet set account_balance = account_balance - #{real_fee}"
			+ ",frozen_balance = frozen_balance - #{frozen_balance} where channel_id = #{channel_id}")
	int updateFinanceByChannelIdWhenPlatformWithdrewSelf(@Param("frozen_balance")BigDecimal frozen_balance,@Param("real_fee")BigDecimal real_fee,@Param("channel_id")long channel_id);
	
	@Select("select * from mp_financial_setting limit 0,1")
    FinancialSetting getFinancialSetting();

}
