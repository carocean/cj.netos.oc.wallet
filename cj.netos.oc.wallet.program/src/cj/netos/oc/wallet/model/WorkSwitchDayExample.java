package cj.netos.oc.wallet.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class WorkSwitchDayExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    public WorkSwitchDayExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(String value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(String value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(String value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(String value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(String value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(String value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLike(String value) {
            addCriterion("id like", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotLike(String value) {
            addCriterion("id not like", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<String> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<String> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(String value1, String value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(String value1, String value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andWorkDayIsNull() {
            addCriterion("work_day is null");
            return (Criteria) this;
        }

        public Criteria andWorkDayIsNotNull() {
            addCriterion("work_day is not null");
            return (Criteria) this;
        }

        public Criteria andWorkDayEqualTo(String value) {
            addCriterion("work_day =", value, "workDay");
            return (Criteria) this;
        }

        public Criteria andWorkDayNotEqualTo(String value) {
            addCriterion("work_day <>", value, "workDay");
            return (Criteria) this;
        }

        public Criteria andWorkDayGreaterThan(String value) {
            addCriterion("work_day >", value, "workDay");
            return (Criteria) this;
        }

        public Criteria andWorkDayGreaterThanOrEqualTo(String value) {
            addCriterion("work_day >=", value, "workDay");
            return (Criteria) this;
        }

        public Criteria andWorkDayLessThan(String value) {
            addCriterion("work_day <", value, "workDay");
            return (Criteria) this;
        }

        public Criteria andWorkDayLessThanOrEqualTo(String value) {
            addCriterion("work_day <=", value, "workDay");
            return (Criteria) this;
        }

        public Criteria andWorkDayLike(String value) {
            addCriterion("work_day like", value, "workDay");
            return (Criteria) this;
        }

        public Criteria andWorkDayNotLike(String value) {
            addCriterion("work_day not like", value, "workDay");
            return (Criteria) this;
        }

        public Criteria andWorkDayIn(List<String> values) {
            addCriterion("work_day in", values, "workDay");
            return (Criteria) this;
        }

        public Criteria andWorkDayNotIn(List<String> values) {
            addCriterion("work_day not in", values, "workDay");
            return (Criteria) this;
        }

        public Criteria andWorkDayBetween(String value1, String value2) {
            addCriterion("work_day between", value1, value2, "workDay");
            return (Criteria) this;
        }

        public Criteria andWorkDayNotBetween(String value1, String value2) {
            addCriterion("work_day not between", value1, value2, "workDay");
            return (Criteria) this;
        }

        public Criteria andPreAmountIsNull() {
            addCriterion("pre_amount is null");
            return (Criteria) this;
        }

        public Criteria andPreAmountIsNotNull() {
            addCriterion("pre_amount is not null");
            return (Criteria) this;
        }

        public Criteria andPreAmountEqualTo(Long value) {
            addCriterion("pre_amount =", value, "preAmount");
            return (Criteria) this;
        }

        public Criteria andPreAmountNotEqualTo(Long value) {
            addCriterion("pre_amount <>", value, "preAmount");
            return (Criteria) this;
        }

        public Criteria andPreAmountGreaterThan(Long value) {
            addCriterion("pre_amount >", value, "preAmount");
            return (Criteria) this;
        }

        public Criteria andPreAmountGreaterThanOrEqualTo(Long value) {
            addCriterion("pre_amount >=", value, "preAmount");
            return (Criteria) this;
        }

        public Criteria andPreAmountLessThan(Long value) {
            addCriterion("pre_amount <", value, "preAmount");
            return (Criteria) this;
        }

        public Criteria andPreAmountLessThanOrEqualTo(Long value) {
            addCriterion("pre_amount <=", value, "preAmount");
            return (Criteria) this;
        }

        public Criteria andPreAmountIn(List<Long> values) {
            addCriterion("pre_amount in", values, "preAmount");
            return (Criteria) this;
        }

        public Criteria andPreAmountNotIn(List<Long> values) {
            addCriterion("pre_amount not in", values, "preAmount");
            return (Criteria) this;
        }

        public Criteria andPreAmountBetween(Long value1, Long value2) {
            addCriterion("pre_amount between", value1, value2, "preAmount");
            return (Criteria) this;
        }

        public Criteria andPreAmountNotBetween(Long value1, Long value2) {
            addCriterion("pre_amount not between", value1, value2, "preAmount");
            return (Criteria) this;
        }

        public Criteria andUnckeckAmountIsNull() {
            addCriterion("unckeck_amount is null");
            return (Criteria) this;
        }

        public Criteria andUnckeckAmountIsNotNull() {
            addCriterion("unckeck_amount is not null");
            return (Criteria) this;
        }

        public Criteria andUnckeckAmountEqualTo(Long value) {
            addCriterion("unckeck_amount =", value, "unckeckAmount");
            return (Criteria) this;
        }

        public Criteria andUnckeckAmountNotEqualTo(Long value) {
            addCriterion("unckeck_amount <>", value, "unckeckAmount");
            return (Criteria) this;
        }

        public Criteria andUnckeckAmountGreaterThan(Long value) {
            addCriterion("unckeck_amount >", value, "unckeckAmount");
            return (Criteria) this;
        }

        public Criteria andUnckeckAmountGreaterThanOrEqualTo(Long value) {
            addCriterion("unckeck_amount >=", value, "unckeckAmount");
            return (Criteria) this;
        }

        public Criteria andUnckeckAmountLessThan(Long value) {
            addCriterion("unckeck_amount <", value, "unckeckAmount");
            return (Criteria) this;
        }

        public Criteria andUnckeckAmountLessThanOrEqualTo(Long value) {
            addCriterion("unckeck_amount <=", value, "unckeckAmount");
            return (Criteria) this;
        }

        public Criteria andUnckeckAmountIn(List<Long> values) {
            addCriterion("unckeck_amount in", values, "unckeckAmount");
            return (Criteria) this;
        }

        public Criteria andUnckeckAmountNotIn(List<Long> values) {
            addCriterion("unckeck_amount not in", values, "unckeckAmount");
            return (Criteria) this;
        }

        public Criteria andUnckeckAmountBetween(Long value1, Long value2) {
            addCriterion("unckeck_amount between", value1, value2, "unckeckAmount");
            return (Criteria) this;
        }

        public Criteria andUnckeckAmountNotBetween(Long value1, Long value2) {
            addCriterion("unckeck_amount not between", value1, value2, "unckeckAmount");
            return (Criteria) this;
        }

        public Criteria andCheckedAmountIsNull() {
            addCriterion("checked_amount is null");
            return (Criteria) this;
        }

        public Criteria andCheckedAmountIsNotNull() {
            addCriterion("checked_amount is not null");
            return (Criteria) this;
        }

        public Criteria andCheckedAmountEqualTo(Long value) {
            addCriterion("checked_amount =", value, "checkedAmount");
            return (Criteria) this;
        }

        public Criteria andCheckedAmountNotEqualTo(Long value) {
            addCriterion("checked_amount <>", value, "checkedAmount");
            return (Criteria) this;
        }

        public Criteria andCheckedAmountGreaterThan(Long value) {
            addCriterion("checked_amount >", value, "checkedAmount");
            return (Criteria) this;
        }

        public Criteria andCheckedAmountGreaterThanOrEqualTo(Long value) {
            addCriterion("checked_amount >=", value, "checkedAmount");
            return (Criteria) this;
        }

        public Criteria andCheckedAmountLessThan(Long value) {
            addCriterion("checked_amount <", value, "checkedAmount");
            return (Criteria) this;
        }

        public Criteria andCheckedAmountLessThanOrEqualTo(Long value) {
            addCriterion("checked_amount <=", value, "checkedAmount");
            return (Criteria) this;
        }

        public Criteria andCheckedAmountIn(List<Long> values) {
            addCriterion("checked_amount in", values, "checkedAmount");
            return (Criteria) this;
        }

        public Criteria andCheckedAmountNotIn(List<Long> values) {
            addCriterion("checked_amount not in", values, "checkedAmount");
            return (Criteria) this;
        }

        public Criteria andCheckedAmountBetween(Long value1, Long value2) {
            addCriterion("checked_amount between", value1, value2, "checkedAmount");
            return (Criteria) this;
        }

        public Criteria andCheckedAmountNotBetween(Long value1, Long value2) {
            addCriterion("checked_amount not between", value1, value2, "checkedAmount");
            return (Criteria) this;
        }

        public Criteria andPreFreezenAmountIsNull() {
            addCriterion("pre_freezen_amount is null");
            return (Criteria) this;
        }

        public Criteria andPreFreezenAmountIsNotNull() {
            addCriterion("pre_freezen_amount is not null");
            return (Criteria) this;
        }

        public Criteria andPreFreezenAmountEqualTo(Long value) {
            addCriterion("pre_freezen_amount =", value, "preFreezenAmount");
            return (Criteria) this;
        }

        public Criteria andPreFreezenAmountNotEqualTo(Long value) {
            addCriterion("pre_freezen_amount <>", value, "preFreezenAmount");
            return (Criteria) this;
        }

        public Criteria andPreFreezenAmountGreaterThan(Long value) {
            addCriterion("pre_freezen_amount >", value, "preFreezenAmount");
            return (Criteria) this;
        }

        public Criteria andPreFreezenAmountGreaterThanOrEqualTo(Long value) {
            addCriterion("pre_freezen_amount >=", value, "preFreezenAmount");
            return (Criteria) this;
        }

        public Criteria andPreFreezenAmountLessThan(Long value) {
            addCriterion("pre_freezen_amount <", value, "preFreezenAmount");
            return (Criteria) this;
        }

        public Criteria andPreFreezenAmountLessThanOrEqualTo(Long value) {
            addCriterion("pre_freezen_amount <=", value, "preFreezenAmount");
            return (Criteria) this;
        }

        public Criteria andPreFreezenAmountIn(List<Long> values) {
            addCriterion("pre_freezen_amount in", values, "preFreezenAmount");
            return (Criteria) this;
        }

        public Criteria andPreFreezenAmountNotIn(List<Long> values) {
            addCriterion("pre_freezen_amount not in", values, "preFreezenAmount");
            return (Criteria) this;
        }

        public Criteria andPreFreezenAmountBetween(Long value1, Long value2) {
            addCriterion("pre_freezen_amount between", value1, value2, "preFreezenAmount");
            return (Criteria) this;
        }

        public Criteria andPreFreezenAmountNotBetween(Long value1, Long value2) {
            addCriterion("pre_freezen_amount not between", value1, value2, "preFreezenAmount");
            return (Criteria) this;
        }

        public Criteria andUnckeckFreezenAmountIsNull() {
            addCriterion("unckeck_freezen_amount is null");
            return (Criteria) this;
        }

        public Criteria andUnckeckFreezenAmountIsNotNull() {
            addCriterion("unckeck_freezen_amount is not null");
            return (Criteria) this;
        }

        public Criteria andUnckeckFreezenAmountEqualTo(Long value) {
            addCriterion("unckeck_freezen_amount =", value, "unckeckFreezenAmount");
            return (Criteria) this;
        }

        public Criteria andUnckeckFreezenAmountNotEqualTo(Long value) {
            addCriterion("unckeck_freezen_amount <>", value, "unckeckFreezenAmount");
            return (Criteria) this;
        }

        public Criteria andUnckeckFreezenAmountGreaterThan(Long value) {
            addCriterion("unckeck_freezen_amount >", value, "unckeckFreezenAmount");
            return (Criteria) this;
        }

        public Criteria andUnckeckFreezenAmountGreaterThanOrEqualTo(Long value) {
            addCriterion("unckeck_freezen_amount >=", value, "unckeckFreezenAmount");
            return (Criteria) this;
        }

        public Criteria andUnckeckFreezenAmountLessThan(Long value) {
            addCriterion("unckeck_freezen_amount <", value, "unckeckFreezenAmount");
            return (Criteria) this;
        }

        public Criteria andUnckeckFreezenAmountLessThanOrEqualTo(Long value) {
            addCriterion("unckeck_freezen_amount <=", value, "unckeckFreezenAmount");
            return (Criteria) this;
        }

        public Criteria andUnckeckFreezenAmountIn(List<Long> values) {
            addCriterion("unckeck_freezen_amount in", values, "unckeckFreezenAmount");
            return (Criteria) this;
        }

        public Criteria andUnckeckFreezenAmountNotIn(List<Long> values) {
            addCriterion("unckeck_freezen_amount not in", values, "unckeckFreezenAmount");
            return (Criteria) this;
        }

        public Criteria andUnckeckFreezenAmountBetween(Long value1, Long value2) {
            addCriterion("unckeck_freezen_amount between", value1, value2, "unckeckFreezenAmount");
            return (Criteria) this;
        }

        public Criteria andUnckeckFreezenAmountNotBetween(Long value1, Long value2) {
            addCriterion("unckeck_freezen_amount not between", value1, value2, "unckeckFreezenAmount");
            return (Criteria) this;
        }

        public Criteria andCheckedFreezenAmountIsNull() {
            addCriterion("checked_freezen_amount is null");
            return (Criteria) this;
        }

        public Criteria andCheckedFreezenAmountIsNotNull() {
            addCriterion("checked_freezen_amount is not null");
            return (Criteria) this;
        }

        public Criteria andCheckedFreezenAmountEqualTo(Long value) {
            addCriterion("checked_freezen_amount =", value, "checkedFreezenAmount");
            return (Criteria) this;
        }

        public Criteria andCheckedFreezenAmountNotEqualTo(Long value) {
            addCriterion("checked_freezen_amount <>", value, "checkedFreezenAmount");
            return (Criteria) this;
        }

        public Criteria andCheckedFreezenAmountGreaterThan(Long value) {
            addCriterion("checked_freezen_amount >", value, "checkedFreezenAmount");
            return (Criteria) this;
        }

        public Criteria andCheckedFreezenAmountGreaterThanOrEqualTo(Long value) {
            addCriterion("checked_freezen_amount >=", value, "checkedFreezenAmount");
            return (Criteria) this;
        }

        public Criteria andCheckedFreezenAmountLessThan(Long value) {
            addCriterion("checked_freezen_amount <", value, "checkedFreezenAmount");
            return (Criteria) this;
        }

        public Criteria andCheckedFreezenAmountLessThanOrEqualTo(Long value) {
            addCriterion("checked_freezen_amount <=", value, "checkedFreezenAmount");
            return (Criteria) this;
        }

        public Criteria andCheckedFreezenAmountIn(List<Long> values) {
            addCriterion("checked_freezen_amount in", values, "checkedFreezenAmount");
            return (Criteria) this;
        }

        public Criteria andCheckedFreezenAmountNotIn(List<Long> values) {
            addCriterion("checked_freezen_amount not in", values, "checkedFreezenAmount");
            return (Criteria) this;
        }

        public Criteria andCheckedFreezenAmountBetween(Long value1, Long value2) {
            addCriterion("checked_freezen_amount between", value1, value2, "checkedFreezenAmount");
            return (Criteria) this;
        }

        public Criteria andCheckedFreezenAmountNotBetween(Long value1, Long value2) {
            addCriterion("checked_freezen_amount not between", value1, value2, "checkedFreezenAmount");
            return (Criteria) this;
        }

        public Criteria andPreProfitAmountIsNull() {
            addCriterion("pre_profit_amount is null");
            return (Criteria) this;
        }

        public Criteria andPreProfitAmountIsNotNull() {
            addCriterion("pre_profit_amount is not null");
            return (Criteria) this;
        }

        public Criteria andPreProfitAmountEqualTo(Long value) {
            addCriterion("pre_profit_amount =", value, "preProfitAmount");
            return (Criteria) this;
        }

        public Criteria andPreProfitAmountNotEqualTo(Long value) {
            addCriterion("pre_profit_amount <>", value, "preProfitAmount");
            return (Criteria) this;
        }

        public Criteria andPreProfitAmountGreaterThan(Long value) {
            addCriterion("pre_profit_amount >", value, "preProfitAmount");
            return (Criteria) this;
        }

        public Criteria andPreProfitAmountGreaterThanOrEqualTo(Long value) {
            addCriterion("pre_profit_amount >=", value, "preProfitAmount");
            return (Criteria) this;
        }

        public Criteria andPreProfitAmountLessThan(Long value) {
            addCriterion("pre_profit_amount <", value, "preProfitAmount");
            return (Criteria) this;
        }

        public Criteria andPreProfitAmountLessThanOrEqualTo(Long value) {
            addCriterion("pre_profit_amount <=", value, "preProfitAmount");
            return (Criteria) this;
        }

        public Criteria andPreProfitAmountIn(List<Long> values) {
            addCriterion("pre_profit_amount in", values, "preProfitAmount");
            return (Criteria) this;
        }

        public Criteria andPreProfitAmountNotIn(List<Long> values) {
            addCriterion("pre_profit_amount not in", values, "preProfitAmount");
            return (Criteria) this;
        }

        public Criteria andPreProfitAmountBetween(Long value1, Long value2) {
            addCriterion("pre_profit_amount between", value1, value2, "preProfitAmount");
            return (Criteria) this;
        }

        public Criteria andPreProfitAmountNotBetween(Long value1, Long value2) {
            addCriterion("pre_profit_amount not between", value1, value2, "preProfitAmount");
            return (Criteria) this;
        }

        public Criteria andUnckechProfitAmountIsNull() {
            addCriterion("unckech_profit_amount is null");
            return (Criteria) this;
        }

        public Criteria andUnckechProfitAmountIsNotNull() {
            addCriterion("unckech_profit_amount is not null");
            return (Criteria) this;
        }

        public Criteria andUnckechProfitAmountEqualTo(Long value) {
            addCriterion("unckech_profit_amount =", value, "unckechProfitAmount");
            return (Criteria) this;
        }

        public Criteria andUnckechProfitAmountNotEqualTo(Long value) {
            addCriterion("unckech_profit_amount <>", value, "unckechProfitAmount");
            return (Criteria) this;
        }

        public Criteria andUnckechProfitAmountGreaterThan(Long value) {
            addCriterion("unckech_profit_amount >", value, "unckechProfitAmount");
            return (Criteria) this;
        }

        public Criteria andUnckechProfitAmountGreaterThanOrEqualTo(Long value) {
            addCriterion("unckech_profit_amount >=", value, "unckechProfitAmount");
            return (Criteria) this;
        }

        public Criteria andUnckechProfitAmountLessThan(Long value) {
            addCriterion("unckech_profit_amount <", value, "unckechProfitAmount");
            return (Criteria) this;
        }

        public Criteria andUnckechProfitAmountLessThanOrEqualTo(Long value) {
            addCriterion("unckech_profit_amount <=", value, "unckechProfitAmount");
            return (Criteria) this;
        }

        public Criteria andUnckechProfitAmountIn(List<Long> values) {
            addCriterion("unckech_profit_amount in", values, "unckechProfitAmount");
            return (Criteria) this;
        }

        public Criteria andUnckechProfitAmountNotIn(List<Long> values) {
            addCriterion("unckech_profit_amount not in", values, "unckechProfitAmount");
            return (Criteria) this;
        }

        public Criteria andUnckechProfitAmountBetween(Long value1, Long value2) {
            addCriterion("unckech_profit_amount between", value1, value2, "unckechProfitAmount");
            return (Criteria) this;
        }

        public Criteria andUnckechProfitAmountNotBetween(Long value1, Long value2) {
            addCriterion("unckech_profit_amount not between", value1, value2, "unckechProfitAmount");
            return (Criteria) this;
        }

        public Criteria andCheckedProfitAmountIsNull() {
            addCriterion("checked_profit_amount is null");
            return (Criteria) this;
        }

        public Criteria andCheckedProfitAmountIsNotNull() {
            addCriterion("checked_profit_amount is not null");
            return (Criteria) this;
        }

        public Criteria andCheckedProfitAmountEqualTo(Long value) {
            addCriterion("checked_profit_amount =", value, "checkedProfitAmount");
            return (Criteria) this;
        }

        public Criteria andCheckedProfitAmountNotEqualTo(Long value) {
            addCriterion("checked_profit_amount <>", value, "checkedProfitAmount");
            return (Criteria) this;
        }

        public Criteria andCheckedProfitAmountGreaterThan(Long value) {
            addCriterion("checked_profit_amount >", value, "checkedProfitAmount");
            return (Criteria) this;
        }

        public Criteria andCheckedProfitAmountGreaterThanOrEqualTo(Long value) {
            addCriterion("checked_profit_amount >=", value, "checkedProfitAmount");
            return (Criteria) this;
        }

        public Criteria andCheckedProfitAmountLessThan(Long value) {
            addCriterion("checked_profit_amount <", value, "checkedProfitAmount");
            return (Criteria) this;
        }

        public Criteria andCheckedProfitAmountLessThanOrEqualTo(Long value) {
            addCriterion("checked_profit_amount <=", value, "checkedProfitAmount");
            return (Criteria) this;
        }

        public Criteria andCheckedProfitAmountIn(List<Long> values) {
            addCriterion("checked_profit_amount in", values, "checkedProfitAmount");
            return (Criteria) this;
        }

        public Criteria andCheckedProfitAmountNotIn(List<Long> values) {
            addCriterion("checked_profit_amount not in", values, "checkedProfitAmount");
            return (Criteria) this;
        }

        public Criteria andCheckedProfitAmountBetween(Long value1, Long value2) {
            addCriterion("checked_profit_amount between", value1, value2, "checkedProfitAmount");
            return (Criteria) this;
        }

        public Criteria andCheckedProfitAmountNotBetween(Long value1, Long value2) {
            addCriterion("checked_profit_amount not between", value1, value2, "checkedProfitAmount");
            return (Criteria) this;
        }

        public Criteria andPreYinjAmountIsNull() {
            addCriterion("pre_yinj_amount is null");
            return (Criteria) this;
        }

        public Criteria andPreYinjAmountIsNotNull() {
            addCriterion("pre_yinj_amount is not null");
            return (Criteria) this;
        }

        public Criteria andPreYinjAmountEqualTo(Long value) {
            addCriterion("pre_yinj_amount =", value, "preYinjAmount");
            return (Criteria) this;
        }

        public Criteria andPreYinjAmountNotEqualTo(Long value) {
            addCriterion("pre_yinj_amount <>", value, "preYinjAmount");
            return (Criteria) this;
        }

        public Criteria andPreYinjAmountGreaterThan(Long value) {
            addCriterion("pre_yinj_amount >", value, "preYinjAmount");
            return (Criteria) this;
        }

        public Criteria andPreYinjAmountGreaterThanOrEqualTo(Long value) {
            addCriterion("pre_yinj_amount >=", value, "preYinjAmount");
            return (Criteria) this;
        }

        public Criteria andPreYinjAmountLessThan(Long value) {
            addCriterion("pre_yinj_amount <", value, "preYinjAmount");
            return (Criteria) this;
        }

        public Criteria andPreYinjAmountLessThanOrEqualTo(Long value) {
            addCriterion("pre_yinj_amount <=", value, "preYinjAmount");
            return (Criteria) this;
        }

        public Criteria andPreYinjAmountIn(List<Long> values) {
            addCriterion("pre_yinj_amount in", values, "preYinjAmount");
            return (Criteria) this;
        }

        public Criteria andPreYinjAmountNotIn(List<Long> values) {
            addCriterion("pre_yinj_amount not in", values, "preYinjAmount");
            return (Criteria) this;
        }

        public Criteria andPreYinjAmountBetween(Long value1, Long value2) {
            addCriterion("pre_yinj_amount between", value1, value2, "preYinjAmount");
            return (Criteria) this;
        }

        public Criteria andPreYinjAmountNotBetween(Long value1, Long value2) {
            addCriterion("pre_yinj_amount not between", value1, value2, "preYinjAmount");
            return (Criteria) this;
        }

        public Criteria andUncheckYinjAmountIsNull() {
            addCriterion("uncheck_yinj_amount is null");
            return (Criteria) this;
        }

        public Criteria andUncheckYinjAmountIsNotNull() {
            addCriterion("uncheck_yinj_amount is not null");
            return (Criteria) this;
        }

        public Criteria andUncheckYinjAmountEqualTo(Long value) {
            addCriterion("uncheck_yinj_amount =", value, "uncheckYinjAmount");
            return (Criteria) this;
        }

        public Criteria andUncheckYinjAmountNotEqualTo(Long value) {
            addCriterion("uncheck_yinj_amount <>", value, "uncheckYinjAmount");
            return (Criteria) this;
        }

        public Criteria andUncheckYinjAmountGreaterThan(Long value) {
            addCriterion("uncheck_yinj_amount >", value, "uncheckYinjAmount");
            return (Criteria) this;
        }

        public Criteria andUncheckYinjAmountGreaterThanOrEqualTo(Long value) {
            addCriterion("uncheck_yinj_amount >=", value, "uncheckYinjAmount");
            return (Criteria) this;
        }

        public Criteria andUncheckYinjAmountLessThan(Long value) {
            addCriterion("uncheck_yinj_amount <", value, "uncheckYinjAmount");
            return (Criteria) this;
        }

        public Criteria andUncheckYinjAmountLessThanOrEqualTo(Long value) {
            addCriterion("uncheck_yinj_amount <=", value, "uncheckYinjAmount");
            return (Criteria) this;
        }

        public Criteria andUncheckYinjAmountIn(List<Long> values) {
            addCriterion("uncheck_yinj_amount in", values, "uncheckYinjAmount");
            return (Criteria) this;
        }

        public Criteria andUncheckYinjAmountNotIn(List<Long> values) {
            addCriterion("uncheck_yinj_amount not in", values, "uncheckYinjAmount");
            return (Criteria) this;
        }

        public Criteria andUncheckYinjAmountBetween(Long value1, Long value2) {
            addCriterion("uncheck_yinj_amount between", value1, value2, "uncheckYinjAmount");
            return (Criteria) this;
        }

        public Criteria andUncheckYinjAmountNotBetween(Long value1, Long value2) {
            addCriterion("uncheck_yinj_amount not between", value1, value2, "uncheckYinjAmount");
            return (Criteria) this;
        }

        public Criteria andCheckedYinjAmountIsNull() {
            addCriterion("checked_yinj_amount is null");
            return (Criteria) this;
        }

        public Criteria andCheckedYinjAmountIsNotNull() {
            addCriterion("checked_yinj_amount is not null");
            return (Criteria) this;
        }

        public Criteria andCheckedYinjAmountEqualTo(Long value) {
            addCriterion("checked_yinj_amount =", value, "checkedYinjAmount");
            return (Criteria) this;
        }

        public Criteria andCheckedYinjAmountNotEqualTo(Long value) {
            addCriterion("checked_yinj_amount <>", value, "checkedYinjAmount");
            return (Criteria) this;
        }

        public Criteria andCheckedYinjAmountGreaterThan(Long value) {
            addCriterion("checked_yinj_amount >", value, "checkedYinjAmount");
            return (Criteria) this;
        }

        public Criteria andCheckedYinjAmountGreaterThanOrEqualTo(Long value) {
            addCriterion("checked_yinj_amount >=", value, "checkedYinjAmount");
            return (Criteria) this;
        }

        public Criteria andCheckedYinjAmountLessThan(Long value) {
            addCriterion("checked_yinj_amount <", value, "checkedYinjAmount");
            return (Criteria) this;
        }

        public Criteria andCheckedYinjAmountLessThanOrEqualTo(Long value) {
            addCriterion("checked_yinj_amount <=", value, "checkedYinjAmount");
            return (Criteria) this;
        }

        public Criteria andCheckedYinjAmountIn(List<Long> values) {
            addCriterion("checked_yinj_amount in", values, "checkedYinjAmount");
            return (Criteria) this;
        }

        public Criteria andCheckedYinjAmountNotIn(List<Long> values) {
            addCriterion("checked_yinj_amount not in", values, "checkedYinjAmount");
            return (Criteria) this;
        }

        public Criteria andCheckedYinjAmountBetween(Long value1, Long value2) {
            addCriterion("checked_yinj_amount between", value1, value2, "checkedYinjAmount");
            return (Criteria) this;
        }

        public Criteria andCheckedYinjAmountNotBetween(Long value1, Long value2) {
            addCriterion("checked_yinj_amount not between", value1, value2, "checkedYinjAmount");
            return (Criteria) this;
        }

        public Criteria andPreWenyQuantitiesIsNull() {
            addCriterion("pre_weny_quantities is null");
            return (Criteria) this;
        }

        public Criteria andPreWenyQuantitiesIsNotNull() {
            addCriterion("pre_weny_quantities is not null");
            return (Criteria) this;
        }

        public Criteria andPreWenyQuantitiesEqualTo(BigDecimal value) {
            addCriterion("pre_weny_quantities =", value, "preWenyQuantities");
            return (Criteria) this;
        }

        public Criteria andPreWenyQuantitiesNotEqualTo(BigDecimal value) {
            addCriterion("pre_weny_quantities <>", value, "preWenyQuantities");
            return (Criteria) this;
        }

        public Criteria andPreWenyQuantitiesGreaterThan(BigDecimal value) {
            addCriterion("pre_weny_quantities >", value, "preWenyQuantities");
            return (Criteria) this;
        }

        public Criteria andPreWenyQuantitiesGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("pre_weny_quantities >=", value, "preWenyQuantities");
            return (Criteria) this;
        }

        public Criteria andPreWenyQuantitiesLessThan(BigDecimal value) {
            addCriterion("pre_weny_quantities <", value, "preWenyQuantities");
            return (Criteria) this;
        }

        public Criteria andPreWenyQuantitiesLessThanOrEqualTo(BigDecimal value) {
            addCriterion("pre_weny_quantities <=", value, "preWenyQuantities");
            return (Criteria) this;
        }

        public Criteria andPreWenyQuantitiesIn(List<BigDecimal> values) {
            addCriterion("pre_weny_quantities in", values, "preWenyQuantities");
            return (Criteria) this;
        }

        public Criteria andPreWenyQuantitiesNotIn(List<BigDecimal> values) {
            addCriterion("pre_weny_quantities not in", values, "preWenyQuantities");
            return (Criteria) this;
        }

        public Criteria andPreWenyQuantitiesBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("pre_weny_quantities between", value1, value2, "preWenyQuantities");
            return (Criteria) this;
        }

        public Criteria andPreWenyQuantitiesNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("pre_weny_quantities not between", value1, value2, "preWenyQuantities");
            return (Criteria) this;
        }

        public Criteria andUnckeckWenyQuatitiesIsNull() {
            addCriterion("unckeck_weny_quatities is null");
            return (Criteria) this;
        }

        public Criteria andUnckeckWenyQuatitiesIsNotNull() {
            addCriterion("unckeck_weny_quatities is not null");
            return (Criteria) this;
        }

        public Criteria andUnckeckWenyQuatitiesEqualTo(BigDecimal value) {
            addCriterion("unckeck_weny_quatities =", value, "unckeckWenyQuatities");
            return (Criteria) this;
        }

        public Criteria andUnckeckWenyQuatitiesNotEqualTo(BigDecimal value) {
            addCriterion("unckeck_weny_quatities <>", value, "unckeckWenyQuatities");
            return (Criteria) this;
        }

        public Criteria andUnckeckWenyQuatitiesGreaterThan(BigDecimal value) {
            addCriterion("unckeck_weny_quatities >", value, "unckeckWenyQuatities");
            return (Criteria) this;
        }

        public Criteria andUnckeckWenyQuatitiesGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("unckeck_weny_quatities >=", value, "unckeckWenyQuatities");
            return (Criteria) this;
        }

        public Criteria andUnckeckWenyQuatitiesLessThan(BigDecimal value) {
            addCriterion("unckeck_weny_quatities <", value, "unckeckWenyQuatities");
            return (Criteria) this;
        }

        public Criteria andUnckeckWenyQuatitiesLessThanOrEqualTo(BigDecimal value) {
            addCriterion("unckeck_weny_quatities <=", value, "unckeckWenyQuatities");
            return (Criteria) this;
        }

        public Criteria andUnckeckWenyQuatitiesIn(List<BigDecimal> values) {
            addCriterion("unckeck_weny_quatities in", values, "unckeckWenyQuatities");
            return (Criteria) this;
        }

        public Criteria andUnckeckWenyQuatitiesNotIn(List<BigDecimal> values) {
            addCriterion("unckeck_weny_quatities not in", values, "unckeckWenyQuatities");
            return (Criteria) this;
        }

        public Criteria andUnckeckWenyQuatitiesBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("unckeck_weny_quatities between", value1, value2, "unckeckWenyQuatities");
            return (Criteria) this;
        }

        public Criteria andUnckeckWenyQuatitiesNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("unckeck_weny_quatities not between", value1, value2, "unckeckWenyQuatities");
            return (Criteria) this;
        }

        public Criteria andCheckedWenyQuantitiesIsNull() {
            addCriterion("checked_weny_quantities is null");
            return (Criteria) this;
        }

        public Criteria andCheckedWenyQuantitiesIsNotNull() {
            addCriterion("checked_weny_quantities is not null");
            return (Criteria) this;
        }

        public Criteria andCheckedWenyQuantitiesEqualTo(BigDecimal value) {
            addCriterion("checked_weny_quantities =", value, "checkedWenyQuantities");
            return (Criteria) this;
        }

        public Criteria andCheckedWenyQuantitiesNotEqualTo(BigDecimal value) {
            addCriterion("checked_weny_quantities <>", value, "checkedWenyQuantities");
            return (Criteria) this;
        }

        public Criteria andCheckedWenyQuantitiesGreaterThan(BigDecimal value) {
            addCriterion("checked_weny_quantities >", value, "checkedWenyQuantities");
            return (Criteria) this;
        }

        public Criteria andCheckedWenyQuantitiesGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("checked_weny_quantities >=", value, "checkedWenyQuantities");
            return (Criteria) this;
        }

        public Criteria andCheckedWenyQuantitiesLessThan(BigDecimal value) {
            addCriterion("checked_weny_quantities <", value, "checkedWenyQuantities");
            return (Criteria) this;
        }

        public Criteria andCheckedWenyQuantitiesLessThanOrEqualTo(BigDecimal value) {
            addCriterion("checked_weny_quantities <=", value, "checkedWenyQuantities");
            return (Criteria) this;
        }

        public Criteria andCheckedWenyQuantitiesIn(List<BigDecimal> values) {
            addCriterion("checked_weny_quantities in", values, "checkedWenyQuantities");
            return (Criteria) this;
        }

        public Criteria andCheckedWenyQuantitiesNotIn(List<BigDecimal> values) {
            addCriterion("checked_weny_quantities not in", values, "checkedWenyQuantities");
            return (Criteria) this;
        }

        public Criteria andCheckedWenyQuantitiesBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("checked_weny_quantities between", value1, value2, "checkedWenyQuantities");
            return (Criteria) this;
        }

        public Criteria andCheckedWenyQuantitiesNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("checked_weny_quantities not between", value1, value2, "checkedWenyQuantities");
            return (Criteria) this;
        }

        public Criteria andDaySwitchStateIsNull() {
            addCriterion("day_switch_state is null");
            return (Criteria) this;
        }

        public Criteria andDaySwitchStateIsNotNull() {
            addCriterion("day_switch_state is not null");
            return (Criteria) this;
        }

        public Criteria andDaySwitchStateEqualTo(Integer value) {
            addCriterion("day_switch_state =", value, "daySwitchState");
            return (Criteria) this;
        }

        public Criteria andDaySwitchStateNotEqualTo(Integer value) {
            addCriterion("day_switch_state <>", value, "daySwitchState");
            return (Criteria) this;
        }

        public Criteria andDaySwitchStateGreaterThan(Integer value) {
            addCriterion("day_switch_state >", value, "daySwitchState");
            return (Criteria) this;
        }

        public Criteria andDaySwitchStateGreaterThanOrEqualTo(Integer value) {
            addCriterion("day_switch_state >=", value, "daySwitchState");
            return (Criteria) this;
        }

        public Criteria andDaySwitchStateLessThan(Integer value) {
            addCriterion("day_switch_state <", value, "daySwitchState");
            return (Criteria) this;
        }

        public Criteria andDaySwitchStateLessThanOrEqualTo(Integer value) {
            addCriterion("day_switch_state <=", value, "daySwitchState");
            return (Criteria) this;
        }

        public Criteria andDaySwitchStateIn(List<Integer> values) {
            addCriterion("day_switch_state in", values, "daySwitchState");
            return (Criteria) this;
        }

        public Criteria andDaySwitchStateNotIn(List<Integer> values) {
            addCriterion("day_switch_state not in", values, "daySwitchState");
            return (Criteria) this;
        }

        public Criteria andDaySwitchStateBetween(Integer value1, Integer value2) {
            addCriterion("day_switch_state between", value1, value2, "daySwitchState");
            return (Criteria) this;
        }

        public Criteria andDaySwitchStateNotBetween(Integer value1, Integer value2) {
            addCriterion("day_switch_state not between", value1, value2, "daySwitchState");
            return (Criteria) this;
        }

        public Criteria andNoteIsNull() {
            addCriterion("note is null");
            return (Criteria) this;
        }

        public Criteria andNoteIsNotNull() {
            addCriterion("note is not null");
            return (Criteria) this;
        }

        public Criteria andNoteEqualTo(String value) {
            addCriterion("note =", value, "note");
            return (Criteria) this;
        }

        public Criteria andNoteNotEqualTo(String value) {
            addCriterion("note <>", value, "note");
            return (Criteria) this;
        }

        public Criteria andNoteGreaterThan(String value) {
            addCriterion("note >", value, "note");
            return (Criteria) this;
        }

        public Criteria andNoteGreaterThanOrEqualTo(String value) {
            addCriterion("note >=", value, "note");
            return (Criteria) this;
        }

        public Criteria andNoteLessThan(String value) {
            addCriterion("note <", value, "note");
            return (Criteria) this;
        }

        public Criteria andNoteLessThanOrEqualTo(String value) {
            addCriterion("note <=", value, "note");
            return (Criteria) this;
        }

        public Criteria andNoteLike(String value) {
            addCriterion("note like", value, "note");
            return (Criteria) this;
        }

        public Criteria andNoteNotLike(String value) {
            addCriterion("note not like", value, "note");
            return (Criteria) this;
        }

        public Criteria andNoteIn(List<String> values) {
            addCriterion("note in", values, "note");
            return (Criteria) this;
        }

        public Criteria andNoteNotIn(List<String> values) {
            addCriterion("note not in", values, "note");
            return (Criteria) this;
        }

        public Criteria andNoteBetween(String value1, String value2) {
            addCriterion("note between", value1, value2, "note");
            return (Criteria) this;
        }

        public Criteria andNoteNotBetween(String value1, String value2) {
            addCriterion("note not between", value1, value2, "note");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}