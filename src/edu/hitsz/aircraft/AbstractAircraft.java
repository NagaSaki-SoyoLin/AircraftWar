package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.shoot.ShootStrategy;

import java.util.List;

/**
 * 所有种类飞机的抽象父类
 *
 * @author hitsz
 */
public abstract class AbstractAircraft extends AbstractFlyingObject {

    //最大生命值
    protected int maxHp;

    //当前生命值
    protected int hp;

    //策略模式：抽象飞机充当上下文角色
    private ShootStrategy shootStrategy;

    //策略模式：构造函数传入射击策略
    public AbstractAircraft(
            int locationX, int locationY, int speedX, int speedY, int hp, ShootStrategy shootStrategy
    ) {
        super(locationX, locationY, speedX, speedY);
        this.hp = hp;
        this.maxHp = hp;
        this.shootStrategy = shootStrategy;
    }

    public void decreaseHp(int decrease) {
        hp -= decrease;
        if (hp <= 0) {
            hp = 0;
            vanish();
        }
    }

    public int getHp() {
        return hp;
    }

    // 新增获取最大生命值方法
    public int getMaxHp() {
        return maxHp;
    }

    // 设置当前血量，同时更新最大血量
    public void setHp(int hp) {
        this.hp = hp;
        this.maxHp = Math.max(this.maxHp, hp);
    }

    /**
     * 飞机射击方法
     *
     * @return 可射击对象需实现，返回子弹列表
     * 非可射击对象空实现，返回空列表
     */

    //创建子弹
    public abstract BaseBullet createBullet(int locationX, int locationY, int speedX, int speedY, int power);

    //策略模式：设置射击策略
    public void setShootStrategy(ShootStrategy shootStrategy) {
        this.shootStrategy = shootStrategy;
    }

    //策略模式：执行射击策略
    public List<BaseBullet> executeShootStrategy() {
        return shootStrategy.shoot(this);
    }

}


