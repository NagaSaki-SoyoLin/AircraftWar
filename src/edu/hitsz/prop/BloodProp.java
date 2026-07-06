package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;

import static java.lang.Math.min;

//加血道具，实现抽象道具接口
public class BloodProp extends Prop {

    @Override
    //加血道具可使英雄机恢复一定血量，但不超过初始最大血量
    public void action(HeroAircraft heroAircraft) {
        //加血道具治疗量
        int bloodAddition = 25;
        int add = min(
                bloodAddition,
                heroAircraft.getMaxHp() - heroAircraft.getHp()
        );
        heroAircraft.decreaseHp(-add); //减负值即加正值
        System.out.println("🩷治疗道具生效!");
    }

}
