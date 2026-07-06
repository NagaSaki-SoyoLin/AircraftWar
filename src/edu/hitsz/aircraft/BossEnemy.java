package edu.hitsz.aircraft;

import edu.hitsz.basic.EnemyConfig;

//Boss机，实现抽象敌机接口
public class BossEnemy extends AbstractEnemyAircraft {

    public BossEnemy(EnemyConfig config) {
        super(config);
    }

    @Override
    public int onBomb() {
        return 0;
    }

    @Override
    public void onFreeze() {
    }

}