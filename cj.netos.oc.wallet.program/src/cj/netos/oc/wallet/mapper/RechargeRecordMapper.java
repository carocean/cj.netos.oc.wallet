package cj.netos.oc.wallet.mapper;

import cj.netos.oc.wallet.model.RechargeRecord;
import cj.netos.oc.wallet.model.RechargeRecordExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RechargeRecordMapper {

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    long countByExample(RechargeRecordExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int deleteByExample(RechargeRecordExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int deleteByPrimaryKey(String sn);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int insert(RechargeRecord record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int insertSelective(RechargeRecord record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    List<RechargeRecord> selectByExample(RechargeRecordExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    RechargeRecord selectByPrimaryKey(String sn);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByExampleSelective(@Param("record") RechargeRecord record, @Param("example") RechargeRecordExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByExample(@Param("record") RechargeRecord record, @Param("example") RechargeRecordExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByPrimaryKeySelective(RechargeRecord record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByPrimaryKey(RechargeRecord record);

    void finish(@Param(value = "sn") String sn, @Param(value = "amount") long amount, @Param(value = "lutime") String lutime, @Param(value = "doneCode") String doneCode, @Param(value = "doneMsg") String doneMsg);
}