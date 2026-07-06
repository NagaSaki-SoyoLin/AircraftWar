package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;

import edu.hitsz.application.Main;
import edu.hitsz.basic.AbstractFlyingObject;

import java.util.Random;

//道具采用简单工厂模式，接口(抽象类)充当产品角色，同时继承抽象飞行物
public abstract class Prop extends AbstractFlyingObject {

    // 道具下落速度随机
    public Prop() {
        this.speedY = new Random().nextInt(5) + 5;
    }

    //边界检查
    @Override
    public void forward() {
        super.forward();
        // 判定 y 轴向下飞行出界
        if (locationY >= Main.WINDOW_HEIGHT) {
            vanish();
        }
    }

    //方法：道具效果
    public abstract void action(HeroAircraft heroAircraft);

}
