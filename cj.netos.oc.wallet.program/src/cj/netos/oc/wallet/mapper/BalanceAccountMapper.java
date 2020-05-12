package cj.netos.oc.wallet.mapper;

import cj.netos.oc.wallet.model.BalanceAccount;
import cj.netos.oc.wallet.model.BalanceAccountExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BalanceAccountMapper {

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    long countByExample(BalanceAccountExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int deleteByExample(BalanceAccountExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int deleteByPrimaryKey(String id);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int insert(BalanceAccount record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int insertSelective(BalanceAccount record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    List<BalanceAccount> selectByExample(BalanceAccountExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    BalanceAccount selectByPrimaryKey(String id);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByExampleSelective(@Param("record") BalanceAccount record, @Param("example") BalanceAccountExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByExample(@Param("record") BalanceAccount record, @Param("example") BalanceAccountExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByPrimaryKeySelective(BalanceAccount record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByPrimaryKey(BalanceAccount record);

    void updateAmount(@Param(value = "id") String id, @Param(value = "balance") Long balance, @Param(value = "lutime") String lutime);
}