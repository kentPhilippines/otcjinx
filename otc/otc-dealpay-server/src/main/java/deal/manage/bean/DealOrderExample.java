package deal.manage.bean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DealOrderExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public DealOrderExample() {
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

        public Criteria andOrderStatusIsNull() {
            addCriterion("orderStatus is null");
            return (Criteria) this;
        }

        public Criteria andOrderStatusIsNotNull() {
            addCriterion("orderStatus is not null");
            return (Criteria) this;
        }

        public Criteria andOrderStatusEqualTo(String value) {
            addCriterion("orderStatus =", value, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andOrderStatusNotEqualTo(String value) {
            addCriterion("orderStatus <>", value, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andOrderStatusGreaterThan(String value) {
            addCriterion("orderStatus >", value, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andOrderStatusGreaterThanOrEqualTo(String value) {
            addCriterion("orderStatus >=", value, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andOrderStatusLessThan(String value) {
            addCriterion("orderStatus <", value, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andOrderStatusLessThanOrEqualTo(String value) {
            addCriterion("orderStatus <=", value, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andOrderStatusLike(String value) {
            addCriterion("orderStatus like", value, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andOrderStatusNotLike(String value) {
            addCriterion("orderStatus not like", value, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andOrderStatusIn(List<String> values) {
            addCriterion("orderStatus in", values, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andOrderStatusNotIn(List<String> values) {
            addCriterion("orderStatus not in", values, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andOrderStatusBetween(String value1, String value2) {
            addCriterion("orderStatus between", value1, value2, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andOrderStatusNotBetween(String value1, String value2) {
            addCriterion("orderStatus not between", value1, value2, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andDealAmountIsNull() {
            addCriterion("dealAmount is null");
            return (Criteria) this;
        }

        public Criteria andDealAmountIsNotNull() {
            addCriterion("dealAmount is not null");
            return (Criteria) this;
        }

        public Criteria andDealAmountEqualTo(BigDecimal value) {
            addCriterion("dealAmount =", value, "dealAmount");
            return (Criteria) this;
        }

        public Criteria andDealAmountNotEqualTo(BigDecimal value) {
            addCriterion("dealAmount <>", value, "dealAmount");
            return (Criteria) this;
        }

        public Criteria andDealAmountGreaterThan(BigDecimal value) {
            addCriterion("dealAmount >", value, "dealAmount");
            return (Criteria) this;
        }

        public Criteria andDealAmountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("dealAmount >=", value, "dealAmount");
            return (Criteria) this;
        }

        public Criteria andDealAmountLessThan(BigDecimal value) {
            addCriterion("dealAmount <", value, "dealAmount");
            return (Criteria) this;
        }

        public Criteria andDealAmountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("dealAmount <=", value, "dealAmount");
            return (Criteria) this;
        }

        public Criteria andDealAmountIn(List<BigDecimal> values) {
            addCriterion("dealAmount in", values, "dealAmount");
            return (Criteria) this;
        }

        public Criteria andDealAmountNotIn(List<BigDecimal> values) {
            addCriterion("dealAmount not in", values, "dealAmount");
            return (Criteria) this;
        }

        public Criteria andDealAmountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("dealAmount between", value1, value2, "dealAmount");
            return (Criteria) this;
        }

        public Criteria andDealAmountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("dealAmount not between", value1, value2, "dealAmount");
            return (Criteria) this;
        }

        public Criteria andDealFeeIsNull() {
            addCriterion("dealFee is null");
            return (Criteria) this;
        }

        public Criteria andDealFeeIsNotNull() {
            addCriterion("dealFee is not null");
            return (Criteria) this;
        }

        public Criteria andDealFeeEqualTo(BigDecimal value) {
            addCriterion("dealFee =", value, "dealFee");
            return (Criteria) this;
        }

        public Criteria andDealFeeNotEqualTo(BigDecimal value) {
            addCriterion("dealFee <>", value, "dealFee");
            return (Criteria) this;
        }

        public Criteria andDealFeeGreaterThan(BigDecimal value) {
            addCriterion("dealFee >", value, "dealFee");
            return (Criteria) this;
        }

        public Criteria andDealFeeGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("dealFee >=", value, "dealFee");
            return (Criteria) this;
        }

        public Criteria andDealFeeLessThan(BigDecimal value) {
            addCriterion("dealFee <", value, "dealFee");
            return (Criteria) this;
        }

        public Criteria andDealFeeLessThanOrEqualTo(BigDecimal value) {
            addCriterion("dealFee <=", value, "dealFee");
            return (Criteria) this;
        }

        public Criteria andDealFeeIn(List<BigDecimal> values) {
            addCriterion("dealFee in", values, "dealFee");
            return (Criteria) this;
        }

        public Criteria andDealFeeNotIn(List<BigDecimal> values) {
            addCriterion("dealFee not in", values, "dealFee");
            return (Criteria) this;
        }

        public Criteria andDealFeeBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("dealFee between", value1, value2, "dealFee");
            return (Criteria) this;
        }

        public Criteria andDealFeeNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("dealFee not between", value1, value2, "dealFee");
            return (Criteria) this;
        }

        public Criteria andActualAmountIsNull() {
            addCriterion("actualAmount is null");
            return (Criteria) this;
        }

        public Criteria andActualAmountIsNotNull() {
            addCriterion("actualAmount is not null");
            return (Criteria) this;
        }

        public Criteria andActualAmountEqualTo(BigDecimal value) {
            addCriterion("actualAmount =", value, "actualAmount");
            return (Criteria) this;
        }

        public Criteria andActualAmountNotEqualTo(BigDecimal value) {
            addCriterion("actualAmount <>", value, "actualAmount");
            return (Criteria) this;
        }

        public Criteria andActualAmountGreaterThan(BigDecimal value) {
            addCriterion("actualAmount >", value, "actualAmount");
            return (Criteria) this;
        }

        public Criteria andActualAmountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("actualAmount >=", value, "actualAmount");
            return (Criteria) this;
        }

        public Criteria andActualAmountLessThan(BigDecimal value) {
            addCriterion("actualAmount <", value, "actualAmount");
            return (Criteria) this;
        }

        public Criteria andActualAmountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("actualAmount <=", value, "actualAmount");
            return (Criteria) this;
        }

        public Criteria andActualAmountIn(List<BigDecimal> values) {
            addCriterion("actualAmount in", values, "actualAmount");
            return (Criteria) this;
        }

        public Criteria andActualAmountNotIn(List<BigDecimal> values) {
            addCriterion("actualAmount not in", values, "actualAmount");
            return (Criteria) this;
        }

        public Criteria andActualAmountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("actualAmount between", value1, value2, "actualAmount");
            return (Criteria) this;
        }

        public Criteria andActualAmountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("actualAmount not between", value1, value2, "actualAmount");
            return (Criteria) this;
        }

        public Criteria andOrderTypeIsNull() {
            addCriterion("orderType is null");
            return (Criteria) this;
        }

        public Criteria andOrderTypeIsNotNull() {
            addCriterion("orderType is not null");
            return (Criteria) this;
        }

        public Criteria andOrderTypeEqualTo(String value) {
            addCriterion("orderType =", value, "orderType");
            return (Criteria) this;
        }

        public Criteria andOrderTypeNotEqualTo(String value) {
            addCriterion("orderType <>", value, "orderType");
            return (Criteria) this;
        }

        public Criteria andOrderTypeGreaterThan(String value) {
            addCriterion("orderType >", value, "orderType");
            return (Criteria) this;
        }

        public Criteria andOrderTypeGreaterThanOrEqualTo(String value) {
            addCriterion("orderType >=", value, "orderType");
            return (Criteria) this;
        }

        public Criteria andOrderTypeLessThan(String value) {
            addCriterion("orderType <", value, "orderType");
            return (Criteria) this;
        }

        public Criteria andOrderTypeLessThanOrEqualTo(String value) {
            addCriterion("orderType <=", value, "orderType");
            return (Criteria) this;
        }

        public Criteria andOrderTypeLike(String value) {
            addCriterion("orderType like", value, "orderType");
            return (Criteria) this;
        }

        public Criteria andOrderTypeNotLike(String value) {
            addCriterion("orderType not like", value, "orderType");
            return (Criteria) this;
        }

        public Criteria andOrderTypeIn(List<String> values) {
            addCriterion("orderType in", values, "orderType");
            return (Criteria) this;
        }

        public Criteria andOrderTypeNotIn(List<String> values) {
            addCriterion("orderType not in", values, "orderType");
            return (Criteria) this;
        }

        public Criteria andOrderTypeBetween(String value1, String value2) {
            addCriterion("orderType between", value1, value2, "orderType");
            return (Criteria) this;
        }

        public Criteria andOrderTypeNotBetween(String value1, String value2) {
            addCriterion("orderType not between", value1, value2, "orderType");
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

        public Criteria andOrderQrUserIsNull() {
            addCriterion("orderQrUser is null");
            return (Criteria) this;
        }

        public Criteria andOrderQrUserIsNotNull() {
            addCriterion("orderQrUser is not null");
            return (Criteria) this;
        }

        public Criteria andOrderQrUserEqualTo(String value) {
            addCriterion("orderQrUser =", value, "orderQrUser");
            return (Criteria) this;
        }

        public Criteria andOrderQrUserNotEqualTo(String value) {
            addCriterion("orderQrUser <>", value, "orderQrUser");
            return (Criteria) this;
        }
        public Criteria andOrderQrUserListEqualTo(List value) {
            addCriterion("orderQrUser in", value, "orderQrUser");
            return (Criteria) this;
        }
        public Criteria andOrderQrUserGreaterThan(String value) {
            addCriterion("orderQrUser >", value, "orderQrUser");
            return (Criteria) this;
        }

        public Criteria andOrderQrUserGreaterThanOrEqualTo(String value) {
            addCriterion("orderQrUser >=", value, "orderQrUser");
            return (Criteria) this;
        }

        public Criteria andOrderQrUserLessThan(String value) {
            addCriterion("orderQrUser <", value, "orderQrUser");
            return (Criteria) this;
        }

        public Criteria andOrderQrUserLessThanOrEqualTo(String value) {
            addCriterion("orderQrUser <=", value, "orderQrUser");
            return (Criteria) this;
        }

        public Criteria andOrderQrUserLike(String value) {
            addCriterion("orderQrUser like", value, "orderQrUser");
            return (Criteria) this;
        }

        public Criteria andOrderQrUserNotLike(String value) {
            addCriterion("orderQrUser not like", value, "orderQrUser");
            return (Criteria) this;
        }

        public Criteria andOrderQrUserIn(List<String> values) {
            addCriterion("orderQrUser in", values, "orderQrUser");
            return (Criteria) this;
        }

        public Criteria andOrderQrUserNotIn(List<String> values) {
            addCriterion("orderQrUser not in", values, "orderQrUser");
            return (Criteria) this;
        }

        public Criteria andOrderQrUserBetween(String value1, String value2) {
            addCriterion("orderQrUser between", value1, value2, "orderQrUser");
            return (Criteria) this;
        }

        public Criteria andOrderQrUserNotBetween(String value1, String value2) {
            addCriterion("orderQrUser not between", value1, value2, "orderQrUser");
            return (Criteria) this;
        }

        public Criteria andOrderQrIsNull() {
            addCriterion("orderQr is null");
            return (Criteria) this;
        }

        public Criteria andOrderQrIsNotNull() {
            addCriterion("orderQr is not null");
            return (Criteria) this;
        }

        public Criteria andOrderQrEqualTo(String value) {
            addCriterion("orderQr =", value, "orderQr");
            return (Criteria) this;
        }

        public Criteria andOrderQrNotEqualTo(String value) {
            addCriterion("orderQr <>", value, "orderQr");
            return (Criteria) this;
        }

        public Criteria andOrderQrGreaterThan(String value) {
            addCriterion("orderQr >", value, "orderQr");
            return (Criteria) this;
        }

        public Criteria andOrderQrGreaterThanOrEqualTo(String value) {
            addCriterion("orderQr >=", value, "orderQr");
            return (Criteria) this;
        }

        public Criteria andOrderQrLessThan(String value) {
            addCriterion("orderQr <", value, "orderQr");
            return (Criteria) this;
        }

        public Criteria andOrderQrLessThanOrEqualTo(String value) {
            addCriterion("orderQr <=", value, "orderQr");
            return (Criteria) this;
        }

        public Criteria andOrderQrLike(String value) {
            addCriterion("orderQr like", value, "orderQr");
            return (Criteria) this;
        }

        public Criteria andOrderQrNotLike(String value) {
            addCriterion("orderQr not like", value, "orderQr");
            return (Criteria) this;
        }

        public Criteria andOrderQrIn(List<String> values) {
            addCriterion("orderQr in", values, "orderQr");
            return (Criteria) this;
        }

        public Criteria andOrderQrNotIn(List<String> values) {
            addCriterion("orderQr not in", values, "orderQr");
            return (Criteria) this;
        }

        public Criteria andOrderQrBetween(String value1, String value2) {
            addCriterion("orderQr between", value1, value2, "orderQr");
            return (Criteria) this;
        }

        public Criteria andOrderQrNotBetween(String value1, String value2) {
            addCriterion("orderQr not between", value1, value2, "orderQr");
            return (Criteria) this;
        }

        public Criteria andExternalOrderIdIsNull() {
            addCriterion("externalOrderId is null");
            return (Criteria) this;
        }

        public Criteria andExternalOrderIdIsNotNull() {
            addCriterion("externalOrderId is not null");
            return (Criteria) this;
        }

        public Criteria andExternalOrderIdEqualTo(String value) {
            addCriterion("externalOrderId =", value, "externalOrderId");
            return (Criteria) this;
        }

        public Criteria andExternalOrderIdNotEqualTo(String value) {
            addCriterion("externalOrderId <>", value, "externalOrderId");
            return (Criteria) this;
        }

        public Criteria andExternalOrderIdGreaterThan(String value) {
            addCriterion("externalOrderId >", value, "externalOrderId");
            return (Criteria) this;
        }

        public Criteria andExternalOrderIdGreaterThanOrEqualTo(String value) {
            addCriterion("externalOrderId >=", value, "externalOrderId");
            return (Criteria) this;
        }

        public Criteria andExternalOrderIdLessThan(String value) {
            addCriterion("externalOrderId <", value, "externalOrderId");
            return (Criteria) this;
        }

        public Criteria andExternalOrderIdLessThanOrEqualTo(String value) {
            addCriterion("externalOrderId <=", value, "externalOrderId");
            return (Criteria) this;
        }

        public Criteria andExternalOrderIdLike(String value) {
            addCriterion("externalOrderId like", value, "externalOrderId");
            return (Criteria) this;
        }

        public Criteria andExternalOrderIdNotLike(String value) {
            addCriterion("externalOrderId not like", value, "externalOrderId");
            return (Criteria) this;
        }

        public Criteria andExternalOrderIdIn(List<String> values) {
            addCriterion("externalOrderId in", values, "externalOrderId");
            return (Criteria) this;
        }

        public Criteria andExternalOrderIdNotIn(List<String> values) {
            addCriterion("externalOrderId not in", values, "externalOrderId");
            return (Criteria) this;
        }

        public Criteria andExternalOrderIdBetween(String value1, String value2) {
            addCriterion("externalOrderId between", value1, value2, "externalOrderId");
            return (Criteria) this;
        }

        public Criteria andExternalOrderIdNotBetween(String value1, String value2) {
            addCriterion("externalOrderId not between", value1, value2, "externalOrderId");
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

        public Criteria andNotifyIsNull() {
            addCriterion("notify is null");
            return (Criteria) this;
        }

        public Criteria andNotifyIsNotNull() {
            addCriterion("notify is not null");
            return (Criteria) this;
        }

        public Criteria andNotifyEqualTo(String value) {
            addCriterion("notify =", value, "notify");
            return (Criteria) this;
        }

        public Criteria andNotifyNotEqualTo(String value) {
            addCriterion("notify <>", value, "notify");
            return (Criteria) this;
        }

        public Criteria andNotifyGreaterThan(String value) {
            addCriterion("notify >", value, "notify");
            return (Criteria) this;
        }

        public Criteria andNotifyGreaterThanOrEqualTo(String value) {
            addCriterion("notify >=", value, "notify");
            return (Criteria) this;
        }

        public Criteria andNotifyLessThan(String value) {
            addCriterion("notify <", value, "notify");
            return (Criteria) this;
        }

        public Criteria andNotifyLessThanOrEqualTo(String value) {
            addCriterion("notify <=", value, "notify");
            return (Criteria) this;
        }

        public Criteria andNotifyLike(String value) {
            addCriterion("notify like", value, "notify");
            return (Criteria) this;
        }

        public Criteria andNotifyNotLike(String value) {
            addCriterion("notify not like", value, "notify");
            return (Criteria) this;
        }

        public Criteria andNotifyIn(List<String> values) {
            addCriterion("notify in", values, "notify");
            return (Criteria) this;
        }

        public Criteria andNotifyNotIn(List<String> values) {
            addCriterion("notify not in", values, "notify");
            return (Criteria) this;
        }

        public Criteria andNotifyBetween(String value1, String value2) {
            addCriterion("notify between", value1, value2, "notify");
            return (Criteria) this;
        }

        public Criteria andNotifyNotBetween(String value1, String value2) {
            addCriterion("notify not between", value1, value2, "notify");
            return (Criteria) this;
        }

        public Criteria andBackIsNull() {
            addCriterion("back is null");
            return (Criteria) this;
        }

        public Criteria andBackIsNotNull() {
            addCriterion("back is not null");
            return (Criteria) this;
        }

        public Criteria andBackEqualTo(String value) {
            addCriterion("back =", value, "back");
            return (Criteria) this;
        }

        public Criteria andBackNotEqualTo(String value) {
            addCriterion("back <>", value, "back");
            return (Criteria) this;
        }

        public Criteria andBackGreaterThan(String value) {
            addCriterion("back >", value, "back");
            return (Criteria) this;
        }

        public Criteria andBackGreaterThanOrEqualTo(String value) {
            addCriterion("back >=", value, "back");
            return (Criteria) this;
        }

        public Criteria andBackLessThan(String value) {
            addCriterion("back <", value, "back");
            return (Criteria) this;
        }

        public Criteria andBackLessThanOrEqualTo(String value) {
            addCriterion("back <=", value, "back");
            return (Criteria) this;
        }

        public Criteria andBackLike(String value) {
            addCriterion("back like", value, "back");
            return (Criteria) this;
        }

        public Criteria andBackNotLike(String value) {
            addCriterion("back not like", value, "back");
            return (Criteria) this;
        }

        public Criteria andBackIn(List<String> values) {
            addCriterion("back in", values, "back");
            return (Criteria) this;
        }

        public Criteria andBackNotIn(List<String> values) {
            addCriterion("back not in", values, "back");
            return (Criteria) this;
        }

        public Criteria andBackBetween(String value1, String value2) {
            addCriterion("back between", value1, value2, "back");
            return (Criteria) this;
        }

        public Criteria andBackNotBetween(String value1, String value2) {
            addCriterion("back not between", value1, value2, "back");
            return (Criteria) this;
        }

        public Criteria andIsNotifyIsNull() {
            addCriterion("isNotify is null");
            return (Criteria) this;
        }

        public Criteria andIsNotifyIsNotNull() {
            addCriterion("isNotify is not null");
            return (Criteria) this;
        }

        public Criteria andIsNotifyEqualTo(String value) {
            addCriterion("isNotify =", value, "isNotify");
            return (Criteria) this;
        }

        public Criteria andIsNotifyNotEqualTo(String value) {
            addCriterion("isNotify <>", value, "isNotify");
            return (Criteria) this;
        }

        public Criteria andIsNotifyGreaterThan(String value) {
            addCriterion("isNotify >", value, "isNotify");
            return (Criteria) this;
        }

        public Criteria andIsNotifyGreaterThanOrEqualTo(String value) {
            addCriterion("isNotify >=", value, "isNotify");
            return (Criteria) this;
        }

        public Criteria andIsNotifyLessThan(String value) {
            addCriterion("isNotify <", value, "isNotify");
            return (Criteria) this;
        }

        public Criteria andIsNotifyLessThanOrEqualTo(String value) {
            addCriterion("isNotify <=", value, "isNotify");
            return (Criteria) this;
        }

        public Criteria andIsNotifyLike(String value) {
            addCriterion("isNotify like", value, "isNotify");
            return (Criteria) this;
        }

        public Criteria andIsNotifyNotLike(String value) {
            addCriterion("isNotify not like", value, "isNotify");
            return (Criteria) this;
        }

        public Criteria andIsNotifyIn(List<String> values) {
            addCriterion("isNotify in", values, "isNotify");
            return (Criteria) this;
        }

        public Criteria andIsNotifyNotIn(List<String> values) {
            addCriterion("isNotify not in", values, "isNotify");
            return (Criteria) this;
        }

        public Criteria andIsNotifyBetween(String value1, String value2) {
            addCriterion("isNotify between", value1, value2, "isNotify");
            return (Criteria) this;
        }

        public Criteria andIsNotifyNotBetween(String value1, String value2) {
            addCriterion("isNotify not between", value1, value2, "isNotify");
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

        public Criteria andFeeIdIsNull() {
            addCriterion("feeId is null");
            return (Criteria) this;
        }

        public Criteria andFeeIdIsNotNull() {
            addCriterion("feeId is not null");
            return (Criteria) this;
        }

        public Criteria andFeeIdEqualTo(Integer value) {
            addCriterion("feeId =", value, "feeId");
            return (Criteria) this;
        }

        public Criteria andFeeIdNotEqualTo(Integer value) {
            addCriterion("feeId <>", value, "feeId");
            return (Criteria) this;
        }

        public Criteria andFeeIdGreaterThan(Integer value) {
            addCriterion("feeId >", value, "feeId");
            return (Criteria) this;
        }

        public Criteria andFeeIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("feeId >=", value, "feeId");
            return (Criteria) this;
        }

        public Criteria andFeeIdLessThan(Integer value) {
            addCriterion("feeId <", value, "feeId");
            return (Criteria) this;
        }

        public Criteria andFeeIdLessThanOrEqualTo(Integer value) {
            addCriterion("feeId <=", value, "feeId");
            return (Criteria) this;
        }

        public Criteria andFeeIdIn(List<Integer> values) {
            addCriterion("feeId in", values, "feeId");
            return (Criteria) this;
        }

        public Criteria andFeeIdNotIn(List<Integer> values) {
            addCriterion("feeId not in", values, "feeId");
            return (Criteria) this;
        }

        public Criteria andFeeIdBetween(Integer value1, Integer value2) {
            addCriterion("feeId between", value1, value2, "feeId");
            return (Criteria) this;
        }

        public Criteria andFeeIdNotBetween(Integer value1, Integer value2) {
            addCriterion("feeId not between", value1, value2, "feeId");
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

        public Criteria andProductTypeIsNull() {
            addCriterion("productType is null");
            return (Criteria) this;
        }

        public Criteria andProductTypeIsNotNull() {
            addCriterion("productType is not null");
            return (Criteria) this;
        }

        public Criteria andProductTypeEqualTo(String value) {
            addCriterion("productType =", value, "productType");
            return (Criteria) this;
        }

        public Criteria andProductTypeNotEqualTo(String value) {
            addCriterion("productType <>", value, "productType");
            return (Criteria) this;
        }

        public Criteria andProductTypeGreaterThan(String value) {
            addCriterion("productType >", value, "productType");
            return (Criteria) this;
        }

        public Criteria andProductTypeGreaterThanOrEqualTo(String value) {
            addCriterion("productType >=", value, "productType");
            return (Criteria) this;
        }

        public Criteria andProductTypeLessThan(String value) {
            addCriterion("productType <", value, "productType");
            return (Criteria) this;
        }

        public Criteria andProductTypeLessThanOrEqualTo(String value) {
            addCriterion("productType <=", value, "productType");
            return (Criteria) this;
        }

        public Criteria andProductTypeLike(String value) {
            addCriterion("productType like", value, "productType");
            return (Criteria) this;
        }

        public Criteria andProductTypeNotLike(String value) {
            addCriterion("productType not like", value, "productType");
            return (Criteria) this;
        }

        public Criteria andProductTypeIn(List<String> values) {
            addCriterion("productType in", values, "productType");
            return (Criteria) this;
        }

        public Criteria andProductTypeNotIn(List<String> values) {
            addCriterion("productType not in", values, "productType");
            return (Criteria) this;
        }

        public Criteria andProductTypeBetween(String value1, String value2) {
            addCriterion("productType between", value1, value2, "productType");
            return (Criteria) this;
        }

        public Criteria andProductTypeNotBetween(String value1, String value2) {
            addCriterion("productType not between", value1, value2, "productType");
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