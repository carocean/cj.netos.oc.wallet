package cj.netos.oc.wallet.mapper;

import cj.netos.oc.wallet.model.AbsorbBill;
import cj.netos.oc.wallet.model.AbsorbBillExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AbsorbBillMapper {
    /**
     * @mbg.generated generated automatically, do not modify!
     */
    long countByExample(AbsorbBillExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int deleteByExample(AbsorbBillExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int deleteByPrimaryKey(String sn);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int insert(AbsorbBill record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int insertSelective(AbsorbBill record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    List<AbsorbBill> selectByExample(AbsorbBillExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    AbsorbBill selectByPrimaryKey(String sn);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByExampleSelective(@Param("record") AbsorbBill record, @Param("example") AbsorbBillExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByExample(@Param("record") AbsorbBill record, @Param("example") AbsorbBillExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByPrimaryKeySelective(AbsorbBill record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByPrimaryKey(AbsorbBill record);
}