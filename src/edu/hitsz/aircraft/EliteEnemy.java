package edu.hitsz.aircraft;

import edu.hitsz.basic.EnemyConfig;

//精英敌机，实现抽象敌机接口
public class EliteEnemy extends AbstractEnemyAircraft {

    public EliteEnemy(EnemyConfig config){
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
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (this.isValid) this.setSpeed(preSpeedX, preSpeedY);
        }).start();
    }

}
