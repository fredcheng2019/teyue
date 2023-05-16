package com.ruoyi.merchant.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 表 mp_merchant_wallet_flow
 *
 * @author ruoyi
 * @date 2020-08-31
 */
public class MerchantWalletFlow extends BaseEntity {
    private static final long serialVersionUID = 1L;
    public static final int FLOW_TYPE1 = 1;//押金流水
    public static final int FLOW_TYPE2 = 2;//余额流水

    public static final int FLOW_DIRECTION1 = 1;//收入
    public static final int FLOW_DIRECTION2 = 2;//支出

    public static final int FLOW_DETAIL_TYPE1 = 1;//用户充值
    public static final int FLOW_DETAIL_TYPE2 = 2;//用户提现
    public static final int FLOW_DETAIL_TYPE3 = 3;//人工增加
    public static final int FLOW_DETAIL_TYPE4 = 4;//人工扣减
    public static final int FLOW_DETAIL_TYPE5 = 5;//补单
    public static final int FLOW_DETAIL_TYPE6 = 6;//扣单
    public static final int FLOW_DETAIL_TYPE7 = 7;//冻结
    public static final int FLOW_DETAIL_TYPE8 = 8;//解冻
    public static final int FLOW_DETAIL_TYPE9 = 9;//预付

    /**
     * 状态
     */
    private Long id;
    /**
     * 商户标识
     */
    private Long merchantId;
    /**
     *
     */
    private String merchantName;
    /**
     * 变动金额
     */
    private BigDecimal amount;
    /**
     * 修改前预付金额
     */
    private BigDecimal previousBalance;
    /**
     * 当前预付金额
     */
    private BigDecimal currentBalance;
    /**
     * 操作时间
     */
    private Date createdTime;
    /**
     * 操作人
     */
    private String createdBy;
    /**
     * 类型 2-余额流水 1-预付流水
     */
    private Integer flowType;

    /**
     * 收支方向: 1-收入 2-支出
     *
     * @param id
     */
    private Integer flowDirection;

    /**
     * 收支细类: 1-充值 2-提现扣款 3-人工增加 4-人工扣减
     *
     * @return
     */
    private Integer flowDetailType;

    /**
     * 订单ID
     */
    private String orderId;

    /**
     * 查询开始时间
     */
    private String beginTime;

    /**
     * 查询结束时间
     */
    private String endTime;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setPreviousBalance(BigDecimal previousBalance) {
        this.previousBalance = previousBalance;
    }

    public BigDecimal getPreviousBalance() {
        return previousBalance;
    }

    public void setCurrentBalance(BigDecimal currentBalance) {
        this.currentBalance = currentBalance;
    }

    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Integer getFlowType() {
        return flowType;
    }

    public void setFlowType(Integer flowType) {
        this.flowType = flowType;
    }

    public Integer getFlowDirection() {
        return flowDirection;
    }

    public void setFlowDirection(Integer flowDirection) {
        this.flowDirection = flowDirection;
    }

    public Integer getFlowDetailType() {
        return flowDetailType;
    }

    public void setFlowDetailType(Integer flowDetailType) {
        this.flowDetailType = flowDetailType;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("merchantId", getMerchantId())
                .append("merchantName", getMerchantName())
                .append("amount", getAmount())
                .append("previousBalance", getPreviousBalance())
                .append("currentBalance", getCurrentBalance())
                .append("remark", getRemark())
                .append("createdTime", getCreatedTime())
                .append("createdBy", getCreatedBy())
                .toString();
    }
}
