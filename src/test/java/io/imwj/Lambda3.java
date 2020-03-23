package io.imwj;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

/**
 * @author langao_q
 * @create 2020-03-21 16:16
 */
public class Lambda3 {

    @Test
    public void fun1(){
        List<Hero> heroes = new ArrayList<>();
        Random r = new Random();
        for (int i = 0; i < 5; i++) {
            heroes.add(new Hero("hero " + i, r.nextInt(1000), r.nextInt(100)));
        }

        //普通方式遍历
        for(Hero hero : heroes){
            if(hero.hp > 100)
                System.out.println(hero);
        }

        System.out.println("开始遍历：");
        //Lambda聚合方式遍历，array是有strea方法得到管道源
        heroes.stream()
                .filter(h -> h.hp>100)
                .forEach(h -> System.out.println(h));

        //数组却没有stream()方法，但可以通过Arrays.stream(hs)、Stream.of(hs)
        System.out.println("管道源：");
        Hero hs[] = heroes.toArray(new Hero[heroes.size()]);
        Arrays.stream(hs)
                .filter(h -> h.hp>200)
                .forEach(h -> System.out.println(h));
        Stream.of(hs)
                .filter(h -> h.hp>500)
                .forEach(h -> System.out.println(h));
    }

    @Test
    public void fun2(){
        List<Hero> heroes = new ArrayList<>();
        Random r = new Random();
        for (int i = 0; i < 5; i++) {
            heroes.add(new Hero("hero " + i, r.nextInt(1000), r.nextInt(100)));
        }
        heroes.add(heroes.get(0));

        //遍历
        System.out.println("遍历数据：");
        heroes.stream()
                .forEach(h -> System.out.println(h));


        System.out.println("取hp第三高的数据：");
        Hero hero = heroes.stream()
                .sorted((h1, h2) -> h1.hp > h2.hp ? -1 : 1)
                .skip(2)
                .findFirst().get();
        System.out.println(hero);

        System.out.println("去除复数据：");
        heroes.stream()
                .distinct()
                .forEach(h -> System.out.println(h));

        System.out.println("指定方式排序：");
        heroes.stream()
                .sorted((h1, h2) -> h1.hp>h2.hp?1:-1)
                .forEach(h -> System.out.println(h));

        System.out.println("取前三条数据：");
        heroes.stream()
                .limit(3)
                .forEach(h -> System.out.println(h));

        System.out.println("去除前面2条数据：");
        heroes.stream()
                .skip(2)
                .forEach(h -> System.out.println(h));

        System.out.println("转换成doubble的stream流：");
        heroes.stream()
                .mapToDouble(Hero :: getHp)
                .forEach(h -> System.out.println(h));

        System.out.println("转换成任意类型的strea流：");
        heroes.stream()
                .map(h -> h.name + "-" + h.hp )
                .forEach(h -> System.out.println(h));

        /*forEach() 遍历每个元素
        toArray() 转换为数组
        min(Comparator<T>) 取最小的元素
        max(Comparator<T>) 取最大的元素
        count() 总数
        findFirst() 第一个元素*/
        System.out.println("转换为数组：");
        heroes.stream().toArray();

        System.out.println("取最小的元素：");
        Hero hero1 = heroes.stream()
                .min((h1, h2) -> h1.damage - h2.damage).get();
        System.out.println(hero1);

        System.out.println("取最大的元素：");
        Hero hero2 = heroes.stream()
                .max((h1, h2) -> h1.damage - h2.damage).get();
        System.out.println(hero2);

        System.out.println("总数：");
        long count = heroes.stream()
                .count();
        System.out.println(count);

        System.out.println("第一个元素：");
        Hero hero3 = heroes.stream()
                .findFirst().get();
        System.out.println(hero3);

    }
}
