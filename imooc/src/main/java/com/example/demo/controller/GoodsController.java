package com.example.demo.controller;

import com.example.demo.domain.Goods;
import com.example.demo.domain.MobileUser;
import com.example.demo.redis.GoodsKey;
import com.example.demo.redis.RedisService;
import com.example.demo.result.Result;
import com.example.demo.service.GoodsService;
import com.example.demo.vo.GoodsDetailVo;
import com.example.demo.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.util.StringUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    private RedisService redisService;

    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;

    @Autowired
    private GoodsService goodsService;



    @RequestMapping(value = "/to_list",produces = "text/html")
    @ResponseBody
    public String list(HttpServletRequest request,HttpServletResponse response,Model model, MobileUser mobileUser){
        model.addAttribute("user",mobileUser);
        //取缓存
        String html = redisService.get(GoodsKey.getGoodsList, "", String.class);
        if(!StringUtils.isEmpty(html)){
            return html;
        }
        List<GoodsVo> goodsVoList = goodsService.getGoodsVoList();
        model.addAttribute("goodsList",goodsVoList);
        IWebContext ctx = new WebContext(request,response,
                request.getServletContext(),request.getLocale(),model.asMap());

        //手动渲染
        html = thymeleafViewResolver.getTemplateEngine().process("goods_list", ctx);
        if(!StringUtils.isEmpty(html)){
            redisService.set(GoodsKey.getGoodsList,"",html);
        }
        return html;
    }
    @RequestMapping(value = "/to_detail/{goodsId}",produces = "text/html")
    @ResponseBody
    public String goodsDetail(HttpServletResponse response,HttpServletRequest request,
                              Model model, MobileUser mobileUser,@PathVariable("goodsId") long goodsId){

        String html = redisService.get(GoodsKey.getGoodsDetail, "" + goodsId, String.class);
        if(!StringUtils.isEmpty(html)){
            return html;
        }

        model.addAttribute("user",mobileUser);
        GoodsVo goodsVo = goodsService.getGoodsById(goodsId);
        model.addAttribute("goods",goodsVo);
        //记录秒杀开始时间、结束时间、和当前时间
        long startAt = goodsVo.getStartDate().getTime();
        long endAt = goodsVo.getEndDate().getTime();
        long now = System.currentTimeMillis();

        //秒杀的状态
        int miaoshaStatus=0;
        //还有多久开始秒杀
        int remainSeconds=0;

        //秒杀还没开始
        if(now<startAt){
            miaoshaStatus=0;
            remainSeconds=(int)(startAt-now)/1000;

        //秒杀已经结束
        }else if(now>endAt){
            miaoshaStatus=2;
            remainSeconds=-1;
        }else {//秒杀进行中
            miaoshaStatus=1;
            remainSeconds=0;
        }
        model.addAttribute("miaoshaStatus",miaoshaStatus);
        model.addAttribute("remainSeconds",remainSeconds);

        IWebContext ctx=new WebContext(request,response,
                request.getServletContext(),request.getLocale(),model.asMap());

        html = thymeleafViewResolver.getTemplateEngine().process("goods_detail", ctx);
        if(!StringUtils.isEmpty(html)){
            redisService.set(GoodsKey.getGoodsDetail,""+goodsId,html);
        }
        return html;
    }
    @RequestMapping(value = "/detail/{goodsId}")
    @ResponseBody
    public Result<GoodsDetailVo> goodsDetail1(HttpServletResponse response, HttpServletRequest request,
                               MobileUser mobileUser, @PathVariable("goodsId") long goodsId){
        GoodsVo goodsVo = goodsService.getGoodsById(goodsId);
        //记录秒杀开始时间、结束时间、和当前时间
        long startAt = goodsVo.getStartDate().getTime();
        long endAt = goodsVo.getEndDate().getTime();
        long now = System.currentTimeMillis();

        //秒杀的状态
        int miaoshaStatus=0;
        //还有多久开始秒杀
        int remainSeconds=0;

        //秒杀还没开始
        if(now<startAt){
            miaoshaStatus=0;
            remainSeconds=(int)(startAt-now)/1000;

            //秒杀已经结束
        }else if(now>endAt){
            miaoshaStatus=2;
            remainSeconds=-1;
        }else {//秒杀进行中
            miaoshaStatus=1;
            remainSeconds=0;
        }
        GoodsDetailVo goodsDetailVo = new GoodsDetailVo();
        goodsDetailVo.setGoodsVo(goodsVo);
        goodsDetailVo.setMiaoshaStatus(miaoshaStatus);
        goodsDetailVo.setRemainSeconds(remainSeconds);
        goodsDetailVo.setMobileUser(mobileUser);
        return Result.success(goodsDetailVo);
    }

}
