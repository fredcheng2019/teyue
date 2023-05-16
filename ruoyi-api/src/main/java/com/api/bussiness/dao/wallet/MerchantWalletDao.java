package com.api.bussiness.dao.wallet;

import java.math.BigDecimal;

import com.api.bussiness.dao.table.merchant.MpMerchantWallet;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface MerchantWalletDao {

    /**
     * 根据商户id和钱包id获取商户钱包，同时要钱够
     *
     * @param merchant_id
     * @param wallet_kind_id
     * @param total_fee
     * @return
     */
    @Select("select * from mp_merchant_wallet where  merchant_id = #{merchant_id} "
            + "and wallet_kind_id = #{wallet_kind_id} and (account_balance  - wait_liquidated_balance - unliquidated_balance_one - unliquidated_balance_two) >= #{total_fee} for update")
    MpMerchantWallet getWalletByMerchantIdAndWalletKindIdAndEnoughMoney(@Param("merchant_id") long merchant_id, @Param("wallet_kind_id") long wallet_kind_id, @Param("total_fee") BigDecimal total_fee);


    /**
     * 根据商户id和钱包id获取商户钱包
     *
     * @param merchant_id
     * @param wallet_kind_id
     * @return
     */
    @Select("select * from mp_merchant_wallet where  merchant_id = #{merchant_id} "
            + "and wallet_kind_id = #{wallet_kind_id}")
    MpMerchantWallet getWalletByMerchantIdAndWalletKindId(@Param("merchant_id") long merchant_id, @Param("wallet_kind_id") long wallet_kind_id);


    /**
     * 根据id
     *
     * @param id
     * @return
     */
    @Select("select * from mp_merchant_wallet where  id = #{id} for update")
    MpMerchantWallet getWalletByIdForUpdate(@Param("id") long id);


    /**
     * 冻结金额
     *
     * @param wait_liquidated_balance
     * @param id
     * @return
     */
    @Update("update mp_merchant_wallet set wait_liquidated_balance = wait_liquidated_balance + ${wait_liquidated_balance} "
            + "where id = #{id}")
    int frozenBalanceById(@Param("wait_liquidated_balance") BigDecimal wait_liquidated_balance, @Param("id") long id);

    /**
     * 根据钱包id冻结金额和增加截留资金,时间范围00:00-11:00开区间
     *
     * @param wait_liquidated_balance
     * @param unliquidated_balance_one
     * @param id
     * @return
     */
    @Update("update mp_merchant_wallet set wait_liquidated_balance = wait_liquidated_balance + ${wait_liquidated_balance},"
            + "unliquidated_balance_one = unliquidated_balance_one + #{unliquidated_balance_one} "
            + "where id = #{id}")
    int frozenBalanceAndAddUnliquidatedBalanceOneById(@Param("wait_liquidated_balance") BigDecimal wait_liquidated_balance, @Param("unliquidated_balance_one") BigDecimal unliquidated_balance_one, @Param("id") long id);


    /**
     * 根据钱包id冻结金额和增加截留资金,时间范围11:00-00:00闭区间
     *
     * @param wait_liquidated_balance
     * @param unliquidated_balance_two
     * @param id
     * @return
     */
    @Update("update mp_merchant_wallet set wait_liquidated_balance = wait_liquidated_balance + ${wait_liquidated_balance},"
            + "unliquidated_balance_two = unliquidated_balance_two + #{unliquidated_balance_two} "
            + "where id = #{id}")
    int frozenBalanceAndAddUnliquidatedBalanceTwoById(@Param("wait_liquidated_balance") BigDecimal wait_liquidated_balance, @Param("unliquidated_balance_two") BigDecimal unliquidated_balance_two, @Param("id") long id);


    /**
     * 解冻金额
     *
     * @param wait_liquidated_balance
     * @param id
     * @return
     */
    @Update("update mp_merchant_wallet set wait_liquidated_balance = wait_liquidated_balance - #{wait_liquidated_balance} "
            + "where id = #{id}")
    int recoveryBalanceById(@Param("wait_liquidated_balance") BigDecimal wait_liquidated_balance, @Param("id") long id);

    /**
     * 代付成功，清除冻结金额
     *
     * @param wait_liquidated_balance
     * @param channel_id
     * @param merchant_id
     * @param wallet_kind_id
     * @return
     */
    @Update("update mp_merchant_wallet set account_balance = account_balance - #{wait_liquidated_balance}, "
            + "wait_liquidated_balance = wait_liquidated_balance - #{wait_liquidated_balance} "
            + "where merchant_id = #{merchant_id} and wallet_kind_id = #{wallet_kind_id}")
    int recoveryBalanceByMchIdAndWalletKindId(@Param("wait_liquidated_balance") BigDecimal wait_liquidated_balance, @Param("merchant_id") long merchant_id, @Param("wallet_kind_id") long wallet_kind_id);


    /**
     * 代付成功，但钱口多了或少了清除冻结金额
     *
     * @param wait_liquidated_balance 原来冻结的金额
     * @param real_balance            实际扣除的金额
     * @param channel_id
     * @param merchant_id
     * @param wallet_kind_id
     * @return
     */
    @Update("update mp_merchant_wallet set account_balance = account_balance - #{real_balance}, "
            + "wait_liquidated_balance = wait_liquidated_balance - #{wait_liquidated_balance} "
            + "where merchant_id = #{merchant_id} and wallet_kind_id = #{wallet_kind_id}")
    int recoveryBalanceByMchIdAndWalletKindIdButBalNoRight(@Param("wait_liquidated_balance") BigDecimal wait_liquidated_balance, @Param("real_balance") BigDecimal real_balance, @Param("merchant_id") long merchant_id, @Param("wallet_kind_id") long wallet_kind_id);


    /**
     * 代付失败，清除冻结金额
     *
     * @param wait_liquidated_balance
     * @param channel_id
     * @param merchant_id
     * @param wallet_kind_id
     * @return
     */
    @Update("update mp_merchant_wallet set wait_liquidated_balance = wait_liquidated_balance - #{wait_liquidated_balance} "
            + "where merchant_id = #{merchant_id} and wallet_kind_id = #{wallet_kind_id}")
    int recoveryBalanceByThreeIdWhenFail(@Param("wait_liquidated_balance") BigDecimal wait_liquidated_balance, @Param("merchant_id") long merchant_id, @Param("wallet_kind_id") long wallet_kind_id);

    /**
     * 根据渠道id、商户id和钱包id更余额
     *
     * @param channel_id
     * @param merchant_id
     * @param wallet_kind_id
     * @param account_balance
     * @return
     */
    @Update("update mp_merchant_wallet set account_balance = account_balance + #{account_balance} where merchant_id = #{merchant_id} and wallet_kind_id = #{wallet_kind_id}")
    int addMoneyByMchIdAndWalletKindId(@Param("merchant_id") long merchant_id, @Param("wallet_kind_id") long wallet_kind_id, @Param("account_balance") BigDecimal account_balance);


    /**
     * 更新资金流
     */
    @Update("update mp_merchant_wallet set account_balance = account_balance + unliquidated_balance_two,"
            + "unliquidated_balance_two = unliquidated_balance_one,"
            + "unliquidated_balance_one = 0 where 1 = 1")
    int updateUnliquidatedBalance();

}
