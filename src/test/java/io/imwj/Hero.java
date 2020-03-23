package io.imwj;

/**
 * @author langao_q
 * @create 2020-03-20 11:29
 */
public class Hero implements Comparable<Hero>{
    public String name;
    public float hp;
    public int damage;

    public float getHp() {
        return hp;
    }

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
