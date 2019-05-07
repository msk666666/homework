package com.example.demo.mapper;

import com.example.demo.domain.Goods;
import com.example.demo.domain.MiaoshaGoods;
import com.example.demo.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GoodsMapper {

    List<GoodsVo> getGoodsVoList();

    List<Goods> getGoodsList();

    GoodsVo getDetailById(Long id);

    int updateGoodsInfo(Long goodsId);

    void resetMSGoodsStock(MiaoshaGoods miaoshaGoods);

    Long getGoodsStock(Long goodsId);

}
