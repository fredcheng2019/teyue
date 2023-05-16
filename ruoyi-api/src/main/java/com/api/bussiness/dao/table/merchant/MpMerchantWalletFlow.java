package com.api.bussiness.dao.table.merchant;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Setter
@Getter
public class MpMerchantWalletFlow {

    public static final int FLOW_TYPE1 = 1;//预付流水
    public static final int FLOW_TYPE2 = 2;//余额流水

    public static final int FLOW_DIRECTION1 = 1;//收入
    public static final int FLOW_DIRECTION2 = 2;//支出

    public static final int FLOW_DETAIL_TYPE1 = 1;//用户充值
    public static final int FLOW_DETAIL_TYPE2 = 2;//用户提现
    public static final int FLOW_DETAIL_TYPE3 = 3;//人工增加
    public static final int FLOW_DETAIL_TYPE4 = 4;//人工扣减
    public static final int FLOW_DETAIL_TYPE5 = 5;//补单
    public static final int FLOW_DETAIL_TYPE6 = 6;//扣单

    /** 状态 */
    private Long id;
    /** 商户标识 */
    private Long merchant_id;
    /**  */
    private String merchant_name;
    /** 变动金额 */
    private BigDecimal amount;
    /** 修改前预付金额 */
    private BigDecimal previous_balance;
    /** 当前预付金额 */
    private BigDecimal current_balance;
    /** 操作时间 */
    private Date created_time;
    /** 操作人 */
    private String created_by;
    /**
     * 类型 2-余额流水 1-预付流水
     */
    private Integer flow_type;

    /**
     * 收支方向: 1-收入 2-支出
     * @param id
     */
    private Integer flow_direction;

    /**
     * 收支细类: 1-充值 2-提现扣款 3-人工增加 4-人工扣减
     * @return
     */
    private Integer  flow_detail_type;

    /** 订单ID */
    private String order_id;
}
