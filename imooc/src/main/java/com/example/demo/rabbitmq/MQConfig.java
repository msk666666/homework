package com.example.demo.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;


@Configuration
public class MQConfig {

    //    public static final String QUEUE="queue";
    public static final String MIAOSHA_QUEUE = "miaosha_queue";
//    public static final String TOPIC_QUEUE1="topic_queue1";
//    public static final String TOPIC_QUEUE2="topic_queue2";
//    public static final String HEADERS_QUEUE="headers_queue2";
//
//    public static final String TOPIC_EXCHANGE="topic_exchange";
//    public static final String FANOUT_EXCHANGE="fanout_exchange";
//    public static final String HEADWES_EXCHANGE="headers_exchange";


//    /**
//     * Direct交互机模式
//     * @return
//     */
//    @Bean
//    public Queue queue(){
//        //第一个参数指明队列的名字，第二个参数指明是否支持持久化
//        return new Queue(QUEUE,true);
//    }

    @Bean
    public Queue miaoshaqueue() {
        return new Queue(MIAOSHA_QUEUE, true);
    }

//    /**
//     * Topic交互模式
//     */
//    @Bean
//    public Queue topicQueue1(){
//        return new Queue(TOPIC_QUEUE1,true);
//    }
//
//    @Bean
//    public Queue topicQueue2(){
//        return new Queue(TOPIC_QUEUE2,true);
//    }
//
//    //创建一个topic交换机
//    @Bean
//    public TopicExchange topicExchange(){
//        return new TopicExchange(TOPIC_EXCHANGE);
//    }
//
//    //给创建的topic类型交换机 绑定queue队列并指定binding key需要匹配的rounting key的规则
//    @Bean
//    public Binding topicBinding1(){
//        return BindingBuilder.bind(topicQueue1()).to(topicExchange()).with("topic.key1");
//    }
//    @Bean
//    public Binding topicBinding2(){
//        return BindingBuilder.bind(topicQueue2()).to(topicExchange()).with("topic.#");
//    }
//
//    //创建fanout交换机
//    @Bean
//    public FanoutExchange fanoutExchange(){
//        return new FanoutExchange(FANOUT_EXCHANGE);
//    }
//    @Bean
//    public Binding fanoutBinding1(){
//        return BindingBuilder.bind(topicQueue1()).to(fanoutExchange());
//    }
//    @Bean
//    public Binding fanoutBinding2(){
//        return BindingBuilder.bind(topicQueue2()).to(fanoutExchange());
//    }
//
//    //创建headers交换机
//    @Bean
//    public HeadersExchange headersExchange(){
//        return new HeadersExchange(HEADWES_EXCHANGE);
//    }
//
//    @Bean
//    public Queue headersQueue(){
//        return new Queue(HEADERS_QUEUE);
//    }
//
//    @Bean
//    public Binding headersBinding(){
//        //创建map并指定匹配键值对
//        HashMap<String, Object> map = new HashMap<>();
//        map.put("header1","value1");
//        map.put("header2","value2");
//        //表示所有的键值对都匹配上这个消息才能入队
//        return BindingBuilder.bind(headersQueue()).to(headersExchange()).whereAll(map).match();
//    }
}
