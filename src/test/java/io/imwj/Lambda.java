package io.imwj;

import org.junit.Test;

import java.util.*;

/**
 * @author langao_q
 * @create 2020-03-20 11:25
 */
public class Lambda {

    @Test
    public void fun1() {
        //匿名内部类的方式
        List<Hero> heroes = new ArrayList<>();
        Random r = new Random();
        for (int i = 0; i < 5; i++) {
            heroes.add(new Hero("hero " + i, r.nextInt(1000), r.nextInt(100)));
        }
        System.out.println("初始化后的集合:" + heroes);

        System.out.println("使用匿名类的方式，筛选出 hp>100 && damange<50的英雄");
        HeroChecker checker = new HeroChecker() {
            @Override
            public boolean test(Hero h) {
                return (h.hp > 100 && h.damage < 50);
            }
        };

        filter(heroes, checker);
    }

    private static void filter(List<Hero> heros, HeroChecker checker) {
        for (Hero hero : heros) {
            if (checker.test(hero))
                System.out.print(hero);
        }
    }

    @Test
    public void fun2() {
        //Lambda表达式方式
        List<Hero> heroes = new ArrayList<>();
        Random r = new Random();
        for (int i = 0; i < 5; i++) {
            heroes.add(new Hero("hero " + i, r.nextInt(1000), r.nextInt(100)));
        }
        System.out.println("初始化后的集合:" + heroes);

        System.out.println("使用Lambda表达式方式，筛选出 hp>100 && damange<50的英雄");
        filter(heroes, h -> h.hp > 100 && h.damage < 50);
    }

    @Test
    public void fun3() {
        //简化一把外面的壳子去掉
        //只保留方法参数和方法体
        //参数和方法体之间加上符号 ->
        List<Hero> heroes = new ArrayList<>();
        Random r = new Random();
        for (int i = 0; i < 5; i++) {
            heroes.add(new Hero("hero " + i, r.nextInt(1000), r.nextInt(100)));
        }
        System.out.println("初始化后的集合:" + heroes);

        System.out.println("使用匿名类的方式，筛选出 hp>100 && damange<50的英雄");
        HeroChecker checker = (Hero h) -> {
            return h.hp > 100 && h.damage < 50;
        };
        filter(heroes, checker);
    }

    @Test
    public void fun4() {
        //简化二把return 和 {}去掉
        List<Hero> heroes = new ArrayList<>();
        Random r = new Random();
        for (int i = 0; i < 5; i++) {
            heroes.add(new Hero("hero " + i, r.nextInt(1000), r.nextInt(100)));
        }
        System.out.println("初始化后的集合:" + heroes);

        System.out.println("使用匿名类的方式，筛选出 hp>100 && damange<50的英雄");
        HeroChecker checker = (Hero h) -> h.hp > 100 && h.damage < 50;
        filter(heroes, checker);
    }


    @Test
    public void fun5() {
        //简化三把参数类型和圆括号去掉(只有一个参数的时候，才可以去掉圆括号)
        List<Hero> heroes = new ArrayList<>();
        Random r = new Random();
        for (int i = 0; i < 5; i++) {
            heroes.add(new Hero("hero " + i, r.nextInt(1000), r.nextInt(100)));
        }
        System.out.println("初始化后的集合:" + heroes);

        System.out.println("使用匿名类的方式，筛选出 hp>100 && damange<50的英雄");
        HeroChecker checker = h -> h.hp > 100 && h.damage < 50;
        filter(heroes, checker);
    }

    @Test
    public void fun6() {
        //简化四直接把表达式传递进去
        List<Hero> heroes = new ArrayList<>();
        Random r = new Random();
        for (int i = 0; i < 5; i++) {
            heroes.add(new Hero("hero " + i, r.nextInt(1000), r.nextInt(100)));
        }
        System.out.println("初始化后的集合:" + heroes);

        System.out.println("使用匿名类的方式，筛选出 hp>100 && damange<50的英雄");
        filter(heroes, h -> h.hp > 100 && h.damage < 50);
    }

    @Test
    public void fun7() {
        List<Hero> heroes = new ArrayList<>();
        Random r = new Random();
        for (int i = 0; i < 5; i++) {
            heroes.add(new Hero("hero " + i, r.nextInt(1000), r.nextInt(100)));
        }
        System.out.println("初始化后的集合:" + heroes);
        Comparator<Hero> c = new Comparator<Hero>() {
            @Override
            public int compare(Hero h1, Hero h2) {
                return h1.damage - h2.damage;
            }
        };
        Collections.sort(heroes,c);
        System.out.println("按照damange排序后的集合：");
        System.out.println(heroes);
    }

    @Test
    public void fun8() {
        List<Hero> heroes = new ArrayList<>();
        Random r = new Random();
        for (int i = 0; i < 5; i++) {
            heroes.add(new Hero("hero " + i, r.nextInt(1000), r.nextInt(100)));
        }
        System.out.println("初始化后的集合:" + heroes);
        Comparator<Hero> c = new Comparator<Hero>() {
            @Override
            public int compare(Hero h1, Hero h2) {
                //按照hp进行排序
                if(h1.hp>=h2.hp)
                    return 1;  //正数表示h1比h2要大
                else
                    return -1;
            }
        };
        Collections.sort(heroes,c);
        System.out.println("按照damange排序后的集合：");
        System.out.println(heroes);
    }

    @Test
    public void fun9() {
        List<Hero> heroes = new ArrayList<>();
        Random r = new Random();
        for (int i = 0; i < 5; i++) {
            heroes.add(new Hero("hero " + i, r.nextInt(1000), r.nextInt(100)));
        }
        System.out.println("初始化后的集合:" + heroes);
        Collections.sort(heroes,(h1,h2) -> h1.hp>h2.hp?-1:1);
        System.out.println("按照damange排序后的集合：");
        System.out.println(heroes);
    }



}
