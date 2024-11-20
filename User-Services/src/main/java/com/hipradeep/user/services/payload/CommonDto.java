package com.hipradeep.user.services.payload;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonDto {

    private String fieldName1;
    private String fieldName2;
    private String fieldName3;
    private String fieldName4;
    private Integer fieldName5;
    private Integer fieldName6;
    private Integer fieldName7;
}
