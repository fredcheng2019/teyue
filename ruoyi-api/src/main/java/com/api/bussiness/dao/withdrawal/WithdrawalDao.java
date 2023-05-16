package com.api.bussiness.dao.withdrawal;

import java.math.BigDecimal;
import java.util.Date;

import com.api.bussiness.dao.table.withdrawal.Withdrawal;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import org.apache.ibatis.annotations.Param;



@Repository
public interface WithdrawalDao {

	@Insert("insert into mp_transaction_withdrawal"
			
			+ "(code,mch_code,fee,ch_fee,mch_fee,fin_fee,begin,status,"//1
			+ "merchant_id,merchant_name,merchant_code,"//2
			+ "channel_id,channel_name,channel_code,channel_withdrawal_fee,channel_mch_withdrawal_fee,"//3
			+ "wallet_kind_id,wallet_kind_name,wallet_kind_code,"//4
			+ "bank_name,bank_code,bank_branch,bank_sub_branch,bank_province,bank_city,"//5
			+ "bank_union_no,bank_account_owner,bank_account_no,bank_account_type,"//6
			+ "callback_url,channel_class_name,merchant_notify_status,sub_fee,ch_code)"//7
			
			+ "values"
			
			+ "(#{info.code},#{info.mch_code},#{info.fee},#{info.ch_fee},#{info.mch_fee},#{info.fin_fee},#{info.begin},#{info.status},"//1
			+ "#{info.merchant_id},#{info.merchant_name},#{info.merchant_code},"//2
			+ "#{info.channel_id},#{info.channel_name},#{info.channel_code},#{info.channel_withdrawal_fee},#{info.channel_mch_withdrawal_fee},"//3
			+ "#{info.wallet_kind_id},#{info.wallet_kind_name},#{info.wallet_kind_code},"//4
			+ "#{info.bank_name},#{info.bank_code},#{info.bank_branch},#{info.bank_sub_branch},#{info.bank_province},#{info.bank_city},"//5
			+ "#{info.bank_union_no},#{info.bank_account_owner},#{info.bank_account_no},#{info.bank_account_type},"//6
			+ "#{info.callback_url},#{info.channel_class_name},#{info.merchant_notify_status},#{info.sub_fee},#{info.ch_code})")//7
	int addWithdrewInfo(@Param("info") Withdrawal info, @Param("tableName")String tableName);
	
	/**
	 * 根据订单号更新代付状态
	 * @param code
	 * @param status
	 * @param ch_code
	 * @return
	 */
	@Update("update mp_transaction_withdrawal set status = #{status},ch_code = #{ch_code} where code = #{code}")
	int updateWithDrawalByChannelWithDrawalResult(@Param("code")String code,@Param("status")int status,@Param("ch_code")String ch_code);
	
	
	/**
	 * 根据渠道订单号更新订单状态
	 * @param code
	 * @param ch_code
	 * @param status
	 * @return
	 */
	@Update("update mp_transaction_withdrawal set status = #{status},end = #{end},ch_code = #{ch_code} where code = #{code}")
	int updateWithDrawalStatusByCodeAndChCode(@Param("code")String code,@Param("ch_code")String ch_code,@Param("status")int status,@Param("end")Date end);
	
	/**
	 * 根据平台订单号更新订单状态
	 * @param code
	 * @param ch_code
	 * @param status
	 * @return
	 */
	@Update("update mp_transaction_withdrawal set status = #{status},end = #{end},ch_code = #{ch_code} where code = #{code} and status != #{status}")
	int updateWithDrawalStatusByCode(@Param("code")String code,@Param("ch_code")String ch_code,@Param("status")int status,@Param("end")Date end);
	
	
	/**
	 * 
	 * @param 根据商户订单号和商户号获取订单
	 * @param code
	 * @return
	 */
	@Select("select * from mp_transaction_withdrawal where mch_code = #{mch_code} and merchant_code = #{merchant_code}")
	Withdrawal getWithDrawalByMChCodeAndMchantCode(@Param("mch_code")String mch_code, @Param("merchant_code")String merchant_code);
	
	@Select("select count(*) from mp_transaction_withdrawal where ch_code = #{ch_code} and merchant_code = #{merchant_code} and channel_code = #{channel_code}")
	int getWithDrawalByMChCodeAndMchantCodeAndchannelCode(@Param("ch_code")String ch_code, @Param("merchant_code")String merchant_code,@Param("channel_code")String channel_code);
	
	
	/**
	 * 
	 * @param 根据平台订单号获取订单
	 * @param code
	 * @return
	 */
	@Select("select * from mp_transaction_withdrawal where code = #{code} for update")
	Withdrawal getWithDrawalByCode(@Param("code")String code);
	
	/**
	 * 
	 * @param 根据平台订单号获取订单
	 * @param code
	 * @return
	 */
	@Select("select * from mp_transaction_withdrawal where code = #{code} for update")
	Withdrawal getWithDrawalByCodeForUpdate(@Param("code")String code);
	
	/**
	 * 根据平台订单号增加通知次数
	 * @return
	 */
	@Update("update mp_transaction_withdrawal set merchant_notify_times = merchant_notify_times + 1 where code = #{code}")
	int updateAddMerchantNotifyTimesByCode(@Param("code")String code);
	
	
	/**
	 * 
	 * @param 根据渠道订单号和平台订单号获取订单
	 * @param code
	 * @return
	 */
	@Select("select * from mp_transaction_withdrawal where code = #{code} and ch_code = #{ch_code}")
	Withdrawal getWithDrawalByCodeAndChCode(@Param("code")String code, @Param("ch_code")String ch_code);
	
	/**
	 * 回调成功且实际支付的钱不一致时使用
	 * @param code
	 * @param fee
	 * @param mch_fee
	 * @param ch_fee
	 * @param fin_fee
	 * @return
	 */
	@Update("update mp_transaction_withdrawal set fee = #{fee},mch_fee = #{mch_fee},ch_fee = #{ch_fee},fin_fee = #{fin_fee} where code = #{code}")
	int updateWithDrawalWhenCallBackSuccessAndFeeChange(@Param("code")String code,@Param("fee")BigDecimal fee,@Param("mch_fee")BigDecimal mch_fee,@Param("ch_fee")BigDecimal ch_fee,@Param("fin_fee")BigDecimal fin_fee);
}
