# Lambda表达式
> 与匿名类概念相比较，Lambda其实就是匿名方法，这是一种把方法作为参数进行传递的编程思想。  
   注意：必须要是要把lambda作为参数传递给方法，是不能分开或独立使用的

## 匿名内部类
匿名内部类也就是没有名字的内部类，匿名内部类只能使用一次，它通常用来简化代码编写  
但使用匿名内部类还有个前提条件：必须继承一个父类或实现一个接口
* 1.准备一个实体类
```
public class Hero implements Comparable<Hero>{
    public String name;
    public float hp;
    public int damage;

    //初始化name,hp,damage的构造方法
    public Hero(String name,float hp, int damage) {
        this.name =name;
        this.hp = hp;
        this.damage = damage;
    }

    @Override
    public int compareTo(Hero anotherHero) {
        if(hp<anotherHero.hp)
            return 1;
        else
            return -1;
    }

    @Override
    public String toString() {
        return "Hero [name=" + name + ", hp=" + hp + ", damage=" + damage + "]\r\n";
    }

    public boolean matched(){
        return this.hp>100 && this.damage<50;
    }
}
```

* 2.准备一个接口
```
public interface HeroChecker {
    //一个未实现的方法
    public boolean test(Hero h);
}
```

* 3.书写匿名内部类
```
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
```

## Lambda表达式
使用lambda表达式来替代匿名内部类：->左边的是参数（一个参数时可以去掉括号），->右边的是方法体
```
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
```
## 简化过程
* 1.在匿名内部类中把外面的壳子去掉,只保留方法参数和方法体,参数和方法体之间加上符号 ->
```
    @Test
    public void fun3() {
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
```

* 2.把return 和 {}去掉
```
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
```

* 3.把参数类型和圆括号去掉(只有一个参数的时候，才可以去掉圆括号)，然后直接把表达式传递进去
```
    @Test
    public void fun6() {
        List<Hero> heroes = new ArrayList<>();
        Random r = new Random();
        for (int i = 0; i < 5; i++) {
            heroes.add(new Hero("hero " + i, r.nextInt(1000), r.nextInt(100)));
        }
        System.out.println("初始化后的集合:" + heroes);

        System.out.println("使用匿名类的方式，筛选出 hp>100 && damange<50的英雄");
        filter(heroes, h -> h.hp > 100 && h.damage < 50);
    }
```

## 一个例子
```
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
```

## Lambda的缺点
Lambda表达式虽然带来了代码的简洁，但是也有其局限性。
1. 可读性差，Lambda表达式一旦变得比较长，就难以理解
2. 不便于调试，很难在Lambda表达式中增加日志等调试信息
3. 版本支持，Lambda表达式在JDK8版本中才开始支持
Lambda比较适合用在简短的业务代码中，并不适合用在复杂的系统中，会加大维护成本。

# Lambda方法引用
1 : 引用静态方法   
2 : 引用对象方法   
3 : 引用容器中的对象的方法   
4 : 引用构造器   
```
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
     * Lambda方法引用：指定条件过滤数据
     */
    @Test
    public void fun1() {
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
        //public boolean matched(){
        //          return this.hp>100 && this.damage<50;
        // }
        filter(heroes, Hero::matched);
    }

    //引用构造器
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

}
```

## 一个例子
```
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
```

# Lambda聚合操作
## Lambda聚合方式遍历集合
```
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
        //Lambda聚合方式遍历，array是有strea方法得到管道源
        heroes.stream()
                .filter(h -> h.hp>100)
                .forEach(h -> System.out.println(h));
    }
```

## Stream和管道的概念
Stream和Collection结构化的数据不一样，Stream是一系列的元素，就像是生产线上的罐头一样，一串串的出来。  
管道指的是一系列的聚合操作。

> 管道又分3个部分：
>    管道源：在这个例子里，源是一个List  
>    中间操作： 每个中间操作，又会返回一个Stream，比如.filter()又返回一个Stream, 中间操作是“懒”操作，并不会真正进行遍历。  
>    结束操作：当这个操作执行后，流就被使用“光”了，无法再被操作。所以这必定是流的最后一个操作。 结束操作不会返回Stream，但是会返回int、float、String、 Collection或者像forEach，什么都不返回。  
>    注意：结束操作才进行真正的遍历行为，在遍历的时候，才会去进行中间操作的相关判断  


## 管道源
* 1.把Collection切换成管道源很简单，调用stream()就行了。
```
heros.stream()
```
* 2.但是数组却没有stream()方法，需要使用
```
Arrays.stream(hs)
或者
Stream.of(hs)
```
* 3.例子
```
        //数组却没有stream()方法，但可以通过Arrays.stream(hs)、Stream.of(hs)
        System.out.println("管道源：");
        Hero hs[] = heroes.toArray(new Hero[heroes.size()]);
        Arrays.stream(hs)
                .filter(h -> h.hp>200)
                .forEach(h -> System.out.println(h));
        Stream.of(hs)
                .filter(h -> h.hp>500)
                .forEach(h -> System.out.println(h));
```

## 中间操作
> 每个中间操作，又会返回一个Stream，比如.filter()又返回一个Stream, 中间操作是“懒”操作，并不会真正进行遍历。  
* 1.对元素进行筛选：  
    filter 匹配  
    distinct 去除重复(根据equals判断)  
    sorted 自然排序  
    sorted(Comparator<T>) 指定排序  
    limit 保留  
    skip 忽略  
* 2.转换为其他形式的流  
    mapToDouble 转换为double的流  
    map 转换为任意类型的流  

```
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
    }
```

## 结束操作
> 当进行结束操作后，流就被使用“光”了，无法再被操作。所以这必定是流的最后一个操作。  
> 结束操作不会返回Stream，但是会返回int、float、String、 Collection或者像forEach，什么都不返回。  
> 结束操作才真正进行遍历行为，前面的中间操作也在这个时候，才真正的执行。 
* 1.常见操作
    forEach() 遍历每个元素  
    toArray() 转换为数组  
    min(Comparator<T>) 取最小的元素  
    max(Comparator<T>) 取最大的元素  
    count() 总数  
    findFirst() 第一个元素  
```

    @Test
    public void fun2(){
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
```
     