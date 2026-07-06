package edu.hitsz.game;

import edu.hitsz.application.ImageManager;
import edu.hitsz.factory.*;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 简单难度游戏类
 * - 敌机数量少、产生慢、射击周期长
 * - 不生成Boss敌机（canSpawnBoss → false）
 * - 难度不随时间变化（updateDifficulty 为空实现）
 */
public class GameEasy extends AbstractGame {

    @Override
    protected void initializeConfig() {
        this.difficultyName = "简单模式";
        this.backGroundImage = ImageManager.BACKGROUND_IMAGE_EASY;
        this.daoFilePath = "src/score_records/easy_game.txt";
        this.maxEnemyCount = 5;
        this.spawnCycle = 25;
        this.heroShootCycle = 25;
        this.enemyShootCycle = 30;
        this.normalFactoryWeights = new LinkedHashMap<>(Map.of(
                new MobEnemyFactory(), 90,
                new EliteEnemyFactory(), 7,
                new ElitePlusEnemyFactory(), 2,
                new EliteProEnemyFactory(), 1
        ));
        this.canSpawnBoss = false;
        this.bossSpawnThreshold = Integer.MAX_VALUE;
        this.bossHpBase = 0;
        this.bossHpIncrement = 0;
        this.difficultyUpdateTime = Integer.MAX_VALUE;
    }

}