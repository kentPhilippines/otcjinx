package alipay.manage.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MediumExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public MediumExample() {
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

        public Criteria andMediumNumberIsNull() {
            addCriterion("mediumNumber is null");
            return (Criteria) this;
        }

        public Criteria andMediumNumberIsNotNull() {
            addCriterion("mediumNumber is not null");
            return (Criteria) this;
        }

        public Criteria andMediumNumberEqualTo(String value) {
            addCriterion("mediumNumber =", value, "mediumNumber");
            return (Criteria) this;
        }

        public Criteria andMediumNumberNotEqualTo(String value) {
            addCriterion("mediumNumber <>", value, "mediumNumber");
            return (Criteria) this;
        }

        public Criteria andMediumNumberGreaterThan(String value) {
            addCriterion("mediumNumber >", value, "mediumNumber");
            return (Criteria) this;
        }

        public Criteria andMediumNumberGreaterThanOrEqualTo(String value) {
            addCriterion("mediumNumber >=", value, "mediumNumber");
            return (Criteria) this;
        }

        public Criteria andMediumNumberLessThan(String value) {
            addCriterion("mediumNumber <", value, "mediumNumber");
            return (Criteria) this;
        }

        public Criteria andMediumNumberLessThanOrEqualTo(String value) {
            addCriterion("mediumNumber <=", value, "mediumNumber");
            return (Criteria) this;
        }

        public Criteria andMediumNumberLike(String value) {
            addCriterion("mediumNumber like", value, "mediumNumber");
            return (Criteria) this;
        }

        public Criteria andMediumNumberNotLike(String value) {
            addCriterion("mediumNumber not like", value, "mediumNumber");
            return (Criteria) this;
        }

        public Criteria andMediumNumberIn(List<String> values) {
            addCriterion("mediumNumber in", values, "mediumNumber");
            return (Criteria) this;
        }

        public Criteria andMediumNumberNotIn(List<String> values) {
            addCriterion("mediumNumber not in", values, "mediumNumber");
            return (Criteria) this;
        }

        public Criteria andMediumNumberBetween(String value1, String value2) {
            addCriterion("mediumNumber between", value1, value2, "mediumNumber");
            return (Criteria) this;
        }

        public Criteria andMediumNumberNotBetween(String value1, String value2) {
            addCriterion("mediumNumber not between", value1, value2, "mediumNumber");
            return (Criteria) this;
        }

        public Criteria andMediumIdIsNull() {
            addCriterion("mediumId is null");
            return (Criteria) this;
        }

        public Criteria andMediumIdIsNotNull() {
            addCriterion("mediumId is not null");
            return (Criteria) this;
        }

        public Criteria andMediumIdEqualTo(String value) {
            addCriterion("mediumId =", value, "mediumId");
            return (Criteria) this;
        }

        public Criteria andMediumIdNotEqualTo(String value) {
            addCriterion("mediumId <>", value, "mediumId");
            return (Criteria) this;
        }

        public Criteria andMediumIdGreaterThan(String value) {
            addCriterion("mediumId >", value, "mediumId");
            return (Criteria) this;
        }

        public Criteria andMediumIdGreaterThanOrEqualTo(String value) {
            addCriterion("mediumId >=", value, "mediumId");
            return (Criteria) this;
        }

        public Criteria andMediumIdLessThan(String value) {
            addCriterion("mediumId <", value, "mediumId");
            return (Criteria) this;
        }

        public Criteria andMediumIdLessThanOrEqualTo(String value) {
            addCriterion("mediumId <=", value, "mediumId");
            return (Criteria) this;
        }

        public Criteria andMediumIdLike(String value) {
            addCriterion("mediumId like", value, "mediumId");
            return (Criteria) this;
        }

        public Criteria andMediumIdNotLike(String value) {
            addCriterion("mediumId not like", value, "mediumId");
            return (Criteria) this;
        }

        public Criteria andMediumIdIn(List<String> values) {
            addCriterion("mediumId in", values, "mediumId");
            return (Criteria) this;
        }

        public Criteria andMediumIdNotIn(List<String> values) {
            addCriterion("mediumId not in", values, "mediumId");
            return (Criteria) this;
        }

        public Criteria andMediumIdBetween(String value1, String value2) {
            addCriterion("mediumId between", value1, value2, "mediumId");
            return (Criteria) this;
        }

        public Criteria andMediumIdNotBetween(String value1, String value2) {
            addCriterion("mediumId not between", value1, value2, "mediumId");
            return (Criteria) this;
        }

        public Criteria andMediumHolderIsNull() {
            addCriterion("mediumHolder is null");
            return (Criteria) this;
        }

        public Criteria andMediumHolderIsNotNull() {
            addCriterion("mediumHolder is not null");
            return (Criteria) this;
        }

        public Criteria andMediumHolderEqualTo(String value) {
            addCriterion("mediumHolder =", value, "mediumHolder");
            return (Criteria) this;
        }

        public Criteria andMediumHolderNotEqualTo(String value) {
            addCriterion("mediumHolder <>", value, "mediumHolder");
            return (Criteria) this;
        }

        public Criteria andMediumHolderGreaterThan(String value) {
            addCriterion("mediumHolder >", value, "mediumHolder");
            return (Criteria) this;
        }

        public Criteria andMediumHolderGreaterThanOrEqualTo(String value) {
            addCriterion("mediumHolder >=", value, "mediumHolder");
            return (Criteria) this;
        }

        public Criteria andMediumHolderLessThan(String value) {
            addCriterion("mediumHolder <", value, "mediumHolder");
            return (Criteria) this;
        }

        public Criteria andMediumHolderLessThanOrEqualTo(String value) {
            addCriterion("mediumHolder <=", value, "mediumHolder");
            return (Criteria) this;
        }

        public Criteria andMediumHolderLike(String value) {
            addCriterion("mediumHolder like", value, "mediumHolder");
            return (Criteria) this;
        }

        public Criteria andMediumHolderNotLike(String value) {
            addCriterion("mediumHolder not like", value, "mediumHolder");
            return (Criteria) this;
        }

        public Criteria andMediumHolderIn(List<String> values) {
            addCriterion("mediumHolder in", values, "mediumHolder");
            return (Criteria) this;
        }

        public Criteria andMediumHolderNotIn(List<String> values) {
            addCriterion("mediumHolder not in", values, "mediumHolder");
            return (Criteria) this;
        }

        public Criteria andMediumHolderBetween(String value1, String value2) {
            addCriterion("mediumHolder between", value1, value2, "mediumHolder");
            return (Criteria) this;
        }

        public Criteria andMediumHolderNotBetween(String value1, String value2) {
            addCriterion("mediumHolder not between", value1, value2, "mediumHolder");
            return (Criteria) this;
        }

        public Criteria andMediumPhoneIsNull() {
            addCriterion("mediumPhone is null");
            return (Criteria) this;
        }

        public Criteria andMediumPhoneIsNotNull() {
            addCriterion("mediumPhone is not null");
            return (Criteria) this;
        }

        public Criteria andMediumPhoneEqualTo(String value) {
            addCriterion("mediumPhone =", value, "mediumPhone");
            return (Criteria) this;
        }

        public Criteria andMediumPhoneNotEqualTo(String value) {
            addCriterion("mediumPhone <>", value, "mediumPhone");
            return (Criteria) this;
        }

        public Criteria andMediumPhoneGreaterThan(String value) {
            addCriterion("mediumPhone >", value, "mediumPhone");
            return (Criteria) this;
        }

        public Criteria andMediumPhoneGreaterThanOrEqualTo(String value) {
            addCriterion("mediumPhone >=", value, "mediumPhone");
            return (Criteria) this;
        }

        public Criteria andMediumPhoneLessThan(String value) {
            addCriterion("mediumPhone <", value, "mediumPhone");
            return (Criteria) this;
        }

        public Criteria andMediumPhoneLessThanOrEqualTo(String value) {
            addCriterion("mediumPhone <=", value, "mediumPhone");
            return (Criteria) this;
        }

        public Criteria andMediumPhoneLike(String value) {
            addCriterion("mediumPhone like", value, "mediumPhone");
            return (Criteria) this;
        }

        public Criteria andMediumPhoneNotLike(String value) {
            addCriterion("mediumPhone not like", value, "mediumPhone");
            return (Criteria) this;
        }

        public Criteria andMediumPhoneIn(List<String> values) {
            addCriterion("mediumPhone in", values, "mediumPhone");
            return (Criteria) this;
        }

        public Criteria andMediumPhoneNotIn(List<String> values) {
            addCriterion("mediumPhone not in", values, "mediumPhone");
            return (Criteria) this;
        }

        public Criteria andMediumPhoneBetween(String value1, String value2) {
            addCriterion("mediumPhone between", value1, value2, "mediumPhone");
            return (Criteria) this;
        }

        public Criteria andMediumPhoneNotBetween(String value1, String value2) {
            addCriterion("mediumPhone not between", value1, value2, "mediumPhone");
            return (Criteria) this;
        }

        public Criteria andQrcodeIdIsNull() {
            addCriterion("qrcodeId is null");
            return (Criteria) this;
        }

        public Criteria andQrcodeIdIsNotNull() {
            addCriterion("qrcodeId is not null");
            return (Criteria) this;
        }

        public Criteria andQrcodeIdEqualTo(String value) {
            addCriterion("qrcodeId =", value, "qrcodeId");
            return (Criteria) this;
        }

        public Criteria andQrcodeIdNotEqualTo(String value) {
            addCriterion("qrcodeId <>", value, "qrcodeId");
            return (Criteria) this;
        }

        public Criteria andQrcodeIdGreaterThan(String value) {
            addCriterion("qrcodeId >", value, "qrcodeId");
            return (Criteria) this;
        }

        public Criteria andQrcodeIdGreaterThanOrEqualTo(String value) {
            addCriterion("qrcodeId >=", value, "qrcodeId");
            return (Criteria) this;
        }

        public Criteria andQrcodeIdLessThan(String value) {
            addCriterion("qrcodeId <", value, "qrcodeId");
            return (Criteria) this;
        }

        public Criteria andQrcodeIdLessThanOrEqualTo(String value) {
            addCriterion("qrcodeId <=", value, "qrcodeId");
            return (Criteria) this;
        }

        public Criteria andQrcodeIdLike(String value) {
            addCriterion("qrcodeId like", value, "qrcodeId");
            return (Criteria) this;
        }

        public Criteria andQrcodeIdNotLike(String value) {
            addCriterion("qrcodeId not like", value, "qrcodeId");
            return (Criteria) this;
        }

        public Criteria andQrcodeIdIn(List<String> values) {
            addCriterion("qrcodeId in", values, "qrcodeId");
            return (Criteria) this;
        }

        public Criteria andQrcodeIdNotIn(List<String> values) {
            addCriterion("qrcodeId not in", values, "qrcodeId");
            return (Criteria) this;
        }

        public Criteria andQrcodeIdBetween(String value1, String value2) {
            addCriterion("qrcodeId between", value1, value2, "qrcodeId");
            return (Criteria) this;
        }

        public Criteria andQrcodeIdNotBetween(String value1, String value2) {
            addCriterion("qrcodeId not between", value1, value2, "qrcodeId");
            return (Criteria) this;
        }

        public Criteria andCodeIsNull() {
            addCriterion("code is null");
            return (Criteria) this;
        }

        public Criteria andCodeIsNotNull() {
            addCriterion("code is not null");
            return (Criteria) this;
        }

        public Criteria andCodeEqualTo(String value) {
            addCriterion("code =", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeNotEqualTo(String value) {
            addCriterion("code <>", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeGreaterThan(String value) {
            addCriterion("code >", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeGreaterThanOrEqualTo(String value) {
            addCriterion("code >=", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeLessThan(String value) {
            addCriterion("code <", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeLessThanOrEqualTo(String value) {
            addCriterion("code <=", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeLike(String value) {
            addCriterion("code like", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeNotLike(String value) {
            addCriterion("code not like", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeIn(List<String> values) {
            addCriterion("code in", values, "code");
            return (Criteria) this;
        }

        public Criteria andCodeNotIn(List<String> values) {
            addCriterion("code not in", values, "code");
            return (Criteria) this;
        }

        public Criteria andCodeBetween(String value1, String value2) {
            addCriterion("code between", value1, value2, "code");
            return (Criteria) this;
        }

        public Criteria andCodeNotBetween(String value1, String value2) {
            addCriterion("code not between", value1, value2, "code");
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

        public Criteria andIsDealIsNull() {
            addCriterion("isDeal is null");
            return (Criteria) this;
        }

        public Criteria andIsDealIsNotNull() {
            addCriterion("isDeal is not null");
            return (Criteria) this;
        }

        public Criteria andIsDealEqualTo(String value) {
            addCriterion("isDeal =", value, "isDeal");
            return (Criteria) this;
        }

        public Criteria andIsDealNotEqualTo(String value) {
            addCriterion("isDeal <>", value, "isDeal");
            return (Criteria) this;
        }

        public Criteria andIsDealGreaterThan(String value) {
            addCriterion("isDeal >", value, "isDeal");
            return (Criteria) this;
        }

        public Criteria andIsDealGreaterThanOrEqualTo(String value) {
            addCriterion("isDeal >=", value, "isDeal");
            return (Criteria) this;
        }

        public Criteria andIsDealLessThan(String value) {
            addCriterion("isDeal <", value, "isDeal");
            return (Criteria) this;
        }

        public Criteria andIsDealLessThanOrEqualTo(String value) {
            addCriterion("isDeal <=", value, "isDeal");
            return (Criteria) this;
        }

        public Criteria andIsDealLike(String value) {
            addCriterion("isDeal like", value, "isDeal");
            return (Criteria) this;
        }

        public Criteria andIsDealNotLike(String value) {
            addCriterion("isDeal not like", value, "isDeal");
            return (Criteria) this;
        }

        public Criteria andIsDealIn(List<String> values) {
            addCriterion("isDeal in", values, "isDeal");
            return (Criteria) this;
        }

        public Criteria andIsDealNotIn(List<String> values) {
            addCriterion("isDeal not in", values, "isDeal");
            return (Criteria) this;
        }

        public Criteria andIsDealBetween(String value1, String value2) {
            addCriterion("isDeal between", value1, value2, "isDeal");
            return (Criteria) this;
        }

        public Criteria andIsDealNotBetween(String value1, String value2) {
            addCriterion("isDeal not between", value1, value2, "isDeal");
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