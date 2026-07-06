package edu.hitsz.factory;

import edu.hitsz.aircraft.AbstractEnemyAircraft;
import edu.hitsz.aircraft.MobEnemy;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.basic.EnemyConfig;
import edu.hitsz.shoot.DirectShootStrategy;

import java.util.Map;

//创建实现工厂接口的具体工厂类，充当具体创建者角色
public class MobEnemyFactory implements EnemyAircraftFactory {
    @Override
    public AbstractEnemyAircraft createEnemyAircraft() {
        // 工厂通过set方法组装配置
        EnemyConfig config = new EnemyConfig();

        // 位置
        config.setLocationX((int) (Math.random() * (Main.WINDOW_WIDTH
                - ImageManager.MOB_ENEMY_IMAGE.getWidth())));
        config.setLocationY((int) (Math.random() * Main.WINDOW_HEIGHT * 0.05));

        // 运动属性
        config.setSpeedX(0);
        config.setSpeedY(10);
        config.setHp(30);

        // 射击策略
        config.setShootStrategy(new DirectShootStrategy(
                0, 0, 0, 0, 0, 0));

        // 击杀奖励
        config.setScore(10);

        // 道具掉落
        config.setPropNum(0);
        config.setPropDropRate(0.0);
        config.setPropWeights(Map.of());

        return new MobEnemy(config);
    }
}