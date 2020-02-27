package alipay.manage.bean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserFundExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public UserFundExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

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

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNull() {
            addCriterion("userId is null");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNotNull() {
            addCriterion("userId is not null");
            return (Criteria) this;
        }

        public Criteria andUserIdEqualTo(String value) {
            addCriterion("userId =", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotEqualTo(String value) {
            addCriterion("userId <>", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThan(String value) {
            addCriterion("userId >", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThanOrEqualTo(String value) {
            addCriterion("userId >=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThan(String value) {
            addCriterion("userId <", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThanOrEqualTo(String value) {
            addCriterion("userId <=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLike(String value) {
            addCriterion("userId like", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotLike(String value) {
            addCriterion("userId not like", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdIn(List<String> values) {
            addCriterion("userId in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotIn(List<String> values) {
            addCriterion("userId not in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdBetween(String value1, String value2) {
            addCriterion("userId between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotBetween(String value1, String value2) {
            addCriterion("userId not between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andUserNameIsNull() {
            addCriterion("userName is null");
            return (Criteria) this;
        }

        public Criteria andUserNameIsNotNull() {
            addCriterion("userName is not null");
            return (Criteria) this;
        }

        public Criteria andUserNameEqualTo(String value) {
            addCriterion("userName =", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameNotEqualTo(String value) {
            addCriterion("userName <>", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameGreaterThan(String value) {
            addCriterion("userName >", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameGreaterThanOrEqualTo(String value) {
            addCriterion("userName >=", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameLessThan(String value) {
            addCriterion("userName <", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameLessThanOrEqualTo(String value) {
            addCriterion("userName <=", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameLike(String value) {
            addCriterion("userName like", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameNotLike(String value) {
            addCriterion("userName not like", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameIn(List<String> values) {
            addCriterion("userName in", values, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameNotIn(List<String> values) {
            addCriterion("userName not in", values, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameBetween(String value1, String value2) {
            addCriterion("userName between", value1, value2, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameNotBetween(String value1, String value2) {
            addCriterion("userName not between", value1, value2, "userName");
            return (Criteria) this;
        }

        public Criteria andCashBalanceIsNull() {
            addCriterion("cashBalance is null");
            return (Criteria) this;
        }

        public Criteria andCashBalanceIsNotNull() {
            addCriterion("cashBalance is not null");
            return (Criteria) this;
        }

        public Criteria andCashBalanceEqualTo(BigDecimal value) {
            addCriterion("cashBalance =", value, "cashBalance");
            return (Criteria) this;
        }

        public Criteria andCashBalanceNotEqualTo(BigDecimal value) {
            addCriterion("cashBalance <>", value, "cashBalance");
            return (Criteria) this;
        }

        public Criteria andCashBalanceGreaterThan(BigDecimal value) {
            addCriterion("cashBalance >", value, "cashBalance");
            return (Criteria) this;
        }

        public Criteria andCashBalanceGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("cashBalance >=", value, "cashBalance");
            return (Criteria) this;
        }

        public Criteria andCashBalanceLessThan(BigDecimal value) {
            addCriterion("cashBalance <", value, "cashBalance");
            return (Criteria) this;
        }

        public Criteria andCashBalanceLessThanOrEqualTo(BigDecimal value) {
            addCriterion("cashBalance <=", value, "cashBalance");
            return (Criteria) this;
        }

        public Criteria andCashBalanceIn(List<BigDecimal> values) {
            addCriterion("cashBalance in", values, "cashBalance");
            return (Criteria) this;
        }

        public Criteria andCashBalanceNotIn(List<BigDecimal> values) {
            addCriterion("cashBalance not in", values, "cashBalance");
            return (Criteria) this;
        }

        public Criteria andCashBalanceBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("cashBalance between", value1, value2, "cashBalance");
            return (Criteria) this;
        }

        public Criteria andCashBalanceNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("cashBalance not between", value1, value2, "cashBalance");
            return (Criteria) this;
        }

        public Criteria andRechargeNumberIsNull() {
            addCriterion("rechargeNumber is null");
            return (Criteria) this;
        }

        public Criteria andRechargeNumberIsNotNull() {
            addCriterion("rechargeNumber is not null");
            return (Criteria) this;
        }

        public Criteria andRechargeNumberEqualTo(BigDecimal value) {
            addCriterion("rechargeNumber =", value, "rechargeNumber");
            return (Criteria) this;
        }

        public Criteria andRechargeNumberNotEqualTo(BigDecimal value) {
            addCriterion("rechargeNumber <>", value, "rechargeNumber");
            return (Criteria) this;
        }

        public Criteria andRechargeNumberGreaterThan(BigDecimal value) {
            addCriterion("rechargeNumber >", value, "rechargeNumber");
            return (Criteria) this;
        }

        public Criteria andRechargeNumberGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("rechargeNumber >=", value, "rechargeNumber");
            return (Criteria) this;
        }

        public Criteria andRechargeNumberLessThan(BigDecimal value) {
            addCriterion("rechargeNumber <", value, "rechargeNumber");
            return (Criteria) this;
        }

        public Criteria andRechargeNumberLessThanOrEqualTo(BigDecimal value) {
            addCriterion("rechargeNumber <=", value, "rechargeNumber");
            return (Criteria) this;
        }

        public Criteria andRechargeNumberIn(List<BigDecimal> values) {
            addCriterion("rechargeNumber in", values, "rechargeNumber");
            return (Criteria) this;
        }

        public Criteria andRechargeNumberNotIn(List<BigDecimal> values) {
            addCriterion("rechargeNumber not in", values, "rechargeNumber");
            return (Criteria) this;
        }

        public Criteria andRechargeNumberBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("rechargeNumber between", value1, value2, "rechargeNumber");
            return (Criteria) this;
        }

        public Criteria andRechargeNumberNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("rechargeNumber not between", value1, value2, "rechargeNumber");
            return (Criteria) this;
        }

        public Criteria andFreezeBalanceIsNull() {
            addCriterion("freezeBalance is null");
            return (Criteria) this;
        }

        public Criteria andFreezeBalanceIsNotNull() {
            addCriterion("freezeBalance is not null");
            return (Criteria) this;
        }

        public Criteria andFreezeBalanceEqualTo(BigDecimal value) {
            addCriterion("freezeBalance =", value, "freezeBalance");
            return (Criteria) this;
        }

        public Criteria andFreezeBalanceNotEqualTo(BigDecimal value) {
            addCriterion("freezeBalance <>", value, "freezeBalance");
            return (Criteria) this;
        }

        public Criteria andFreezeBalanceGreaterThan(BigDecimal value) {
            addCriterion("freezeBalance >", value, "freezeBalance");
            return (Criteria) this;
        }

        public Criteria andFreezeBalanceGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("freezeBalance >=", value, "freezeBalance");
            return (Criteria) this;
        }

        public Criteria andFreezeBalanceLessThan(BigDecimal value) {
            addCriterion("freezeBalance <", value, "freezeBalance");
            return (Criteria) this;
        }

        public Criteria andFreezeBalanceLessThanOrEqualTo(BigDecimal value) {
            addCriterion("freezeBalance <=", value, "freezeBalance");
            return (Criteria) this;
        }

        public Criteria andFreezeBalanceIn(List<BigDecimal> values) {
            addCriterion("freezeBalance in", values, "freezeBalance");
            return (Criteria) this;
        }

        public Criteria andFreezeBalanceNotIn(List<BigDecimal> values) {
            addCriterion("freezeBalance not in", values, "freezeBalance");
            return (Criteria) this;
        }

        public Criteria andFreezeBalanceBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("freezeBalance between", value1, value2, "freezeBalance");
            return (Criteria) this;
        }

        public Criteria andFreezeBalanceNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("freezeBalance not between", value1, value2, "freezeBalance");
            return (Criteria) this;
        }

        public Criteria andAccountBalanceIsNull() {
            addCriterion("accountBalance is null");
            return (Criteria) this;
        }

        public Criteria andAccountBalanceIsNotNull() {
            addCriterion("accountBalance is not null");
            return (Criteria) this;
        }

        public Criteria andAccountBalanceEqualTo(BigDecimal value) {
            addCriterion("accountBalance =", value, "accountBalance");
            return (Criteria) this;
        }

        public Criteria andAccountBalanceNotEqualTo(BigDecimal value) {
            addCriterion("accountBalance <>", value, "accountBalance");
            return (Criteria) this;
        }

        public Criteria andAccountBalanceGreaterThan(BigDecimal value) {
            addCriterion("accountBalance >", value, "accountBalance");
            return (Criteria) this;
        }

        public Criteria andAccountBalanceGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("accountBalance >=", value, "accountBalance");
            return (Criteria) this;
        }

        public Criteria andAccountBalanceLessThan(BigDecimal value) {
            addCriterion("accountBalance <", value, "accountBalance");
            return (Criteria) this;
        }

        public Criteria andAccountBalanceLessThanOrEqualTo(BigDecimal value) {
            addCriterion("accountBalance <=", value, "accountBalance");
            return (Criteria) this;
        }

        public Criteria andAccountBalanceIn(List<BigDecimal> values) {
            addCriterion("accountBalance in", values, "accountBalance");
            return (Criteria) this;
        }

        public Criteria andAccountBalanceNotIn(List<BigDecimal> values) {
            addCriterion("accountBalance not in", values, "accountBalance");
            return (Criteria) this;
        }

        public Criteria andAccountBalanceBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("accountBalance between", value1, value2, "accountBalance");
            return (Criteria) this;
        }

        public Criteria andAccountBalanceNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("accountBalance not between", value1, value2, "accountBalance");
            return (Criteria) this;
        }

        public Criteria andSumDealAmountIsNull() {
            addCriterion("sumDealAmount is null");
            return (Criteria) this;
        }

        public Criteria andSumDealAmountIsNotNull() {
            addCriterion("sumDealAmount is not null");
            return (Criteria) this;
        }

        public Criteria andSumDealAmountEqualTo(BigDecimal value) {
            addCriterion("sumDealAmount =", value, "sumDealAmount");
            return (Criteria) this;
        }

        public Criteria andSumDealAmountNotEqualTo(BigDecimal value) {
            addCriterion("sumDealAmount <>", value, "sumDealAmount");
            return (Criteria) this;
        }

        public Criteria andSumDealAmountGreaterThan(BigDecimal value) {
            addCriterion("sumDealAmount >", value, "sumDealAmount");
            return (Criteria) this;
        }

        public Criteria andSumDealAmountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("sumDealAmount >=", value, "sumDealAmount");
            return (Criteria) this;
        }

        public Criteria andSumDealAmountLessThan(BigDecimal value) {
            addCriterion("sumDealAmount <", value, "sumDealAmount");
            return (Criteria) this;
        }

        public Criteria andSumDealAmountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("sumDealAmount <=", value, "sumDealAmount");
            return (Criteria) this;
        }

        public Criteria andSumDealAmountIn(List<BigDecimal> values) {
            addCriterion("sumDealAmount in", values, "sumDealAmount");
            return (Criteria) this;
        }

        public Criteria andSumDealAmountNotIn(List<BigDecimal> values) {
            addCriterion("sumDealAmount not in", values, "sumDealAmount");
            return (Criteria) this;
        }

        public Criteria andSumDealAmountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("sumDealAmount between", value1, value2, "sumDealAmount");
            return (Criteria) this;
        }

        public Criteria andSumDealAmountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("sumDealAmount not between", value1, value2, "sumDealAmount");
            return (Criteria) this;
        }

        public Criteria andSumRechargeAmountIsNull() {
            addCriterion("sumRechargeAmount is null");
            return (Criteria) this;
        }

        public Criteria andSumRechargeAmountIsNotNull() {
            addCriterion("sumRechargeAmount is not null");
            return (Criteria) this;
        }

        public Criteria andSumRechargeAmountEqualTo(BigDecimal value) {
            addCriterion("sumRechargeAmount =", value, "sumRechargeAmount");
            return (Criteria) this;
        }

        public Criteria andSumRechargeAmountNotEqualTo(BigDecimal value) {
            addCriterion("sumRechargeAmount <>", value, "sumRechargeAmount");
            return (Criteria) this;
        }

        public Criteria andSumRechargeAmountGreaterThan(BigDecimal value) {
            addCriterion("sumRechargeAmount >", value, "sumRechargeAmount");
            return (Criteria) this;
        }

        public Criteria andSumRechargeAmountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("sumRechargeAmount >=", value, "sumRechargeAmount");
            return (Criteria) this;
        }

        public Criteria andSumRechargeAmountLessThan(BigDecimal value) {
            addCriterion("sumRechargeAmount <", value, "sumRechargeAmount");
            return (Criteria) this;
        }

        public Criteria andSumRechargeAmountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("sumRechargeAmount <=", value, "sumRechargeAmount");
            return (Criteria) this;
        }

        public Criteria andSumRechargeAmountIn(List<BigDecimal> values) {
            addCriterion("sumRechargeAmount in", values, "sumRechargeAmount");
            return (Criteria) this;
        }

        public Criteria andSumRechargeAmountNotIn(List<BigDecimal> values) {
            addCriterion("sumRechargeAmount not in", values, "sumRechargeAmount");
            return (Criteria) this;
        }

        public Criteria andSumRechargeAmountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("sumRechargeAmount between", value1, value2, "sumRechargeAmount");
            return (Criteria) this;
        }

        public Criteria andSumRechargeAmountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("sumRechargeAmount not between", value1, value2, "sumRechargeAmount");
            return (Criteria) this;
        }

        public Criteria andSumProfitIsNull() {
            addCriterion("sumProfit is null");
            return (Criteria) this;
        }

        public Criteria andSumProfitIsNotNull() {
            addCriterion("sumProfit is not null");
            return (Criteria) this;
        }

        public Criteria andSumProfitEqualTo(BigDecimal value) {
            addCriterion("sumProfit =", value, "sumProfit");
            return (Criteria) this;
        }

        public Criteria andSumProfitNotEqualTo(BigDecimal value) {
            addCriterion("sumProfit <>", value, "sumProfit");
            return (Criteria) this;
        }

        public Criteria andSumProfitGreaterThan(BigDecimal value) {
            addCriterion("sumProfit >", value, "sumProfit");
            return (Criteria) this;
        }

        public Criteria andSumProfitGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("sumProfit >=", value, "sumProfit");
            return (Criteria) this;
        }

        public Criteria andSumProfitLessThan(BigDecimal value) {
            addCriterion("sumProfit <", value, "sumProfit");
            return (Criteria) this;
        }

        public Criteria andSumProfitLessThanOrEqualTo(BigDecimal value) {
            addCriterion("sumProfit <=", value, "sumProfit");
            return (Criteria) this;
        }

        public Criteria andSumProfitIn(List<BigDecimal> values) {
            addCriterion("sumProfit in", values, "sumProfit");
            return (Criteria) this;
        }

        public Criteria andSumProfitNotIn(List<BigDecimal> values) {
            addCriterion("sumProfit not in", values, "sumProfit");
            return (Criteria) this;
        }

        public Criteria andSumProfitBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("sumProfit between", value1, value2, "sumProfit");
            return (Criteria) this;
        }

        public Criteria andSumProfitNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("sumProfit not between", value1, value2, "sumProfit");
            return (Criteria) this;
        }

        public Criteria andSumAgentProfitIsNull() {
            addCriterion("sumAgentProfit is null");
            return (Criteria) this;
        }

        public Criteria andSumAgentProfitIsNotNull() {
            addCriterion("sumAgentProfit is not null");
            return (Criteria) this;
        }

        public Criteria andSumAgentProfitEqualTo(BigDecimal value) {
            addCriterion("sumAgentProfit =", value, "sumAgentProfit");
            return (Criteria) this;
        }

        public Criteria andSumAgentProfitNotEqualTo(BigDecimal value) {
            addCriterion("sumAgentProfit <>", value, "sumAgentProfit");
            return (Criteria) this;
        }

        public Criteria andSumAgentProfitGreaterThan(BigDecimal value) {
            addCriterion("sumAgentProfit >", value, "sumAgentProfit");
            return (Criteria) this;
        }

        public Criteria andSumAgentProfitGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("sumAgentProfit >=", value, "sumAgentProfit");
            return (Criteria) this;
        }

        public Criteria andSumAgentProfitLessThan(BigDecimal value) {
            addCriterion("sumAgentProfit <", value, "sumAgentProfit");
            return (Criteria) this;
        }

        public Criteria andSumAgentProfitLessThanOrEqualTo(BigDecimal value) {
            addCriterion("sumAgentProfit <=", value, "sumAgentProfit");
            return (Criteria) this;
        }

        public Criteria andSumAgentProfitIn(List<BigDecimal> values) {
            addCriterion("sumAgentProfit in", values, "sumAgentProfit");
            return (Criteria) this;
        }

        public Criteria andSumAgentProfitNotIn(List<BigDecimal> values) {
            addCriterion("sumAgentProfit not in", values, "sumAgentProfit");
            return (Criteria) this;
        }

        public Criteria andSumAgentProfitBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("sumAgentProfit between", value1, value2, "sumAgentProfit");
            return (Criteria) this;
        }

        public Criteria andSumAgentProfitNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("sumAgentProfit not between", value1, value2, "sumAgentProfit");
            return (Criteria) this;
        }

        public Criteria andSumOrderCountIsNull() {
            addCriterion("sumOrderCount is null");
            return (Criteria) this;
        }

        public Criteria andSumOrderCountIsNotNull() {
            addCriterion("sumOrderCount is not null");
            return (Criteria) this;
        }

        public Criteria andSumOrderCountEqualTo(Integer value) {
            addCriterion("sumOrderCount =", value, "sumOrderCount");
            return (Criteria) this;
        }

        public Criteria andSumOrderCountNotEqualTo(Integer value) {
            addCriterion("sumOrderCount <>", value, "sumOrderCount");
            return (Criteria) this;
        }

        public Criteria andSumOrderCountGreaterThan(Integer value) {
            addCriterion("sumOrderCount >", value, "sumOrderCount");
            return (Criteria) this;
        }

        public Criteria andSumOrderCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("sumOrderCount >=", value, "sumOrderCount");
            return (Criteria) this;
        }

        public Criteria andSumOrderCountLessThan(Integer value) {
            addCriterion("sumOrderCount <", value, "sumOrderCount");
            return (Criteria) this;
        }

        public Criteria andSumOrderCountLessThanOrEqualTo(Integer value) {
            addCriterion("sumOrderCount <=", value, "sumOrderCount");
            return (Criteria) this;
        }

        public Criteria andSumOrderCountIn(List<Integer> values) {
            addCriterion("sumOrderCount in", values, "sumOrderCount");
            return (Criteria) this;
        }

        public Criteria andSumOrderCountNotIn(List<Integer> values) {
            addCriterion("sumOrderCount not in", values, "sumOrderCount");
            return (Criteria) this;
        }

        public Criteria andSumOrderCountBetween(Integer value1, Integer value2) {
            addCriterion("sumOrderCount between", value1, value2, "sumOrderCount");
            return (Criteria) this;
        }

        public Criteria andSumOrderCountNotBetween(Integer value1, Integer value2) {
            addCriterion("sumOrderCount not between", value1, value2, "sumOrderCount");
            return (Criteria) this;
        }

        public Criteria andTodayDealAmountIsNull() {
            addCriterion("todayDealAmount is null");
            return (Criteria) this;
        }

        public Criteria andTodayDealAmountIsNotNull() {
            addCriterion("todayDealAmount is not null");
            return (Criteria) this;
        }

        public Criteria andTodayDealAmountEqualTo(BigDecimal value) {
            addCriterion("todayDealAmount =", value, "todayDealAmount");
            return (Criteria) this;
        }

        public Criteria andTodayDealAmountNotEqualTo(BigDecimal value) {
            addCriterion("todayDealAmount <>", value, "todayDealAmount");
            return (Criteria) this;
        }

        public Criteria andTodayDealAmountGreaterThan(BigDecimal value) {
            addCriterion("todayDealAmount >", value, "todayDealAmount");
            return (Criteria) this;
        }

        public Criteria andTodayDealAmountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("todayDealAmount >=", value, "todayDealAmount");
            return (Criteria) this;
        }

        public Criteria andTodayDealAmountLessThan(BigDecimal value) {
            addCriterion("todayDealAmount <", value, "todayDealAmount");
            return (Criteria) this;
        }

        public Criteria andTodayDealAmountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("todayDealAmount <=", value, "todayDealAmount");
            return (Criteria) this;
        }

        public Criteria andTodayDealAmountIn(List<BigDecimal> values) {
            addCriterion("todayDealAmount in", values, "todayDealAmount");
            return (Criteria) this;
        }

        public Criteria andTodayDealAmountNotIn(List<BigDecimal> values) {
            addCriterion("todayDealAmount not in", values, "todayDealAmount");
            return (Criteria) this;
        }

        public Criteria andTodayDealAmountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("todayDealAmount between", value1, value2, "todayDealAmount");
            return (Criteria) this;
        }

        public Criteria andTodayDealAmountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("todayDealAmount not between", value1, value2, "todayDealAmount");
            return (Criteria) this;
        }

        public Criteria andTodayProfitIsNull() {
            addCriterion("todayProfit is null");
            return (Criteria) this;
        }

        public Criteria andTodayProfitIsNotNull() {
            addCriterion("todayProfit is not null");
            return (Criteria) this;
        }

        public Criteria andTodayProfitEqualTo(BigDecimal value) {
            addCriterion("todayProfit =", value, "todayProfit");
            return (Criteria) this;
        }

        public Criteria andTodayProfitNotEqualTo(BigDecimal value) {
            addCriterion("todayProfit <>", value, "todayProfit");
            return (Criteria) this;
        }

        public Criteria andTodayProfitGreaterThan(BigDecimal value) {
            addCriterion("todayProfit >", value, "todayProfit");
            return (Criteria) this;
        }

        public Criteria andTodayProfitGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("todayProfit >=", value, "todayProfit");
            return (Criteria) this;
        }

        public Criteria andTodayProfitLessThan(BigDecimal value) {
            addCriterion("todayProfit <", value, "todayProfit");
            return (Criteria) this;
        }

        public Criteria andTodayProfitLessThanOrEqualTo(BigDecimal value) {
            addCriterion("todayProfit <=", value, "todayProfit");
            return (Criteria) this;
        }

        public Criteria andTodayProfitIn(List<BigDecimal> values) {
            addCriterion("todayProfit in", values, "todayProfit");
            return (Criteria) this;
        }

        public Criteria andTodayProfitNotIn(List<BigDecimal> values) {
            addCriterion("todayProfit not in", values, "todayProfit");
            return (Criteria) this;
        }

        public Criteria andTodayProfitBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("todayProfit between", value1, value2, "todayProfit");
            return (Criteria) this;
        }

        public Criteria andTodayProfitNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("todayProfit not between", value1, value2, "todayProfit");
            return (Criteria) this;
        }

        public Criteria andTodayOrderCountIsNull() {
            addCriterion("todayOrderCount is null");
            return (Criteria) this;
        }

        public Criteria andTodayOrderCountIsNotNull() {
            addCriterion("todayOrderCount is not null");
            return (Criteria) this;
        }

        public Criteria andTodayOrderCountEqualTo(Integer value) {
            addCriterion("todayOrderCount =", value, "todayOrderCount");
            return (Criteria) this;
        }

        public Criteria andTodayOrderCountNotEqualTo(Integer value) {
            addCriterion("todayOrderCount <>", value, "todayOrderCount");
            return (Criteria) this;
        }

        public Criteria andTodayOrderCountGreaterThan(Integer value) {
            addCriterion("todayOrderCount >", value, "todayOrderCount");
            return (Criteria) this;
        }

        public Criteria andTodayOrderCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("todayOrderCount >=", value, "todayOrderCount");
            return (Criteria) this;
        }

        public Criteria andTodayOrderCountLessThan(Integer value) {
            addCriterion("todayOrderCount <", value, "todayOrderCount");
            return (Criteria) this;
        }

        public Criteria andTodayOrderCountLessThanOrEqualTo(Integer value) {
            addCriterion("todayOrderCount <=", value, "todayOrderCount");
            return (Criteria) this;
        }

        public Criteria andTodayOrderCountIn(List<Integer> values) {
            addCriterion("todayOrderCount in", values, "todayOrderCount");
            return (Criteria) this;
        }

        public Criteria andTodayOrderCountNotIn(List<Integer> values) {
            addCriterion("todayOrderCount not in", values, "todayOrderCount");
            return (Criteria) this;
        }

        public Criteria andTodayOrderCountBetween(Integer value1, Integer value2) {
            addCriterion("todayOrderCount between", value1, value2, "todayOrderCount");
            return (Criteria) this;
        }

        public Criteria andTodayOrderCountNotBetween(Integer value1, Integer value2) {
            addCriterion("todayOrderCount not between", value1, value2, "todayOrderCount");
            return (Criteria) this;
        }

        public Criteria andTodayAgentProfitIsNull() {
            addCriterion("todayAgentProfit is null");
            return (Criteria) this;
        }

        public Criteria andTodayAgentProfitIsNotNull() {
            addCriterion("todayAgentProfit is not null");
            return (Criteria) this;
        }

        public Criteria andTodayAgentProfitEqualTo(BigDecimal value) {
            addCriterion("todayAgentProfit =", value, "todayAgentProfit");
            return (Criteria) this;
        }

        public Criteria andTodayAgentProfitNotEqualTo(BigDecimal value) {
            addCriterion("todayAgentProfit <>", value, "todayAgentProfit");
            return (Criteria) this;
        }

        public Criteria andTodayAgentProfitGreaterThan(BigDecimal value) {
            addCriterion("todayAgentProfit >", value, "todayAgentProfit");
            return (Criteria) this;
        }

        public Criteria andTodayAgentProfitGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("todayAgentProfit >=", value, "todayAgentProfit");
            return (Criteria) this;
        }

        public Criteria andTodayAgentProfitLessThan(BigDecimal value) {
            addCriterion("todayAgentProfit <", value, "todayAgentProfit");
            return (Criteria) this;
        }

        public Criteria andTodayAgentProfitLessThanOrEqualTo(BigDecimal value) {
            addCriterion("todayAgentProfit <=", value, "todayAgentProfit");
            return (Criteria) this;
        }

        public Criteria andTodayAgentProfitIn(List<BigDecimal> values) {
            addCriterion("todayAgentProfit in", values, "todayAgentProfit");
            return (Criteria) this;
        }

        public Criteria andTodayAgentProfitNotIn(List<BigDecimal> values) {
            addCriterion("todayAgentProfit not in", values, "todayAgentProfit");
            return (Criteria) this;
        }

        public Criteria andTodayAgentProfitBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("todayAgentProfit between", value1, value2, "todayAgentProfit");
            return (Criteria) this;
        }

        public Criteria andTodayAgentProfitNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("todayAgentProfit not between", value1, value2, "todayAgentProfit");
            return (Criteria) this;
        }

        public Criteria andUserTypeIsNull() {
            addCriterion("userType is null");
            return (Criteria) this;
        }

        public Criteria andUserTypeIsNotNull() {
            addCriterion("userType is not null");
            return (Criteria) this;
        }

        public Criteria andUserTypeEqualTo(String value) {
            addCriterion("userType =", value, "userType");
            return (Criteria) this;
        }

        public Criteria andUserTypeNotEqualTo(String value) {
            addCriterion("userType <>", value, "userType");
            return (Criteria) this;
        }

        public Criteria andUserTypeGreaterThan(String value) {
            addCriterion("userType >", value, "userType");
            return (Criteria) this;
        }

        public Criteria andUserTypeGreaterThanOrEqualTo(String value) {
            addCriterion("userType >=", value, "userType");
            return (Criteria) this;
        }

        public Criteria andUserTypeLessThan(String value) {
            addCriterion("userType <", value, "userType");
            return (Criteria) this;
        }

        public Criteria andUserTypeLessThanOrEqualTo(String value) {
            addCriterion("userType <=", value, "userType");
            return (Criteria) this;
        }

        public Criteria andUserTypeLike(String value) {
            addCriterion("userType like", value, "userType");
            return (Criteria) this;
        }

        public Criteria andUserTypeNotLike(String value) {
            addCriterion("userType not like", value, "userType");
            return (Criteria) this;
        }

        public Criteria andUserTypeIn(List<String> values) {
            addCriterion("userType in", values, "userType");
            return (Criteria) this;
        }

        public Criteria andUserTypeNotIn(List<String> values) {
            addCriterion("userType not in", values, "userType");
            return (Criteria) this;
        }

        public Criteria andUserTypeBetween(String value1, String value2) {
            addCriterion("userType between", value1, value2, "userType");
            return (Criteria) this;
        }

        public Criteria andUserTypeNotBetween(String value1, String value2) {
            addCriterion("userType not between", value1, value2, "userType");
            return (Criteria) this;
        }

        public Criteria andAgentIsNull() {
            addCriterion("agent is null");
            return (Criteria) this;
        }

        public Criteria andAgentIsNotNull() {
            addCriterion("agent is not null");
            return (Criteria) this;
        }

        public Criteria andAgentEqualTo(String value) {
            addCriterion("agent =", value, "agent");
            return (Criteria) this;
        }

        public Criteria andAgentNotEqualTo(String value) {
            addCriterion("agent <>", value, "agent");
            return (Criteria) this;
        }

        public Criteria andAgentGreaterThan(String value) {
            addCriterion("agent >", value, "agent");
            return (Criteria) this;
        }

        public Criteria andAgentGreaterThanOrEqualTo(String value) {
            addCriterion("agent >=", value, "agent");
            return (Criteria) this;
        }

        public Criteria andAgentLessThan(String value) {
            addCriterion("agent <", value, "agent");
            return (Criteria) this;
        }

        public Criteria andAgentLessThanOrEqualTo(String value) {
            addCriterion("agent <=", value, "agent");
            return (Criteria) this;
        }

        public Criteria andAgentLike(String value) {
            addCriterion("agent like", value, "agent");
            return (Criteria) this;
        }

        public Criteria andAgentNotLike(String value) {
            addCriterion("agent not like", value, "agent");
            return (Criteria) this;
        }

        public Criteria andAgentIn(List<String> values) {
            addCriterion("agent in", values, "agent");
            return (Criteria) this;
        }

        public Criteria andAgentNotIn(List<String> values) {
            addCriterion("agent not in", values, "agent");
            return (Criteria) this;
        }

        public Criteria andAgentBetween(String value1, String value2) {
            addCriterion("agent between", value1, value2, "agent");
            return (Criteria) this;
        }

        public Criteria andAgentNotBetween(String value1, String value2) {
            addCriterion("agent not between", value1, value2, "agent");
            return (Criteria) this;
        }

        public Criteria andIsAgentIsNull() {
            addCriterion("isAgent is null");
            return (Criteria) this;
        }

        public Criteria andIsAgentIsNotNull() {
            addCriterion("isAgent is not null");
            return (Criteria) this;
        }

        public Criteria andIsAgentEqualTo(String value) {
            addCriterion("isAgent =", value, "isAgent");
            return (Criteria) this;
        }

        public Criteria andIsAgentNotEqualTo(String value) {
            addCriterion("isAgent <>", value, "isAgent");
            return (Criteria) this;
        }

        public Criteria andIsAgentGreaterThan(String value) {
            addCriterion("isAgent >", value, "isAgent");
            return (Criteria) this;
        }

        public Criteria andIsAgentGreaterThanOrEqualTo(String value) {
            addCriterion("isAgent >=", value, "isAgent");
            return (Criteria) this;
        }

        public Criteria andIsAgentLessThan(String value) {
            addCriterion("isAgent <", value, "isAgent");
            return (Criteria) this;
        }

        public Criteria andIsAgentLessThanOrEqualTo(String value) {
            addCriterion("isAgent <=", value, "isAgent");
            return (Criteria) this;
        }

        public Criteria andIsAgentLike(String value) {
            addCriterion("isAgent like", value, "isAgent");
            return (Criteria) this;
        }

        public Criteria andIsAgentNotLike(String value) {
            addCriterion("isAgent not like", value, "isAgent");
            return (Criteria) this;
        }

        public Criteria andIsAgentIn(List<String> values) {
            addCriterion("isAgent in", values, "isAgent");
            return (Criteria) this;
        }

        public Criteria andIsAgentNotIn(List<String> values) {
            addCriterion("isAgent not in", values, "isAgent");
            return (Criteria) this;
        }

        public Criteria andIsAgentBetween(String value1, String value2) {
            addCriterion("isAgent between", value1, value2, "isAgent");
            return (Criteria) this;
        }

        public Criteria andIsAgentNotBetween(String value1, String value2) {
            addCriterion("isAgent not between", value1, value2, "isAgent");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("createTime is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("createTime is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Date value) {
            addCriterion("createTime =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Date value) {
            addCriterion("createTime <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("createTime >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("createTime >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Date value) {
            addCriterion("createTime <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("createTime <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("createTime in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("createTime not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("createTime between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("createTime not between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andSubmitTimeIsNull() {
            addCriterion("submitTime is null");
            return (Criteria) this;
        }

        public Criteria andSubmitTimeIsNotNull() {
            addCriterion("submitTime is not null");
            return (Criteria) this;
        }

        public Criteria andSubmitTimeEqualTo(Date value) {
            addCriterion("submitTime =", value, "submitTime");
            return (Criteria) this;
        }

        public Criteria andSubmitTimeNotEqualTo(Date value) {
            addCriterion("submitTime <>", value, "submitTime");
            return (Criteria) this;
        }

        public Criteria andSubmitTimeGreaterThan(Date value) {
            addCriterion("submitTime >", value, "submitTime");
            return (Criteria) this;
        }

        public Criteria andSubmitTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("submitTime >=", value, "submitTime");
            return (Criteria) this;
        }

        public Criteria andSubmitTimeLessThan(Date value) {
            addCriterion("submitTime <", value, "submitTime");
            return (Criteria) this;
        }

        public Criteria andSubmitTimeLessThanOrEqualTo(Date value) {
            addCriterion("submitTime <=", value, "submitTime");
            return (Criteria) this;
        }

        public Criteria andSubmitTimeIn(List<Date> values) {
            addCriterion("submitTime in", values, "submitTime");
            return (Criteria) this;
        }

        public Criteria andSubmitTimeNotIn(List<Date> values) {
            addCriterion("submitTime not in", values, "submitTime");
            return (Criteria) this;
        }

        public Criteria andSubmitTimeBetween(Date value1, Date value2) {
            addCriterion("submitTime between", value1, value2, "submitTime");
            return (Criteria) this;
        }

        public Criteria andSubmitTimeNotBetween(Date value1, Date value2) {
            addCriterion("submitTime not between", value1, value2, "submitTime");
            return (Criteria) this;
        }

        public Criteria andStatusIsNull() {
            addCriterion("status is null");
            return (Criteria) this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("status is not null");
            return (Criteria) this;
        }

        public Criteria andStatusEqualTo(Integer value) {
            addCriterion("status =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(Integer value) {
            addCriterion("status <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(Integer value) {
            addCriterion("status >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("status >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(Integer value) {
            addCriterion("status <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(Integer value) {
            addCriterion("status <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<Integer> values) {
            addCriterion("status in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<Integer> values) {
            addCriterion("status not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(Integer value1, Integer value2) {
            addCriterion("status between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("status not between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andVersionIsNull() {
            addCriterion("version is null");
            return (Criteria) this;
        }

        public Criteria andVersionIsNotNull() {
            addCriterion("version is not null");
            return (Criteria) this;
        }

        public Criteria andVersionEqualTo(Integer value) {
            addCriterion("version =", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionNotEqualTo(Integer value) {
            addCriterion("version <>", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionGreaterThan(Integer value) {
            addCriterion("version >", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionGreaterThanOrEqualTo(Integer value) {
            addCriterion("version >=", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionLessThan(Integer value) {
            addCriterion("version <", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionLessThanOrEqualTo(Integer value) {
            addCriterion("version <=", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionIn(List<Integer> values) {
            addCriterion("version in", values, "version");
            return (Criteria) this;
        }

        public Criteria andVersionNotIn(List<Integer> values) {
            addCriterion("version not in", values, "version");
            return (Criteria) this;
        }

        public Criteria andVersionBetween(Integer value1, Integer value2) {
            addCriterion("version between", value1, value2, "version");
            return (Criteria) this;
        }

        public Criteria andVersionNotBetween(Integer value1, Integer value2) {
            addCriterion("version not between", value1, value2, "version");
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