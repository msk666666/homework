package com.example.demo.service;

import com.example.demo.domain.Goods;
import com.example.demo.domain.MiaoshaGoods;
import com.example.demo.mapper.GoodsMapper;
import com.example.demo.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsService {
    @Autowired
    private GoodsMapper goodsMapper;

    public List<GoodsVo> getGoodsVoList(){
        return goodsMapper.getGoodsVoList();
    }
    public GoodsVo getGoodsById(Long id){
        return goodsMapper.getDetailById(id);
    }

    public Long getGoodsStock(Long goodsId){
        return goodsMapper.getGoodsStock(goodsId);
    }

    public void resetGoods(){
        List<Goods> goods = goodsMapper.getGoodsList();
        for (Goods good : goods) {
            MiaoshaGoods miaoshaGoods = new MiaoshaGoods();
            miaoshaGoods.setGoodsId(good.getId());
            miaoshaGoods.setStockCount(good.getGoodsStock());
            goodsMapper.resetMSGoodsStock(miaoshaGoods);
        }

    }
}
