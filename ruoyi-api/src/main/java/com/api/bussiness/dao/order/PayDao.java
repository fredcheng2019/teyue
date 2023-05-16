package com.api.bussiness.dao.order;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.api.bussiness.dao.table.order.MpPay;

import org.apache.ibatis.annotations.Param;

@Repository
public interface PayDao {

    @Insert("insert into mp_transaction_pay"
            + "(code,mch_code,fee,ch_fee,mch_fee,fin_fee,begin,status,"
            + "merchant_method_id,merchant_id,merchant_method_pay_rate,merchant_name,merchant_code,"
            + "channel_method_id,channel_method_pay_rate,"
            + "channel_id,channel_name,"
            + "channel_code,pay_kind_id,pay_kind_name,pay_kind_code,"
            + "wallet_kind_id,wallet_kind_name,wallet_kind_code,callback_url,redirect_url,channel_class_name,merchant_notify_status,"
            + "sub_fee,expires,merchant_notify_times,goods,ch_code, remarks, agent_id, agent_fee, agent_pay_rate, client_ip)"
            + "values"
            + "(#{bussiness.code},#{bussiness.mch_code},#{bussiness.fee},#{bussiness.ch_fee},#{bussiness.mch_fee},#{bussiness.fin_fee},#{bussiness.begin},#{bussiness.status},"
            + "#{bussiness.merchant_method_id},#{bussiness.merchant_id},#{bussiness.merchant_method_pay_rate},#{bussiness.merchant_name},#{bussiness.merchant_code},"
            + "#{bussiness.channel_method_id},#{bussiness.channel_method_pay_rate},"
            + "#{bussiness.channel_id},#{bussiness.channel_name},"
            + "#{bussiness.channel_code},#{bussiness.pay_kind_id},#{bussiness.pay_kind_name},#{bussiness.pay_kind_code},"
            + "#{bussiness.wallet_kind_id},#{bussiness.wallet_kind_name},#{bussiness.wallet_kind_code},#{bussiness.callback_url},#{bussiness.redirect_url},#{bussiness.channel_class_name},#{bussiness.merchant_notify_status},"
            + "#{bussiness.sub_fee},#{bussiness.expires},#{bussiness.merchant_notify_times},#{bussiness.goods},#{bussiness.ch_code} ,#{bussiness.remarks},#{bussiness.agent_id},#{bussiness.agent_fee},#{bussiness.agent_pay_rate},#{bussiness.client_ip})")
    int addPay(@Param("bussiness")MpPay pay,@Param("tableName")String tableName);//新增支付记录，我们平台生成的，未向上游发起下单的

    /**
     * 根据提单订单结果更新订单状态
     * @param code
     * @param status
     * @param ch_code
     * @return
     */
    @Update("update mp_transaction_pay set status = #{status},ch_code = #{ch_code} where code = #{code}")
    int updatePayByChannelPayResult(@Param("code")String code,@Param("status")int status,@Param("ch_code")String ch_code);//更新上游支付响应结果，可能支付成功、失败或者网络不通


    /**
     * 根据提单订单结果更新订单状态
     * @param code
     * @param status
     * @param ch_code
     * @return
     */
    @Update("update mp_transaction_pay set status = #{status},ch_code = #{ch_code},end = #{end} where code = #{code}")
    int updatePayByChannelPayResultWithEnd(@Param("code")String code,@Param("status")int status,@Param("ch_code")String ch_code,@Param("end")Date end);//更新上游支付响应结果，可能支付成功、失败或者网络不通


    /**
     * 回调成功且实际支付的钱不一致时使用
     * @param code
     * @param fee
     * @param mch_fee
     * @param ch_fee
     * @param fin_fee
     * @return
     */
    @Update("update mp_transaction_pay set fee = #{fee},mch_fee = #{mch_fee},ch_fee = #{ch_fee},fin_fee = #{fin_fee},ch_code = #{ch_code} where code = #{code}")
    int updatePayWhenCallBackSuccessAndFeeChange(@Param("code")String code,@Param("fee")BigDecimal fee,@Param("mch_fee")BigDecimal mch_fee,@Param("ch_fee")BigDecimal ch_fee,@Param("fin_fee")BigDecimal fin_fee,@Param("ch_code")String ch_code);

    /**
     * 判断订单号是否存在
     * @param merchantPayCode
     * @return
     */
    @Select("select count(*) from mp_transaction_pay where mch_code = #{merchantPayCode}")
    int getMerchantPayCodeNum(@Param("merchantPayCode")String merchantPayCode);

    /**
     * 根据商户号和商户订单号判断订单是否存在
     * @param merchant_code
     * @param code
     * @return
     */
    @Select("select count(*) from mp_transaction_pay where mch_code = #{mch_code} and merchant_code = #{merchant_code}")
    int getOrderByMchIdAndMchCodeCount(@Param("merchant_code")String merchant_code,@Param("mch_code")String mch_code);

    /**
     * 根据商户号、渠道号、渠道订单号判断订单是否存在
     * @param merchant_code
     * @param channel_code
     * @param ch_code
     * @return
     */
    @Select("select count(*) from mp_transaction_pay where ch_code = #{ch_code} and merchant_code = #{merchant_code} and channel_code = #{channel_code}")
    int getOrderByMchIdAndChCodeAndChannelCodeCount(@Param("merchant_code")String merchant_code,@Param("channel_code")String channel_code,@Param("ch_code")String ch_code);

    /**
     *
     * @param 根据商户号和商户订单号查询订单
     * @param code
     * @return
     */
    @Select("select * from mp_transaction_pay where mch_code = #{mch_code} and merchant_code = #{merchant_code}")
    MpPay getPayByMchIdAndMchCode(@Param("merchant_code")String merchant_code, @Param("mch_code")String mch_code);

    /**
     * 根据渠道订单号和平台订单号更新订单状态,同时更新渠道通知状态
     * @param code
     * @param ch_code
     * @param status
     * @return
     */
    @Update("update mp_transaction_pay set status = #{status} where code = #{code} and ch_code = #{ch_code}")
    int updatePayStatusByCodeAndChCode(@Param("code")String code,@Param("ch_code")String ch_code,@Param("status")int status);


    /**
     * 根据渠道平台订单号更新状态
     * @param code
     * @param ch_code
     * @param status
     * @return
     */
    @Update("update mp_transaction_pay set status = #{status} where code = #{code}")
    int updatePayStatusByCode(@Param("code")String code,@Param("status")int status);

    @Update("update mp_transaction_pay set status = #{status},end = #{end},ch_code = #{ch_code} where code = #{code} and status != #{status}")
    int updatePayStatusEndByCode(@Param("code")String code,@Param("status")int status,@Param("end")Date end,@Param("ch_code")String ch_code);
    /**
     * 根据渠道订单号和平台订单号更新商户回调通知状态
     * @param code
     * @param ch_code
     * @param status
     * @param merchant_notify_status
     * @return
     */
    @Update("update mp_transaction_pay set status = #{status},merchant_notify_times = merchant_notify_times + 1 "
            + "where code = #{code} and ch_code = #{ch_code} and merchant_notify_status = #{merchant_notify_status}")
    int updatePayMerchantNotifyStatusByCodeAndChCode(@Param("code")String code,@Param("ch_code")String ch_code,@Param("merchant_notify_status")int merchant_notify_status);


    /**
     * 根据平台订单号更新商户回调通知状态
     * @param code
     * @param ch_code
     * @param status
     * @param merchant_notify_status
     * @return
     */
    @Update("update mp_transaction_pay set merchant_notify_status = #{merchant_notify_status},merchant_notify_times = merchant_notify_times + 1 "
            + "where code = #{code}")
    int updatePayMerchantNotifyStatusByCode(@Param("code")String code,@Param("merchant_notify_status")int merchant_notify_status);

    /**
     * 根据平台订单号增加通知次数
     * @return
     */
    @Update("update mp_transaction_pay set merchant_notify_times = merchant_notify_times + 1 where code = #{code}")
    int updateAddMerchantNotifyTimesByCode(@Param("code")String code);


    /**
     * @param 根据渠道订单号和平台订单号获取订单
     * @param code
     * @param ch_code
     * @return
     */
    @Select("select * from mp_transaction_pay where code = #{code} and ch_code = #{ch_code}")
    MpPay getOrderByCodeAndChCode(@Param("code")String code,@Param("ch_code")String ch_code);


    /**
     *
     * @param 根据平台订单号获取订单
     * @param code
     * @return
     */
    @Select("select * from mp_transaction_pay where code = #{code}")
    MpPay getOrderByCode(@Param("code")String code);

    /**
     *
     * @param 根据平台订单号获取订单
     * @param code
     * @return
     */
    @Select("select * from mp_transaction_pay where code = #{code} for update")
    MpPay getOrderByCodeForUpdate(@Param("code")String code);

    /**
     *
     * @param 根据渠道订单号获取订单
     * @param ch_code
     * @return
     */
    @Select("select * from mp_transaction_pay where ch_code = #{ch_code} for update")
    MpPay getOrderByChCodeForUpdate(@Param("ch_code")String ch_code);

    /**
     * 查询状态为已支付、通知状态为未通知、通知次数小于8次和近一个小时的的订单
     * @return
     */
    @Select("select * from mp_transaction_pay where status = 5 and merchant_notify_status = 0 and merchant_notify_times < 8 and end IS NOT NULL  and now() < SUBDATE(end,interval -1 hour) and callback_url != ''")
    List<MpPay> getWaitToNotifyPay();

    /**
     * 根据提单订单结果更新订单状态
     * @param id
     * @param status
     * @param ch_code
     * @return
     */
    @Update("update mp_transaction_pay set status = #{status},ch_code = #{ch_code},end = #{end} where id = #{id} and status != #{status}")
    int updatePayByChannelPayResultWithId(@Param("id")long id,@Param("status")int status,@Param("ch_code")String ch_code,@Param("end")Date end);//更新上游支付响应结果，可能支付成功、失败或者网络不通

    @Update("update mp_transaction_pay set status = #{status},end = #{end} where id = #{id} and status != #{status}")
    int updatePayStatusAndEndById(@Param("id")long id,@Param("status")int status,@Param("end")Date end);

    /**
     * 获取USDT费率
     * @return
     */
    @Select("SELECT config_value FROM `sys_config` where config_key = 'usdt-rate'")
    BigDecimal getUsdtRate();
}
