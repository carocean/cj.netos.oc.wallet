package cj.netos.oc.wallet.mapper;

import cj.netos.oc.wallet.model.OnorderBill;
import cj.netos.oc.wallet.model.OnorderBillExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OnorderBillMapper {

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    long countByExample(OnorderBillExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int deleteByExample(OnorderBillExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int deleteByPrimaryKey(String sn);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int insert(OnorderBill record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int insertSelective(OnorderBill record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    List<OnorderBill> selectByExample(OnorderBillExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    OnorderBill selectByPrimaryKey(String sn);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByExampleSelective(@Param("record") OnorderBill record, @Param("example") OnorderBillExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByExample(@Param("record") OnorderBill record, @Param("example") OnorderBillExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByPrimaryKeySelective(OnorderBill record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByPrimaryKey(OnorderBill record);

    List<OnorderBill> page(@Param(value = "accountid") String accountid, @Param(value = "limit") int limit, @Param(value = "offset") long offset);

    List<OnorderBill> month(@Param(value = "accountid") String accountid,@Param(value = "year") int year,@Param(value = "month") int month, @Param(value = "limit") int limit, @Param(value = "offset") long offset);

}