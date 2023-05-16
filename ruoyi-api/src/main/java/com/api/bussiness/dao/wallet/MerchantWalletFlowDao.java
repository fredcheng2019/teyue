package com.api.bussiness.dao.wallet;

import com.api.bussiness.dao.table.merchant.MpMerchantWalletFlow;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MerchantWalletFlowDao {

    @Insert("insert into mp_merchant_wallet_flow"

            + "(merchant_id,"//1
            + "merchant_name,"//2
            + "amount,"//3
            + "previous_balance,"//4
            + "current_balance,"//5
            + "created_time,"//6
            + "created_by,"//7
            + "flow_type,flow_direction,flow_detail_type,order_id)"//8

            + "values"

            + "(#{info.merchant_id},"//1
            + "#{info.merchant_name},"//2
            + "#{info.amount},"//3
            + "#{info.previous_balance},"//4
            + "#{info.current_balance},"//5
            + "#{info.created_time},"//6
            + "#{info.created_by},"//7
            + "#{info.flow_type},#{info.flow_direction},#{info.flow_detail_type},#{info.order_id})")//7
    int addMerchantWalletFlow(@Param("info") MpMerchantWalletFlow info, @Param("tableName")String tableName);
}
