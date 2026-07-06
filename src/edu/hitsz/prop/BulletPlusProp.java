package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.shoot.CircleShootStrategy;
import edu.hitsz.shoot.ShootStrategy;

//超级火力道具，实现抽象道具接口
public class BulletPlusProp extends Prop {

    public void action(HeroAircraft heroAircraft) {
        ShootStrategy defaultShootStrategy = heroAircraft.getDefaultShootStrategy();
        new Thread(
                () -> {
                    heroAircraft.setShootStrategy(
                            new CircleShootStrategy(
                                    10, 30, -1, 0,
                                    20, 30, 10, 200
                            )
                    );
                    System.out.println("🚀超级火力道具生效!");
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        System.out.println("FirePlusSupply interrupted by another prop!");
                    }
                    heroAircraft.setShootStrategy(defaultShootStrategy);
                }
        ).start();
    }

}