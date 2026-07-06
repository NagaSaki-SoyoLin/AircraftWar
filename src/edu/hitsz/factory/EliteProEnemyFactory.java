package edu.hitsz.factory;

import edu.hitsz.aircraft.AbstractEnemyAircraft;
import edu.hitsz.aircraft.EliteProEnemy;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.basic.EnemyConfig;
import edu.hitsz.shoot.SectorShootStrategy;

import java.util.Map;

//创建实现工厂接口的具体工厂类，充当具体创建者角色
public class EliteProEnemyFactory implements EnemyAircraftFactory {
    @Override
    public AbstractEnemyAircraft createEnemyAircraft() {
        // 工厂通过set方法组装配置
        EnemyConfig config = new EnemyConfig();

        // 位置
        config.setLocationX((int) (Math.random() * (Main.WINDOW_WIDTH
                - ImageManager.ELITE_PRO_ENEMY_IMAGE.getWidth())));
        config.setLocationY((int) (Math.random() * Main.WINDOW_HEIGHT * 0.05));

        // 运动属性
        config.setSpeedX(Math.random() < 0.5 ? -2 : 2);
        config.setSpeedY(4);
        config.setHp(180);

        // 射击策略
        config.setShootStrategy(new SectorShootStrategy(
                3, 5, 1, 32, 7, 3));

        // 击杀奖励
        config.setScore(50);

        // 道具掉落
        config.setPropNum(1);
        config.setPropDropRate(0.8);
        config.setPropWeights(Map.of(
                "BloodProp", 20,
                "BulletProp", 30,
                "BulletPlusProp", 10,
                "BombProp", 10,
                "FreezeProp", 30
        ));

        return new EliteProEnemy(config);
    }
}
