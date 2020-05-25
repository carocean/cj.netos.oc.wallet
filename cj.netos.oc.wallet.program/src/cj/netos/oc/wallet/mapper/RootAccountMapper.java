package cj.netos.oc.wallet.mapper;

import cj.netos.oc.wallet.model.RootAccount;
import cj.netos.oc.wallet.model.RootAccountExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RootAccountMapper {

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    long countByExample(RootAccountExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int deleteByExample(RootAccountExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int deleteByPrimaryKey(String id);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int insert(RootAccount record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int insertSelective(RootAccount record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    List<RootAccount> selectByExample(RootAccountExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    RootAccount selectByPrimaryKey(String id);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByExampleSelective(@Param("record") RootAccount record, @Param("example") RootAccountExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByExample(@Param("record") RootAccount record, @Param("example") RootAccountExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByPrimaryKeySelective(RootAccount record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByPrimaryKey(RootAccount record);

    void setAmountOnorder(@Param(value = "id") String id, @Param(value = "onorderAmount") Long onorderAmount, @Param(value = "lutime") String lutime);
}