package cj.netos.oc.wallet.mapper;

import cj.netos.oc.wallet.model.PayChannel;
import cj.netos.oc.wallet.model.PayChannelExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PayChannelMapper {

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    long countByExample(PayChannelExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int deleteByExample(PayChannelExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int deleteByPrimaryKey(String id);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int insert(PayChannel record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int insertSelective(PayChannel record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    List<PayChannel> selectByExample(PayChannelExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    PayChannel selectByPrimaryKey(String id);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByExampleSelective(@Param("record") PayChannel record, @Param("example") PayChannelExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByExample(@Param("record") PayChannel record, @Param("example") PayChannelExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByPrimaryKeySelective(PayChannel record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByPrimaryKey(PayChannel record);
}