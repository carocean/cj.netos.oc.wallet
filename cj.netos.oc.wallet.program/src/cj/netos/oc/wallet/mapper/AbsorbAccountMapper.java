package cj.netos.oc.wallet.mapper;

import cj.netos.oc.wallet.model.AbsorbAccount;
import cj.netos.oc.wallet.model.AbsorbAccountExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AbsorbAccountMapper {

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    long countByExample(AbsorbAccountExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int deleteByExample(AbsorbAccountExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int deleteByPrimaryKey(String id);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int insert(AbsorbAccount record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int insertSelective(AbsorbAccount record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    List<AbsorbAccount> selectByExample(AbsorbAccountExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    AbsorbAccount selectByPrimaryKey(String id);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByExampleSelective(@Param("record") AbsorbAccount record, @Param("example") AbsorbAccountExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByExample(@Param("record") AbsorbAccount record, @Param("example") AbsorbAccountExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByPrimaryKeySelective(AbsorbAccount record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByPrimaryKey(AbsorbAccount record);

    void updateAmount(@Param(value = "id") String id, @Param(value = "balance") Long balance, @Param(value = "lutime") String lutime);
}