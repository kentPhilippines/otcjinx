package deal.manage.bean;

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

        public Criteria andSumDealAmountRIsNull() {
            addCriterion("sumDealAmountR is null");
            return (Criteria) this;
        }

        public Criteria andSumDealAmountRIsNotNull() {
            addCriterion("sumDealAmountR is not null");
            return (Criteria) this;
        }

        public Criteria andSumDealAmountREqualTo(BigDecimal value) {
            addCriterion("sumDealAmountR =", value, "sumDealAmountR");
            return (Criteria) this;
        }

        public Criteria andSumDealAmountRNotEqualTo(BigDecimal value) {
            addCriterion("sumDealAmountR <>", value, "sumDealAmountR");
            return (Criteria) this;
        }

        public Criteria andSumDealAmountRGreaterThan(BigDecimal value) {
            addCriterion("sumDealAmountR >", value, "sumDealAmountR");
            return (Criteria) this;
        }

        public Criteria andSumDealAmountRGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("sumDealAmountR >=", value, "sumDealAmountR");
            return (Criteria) this;
        }

        public Criteria andSumDealAmountRLessThan(BigDecimal value) {
            addCriterion("sumDealAmountR <", value, "sumDealAmountR");
            return (Criteria) this;
        }

        public Criteria andSumDealAmountRLessThanOrEqualTo(BigDecimal value) {
            addCriterion("sumDealAmountR <=", value, "sumDealAmountR");
            return (Criteria) this;
        }

        public Criteria andSumDealAmountRIn(List<BigDecimal> values) {
            addCriterion("sumDealAmountR in", values, "sumDealAmountR");
            return (Criteria) this;
        }

        public Criteria andSumDealAmountRNotIn(List<BigDecimal> values) {
            addCriterion("sumDealAmountR not in", values, "sumDealAmountR");
            return (Criteria) this;
        }

        public Criteria andSumDealAmountRBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("sumDealAmountR between", value1, value2, "sumDealAmountR");
            return (Criteria) this;
        }

        public Criteria andSumDealAmountRNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("sumDealAmountR not between", value1, value2, "sumDealAmountR");
            return (Criteria) this;
        }

        public Criteria andSumDealAmountCIsNull() {
            addCriterion("sumDealAmountC is null");
            return (Criteria) this;
        }

        public Criteria andSumDealAmountCIsNotNull() {
            addCriterion("sumDealAmountC is not null");
            return (Criteria) this;
        }

        public Criteria andSumDealAmountCEqualTo(BigDecimal value) {
            addCriterion("sumDealAmountC =", value, "sumDealAmountC");
            return (Criteria) this;
        }

        public Criteria andSumDealAmountCNotEqualTo(BigDecimal value) {
            addCriterion("sumDealAmountC <>", value, "sumDealAmountC");
            return (Criteria) this;
        }

        public Criteria andSumDealAmountCGreaterThan(BigDecimal value) {
            addCriterion("sumDealAmountC >", value, "sumDealAmountC");
            return (Criteria) this;
        }

        public Criteria andSumDealAmountCGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("sumDealAmountC >=", value, "sumDealAmountC");
            return (Criteria) this;
        }

        public Criteria andSumDealAmountCLessThan(BigDecimal value) {
            addCriterion("sumDealAmountC <", value, "sumDealAmountC");
            return (Criteria) this;
        }

        public Criteria andSumDealAmountCLessThanOrEqualTo(BigDecimal value) {
            addCriterion("sumDealAmountC <=", value, "sumDealAmountC");
            return (Criteria) this;
        }

        public Criteria andSumDealAmountCIn(List<BigDecimal> values) {
            addCriterion("sumDealAmountC in", values, "sumDealAmountC");
            return (Criteria) this;
        }

        public Criteria andSumDealAmountCNotIn(List<BigDecimal> values) {
            addCriterion("sumDealAmountC not in", values, "sumDealAmountC");
            return (Criteria) this;
        }

        public Criteria andSumDealAmountCBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("sumDealAmountC between", value1, value2, "sumDealAmountC");
            return (Criteria) this;
        }

        public Criteria andSumDealAmountCNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("sumDealAmountC not between", value1, value2, "sumDealAmountC");
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

        public Criteria andSumOrderCountRIsNull() {
            addCriterion("sumOrderCountR is null");
            return (Criteria) this;
        }

        public Criteria andSumOrderCountRIsNotNull() {
            addCriterion("sumOrderCountR is not null");
            return (Criteria) this;
        }

        public Criteria andSumOrderCountREqualTo(Integer value) {
            addCriterion("sumOrderCountR =", value, "sumOrderCountR");
            return (Criteria) this;
        }

        public Criteria andSumOrderCountRNotEqualTo(Integer value) {
            addCriterion("sumOrderCountR <>", value, "sumOrderCountR");
            return (Criteria) this;
        }

        public Criteria andSumOrderCountRGreaterThan(Integer value) {
            addCriterion("sumOrderCountR >", value, "sumOrderCountR");
            return (Criteria) this;
        }

        public Criteria andSumOrderCountRGreaterThanOrEqualTo(Integer value) {
            addCriterion("sumOrderCountR >=", value, "sumOrderCountR");
            return (Criteria) this;
        }

        public Criteria andSumOrderCountRLessThan(Integer value) {
            addCriterion("sumOrderCountR <", value, "sumOrderCountR");
            return (Criteria) this;
        }

        public Criteria andSumOrderCountRLessThanOrEqualTo(Integer value) {
            addCriterion("sumOrderCountR <=", value, "sumOrderCountR");
            return (Criteria) this;
        }

        public Criteria andSumOrderCountRIn(List<Integer> values) {
            addCriterion("sumOrderCountR in", values, "sumOrderCountR");
            return (Criteria) this;
        }

        public Criteria andSumOrderCountRNotIn(List<Integer> values) {
            addCriterion("sumOrderCountR not in", values, "sumOrderCountR");
            return (Criteria) this;
        }

        public Criteria andSumOrderCountRBetween(Integer value1, Integer value2) {
            addCriterion("sumOrderCountR between", value1, value2, "sumOrderCountR");
            return (Criteria) this;
        }

        public Criteria andSumOrderCountRNotBetween(Integer value1, Integer value2) {
            addCriterion("sumOrderCountR not between", value1, value2, "sumOrderCountR");
            return (Criteria) this;
        }

        public Criteria andSumOrderCountCIsNull() {
            addCriterion("sumOrderCountC is null");
            return (Criteria) this;
        }

        public Criteria andSumOrderCountCIsNotNull() {
            addCriterion("sumOrderCountC is not null");
            return (Criteria) this;
        }

        public Criteria andSumOrderCountCEqualTo(Integer value) {
            addCriterion("sumOrderCountC =", value, "sumOrderCountC");
            return (Criteria) this;
        }

        public Criteria andSumOrderCountCNotEqualTo(Integer value) {
            addCriterion("sumOrderCountC <>", value, "sumOrderCountC");
            return (Criteria) this;
        }

        public Criteria andSumOrderCountCGreaterThan(Integer value) {
            addCriterion("sumOrderCountC >", value, "sumOrderCountC");
            return (Criteria) this;
        }

        public Criteria andSumOrderCountCGreaterThanOrEqualTo(Integer value) {
            addCriterion("sumOrderCountC >=", value, "sumOrderCountC");
            return (Criteria) this;
        }

        public Criteria andSumOrderCountCLessThan(Integer value) {
            addCriterion("sumOrderCountC <", value, "sumOrderCountC");
            return (Criteria) this;
        }

        public Criteria andSumOrderCountCLessThanOrEqualTo(Integer value) {
            addCriterion("sumOrderCountC <=", value, "sumOrderCountC");
            return (Criteria) this;
        }

        public Criteria andSumOrderCountCIn(List<Integer> values) {
            addCriterion("sumOrderCountC in", values, "sumOrderCountC");
            return (Criteria) this;
        }

        public Criteria andSumOrderCountCNotIn(List<Integer> values) {
            addCriterion("sumOrderCountC not in", values, "sumOrderCountC");
            return (Criteria) this;
        }

        public Criteria andSumOrderCountCBetween(Integer value1, Integer value2) {
            addCriterion("sumOrderCountC between", value1, value2, "sumOrderCountC");
            return (Criteria) this;
        }

        public Criteria andSumOrderCountCNotBetween(Integer value1, Integer value2) {
            addCriterion("sumOrderCountC not between", value1, value2, "sumOrderCountC");
            return (Criteria) this;
        }

        public Criteria andTodayDealAmountRIsNull() {
            addCriterion("todayDealAmountR is null");
            return (Criteria) this;
        }

        public Criteria andTodayDealAmountRIsNotNull() {
            addCriterion("todayDealAmountR is not null");
            return (Criteria) this;
        }

        public Criteria andTodayDealAmountREqualTo(BigDecimal value) {
            addCriterion("todayDealAmountR =", value, "todayDealAmountR");
            return (Criteria) this;
        }

        public Criteria andTodayDealAmountRNotEqualTo(BigDecimal value) {
            addCriterion("todayDealAmountR <>", value, "todayDealAmountR");
            return (Criteria) this;
        }

        public Criteria andTodayDealAmountRGreaterThan(BigDecimal value) {
            addCriterion("todayDealAmountR >", value, "todayDealAmountR");
            return (Criteria) this;
        }

        public Criteria andTodayDealAmountRGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("todayDealAmountR >=", value, "todayDealAmountR");
            return (Criteria) this;
        }

        public Criteria andTodayDealAmountRLessThan(BigDecimal value) {
            addCriterion("todayDealAmountR <", value, "todayDealAmountR");
            return (Criteria) this;
        }

        public Criteria andTodayDealAmountRLessThanOrEqualTo(BigDecimal value) {
            addCriterion("todayDealAmountR <=", value, "todayDealAmountR");
            return (Criteria) this;
        }

        public Criteria andTodayDealAmountRIn(List<BigDecimal> values) {
            addCriterion("todayDealAmountR in", values, "todayDealAmountR");
            return (Criteria) this;
        }

        public Criteria andTodayDealAmountRNotIn(List<BigDecimal> values) {
            addCriterion("todayDealAmountR not in", values, "todayDealAmountR");
            return (Criteria) this;
        }

        public Criteria andTodayDealAmountRBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("todayDealAmountR between", value1, value2, "todayDealAmountR");
            return (Criteria) this;
        }

        public Criteria andTodayDealAmountRNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("todayDealAmountR not between", value1, value2, "todayDealAmountR");
            return (Criteria) this;
        }

        public Criteria andTodayDealAmountCIsNull() {
            addCriterion("todayDealAmountC is null");
            return (Criteria) this;
        }

        public Criteria andTodayDealAmountCIsNotNull() {
            addCriterion("todayDealAmountC is not null");
            return (Criteria) this;
        }

        public Criteria andTodayDealAmountCEqualTo(BigDecimal value) {
            addCriterion("todayDealAmountC =", value, "todayDealAmountC");
            return (Criteria) this;
        }

        public Criteria andTodayDealAmountCNotEqualTo(BigDecimal value) {
            addCriterion("todayDealAmountC <>", value, "todayDealAmountC");
            return (Criteria) this;
        }

        public Criteria andTodayDealAmountCGreaterThan(BigDecimal value) {
            addCriterion("todayDealAmountC >", value, "todayDealAmountC");
            return (Criteria) this;
        }

        public Criteria andTodayDealAmountCGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("todayDealAmountC >=", value, "todayDealAmountC");
            return (Criteria) this;
        }

        public Criteria andTodayDealAmountCLessThan(BigDecimal value) {
            addCriterion("todayDealAmountC <", value, "todayDealAmountC");
            return (Criteria) this;
        }

        public Criteria andTodayDealAmountCLessThanOrEqualTo(BigDecimal value) {
            addCriterion("todayDealAmountC <=", value, "todayDealAmountC");
            return (Criteria) this;
        }

        public Criteria andTodayDealAmountCIn(List<BigDecimal> values) {
            addCriterion("todayDealAmountC in", values, "todayDealAmountC");
            return (Criteria) this;
        }

        public Criteria andTodayDealAmountCNotIn(List<BigDecimal> values) {
            addCriterion("todayDealAmountC not in", values, "todayDealAmountC");
            return (Criteria) this;
        }

        public Criteria andTodayDealAmountCBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("todayDealAmountC between", value1, value2, "todayDealAmountC");
            return (Criteria) this;
        }

        public Criteria andTodayDealAmountCNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("todayDealAmountC not between", value1, value2, "todayDealAmountC");
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

        public Criteria andTodayOrderCountRIsNull() {
            addCriterion("todayOrderCountR is null");
            return (Criteria) this;
        }

        public Criteria andTodayOrderCountRIsNotNull() {
            addCriterion("todayOrderCountR is not null");
            return (Criteria) this;
        }

        public Criteria andTodayOrderCountREqualTo(Integer value) {
            addCriterion("todayOrderCountR =", value, "todayOrderCountR");
            return (Criteria) this;
        }

        public Criteria andTodayOrderCountRNotEqualTo(Integer value) {
            addCriterion("todayOrderCountR <>", value, "todayOrderCountR");
            return (Criteria) this;
        }

        public Criteria andTodayOrderCountRGreaterThan(Integer value) {
            addCriterion("todayOrderCountR >", value, "todayOrderCountR");
            return (Criteria) this;
        }

        public Criteria andTodayOrderCountRGreaterThanOrEqualTo(Integer value) {
            addCriterion("todayOrderCountR >=", value, "todayOrderCountR");
            return (Criteria) this;
        }

        public Criteria andTodayOrderCountRLessThan(Integer value) {
            addCriterion("todayOrderCountR <", value, "todayOrderCountR");
            return (Criteria) this;
        }

        public Criteria andTodayOrderCountRLessThanOrEqualTo(Integer value) {
            addCriterion("todayOrderCountR <=", value, "todayOrderCountR");
            return (Criteria) this;
        }

        public Criteria andTodayOrderCountRIn(List<Integer> values) {
            addCriterion("todayOrderCountR in", values, "todayOrderCountR");
            return (Criteria) this;
        }

        public Criteria andTodayOrderCountRNotIn(List<Integer> values) {
            addCriterion("todayOrderCountR not in", values, "todayOrderCountR");
            return (Criteria) this;
        }

        public Criteria andTodayOrderCountRBetween(Integer value1, Integer value2) {
            addCriterion("todayOrderCountR between", value1, value2, "todayOrderCountR");
            return (Criteria) this;
        }

        public Criteria andTodayOrderCountRNotBetween(Integer value1, Integer value2) {
            addCriterion("todayOrderCountR not between", value1, value2, "todayOrderCountR");
            return (Criteria) this;
        }

        public Criteria andTodayOrderCountCIsNull() {
            addCriterion("todayOrderCountC is null");
            return (Criteria) this;
        }

        public Criteria andTodayOrderCountCIsNotNull() {
            addCriterion("todayOrderCountC is not null");
            return (Criteria) this;
        }

        public Criteria andTodayOrderCountCEqualTo(Integer value) {
            addCriterion("todayOrderCountC =", value, "todayOrderCountC");
            return (Criteria) this;
        }

        public Criteria andTodayOrderCountCNotEqualTo(Integer value) {
            addCriterion("todayOrderCountC <>", value, "todayOrderCountC");
            return (Criteria) this;
        }

        public Criteria andTodayOrderCountCGreaterThan(Integer value) {
            addCriterion("todayOrderCountC >", value, "todayOrderCountC");
            return (Criteria) this;
        }

        public Criteria andTodayOrderCountCGreaterThanOrEqualTo(Integer value) {
            addCriterion("todayOrderCountC >=", value, "todayOrderCountC");
            return (Criteria) this;
        }

        public Criteria andTodayOrderCountCLessThan(Integer value) {
            addCriterion("todayOrderCountC <", value, "todayOrderCountC");
            return (Criteria) this;
        }

        public Criteria andTodayOrderCountCLessThanOrEqualTo(Integer value) {
            addCriterion("todayOrderCountC <=", value, "todayOrderCountC");
            return (Criteria) this;
        }

        public Criteria andTodayOrderCountCIn(List<Integer> values) {
            addCriterion("todayOrderCountC in", values, "todayOrderCountC");
            return (Criteria) this;
        }

        public Criteria andTodayOrderCountCNotIn(List<Integer> values) {
            addCriterion("todayOrderCountC not in", values, "todayOrderCountC");
            return (Criteria) this;
        }

        public Criteria andTodayOrderCountCBetween(Integer value1, Integer value2) {
            addCriterion("todayOrderCountC between", value1, value2, "todayOrderCountC");
            return (Criteria) this;
        }

        public Criteria andTodayOrderCountCNotBetween(Integer value1, Integer value2) {
            addCriterion("todayOrderCountC not between", value1, value2, "todayOrderCountC");
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