package com.abc.business.excels.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author trivis
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private String id;

    private String name;
    private String code;
    private Integer age;
    private LocalDateTime createTime;

    private String description;
}
