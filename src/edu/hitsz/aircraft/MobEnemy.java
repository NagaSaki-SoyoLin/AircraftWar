package edu.hitsz.aircraft;

import edu.hitsz.basic.EnemyConfig;

/**
 * 普通敌机
 * 不可射击、不掉落道具
 *
 * @author hitsz
 */
//普通敌机，实现抽象敌机接口
public class MobEnemy extends AbstractEnemyAircraft {

    public MobEnemy(EnemyConfig config){
        super(config);
    }

    @Override
    public int onBomb() {
        this.vanish();
        return this.score;
    }

    @Override
    public void onFreeze() {
        this.setSpeed(0, 0);
    }

}
