package com.api.bussiness.dao.order;


import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.api.bussiness.dao.table.merchant.MpMerchant;
import com.api.bussiness.dao.table.merchant.MpMerchantBank;
import com.api.bussiness.dao.table.merchant.MpMerchantMethod;

@Repository
public interface MerchantDao {

    /**
     * 根据商户号获取商户信息
     *
     * @param code
     * @return
     */
    @Select("select * from mp_merchant where code = #{code}")
    MpMerchant getMerchantByCode(@Param("code") String code);

    /**
     * 根据商户id获取商户信息
     *
     * @return
     */
    @Select("select * from mp_merchant where id = #{id}")
    MpMerchant getMerchantById(@Param("id") long id);

    /**
     * 根据商户id获取商户信息
     *
     * @return
     */
    @Select("select * from mp_merchant where user_id = #{userId}")
    MpMerchant getMerchantByUserId(@Param("userId") long userId);

    /**
     * 根据商户支付方式id获取商户支付方式
     *
     * @param id
     * @return
     */
    @Select("select * from mp_merchant_method where id = #{id} and status = 0 and merchant_status = 0 "
            + "and channel_status = 0 and channel_method_status = 0")
    MpMerchantMethod getMpMerchantMethodById(@Param("id") long id);

    /**
     * 获取优先级最高商户支付方式
     *
     * @param id
     * @return
     */
    @Select("select * from mp_merchant_method where level = (select max(level) from mp_merchant_method where "
            + "id = #{id} and status = 0 and merchant_status = 0 and channel_status = 0 and channel_method_status = 0) "
            + "and status = 0 and merchant_status = 0 and channel_status = 0 and channel_method_status = 0")
    MpMerchantMethod getHighestMerchantMethodByMerchantId(@Param("id") int id);


    /**
     * 根据商户号获取商户密钥
     *
     * @param code
     * @return
     */
    @Select("Select secret_key from mp_merchant where code = #{code}")
    String getMerchantKeyByCode(@Param("code") String code);


    /**
     * 根据商户id、银行id和银行卡号查询商户的银行卡信息
     *
     * @param bank_id
     * @return
     */
    @Select("select * from mp_merchant_bank where merchant_id = #{mch_id} and bank_id = #{bank_id} and bank_union_no = #{bank_no}")
    MpMerchantBank getMerchantBankByMerchantBankIdAndBankIdAndBankNo(@Param("mch_id") long mch_id, @Param("bank_id") long bank_id, @Param("bank_no") String bank_no);


    /**
     * 获得商户级别最高的支付方式
     *
     * @param ids
     * @param merchant_id
     * @return
     */
    @Select("select * from mp_merchant_method where channel_method_id in(${ids}) "
            + "and status = 0 and merchant_status = 0 and channel_status = 0 and "
            + "channel_method_status = 0 and "
            + "merchant_id = #{merchant_id} order by level desc limit 0,1")
    MpMerchantMethod getMerchantHighestLevelChannel(@Param("ids") String ids, @Param("merchant_id") long merchant_id);


    /**
     * 根据商户id和支付方式获得优先级最高的支付方式
     * sql解析：先查优先级最高的出来，然后根据商户id、支付方式码和优先级查出可能商户支付方式
     *
     * @param ids
     * @param merchant_id
     * @return
     */
    @Select("select * from mp_merchant_method where"
            + " status = 0 and merchant_status = 0 and channel_status = 0 "
            + "and merchant_id = #{merchant_id} and pay_kind_code = #{pay_kind_code} order by level desc limit 0,1")
    MpMerchantMethod getHighestLevelByMchIdAndPayKindCode(@Param("merchant_id") long merchant_id, @Param("pay_kind_code") String pay_kind_code);


    /**
     * 根据商户id和支付方式获得优先级最高的支付方式
     *
     * @param ids
     * @param merchant_id
     * @return
     */
    @Select("select * from mp_merchant_method where "
            + "merchant_id = #{merchant_id} and "
            + "pay_kind_code = #{pay_kind_code} and status = 0 and merchant_status = 0 and channel_status = 0 and channel_method_status = 0 "
            + "and level = "
            + "(select level from mp_merchant_method where "
            + "status = 0 and merchant_status = 0 and channel_status = 0 and channel_method_status = 0 "
            + "and pay_kind_code = #{pay_kind_code}  and merchant_id = #{merchant_id} "
            + "group by level order by level desc limit 0,1)")
    List<MpMerchantMethod> getHighestLevelsByMchIdAndPayKindCode(@Param("merchant_id") long merchant_id, @Param("pay_kind_code") String pay_kind_code);

    /**
     * 根据 商户号获取商户支付方式集合
     *
     * @param merchant_code
     * @return
     */
    @Select("select * from mp_merchant_method where merchant_code = #{merchant_code} and channel_method_status = 0 "
            + "and status = 0 and merchant_status = 0 and channel_status = 0")
    List<MpMerchantMethod> getMpMerchantMethodByMerchantCode(@Param("merchant_code") String merchant_code);

    @Select("select * from mp_merchant_method where "
            + "merchant_id = #{merchant_id} and "
            + "pay_kind_code = #{pay_kind_code} and status = 0 and merchant_status = 0 and channel_status = 0 and channel_method_status = 0 "
            +"and channel_method_pay_limit_left <= #{fee} and channel_method_pay_limit_right >= #{fee} "
            + "and level = "
            + "(select level from mp_merchant_method where "
            + "status = 0 and merchant_status = 0 and channel_status = 0 and channel_method_status = 0 "
            + "and pay_kind_code = #{pay_kind_code}  and merchant_id = #{merchant_id} "
            +"and channel_method_pay_limit_left <= #{fee} and channel_method_pay_limit_right >= #{fee} "
            +"and (method_pay_limit_left is null or method_pay_limit_right is null or (method_pay_limit_left <= #{fee} and method_pay_limit_right >= #{fee})) "
            + "group by level order by level desc limit 0,1)")
    List<MpMerchantMethod> getHighestLevelsByMchIdAndPayKindCodeAndFee(@Param("merchant_id") long merchant_id, @Param("pay_kind_code") String pay_kind_code, @Param("fee")BigDecimal fee);
}
