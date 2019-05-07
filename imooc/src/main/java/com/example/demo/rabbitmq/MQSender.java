package com.example.demo.rabbitmq;

import com.example.demo.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MQSender {

    @Autowired
    AmqpTemplate amqpTemplate;

    private static Logger logger=LoggerFactory.getLogger(MQReceiver.class);


    public void sendMiaoshaMessage(MiaoShaMessage message){
//        String msg = RedisService.beanToString(message);
        logger.info("Send"+message);
        amqpTemplate.convertAndSend(MQConfig.MIAOSHA_QUEUE,message);
    }

//    public void  sendTopic(Object topicMessage){
//        String msg = RedisService.beanToString(topicMessage);
//        logger.info(msg);
//        //指定交互机名称，指定routing key，放入要发送的消息
//        amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE,"topic.key1",msg+"1");
//        amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE,"topic.key2",msg+"2");
//    }
//
//    public void sendFanout(Object message){
//        String msg = RedisService.beanToString(message);
//        logger.info(msg);
//        amqpTemplate.convertAndSend(MQConfig.FANOUT_EXCHANGE,"",msg);
//    }
//    public void sendheaders(Object message){
//        String msg = RedisService.beanToString(message);
//        logger.info("send headers message:"+msg);
//        MessageProperties properties = new MessageProperties();
//        properties.setHeader("header1","value1");
//        properties.setHeader("header2","value2");
//        Message obj = new Message(msg.getBytes(), properties);
//        amqpTemplate.convertAndSend(MQConfig.HEADWES_EXCHANGE,"",obj);
//    }
}
