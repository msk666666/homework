package com.example.demo.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class MobileUser implements Serializable {
    private Long id;
    private String nickname;
    private String password;
    private String salt;
    private String head;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date registerDate;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date lastLoginDate;
    private Integer loginCount;


}
