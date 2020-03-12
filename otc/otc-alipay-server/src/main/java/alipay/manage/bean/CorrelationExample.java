package alipay.manage.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CorrelationExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public CorrelationExample() {
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

        public Criteria andParentIdIsNull() {
            addCriterion("parentId is null");
            return (Criteria) this;
        }

        public Criteria andParentIdIsNotNull() {
            addCriterion("parentId is not null");
            return (Criteria) this;
        }

        public Criteria andParentIdEqualTo(Integer value) {
            addCriterion("parentId =", value, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdNotEqualTo(Integer value) {
            addCriterion("parentId <>", value, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdGreaterThan(Integer value) {
            addCriterion("parentId >", value, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("parentId >=", value, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdLessThan(Integer value) {
            addCriterion("parentId <", value, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdLessThanOrEqualTo(Integer value) {
            addCriterion("parentId <=", value, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdIn(List<Integer> values) {
            addCriterion("parentId in", values, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdNotIn(List<Integer> values) {
            addCriterion("parentId not in", values, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdBetween(Integer value1, Integer value2) {
            addCriterion("parentId between", value1, value2, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdNotBetween(Integer value1, Integer value2) {
            addCriterion("parentId not between", value1, value2, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentNameIsNull() {
            addCriterion("parentName is null");
            return (Criteria) this;
        }

        public Criteria andParentNameIsNotNull() {
            addCriterion("parentName is not null");
            return (Criteria) this;
        }

        public Criteria andParentNameEqualTo(String value) {
            addCriterion("parentName =", value, "parentName");
            return (Criteria) this;
        }

        public Criteria andParentNameNotEqualTo(String value) {
            addCriterion("parentName <>", value, "parentName");
            return (Criteria) this;
        }

        public Criteria andParentNameGreaterThan(String value) {
            addCriterion("parentName >", value, "parentName");
            return (Criteria) this;
        }

        public Criteria andParentNameGreaterThanOrEqualTo(String value) {
            addCriterion("parentName >=", value, "parentName");
            return (Criteria) this;
        }

        public Criteria andParentNameLessThan(String value) {
            addCriterion("parentName <", value, "parentName");
            return (Criteria) this;
        }

        public Criteria andParentNameLessThanOrEqualTo(String value) {
            addCriterion("parentName <=", value, "parentName");
            return (Criteria) this;
        }

        public Criteria andParentNameLike(String value) {
            addCriterion("parentName like", value, "parentName");
            return (Criteria) this;
        }

        public Criteria andParentNameNotLike(String value) {
            addCriterion("parentName not like", value, "parentName");
            return (Criteria) this;
        }

        public Criteria andParentNameIn(List<String> values) {
            addCriterion("parentName in", values, "parentName");
            return (Criteria) this;
        }

        public Criteria andParentNameNotIn(List<String> values) {
            addCriterion("parentName not in", values, "parentName");
            return (Criteria) this;
        }

        public Criteria andParentNameBetween(String value1, String value2) {
            addCriterion("parentName between", value1, value2, "parentName");
            return (Criteria) this;
        }

        public Criteria andParentNameNotBetween(String value1, String value2) {
            addCriterion("parentName not between", value1, value2, "parentName");
            return (Criteria) this;
        }

        public Criteria andChildrenIdIsNull() {
            addCriterion("childrenId is null");
            return (Criteria) this;
        }

        public Criteria andChildrenIdIsNotNull() {
            addCriterion("childrenId is not null");
            return (Criteria) this;
        }

        public Criteria andChildrenIdEqualTo(String value) {
            addCriterion("childrenId =", value, "childrenId");
            return (Criteria) this;
        }

        public Criteria andChildrenIdNotEqualTo(String value) {
            addCriterion("childrenId <>", value, "childrenId");
            return (Criteria) this;
        }

        public Criteria andChildrenIdGreaterThan(String value) {
            addCriterion("childrenId >", value, "childrenId");
            return (Criteria) this;
        }

        public Criteria andChildrenIdGreaterThanOrEqualTo(String value) {
            addCriterion("childrenId >=", value, "childrenId");
            return (Criteria) this;
        }

        public Criteria andChildrenIdLessThan(String value) {
            addCriterion("childrenId <", value, "childrenId");
            return (Criteria) this;
        }

        public Criteria andChildrenIdLessThanOrEqualTo(String value) {
            addCriterion("childrenId <=", value, "childrenId");
            return (Criteria) this;
        }

        public Criteria andChildrenIdIn(List<String> values) {
            addCriterion("childrenId in", values, "childrenId");
            return (Criteria) this;
        }

        public Criteria andChildrenIdNotIn(List<String> values) {
            addCriterion("childrenId not in", values, "childrenId");
            return (Criteria) this;
        }

        public Criteria andChildrenIdBetween(String value1, String value2) {
            addCriterion("childrenId between", value1, value2, "childrenId");
            return (Criteria) this;
        }

        public Criteria andChildrenIdNotBetween(String value1, String value2) {
            addCriterion("childrenId not between", value1, value2, "childrenId");
            return (Criteria) this;
        }

        public Criteria andChildrenNameIsNull() {
            addCriterion("childrenName is null");
            return (Criteria) this;
        }

        public Criteria andChildrenNameIsNotNull() {
            addCriterion("childrenName is not null");
            return (Criteria) this;
        }

        public Criteria andChildrenNameEqualTo(String value) {
            addCriterion("childrenName =", value, "childrenName");
            return (Criteria) this;
        }

        public Criteria andChildrenNameNotEqualTo(String value) {
            addCriterion("childrenName <>", value, "childrenName");
            return (Criteria) this;
        }

        public Criteria andChildrenNameGreaterThan(String value) {
            addCriterion("childrenName >", value, "childrenName");
            return (Criteria) this;
        }

        public Criteria andChildrenNameGreaterThanOrEqualTo(String value) {
            addCriterion("childrenName >=", value, "childrenName");
            return (Criteria) this;
        }

        public Criteria andChildrenNameLessThan(String value) {
            addCriterion("childrenName <", value, "childrenName");
            return (Criteria) this;
        }

        public Criteria andChildrenNameLessThanOrEqualTo(String value) {
            addCriterion("childrenName <=", value, "childrenName");
            return (Criteria) this;
        }

        public Criteria andChildrenNameLike(String value) {
            addCriterion("childrenName like", value, "childrenName");
            return (Criteria) this;
        }

        public Criteria andChildrenNameNotLike(String value) {
            addCriterion("childrenName not like", value, "childrenName");
            return (Criteria) this;
        }

        public Criteria andChildrenNameIn(List<String> values) {
            addCriterion("childrenName in", values, "childrenName");
            return (Criteria) this;
        }

        public Criteria andChildrenNameNotIn(List<String> values) {
            addCriterion("childrenName not in", values, "childrenName");
            return (Criteria) this;
        }

        public Criteria andChildrenNameBetween(String value1, String value2) {
            addCriterion("childrenName between", value1, value2, "childrenName");
            return (Criteria) this;
        }

        public Criteria andChildrenNameNotBetween(String value1, String value2) {
            addCriterion("childrenName not between", value1, value2, "childrenName");
            return (Criteria) this;
        }

        public Criteria andDistanceIsNull() {
            addCriterion("distance is null");
            return (Criteria) this;
        }

        public Criteria andDistanceIsNotNull() {
            addCriterion("distance is not null");
            return (Criteria) this;
        }

        public Criteria andDistanceEqualTo(Integer value) {
            addCriterion("distance =", value, "distance");
            return (Criteria) this;
        }

        public Criteria andDistanceNotEqualTo(Integer value) {
            addCriterion("distance <>", value, "distance");
            return (Criteria) this;
        }

        public Criteria andDistanceGreaterThan(Integer value) {
            addCriterion("distance >", value, "distance");
            return (Criteria) this;
        }

        public Criteria andDistanceGreaterThanOrEqualTo(Integer value) {
            addCriterion("distance >=", value, "distance");
            return (Criteria) this;
        }

        public Criteria andDistanceLessThan(Integer value) {
            addCriterion("distance <", value, "distance");
            return (Criteria) this;
        }

        public Criteria andDistanceLessThanOrEqualTo(Integer value) {
            addCriterion("distance <=", value, "distance");
            return (Criteria) this;
        }

        public Criteria andDistanceIn(List<Integer> values) {
            addCriterion("distance in", values, "distance");
            return (Criteria) this;
        }

        public Criteria andDistanceNotIn(List<Integer> values) {
            addCriterion("distance not in", values, "distance");
            return (Criteria) this;
        }

        public Criteria andDistanceBetween(Integer value1, Integer value2) {
            addCriterion("distance between", value1, value2, "distance");
            return (Criteria) this;
        }

        public Criteria andDistanceNotBetween(Integer value1, Integer value2) {
            addCriterion("distance not between", value1, value2, "distance");
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

        public Criteria andParentTypeIsNull() {
            addCriterion("parentType is null");
            return (Criteria) this;
        }

        public Criteria andParentTypeIsNotNull() {
            addCriterion("parentType is not null");
            return (Criteria) this;
        }

        public Criteria andParentTypeEqualTo(Integer value) {
            addCriterion("parentType =", value, "parentType");
            return (Criteria) this;
        }

        public Criteria andParentTypeNotEqualTo(Integer value) {
            addCriterion("parentType <>", value, "parentType");
            return (Criteria) this;
        }

        public Criteria andParentTypeGreaterThan(Integer value) {
            addCriterion("parentType >", value, "parentType");
            return (Criteria) this;
        }

        public Criteria andParentTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("parentType >=", value, "parentType");
            return (Criteria) this;
        }

        public Criteria andParentTypeLessThan(Integer value) {
            addCriterion("parentType <", value, "parentType");
            return (Criteria) this;
        }

        public Criteria andParentTypeLessThanOrEqualTo(Integer value) {
            addCriterion("parentType <=", value, "parentType");
            return (Criteria) this;
        }

        public Criteria andParentTypeIn(List<Integer> values) {
            addCriterion("parentType in", values, "parentType");
            return (Criteria) this;
        }

        public Criteria andParentTypeNotIn(List<Integer> values) {
            addCriterion("parentType not in", values, "parentType");
            return (Criteria) this;
        }

        public Criteria andParentTypeBetween(Integer value1, Integer value2) {
            addCriterion("parentType between", value1, value2, "parentType");
            return (Criteria) this;
        }

        public Criteria andParentTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("parentType not between", value1, value2, "parentType");
            return (Criteria) this;
        }

        public Criteria andChildrenTypeIsNull() {
            addCriterion("childrenType is null");
            return (Criteria) this;
        }

        public Criteria andChildrenTypeIsNotNull() {
            addCriterion("childrenType is not null");
            return (Criteria) this;
        }

        public Criteria andChildrenTypeEqualTo(Integer value) {
            addCriterion("childrenType =", value, "childrenType");
            return (Criteria) this;
        }

        public Criteria andChildrenTypeNotEqualTo(Integer value) {
            addCriterion("childrenType <>", value, "childrenType");
            return (Criteria) this;
        }

        public Criteria andChildrenTypeGreaterThan(Integer value) {
            addCriterion("childrenType >", value, "childrenType");
            return (Criteria) this;
        }

        public Criteria andChildrenTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("childrenType >=", value, "childrenType");
            return (Criteria) this;
        }

        public Criteria andChildrenTypeLessThan(Integer value) {
            addCriterion("childrenType <", value, "childrenType");
            return (Criteria) this;
        }

        public Criteria andChildrenTypeLessThanOrEqualTo(Integer value) {
            addCriterion("childrenType <=", value, "childrenType");
            return (Criteria) this;
        }

        public Criteria andChildrenTypeIn(List<Integer> values) {
            addCriterion("childrenType in", values, "childrenType");
            return (Criteria) this;
        }

        public Criteria andChildrenTypeNotIn(List<Integer> values) {
            addCriterion("childrenType not in", values, "childrenType");
            return (Criteria) this;
        }

        public Criteria andChildrenTypeBetween(Integer value1, Integer value2) {
            addCriterion("childrenType between", value1, value2, "childrenType");
            return (Criteria) this;
        }

        public Criteria andChildrenTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("childrenType not between", value1, value2, "childrenType");
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