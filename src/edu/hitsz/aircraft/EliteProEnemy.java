package edu.hitsz.aircraft;

import edu.hitsz.basic.EnemyConfig;

//王牌敌机，实现抽象敌机接口
public class EliteProEnemy extends AbstractEnemyAircraft {

    public EliteProEnemy(EnemyConfig config){
        super(config);
    }

    @Override
    public int onBomb() {
        this.decreaseHp(this.hp / 2);
        return 0;
    }

    @Override
    public void onFreeze() {
        int preSpeedX = this.getSpeedX();
        int preSpeedY = this.getSpeedY();
        this.setSpeed(preSpeedX / 2, preSpeedY / 2);
        new Thread(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (this.isValid) this.setSpeed(preSpeedX, preSpeedY);
        }).start();
    }

}