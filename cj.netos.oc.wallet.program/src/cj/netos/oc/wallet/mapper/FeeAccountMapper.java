package cj.netos.oc.wallet.mapper;

import cj.netos.oc.wallet.model.FeeAccount;
import cj.netos.oc.wallet.model.FeeAccountExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface FeeAccountMapper {

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    long countByExample(FeeAccountExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int deleteByExample(FeeAccountExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int deleteByPrimaryKey(String id);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int insert(FeeAccount record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int insertSelective(FeeAccount record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    List<FeeAccount> selectByExample(FeeAccountExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    FeeAccount selectByPrimaryKey(String id);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByExampleSelective(@Param("record") FeeAccount record, @Param("example") FeeAccountExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByExample(@Param("record") FeeAccount record, @Param("example") FeeAccountExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByPrimaryKeySelective(FeeAccount record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByPrimaryKey(FeeAccount record);

    void updateAmount(@Param(value = "id") String id, @Param(value = "amount") Long amount, @Param(value = "lutime") String lutime);
}