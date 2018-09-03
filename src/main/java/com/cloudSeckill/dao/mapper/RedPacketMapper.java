package com.cloudSeckill.dao.mapper;

import com.cloudSeckill.dao.domain.RedPacket;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RedPacketMapper {

    /**
     * 插入一条红包数据到数据库
     */
    void insertRedPacket(RedPacket redPacket);

    /**
     * 查询指定用户当日所有的红包金额总和
     */
    int selectRedPacketSumToday(RedPacket redPacket);

    /**
     * 查询指定用户的指定日期的所有红包列表
     */
    List<RedPacket> selectRedPacketByDateAndWechatId(RedPacket redPacket);
}
