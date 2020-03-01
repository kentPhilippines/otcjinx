package alipay.manage.bean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RunOrderExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public RunOrderExample() {
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

        public Criteria andOrderIdIsNull() {
            addCriterion("orderId is null");
            return (Criteria) this;
        }

        public Criteria andOrderIdIsNotNull() {
            addCriterion("orderId is not null");
            return (Criteria) this;
        }

        public Criteria andOrderIdEqualTo(String value) {
            addCriterion("orderId =", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdNotEqualTo(String value) {
            addCriterion("orderId <>", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdGreaterThan(String value) {
            addCriterion("orderId >", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdGreaterThanOrEqualTo(String value) {
            addCriterion("orderId >=", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdLessThan(String value) {
            addCriterion("orderId <", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdLessThanOrEqualTo(String value) {
            addCriterion("orderId <=", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdLike(String value) {
            addCriterion("orderId like", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdNotLike(String value) {
            addCriterion("orderId not like", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdIn(List<String> values) {
            addCriterion("orderId in", values, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdNotIn(List<String> values) {
            addCriterion("orderId not in", values, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdBetween(String value1, String value2) {
            addCriterion("orderId between", value1, value2, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdNotBetween(String value1, String value2) {
            addCriterion("orderId not between", value1, value2, "orderId");
            return (Criteria) this;
        }

        public Criteria andAssociatedIdIsNull() {
            addCriterion("associatedId is null");
            return (Criteria) this;
        }

        public Criteria andAssociatedIdIsNotNull() {
            addCriterion("associatedId is not null");
            return (Criteria) this;
        }

        public Criteria andAssociatedIdEqualTo(String value) {
            addCriterion("associatedId =", value, "associatedId");
            return (Criteria) this;
        }

        public Criteria andAssociatedIdNotEqualTo(String value) {
            addCriterion("associatedId <>", value, "associatedId");
            return (Criteria) this;
        }

        public Criteria andAssociatedIdGreaterThan(String value) {
            addCriterion("associatedId >", value, "associatedId");
            return (Criteria) this;
        }

        public Criteria andAssociatedIdGreaterThanOrEqualTo(String value) {
            addCriterion("associatedId >=", value, "associatedId");
            return (Criteria) this;
        }

        public Criteria andAssociatedIdLessThan(String value) {
            addCriterion("associatedId <", value, "associatedId");
            return (Criteria) this;
        }

        public Criteria andAssociatedIdLessThanOrEqualTo(String value) {
            addCriterion("associatedId <=", value, "associatedId");
            return (Criteria) this;
        }

        public Criteria andAssociatedIdLike(String value) {
            addCriterion("associatedId like", value, "associatedId");
            return (Criteria) this;
        }

        public Criteria andAssociatedIdNotLike(String value) {
            addCriterion("associatedId not like", value, "associatedId");
            return (Criteria) this;
        }

        public Criteria andAssociatedIdIn(List<String> values) {
            addCriterion("associatedId in", values, "associatedId");
            return (Criteria) this;
        }

        public Criteria andAssociatedIdNotIn(List<String> values) {
            addCriterion("associatedId not in", values, "associatedId");
            return (Criteria) this;
        }

        public Criteria andAssociatedIdBetween(String value1, String value2) {
            addCriterion("associatedId between", value1, value2, "associatedId");
            return (Criteria) this;
        }

        public Criteria andAssociatedIdNotBetween(String value1, String value2) {
            addCriterion("associatedId not between", value1, value2, "associatedId");
            return (Criteria) this;
        }

        public Criteria andOrderAccountIsNull() {
            addCriterion("orderAccount is null");
            return (Criteria) this;
        }

        public Criteria andOrderAccountIsNotNull() {
            addCriterion("orderAccount is not null");
            return (Criteria) this;
        }

        public Criteria andOrderAccountEqualTo(String value) {
            addCriterion("orderAccount =", value, "orderAccount");
            return (Criteria) this;
        }

        public Criteria andOrderAccountNotEqualTo(String value) {
            addCriterion("orderAccount <>", value, "orderAccount");
            return (Criteria) this;
        }

        public Criteria andOrderAccountGreaterThan(String value) {
            addCriterion("orderAccount >", value, "orderAccount");
            return (Criteria) this;
        }

        public Criteria andOrderAccountGreaterThanOrEqualTo(String value) {
            addCriterion("orderAccount >=", value, "orderAccount");
            return (Criteria) this;
        }

        public Criteria andOrderAccountLessThan(String value) {
            addCriterion("orderAccount <", value, "orderAccount");
            return (Criteria) this;
        }

        public Criteria andOrderAccountLessThanOrEqualTo(String value) {
            addCriterion("orderAccount <=", value, "orderAccount");
            return (Criteria) this;
        }

        public Criteria andOrderAccountLike(String value) {
            addCriterion("orderAccount like", value, "orderAccount");
            return (Criteria) this;
        }

        public Criteria andOrderAccountNotLike(String value) {
            addCriterion("orderAccount not like", value, "orderAccount");
            return (Criteria) this;
        }

        public Criteria andOrderAccountIn(List<String> values) {
            addCriterion("orderAccount in", values, "orderAccount");
            return (Criteria) this;
        }

        public Criteria andOrderAccountNotIn(List<String> values) {
            addCriterion("orderAccount not in", values, "orderAccount");
            return (Criteria) this;
        }

        public Criteria andOrderAccountBetween(String value1, String value2) {
            addCriterion("orderAccount between", value1, value2, "orderAccount");
            return (Criteria) this;
        }

        public Criteria andOrderAccountNotBetween(String value1, String value2) {
            addCriterion("orderAccount not between", value1, value2, "orderAccount");
            return (Criteria) this;
        }

        public Criteria andRunOrderTypeIsNull() {
            addCriterion("runOrderType is null");
            return (Criteria) this;
        }

        public Criteria andRunOrderTypeIsNotNull() {
            addCriterion("runOrderType is not null");
            return (Criteria) this;
        }

        public Criteria andRunOrderTypeEqualTo(Integer value) {
            addCriterion("runOrderType =", value, "runOrderType");
            return (Criteria) this;
        }

        public Criteria andRunOrderTypeNotEqualTo(Integer value) {
            addCriterion("runOrderType <>", value, "runOrderType");
            return (Criteria) this;
        }

        public Criteria andRunOrderTypeGreaterThan(Integer value) {
            addCriterion("runOrderType >", value, "runOrderType");
            return (Criteria) this;
        }

        public Criteria andRunOrderTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("runOrderType >=", value, "runOrderType");
            return (Criteria) this;
        }

        public Criteria andRunOrderTypeLessThan(Integer value) {
            addCriterion("runOrderType <", value, "runOrderType");
            return (Criteria) this;
        }

        public Criteria andRunOrderTypeLessThanOrEqualTo(Integer value) {
            addCriterion("runOrderType <=", value, "runOrderType");
            return (Criteria) this;
        }

        public Criteria andRunOrderTypeIn(List<Integer> values) {
            addCriterion("runOrderType in", values, "runOrderType");
            return (Criteria) this;
        }

        public Criteria andRunOrderTypeNotIn(List<Integer> values) {
            addCriterion("runOrderType not in", values, "runOrderType");
            return (Criteria) this;
        }

        public Criteria andRunOrderTypeBetween(Integer value1, Integer value2) {
            addCriterion("runOrderType between", value1, value2, "runOrderType");
            return (Criteria) this;
        }

        public Criteria andRunOrderTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("runOrderType not between", value1, value2, "runOrderType");
            return (Criteria) this;
        }

        public Criteria andAmountIsNull() {
            addCriterion("amount is null");
            return (Criteria) this;
        }

        public Criteria andAmountIsNotNull() {
            addCriterion("amount is not null");
            return (Criteria) this;
        }

        public Criteria andAmountEqualTo(BigDecimal value) {
            addCriterion("amount =", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountNotEqualTo(BigDecimal value) {
            addCriterion("amount <>", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountGreaterThan(BigDecimal value) {
            addCriterion("amount >", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("amount >=", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountLessThan(BigDecimal value) {
            addCriterion("amount <", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("amount <=", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountIn(List<BigDecimal> values) {
            addCriterion("amount in", values, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountNotIn(List<BigDecimal> values) {
            addCriterion("amount not in", values, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("amount between", value1, value2, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("amount not between", value1, value2, "amount");
            return (Criteria) this;
        }

        public Criteria andGenerationIpIsNull() {
            addCriterion("generationIp is null");
            return (Criteria) this;
        }

        public Criteria andGenerationIpIsNotNull() {
            addCriterion("generationIp is not null");
            return (Criteria) this;
        }

        public Criteria andGenerationIpEqualTo(String value) {
            addCriterion("generationIp =", value, "generationIp");
            return (Criteria) this;
        }

        public Criteria andGenerationIpNotEqualTo(String value) {
            addCriterion("generationIp <>", value, "generationIp");
            return (Criteria) this;
        }

        public Criteria andGenerationIpGreaterThan(String value) {
            addCriterion("generationIp >", value, "generationIp");
            return (Criteria) this;
        }

        public Criteria andGenerationIpGreaterThanOrEqualTo(String value) {
            addCriterion("generationIp >=", value, "generationIp");
            return (Criteria) this;
        }

        public Criteria andGenerationIpLessThan(String value) {
            addCriterion("generationIp <", value, "generationIp");
            return (Criteria) this;
        }

        public Criteria andGenerationIpLessThanOrEqualTo(String value) {
            addCriterion("generationIp <=", value, "generationIp");
            return (Criteria) this;
        }

        public Criteria andGenerationIpLike(String value) {
            addCriterion("generationIp like", value, "generationIp");
            return (Criteria) this;
        }

        public Criteria andGenerationIpNotLike(String value) {
            addCriterion("generationIp not like", value, "generationIp");
            return (Criteria) this;
        }

        public Criteria andGenerationIpIn(List<String> values) {
            addCriterion("generationIp in", values, "generationIp");
            return (Criteria) this;
        }

        public Criteria andGenerationIpNotIn(List<String> values) {
            addCriterion("generationIp not in", values, "generationIp");
            return (Criteria) this;
        }

        public Criteria andGenerationIpBetween(String value1, String value2) {
            addCriterion("generationIp between", value1, value2, "generationIp");
            return (Criteria) this;
        }

        public Criteria andGenerationIpNotBetween(String value1, String value2) {
            addCriterion("generationIp not between", value1, value2, "generationIp");
            return (Criteria) this;
        }

        public Criteria andAcountRIsNull() {
            addCriterion("acountR is null");
            return (Criteria) this;
        }

        public Criteria andAcountRIsNotNull() {
            addCriterion("acountR is not null");
            return (Criteria) this;
        }

        public Criteria andAcountREqualTo(String value) {
            addCriterion("acountR =", value, "acountR");
            return (Criteria) this;
        }

        public Criteria andAcountRNotEqualTo(String value) {
            addCriterion("acountR <>", value, "acountR");
            return (Criteria) this;
        }

        public Criteria andAcountRGreaterThan(String value) {
            addCriterion("acountR >", value, "acountR");
            return (Criteria) this;
        }

        public Criteria andAcountRGreaterThanOrEqualTo(String value) {
            addCriterion("acountR >=", value, "acountR");
            return (Criteria) this;
        }

        public Criteria andAcountRLessThan(String value) {
            addCriterion("acountR <", value, "acountR");
            return (Criteria) this;
        }

        public Criteria andAcountRLessThanOrEqualTo(String value) {
            addCriterion("acountR <=", value, "acountR");
            return (Criteria) this;
        }

        public Criteria andAcountRLike(String value) {
            addCriterion("acountR like", value, "acountR");
            return (Criteria) this;
        }

        public Criteria andAcountRNotLike(String value) {
            addCriterion("acountR not like", value, "acountR");
            return (Criteria) this;
        }

        public Criteria andAcountRIn(List<String> values) {
            addCriterion("acountR in", values, "acountR");
            return (Criteria) this;
        }

        public Criteria andAcountRNotIn(List<String> values) {
            addCriterion("acountR not in", values, "acountR");
            return (Criteria) this;
        }

        public Criteria andAcountRBetween(String value1, String value2) {
            addCriterion("acountR between", value1, value2, "acountR");
            return (Criteria) this;
        }

        public Criteria andAcountRNotBetween(String value1, String value2) {
            addCriterion("acountR not between", value1, value2, "acountR");
            return (Criteria) this;
        }

        public Criteria andAccountWIsNull() {
            addCriterion("accountW is null");
            return (Criteria) this;
        }

        public Criteria andAccountWIsNotNull() {
            addCriterion("accountW is not null");
            return (Criteria) this;
        }

        public Criteria andAccountWEqualTo(String value) {
            addCriterion("accountW =", value, "accountW");
            return (Criteria) this;
        }

        public Criteria andAccountWNotEqualTo(String value) {
            addCriterion("accountW <>", value, "accountW");
            return (Criteria) this;
        }

        public Criteria andAccountWGreaterThan(String value) {
            addCriterion("accountW >", value, "accountW");
            return (Criteria) this;
        }

        public Criteria andAccountWGreaterThanOrEqualTo(String value) {
            addCriterion("accountW >=", value, "accountW");
            return (Criteria) this;
        }

        public Criteria andAccountWLessThan(String value) {
            addCriterion("accountW <", value, "accountW");
            return (Criteria) this;
        }

        public Criteria andAccountWLessThanOrEqualTo(String value) {
            addCriterion("accountW <=", value, "accountW");
            return (Criteria) this;
        }

        public Criteria andAccountWLike(String value) {
            addCriterion("accountW like", value, "accountW");
            return (Criteria) this;
        }

        public Criteria andAccountWNotLike(String value) {
            addCriterion("accountW not like", value, "accountW");
            return (Criteria) this;
        }

        public Criteria andAccountWIn(List<String> values) {
            addCriterion("accountW in", values, "accountW");
            return (Criteria) this;
        }

        public Criteria andAccountWNotIn(List<String> values) {
            addCriterion("accountW not in", values, "accountW");
            return (Criteria) this;
        }

        public Criteria andAccountWBetween(String value1, String value2) {
            addCriterion("accountW between", value1, value2, "accountW");
            return (Criteria) this;
        }

        public Criteria andAccountWNotBetween(String value1, String value2) {
            addCriterion("accountW not between", value1, value2, "accountW");
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

        public Criteria andRunTypeIsNull() {
            addCriterion("runType is null");
            return (Criteria) this;
        }

        public Criteria andRunTypeIsNotNull() {
            addCriterion("runType is not null");
            return (Criteria) this;
        }

        public Criteria andRunTypeEqualTo(String value) {
            addCriterion("runType =", value, "runType");
            return (Criteria) this;
        }

        public Criteria andRunTypeNotEqualTo(String value) {
            addCriterion("runType <>", value, "runType");
            return (Criteria) this;
        }

        public Criteria andRunTypeGreaterThan(String value) {
            addCriterion("runType >", value, "runType");
            return (Criteria) this;
        }

        public Criteria andRunTypeGreaterThanOrEqualTo(String value) {
            addCriterion("runType >=", value, "runType");
            return (Criteria) this;
        }

        public Criteria andRunTypeLessThan(String value) {
            addCriterion("runType <", value, "runType");
            return (Criteria) this;
        }

        public Criteria andRunTypeLessThanOrEqualTo(String value) {
            addCriterion("runType <=", value, "runType");
            return (Criteria) this;
        }

        public Criteria andRunTypeLike(String value) {
            addCriterion("runType like", value, "runType");
            return (Criteria) this;
        }

        public Criteria andRunTypeNotLike(String value) {
            addCriterion("runType not like", value, "runType");
            return (Criteria) this;
        }

        public Criteria andRunTypeIn(List<String> values) {
            addCriterion("runType in", values, "runType");
            return (Criteria) this;
        }

        public Criteria andRunTypeNotIn(List<String> values) {
            addCriterion("runType not in", values, "runType");
            return (Criteria) this;
        }

        public Criteria andRunTypeBetween(String value1, String value2) {
            addCriterion("runType between", value1, value2, "runType");
            return (Criteria) this;
        }

        public Criteria andRunTypeNotBetween(String value1, String value2) {
            addCriterion("runType not between", value1, value2, "runType");
            return (Criteria) this;
        }

        public Criteria andAmountTypeIsNull() {
            addCriterion("amountType is null");
            return (Criteria) this;
        }

        public Criteria andAmountTypeIsNotNull() {
            addCriterion("amountType is not null");
            return (Criteria) this;
        }

        public Criteria andAmountTypeEqualTo(String value) {
            addCriterion("amountType =", value, "amountType");
            return (Criteria) this;
        }

        public Criteria andAmountTypeNotEqualTo(String value) {
            addCriterion("amountType <>", value, "amountType");
            return (Criteria) this;
        }

        public Criteria andAmountTypeGreaterThan(String value) {
            addCriterion("amountType >", value, "amountType");
            return (Criteria) this;
        }

        public Criteria andAmountTypeGreaterThanOrEqualTo(String value) {
            addCriterion("amountType >=", value, "amountType");
            return (Criteria) this;
        }

        public Criteria andAmountTypeLessThan(String value) {
            addCriterion("amountType <", value, "amountType");
            return (Criteria) this;
        }

        public Criteria andAmountTypeLessThanOrEqualTo(String value) {
            addCriterion("amountType <=", value, "amountType");
            return (Criteria) this;
        }

        public Criteria andAmountTypeLike(String value) {
            addCriterion("amountType like", value, "amountType");
            return (Criteria) this;
        }

        public Criteria andAmountTypeNotLike(String value) {
            addCriterion("amountType not like", value, "amountType");
            return (Criteria) this;
        }

        public Criteria andAmountTypeIn(List<String> values) {
            addCriterion("amountType in", values, "amountType");
            return (Criteria) this;
        }

        public Criteria andAmountTypeNotIn(List<String> values) {
            addCriterion("amountType not in", values, "amountType");
            return (Criteria) this;
        }

        public Criteria andAmountTypeBetween(String value1, String value2) {
            addCriterion("amountType between", value1, value2, "amountType");
            return (Criteria) this;
        }

        public Criteria andAmountTypeNotBetween(String value1, String value2) {
            addCriterion("amountType not between", value1, value2, "amountType");
            return (Criteria) this;
        }

        public Criteria andAmountNowIsNull() {
            addCriterion("amountNow is null");
            return (Criteria) this;
        }

        public Criteria andAmountNowIsNotNull() {
            addCriterion("amountNow is not null");
            return (Criteria) this;
        }

        public Criteria andAmountNowEqualTo(BigDecimal value) {
            addCriterion("amountNow =", value, "amountNow");
            return (Criteria) this;
        }

        public Criteria andAmountNowNotEqualTo(BigDecimal value) {
            addCriterion("amountNow <>", value, "amountNow");
            return (Criteria) this;
        }

        public Criteria andAmountNowGreaterThan(BigDecimal value) {
            addCriterion("amountNow >", value, "amountNow");
            return (Criteria) this;
        }

        public Criteria andAmountNowGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("amountNow >=", value, "amountNow");
            return (Criteria) this;
        }

        public Criteria andAmountNowLessThan(BigDecimal value) {
            addCriterion("amountNow <", value, "amountNow");
            return (Criteria) this;
        }

        public Criteria andAmountNowLessThanOrEqualTo(BigDecimal value) {
            addCriterion("amountNow <=", value, "amountNow");
            return (Criteria) this;
        }

        public Criteria andAmountNowIn(List<BigDecimal> values) {
            addCriterion("amountNow in", values, "amountNow");
            return (Criteria) this;
        }

        public Criteria andAmountNowNotIn(List<BigDecimal> values) {
            addCriterion("amountNow not in", values, "amountNow");
            return (Criteria) this;
        }

        public Criteria andAmountNowBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("amountNow between", value1, value2, "amountNow");
            return (Criteria) this;
        }

        public Criteria andAmountNowNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("amountNow not between", value1, value2, "amountNow");
            return (Criteria) this;
        }

        public Criteria andRetain4IsNull() {
            addCriterion("retain4 is null");
            return (Criteria) this;
        }

        public Criteria andRetain4IsNotNull() {
            addCriterion("retain4 is not null");
            return (Criteria) this;
        }

        public Criteria andRetain4EqualTo(String value) {
            addCriterion("retain4 =", value, "retain4");
            return (Criteria) this;
        }

        public Criteria andRetain4NotEqualTo(String value) {
            addCriterion("retain4 <>", value, "retain4");
            return (Criteria) this;
        }

        public Criteria andRetain4GreaterThan(String value) {
            addCriterion("retain4 >", value, "retain4");
            return (Criteria) this;
        }

        public Criteria andRetain4GreaterThanOrEqualTo(String value) {
            addCriterion("retain4 >=", value, "retain4");
            return (Criteria) this;
        }

        public Criteria andRetain4LessThan(String value) {
            addCriterion("retain4 <", value, "retain4");
            return (Criteria) this;
        }

        public Criteria andRetain4LessThanOrEqualTo(String value) {
            addCriterion("retain4 <=", value, "retain4");
            return (Criteria) this;
        }

        public Criteria andRetain4Like(String value) {
            addCriterion("retain4 like", value, "retain4");
            return (Criteria) this;
        }

        public Criteria andRetain4NotLike(String value) {
            addCriterion("retain4 not like", value, "retain4");
            return (Criteria) this;
        }

        public Criteria andRetain4In(List<String> values) {
            addCriterion("retain4 in", values, "retain4");
            return (Criteria) this;
        }

        public Criteria andRetain4NotIn(List<String> values) {
            addCriterion("retain4 not in", values, "retain4");
            return (Criteria) this;
        }

        public Criteria andRetain4Between(String value1, String value2) {
            addCriterion("retain4 between", value1, value2, "retain4");
            return (Criteria) this;
        }

        public Criteria andRetain4NotBetween(String value1, String value2) {
            addCriterion("retain4 not between", value1, value2, "retain4");
            return (Criteria) this;
        }

        public Criteria andRetain5IsNull() {
            addCriterion("retain5 is null");
            return (Criteria) this;
        }

        public Criteria andRetain5IsNotNull() {
            addCriterion("retain5 is not null");
            return (Criteria) this;
        }

        public Criteria andRetain5EqualTo(String value) {
            addCriterion("retain5 =", value, "retain5");
            return (Criteria) this;
        }

        public Criteria andRetain5NotEqualTo(String value) {
            addCriterion("retain5 <>", value, "retain5");
            return (Criteria) this;
        }

        public Criteria andRetain5GreaterThan(String value) {
            addCriterion("retain5 >", value, "retain5");
            return (Criteria) this;
        }

        public Criteria andRetain5GreaterThanOrEqualTo(String value) {
            addCriterion("retain5 >=", value, "retain5");
            return (Criteria) this;
        }

        public Criteria andRetain5LessThan(String value) {
            addCriterion("retain5 <", value, "retain5");
            return (Criteria) this;
        }

        public Criteria andRetain5LessThanOrEqualTo(String value) {
            addCriterion("retain5 <=", value, "retain5");
            return (Criteria) this;
        }

        public Criteria andRetain5Like(String value) {
            addCriterion("retain5 like", value, "retain5");
            return (Criteria) this;
        }

        public Criteria andRetain5NotLike(String value) {
            addCriterion("retain5 not like", value, "retain5");
            return (Criteria) this;
        }

        public Criteria andRetain5In(List<String> values) {
            addCriterion("retain5 in", values, "retain5");
            return (Criteria) this;
        }

        public Criteria andRetain5NotIn(List<String> values) {
            addCriterion("retain5 not in", values, "retain5");
            return (Criteria) this;
        }

        public Criteria andRetain5Between(String value1, String value2) {
            addCriterion("retain5 between", value1, value2, "retain5");
            return (Criteria) this;
        }

        public Criteria andRetain5NotBetween(String value1, String value2) {
            addCriterion("retain5 not between", value1, value2, "retain5");
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