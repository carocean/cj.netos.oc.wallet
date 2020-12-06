package cj.netos.oc.wallet.mapper;

import cj.netos.oc.wallet.model.TrialBill;
import cj.netos.oc.wallet.model.TrialBillExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TrialBillMapper {

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    long countByExample(TrialBillExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int deleteByExample(TrialBillExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int deleteByPrimaryKey(String sn);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int insert(TrialBill record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int insertSelective(TrialBill record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    List<TrialBill> selectByExample(TrialBillExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    TrialBill selectByPrimaryKey(String sn);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByExampleSelective(@Param("record") TrialBill record, @Param("example") TrialBillExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByExample(@Param("record") TrialBill record, @Param("example") TrialBillExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByPrimaryKeySelective(TrialBill record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByPrimaryKey(TrialBill record);

    List<TrialBill> page(@Param(value = "accountid") String accountid, @Param(value = "limit") int limit, @Param(value = "offset") long offset);

    List<TrialBill> month(@Param(value = "accountid") String accountid, @Param(value = "year") int year, @Param(value = "month") int month, @Param(value = "limit") int limit, @Param(value = "offset") long offset);

}