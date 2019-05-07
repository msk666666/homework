package com.example.demo.rabbitmq;


import com.example.demo.domain.MiaoshaOrder;
import com.example.demo.domain.MobileUser;
import com.example.demo.domain.OrderInfo;
import com.example.demo.redis.RedisService;
import com.example.demo.service.GoodsService;
import com.example.demo.service.MiaoShaService;
import com.example.demo.service.OrderInfoService;
import com.example.demo.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.PrintStream;


@Service
public class MQReceiver {


    private static Logger logger=LoggerFactory.getLogger(MQReceiver.class);

    @Autowired
    GoodsService goodsService;

    @Autowired
    MiaoShaService miaoShaService;

    @Autowired
    OrderInfoService orderInfoService;

    @Autowired
    AmqpTemplate amqpTemplate;

//    @RabbitListener(queues = MQConfig.QUEUE)
//    public void receive(String message){
//        logger.info("receiver"+message);
//    }
//
//    @RabbitListener(queues = MQConfig.TOPIC_QUEUE1)
//    public void receiveTopic1(String message){
//        logger.info("topic queue1 message:"+message);
//    }
//    @RabbitListener(queues = MQConfig.TOPIC_QUEUE2)
//    public void receiveTopic2(String message){
//        logger.info("topic queue2 message:"+message);
//    }
//    @RabbitListener(queues = MQConfig.HEADERS_QUEUE)
//    public void receiveHeaders(byte[] message){
//        logger.info("headers queue message:"+new String(message));
//    }

    @RabbitListener(queues = MQConfig.MIAOSHA_QUEUE)
    public void receivemiaosha(MiaoShaMessage miaoShaMessage) throws FileNotFoundException {
//        MiaoShaMessage miaoShaMessage = RedisService.stringToBean(message, MiaoShaMessage.class);
        MobileUser user = miaoShaMessage.getMobileUser();
        Long goodsId = miaoShaMessage.getGoodsId();

        Long stock = goodsService.getGoodsStock(goodsId);
        if(stock<0){
            return;
        }
        MiaoshaOrder order = orderInfoService.getOrderInfoByUserIdAndGoodsId(user.getId(),goodsId);
        if(order!=null){
            return;
        }
        GoodsVo goods = goodsService.getGoodsById(goodsId);
        try {
            miaoShaService.doMiaosha(user,goods);
        }catch (DuplicateKeyException e){
            e.getMessage();
        }

    }

}
