package cj.netos.oc.wallet.mapper;

import cj.netos.oc.wallet.model.ProfitAccount;
import cj.netos.oc.wallet.model.ProfitAccountExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ProfitAccountMapper {

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    long countByExample(ProfitAccountExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int deleteByExample(ProfitAccountExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int deleteByPrimaryKey(String id);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int insert(ProfitAccount record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int insertSelective(ProfitAccount record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    List<ProfitAccount> selectByExample(ProfitAccountExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    ProfitAccount selectByPrimaryKey(String id);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByExampleSelective(@Param("record") ProfitAccount record, @Param("example") ProfitAccountExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByExample(@Param("record") ProfitAccount record, @Param("example") ProfitAccountExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByPrimaryKeySelective(ProfitAccount record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByPrimaryKey(ProfitAccount record);
}