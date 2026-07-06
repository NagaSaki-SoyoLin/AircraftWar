package edu.hitsz.shoot;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.bullet.BaseBullet;

import java.util.LinkedList;
import java.util.List;

//散射策略，具体策略角色，实现ShootStrategy接口
public class SectorShootStrategy extends ShootStrategy {

    //子弹横向速度差距
    private final int speedXInterval;

    public SectorShootStrategy(int shootNum, int power, int direction, int YOffset, int speedFactor, int speedXInterval) {
        super(shootNum, power, direction, YOffset, speedFactor);
        this.speedXInterval = speedXInterval;
    }

    @Override
    public List<BaseBullet> shoot(AbstractAircraft aircraft) {
        List<BaseBullet> res = new LinkedList<>();
        int x = aircraft.getLocationX();
        int y = aircraft.getLocationY() + direction * YOffset;
        int speedX = 0;
        int speedY = aircraft.getSpeedY() + direction * speedFactor;

        for (int i = 0; i < shootNum; i++) {
            // 根据飞机类型创建不同子弹
            res.add(aircraft.createBullet(
                    x, y, speedX + (i * 2 - shootNum + 1) * speedXInterval, speedY, power)
            );
        }
        return res;
    }

}
