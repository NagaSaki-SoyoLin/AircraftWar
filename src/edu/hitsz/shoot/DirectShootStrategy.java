package edu.hitsz.shoot;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.bullet.BaseBullet;

import java.util.LinkedList;
import java.util.List;

//直射策略，具体策略角色，实现ShootStrategy接口
public class DirectShootStrategy extends ShootStrategy {

    //子弹横坐标间距
    private final int XInterval;

    public DirectShootStrategy(int shootNum, int power, int direction, int YOffset, int speedFactor, int XInterval) {
        super(shootNum, power, direction, YOffset, speedFactor);
        this.XInterval = XInterval;
    }

    /**
     * 通过射击产生子弹
     *
     * @return 射击出的子弹List
     */
    @Override
    public List<BaseBullet> shoot(AbstractAircraft aircraft) {
        List<BaseBullet> res = new LinkedList<>();
        int x = aircraft.getLocationX();
        int y = aircraft.getLocationY() + direction * YOffset;
        int speedX = 0;
        int speedY = aircraft.getSpeedY() + direction * speedFactor;

        for (int i = 0; i < shootNum; i++) {
            // 根据飞机类型创建不同子弹
            res.add(aircraft.createBullet(x + (i * 2 - shootNum + 1) * XInterval, y, speedX, speedY, power));
        }
        return res;
    }

}
