package cj.netos.oc.wallet.mapper;

import cj.netos.oc.wallet.model.FreezenAccount;
import cj.netos.oc.wallet.model.FreezenAccountExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface FreezenAccountMapper {

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    long countByExample(FreezenAccountExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int deleteByExample(FreezenAccountExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int deleteByPrimaryKey(String id);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int insert(FreezenAccount record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int insertSelective(FreezenAccount record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    List<FreezenAccount> selectByExample(FreezenAccountExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    FreezenAccount selectByPrimaryKey(String id);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByExampleSelective(@Param("record") FreezenAccount record, @Param("example") FreezenAccountExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByExample(@Param("record") FreezenAccount record, @Param("example") FreezenAccountExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByPrimaryKeySelective(FreezenAccount record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByPrimaryKey(FreezenAccount record);
}