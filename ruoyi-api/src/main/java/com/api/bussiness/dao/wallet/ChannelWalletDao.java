package com.api.bussiness.dao.wallet;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.api.bussiness.dao.table.channel.MpChannelWallet;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;


@Repository
public interface ChannelWalletDao {

	/**
	 * 根据渠道id获取渠道钱包
	 * @param channel_id
	 * @param total_fee
	 * @return
	 */
	@Select("select * from mp_channel_wallet where channel_id = #{channel_id} "
			+ "and (account_balance - wait_liquidated_balance) >= #{total_fee}")
    MpChannelWallet getChannelWalletByChannelIdAndEnoughMoney(@Param("channel_id")long channel_id, @Param("total_fee")BigDecimal total_fee);
	
	/**
	 * 根据id获取渠道钱包且要钱够
	 * @param id
	 * @param total_fee
	 * @return
	 */
	@Select("select * from mp_channel_wallet where id = #{id} and (account_balance - wait_liquidated_balance) >= #{total_fee}")
	MpChannelWallet getWalletByIdAndEnoughMoney(@Param("id")long id,@Param("total_fee")BigDecimal total_fee);
	
	/**
	 * 根据渠道id获取渠道钱包
	 * @param channel_id
	 * @return
	 */
	@Select("select * from mp_channel_wallet where channel_id = #{channel_id}")
	MpChannelWallet getWalletByChannelId(@Param("channel_id")long channel_id);
	
	/**
	 * 根据商户id获取钱够的钱包
	 * @param channel_id
	 * @param total_fee
	 * @return
	 */
	@Select("select * from mp_channel_wallet where channel_id = #{channel_id} and (account_balance - wait_liquidated_balance) >= #{total_fee} for update")
	MpChannelWallet getWalletByMchIdAndEnoughMoney(@Param("channel_id")long channel_id,@Param("total_fee")BigDecimal total_fee);
	
	/**
	 * 获取钱最多的渠道
	 * @return
	 */
	@Select("select * from mp_channel_wallet where (account_balance - wait_liquidated_balance) = "
			+ "(select (account_balance - wait_liquidated_balance) as balance from mp_channel_wallet GROUP BY balance ORDER BY balance desc limit 0,1)")
	List<MpChannelWallet> getMostMoneyMpChannelWallets();
	
	
	/**
	 * 根据渠道id获取钱最多的渠道
	 * @param channel_id
	 * @return
	 */
	@Select("select * from mp_channel_wallet where channel_id in (${channel_ids}) and (account_balance - wait_liquidated_balance) = "
			+ "(select (account_balance - wait_liquidated_balance) as balance from mp_channel_wallet where channel_id in (${channel_ids}) and (account_balance - wait_liquidated_balance) > #{total_fee} GROUP BY balance ORDER BY balance desc limit 0,1)")
	List<MpChannelWallet> getMostMoneyMpChannelWalletsByChannelIds(@Param("channel_ids")String channel_ids,@Param("total_fee")BigDecimal total_fee);
	
	
	@Select("select * from mp_channel_wallet where channel_id = #{channel_id} for update")
	MpChannelWallet getMpChannelWalletByChannelIdForUpdate(@Param("channel_id")long channel_id);
	
	/**
	 * 更新查询余额时间
	 * @param latest_check_time
	 * @return
	 */
	@Update("update mp_channel_wallet set latest_check_time = #{latest_check_time} where channel_id = #{channel_id}")
	int updateCheckDateByChannelId(@Param("latest_check_time")Date latest_check_time,@Param("channel_id")long channel_id);

	/**
	 * 更新余额和查询余额时间
	 * @param latest_check_time
	 * @return
	 */
	@Update("update mp_channel_wallet set account_balance = #{account_balance}, latest_check_time = #{latest_check_time} where channel_id = #{channel_id}")
	int updateCheckDateAndBalanceByChannelId(@Param("account_balance")BigDecimal account_balance, @Param("latest_check_time")Date latest_check_time,@Param("channel_id")long channel_id);
	
	/**
	 * 冻结金额
	 * @param account_balance
	 * @param wait_liquidated_balance
	 * @param id
	 * @return
	 */
	@Update("update mp_channel_wallet set wait_liquidated_balance = wait_liquidated_balance + ${wait_liquidated_balance}"
			+ " where id = #{id}")
	int frozenBalanceById(@Param("wait_liquidated_balance")BigDecimal wait_liquidated_balance,@Param("id")long id);
	
	/**
	 * 根据id解冻
	 * @param wait_liquidated_balance
	 * @param id
	 * @return
	 */
	@Update("update mp_channel_wallet set wait_liquidated_balance = wait_liquidated_balance - #{wait_liquidated_balance}"
			+ "where id = #{id}")
	int recoveryBalanceById(@Param("wait_liquidated_balance")BigDecimal wait_liquidated_balance,@Param("id")long id);
	
	/**
	 * 代付成功，清除冻结金额,扣除金额
	 * @param wait_liquidated_balance
	 * @param channel_id
	 * @param wallet_kind_id
	 * @return
	 */
	@Update("update mp_channel_wallet set account_balance = account_balance - #{wait_liquidated_balance},"
			+ "wait_liquidated_balance = wait_liquidated_balance - #{wait_liquidated_balance}"
			+ "where channel_id = #{channel_id}")
	int recoveryBalanceByChIdAndWalletKindId(@Param("wait_liquidated_balance")BigDecimal wait_liquidated_balance,@Param("channel_id")long channel_id);
	
	
	/**
	 * 代付成功，但钱口多了或者少了
	 * @param wait_liquidated_balance 原来冻结的金额
	 * @param real_balance   实际扣除金额
	 * @param channel_id
	 * @param wallet_kind_id
	 * @return
	 */
	@Update("update mp_channel_wallet set account_balance = account_balance - #{real_balance},"
			+ "wait_liquidated_balance = wait_liquidated_balance - #{wait_liquidated_balance}"
			+ "where channel_id = #{channel_id}")
	int recoveryBalanceByChIdAndWalletKindIdButBalNotRight(@Param("wait_liquidated_balance")BigDecimal wait_liquidated_balance,@Param("real_balance")BigDecimal real_balance,@Param("channel_id")long channel_id);
	
	/**
	 * 代付失败，清除冻结金额
	 * @param wait_liquidated_balance
	 * @param channel_id
	 * @param wallet_kind_id
	 * @return
	 */
	@Update("update mp_channel_wallet set wait_liquidated_balance = wait_liquidated_balance - #{wait_liquidated_balance}"
			+ "where channel_id = #{channel_id}")
	int recoveryBalanceByChIdAndWalletKindIdWhenFail(@Param("wait_liquidated_balance")BigDecimal wait_liquidated_balance,@Param("channel_id")long channel_id);
	
	
	/**
	 * 根据渠道id更新渠道钱包
	 * @param wait_liquidated_balance
	 * @param channel_id
	 * @param wallet_kind_id
	 * @return
	 */
	@Update("update mp_channel_wallet set account_balance = account_balance + #{wait_liquidated_balance} where channel_id = #{channel_id}")
	int addMoneyByChannelId(@Param("wait_liquidated_balance")BigDecimal wait_liquidated_balance,@Param("channel_id")long channel_id);
	
}
