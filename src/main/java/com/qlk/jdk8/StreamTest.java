package com.qlk.jdk8;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.*;

public class StreamTest {

    public static void main(String[] args) {
        List<String> list= Arrays.asList("1","2","3","44");
        String temp="aa";
        list.sort(Comparator.comparing(String::length));
        Comparator<Integer> comparator=Integer::compareTo;
        Runnable runnable=()->{
        };
        List<Integer> nums=list.stream().map(Integer::valueOf).sorted(comparator.reversed()).filter(i->i>2).collect(toList());

//        Map<String,Long> map=
//        System.out.println(list.stream().map(str->str.split("")).flatMap(Arrays::stream).collect(toList()).toString());
        Map<String ,List<String>> map=list.stream().collect(groupingBy(String::toString));
//        System.out.println(nums);
//                .filter((str)->{

//                    System.out.printf(str);return str!=null;}).count();
        // .collect(Collectors.groupingBy(String::toString,Collectors.counting()));
//        System.out.printf(map.toString());
        String a="bb";
        a="aa";
//        Map<Boolean,Dish> map1=menu.stream().collect(groupingBy(Dish::isVegetarian,collectingAndThen(maxBy(Comparator.comparing(Dish::getCalories)),Optional::get)));
//        Map<Dish.Type,List<Integer>> map2=menu.stream().collect(groupingBy(Dish::getType,mapping(dish->{if(dish.getCalories()<300)return  1;else return 2;},toList())));
//        System.out.println(map2);

        List<Dish> menu = Arrays.asList(
                new Dish("pork", false, 800, Dish.Type.MEAT),
                new Dish("beef", false, 700, Dish.Type.MEAT),
                new Dish("chicken", false, 400, Dish.Type.MEAT),
                new Dish("french fries", true, 530, Dish.Type.OTHER),
                new Dish("rice", true, 350, Dish.Type.OTHER),
                new Dish("season fruit", true, 120, Dish.Type.OTHER),
                new Dish("pizza", true, 550, Dish.Type.OTHER),
                new Dish("prawns", false, 300, Dish.Type.FISH),
                new Dish("salmon", false, 450, Dish.Type.FISH) );

        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("test");
            }
        });
        Thread thread1 =new Thread(()->{System.out.println("test");});

        //filter
        menu.stream().filter(new Predicate<Dish>() {
            @Override
            public boolean test(Dish dish) {
                return dish.isVegetarian();
            }
        });
        List<Dish> list1= menu.stream().filter((dish)->dish.isVegetarian()).collect(toList());

        Map<Dish.Type,List<Dish>> newMap=menu.stream().collect(groupingBy((dish)->dish.getType()));

        //map
        List<String> list2=menu.stream().map(Dish::getName).collect(toList());

        List<String> stringList=new ArrayList<>();
        stringList.add("aa");
        //flatMap
        List list3= Stream.of(menu,menu)
                .flatMap((tempList)->tempList.stream())
                .collect(toList());
        System.out.println(list3);

        //min max
        Dish dish3=menu.stream().min((dish1, dish2)->dish1.getCalories().compareTo(dish2.getCalories())).get();

        //数值流
        int sumInt= IntStream.range(1,100).sum();
        int sumInt1=menu.stream().mapToInt((dish)->dish.getCalories()).sum();
        int sumInt2=menu.stream().mapToInt((dish)->dish.getCalories()).boxed().collect(summingInt(Integer::intValue));


        //reduce
        Integer calories=menu.stream()
                .map((ele)->ele.getCalories())
                .reduce((sum,ele)->sum+ele).get();

        //方法引用
        Integer calories1=menu.stream()
                .map(Dish::getCalories)
                .reduce((sum,ele)->sum+ele).get();
        //方法引用例子
//        (Apple a) -> a.getWeight()  Apple::getWeight
//        () -> Thread.currentThread().dumpStack()  Thread.currentThread()::dumpStack
//        (str, i) -> str.substring(i)  String::substring
//        Comparator<Integer> comparator1=(a,b)->a.compareTo(b);        Comparator<Integer> comparator1=Integer::compareTo;
//        (String s) -> System.out.println(s)  System.out::println


        //收集器collect
        long count=menu.stream().collect(counting());
        double avgCalories =menu.stream().collect(averagingInt(Dish::getCalories));
        int sumCalories=menu.stream().collect(summingInt(Dish::getCalories));
        int maxCalories=menu.stream().collect(maxBy(comparingInt(Dish::getCalories))).get().getCalories();
        IntSummaryStatistics menuStatistics =menu.stream().collect(summarizingInt(Dish::getCalories));

        //收集器的字符串操作
        String shortMenu = menu.stream().map(Dish::getName).collect(joining(", "));
        String shortMenu1 = menu.stream().map(Dish::getName).collect(joining(",","[","]"));

        Map<String,Integer> map2=menu.stream().collect(toMap((dish1)->dish1.getName(),(dish2)->dish2.getCalories()));
        System.out.println(map2);

        //分组grouppingBy
        Map<Dish.Type,List<Dish>> map1=menu.stream().collect(groupingBy(Dish::getType));
        Map<Dish.Type, Long> typesCount = menu.stream().collect(groupingBy(Dish::getType, counting()));
        Map<Dish.Type, Optional<Dish>> mostCaloricByType =menu.stream().collect(groupingBy(Dish::getType,maxBy(comparingInt(Dish::getCalories))));
        Map<Dish.Type, Map<Integer, List<Dish>>> dishesByTypeCaloricLevel =
                menu.stream().collect(
                        groupingBy(Dish::getType, groupingBy(dish -> {
                                    if (dish.getCalories() <= 400)
                                        return 1;
                                    else if (dish.getCalories() <= 700)
                                        return 2;
                                    else return 3;
                                } )
                        )
                );

        //mapping
        Map<Dish.Type, List<Integer>> caloricLevelsByType =
                menu.stream().collect(
                        groupingBy(Dish::getType, mapping(
                                dish -> { if (dish.getCalories() <= 400) return 1;
                                else if (dish.getCalories() <= 700) return 2;
                                else return 3; },
                                toList() )));


        //Optional

        Dish dish = null;
        int calorieafter=0;
        calorieafter=Optional.ofNullable(dish).map(Dish::getCalories)
                .map((tmp)->tmp+1)
                .orElse(1000);

    }


}
class Dish {
    private final String name;
    private final boolean vegetarian;
    private final Integer calories;
    private final Type type;
    public Dish(String name, boolean vegetarian, Integer calories, Type type) {
        this.name = name;
        this.vegetarian = vegetarian;
        this.calories = calories;
        this.type = type;
    }
    public String getName() {
        return name;
    }
    public boolean isVegetarian() {
        return vegetarian;
    }
    public Integer getCalories() {
        return calories;
    }
    public Type getType() {
        return type;
    }
    @Override
    public String toString() {
        return name;
    }
    public enum Type { MEAT, FISH, OTHER }
}