package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.shoot.SectorShootStrategy;
import edu.hitsz.shoot.ShootStrategy;

//火力道具，实现抽象道具接口
public class BulletProp extends Prop {

    public void action(HeroAircraft heroAircraft) {
        ShootStrategy defaultShootStrategy = heroAircraft.getDefaultShootStrategy();
        new Thread(
                () -> {
                    heroAircraft.setShootStrategy(
                            new SectorShootStrategy(
                                    5, 30, -1, 15, 20, 2
                            )
                    );
                    System.out.println("🔥火力道具生效!");
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        System.out.println("FireSupply interrupted by another prop!");
                    }
                    heroAircraft.setShootStrategy(defaultShootStrategy);
                }
        ).start();
    }

}