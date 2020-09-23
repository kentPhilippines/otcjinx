package alipay.manage.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExceptionOrderExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ExceptionOrderExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
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

        public Criteria andOrderExceptIdIsNull() {
            addCriterion("orderExceptId is null");
            return (Criteria) this;
        }

        public Criteria andOrderExceptIdIsNotNull() {
            addCriterion("orderExceptId is not null");
            return (Criteria) this;
        }

        public Criteria andOrderExceptIdEqualTo(String value) {
            addCriterion("orderExceptId =", value, "orderExceptId");
            return (Criteria) this;
        }

        public Criteria andOrderExceptIdNotEqualTo(String value) {
            addCriterion("orderExceptId <>", value, "orderExceptId");
            return (Criteria) this;
        }

        public Criteria andOrderExceptIdGreaterThan(String value) {
            addCriterion("orderExceptId >", value, "orderExceptId");
            return (Criteria) this;
        }

        public Criteria andOrderExceptIdGreaterThanOrEqualTo(String value) {
            addCriterion("orderExceptId >=", value, "orderExceptId");
            return (Criteria) this;
        }

        public Criteria andOrderExceptIdLessThan(String value) {
            addCriterion("orderExceptId <", value, "orderExceptId");
            return (Criteria) this;
        }

        public Criteria andOrderExceptIdLessThanOrEqualTo(String value) {
            addCriterion("orderExceptId <=", value, "orderExceptId");
            return (Criteria) this;
        }

        public Criteria andOrderExceptIdLike(String value) {
            addCriterion("orderExceptId like", value, "orderExceptId");
            return (Criteria) this;
        }

        public Criteria andOrderExceptIdNotLike(String value) {
            addCriterion("orderExceptId not like", value, "orderExceptId");
            return (Criteria) this;
        }

        public Criteria andOrderExceptIdIn(List<String> values) {
            addCriterion("orderExceptId in", values, "orderExceptId");
            return (Criteria) this;
        }

        public Criteria andOrderExceptIdNotIn(List<String> values) {
            addCriterion("orderExceptId not in", values, "orderExceptId");
            return (Criteria) this;
        }

        public Criteria andOrderExceptIdBetween(String value1, String value2) {
            addCriterion("orderExceptId between", value1, value2, "orderExceptId");
            return (Criteria) this;
        }

        public Criteria andOrderExceptIdNotBetween(String value1, String value2) {
            addCriterion("orderExceptId not between", value1, value2, "orderExceptId");
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

        public Criteria andExceptStatusIsNull() {
            addCriterion("exceptStatus is null");
            return (Criteria) this;
        }

        public Criteria andExceptStatusIsNotNull() {
            addCriterion("exceptStatus is not null");
            return (Criteria) this;
        }

        public Criteria andExceptStatusEqualTo(Integer value) {
            addCriterion("exceptStatus =", value, "exceptStatus");
            return (Criteria) this;
        }

        public Criteria andExceptStatusNotEqualTo(Integer value) {
            addCriterion("exceptStatus <>", value, "exceptStatus");
            return (Criteria) this;
        }

        public Criteria andExceptStatusGreaterThan(Integer value) {
            addCriterion("exceptStatus >", value, "exceptStatus");
            return (Criteria) this;
        }

        public Criteria andExceptStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("exceptStatus >=", value, "exceptStatus");
            return (Criteria) this;
        }

        public Criteria andExceptStatusLessThan(Integer value) {
            addCriterion("exceptStatus <", value, "exceptStatus");
            return (Criteria) this;
        }

        public Criteria andExceptStatusLessThanOrEqualTo(Integer value) {
            addCriterion("exceptStatus <=", value, "exceptStatus");
            return (Criteria) this;
        }

        public Criteria andExceptStatusIn(List<Integer> values) {
            addCriterion("exceptStatus in", values, "exceptStatus");
            return (Criteria) this;
        }

        public Criteria andExceptStatusNotIn(List<Integer> values) {
            addCriterion("exceptStatus not in", values, "exceptStatus");
            return (Criteria) this;
        }

        public Criteria andExceptStatusBetween(Integer value1, Integer value2) {
            addCriterion("exceptStatus between", value1, value2, "exceptStatus");
            return (Criteria) this;
        }

        public Criteria andExceptStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("exceptStatus not between", value1, value2, "exceptStatus");
            return (Criteria) this;
        }

        public Criteria andExceptTypeIsNull() {
            addCriterion("exceptType is null");
            return (Criteria) this;
        }

        public Criteria andExceptTypeIsNotNull() {
            addCriterion("exceptType is not null");
            return (Criteria) this;
        }

        public Criteria andExceptTypeEqualTo(Integer value) {
            addCriterion("exceptType =", value, "exceptType");
            return (Criteria) this;
        }

        public Criteria andExceptTypeNotEqualTo(Integer value) {
            addCriterion("exceptType <>", value, "exceptType");
            return (Criteria) this;
        }

        public Criteria andExceptTypeGreaterThan(Integer value) {
            addCriterion("exceptType >", value, "exceptType");
            return (Criteria) this;
        }

        public Criteria andExceptTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("exceptType >=", value, "exceptType");
            return (Criteria) this;
        }

        public Criteria andExceptTypeLessThan(Integer value) {
            addCriterion("exceptType <", value, "exceptType");
            return (Criteria) this;
        }

        public Criteria andExceptTypeLessThanOrEqualTo(Integer value) {
            addCriterion("exceptType <=", value, "exceptType");
            return (Criteria) this;
        }

        public Criteria andExceptTypeIn(List<Integer> values) {
            addCriterion("exceptType in", values, "exceptType");
            return (Criteria) this;
        }

        public Criteria andExceptTypeNotIn(List<Integer> values) {
            addCriterion("exceptType not in", values, "exceptType");
            return (Criteria) this;
        }

        public Criteria andExceptTypeBetween(Integer value1, Integer value2) {
            addCriterion("exceptType between", value1, value2, "exceptType");
            return (Criteria) this;
        }

        public Criteria andExceptTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("exceptType not between", value1, value2, "exceptType");
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

        public Criteria andExceptOrderAmountIsNull() {
            addCriterion("exceptOrderAmount is null");
            return (Criteria) this;
        }

        public Criteria andExceptOrderAmountIsNotNull() {
            addCriterion("exceptOrderAmount is not null");
            return (Criteria) this;
        }

        public Criteria andExceptOrderAmountEqualTo(String value) {
            addCriterion("exceptOrderAmount =", value, "exceptOrderAmount");
            return (Criteria) this;
        }

        public Criteria andExceptOrderAmountNotEqualTo(String value) {
            addCriterion("exceptOrderAmount <>", value, "exceptOrderAmount");
            return (Criteria) this;
        }

        public Criteria andExceptOrderAmountGreaterThan(String value) {
            addCriterion("exceptOrderAmount >", value, "exceptOrderAmount");
            return (Criteria) this;
        }

        public Criteria andExceptOrderAmountGreaterThanOrEqualTo(String value) {
            addCriterion("exceptOrderAmount >=", value, "exceptOrderAmount");
            return (Criteria) this;
        }

        public Criteria andExceptOrderAmountLessThan(String value) {
            addCriterion("exceptOrderAmount <", value, "exceptOrderAmount");
            return (Criteria) this;
        }

        public Criteria andExceptOrderAmountLessThanOrEqualTo(String value) {
            addCriterion("exceptOrderAmount <=", value, "exceptOrderAmount");
            return (Criteria) this;
        }

        public Criteria andExceptOrderAmountLike(String value) {
            addCriterion("exceptOrderAmount like", value, "exceptOrderAmount");
            return (Criteria) this;
        }

        public Criteria andExceptOrderAmountNotLike(String value) {
            addCriterion("exceptOrderAmount not like", value, "exceptOrderAmount");
            return (Criteria) this;
        }

        public Criteria andExceptOrderAmountIn(List<String> values) {
            addCriterion("exceptOrderAmount in", values, "exceptOrderAmount");
            return (Criteria) this;
        }

        public Criteria andExceptOrderAmountNotIn(List<String> values) {
            addCriterion("exceptOrderAmount not in", values, "exceptOrderAmount");
            return (Criteria) this;
        }

        public Criteria andExceptOrderAmountBetween(String value1, String value2) {
            addCriterion("exceptOrderAmount between", value1, value2, "exceptOrderAmount");
            return (Criteria) this;
        }

        public Criteria andExceptOrderAmountNotBetween(String value1, String value2) {
            addCriterion("exceptOrderAmount not between", value1, value2, "exceptOrderAmount");
            return (Criteria) this;
        }

        public Criteria andOrderGenerationIpIsNull() {
            addCriterion("orderGenerationIp is null");
            return (Criteria) this;
        }

        public Criteria andOrderGenerationIpIsNotNull() {
            addCriterion("orderGenerationIp is not null");
            return (Criteria) this;
        }

        public Criteria andOrderGenerationIpEqualTo(String value) {
            addCriterion("orderGenerationIp =", value, "orderGenerationIp");
            return (Criteria) this;
        }

        public Criteria andOrderGenerationIpNotEqualTo(String value) {
            addCriterion("orderGenerationIp <>", value, "orderGenerationIp");
            return (Criteria) this;
        }

        public Criteria andOrderGenerationIpGreaterThan(String value) {
            addCriterion("orderGenerationIp >", value, "orderGenerationIp");
            return (Criteria) this;
        }

        public Criteria andOrderGenerationIpGreaterThanOrEqualTo(String value) {
            addCriterion("orderGenerationIp >=", value, "orderGenerationIp");
            return (Criteria) this;
        }

        public Criteria andOrderGenerationIpLessThan(String value) {
            addCriterion("orderGenerationIp <", value, "orderGenerationIp");
            return (Criteria) this;
        }

        public Criteria andOrderGenerationIpLessThanOrEqualTo(String value) {
            addCriterion("orderGenerationIp <=", value, "orderGenerationIp");
            return (Criteria) this;
        }

        public Criteria andOrderGenerationIpLike(String value) {
            addCriterion("orderGenerationIp like", value, "orderGenerationIp");
            return (Criteria) this;
        }

        public Criteria andOrderGenerationIpNotLike(String value) {
            addCriterion("orderGenerationIp not like", value, "orderGenerationIp");
            return (Criteria) this;
        }

        public Criteria andOrderGenerationIpIn(List<String> values) {
            addCriterion("orderGenerationIp in", values, "orderGenerationIp");
            return (Criteria) this;
        }

        public Criteria andOrderGenerationIpNotIn(List<String> values) {
            addCriterion("orderGenerationIp not in", values, "orderGenerationIp");
            return (Criteria) this;
        }

        public Criteria andOrderGenerationIpBetween(String value1, String value2) {
            addCriterion("orderGenerationIp between", value1, value2, "orderGenerationIp");
            return (Criteria) this;
        }

        public Criteria andOrderGenerationIpNotBetween(String value1, String value2) {
            addCriterion("orderGenerationIp not between", value1, value2, "orderGenerationIp");
            return (Criteria) this;
        }

        public Criteria andOperationIsNull() {
            addCriterion("operation is null");
            return (Criteria) this;
        }

        public Criteria andOperationIsNotNull() {
            addCriterion("operation is not null");
            return (Criteria) this;
        }

        public Criteria andOperationEqualTo(String value) {
            addCriterion("operation =", value, "operation");
            return (Criteria) this;
        }

        public Criteria andOperationNotEqualTo(String value) {
            addCriterion("operation <>", value, "operation");
            return (Criteria) this;
        }

        public Criteria andOperationGreaterThan(String value) {
            addCriterion("operation >", value, "operation");
            return (Criteria) this;
        }

        public Criteria andOperationGreaterThanOrEqualTo(String value) {
            addCriterion("operation >=", value, "operation");
            return (Criteria) this;
        }

        public Criteria andOperationLessThan(String value) {
            addCriterion("operation <", value, "operation");
            return (Criteria) this;
        }

        public Criteria andOperationLessThanOrEqualTo(String value) {
            addCriterion("operation <=", value, "operation");
            return (Criteria) this;
        }

        public Criteria andOperationLike(String value) {
            addCriterion("operation like", value, "operation");
            return (Criteria) this;
        }

        public Criteria andOperationNotLike(String value) {
            addCriterion("operation not like", value, "operation");
            return (Criteria) this;
        }

        public Criteria andOperationIn(List<String> values) {
            addCriterion("operation in", values, "operation");
            return (Criteria) this;
        }

        public Criteria andOperationNotIn(List<String> values) {
            addCriterion("operation not in", values, "operation");
            return (Criteria) this;
        }

        public Criteria andOperationBetween(String value1, String value2) {
            addCriterion("operation between", value1, value2, "operation");
            return (Criteria) this;
        }

        public Criteria andOperationNotBetween(String value1, String value2) {
            addCriterion("operation not between", value1, value2, "operation");
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

        public Criteria andSubmitSystemIsNull() {
            addCriterion("submitSystem is null");
            return (Criteria) this;
        }

        public Criteria andSubmitSystemIsNotNull() {
            addCriterion("submitSystem is not null");
            return (Criteria) this;
        }

        public Criteria andSubmitSystemEqualTo(String value) {
            addCriterion("submitSystem =", value, "submitSystem");
            return (Criteria) this;
        }

        public Criteria andSubmitSystemNotEqualTo(String value) {
            addCriterion("submitSystem <>", value, "submitSystem");
            return (Criteria) this;
        }

        public Criteria andSubmitSystemGreaterThan(String value) {
            addCriterion("submitSystem >", value, "submitSystem");
            return (Criteria) this;
        }

        public Criteria andSubmitSystemGreaterThanOrEqualTo(String value) {
            addCriterion("submitSystem >=", value, "submitSystem");
            return (Criteria) this;
        }

        public Criteria andSubmitSystemLessThan(String value) {
            addCriterion("submitSystem <", value, "submitSystem");
            return (Criteria) this;
        }

        public Criteria andSubmitSystemLessThanOrEqualTo(String value) {
            addCriterion("submitSystem <=", value, "submitSystem");
            return (Criteria) this;
        }

        public Criteria andSubmitSystemLike(String value) {
            addCriterion("submitSystem like", value, "submitSystem");
            return (Criteria) this;
        }

        public Criteria andSubmitSystemNotLike(String value) {
            addCriterion("submitSystem not like", value, "submitSystem");
            return (Criteria) this;
        }

        public Criteria andSubmitSystemIn(List<String> values) {
            addCriterion("submitSystem in", values, "submitSystem");
            return (Criteria) this;
        }

        public Criteria andSubmitSystemNotIn(List<String> values) {
            addCriterion("submitSystem not in", values, "submitSystem");
            return (Criteria) this;
        }

        public Criteria andSubmitSystemBetween(String value1, String value2) {
            addCriterion("submitSystem between", value1, value2, "submitSystem");
            return (Criteria) this;
        }

        public Criteria andSubmitSystemNotBetween(String value1, String value2) {
            addCriterion("submitSystem not between", value1, value2, "submitSystem");
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

        public Criteria andRetain1IsNull() {
            addCriterion("retain1 is null");
            return (Criteria) this;
        }

        public Criteria andRetain1IsNotNull() {
            addCriterion("retain1 is not null");
            return (Criteria) this;
        }

        public Criteria andRetain1EqualTo(String value) {
            addCriterion("retain1 =", value, "retain1");
            return (Criteria) this;
        }

        public Criteria andRetain1NotEqualTo(String value) {
            addCriterion("retain1 <>", value, "retain1");
            return (Criteria) this;
        }

        public Criteria andRetain1GreaterThan(String value) {
            addCriterion("retain1 >", value, "retain1");
            return (Criteria) this;
        }

        public Criteria andRetain1GreaterThanOrEqualTo(String value) {
            addCriterion("retain1 >=", value, "retain1");
            return (Criteria) this;
        }

        public Criteria andRetain1LessThan(String value) {
            addCriterion("retain1 <", value, "retain1");
            return (Criteria) this;
        }

        public Criteria andRetain1LessThanOrEqualTo(String value) {
            addCriterion("retain1 <=", value, "retain1");
            return (Criteria) this;
        }

        public Criteria andRetain1Like(String value) {
            addCriterion("retain1 like", value, "retain1");
            return (Criteria) this;
        }

        public Criteria andRetain1NotLike(String value) {
            addCriterion("retain1 not like", value, "retain1");
            return (Criteria) this;
        }

        public Criteria andRetain1In(List<String> values) {
            addCriterion("retain1 in", values, "retain1");
            return (Criteria) this;
        }

        public Criteria andRetain1NotIn(List<String> values) {
            addCriterion("retain1 not in", values, "retain1");
            return (Criteria) this;
        }

        public Criteria andRetain1Between(String value1, String value2) {
            addCriterion("retain1 between", value1, value2, "retain1");
            return (Criteria) this;
        }

        public Criteria andRetain1NotBetween(String value1, String value2) {
            addCriterion("retain1 not between", value1, value2, "retain1");
            return (Criteria) this;
        }

        public Criteria andRetain2IsNull() {
            addCriterion("retain2 is null");
            return (Criteria) this;
        }

        public Criteria andRetain2IsNotNull() {
            addCriterion("retain2 is not null");
            return (Criteria) this;
        }

        public Criteria andRetain2EqualTo(String value) {
            addCriterion("retain2 =", value, "retain2");
            return (Criteria) this;
        }

        public Criteria andRetain2NotEqualTo(String value) {
            addCriterion("retain2 <>", value, "retain2");
            return (Criteria) this;
        }

        public Criteria andRetain2GreaterThan(String value) {
            addCriterion("retain2 >", value, "retain2");
            return (Criteria) this;
        }

        public Criteria andRetain2GreaterThanOrEqualTo(String value) {
            addCriterion("retain2 >=", value, "retain2");
            return (Criteria) this;
        }

        public Criteria andRetain2LessThan(String value) {
            addCriterion("retain2 <", value, "retain2");
            return (Criteria) this;
        }

        public Criteria andRetain2LessThanOrEqualTo(String value) {
            addCriterion("retain2 <=", value, "retain2");
            return (Criteria) this;
        }

        public Criteria andRetain2Like(String value) {
            addCriterion("retain2 like", value, "retain2");
            return (Criteria) this;
        }

        public Criteria andRetain2NotLike(String value) {
            addCriterion("retain2 not like", value, "retain2");
            return (Criteria) this;
        }

        public Criteria andRetain2In(List<String> values) {
            addCriterion("retain2 in", values, "retain2");
            return (Criteria) this;
        }

        public Criteria andRetain2NotIn(List<String> values) {
            addCriterion("retain2 not in", values, "retain2");
            return (Criteria) this;
        }

        public Criteria andRetain2Between(String value1, String value2) {
            addCriterion("retain2 between", value1, value2, "retain2");
            return (Criteria) this;
        }

        public Criteria andRetain2NotBetween(String value1, String value2) {
            addCriterion("retain2 not between", value1, value2, "retain2");
            return (Criteria) this;
        }

        public Criteria andRetain3IsNull() {
            addCriterion("retain3 is null");
            return (Criteria) this;
        }

        public Criteria andRetain3IsNotNull() {
            addCriterion("retain3 is not null");
            return (Criteria) this;
        }

        public Criteria andRetain3EqualTo(String value) {
            addCriterion("retain3 =", value, "retain3");
            return (Criteria) this;
        }

        public Criteria andRetain3NotEqualTo(String value) {
            addCriterion("retain3 <>", value, "retain3");
            return (Criteria) this;
        }

        public Criteria andRetain3GreaterThan(String value) {
            addCriterion("retain3 >", value, "retain3");
            return (Criteria) this;
        }

        public Criteria andRetain3GreaterThanOrEqualTo(String value) {
            addCriterion("retain3 >=", value, "retain3");
            return (Criteria) this;
        }

        public Criteria andRetain3LessThan(String value) {
            addCriterion("retain3 <", value, "retain3");
            return (Criteria) this;
        }

        public Criteria andRetain3LessThanOrEqualTo(String value) {
            addCriterion("retain3 <=", value, "retain3");
            return (Criteria) this;
        }

        public Criteria andRetain3Like(String value) {
            addCriterion("retain3 like", value, "retain3");
            return (Criteria) this;
        }

        public Criteria andRetain3NotLike(String value) {
            addCriterion("retain3 not like", value, "retain3");
            return (Criteria) this;
        }

        public Criteria andRetain3In(List<String> values) {
            addCriterion("retain3 in", values, "retain3");
            return (Criteria) this;
        }

        public Criteria andRetain3NotIn(List<String> values) {
            addCriterion("retain3 not in", values, "retain3");
            return (Criteria) this;
        }

        public Criteria andRetain3Between(String value1, String value2) {
            addCriterion("retain3 between", value1, value2, "retain3");
            return (Criteria) this;
        }

        public Criteria andRetain3NotBetween(String value1, String value2) {
            addCriterion("retain3 not between", value1, value2, "retain3");
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
    }
}
