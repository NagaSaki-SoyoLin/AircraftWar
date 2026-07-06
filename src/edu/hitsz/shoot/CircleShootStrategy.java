package edu.hitsz.shoot;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.bullet.BaseBullet;

import java.util.LinkedList;
import java.util.List;

//环射策略，具体策略角色，实现ShootStrategy接口
public class CircleShootStrategy extends ShootStrategy {

    //椭圆水平半轴长
    private final int radiusX;

    //椭圆垂直半轴长
    private final int radiusY;

    //椭圆弧对应的角度范围
    private final int angleSpan;

    public CircleShootStrategy(int shootNum, int power, int direction, int YOffset, int speedFactor,
                               int radiusX, int radiusY, int angleSpan
    ) {
        super(shootNum, power, direction, YOffset, speedFactor);
        this.radiusX = radiusX;
        this.radiusY = radiusY;
        this.angleSpan = angleSpan;
    }

    @Override
    public List<BaseBullet> shoot(AbstractAircraft aircraft) {
        List<BaseBullet> res = new LinkedList<>();
        int x = aircraft.getLocationX();
        int y = aircraft.getLocationY();
        int speedX = 0;
        int speedY = aircraft.getSpeedY();

        double startAngle = -Math.toRadians(angleSpan / 2.0);
        double angleStep = Math.toRadians(angleSpan) / (shootNum - 1);

        for (int i = 0; i < shootNum; i++) {
            double angle = startAngle + i * angleStep;
            // 根据飞机类型创建不同子弹
            res.add(aircraft.createBullet(
                            x + (int) (Math.sin(angle) * radiusX),
                            y + (int) (-Math.cos(angle) * radiusY),
                            speedX + (int) (Math.sin(angle) * speedFactor),
                            speedY + direction * (int) (Math.cos(angle) * speedFactor),
                            power
                    )
            );
        }
        return res;
    }

}
