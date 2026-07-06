package edu.hitsz.game;

import edu.hitsz.aircraft.AbstractEnemyAircraft;
import edu.hitsz.aircraft.BossEnemy;
import edu.hitsz.application.ImageManager;
import edu.hitsz.factory.*;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 困难难度游戏类
 * - 敌机多、产生快、射击频繁
 * - Boss血量每次召唤时递增（calculateBossHp 实现递增逻辑）
 * - 随时间变化（每40秒）：在普通基础上，额外增加英雄/敌机射击频率加快
 */
public class GameHard extends AbstractGame {

    @Override
    protected void initializeConfig() {
        this.difficultyName = "困难模式";
        this.backGroundImage = ImageManager.BACKGROUND_IMAGE_HARD;
        this.daoFilePath = "src/score_records/hard_game.txt";
        this.maxEnemyCount = 8;
        this.spawnCycle = 15;
        this.heroShootCycle = 18;
        this.enemyShootCycle = 20;
        this.normalFactoryWeights = new LinkedHashMap<>(Map.of(
                new MobEnemyFactory(), 75,
                new EliteEnemyFactory(), 15,
                new ElitePlusEnemyFactory(), 6,
                new EliteProEnemyFactory(), 4
        ));
        this.canSpawnBoss = true;
        this.bossSpawnThreshold = 1500;
        this.bossHpBase = 900;
        this.bossHpIncrement = 300;
        this.difficultyUpdateTime = 40;
    }

    @Override
    protected int calculateBossHp() {
        return bossHpBase + bossSummonCount * bossHpIncrement;
    }

    @Override
    protected void updateDifficulty() {
        int level = checkAndReturnNewLevel();
        if (level < 0) return;  // 等级没变化，直接返回
        // 敌机产生周期缩短（下限为5帧）
        this.spawnCycle = Math.max(5, 15 - level * 1.5);
        // 英雄射击周期缩短（下限为8帧）
        this.heroShootCycle = Math.max(8, 18 - level * 0.8);
        // 敌机射击周期缩短（下限为6帧）
        this.enemyShootCycle = Math.max(6, 20 - level * 1.0);

        // 控制台输出难度提升信息
        System.out.println("=======================");
        System.out.println("【困难模式】难度提升！");
        System.out.println("  当前等级: " + level);
        System.out.println("  参数变化:");
        System.out.println("    - 敌机生成周期: " + this.spawnCycle + "帧");
        System.out.println("    - 英雄射击周期: " + this.heroShootCycle + "帧");
        System.out.println("    - 敌机射击周期: " + this.enemyShootCycle + "帧");
        System.out.println("=======================");

        // 敌机速度加快，血量增加
        for (AbstractEnemyAircraft enemy : enemyAircrafts) {
            if (enemy instanceof BossEnemy) continue;  // Boss不受影响
            // 速度x不为0时，速度x+1，否则为0，上限12，y+1，上限20
            int speedX = Math.min(enemy.getSpeedX() == 0 ? 0 : enemy.getSpeedX() + 1, 12);
            int speedY = Math.min(enemy.getSpeedY() + 1, 20);
            enemy.setSpeed(speedX, speedY);
            // 血量不超过500
            int hp = Math.min(enemy.getHp() + level * 10, 500);
            enemy.setHp(hp);
        }
    }

}