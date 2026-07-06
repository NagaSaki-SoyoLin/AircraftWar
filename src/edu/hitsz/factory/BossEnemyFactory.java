package edu.hitsz.factory;

import edu.hitsz.aircraft.BossEnemy;
import edu.hitsz.aircraft.AbstractEnemyAircraft;
import edu.hitsz.application.Main;
import edu.hitsz.basic.EnemyConfig;
import edu.hitsz.shoot.CircleShootStrategy;

import java.util.Map;

//创建实现工厂接口的具体工厂类，充当具体创建者角色
public class BossEnemyFactory implements EnemyAircraftFactory {
    @Override
    public AbstractEnemyAircraft createEnemyAircraft() {
        // 工厂通过set方法组装配置
        EnemyConfig config = new EnemyConfig();

        // 位置
        config.setLocationX(Main.WINDOW_WIDTH / 2);
        config.setLocationY(Main.WINDOW_HEIGHT / 7);

        // 运动属性
        config.setSpeedX(Math.random() < 0.5 ? -3 : 3);
        config.setSpeedY(0);
        config.setHp(900);

        // 射击策略
        config.setShootStrategy(new CircleShootStrategy(
                20, 10, 1, 0, 10, 96, 20, 360));

        // 击杀奖励
        config.setScore(500);

        // 道具掉落
        config.setPropNum(3);
        config.setPropDropRate(1.0);
        config.setPropWeights(Map.of(
                "BloodProp", 40,
                "BulletProp", 20,
                "BulletPlusProp", 10,
                "BombProp", 20,
                "FreezeProp", 10
        ));

        return new BossEnemy(config);
    }
}