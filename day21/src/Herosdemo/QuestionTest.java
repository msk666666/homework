package Herosdemo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class QuestionTest {
    public static void main(String[] args) throws IOException {
        Stream<String> lines = Files.lines(Paths.get("heroes.txt"));
        List<Heros> heros = lines.map(line -> line.split("\t")).map((split) -> {
            return new Heros(Integer.parseInt(split[0]),
                    split[1],
                    split[2],
                    split[3],
                    Integer.parseInt(split[4]),
                    Integer.parseInt(split[5]),
                    Integer.parseInt(split[6]));
        }).collect(Collectors.toList());


////        按出生地分组
        groupCity(heros);
        System.out.println("--------");
        //武力排行前三
        sortWuli(heros);
        System.out.println("-------");
        //寿命排行前三
        sortife(heros);
        //女性寿命最长
        System.out.println("---------");
        femaleLongLife(heros);
        //寿命分组
        System.out.println("-----------");
        groupAge(heros);
        //武力值分组
        System.out.println("-------------");
        groupWuli(heros);
        System.out.println("-------------");
        //统计city
        tongjiCity(heros);




    }

    private static void tongjiCity(List<Heros> heros) {
        Map<String, List<Heros>> map = heros.stream().collect(Collectors.groupingBy(hero -> hero.getCity()));
        HashMap<String, Integer> tongjiMap = new HashMap<>();
        map.forEach((key,value)->{
            tongjiMap.put(key,value.size());
        });
        System.out.println(tongjiMap);
    }

    private static void groupWuli(List<Heros> heros) {
        System.out.println(heros.stream().collect(Collectors.groupingBy(h->rangeWuli(h.getWuli()))));
//        System.out.println("小于70"+heros.stream().filter(h->h.getWuli()<70).collect(Collectors.toList()));
//        System.out.println("70~79"+heros.stream().filter(h->70<=h.getWuli()&&h.getWuli()<=79).collect(Collectors.toList()));
//        System.out.println("80~89"+heros.stream().filter(h->80<=h.getWuli()&&h.getWuli()<=89).collect(Collectors.toList()));
//        System.out.println("大于90"+heros.stream().filter(h->h.getWuli()>=90).collect(Collectors.toList()));
    }

    private static void groupAge(List<Heros> heros) {
        System.out.println(heros.stream().collect(Collectors.groupingBy(h->rangeAge((h.getDie()-h.getBirth())))));
//        System.out.println("小于20"+heros.stream().filter(h->0<=h.getDie()-h.getBirth()&&h.getDie()-h.getBirth()<=20).collect(Collectors.toList()));
//        System.out.println("20~40"+heros.stream().filter(h->21<=h.getDie()-h.getBirth()&&h.getDie()-h.getBirth()<=40).collect(Collectors.toList()));
//        System.out.println("40~60"+heros.stream().filter(h->41<=h.getDie()-h.getBirth()&&h.getDie()-h.getBirth()<=60).collect(Collectors.toList()));
//        System.out.println("大于60"+heros.stream().filter(h->h.getDie()-h.getBirth()>=60).collect(Collectors.toList()));
    }

    private static void femaleLongLife(List<Heros> heros) {
        Map<String, List<Heros>> map = heros.stream().collect(Collectors.groupingBy(h ->h.getSex()));
        List<Heros> female = map.get("女");
        Optional<Heros> max = female.stream().max((h1, h2) -> ((h1.getDie() - h1.getBirth()) - (h2.getDie() - h2.getBirth())));
        System.out.println(max);
    }

    private static void sortife(List<Heros> heros) {
        List<Heros> collect = heros.stream().sorted((h1, h2) -> -((h1.getDie() - h1.getBirth()) - (h2.getDie() - h2.getBirth()))).limit(3).collect(Collectors.toList());
        System.out.println(collect);
    }

    private static void sortWuli(List<Heros> heros) {
        List<Heros> collect = heros.stream().sorted((h1, h2) -> -(h1.getWuli() - h2.getWuli())).limit(4).collect(Collectors.toList());
        System.out.println(collect);
    }

    private static void groupCity(List<Heros> heros) {
        System.out.println(heros.stream().collect(Collectors.groupingBy(hero->hero.getCity())));
    }
    private static String rangeAge(int age){
        if(age>=0&&age<=20){
            return "0~20";
        }else if(age>=21&&age<=40){
            return "21~40";
        }else if(age>=41&&age<=60){
            return "41~60";
        }else if(age>=60){
            return "大于60";
        }
        return null;
    }
    private static String rangeWuli(int Wuli){
        if(Wuli<70){
            return "小于七十";
        }else if(Wuli>=70&&Wuli<=79){
            return "70~79";
        }else if(Wuli>=80&&Wuli<=89){
            return "80~89";
        }else if(Wuli>=90){
            return "大于90";
        }
        return null;
    }
}
