package edu.hitsz.aircraft;

import edu.hitsz.basic.EnemyConfig;

//精锐敌机，实现抽象敌机接口
public class ElitePlusEnemy extends AbstractEnemyAircraft {

    public ElitePlusEnemy(EnemyConfig config){
        super(config);
    }

    @Override
    public int onBomb() {
        this.vanish();
        return this.score;
    }

    @Override
    public void onFreeze() {
        int preSpeedX = this.getSpeedX();
        int preSpeedY = this.getSpeedY();
        this.setSpeed(0, 0);
        new Thread(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (this.isValid) this.setSpeed(preSpeedX, preSpeedY);
        }).start();
    }

}