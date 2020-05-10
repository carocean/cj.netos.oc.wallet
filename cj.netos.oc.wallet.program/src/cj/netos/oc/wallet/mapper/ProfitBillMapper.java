package cj.netos.oc.wallet.mapper;

import cj.netos.oc.wallet.model.ProfitBill;
import cj.netos.oc.wallet.model.ProfitBillExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ProfitBillMapper {
    /**
     * @mbg.generated generated automatically, do not modify!
     */
    long countByExample(ProfitBillExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int deleteByExample(ProfitBillExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int deleteByPrimaryKey(String sn);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int insert(ProfitBill record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int insertSelective(ProfitBill record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    List<ProfitBill> selectByExample(ProfitBillExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    ProfitBill selectByPrimaryKey(String sn);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByExampleSelective(@Param("record") ProfitBill record, @Param("example") ProfitBillExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByExample(@Param("record") ProfitBill record, @Param("example") ProfitBillExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByPrimaryKeySelective(ProfitBill record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByPrimaryKey(ProfitBill record);
}