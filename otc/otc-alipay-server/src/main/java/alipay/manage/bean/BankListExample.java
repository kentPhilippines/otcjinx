package alipay.manage.bean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BankListExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public BankListExample() {
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

        public Criteria andBankcardIdIsNull() {
            addCriterion("bankcardId is null");
            return (Criteria) this;
        }

        public Criteria andBankcardIdIsNotNull() {
            addCriterion("bankcardId is not null");
            return (Criteria) this;
        }

        public Criteria andBankcardIdEqualTo(String value) {
            addCriterion("bankcardId =", value, "bankcardId");
            return (Criteria) this;
        }

        public Criteria andBankcardIdNotEqualTo(String value) {
            addCriterion("bankcardId <>", value, "bankcardId");
            return (Criteria) this;
        }

        public Criteria andBankcardIdGreaterThan(String value) {
            addCriterion("bankcardId >", value, "bankcardId");
            return (Criteria) this;
        }

        public Criteria andBankcardIdGreaterThanOrEqualTo(String value) {
            addCriterion("bankcardId >=", value, "bankcardId");
            return (Criteria) this;
        }

        public Criteria andBankcardIdLessThan(String value) {
            addCriterion("bankcardId <", value, "bankcardId");
            return (Criteria) this;
        }

        public Criteria andBankcardIdLessThanOrEqualTo(String value) {
            addCriterion("bankcardId <=", value, "bankcardId");
            return (Criteria) this;
        }

        public Criteria andBankcardIdLike(String value) {
            addCriterion("bankcardId like", value, "bankcardId");
            return (Criteria) this;
        }

        public Criteria andBankcardIdNotLike(String value) {
            addCriterion("bankcardId not like", value, "bankcardId");
            return (Criteria) this;
        }

        public Criteria andBankcardIdIn(List<String> values) {
            addCriterion("bankcardId in", values, "bankcardId");
            return (Criteria) this;
        }

        public Criteria andBankcardIdNotIn(List<String> values) {
            addCriterion("bankcardId not in", values, "bankcardId");
            return (Criteria) this;
        }

        public Criteria andBankcardIdBetween(String value1, String value2) {
            addCriterion("bankcardId between", value1, value2, "bankcardId");
            return (Criteria) this;
        }

        public Criteria andBankcardIdNotBetween(String value1, String value2) {
            addCriterion("bankcardId not between", value1, value2, "bankcardId");
            return (Criteria) this;
        }

        public Criteria andBankcardAccountIsNull() {
            addCriterion("bankcardAccount is null");
            return (Criteria) this;
        }

        public Criteria andBankcardAccountIsNotNull() {
            addCriterion("bankcardAccount is not null");
            return (Criteria) this;
        }

        public Criteria andBankcardAccountEqualTo(String value) {
            addCriterion("bankcardAccount =", value, "bankcardAccount");
            return (Criteria) this;
        }

        public Criteria andBankcardAccountNotEqualTo(String value) {
            addCriterion("bankcardAccount <>", value, "bankcardAccount");
            return (Criteria) this;
        }

        public Criteria andBankcardAccountGreaterThan(String value) {
            addCriterion("bankcardAccount >", value, "bankcardAccount");
            return (Criteria) this;
        }

        public Criteria andBankcardAccountGreaterThanOrEqualTo(String value) {
            addCriterion("bankcardAccount >=", value, "bankcardAccount");
            return (Criteria) this;
        }

        public Criteria andBankcardAccountLessThan(String value) {
            addCriterion("bankcardAccount <", value, "bankcardAccount");
            return (Criteria) this;
        }

        public Criteria andBankcardAccountLessThanOrEqualTo(String value) {
            addCriterion("bankcardAccount <=", value, "bankcardAccount");
            return (Criteria) this;
        }

        public Criteria andBankcardAccountLike(String value) {
            addCriterion("bankcardAccount like", value, "bankcardAccount");
            return (Criteria) this;
        }

        public Criteria andBankcardAccountNotLike(String value) {
            addCriterion("bankcardAccount not like", value, "bankcardAccount");
            return (Criteria) this;
        }

        public Criteria andBankcardAccountIn(List<String> values) {
            addCriterion("bankcardAccount in", values, "bankcardAccount");
            return (Criteria) this;
        }

        public Criteria andBankcardAccountNotIn(List<String> values) {
            addCriterion("bankcardAccount not in", values, "bankcardAccount");
            return (Criteria) this;
        }

        public Criteria andBankcardAccountBetween(String value1, String value2) {
            addCriterion("bankcardAccount between", value1, value2, "bankcardAccount");
            return (Criteria) this;
        }

        public Criteria andBankcardAccountNotBetween(String value1, String value2) {
            addCriterion("bankcardAccount not between", value1, value2, "bankcardAccount");
            return (Criteria) this;
        }

        public Criteria andAccountHolderIsNull() {
            addCriterion("accountHolder is null");
            return (Criteria) this;
        }

        public Criteria andAccountHolderIsNotNull() {
            addCriterion("accountHolder is not null");
            return (Criteria) this;
        }

        public Criteria andAccountHolderEqualTo(String value) {
            addCriterion("accountHolder =", value, "accountHolder");
            return (Criteria) this;
        }

        public Criteria andAccountHolderNotEqualTo(String value) {
            addCriterion("accountHolder <>", value, "accountHolder");
            return (Criteria) this;
        }

        public Criteria andAccountHolderGreaterThan(String value) {
            addCriterion("accountHolder >", value, "accountHolder");
            return (Criteria) this;
        }

        public Criteria andAccountHolderGreaterThanOrEqualTo(String value) {
            addCriterion("accountHolder >=", value, "accountHolder");
            return (Criteria) this;
        }

        public Criteria andAccountHolderLessThan(String value) {
            addCriterion("accountHolder <", value, "accountHolder");
            return (Criteria) this;
        }

        public Criteria andAccountHolderLessThanOrEqualTo(String value) {
            addCriterion("accountHolder <=", value, "accountHolder");
            return (Criteria) this;
        }

        public Criteria andAccountHolderLike(String value) {
            addCriterion("accountHolder like", value, "accountHolder");
            return (Criteria) this;
        }

        public Criteria andAccountHolderNotLike(String value) {
            addCriterion("accountHolder not like", value, "accountHolder");
            return (Criteria) this;
        }

        public Criteria andAccountHolderIn(List<String> values) {
            addCriterion("accountHolder in", values, "accountHolder");
            return (Criteria) this;
        }

        public Criteria andAccountHolderNotIn(List<String> values) {
            addCriterion("accountHolder not in", values, "accountHolder");
            return (Criteria) this;
        }

        public Criteria andAccountHolderBetween(String value1, String value2) {
            addCriterion("accountHolder between", value1, value2, "accountHolder");
            return (Criteria) this;
        }

        public Criteria andAccountHolderNotBetween(String value1, String value2) {
            addCriterion("accountHolder not between", value1, value2, "accountHolder");
            return (Criteria) this;
        }

        public Criteria andOpenAccountBankIsNull() {
            addCriterion("openAccountBank is null");
            return (Criteria) this;
        }

        public Criteria andOpenAccountBankIsNotNull() {
            addCriterion("openAccountBank is not null");
            return (Criteria) this;
        }

        public Criteria andOpenAccountBankEqualTo(String value) {
            addCriterion("openAccountBank =", value, "openAccountBank");
            return (Criteria) this;
        }

        public Criteria andOpenAccountBankNotEqualTo(String value) {
            addCriterion("openAccountBank <>", value, "openAccountBank");
            return (Criteria) this;
        }

        public Criteria andOpenAccountBankGreaterThan(String value) {
            addCriterion("openAccountBank >", value, "openAccountBank");
            return (Criteria) this;
        }

        public Criteria andOpenAccountBankGreaterThanOrEqualTo(String value) {
            addCriterion("openAccountBank >=", value, "openAccountBank");
            return (Criteria) this;
        }

        public Criteria andOpenAccountBankLessThan(String value) {
            addCriterion("openAccountBank <", value, "openAccountBank");
            return (Criteria) this;
        }

        public Criteria andOpenAccountBankLessThanOrEqualTo(String value) {
            addCriterion("openAccountBank <=", value, "openAccountBank");
            return (Criteria) this;
        }

        public Criteria andOpenAccountBankLike(String value) {
            addCriterion("openAccountBank like", value, "openAccountBank");
            return (Criteria) this;
        }

        public Criteria andOpenAccountBankNotLike(String value) {
            addCriterion("openAccountBank not like", value, "openAccountBank");
            return (Criteria) this;
        }

        public Criteria andOpenAccountBankIn(List<String> values) {
            addCriterion("openAccountBank in", values, "openAccountBank");
            return (Criteria) this;
        }

        public Criteria andOpenAccountBankNotIn(List<String> values) {
            addCriterion("openAccountBank not in", values, "openAccountBank");
            return (Criteria) this;
        }

        public Criteria andOpenAccountBankBetween(String value1, String value2) {
            addCriterion("openAccountBank between", value1, value2, "openAccountBank");
            return (Criteria) this;
        }

        public Criteria andOpenAccountBankNotBetween(String value1, String value2) {
            addCriterion("openAccountBank not between", value1, value2, "openAccountBank");
            return (Criteria) this;
        }

        public Criteria andBankTypeIsNull() {
            addCriterion("bankType is null");
            return (Criteria) this;
        }

        public Criteria andBankTypeIsNotNull() {
            addCriterion("bankType is not null");
            return (Criteria) this;
        }

        public Criteria andBankTypeEqualTo(String value) {
            addCriterion("bankType =", value, "bankType");
            return (Criteria) this;
        }

        public Criteria andBankTypeNotEqualTo(String value) {
            addCriterion("bankType <>", value, "bankType");
            return (Criteria) this;
        }

        public Criteria andBankTypeGreaterThan(String value) {
            addCriterion("bankType >", value, "bankType");
            return (Criteria) this;
        }

        public Criteria andBankTypeGreaterThanOrEqualTo(String value) {
            addCriterion("bankType >=", value, "bankType");
            return (Criteria) this;
        }

        public Criteria andBankTypeLessThan(String value) {
            addCriterion("bankType <", value, "bankType");
            return (Criteria) this;
        }

        public Criteria andBankTypeLessThanOrEqualTo(String value) {
            addCriterion("bankType <=", value, "bankType");
            return (Criteria) this;
        }

        public Criteria andBankTypeLike(String value) {
            addCriterion("bankType like", value, "bankType");
            return (Criteria) this;
        }

        public Criteria andBankTypeNotLike(String value) {
            addCriterion("bankType not like", value, "bankType");
            return (Criteria) this;
        }

        public Criteria andBankTypeIn(List<String> values) {
            addCriterion("bankType in", values, "bankType");
            return (Criteria) this;
        }

        public Criteria andBankTypeNotIn(List<String> values) {
            addCriterion("bankType not in", values, "bankType");
            return (Criteria) this;
        }

        public Criteria andBankTypeBetween(String value1, String value2) {
            addCriterion("bankType between", value1, value2, "bankType");
            return (Criteria) this;
        }

        public Criteria andBankTypeNotBetween(String value1, String value2) {
            addCriterion("bankType not between", value1, value2, "bankType");
            return (Criteria) this;
        }

        public Criteria andBankcodeIsNull() {
            addCriterion("bankcode is null");
            return (Criteria) this;
        }

        public Criteria andBankcodeIsNotNull() {
            addCriterion("bankcode is not null");
            return (Criteria) this;
        }

        public Criteria andBankcodeEqualTo(String value) {
            addCriterion("bankcode =", value, "bankcode");
            return (Criteria) this;
        }

        public Criteria andBankcodeNotEqualTo(String value) {
            addCriterion("bankcode <>", value, "bankcode");
            return (Criteria) this;
        }

        public Criteria andBankcodeGreaterThan(String value) {
            addCriterion("bankcode >", value, "bankcode");
            return (Criteria) this;
        }

        public Criteria andBankcodeGreaterThanOrEqualTo(String value) {
            addCriterion("bankcode >=", value, "bankcode");
            return (Criteria) this;
        }

        public Criteria andBankcodeLessThan(String value) {
            addCriterion("bankcode <", value, "bankcode");
            return (Criteria) this;
        }

        public Criteria andBankcodeLessThanOrEqualTo(String value) {
            addCriterion("bankcode <=", value, "bankcode");
            return (Criteria) this;
        }

        public Criteria andBankcodeLike(String value) {
            addCriterion("bankcode like", value, "bankcode");
            return (Criteria) this;
        }

        public Criteria andBankcodeNotLike(String value) {
            addCriterion("bankcode not like", value, "bankcode");
            return (Criteria) this;
        }

        public Criteria andBankcodeIn(List<String> values) {
            addCriterion("bankcode in", values, "bankcode");
            return (Criteria) this;
        }

        public Criteria andBankcodeNotIn(List<String> values) {
            addCriterion("bankcode not in", values, "bankcode");
            return (Criteria) this;
        }

        public Criteria andBankcodeBetween(String value1, String value2) {
            addCriterion("bankcode between", value1, value2, "bankcode");
            return (Criteria) this;
        }

        public Criteria andBankcodeNotBetween(String value1, String value2) {
            addCriterion("bankcode not between", value1, value2, "bankcode");
            return (Criteria) this;
        }

        public Criteria andSysTYpeIsNull() {
            addCriterion("sysTYpe is null");
            return (Criteria) this;
        }

        public Criteria andSysTYpeIsNotNull() {
            addCriterion("sysTYpe is not null");
            return (Criteria) this;
        }

        public Criteria andSysTYpeEqualTo(Integer value) {
            addCriterion("sysTYpe =", value, "sysTYpe");
            return (Criteria) this;
        }

        public Criteria andSysTYpeNotEqualTo(Integer value) {
            addCriterion("sysTYpe <>", value, "sysTYpe");
            return (Criteria) this;
        }

        public Criteria andSysTYpeGreaterThan(Integer value) {
            addCriterion("sysTYpe >", value, "sysTYpe");
            return (Criteria) this;
        }

        public Criteria andSysTYpeGreaterThanOrEqualTo(Integer value) {
            addCriterion("sysTYpe >=", value, "sysTYpe");
            return (Criteria) this;
        }

        public Criteria andSysTYpeLessThan(Integer value) {
            addCriterion("sysTYpe <", value, "sysTYpe");
            return (Criteria) this;
        }

        public Criteria andSysTYpeLessThanOrEqualTo(Integer value) {
            addCriterion("sysTYpe <=", value, "sysTYpe");
            return (Criteria) this;
        }

        public Criteria andSysTYpeIn(List<Integer> values) {
            addCriterion("sysTYpe in", values, "sysTYpe");
            return (Criteria) this;
        }

        public Criteria andSysTYpeNotIn(List<Integer> values) {
            addCriterion("sysTYpe not in", values, "sysTYpe");
            return (Criteria) this;
        }

        public Criteria andSysTYpeBetween(Integer value1, Integer value2) {
            addCriterion("sysTYpe between", value1, value2, "sysTYpe");
            return (Criteria) this;
        }

        public Criteria andSysTYpeNotBetween(Integer value1, Integer value2) {
            addCriterion("sysTYpe not between", value1, value2, "sysTYpe");
            return (Criteria) this;
        }

        public Criteria andAccountIsNull() {
            addCriterion("account is null");
            return (Criteria) this;
        }

        public Criteria andAccountIsNotNull() {
            addCriterion("account is not null");
            return (Criteria) this;
        }

        public Criteria andAccountEqualTo(String value) {
            addCriterion("account =", value, "account");
            return (Criteria) this;
        }

        public Criteria andAccountNotEqualTo(String value) {
            addCriterion("account <>", value, "account");
            return (Criteria) this;
        }

        public Criteria andAccountGreaterThan(String value) {
            addCriterion("account >", value, "account");
            return (Criteria) this;
        }

        public Criteria andAccountGreaterThanOrEqualTo(String value) {
            addCriterion("account >=", value, "account");
            return (Criteria) this;
        }

        public Criteria andAccountLessThan(String value) {
            addCriterion("account <", value, "account");
            return (Criteria) this;
        }

        public Criteria andAccountLessThanOrEqualTo(String value) {
            addCriterion("account <=", value, "account");
            return (Criteria) this;
        }

        public Criteria andAccountLike(String value) {
            addCriterion("account like", value, "account");
            return (Criteria) this;
        }

        public Criteria andAccountNotLike(String value) {
            addCriterion("account not like", value, "account");
            return (Criteria) this;
        }

        public Criteria andAccountIn(List<String> values) {
            addCriterion("account in", values, "account");
            return (Criteria) this;
        }

        public Criteria andAccountNotIn(List<String> values) {
            addCriterion("account not in", values, "account");
            return (Criteria) this;
        }

        public Criteria andAccountBetween(String value1, String value2) {
            addCriterion("account between", value1, value2, "account");
            return (Criteria) this;
        }

        public Criteria andAccountNotBetween(String value1, String value2) {
            addCriterion("account not between", value1, value2, "account");
            return (Criteria) this;
        }

        public Criteria andPhoneIsNull() {
            addCriterion("phone is null");
            return (Criteria) this;
        }

        public Criteria andPhoneIsNotNull() {
            addCriterion("phone is not null");
            return (Criteria) this;
        }

        public Criteria andPhoneEqualTo(String value) {
            addCriterion("phone =", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneNotEqualTo(String value) {
            addCriterion("phone <>", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneGreaterThan(String value) {
            addCriterion("phone >", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneGreaterThanOrEqualTo(String value) {
            addCriterion("phone >=", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneLessThan(String value) {
            addCriterion("phone <", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneLessThanOrEqualTo(String value) {
            addCriterion("phone <=", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneLike(String value) {
            addCriterion("phone like", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneNotLike(String value) {
            addCriterion("phone not like", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneIn(List<String> values) {
            addCriterion("phone in", values, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneNotIn(List<String> values) {
            addCriterion("phone not in", values, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneBetween(String value1, String value2) {
            addCriterion("phone between", value1, value2, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneNotBetween(String value1, String value2) {
            addCriterion("phone not between", value1, value2, "phone");
            return (Criteria) this;
        }

        public Criteria andLimitAmountIsNull() {
            addCriterion("limitAmount is null");
            return (Criteria) this;
        }

        public Criteria andLimitAmountIsNotNull() {
            addCriterion("limitAmount is not null");
            return (Criteria) this;
        }

        public Criteria andLimitAmountEqualTo(BigDecimal value) {
            addCriterion("limitAmount =", value, "limitAmount");
            return (Criteria) this;
        }

        public Criteria andLimitAmountNotEqualTo(BigDecimal value) {
            addCriterion("limitAmount <>", value, "limitAmount");
            return (Criteria) this;
        }

        public Criteria andLimitAmountGreaterThan(BigDecimal value) {
            addCriterion("limitAmount >", value, "limitAmount");
            return (Criteria) this;
        }

        public Criteria andLimitAmountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("limitAmount >=", value, "limitAmount");
            return (Criteria) this;
        }

        public Criteria andLimitAmountLessThan(BigDecimal value) {
            addCriterion("limitAmount <", value, "limitAmount");
            return (Criteria) this;
        }

        public Criteria andLimitAmountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("limitAmount <=", value, "limitAmount");
            return (Criteria) this;
        }

        public Criteria andLimitAmountIn(List<BigDecimal> values) {
            addCriterion("limitAmount in", values, "limitAmount");
            return (Criteria) this;
        }

        public Criteria andLimitAmountNotIn(List<BigDecimal> values) {
            addCriterion("limitAmount not in", values, "limitAmount");
            return (Criteria) this;
        }

        public Criteria andLimitAmountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("limitAmount between", value1, value2, "limitAmount");
            return (Criteria) this;
        }

        public Criteria andLimitAmountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("limitAmount not between", value1, value2, "limitAmount");
            return (Criteria) this;
        }

        public Criteria andBankAmountIsNull() {
            addCriterion("bankAmount is null");
            return (Criteria) this;
        }

        public Criteria andBankAmountIsNotNull() {
            addCriterion("bankAmount is not null");
            return (Criteria) this;
        }

        public Criteria andBankAmountEqualTo(BigDecimal value) {
            addCriterion("bankAmount =", value, "bankAmount");
            return (Criteria) this;
        }

        public Criteria andBankAmountNotEqualTo(BigDecimal value) {
            addCriterion("bankAmount <>", value, "bankAmount");
            return (Criteria) this;
        }

        public Criteria andBankAmountGreaterThan(BigDecimal value) {
            addCriterion("bankAmount >", value, "bankAmount");
            return (Criteria) this;
        }

        public Criteria andBankAmountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("bankAmount >=", value, "bankAmount");
            return (Criteria) this;
        }

        public Criteria andBankAmountLessThan(BigDecimal value) {
            addCriterion("bankAmount <", value, "bankAmount");
            return (Criteria) this;
        }

        public Criteria andBankAmountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("bankAmount <=", value, "bankAmount");
            return (Criteria) this;
        }

        public Criteria andBankAmountIn(List<BigDecimal> values) {
            addCriterion("bankAmount in", values, "bankAmount");
            return (Criteria) this;
        }

        public Criteria andBankAmountNotIn(List<BigDecimal> values) {
            addCriterion("bankAmount not in", values, "bankAmount");
            return (Criteria) this;
        }

        public Criteria andBankAmountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("bankAmount between", value1, value2, "bankAmount");
            return (Criteria) this;
        }

        public Criteria andBankAmountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("bankAmount not between", value1, value2, "bankAmount");
            return (Criteria) this;
        }

        public Criteria andCardTypeIsNull() {
            addCriterion("cardType is null");
            return (Criteria) this;
        }

        public Criteria andCardTypeIsNotNull() {
            addCriterion("cardType is not null");
            return (Criteria) this;
        }

        public Criteria andCardTypeEqualTo(Integer value) {
            addCriterion("cardType =", value, "cardType");
            return (Criteria) this;
        }

        public Criteria andCardTypeNotEqualTo(Integer value) {
            addCriterion("cardType <>", value, "cardType");
            return (Criteria) this;
        }

        public Criteria andCardTypeGreaterThan(Integer value) {
            addCriterion("cardType >", value, "cardType");
            return (Criteria) this;
        }

        public Criteria andCardTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("cardType >=", value, "cardType");
            return (Criteria) this;
        }

        public Criteria andCardTypeLessThan(Integer value) {
            addCriterion("cardType <", value, "cardType");
            return (Criteria) this;
        }

        public Criteria andCardTypeLessThanOrEqualTo(Integer value) {
            addCriterion("cardType <=", value, "cardType");
            return (Criteria) this;
        }

        public Criteria andCardTypeIn(List<Integer> values) {
            addCriterion("cardType in", values, "cardType");
            return (Criteria) this;
        }

        public Criteria andCardTypeNotIn(List<Integer> values) {
            addCriterion("cardType not in", values, "cardType");
            return (Criteria) this;
        }

        public Criteria andCardTypeBetween(Integer value1, Integer value2) {
            addCriterion("cardType between", value1, value2, "cardType");
            return (Criteria) this;
        }

        public Criteria andCardTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("cardType not between", value1, value2, "cardType");
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

        public Criteria andStatusEqualTo(String value) {
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

        public Criteria andIsDealIsNull() {
            addCriterion("isDeal is null");
            return (Criteria) this;
        }

        public Criteria andIsDealIsNotNull() {
            addCriterion("isDeal is not null");
            return (Criteria) this;
        }

        public Criteria andIsDealEqualTo(Integer value) {
            addCriterion("isDeal =", value, "isDeal");
            return (Criteria) this;
        }

        public Criteria andIsDealNotEqualTo(Integer value) {
            addCriterion("isDeal <>", value, "isDeal");
            return (Criteria) this;
        }

        public Criteria andIsDealGreaterThan(Integer value) {
            addCriterion("isDeal >", value, "isDeal");
            return (Criteria) this;
        }

        public Criteria andIsDealGreaterThanOrEqualTo(Integer value) {
            addCriterion("isDeal >=", value, "isDeal");
            return (Criteria) this;
        }

        public Criteria andIsDealLessThan(Integer value) {
            addCriterion("isDeal <", value, "isDeal");
            return (Criteria) this;
        }

        public Criteria andIsDealLessThanOrEqualTo(Integer value) {
            addCriterion("isDeal <=", value, "isDeal");
            return (Criteria) this;
        }

        public Criteria andIsDealIn(List<Integer> values) {
            addCriterion("isDeal in", values, "isDeal");
            return (Criteria) this;
        }

        public Criteria andIsDealNotIn(List<Integer> values) {
            addCriterion("isDeal not in", values, "isDeal");
            return (Criteria) this;
        }

        public Criteria andIsDealBetween(Integer value1, Integer value2) {
            addCriterion("isDeal between", value1, value2, "isDeal");
            return (Criteria) this;
        }

        public Criteria andIsDealNotBetween(Integer value1, Integer value2) {
            addCriterion("isDeal not between", value1, value2, "isDeal");
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