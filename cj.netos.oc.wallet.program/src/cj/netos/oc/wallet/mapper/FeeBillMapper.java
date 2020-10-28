package cj.netos.oc.wallet.mapper;

import cj.netos.oc.wallet.model.FeeBill;
import cj.netos.oc.wallet.model.FeeBillExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface FeeBillMapper {

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    long countByExample(FeeBillExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int deleteByExample(FeeBillExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int deleteByPrimaryKey(String sn);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int insert(FeeBill record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int insertSelective(FeeBill record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    List<FeeBill> selectByExample(FeeBillExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    FeeBill selectByPrimaryKey(String sn);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByExampleSelective(@Param("record") FeeBill record, @Param("example") FeeBillExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByExample(@Param("record") FeeBill record, @Param("example") FeeBillExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByPrimaryKeySelective(FeeBill record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByPrimaryKey(FeeBill record);
}