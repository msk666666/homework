package com.example.demo.service;

import com.example.demo.domain.MiaoshaOrder;
import com.example.demo.domain.MobileUser;
import com.example.demo.domain.OrderInfo;
import com.example.demo.mapper.GoodsMapper;
import com.example.demo.mapper.MiaoShaMapper;
import com.example.demo.mapper.OrderMapper;
import com.example.demo.redis.MiaoshaKey;
import com.example.demo.redis.RedisService;
import com.example.demo.result.CodeMsg;
import com.example.demo.result.Result;
import com.example.demo.util.MD5Util;
import com.example.demo.util.UUIDUtil;
import com.example.demo.vo.GoodsVo;
import com.example.demo.vo.OrderInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.beans.Transient;
import java.util.Random;

@Service
public class MiaoShaService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private MiaoShaMapper miaoShaMapper;

    @Autowired
    private RedisService redisService;

    @Transactional
    public OrderInfo doMiaosha(MobileUser mobileUser, GoodsVo goodsVo){
        //如果减库存成功则创建订单
        int n = goodsMapper.updateGoodsInfo(goodsVo.getId());
        //如果减库存操作失败
        if(n<0){
            //redis中设置标记
            setGoodsOver(goodsVo.getId());
            return null;
        }else {
            return orderInfoService.createOrder(mobileUser,goodsVo);
        }
    }

    public String createMiaoshaPath(MobileUser user, @RequestParam("goodsId") long goodsId) {
        if(user==null||goodsId<=0){
            return null;
        }
        String uuid = UUIDUtil.uuid();
        String path = MD5Util.inputPassToDBPass(uuid, "12345678");
        redisService.set(MiaoshaKey.miaoshaPath,user.getId()+"_"+goodsId,path);
        return path;
    }

    public long getMiaoshaResult(long userId,long goodsId){
        MiaoshaOrder order = orderInfoService.getOrderInfoByUserIdAndGoodsId(userId, goodsId);
        if(order!=null){//秒杀成功
            return order.getOrderId();
        }else {
            boolean goodsOver = getGoodsOver(goodsId);
            //如果redis中没有失败标记则说明服务器还在处理，返回0表示排队中，否则返回-1表示秒杀失败
            if(goodsOver){
                return -1;
            }else {
                return 0;
            }
        }
    }

    private void setGoodsOver(long goodsId) {
        redisService.set(MiaoshaKey.isgoodsOver,""+goodsId,true);
    }
    private boolean getGoodsOver(long goodsId) {
        return redisService.exist(MiaoshaKey.isgoodsOver,""+goodsId);
    }


    public boolean checkMiaoshaPath(MobileUser mobileUser, long goodsId, String path) {
        if (mobileUser == null || goodsId <= 0) {
            return false;
        }
        String redisPath = redisService.get(MiaoshaKey.miaoshaPath, mobileUser.getId() + "_" + goodsId, String.class);
        if(redisPath==null||!redisPath.equals(path)){
            return false;
        }
        return true;
    }

    public BufferedImage createVerifyCode(MobileUser user, long goodsId) {
        if(user==null||goodsId<=0){
            return null;
        }
        int width=80;
        int height=32;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
        Graphics g = image.getGraphics();
        g.setColor(new Color(0xDCDCDC));
        g.fillRect(0,0,width,height);

        g.setColor(Color.BLACK);
        g.drawRect(0,0,width-1,height-1);

        //创建干扰的黑点
        Random rdm = new Random();
        for (int i = 0; i < 50; i++) {
            int x = rdm.nextInt(width);
            int y = rdm.nextInt(height);
            g.drawOval(x,y,0,0);
        }
        String verifyCode=generateVerifyCode(rdm);
        g.setColor(new Color(0,100,0));
        g.setFont(new Font("Candara",Font.BOLD,24));
        g.drawString(verifyCode,8,24);
        g.dispose();
        int rnd=calc(verifyCode);
        redisService.set(MiaoshaKey.miaoshaVerifyCode,user.getId()+"_"+goodsId,rnd);
        return image;
    }

    private int calc(String verifyCode) {
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("JavaScript");
            return (Integer)engine.eval(verifyCode);
        } catch (ScriptException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private static char[] ops=new char[]{'+','-','*'};
    private String generateVerifyCode(Random rdm) {
        int num1 = rdm.nextInt(10);
        int num2 = rdm.nextInt(10);
        int num3 = rdm.nextInt(10);
        char op1 = ops[rdm.nextInt(3)];
        char op2 = ops[rdm.nextInt(3)];
        String exp=""+num1+op1+num2+op2+num3;
        return exp;
    }

    public boolean checkMiaoshaVerifyCode(MobileUser user, long goodsId,int verifyCode) {
        if (user == null || goodsId <= 0) {
            return false;
        }
        Integer redisVerifyCode = redisService.get(MiaoshaKey.miaoshaVerifyCode, user.getId() + "_" + goodsId, Integer.class);
        if(redisVerifyCode!=null&&verifyCode==redisVerifyCode){
            return true;
        }
        return false;
    }
}
