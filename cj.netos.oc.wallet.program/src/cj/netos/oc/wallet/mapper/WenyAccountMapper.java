package cj.netos.oc.wallet.mapper;

import cj.netos.oc.wallet.model.WenyAccount;
import cj.netos.oc.wallet.model.WenyAccountExample;

import java.math.BigDecimal;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface WenyAccountMapper {

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    long countByExample(WenyAccountExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int deleteByExample(WenyAccountExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int deleteByPrimaryKey(String id);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int insert(WenyAccount record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int insertSelective(WenyAccount record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    List<WenyAccount> selectByExample(WenyAccountExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    WenyAccount selectByPrimaryKey(String id);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByExampleSelective(@Param("record") WenyAccount record, @Param("example") WenyAccountExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByExample(@Param("record") WenyAccount record, @Param("example") WenyAccountExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByPrimaryKeySelective(WenyAccount record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByPrimaryKey(WenyAccount record);

    void updateStock(@Param(value = "id") String id,@Param(value = "stock") BigDecimal stock,@Param(value = "lutime") String lutime);

}