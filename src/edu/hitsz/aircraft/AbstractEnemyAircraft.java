package edu.hitsz.aircraft;

import edu.hitsz.application.Main;
import edu.hitsz.basic.EnemyConfig;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.factory.PropSimpleFactory;
import edu.hitsz.observer.EnemyObserver;
import edu.hitsz.prop.Prop;

import java.util.*;

// 敌机采用工厂模式，接口(抽象类)充当产品角色，同时继承抽象飞机
public abstract class AbstractEnemyAircraft extends AbstractAircraft implements EnemyObserver {

    // 敌机被击落后获得的分数
    protected int score;

    // 掉落道具的数量
    protected int propNum;

    // 是否掉落道具的概率0.0~1.0，1.0表示必定掉落
    protected double propDropRate;

    // 键值对存储道具类型及其权重(控制道具类型与概率)
    protected Map<String, Integer> propWeights;

    // 构造函数，传入配置文件
    public AbstractEnemyAircraft(EnemyConfig config) {
        super(config.getLocationX(), config.getLocationY(),
                config.getSpeedX(), config.getSpeedY(),
                config.getHp(), config.getShootStrategy());
        this.score = config.getScore();
        this.propNum = config.getPropNum();
        this.propDropRate = config.getPropDropRate();
        this.propWeights = config.getPropWeights();
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

    @Override
    public BaseBullet createBullet(int locationX, int locationY, int speedX, int speedY, int power) {
        return new EnemyBullet(locationX, locationY, speedX, speedY, power);
    }

    // 获取分数
    public int getScore() {
        return score;
    }

    // 模板方法：统一的道具生成逻辑，击落敌机掉落道具
    public Set<Prop> generateProp() {
        Set<Prop> props = new HashSet<>();

        //判定是否掉落道具
        if (Math.random() >= propDropRate) return props;

        int totalWeight = 0; //计算总权重
        for (int w : propWeights.values()) totalWeight += w;

        // 追踪已选类型
        Set<String> selectedTypes = new HashSet<>();

        while (props.size() < propNum && selectedTypes.size() < propWeights.size()) {
            double p = Math.random() * totalWeight;
            double cumulative = 0; //累计权重

            for (Map.Entry<String, Integer> entry : propWeights.entrySet()) {
                cumulative += entry.getValue();
                if (p < cumulative) {
                    String type = entry.getKey();
                    if (!selectedTypes.contains(type)) {
                        selectedTypes.add(type);
                        props.add(PropSimpleFactory.createProp(entry.getKey()));
                    }
                    break;
                }
            }
        }

        return props;
    }

}
