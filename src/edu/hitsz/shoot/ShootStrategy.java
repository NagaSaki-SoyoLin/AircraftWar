package edu.hitsz.shoot;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.bullet.BaseBullet;

import java.util.List;

//策略模式：创建一个射击策略接口(抽象类)，充当抽象策略角色
public abstract class ShootStrategy {

    //每次射击发射子弹数量
    protected int shootNum;

    //子弹威力
    protected int power;

    //子弹射击方向 (向上发射：-1，向下发射：1)
    protected int direction;

    //控制子弹起始点相对飞机的纵向偏移
    protected int YOffset;

    //控制子弹速度的基础值
    protected int speedFactor;

    public ShootStrategy(int shootNum, int power, int direction, int YOffset, int speedFactor) {
        this.shootNum = shootNum;
        this.power = power;
        this.direction = direction;
        this.YOffset = YOffset;
        this.speedFactor = speedFactor;
    }

    public abstract List<BaseBullet> shoot(AbstractAircraft aircraft);

}
