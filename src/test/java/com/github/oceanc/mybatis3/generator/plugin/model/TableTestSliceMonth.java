package com.github.oceanc.mybatis3.generator.plugin.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import lombok.Data;

@Data
public class TableTestSliceMonth implements Serializable {
    private Long id;

    private Date sliceMonthId;

    @JsonProperty("jacksonProperty1")
    private Long jacksonId1;

    private Long version;

    @JsonProperty("jacksonProperty2")
    private String jacksonId2;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date jacksonTime;

    private Integer couldSumCol;

    private static final long serialVersionUID = 1L;

    private String tableNameSuffix;

    private String updateSql;

    public void setSliceMonthId(Date sliceMonthId) {
        this.sliceMonthId = sliceMonthId;
        if (this.sliceMonthId != null ) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(sliceMonthId.getTime());
            int m = calendar.get(Calendar.MONTH) + 1;
            this.tableNameSuffix = calendar.get(Calendar.YEAR) + (m < 10 ? "0" + m : "" + m);
        }
    }

    public String toJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setTimeZone(TimeZone.getDefault());
        return mapper.writeValueAsString(this);
    }
}