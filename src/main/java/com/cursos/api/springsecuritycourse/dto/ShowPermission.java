package com.cursos.api.springsecuritycourse.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@Data
public class ShowPermission implements Serializable {
    private Long id;
    private String operation;
    private String httpMethod;
    private String module;
    private String role;

}
