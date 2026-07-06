package edu.hitsz.aircraft;

import edu.hitsz.factory.MobEnemyFactory;

import static org.junit.jupiter.api.Assertions.*;

class AbstractAircraftTest {

    @org.junit.jupiter.api.Test
    void decreaseHp() {
        // 创建一个测试用的敌机实例
        AbstractEnemyAircraft enemy = new MobEnemyFactory().createEnemyAircraft();

        // 测试初始HP
        assertEquals(30, enemy.getHp());

        // 测试减少部分HP
        enemy.decreaseHp(10);
        assertEquals(20, enemy.getHp());

        // 测试减少到0
        enemy.decreaseHp(20);
        assertEquals(0, enemy.getHp());

        // 测试减少到负数，期望结果为0
        enemy.decreaseHp(5);
        assertEquals(0, enemy.getHp());
    }

    @org.junit.jupiter.api.Test
    void getHp() {
        // 创建一个测试用的敌机实例
        AbstractEnemyAircraft enemy = new MobEnemyFactory().createEnemyAircraft();

        // 测试初始HP
        assertEquals(30, enemy.getHp());

        // 测试HP减少后的值
        enemy.decreaseHp(10);
        assertEquals(20, enemy.getHp());

        // 测试HP减少到负数的情况，期望结果为0
        enemy.decreaseHp(25);
        assertEquals(0, enemy.getHp());
    }

    @org.junit.jupiter.api.Test
    void crash() {
        // 初始化英雄机并设置其坐标为(100, 100)
        HeroAircraft heroAircraft = HeroAircraft.getHeroAircraft();
        heroAircraft.setLocation(100, 100);

        // 创建一个火力道具并设置其坐标为可与英雄机相撞的坐标(100, 100)
        edu.hitsz.prop.BulletProp bulletProp = new edu.hitsz.prop.BulletProp();
        bulletProp.setLocation(100, 100);

        // 调用英雄机的crash方法并将火力道具作为参数传入
        boolean result = heroAircraft.crash(bulletProp);

        // 判断是否成功检测到碰撞，期望结果为true
        assertTrue(result);
    }

}