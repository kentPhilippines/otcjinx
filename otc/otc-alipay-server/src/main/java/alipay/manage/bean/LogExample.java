package alipay.manage.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LogExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public LogExample() {
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

        public Criteria andIpAddrIsNull() {
            addCriterion("ipAddr is null");
            return (Criteria) this;
        }

        public Criteria andIpAddrIsNotNull() {
            addCriterion("ipAddr is not null");
            return (Criteria) this;
        }

        public Criteria andIpAddrEqualTo(String value) {
            addCriterion("ipAddr =", value, "ipAddr");
            return (Criteria) this;
        }

        public Criteria andIpAddrNotEqualTo(String value) {
            addCriterion("ipAddr <>", value, "ipAddr");
            return (Criteria) this;
        }

        public Criteria andIpAddrGreaterThan(String value) {
            addCriterion("ipAddr >", value, "ipAddr");
            return (Criteria) this;
        }

        public Criteria andIpAddrGreaterThanOrEqualTo(String value) {
            addCriterion("ipAddr >=", value, "ipAddr");
            return (Criteria) this;
        }

        public Criteria andIpAddrLessThan(String value) {
            addCriterion("ipAddr <", value, "ipAddr");
            return (Criteria) this;
        }

        public Criteria andIpAddrLessThanOrEqualTo(String value) {
            addCriterion("ipAddr <=", value, "ipAddr");
            return (Criteria) this;
        }

        public Criteria andIpAddrLike(String value) {
            addCriterion("ipAddr like", value, "ipAddr");
            return (Criteria) this;
        }

        public Criteria andIpAddrNotLike(String value) {
            addCriterion("ipAddr not like", value, "ipAddr");
            return (Criteria) this;
        }

        public Criteria andIpAddrIn(List<String> values) {
            addCriterion("ipAddr in", values, "ipAddr");
            return (Criteria) this;
        }

        public Criteria andIpAddrNotIn(List<String> values) {
            addCriterion("ipAddr not in", values, "ipAddr");
            return (Criteria) this;
        }

        public Criteria andIpAddrBetween(String value1, String value2) {
            addCriterion("ipAddr between", value1, value2, "ipAddr");
            return (Criteria) this;
        }

        public Criteria andIpAddrNotBetween(String value1, String value2) {
            addCriterion("ipAddr not between", value1, value2, "ipAddr");
            return (Criteria) this;
        }

        public Criteria andLoginLocationIsNull() {
            addCriterion("loginLocation is null");
            return (Criteria) this;
        }

        public Criteria andLoginLocationIsNotNull() {
            addCriterion("loginLocation is not null");
            return (Criteria) this;
        }

        public Criteria andLoginLocationEqualTo(String value) {
            addCriterion("loginLocation =", value, "loginLocation");
            return (Criteria) this;
        }

        public Criteria andLoginLocationNotEqualTo(String value) {
            addCriterion("loginLocation <>", value, "loginLocation");
            return (Criteria) this;
        }

        public Criteria andLoginLocationGreaterThan(String value) {
            addCriterion("loginLocation >", value, "loginLocation");
            return (Criteria) this;
        }

        public Criteria andLoginLocationGreaterThanOrEqualTo(String value) {
            addCriterion("loginLocation >=", value, "loginLocation");
            return (Criteria) this;
        }

        public Criteria andLoginLocationLessThan(String value) {
            addCriterion("loginLocation <", value, "loginLocation");
            return (Criteria) this;
        }

        public Criteria andLoginLocationLessThanOrEqualTo(String value) {
            addCriterion("loginLocation <=", value, "loginLocation");
            return (Criteria) this;
        }

        public Criteria andLoginLocationLike(String value) {
            addCriterion("loginLocation like", value, "loginLocation");
            return (Criteria) this;
        }

        public Criteria andLoginLocationNotLike(String value) {
            addCriterion("loginLocation not like", value, "loginLocation");
            return (Criteria) this;
        }

        public Criteria andLoginLocationIn(List<String> values) {
            addCriterion("loginLocation in", values, "loginLocation");
            return (Criteria) this;
        }

        public Criteria andLoginLocationNotIn(List<String> values) {
            addCriterion("loginLocation not in", values, "loginLocation");
            return (Criteria) this;
        }

        public Criteria andLoginLocationBetween(String value1, String value2) {
            addCriterion("loginLocation between", value1, value2, "loginLocation");
            return (Criteria) this;
        }

        public Criteria andLoginLocationNotBetween(String value1, String value2) {
            addCriterion("loginLocation not between", value1, value2, "loginLocation");
            return (Criteria) this;
        }

        public Criteria andLoginTimeIsNull() {
            addCriterion("loginTime is null");
            return (Criteria) this;
        }

        public Criteria andLoginTimeIsNotNull() {
            addCriterion("loginTime is not null");
            return (Criteria) this;
        }

        public Criteria andLoginTimeEqualTo(Date value) {
            addCriterion("loginTime =", value, "loginTime");
            return (Criteria) this;
        }

        public Criteria andLoginTimeNotEqualTo(Date value) {
            addCriterion("loginTime <>", value, "loginTime");
            return (Criteria) this;
        }

        public Criteria andLoginTimeGreaterThan(Date value) {
            addCriterion("loginTime >", value, "loginTime");
            return (Criteria) this;
        }

        public Criteria andLoginTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("loginTime >=", value, "loginTime");
            return (Criteria) this;
        }

        public Criteria andLoginTimeLessThan(Date value) {
            addCriterion("loginTime <", value, "loginTime");
            return (Criteria) this;
        }

        public Criteria andLoginTimeLessThanOrEqualTo(Date value) {
            addCriterion("loginTime <=", value, "loginTime");
            return (Criteria) this;
        }

        public Criteria andLoginTimeIn(List<Date> values) {
            addCriterion("loginTime in", values, "loginTime");
            return (Criteria) this;
        }

        public Criteria andLoginTimeNotIn(List<Date> values) {
            addCriterion("loginTime not in", values, "loginTime");
            return (Criteria) this;
        }

        public Criteria andLoginTimeBetween(Date value1, Date value2) {
            addCriterion("loginTime between", value1, value2, "loginTime");
            return (Criteria) this;
        }

        public Criteria andLoginTimeNotBetween(Date value1, Date value2) {
            addCriterion("loginTime not between", value1, value2, "loginTime");
            return (Criteria) this;
        }

        public Criteria andUseNameIsNull() {
            addCriterion("useName is null");
            return (Criteria) this;
        }

        public Criteria andUseNameIsNotNull() {
            addCriterion("useName is not null");
            return (Criteria) this;
        }

        public Criteria andUseNameEqualTo(String value) {
            addCriterion("useName =", value, "useName");
            return (Criteria) this;
        }

        public Criteria andUseNameNotEqualTo(String value) {
            addCriterion("useName <>", value, "useName");
            return (Criteria) this;
        }

        public Criteria andUseNameGreaterThan(String value) {
            addCriterion("useName >", value, "useName");
            return (Criteria) this;
        }

        public Criteria andUseNameGreaterThanOrEqualTo(String value) {
            addCriterion("useName >=", value, "useName");
            return (Criteria) this;
        }

        public Criteria andUseNameLessThan(String value) {
            addCriterion("useName <", value, "useName");
            return (Criteria) this;
        }

        public Criteria andUseNameLessThanOrEqualTo(String value) {
            addCriterion("useName <=", value, "useName");
            return (Criteria) this;
        }

        public Criteria andUseNameLike(String value) {
            addCriterion("useName like", value, "useName");
            return (Criteria) this;
        }

        public Criteria andUseNameNotLike(String value) {
            addCriterion("useName not like", value, "useName");
            return (Criteria) this;
        }

        public Criteria andUseNameIn(List<String> values) {
            addCriterion("useName in", values, "useName");
            return (Criteria) this;
        }

        public Criteria andUseNameNotIn(List<String> values) {
            addCriterion("useName not in", values, "useName");
            return (Criteria) this;
        }

        public Criteria andUseNameBetween(String value1, String value2) {
            addCriterion("useName between", value1, value2, "useName");
            return (Criteria) this;
        }

        public Criteria andUseNameNotBetween(String value1, String value2) {
            addCriterion("useName not between", value1, value2, "useName");
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