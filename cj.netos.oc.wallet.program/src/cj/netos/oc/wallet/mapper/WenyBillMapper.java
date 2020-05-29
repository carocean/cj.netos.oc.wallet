package cj.netos.oc.wallet.mapper;

import cj.netos.oc.wallet.model.FreezenBill;
import cj.netos.oc.wallet.model.WenyBill;
import cj.netos.oc.wallet.model.WenyBillExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface WenyBillMapper {

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    long countByExample(WenyBillExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int deleteByExample(WenyBillExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int deleteByPrimaryKey(String sn);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int insert(WenyBill record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int insertSelective(WenyBill record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    List<WenyBill> selectByExample(WenyBillExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    WenyBill selectByPrimaryKey(String sn);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByExampleSelective(@Param("record") WenyBill record, @Param("example") WenyBillExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByExample(@Param("record") WenyBill record, @Param("example") WenyBillExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByPrimaryKeySelective(WenyBill record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByPrimaryKey(WenyBill record);

    List<WenyBill> page(@Param(value = "accountid") String accountid, @Param(value = "bankid") String bankid, @Param(value = "limit") int limit, @Param(value = "offset") long offset);

    List<WenyBill> month(@Param(value = "accountid") String accountid,@Param(value = "bankid") String bankid,@Param(value = "year") int year,@Param(value = "month") int month, @Param(value = "limit") int limit, @Param(value = "offset") long offset);

}