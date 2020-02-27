package alipay.manage.bean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserInfoExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public UserInfoExample() {
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

        public Criteria andPasswordIsNull() {
            addCriterion("password is null");
            return (Criteria) this;
        }

        public Criteria andPasswordIsNotNull() {
            addCriterion("password is not null");
            return (Criteria) this;
        }

        public Criteria andPasswordEqualTo(String value) {
            addCriterion("password =", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordNotEqualTo(String value) {
            addCriterion("password <>", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordGreaterThan(String value) {
            addCriterion("password >", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordGreaterThanOrEqualTo(String value) {
            addCriterion("password >=", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordLessThan(String value) {
            addCriterion("password <", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordLessThanOrEqualTo(String value) {
            addCriterion("password <=", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordLike(String value) {
            addCriterion("password like", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordNotLike(String value) {
            addCriterion("password not like", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordIn(List<String> values) {
            addCriterion("password in", values, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordNotIn(List<String> values) {
            addCriterion("password not in", values, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordBetween(String value1, String value2) {
            addCriterion("password between", value1, value2, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordNotBetween(String value1, String value2) {
            addCriterion("password not between", value1, value2, "password");
            return (Criteria) this;
        }

        public Criteria andPayPaswordIsNull() {
            addCriterion("payPasword is null");
            return (Criteria) this;
        }

        public Criteria andPayPaswordIsNotNull() {
            addCriterion("payPasword is not null");
            return (Criteria) this;
        }

        public Criteria andPayPaswordEqualTo(String value) {
            addCriterion("payPasword =", value, "payPasword");
            return (Criteria) this;
        }

        public Criteria andPayPaswordNotEqualTo(String value) {
            addCriterion("payPasword <>", value, "payPasword");
            return (Criteria) this;
        }

        public Criteria andPayPaswordGreaterThan(String value) {
            addCriterion("payPasword >", value, "payPasword");
            return (Criteria) this;
        }

        public Criteria andPayPaswordGreaterThanOrEqualTo(String value) {
            addCriterion("payPasword >=", value, "payPasword");
            return (Criteria) this;
        }

        public Criteria andPayPaswordLessThan(String value) {
            addCriterion("payPasword <", value, "payPasword");
            return (Criteria) this;
        }

        public Criteria andPayPaswordLessThanOrEqualTo(String value) {
            addCriterion("payPasword <=", value, "payPasword");
            return (Criteria) this;
        }

        public Criteria andPayPaswordLike(String value) {
            addCriterion("payPasword like", value, "payPasword");
            return (Criteria) this;
        }

        public Criteria andPayPaswordNotLike(String value) {
            addCriterion("payPasword not like", value, "payPasword");
            return (Criteria) this;
        }

        public Criteria andPayPaswordIn(List<String> values) {
            addCriterion("payPasword in", values, "payPasword");
            return (Criteria) this;
        }

        public Criteria andPayPaswordNotIn(List<String> values) {
            addCriterion("payPasword not in", values, "payPasword");
            return (Criteria) this;
        }

        public Criteria andPayPaswordBetween(String value1, String value2) {
            addCriterion("payPasword between", value1, value2, "payPasword");
            return (Criteria) this;
        }

        public Criteria andPayPaswordNotBetween(String value1, String value2) {
            addCriterion("payPasword not between", value1, value2, "payPasword");
            return (Criteria) this;
        }

        public Criteria andSaltIsNull() {
            addCriterion("salt is null");
            return (Criteria) this;
        }

        public Criteria andSaltIsNotNull() {
            addCriterion("salt is not null");
            return (Criteria) this;
        }

        public Criteria andSaltEqualTo(String value) {
            addCriterion("salt =", value, "salt");
            return (Criteria) this;
        }

        public Criteria andSaltNotEqualTo(String value) {
            addCriterion("salt <>", value, "salt");
            return (Criteria) this;
        }

        public Criteria andSaltGreaterThan(String value) {
            addCriterion("salt >", value, "salt");
            return (Criteria) this;
        }

        public Criteria andSaltGreaterThanOrEqualTo(String value) {
            addCriterion("salt >=", value, "salt");
            return (Criteria) this;
        }

        public Criteria andSaltLessThan(String value) {
            addCriterion("salt <", value, "salt");
            return (Criteria) this;
        }

        public Criteria andSaltLessThanOrEqualTo(String value) {
            addCriterion("salt <=", value, "salt");
            return (Criteria) this;
        }

        public Criteria andSaltLike(String value) {
            addCriterion("salt like", value, "salt");
            return (Criteria) this;
        }

        public Criteria andSaltNotLike(String value) {
            addCriterion("salt not like", value, "salt");
            return (Criteria) this;
        }

        public Criteria andSaltIn(List<String> values) {
            addCriterion("salt in", values, "salt");
            return (Criteria) this;
        }

        public Criteria andSaltNotIn(List<String> values) {
            addCriterion("salt not in", values, "salt");
            return (Criteria) this;
        }

        public Criteria andSaltBetween(String value1, String value2) {
            addCriterion("salt between", value1, value2, "salt");
            return (Criteria) this;
        }

        public Criteria andSaltNotBetween(String value1, String value2) {
            addCriterion("salt not between", value1, value2, "salt");
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

        public Criteria andUserTypeEqualTo(Integer value) {
            addCriterion("userType =", value, "userType");
            return (Criteria) this;
        }

        public Criteria andUserTypeNotEqualTo(Integer value) {
            addCriterion("userType <>", value, "userType");
            return (Criteria) this;
        }

        public Criteria andUserTypeGreaterThan(Integer value) {
            addCriterion("userType >", value, "userType");
            return (Criteria) this;
        }

        public Criteria andUserTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("userType >=", value, "userType");
            return (Criteria) this;
        }

        public Criteria andUserTypeLessThan(Integer value) {
            addCriterion("userType <", value, "userType");
            return (Criteria) this;
        }

        public Criteria andUserTypeLessThanOrEqualTo(Integer value) {
            addCriterion("userType <=", value, "userType");
            return (Criteria) this;
        }

        public Criteria andUserTypeIn(List<Integer> values) {
            addCriterion("userType in", values, "userType");
            return (Criteria) this;
        }

        public Criteria andUserTypeNotIn(List<Integer> values) {
            addCriterion("userType not in", values, "userType");
            return (Criteria) this;
        }

        public Criteria andUserTypeBetween(Integer value1, Integer value2) {
            addCriterion("userType between", value1, value2, "userType");
            return (Criteria) this;
        }

        public Criteria andUserTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("userType not between", value1, value2, "userType");
            return (Criteria) this;
        }

        public Criteria andSwitchsIsNull() {
            addCriterion("switchs is null");
            return (Criteria) this;
        }

        public Criteria andSwitchsIsNotNull() {
            addCriterion("switchs is not null");
            return (Criteria) this;
        }

        public Criteria andSwitchsEqualTo(Integer value) {
            addCriterion("switchs =", value, "switchs");
            return (Criteria) this;
        }

        public Criteria andSwitchsNotEqualTo(Integer value) {
            addCriterion("switchs <>", value, "switchs");
            return (Criteria) this;
        }

        public Criteria andSwitchsGreaterThan(Integer value) {
            addCriterion("switchs >", value, "switchs");
            return (Criteria) this;
        }

        public Criteria andSwitchsGreaterThanOrEqualTo(Integer value) {
            addCriterion("switchs >=", value, "switchs");
            return (Criteria) this;
        }

        public Criteria andSwitchsLessThan(Integer value) {
            addCriterion("switchs <", value, "switchs");
            return (Criteria) this;
        }

        public Criteria andSwitchsLessThanOrEqualTo(Integer value) {
            addCriterion("switchs <=", value, "switchs");
            return (Criteria) this;
        }

        public Criteria andSwitchsIn(List<Integer> values) {
            addCriterion("switchs in", values, "switchs");
            return (Criteria) this;
        }

        public Criteria andSwitchsNotIn(List<Integer> values) {
            addCriterion("switchs not in", values, "switchs");
            return (Criteria) this;
        }

        public Criteria andSwitchsBetween(Integer value1, Integer value2) {
            addCriterion("switchs between", value1, value2, "switchs");
            return (Criteria) this;
        }

        public Criteria andSwitchsNotBetween(Integer value1, Integer value2) {
            addCriterion("switchs not between", value1, value2, "switchs");
            return (Criteria) this;
        }

        public Criteria andUserNodeIsNull() {
            addCriterion("userNode is null");
            return (Criteria) this;
        }

        public Criteria andUserNodeIsNotNull() {
            addCriterion("userNode is not null");
            return (Criteria) this;
        }

        public Criteria andUserNodeEqualTo(String value) {
            addCriterion("userNode =", value, "userNode");
            return (Criteria) this;
        }

        public Criteria andUserNodeNotEqualTo(String value) {
            addCriterion("userNode <>", value, "userNode");
            return (Criteria) this;
        }

        public Criteria andUserNodeGreaterThan(String value) {
            addCriterion("userNode >", value, "userNode");
            return (Criteria) this;
        }

        public Criteria andUserNodeGreaterThanOrEqualTo(String value) {
            addCriterion("userNode >=", value, "userNode");
            return (Criteria) this;
        }

        public Criteria andUserNodeLessThan(String value) {
            addCriterion("userNode <", value, "userNode");
            return (Criteria) this;
        }

        public Criteria andUserNodeLessThanOrEqualTo(String value) {
            addCriterion("userNode <=", value, "userNode");
            return (Criteria) this;
        }

        public Criteria andUserNodeLike(String value) {
            addCriterion("userNode like", value, "userNode");
            return (Criteria) this;
        }

        public Criteria andUserNodeNotLike(String value) {
            addCriterion("userNode not like", value, "userNode");
            return (Criteria) this;
        }

        public Criteria andUserNodeIn(List<String> values) {
            addCriterion("userNode in", values, "userNode");
            return (Criteria) this;
        }

        public Criteria andUserNodeNotIn(List<String> values) {
            addCriterion("userNode not in", values, "userNode");
            return (Criteria) this;
        }

        public Criteria andUserNodeBetween(String value1, String value2) {
            addCriterion("userNode between", value1, value2, "userNode");
            return (Criteria) this;
        }

        public Criteria andUserNodeNotBetween(String value1, String value2) {
            addCriterion("userNode not between", value1, value2, "userNode");
            return (Criteria) this;
        }

        public Criteria andEmailIsNull() {
            addCriterion("email is null");
            return (Criteria) this;
        }

        public Criteria andEmailIsNotNull() {
            addCriterion("email is not null");
            return (Criteria) this;
        }

        public Criteria andEmailEqualTo(String value) {
            addCriterion("email =", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailNotEqualTo(String value) {
            addCriterion("email <>", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailGreaterThan(String value) {
            addCriterion("email >", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailGreaterThanOrEqualTo(String value) {
            addCriterion("email >=", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailLessThan(String value) {
            addCriterion("email <", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailLessThanOrEqualTo(String value) {
            addCriterion("email <=", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailLike(String value) {
            addCriterion("email like", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailNotLike(String value) {
            addCriterion("email not like", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailIn(List<String> values) {
            addCriterion("email in", values, "email");
            return (Criteria) this;
        }

        public Criteria andEmailNotIn(List<String> values) {
            addCriterion("email not in", values, "email");
            return (Criteria) this;
        }

        public Criteria andEmailBetween(String value1, String value2) {
            addCriterion("email between", value1, value2, "email");
            return (Criteria) this;
        }

        public Criteria andEmailNotBetween(String value1, String value2) {
            addCriterion("email not between", value1, value2, "email");
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

        public Criteria andCreditIsNull() {
            addCriterion("credit is null");
            return (Criteria) this;
        }

        public Criteria andCreditIsNotNull() {
            addCriterion("credit is not null");
            return (Criteria) this;
        }

        public Criteria andCreditEqualTo(BigDecimal value) {
            addCriterion("credit =", value, "credit");
            return (Criteria) this;
        }

        public Criteria andCreditNotEqualTo(BigDecimal value) {
            addCriterion("credit <>", value, "credit");
            return (Criteria) this;
        }

        public Criteria andCreditGreaterThan(BigDecimal value) {
            addCriterion("credit >", value, "credit");
            return (Criteria) this;
        }

        public Criteria andCreditGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("credit >=", value, "credit");
            return (Criteria) this;
        }

        public Criteria andCreditLessThan(BigDecimal value) {
            addCriterion("credit <", value, "credit");
            return (Criteria) this;
        }

        public Criteria andCreditLessThanOrEqualTo(BigDecimal value) {
            addCriterion("credit <=", value, "credit");
            return (Criteria) this;
        }

        public Criteria andCreditIn(List<BigDecimal> values) {
            addCriterion("credit in", values, "credit");
            return (Criteria) this;
        }

        public Criteria andCreditNotIn(List<BigDecimal> values) {
            addCriterion("credit not in", values, "credit");
            return (Criteria) this;
        }

        public Criteria andCreditBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("credit between", value1, value2, "credit");
            return (Criteria) this;
        }

        public Criteria andCreditNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("credit not between", value1, value2, "credit");
            return (Criteria) this;
        }

        public Criteria andReceiveOrderStateIsNull() {
            addCriterion("receiveOrderState is null");
            return (Criteria) this;
        }

        public Criteria andReceiveOrderStateIsNotNull() {
            addCriterion("receiveOrderState is not null");
            return (Criteria) this;
        }

        public Criteria andReceiveOrderStateEqualTo(Integer value) {
            addCriterion("receiveOrderState =", value, "receiveOrderState");
            return (Criteria) this;
        }

        public Criteria andReceiveOrderStateNotEqualTo(Integer value) {
            addCriterion("receiveOrderState <>", value, "receiveOrderState");
            return (Criteria) this;
        }

        public Criteria andReceiveOrderStateGreaterThan(Integer value) {
            addCriterion("receiveOrderState >", value, "receiveOrderState");
            return (Criteria) this;
        }

        public Criteria andReceiveOrderStateGreaterThanOrEqualTo(Integer value) {
            addCriterion("receiveOrderState >=", value, "receiveOrderState");
            return (Criteria) this;
        }

        public Criteria andReceiveOrderStateLessThan(Integer value) {
            addCriterion("receiveOrderState <", value, "receiveOrderState");
            return (Criteria) this;
        }

        public Criteria andReceiveOrderStateLessThanOrEqualTo(Integer value) {
            addCriterion("receiveOrderState <=", value, "receiveOrderState");
            return (Criteria) this;
        }

        public Criteria andReceiveOrderStateIn(List<Integer> values) {
            addCriterion("receiveOrderState in", values, "receiveOrderState");
            return (Criteria) this;
        }

        public Criteria andReceiveOrderStateNotIn(List<Integer> values) {
            addCriterion("receiveOrderState not in", values, "receiveOrderState");
            return (Criteria) this;
        }

        public Criteria andReceiveOrderStateBetween(Integer value1, Integer value2) {
            addCriterion("receiveOrderState between", value1, value2, "receiveOrderState");
            return (Criteria) this;
        }

        public Criteria andReceiveOrderStateNotBetween(Integer value1, Integer value2) {
            addCriterion("receiveOrderState not between", value1, value2, "receiveOrderState");
            return (Criteria) this;
        }

        public Criteria andRemitOrderStateIsNull() {
            addCriterion("remitOrderState is null");
            return (Criteria) this;
        }

        public Criteria andRemitOrderStateIsNotNull() {
            addCriterion("remitOrderState is not null");
            return (Criteria) this;
        }

        public Criteria andRemitOrderStateEqualTo(Integer value) {
            addCriterion("remitOrderState =", value, "remitOrderState");
            return (Criteria) this;
        }

        public Criteria andRemitOrderStateNotEqualTo(Integer value) {
            addCriterion("remitOrderState <>", value, "remitOrderState");
            return (Criteria) this;
        }

        public Criteria andRemitOrderStateGreaterThan(Integer value) {
            addCriterion("remitOrderState >", value, "remitOrderState");
            return (Criteria) this;
        }

        public Criteria andRemitOrderStateGreaterThanOrEqualTo(Integer value) {
            addCriterion("remitOrderState >=", value, "remitOrderState");
            return (Criteria) this;
        }

        public Criteria andRemitOrderStateLessThan(Integer value) {
            addCriterion("remitOrderState <", value, "remitOrderState");
            return (Criteria) this;
        }

        public Criteria andRemitOrderStateLessThanOrEqualTo(Integer value) {
            addCriterion("remitOrderState <=", value, "remitOrderState");
            return (Criteria) this;
        }

        public Criteria andRemitOrderStateIn(List<Integer> values) {
            addCriterion("remitOrderState in", values, "remitOrderState");
            return (Criteria) this;
        }

        public Criteria andRemitOrderStateNotIn(List<Integer> values) {
            addCriterion("remitOrderState not in", values, "remitOrderState");
            return (Criteria) this;
        }

        public Criteria andRemitOrderStateBetween(Integer value1, Integer value2) {
            addCriterion("remitOrderState between", value1, value2, "remitOrderState");
            return (Criteria) this;
        }

        public Criteria andRemitOrderStateNotBetween(Integer value1, Integer value2) {
            addCriterion("remitOrderState not between", value1, value2, "remitOrderState");
            return (Criteria) this;
        }

        public Criteria andQQIsNull() {
            addCriterion("QQ is null");
            return (Criteria) this;
        }

        public Criteria andQQIsNotNull() {
            addCriterion("QQ is not null");
            return (Criteria) this;
        }

        public Criteria andQQEqualTo(String value) {
            addCriterion("QQ =", value, "QQ");
            return (Criteria) this;
        }

        public Criteria andQQNotEqualTo(String value) {
            addCriterion("QQ <>", value, "QQ");
            return (Criteria) this;
        }

        public Criteria andQQGreaterThan(String value) {
            addCriterion("QQ >", value, "QQ");
            return (Criteria) this;
        }

        public Criteria andQQGreaterThanOrEqualTo(String value) {
            addCriterion("QQ >=", value, "QQ");
            return (Criteria) this;
        }

        public Criteria andQQLessThan(String value) {
            addCriterion("QQ <", value, "QQ");
            return (Criteria) this;
        }

        public Criteria andQQLessThanOrEqualTo(String value) {
            addCriterion("QQ <=", value, "QQ");
            return (Criteria) this;
        }

        public Criteria andQQLike(String value) {
            addCriterion("QQ like", value, "QQ");
            return (Criteria) this;
        }

        public Criteria andQQNotLike(String value) {
            addCriterion("QQ not like", value, "QQ");
            return (Criteria) this;
        }

        public Criteria andQQIn(List<String> values) {
            addCriterion("QQ in", values, "QQ");
            return (Criteria) this;
        }

        public Criteria andQQNotIn(List<String> values) {
            addCriterion("QQ not in", values, "QQ");
            return (Criteria) this;
        }

        public Criteria andQQBetween(String value1, String value2) {
            addCriterion("QQ between", value1, value2, "QQ");
            return (Criteria) this;
        }

        public Criteria andQQNotBetween(String value1, String value2) {
            addCriterion("QQ not between", value1, value2, "QQ");
            return (Criteria) this;
        }

        public Criteria andTelegramIsNull() {
            addCriterion("telegram is null");
            return (Criteria) this;
        }

        public Criteria andTelegramIsNotNull() {
            addCriterion("telegram is not null");
            return (Criteria) this;
        }

        public Criteria andTelegramEqualTo(String value) {
            addCriterion("telegram =", value, "telegram");
            return (Criteria) this;
        }

        public Criteria andTelegramNotEqualTo(String value) {
            addCriterion("telegram <>", value, "telegram");
            return (Criteria) this;
        }

        public Criteria andTelegramGreaterThan(String value) {
            addCriterion("telegram >", value, "telegram");
            return (Criteria) this;
        }

        public Criteria andTelegramGreaterThanOrEqualTo(String value) {
            addCriterion("telegram >=", value, "telegram");
            return (Criteria) this;
        }

        public Criteria andTelegramLessThan(String value) {
            addCriterion("telegram <", value, "telegram");
            return (Criteria) this;
        }

        public Criteria andTelegramLessThanOrEqualTo(String value) {
            addCriterion("telegram <=", value, "telegram");
            return (Criteria) this;
        }

        public Criteria andTelegramLike(String value) {
            addCriterion("telegram like", value, "telegram");
            return (Criteria) this;
        }

        public Criteria andTelegramNotLike(String value) {
            addCriterion("telegram not like", value, "telegram");
            return (Criteria) this;
        }

        public Criteria andTelegramIn(List<String> values) {
            addCriterion("telegram in", values, "telegram");
            return (Criteria) this;
        }

        public Criteria andTelegramNotIn(List<String> values) {
            addCriterion("telegram not in", values, "telegram");
            return (Criteria) this;
        }

        public Criteria andTelegramBetween(String value1, String value2) {
            addCriterion("telegram between", value1, value2, "telegram");
            return (Criteria) this;
        }

        public Criteria andTelegramNotBetween(String value1, String value2) {
            addCriterion("telegram not between", value1, value2, "telegram");
            return (Criteria) this;
        }

        public Criteria andSkypeIsNull() {
            addCriterion("skype is null");
            return (Criteria) this;
        }

        public Criteria andSkypeIsNotNull() {
            addCriterion("skype is not null");
            return (Criteria) this;
        }

        public Criteria andSkypeEqualTo(String value) {
            addCriterion("skype =", value, "skype");
            return (Criteria) this;
        }

        public Criteria andSkypeNotEqualTo(String value) {
            addCriterion("skype <>", value, "skype");
            return (Criteria) this;
        }

        public Criteria andSkypeGreaterThan(String value) {
            addCriterion("skype >", value, "skype");
            return (Criteria) this;
        }

        public Criteria andSkypeGreaterThanOrEqualTo(String value) {
            addCriterion("skype >=", value, "skype");
            return (Criteria) this;
        }

        public Criteria andSkypeLessThan(String value) {
            addCriterion("skype <", value, "skype");
            return (Criteria) this;
        }

        public Criteria andSkypeLessThanOrEqualTo(String value) {
            addCriterion("skype <=", value, "skype");
            return (Criteria) this;
        }

        public Criteria andSkypeLike(String value) {
            addCriterion("skype like", value, "skype");
            return (Criteria) this;
        }

        public Criteria andSkypeNotLike(String value) {
            addCriterion("skype not like", value, "skype");
            return (Criteria) this;
        }

        public Criteria andSkypeIn(List<String> values) {
            addCriterion("skype in", values, "skype");
            return (Criteria) this;
        }

        public Criteria andSkypeNotIn(List<String> values) {
            addCriterion("skype not in", values, "skype");
            return (Criteria) this;
        }

        public Criteria andSkypeBetween(String value1, String value2) {
            addCriterion("skype between", value1, value2, "skype");
            return (Criteria) this;
        }

        public Criteria andSkypeNotBetween(String value1, String value2) {
            addCriterion("skype not between", value1, value2, "skype");
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