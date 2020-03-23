package io.imwj;

import com.google.common.base.Supplier;
import org.junit.Test;

import java.util.*;

/**
 * @author langao_q
 * @create 2020-03-21 15:10
 */
public class Lambda2 {

    public static boolean testHero(Hero h) {
        return h.hp>100 && h.damage<50;
    }

    public boolean testHero2(Hero h) {
        return h.hp>100 && h.damage<50;
    }

    private static void filter(List<Hero> heros, HeroChecker checker) {
        for (Hero hero : heros) {
            if (checker.test(hero))
                System.out.print(hero);
        }
    }

    /**
     * Lambda方法引用
     */
    @Test
    public void fun1() {
        //简化四直接把表达式传递进去
        List<Hero> heroes = new ArrayList<>();
        Random r = new Random();
        for (int i = 0; i < 5; i++) {
            heroes.add(new Hero("hero " + i, r.nextInt(1000), r.nextInt(100)));
        }
        HeroChecker c = new HeroChecker() {
            @Override
            public boolean test(Hero h) {
                return h.hp>100 && h.damage<50;
            }
        };

        System.out.println("使用匿名类过滤");
        filter(heroes, c);
        System.out.println("使用Lambda表达式");
        filter(heroes, h->h.hp>100 && h.damage<50);
        System.out.println("直接引用静态方法");
        filter(heroes, h -> Lambda2.testHero(h) );
        System.out.println("在Lambda表达式中使用静态方法");
        filter(heroes, Lambda2::testHero);
        System.out.println("在Lambda表达式中使用对象方法");
        Lambda2 l2 = new Lambda2();
        filter(heroes, l2::testHero2);
        System.out.println("引用容器中的对象的方法");//matched恰好就是容器中的对象Hero的方法
        filter(heroes, Hero::matched);
    }

    @Test
    public void fun2(){
        Supplier<List> s = new Supplier<List>() {
            public List get() {
                return new ArrayList();
            }
        };
        //匿名类
        List list1 = getList(s);
        //Lambda表达式
        List list2 = getList(()->new ArrayList());
        //引用构造器
        List list3 = getList(ArrayList::new);
    }

    public static List getList(Supplier<List> s){
        return s.get();
    }


    @Test
    public void fun3(){
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
        System.out.println("匿名内部类方式："+heroes);

        Collections.sort(heroes,(h1, h2)->h1.hp>=h2.hp?-1:1);
        System.out.println("Lambda表达式方式："+heroes);

        Collections.sort(heroes,Lambda2::test1);
        System.out.println("Lambda引用静态方法："+heroes);

        Collections.sort(heroes,Hero::compareTo);
        System.out.println("Lambda引用容器中的方法："+heroes);
    }
    public static int test1(Hero h1,Hero h2) {
        if(h1.hp>=h2.hp)
            return 1;  //正数表示h1比h2要大
        else
            return -1;
    }

}
