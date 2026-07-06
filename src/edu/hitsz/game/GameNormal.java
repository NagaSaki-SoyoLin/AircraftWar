package edu.hitsz.game;

import edu.hitsz.aircraft.AbstractEnemyAircraft;
import edu.hitsz.aircraft.BossEnemy;
import edu.hitsz.application.ImageManager;
import edu.hitsz.factory.*;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 普通难度游戏类
 * - 中等敌机数量和产生速度
 * - 可以生成Boss，但Boss血量固定不变（calculateBossHp 使用默认实现）
 * - 随时间变化（每60秒）：敌机产生周期缩短、敌机速度加快、敌机血量增加
 */
public class GameNormal extends AbstractGame {

    @Override
    protected void initializeConfig() {
        this.difficultyName = "普通模式";
        this.maxEnemyCount = 6;
        this.spawnCycle = 20;
        this.heroShootCycle = 20;
        this.enemyShootCycle = 25;
        this.backGroundImage = ImageManager.BACKGROUND_IMAGE_NORMAL;
        this.daoFilePath = "src/score_records/normal_game.txt";
        this.normalFactoryWeights = new LinkedHashMap<>(Map.of(
                new MobEnemyFactory(), 85,
                new EliteEnemyFactory(), 10,
                new ElitePlusEnemyFactory(), 3,
                new EliteProEnemyFactory(), 2
        ));
        this.canSpawnBoss = true;
        this.bossSpawnThreshold = 1000;
        this.bossHpBase = 900;
        this.bossHpIncrement = 0;
        this.difficultyUpdateTime = 60;
    }

    @Override
    protected void updateDifficulty() {
        int level = checkAndReturnNewLevel();
        if (level < 0) return;  // 等级没变化，直接返回
        // 敌机产生周期缩短（下限为8帧）
        this.spawnCycle = Math.max(8, 20 - level * 1.5);
        // 英雄射击周期缩短（下限为12帧）
        this.heroShootCycle = Math.max(12, 20 - level * 0.8);
        // 敌机射击周期缩短（下限为10帧）
        this.enemyShootCycle = Math.max(10, 25 - level * 1.0);

        // 控制台输出难度提升信息
        System.out.println("=======================");
        System.out.println("【普通模式】难度提升！");
        System.out.println("  当前等级: " + level);
        System.out.println("  参数变化:");
        System.out.println("    - 敌机生成周期: " + this.spawnCycle + "帧");
        System.out.println("    - 英雄射击周期: " + this.heroShootCycle + "帧");
        System.out.println("    - 敌机射击周期: " + this.enemyShootCycle + "帧");
        System.out.println("=======================");

        // 敌机速度加快，血量增加
        for (AbstractEnemyAircraft enemy : enemyAircrafts) {
            if (enemy instanceof BossEnemy) continue;  // Boss不受影响
            // 速度x不为0时，速度x+1，否则为0，上限8，y+1，上限15
            int speedX = Math.min(enemy.getSpeedX() == 0 ? 0 : enemy.getSpeedX() + 1, 8);
            int speedY = Math.min(enemy.getSpeedY() + 1, 15);
            enemy.setSpeed(speedX, speedY);
            // 血量不超过250
            int hp = Math.min(enemy.getHp() + level * 5, 250);
            enemy.setHp(hp);
        }
    }

}