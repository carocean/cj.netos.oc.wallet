package cj.netos.oc.wallet.mapper;

import cj.netos.oc.wallet.model.BalanceBill;
import cj.netos.oc.wallet.model.BalanceBillExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BalanceBillMapper {

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    long countByExample(BalanceBillExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int deleteByExample(BalanceBillExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int deleteByPrimaryKey(String sn);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int insert(BalanceBill record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int insertSelective(BalanceBill record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    List<BalanceBill> selectByExample(BalanceBillExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    BalanceBill selectByPrimaryKey(String sn);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByExampleSelective(@Param("record") BalanceBill record, @Param("example") BalanceBillExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByExample(@Param("record") BalanceBill record, @Param("example") BalanceBillExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByPrimaryKeySelective(BalanceBill record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByPrimaryKey(BalanceBill record);

    List<BalanceBill> page(@Param(value = "accountid") String accountid, @Param(value = "limit") int limit, @Param(value = "offset") long offset);

    List<BalanceBill> month(@Param(value = "accountid") String accountid, @Param(value = "year") int year, @Param(value = "month") int month, @Param(value = "limit") int limit, @Param(value = "offset") long offset);
}