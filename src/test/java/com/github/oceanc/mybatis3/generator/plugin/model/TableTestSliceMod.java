package com.github.oceanc.mybatis3.generator.plugin.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.TimeZone;
import lombok.Data;

@Data
public class TableTestSliceMod implements Serializable {
    private Long id;

    private Long sliceModId;

    private String tableNameSuffix;

    @JsonProperty("jacksonProperty1")
    private Long jacksonId1;

    @JsonProperty("jacksonProperty2")
    private String jacksonId2;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date jacksonTime;

    private Integer couldSumCol;

    private static final long serialVersionUID = 1L;

    private String updateSql;

    public void setSliceModId(Long sliceModId) {
        this.sliceModId = sliceModId;
        if (this.sliceModId != null ) {
            long nan = 0;
            StringBuilder sb = new StringBuilder("0");
            for (char c : String.valueOf(sliceModId).toCharArray()) {
                if (Character.isDigit(c)) sb.append(c);
                else nan += c;
            }
            long lid = Long.parseLong(sb.toString());
            if(nan > 0) lid += 83 + nan;
            this.tableNameSuffix = (Math.abs(lid) % 83) + "";
        }
    }

    public String toJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setTimeZone(TimeZone.getDefault());
        return mapper.writeValueAsString(this);
    }
}