package edu.hitsz.factory;

import edu.hitsz.aircraft.AbstractEnemyAircraft;
import edu.hitsz.aircraft.ElitePlusEnemy;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.basic.EnemyConfig;
import edu.hitsz.shoot.DirectShootStrategy;

import java.util.Map;

//创建实现工厂接口的具体工厂类，充当具体创建者角色
public class ElitePlusEnemyFactory implements EnemyAircraftFactory {
    @Override
    public AbstractEnemyAircraft createEnemyAircraft() {
        // 工厂通过set方法组装配置
        EnemyConfig config = new EnemyConfig();

        // 位置
        config.setLocationX((int) (Math.random() * (Main.WINDOW_WIDTH
                - ImageManager.ELITE_PLUS_ENEMY_IMAGE.getWidth())));
        config.setLocationY((int) (Math.random() * Main.WINDOW_HEIGHT * 0.05));

        // 运动属性
        config.setSpeedX(Math.random() < 0.5 ? -4 : 4);
        config.setSpeedY(6);
        config.setHp(120);

        // 射击策略
        config.setShootStrategy(new DirectShootStrategy(
                2, 5, 1, 35, 8, 22));

        // 击杀奖励
        config.setScore(30);

        // 道具掉落
        config.setPropNum(1);
        config.setPropDropRate(0.6);
        config.setPropWeights(Map.of(
                "BloodProp", 40,
                "BulletProp", 25,
                "BulletPlusProp", 20,
                "BombProp", 15
        ));

        return new ElitePlusEnemy(config);
    }
}
