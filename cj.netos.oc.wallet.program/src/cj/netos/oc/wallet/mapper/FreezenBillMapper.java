package cj.netos.oc.wallet.mapper;

import cj.netos.oc.wallet.model.FreezenBill;
import cj.netos.oc.wallet.model.FreezenBillExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface FreezenBillMapper {

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    long countByExample(FreezenBillExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int deleteByExample(FreezenBillExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int deleteByPrimaryKey(String sn);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int insert(FreezenBill record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int insertSelective(FreezenBill record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    List<FreezenBill> selectByExample(FreezenBillExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    FreezenBill selectByPrimaryKey(String sn);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByExampleSelective(@Param("record") FreezenBill record, @Param("example") FreezenBillExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByExample(@Param("record") FreezenBill record, @Param("example") FreezenBillExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByPrimaryKeySelective(FreezenBill record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByPrimaryKey(FreezenBill record);
}