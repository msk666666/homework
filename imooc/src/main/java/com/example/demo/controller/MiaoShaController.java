package com.example.demo.controller;

import com.example.demo.domain.MiaoshaOrder;
import com.example.demo.domain.MobileUser;
import com.example.demo.domain.OrderInfo;
import com.example.demo.filter.AccessLimit;
import com.example.demo.rabbitmq.MQReceiver;
import com.example.demo.rabbitmq.MQSender;
import com.example.demo.rabbitmq.MiaoShaMessage;
import com.example.demo.redis.GoodsKey;
import com.example.demo.redis.MiaoshaKey;
import com.example.demo.redis.OrderKey;
import com.example.demo.redis.RedisService;
import com.example.demo.result.CodeMsg;
import com.example.demo.result.Result;
import com.example.demo.service.GoodsService;
import com.example.demo.service.MiaoShaService;
import com.example.demo.service.OrderInfoService;
import com.example.demo.util.MD5Util;
import com.example.demo.util.UUIDUtil;
import com.example.demo.vo.GoodsVo;
import com.example.demo.vo.OrderInfoVo;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/miaosha")
public class MiaoShaController implements InitializingBean {

    private static Logger logger=LoggerFactory.getLogger(MiaoShaController.class);

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private OrderInfoService orderInfoService;
    @Autowired
    private MiaoShaService miaoShaService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private MQSender sender;

//    @RequestMapping("/do_miaosha")
//    public String doMiaoSha(Model model, MobileUser mobileUser, @RequestParam("goodsId") long goodsId){
//        //检测库存数量
//        GoodsVo goods = goodsService.getGoodsById(goodsId);
//        if(goods.getStockCount()<=0){
//            model.addAttribute("miaoshaErr",CodeMsg.Stock_NotEnough);
//            return "miaoshafail";
//        }
//
//        //检查是否已经秒杀到了
//        OrderInfo order = orderInfoService.getOrderInfoByUserIdAndGoodsId(mobileUser.getId(), goodsId);
//        if(order!=null){
////            return Result.error(CodeMsg.MiaoSha_HasFinished);
//            model.addAttribute("miaoshaErr",CodeMsg.MiaoSha_HasFinished);
//            return "miaoshafail";
//        }
//        //否则执行秒杀
//        //1. 库存数量减1；
//        //2. 下订单
//        //3. 写入秒杀订单表
//        OrderInfo orderInfo = miaoShaService.doMiaosha(mobileUser, goods);
//        model.addAttribute("goods",goods);
//        model.addAttribute("order",orderInfo);
//        model.addAttribute("user",mobileUser);
//        return "orderdetail";
//    }
    private HashMap<Long,Boolean> localOverMap=new HashMap();

    @AccessLimit(maxCount = 5,seconds = 10,needLogin = true)
    @RequestMapping(value = "/{path}/miaosha",method = RequestMethod.POST)
    @ResponseBody
    public Result<Integer> doMiaoSha1( MobileUser mobileUser, @RequestParam("goodsId") long goodsId,
                                       @PathVariable("path") String path){

        if(mobileUser==null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        boolean isPathExist=miaoShaService.checkMiaoshaPath(mobileUser,goodsId,path);
        if(!isPathExist){
            return Result.error(CodeMsg.MiaoSha_URLERROR);
        }
        //内存标记，减少对redis的访问
        if(localOverMap.get(goodsId)){
            return Result.error(CodeMsg.Stock_NotEnough);
        }
        //预减库存
        Long stock = redisService.decr(GoodsKey.getGoodsStock, "" + goodsId);
        if(stock<0){
            //商品卖完后，将map中的值设为true
            localOverMap.put(goodsId,true);
            return Result.error(CodeMsg.Stock_NotEnough);
        }
        MiaoshaOrder order = orderInfoService.getOrderInfoByUserIdAndGoodsId(mobileUser.getId(), goodsId);
        if(order!=null){
            return Result.error(CodeMsg.MiaoSha_HasFinished);
        }
        //用户对象和商品Id 入队
        MiaoShaMessage miaoShaMessage = new MiaoShaMessage();
        miaoShaMessage.setGoodsId(goodsId);
        miaoShaMessage.setMobileUser(mobileUser);
        sender.sendMiaoshaMessage(miaoShaMessage);

        //排队中
        return Result.success(0);


//        //检测库存数量
//        GoodsVo goods = goodsService.getGoodsById(goodsId);
//        if(goods.getStockCount()<=0){
//
//            return Result.error(CodeMsg.Stock_NotEnough);
//        }
//
//        //检查是否已经秒杀到了
//        OrderInfo order = orderInfoService.getOrderInfoByUserIdAndGoodsId(mobileUser.getId(), goodsId);
//        if(order!=null){
////            return Result.error(CodeMsg.MiaoSha_HasFinished);
//            return Result.error(CodeMsg.MiaoSha_HasFinished);
//        }
//        //否则执行秒杀
//        //1. 库存数量减1；
//        //2. 下订单
//        //3. 写入秒杀订单表
//        OrderInfo orderInfo = miaoShaService.doMiaosha(mobileUser, goods);
//        return Result.success(orderInfo);
    }
    @RequestMapping(value = "/result",method = RequestMethod.GET)
    @ResponseBody
    public Result<Long> miaoshaResult(Model model, MobileUser user,
                                      @RequestParam("goodsId") long goodsId){
        model.addAttribute("user",user);
        if(user==null){
            return Result.error(CodeMsg.USER_NOTEXIST);
        }
        long result = miaoShaService.getMiaoshaResult(user.getId(), goodsId);
        return Result.success(result);
    }

    //初始化时将所有商品库存放入redis中
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodsVoList = goodsService.getGoodsVoList();
        if(goodsVoList==null){
            return;
        }
        for (GoodsVo goodsVo : goodsVoList) {
            redisService.set(GoodsKey.getGoodsStock,""+goodsVo.getId(),goodsVo.getStockCount());
            //false代表初始化的时候，每个商品都是没有卖完的
            localOverMap.put(goodsVo.getId(),false);
        }
    }

    @AccessLimit(maxCount = 5,seconds = 5,needLogin = true)
    @RequestMapping(value = "/path",method = RequestMethod.GET)
    @ResponseBody
    public Result<String> getMiaoshaPath(HttpServletRequest request,MobileUser user, @RequestParam("goodsId") long goodsId,
                                         @RequestParam(value = "verifyCode",defaultValue = "0") int verifyCode){
        if(user==null){
            return Result.error(CodeMsg.USER_NOTEXIST);
        }
        boolean isVerSuccess = miaoShaService.checkMiaoshaVerifyCode(user, goodsId,verifyCode);
        if(!isVerSuccess){
            return Result.error(CodeMsg.MiaoSha_VerifyFailed);
        }
        String miaoshaPath = miaoShaService.createMiaoshaPath(user, goodsId);
        return Result.success(miaoshaPath);
    }
    @RequestMapping(value = "/verifyCode",method = RequestMethod.GET)
    @ResponseBody
    public Result<String> getVerifyCode(HttpServletResponse response,MobileUser user, @RequestParam("goodsId") long goodsId){
        if(user==null){
            return Result.error(CodeMsg.USER_NOTEXIST);
        }
        try {
            BufferedImage image=miaoShaService.createVerifyCode(user,goodsId);
            OutputStream out = response.getOutputStream();
            ImageIO.write(image,"JPEG",out);
            out.flush();
            out.close();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error(CodeMsg.MiaoSha_Failed);
        }

    }



    @RequestMapping("/reset")
    @ResponseBody
    public Result<Boolean> reset(){
        List<GoodsVo> goodsVoList = goodsService.getGoodsVoList();
        for (GoodsVo goodsVo : goodsVoList) {
            goodsVo.setGoodsStock(10);
            redisService.set(GoodsKey.getGoodsStock,""+goodsVo.getId(),"10");
            localOverMap.put(goodsVo.getId(),false);

        }
        redisService.delete(OrderKey.getOrderByGoodsIdAndUserId);
        redisService.delete(MiaoshaKey.isgoodsOver);
        goodsService.resetGoods();
        orderInfoService.reset();
        return Result.success(true);
    }
}
