package cj.netos.oc.wallet.mapper;

import cj.netos.oc.wallet.model.TrialAccount;
import cj.netos.oc.wallet.model.TrialAccountExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TrialAccountMapper {

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    long countByExample(TrialAccountExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int deleteByExample(TrialAccountExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int deleteByPrimaryKey(String id);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int insert(TrialAccount record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int insertSelective(TrialAccount record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    List<TrialAccount> selectByExample(TrialAccountExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    TrialAccount selectByPrimaryKey(String id);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByExampleSelective(@Param("record") TrialAccount record, @Param("example") TrialAccountExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByExample(@Param("record") TrialAccount record, @Param("example") TrialAccountExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByPrimaryKeySelective(TrialAccount record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByPrimaryKey(TrialAccount record);


    void updateAmount(@Param(value = "id") String id, @Param(value = "amount") long amount, @Param(value = "lutime") String lutime);

}