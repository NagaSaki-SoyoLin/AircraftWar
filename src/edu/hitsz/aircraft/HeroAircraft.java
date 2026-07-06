package edu.hitsz.aircraft;

import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.HeroBullet;
import edu.hitsz.shoot.DirectShootStrategy;
import edu.hitsz.shoot.ShootStrategy;

/**
 * 英雄飞机，游戏玩家操控
 *
 * @author hitsz
 */
public class HeroAircraft extends AbstractAircraft {

    //单例模式
    private volatile static HeroAircraft heroAircraft;

    // 默认的射击策略
    private static final ShootStrategy defaultShootStrategy = new DirectShootStrategy(
            2, 30, -1, 5, 20, 24
    );

    private HeroAircraft(
            int locationX, int locationY, int speedX, int speedY, int hp, ShootStrategy shootStrategy
    ) {
        super(locationX, locationY, speedX, speedY, hp, shootStrategy);
    }

    public static HeroAircraft getHeroAircraft() {
        if (heroAircraft == null) {
            synchronized (HeroAircraft.class) {
                if (heroAircraft == null) {
                    heroAircraft = new HeroAircraft(Main.WINDOW_WIDTH / 2,
                            Main.WINDOW_HEIGHT - ImageManager.HERO_IMAGE.getHeight(),
                            0, 0, 100, defaultShootStrategy
                    );
                }
            }
        }
        return heroAircraft;
    }

    @Override
    public void forward() {
        // 英雄机由鼠标控制，不通过forward函数移动
    }

    @Override
    public BaseBullet createBullet(int locationX, int locationY, int speedX, int speedY, int power) {
        return new HeroBullet(locationX, locationY, speedX, speedY, power);
    }

    // 获取默认的射击策略
    public ShootStrategy getDefaultShootStrategy() {
        return defaultShootStrategy;
    }

}
