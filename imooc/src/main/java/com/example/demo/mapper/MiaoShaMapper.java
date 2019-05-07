package com.example.demo.mapper;

import com.example.demo.vo.OrderInfoVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MiaoShaMapper {

    OrderInfoVo getMiaoshaOrder(Long id);

    void deleteMiaoshaOrder();
}
