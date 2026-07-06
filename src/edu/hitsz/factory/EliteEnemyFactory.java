package edu.hitsz.factory;

import edu.hitsz.aircraft.EliteEnemy;
import edu.hitsz.aircraft.AbstractEnemyAircraft;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.basic.EnemyConfig;
import edu.hitsz.shoot.DirectShootStrategy;

import java.util.Map;

//创建实现工厂接口的具体工厂类，充当具体创建者角色
public class EliteEnemyFactory implements EnemyAircraftFactory {
    @Override
    public AbstractEnemyAircraft createEnemyAircraft() {
        // 工厂通过set方法组装配置
        EnemyConfig config = new EnemyConfig();

        // 位置
        config.setLocationX((int) (Math.random() * (Main.WINDOW_WIDTH
                - ImageManager.ELITE_ENEMY_IMAGE.getWidth())));
        config.setLocationY((int) (Math.random() * Main.WINDOW_HEIGHT * 0.05));

        // 运动属性
        config.setSpeedX(0);
        config.setSpeedY(8);
        config.setHp(60);

        // 射击策略
        config.setShootStrategy(new DirectShootStrategy(
                1, 5, 1, 2, 9, 21));

        // 击杀奖励
        config.setScore(20);

        // 道具掉落
        config.setPropNum(1);
        config.setPropDropRate(0.4);
        config.setPropWeights(Map.of(
                "BloodProp", 50,
                "BulletProp", 30,
                "BulletPlusProp", 20
        ));

        return new EliteEnemy(config);
    }
}
