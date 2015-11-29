package com.github.oceanc.mybatis3.generator.plugin.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TableTestSliceModExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    private String tableNameSuffix;

    private String sumCol;

    public TableTestSliceModExample() {
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
        this.tableNameSuffix = null;
        this.sumCol = null;
    }

    public TableTestSliceModExample partitionFactorSliceModId(Long sliceModId) {
        if (sliceModId != null ) {
            long nan = 0;
            StringBuilder sb = new StringBuilder("0");
            for (char c : String.valueOf(sliceModId).toCharArray()) {
                if (Character.isDigit(c)) sb.append(c);
                else nan += c;
            }
            long lid = new BigDecimal(sb.toString()).longValue();
            if(nan > 0) lid += 83 + nan;
            this.tableNameSuffix = (Math.abs(lid) % 83) + "";
        }
        return this;
    }

    public String getTableNameSuffix() {
        return this.tableNameSuffix;
    }

    public void setTableNameSuffix(String tableNameSuffix) {
        this.tableNameSuffix = tableNameSuffix;
    }

    public String getSumCol() {
        return this.sumCol;
    }

    public void setSumCol(String sumCol) {
        this.sumCol = sumCol;
    }

    public TableTestSliceModExample sumId() {
        this.sumCol="ID";
        return this;
    }

    public TableTestSliceModExample sumSliceModId() {
        this.sumCol="SLICE_MOD_ID";
        return this;
    }

    public TableTestSliceModExample sumJacksonId1() {
        this.sumCol="JACKSON_ID1";
        return this;
    }

    public TableTestSliceModExample sumJacksonId2() {
        this.sumCol="JACKSON_ID2";
        return this;
    }

    public TableTestSliceModExample sumJacksonTime() {
        this.sumCol="JACKSON_TIME";
        return this;
    }

    public TableTestSliceModExample sumCouldSumCol() {
        this.sumCol="COULD_SUM_COL";
        return this;
    }

    public TableTestSliceModExample sumVersion() {
        this.sumCol="VERSION";
        return this;
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
            addCriterion("ID is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("ID is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Long value) {
            addCriterion("ID =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("ID <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("ID >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("ID >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("ID <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("ID <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("ID in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("ID not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("ID between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("ID not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andSliceModIdIsNull() {
            addCriterion("SLICE_MOD_ID is null");
            return (Criteria) this;
        }

        public Criteria andSliceModIdIsNotNull() {
            addCriterion("SLICE_MOD_ID is not null");
            return (Criteria) this;
        }

        public Criteria andSliceModIdEqualTo(Long value) {
            addCriterion("SLICE_MOD_ID =", value, "sliceModId");
            return (Criteria) this;
        }

        public Criteria andSliceModIdNotEqualTo(Long value) {
            addCriterion("SLICE_MOD_ID <>", value, "sliceModId");
            return (Criteria) this;
        }

        public Criteria andSliceModIdGreaterThan(Long value) {
            addCriterion("SLICE_MOD_ID >", value, "sliceModId");
            return (Criteria) this;
        }

        public Criteria andSliceModIdGreaterThanOrEqualTo(Long value) {
            addCriterion("SLICE_MOD_ID >=", value, "sliceModId");
            return (Criteria) this;
        }

        public Criteria andSliceModIdLessThan(Long value) {
            addCriterion("SLICE_MOD_ID <", value, "sliceModId");
            return (Criteria) this;
        }

        public Criteria andSliceModIdLessThanOrEqualTo(Long value) {
            addCriterion("SLICE_MOD_ID <=", value, "sliceModId");
            return (Criteria) this;
        }

        public Criteria andSliceModIdIn(List<Long> values) {
            addCriterion("SLICE_MOD_ID in", values, "sliceModId");
            return (Criteria) this;
        }

        public Criteria andSliceModIdNotIn(List<Long> values) {
            addCriterion("SLICE_MOD_ID not in", values, "sliceModId");
            return (Criteria) this;
        }

        public Criteria andSliceModIdBetween(Long value1, Long value2) {
            addCriterion("SLICE_MOD_ID between", value1, value2, "sliceModId");
            return (Criteria) this;
        }

        public Criteria andSliceModIdNotBetween(Long value1, Long value2) {
            addCriterion("SLICE_MOD_ID not between", value1, value2, "sliceModId");
            return (Criteria) this;
        }

        public Criteria andJacksonId1IsNull() {
            addCriterion("JACKSON_ID1 is null");
            return (Criteria) this;
        }

        public Criteria andJacksonId1IsNotNull() {
            addCriterion("JACKSON_ID1 is not null");
            return (Criteria) this;
        }

        public Criteria andJacksonId1EqualTo(Long value) {
            addCriterion("JACKSON_ID1 =", value, "jacksonId1");
            return (Criteria) this;
        }

        public Criteria andJacksonId1NotEqualTo(Long value) {
            addCriterion("JACKSON_ID1 <>", value, "jacksonId1");
            return (Criteria) this;
        }

        public Criteria andJacksonId1GreaterThan(Long value) {
            addCriterion("JACKSON_ID1 >", value, "jacksonId1");
            return (Criteria) this;
        }

        public Criteria andJacksonId1GreaterThanOrEqualTo(Long value) {
            addCriterion("JACKSON_ID1 >=", value, "jacksonId1");
            return (Criteria) this;
        }

        public Criteria andJacksonId1LessThan(Long value) {
            addCriterion("JACKSON_ID1 <", value, "jacksonId1");
            return (Criteria) this;
        }

        public Criteria andJacksonId1LessThanOrEqualTo(Long value) {
            addCriterion("JACKSON_ID1 <=", value, "jacksonId1");
            return (Criteria) this;
        }

        public Criteria andJacksonId1In(List<Long> values) {
            addCriterion("JACKSON_ID1 in", values, "jacksonId1");
            return (Criteria) this;
        }

        public Criteria andJacksonId1NotIn(List<Long> values) {
            addCriterion("JACKSON_ID1 not in", values, "jacksonId1");
            return (Criteria) this;
        }

        public Criteria andJacksonId1Between(Long value1, Long value2) {
            addCriterion("JACKSON_ID1 between", value1, value2, "jacksonId1");
            return (Criteria) this;
        }

        public Criteria andJacksonId1NotBetween(Long value1, Long value2) {
            addCriterion("JACKSON_ID1 not between", value1, value2, "jacksonId1");
            return (Criteria) this;
        }

        public Criteria andJacksonId2IsNull() {
            addCriterion("JACKSON_ID2 is null");
            return (Criteria) this;
        }

        public Criteria andJacksonId2IsNotNull() {
            addCriterion("JACKSON_ID2 is not null");
            return (Criteria) this;
        }

        public Criteria andJacksonId2EqualTo(String value) {
            addCriterion("JACKSON_ID2 =", value, "jacksonId2");
            return (Criteria) this;
        }

        public Criteria andJacksonId2NotEqualTo(String value) {
            addCriterion("JACKSON_ID2 <>", value, "jacksonId2");
            return (Criteria) this;
        }

        public Criteria andJacksonId2GreaterThan(String value) {
            addCriterion("JACKSON_ID2 >", value, "jacksonId2");
            return (Criteria) this;
        }

        public Criteria andJacksonId2GreaterThanOrEqualTo(String value) {
            addCriterion("JACKSON_ID2 >=", value, "jacksonId2");
            return (Criteria) this;
        }

        public Criteria andJacksonId2LessThan(String value) {
            addCriterion("JACKSON_ID2 <", value, "jacksonId2");
            return (Criteria) this;
        }

        public Criteria andJacksonId2LessThanOrEqualTo(String value) {
            addCriterion("JACKSON_ID2 <=", value, "jacksonId2");
            return (Criteria) this;
        }

        public Criteria andJacksonId2Like(String value) {
            addCriterion("JACKSON_ID2 like", value, "jacksonId2");
            return (Criteria) this;
        }

        public Criteria andJacksonId2NotLike(String value) {
            addCriterion("JACKSON_ID2 not like", value, "jacksonId2");
            return (Criteria) this;
        }

        public Criteria andJacksonId2In(List<String> values) {
            addCriterion("JACKSON_ID2 in", values, "jacksonId2");
            return (Criteria) this;
        }

        public Criteria andJacksonId2NotIn(List<String> values) {
            addCriterion("JACKSON_ID2 not in", values, "jacksonId2");
            return (Criteria) this;
        }

        public Criteria andJacksonId2Between(String value1, String value2) {
            addCriterion("JACKSON_ID2 between", value1, value2, "jacksonId2");
            return (Criteria) this;
        }

        public Criteria andJacksonId2NotBetween(String value1, String value2) {
            addCriterion("JACKSON_ID2 not between", value1, value2, "jacksonId2");
            return (Criteria) this;
        }

        public Criteria andJacksonTimeIsNull() {
            addCriterion("JACKSON_TIME is null");
            return (Criteria) this;
        }

        public Criteria andJacksonTimeIsNotNull() {
            addCriterion("JACKSON_TIME is not null");
            return (Criteria) this;
        }

        public Criteria andJacksonTimeEqualTo(Date value) {
            addCriterion("JACKSON_TIME =", value, "jacksonTime");
            return (Criteria) this;
        }

        public Criteria andJacksonTimeNotEqualTo(Date value) {
            addCriterion("JACKSON_TIME <>", value, "jacksonTime");
            return (Criteria) this;
        }

        public Criteria andJacksonTimeGreaterThan(Date value) {
            addCriterion("JACKSON_TIME >", value, "jacksonTime");
            return (Criteria) this;
        }

        public Criteria andJacksonTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("JACKSON_TIME >=", value, "jacksonTime");
            return (Criteria) this;
        }

        public Criteria andJacksonTimeLessThan(Date value) {
            addCriterion("JACKSON_TIME <", value, "jacksonTime");
            return (Criteria) this;
        }

        public Criteria andJacksonTimeLessThanOrEqualTo(Date value) {
            addCriterion("JACKSON_TIME <=", value, "jacksonTime");
            return (Criteria) this;
        }

        public Criteria andJacksonTimeIn(List<Date> values) {
            addCriterion("JACKSON_TIME in", values, "jacksonTime");
            return (Criteria) this;
        }

        public Criteria andJacksonTimeNotIn(List<Date> values) {
            addCriterion("JACKSON_TIME not in", values, "jacksonTime");
            return (Criteria) this;
        }

        public Criteria andJacksonTimeBetween(Date value1, Date value2) {
            addCriterion("JACKSON_TIME between", value1, value2, "jacksonTime");
            return (Criteria) this;
        }

        public Criteria andJacksonTimeNotBetween(Date value1, Date value2) {
            addCriterion("JACKSON_TIME not between", value1, value2, "jacksonTime");
            return (Criteria) this;
        }

        public Criteria andCouldSumColIsNull() {
            addCriterion("COULD_SUM_COL is null");
            return (Criteria) this;
        }

        public Criteria andCouldSumColIsNotNull() {
            addCriterion("COULD_SUM_COL is not null");
            return (Criteria) this;
        }

        public Criteria andCouldSumColEqualTo(Integer value) {
            addCriterion("COULD_SUM_COL =", value, "couldSumCol");
            return (Criteria) this;
        }

        public Criteria andCouldSumColNotEqualTo(Integer value) {
            addCriterion("COULD_SUM_COL <>", value, "couldSumCol");
            return (Criteria) this;
        }

        public Criteria andCouldSumColGreaterThan(Integer value) {
            addCriterion("COULD_SUM_COL >", value, "couldSumCol");
            return (Criteria) this;
        }

        public Criteria andCouldSumColGreaterThanOrEqualTo(Integer value) {
            addCriterion("COULD_SUM_COL >=", value, "couldSumCol");
            return (Criteria) this;
        }

        public Criteria andCouldSumColLessThan(Integer value) {
            addCriterion("COULD_SUM_COL <", value, "couldSumCol");
            return (Criteria) this;
        }

        public Criteria andCouldSumColLessThanOrEqualTo(Integer value) {
            addCriterion("COULD_SUM_COL <=", value, "couldSumCol");
            return (Criteria) this;
        }

        public Criteria andCouldSumColIn(List<Integer> values) {
            addCriterion("COULD_SUM_COL in", values, "couldSumCol");
            return (Criteria) this;
        }

        public Criteria andCouldSumColNotIn(List<Integer> values) {
            addCriterion("COULD_SUM_COL not in", values, "couldSumCol");
            return (Criteria) this;
        }

        public Criteria andCouldSumColBetween(Integer value1, Integer value2) {
            addCriterion("COULD_SUM_COL between", value1, value2, "couldSumCol");
            return (Criteria) this;
        }

        public Criteria andCouldSumColNotBetween(Integer value1, Integer value2) {
            addCriterion("COULD_SUM_COL not between", value1, value2, "couldSumCol");
            return (Criteria) this;
        }

        public Criteria andVersionIsNull() {
            addCriterion("VERSION is null");
            return (Criteria) this;
        }

        public Criteria andVersionIsNotNull() {
            addCriterion("VERSION is not null");
            return (Criteria) this;
        }

        public Criteria andVersionEqualTo(Long value) {
            addCriterion("VERSION =", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionNotEqualTo(Long value) {
            addCriterion("VERSION <>", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionGreaterThan(Long value) {
            addCriterion("VERSION >", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionGreaterThanOrEqualTo(Long value) {
            addCriterion("VERSION >=", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionLessThan(Long value) {
            addCriterion("VERSION <", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionLessThanOrEqualTo(Long value) {
            addCriterion("VERSION <=", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionIn(List<Long> values) {
            addCriterion("VERSION in", values, "version");
            return (Criteria) this;
        }

        public Criteria andVersionNotIn(List<Long> values) {
            addCriterion("VERSION not in", values, "version");
            return (Criteria) this;
        }

        public Criteria andVersionBetween(Long value1, Long value2) {
            addCriterion("VERSION between", value1, value2, "version");
            return (Criteria) this;
        }

        public Criteria andVersionNotBetween(Long value1, Long value2) {
            addCriterion("VERSION not between", value1, value2, "version");
            return (Criteria) this;
        }

        public Criteria addConditionSql(String conditionSql) {
            addCriterion(conditionSql);
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