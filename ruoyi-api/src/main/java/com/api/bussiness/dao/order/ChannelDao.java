package com.api.bussiness.dao.order;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.api.bussiness.dao.table.channel.MpChannel;
import com.api.bussiness.dao.table.channel.MpChannelMethod;


@Repository
public interface ChannelDao {

    /**
     * 根据渠道号获取渠道信息
     *
     * @param code
     * @return
     */
    @Select("select * from mp_channel where code = #{code} and status = 0")
    MpChannel getChannelByCode(@Param("code") String code);

    /**
     * 根据渠道id获取渠道信息
     *
     * @param id
     * @return
     */
    @Select("select * from mp_channel where id = #{id} and status = 0")
    MpChannel getChannelById(@Param("id") long id);

    /**
     * 判断渠道的代付级别
     *
     * @return
     */
    @Select("select count(*) from (select withdrawal_level from mp_channel where status = 0 group by withdrawal_level) mch")
    int getWithdrawalLevelCount();

    /**
     * 获取支付级别最高的渠道
     *
     * @return
     */
    @Select("select * from mp_channel where status = 0 order by withdrawal_level desc limit 0,1")
    MpChannel getHighestWithDrawalLevelMpChannel();

    /**
     * 根据类名获取渠道信息
     *
     * @param class_name
     * @return
     */
    @Select("select * from mp_channel where class_name = #{class_name} and status = 0")
    MpChannel getChannelByClassName(@Param("class_name") String class_name);

    /**
     * 根据支付方式获取渠道的支付方式
     *
     * @param payKindId
     * @return
     */
    @Select("select * from mp_channel_method where pay_kind_id = #{payKindId} and status = 0")
    List<MpChannelMethod> getMpChannelMethodsByPayKindId(@Param("payKindId") long payKindId);

    /**
     * 获得优先级最高的支付方式
     *
     * @return
     */
    @Select("select * from mp_channel where "
            + "status = 0 and "
            + "level = "
            + "(select level from mp_channel where "
            + "status = 0 "
            + "group by level order by level desc limit 0,1)")
    List<MpChannel> getHighestLevelsMpChannel();

    /**
     * 根据渠道id获得优先级最高的支付方式
     *
     * @param ids
     * @return
     */
    @Select("select * from mp_channel where "
            + "status = 0 and id in (${ids}) and "
            + "level = "
            + "(select level from mp_channel where "
            + "status = 0 and id in (${ids}) "
            + "group by level order by level desc limit 0,1)")
    List<MpChannel> getHighestLevelsMpChannelByIds(@Param("ids") String ids);

    /**
     * 根据id获取渠道支付信息
     *
     * @param id
     * @return
     */
    @Select("select channel_method_code from mp_channel_method where id = #{id}")
    String getChannelMethodById(@Param("id") long id);


}
