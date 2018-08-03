package com.qlk.jdk8;

import lombok.Data;
import sun.rmi.runtime.Log;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FutureTest {
    private static final Executor executor = Executors.newFixedThreadPool(1);

    public static void sleep1S(){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static void sleepRandom(){
        try {
            Thread.sleep(500+new Random().nextInt(2000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

   //获取价格
    public static Float getPrice(Integer goodsCode){
          sleep1S();
//          sleepRandom();
          return Float.valueOf(goodsCode);
    }
    //获取折扣
    public  static Float getDiscount(Integer goodsCode){
        sleep1S();
        return  (50+new Random().nextInt(50))/100F;
    }
    //获取优惠券
    public  static String getCoupon(Float price){
        sleep1S();
//        sleepRandom();
        return  "Coupon code:"+price;
    }
    public static void futureTest(List<Goods> goodsList){
        long start= System.currentTimeMillis();
        List<CompletableFuture<Goods>> futures = goodsList.stream().
                map((goods) -> CompletableFuture.supplyAsync(() -> getDiscount(goods.getGoodsCode())
                , executor).thenCombine(CompletableFuture.supplyAsync(() -> getPrice(goods.getGoodsCode()), executor), (dis, price) -> {
            goods.setDiscount(dis);
            goods.setPrice(price);
            goods.setRealPrice(price * dis);
            return goods;
        }).thenCompose((goods1) -> CompletableFuture.supplyAsync(() -> {
            goods1.setCouponCode(getCoupon(goods1.getPrice()));
            return goods1;
        }, executor))).collect(Collectors.toList());
         futures.stream().map(CompletableFuture::join).collect(Collectors.toList());
        // goodsList.forEach(System.out::println);
        System.out.println("time:"+(System.currentTimeMillis()-start));
    }
public static void parallelStreamTest(List<Goods> goodsList){
    long start= System.currentTimeMillis();
        goodsList.stream().map(goods -> {
            goods.setPrice(getPrice(goods.getGoodsCode()));
            goods.setDiscount(getDiscount(goods.getGoodsCode()));
            goods.setCouponCode(getCoupon(goods.getPrice()));
            goods.setRealPrice(goods.getPrice()*goods.getDiscount());
            return goods;
        }).forEach(System.out::println);
    System.out.println("time:"+(System.currentTimeMillis()-start));
}

    public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {
        List<Goods> goodsList = Arrays.asList(
                new Goods(1, "药品1"),
                new Goods(2, "药品2"),
                new Goods(3, "药品3"),
                new Goods(4, "药品4")
        );
       // System.out.println(Runtime.getRuntime().availableProcessors());
        //parallelStreamTest(goodsList);
        futureTest(goodsList);
    }



}
@Data
class Goods {

    private Integer goodsCode;

    private String goodsName;

    private Float price;

    private Float realPrice;

    private String couponCode;

    private Float discount;

    public Goods(Integer goodsCode, String goodsName) {
        this.goodsCode = goodsCode;
        this.goodsName = goodsName;
    }
}