package com.example.demo.rabbitmq;


import com.example.demo.domain.MobileUser;
import lombok.Data;

import java.io.Serializable;

@Data
public class MiaoShaMessage implements Serializable {
    private MobileUser mobileUser;
    private Long goodsId;
}
