package alipay.manage.bean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FileListExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public FileListExample() {
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

        public Criteria andFileIdIsNull() {
            addCriterion("fileId is null");
            return (Criteria) this;
        }

        public Criteria andFileIdIsNotNull() {
            addCriterion("fileId is not null");
            return (Criteria) this;
        }

        public Criteria andFileIdEqualTo(String value) {
            addCriterion("fileId =", value, "fileId");
            return (Criteria) this;
        }

        public Criteria andFileIdNotEqualTo(String value) {
            addCriterion("fileId <>", value, "fileId");
            return (Criteria) this;
        }

        public Criteria andFileIdGreaterThan(String value) {
            addCriterion("fileId >", value, "fileId");
            return (Criteria) this;
        }

        public Criteria andFileIdGreaterThanOrEqualTo(String value) {
            addCriterion("fileId >=", value, "fileId");
            return (Criteria) this;
        }

        public Criteria andFileIdLessThan(String value) {
            addCriterion("fileId <", value, "fileId");
            return (Criteria) this;
        }

        public Criteria andFileIdLessThanOrEqualTo(String value) {
            addCriterion("fileId <=", value, "fileId");
            return (Criteria) this;
        }

        public Criteria andFileIdLike(String value) {
            addCriterion("fileId like", value, "fileId");
            return (Criteria) this;
        }

        public Criteria andFileIdNotLike(String value) {
            addCriterion("fileId not like", value, "fileId");
            return (Criteria) this;
        }

        public Criteria andFileIdIn(List<String> values) {
            addCriterion("fileId in", values, "fileId");
            return (Criteria) this;
        }

        public Criteria andFileIdNotIn(List<String> values) {
            addCriterion("fileId not in", values, "fileId");
            return (Criteria) this;
        }

        public Criteria andFileIdBetween(String value1, String value2) {
            addCriterion("fileId between", value1, value2, "fileId");
            return (Criteria) this;
        }

        public Criteria andFileIdNotBetween(String value1, String value2) {
            addCriterion("fileId not between", value1, value2, "fileId");
            return (Criteria) this;
        }

        public Criteria andFileholderIsNull() {
            addCriterion("fileholder is null");
            return (Criteria) this;
        }

        public Criteria andFileholderIsNotNull() {
            addCriterion("fileholder is not null");
            return (Criteria) this;
        }

        public Criteria andFileholderEqualTo(String value) {
            addCriterion("fileholder =", value, "fileholder");
            return (Criteria) this;
        }

        public Criteria andFileholderNotEqualTo(String value) {
            addCriterion("fileholder <>", value, "fileholder");
            return (Criteria) this;
        }

        public Criteria andFileholderGreaterThan(String value) {
            addCriterion("fileholder >", value, "fileholder");
            return (Criteria) this;
        }

        public Criteria andFileholderGreaterThanOrEqualTo(String value) {
            addCriterion("fileholder >=", value, "fileholder");
            return (Criteria) this;
        }

        public Criteria andFileholderLessThan(String value) {
            addCriterion("fileholder <", value, "fileholder");
            return (Criteria) this;
        }

        public Criteria andFileholderLessThanOrEqualTo(String value) {
            addCriterion("fileholder <=", value, "fileholder");
            return (Criteria) this;
        }

        public Criteria andFileholderLike(String value) {
            addCriterion("fileholder like", value, "fileholder");
            return (Criteria) this;
        }

        public Criteria andFileholderNotLike(String value) {
            addCriterion("fileholder not like", value, "fileholder");
            return (Criteria) this;
        }

        public Criteria andFileholderIn(List<String> values) {
            addCriterion("fileholder in", values, "fileholder");
            return (Criteria) this;
        }

        public Criteria andFileholderNotIn(List<String> values) {
            addCriterion("fileholder not in", values, "fileholder");
            return (Criteria) this;
        }

        public Criteria andFileholderBetween(String value1, String value2) {
            addCriterion("fileholder between", value1, value2, "fileholder");
            return (Criteria) this;
        }

        public Criteria andFileholderNotBetween(String value1, String value2) {
            addCriterion("fileholder not between", value1, value2, "fileholder");
            return (Criteria) this;
        }

        public Criteria andIsFixationIsNull() {
            addCriterion("isFixation is null");
            return (Criteria) this;
        }

        public Criteria andIsFixationIsNotNull() {
            addCriterion("isFixation is not null");
            return (Criteria) this;
        }

        public Criteria andIsFixationEqualTo(String value) {
            addCriterion("isFixation =", value, "isFixation");
            return (Criteria) this;
        }

        public Criteria andIsFixationNotEqualTo(String value) {
            addCriterion("isFixation <>", value, "isFixation");
            return (Criteria) this;
        }

        public Criteria andIsFixationGreaterThan(String value) {
            addCriterion("isFixation >", value, "isFixation");
            return (Criteria) this;
        }

        public Criteria andIsFixationGreaterThanOrEqualTo(String value) {
            addCriterion("isFixation >=", value, "isFixation");
            return (Criteria) this;
        }

        public Criteria andIsFixationLessThan(String value) {
            addCriterion("isFixation <", value, "isFixation");
            return (Criteria) this;
        }

        public Criteria andIsFixationLessThanOrEqualTo(String value) {
            addCriterion("isFixation <=", value, "isFixation");
            return (Criteria) this;
        }

        public Criteria andIsFixationLike(String value) {
            addCriterion("isFixation like", value, "isFixation");
            return (Criteria) this;
        }

        public Criteria andIsFixationNotLike(String value) {
            addCriterion("isFixation not like", value, "isFixation");
            return (Criteria) this;
        }

        public Criteria andIsFixationIn(List<String> values) {
            addCriterion("isFixation in", values, "isFixation");
            return (Criteria) this;
        }

        public Criteria andIsFixationNotIn(List<String> values) {
            addCriterion("isFixation not in", values, "isFixation");
            return (Criteria) this;
        }

        public Criteria andIsFixationBetween(String value1, String value2) {
            addCriterion("isFixation between", value1, value2, "isFixation");
            return (Criteria) this;
        }

        public Criteria andIsFixationNotBetween(String value1, String value2) {
            addCriterion("isFixation not between", value1, value2, "isFixation");
            return (Criteria) this;
        }

        public Criteria andFixationAmountIsNull() {
            addCriterion("fixationAmount is null");
            return (Criteria) this;
        }

        public Criteria andFixationAmountIsNotNull() {
            addCriterion("fixationAmount is not null");
            return (Criteria) this;
        }

        public Criteria andFixationAmountEqualTo(BigDecimal value) {
            addCriterion("fixationAmount =", value, "fixationAmount");
            return (Criteria) this;
        }

        public Criteria andFixationAmountNotEqualTo(BigDecimal value) {
            addCriterion("fixationAmount <>", value, "fixationAmount");
            return (Criteria) this;
        }

        public Criteria andFixationAmountGreaterThan(BigDecimal value) {
            addCriterion("fixationAmount >", value, "fixationAmount");
            return (Criteria) this;
        }

        public Criteria andFixationAmountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("fixationAmount >=", value, "fixationAmount");
            return (Criteria) this;
        }

        public Criteria andFixationAmountLessThan(BigDecimal value) {
            addCriterion("fixationAmount <", value, "fixationAmount");
            return (Criteria) this;
        }

        public Criteria andFixationAmountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("fixationAmount <=", value, "fixationAmount");
            return (Criteria) this;
        }

        public Criteria andFixationAmountIn(List<BigDecimal> values) {
            addCriterion("fixationAmount in", values, "fixationAmount");
            return (Criteria) this;
        }

        public Criteria andFixationAmountNotIn(List<BigDecimal> values) {
            addCriterion("fixationAmount not in", values, "fixationAmount");
            return (Criteria) this;
        }

        public Criteria andFixationAmountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("fixationAmount between", value1, value2, "fixationAmount");
            return (Criteria) this;
        }

        public Criteria andFixationAmountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("fixationAmount not between", value1, value2, "fixationAmount");
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

        public Criteria andConcealIdIsNull() {
            addCriterion("concealId is null");
            return (Criteria) this;
        }

        public Criteria andConcealIdIsNotNull() {
            addCriterion("concealId is not null");
            return (Criteria) this;
        }

        public Criteria andConcealIdEqualTo(String value) {
            addCriterion("concealId =", value, "concealId");
            return (Criteria) this;
        }

        public Criteria andConcealIdNotEqualTo(String value) {
            addCriterion("concealId <>", value, "concealId");
            return (Criteria) this;
        }

        public Criteria andConcealIdGreaterThan(String value) {
            addCriterion("concealId >", value, "concealId");
            return (Criteria) this;
        }

        public Criteria andConcealIdGreaterThanOrEqualTo(String value) {
            addCriterion("concealId >=", value, "concealId");
            return (Criteria) this;
        }

        public Criteria andConcealIdLessThan(String value) {
            addCriterion("concealId <", value, "concealId");
            return (Criteria) this;
        }

        public Criteria andConcealIdLessThanOrEqualTo(String value) {
            addCriterion("concealId <=", value, "concealId");
            return (Criteria) this;
        }

        public Criteria andConcealIdLike(String value) {
            addCriterion("concealId like", value, "concealId");
            return (Criteria) this;
        }

        public Criteria andConcealIdNotLike(String value) {
            addCriterion("concealId not like", value, "concealId");
            return (Criteria) this;
        }

        public Criteria andConcealIdIn(List<String> values) {
            addCriterion("concealId in", values, "concealId");
            return (Criteria) this;
        }

        public Criteria andConcealIdNotIn(List<String> values) {
            addCriterion("concealId not in", values, "concealId");
            return (Criteria) this;
        }

        public Criteria andConcealIdBetween(String value1, String value2) {
            addCriterion("concealId between", value1, value2, "concealId");
            return (Criteria) this;
        }

        public Criteria andConcealIdNotBetween(String value1, String value2) {
            addCriterion("concealId not between", value1, value2, "concealId");
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

        public Criteria andIsCutIsNull() {
            addCriterion("isCut is null");
            return (Criteria) this;
        }

        public Criteria andIsCutIsNotNull() {
            addCriterion("isCut is not null");
            return (Criteria) this;
        }

        public Criteria andIsCutEqualTo(String value) {
            addCriterion("isCut =", value, "isCut");
            return (Criteria) this;
        }

        public Criteria andIsCutNotEqualTo(String value) {
            addCriterion("isCut <>", value, "isCut");
            return (Criteria) this;
        }

        public Criteria andIsCutGreaterThan(String value) {
            addCriterion("isCut >", value, "isCut");
            return (Criteria) this;
        }

        public Criteria andIsCutGreaterThanOrEqualTo(String value) {
            addCriterion("isCut >=", value, "isCut");
            return (Criteria) this;
        }

        public Criteria andIsCutLessThan(String value) {
            addCriterion("isCut <", value, "isCut");
            return (Criteria) this;
        }

        public Criteria andIsCutLessThanOrEqualTo(String value) {
            addCriterion("isCut <=", value, "isCut");
            return (Criteria) this;
        }

        public Criteria andIsCutLike(String value) {
            addCriterion("isCut like", value, "isCut");
            return (Criteria) this;
        }

        public Criteria andIsCutNotLike(String value) {
            addCriterion("isCut not like", value, "isCut");
            return (Criteria) this;
        }

        public Criteria andIsCutIn(List<String> values) {
            addCriterion("isCut in", values, "isCut");
            return (Criteria) this;
        }

        public Criteria andIsCutNotIn(List<String> values) {
            addCriterion("isCut not in", values, "isCut");
            return (Criteria) this;
        }

        public Criteria andIsCutBetween(String value1, String value2) {
            addCriterion("isCut between", value1, value2, "isCut");
            return (Criteria) this;
        }

        public Criteria andIsCutNotBetween(String value1, String value2) {
            addCriterion("isCut not between", value1, value2, "isCut");
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