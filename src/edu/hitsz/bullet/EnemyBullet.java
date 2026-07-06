package edu.hitsz.bullet;

import edu.hitsz.observer.EnemyObserver;

/**
 * 敌机子弹
 * &#064;Author  hitsz
 */
public class EnemyBullet extends BaseBullet implements EnemyObserver {

    public EnemyBullet(int locationX, int locationY, int speedX, int speedY, int power) {
        super(locationX, locationY, speedX, speedY, power);
    }

    @Override
    public int onBomb() {
        this.vanish();
        return 0;
    }

    @Override
    public void onFreeze() {
        int preSpeedX = this.getSpeedX();
        int preSpeedY = this.getSpeedY();
        this.setSpeed(0, 0);
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