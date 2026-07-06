package edu.hitsz.game;

import edu.hitsz.aircraft.*;
import edu.hitsz.application.*;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.factory.*;
import edu.hitsz.observer.PropSubject;
import edu.hitsz.prop.BombProp;
import edu.hitsz.prop.Prop;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import java.util.Timer;

/**
 * 抽象游戏模板类 - 使用模板方法模式
 *
 * @author hitsz
 * 算法骨架: action() 方法定义了完整的游戏主循环流程
 * 可变步骤: 通过抽象方法和钩子方法交由子类实现
 */
public abstract class AbstractGame extends JPanel {

    // 公共游戏对象
    private final HeroAircraft heroAircraft; // 英雄机
    protected final List<AbstractEnemyAircraft> enemyAircrafts; // 敌机
    private final List<BaseBullet> heroBullets; // 英雄机子弹
    private final List<BaseBullet> enemyBullets; // 敌机子弹
    private final List<Prop> props; // 道具

    // 计数器与标志
    private int spawnCounter = 0; // 敌机生成计数器
    private int shootCounter = 0; // 英雄机射击计数器
    private int score = 0; // 当前玩家分数
    private boolean gameOverFlag = false; // 游戏结束标志
    private boolean isBossExist = false; // Boss机存在标志
    private int lastBossScore = 0; // Boss机上次坠毁时的分数
    protected int bossSummonCount = 0; // Boss机召唤次数（用于困难模式）
    protected long gameStartTime = 0; // 游戏开始时间（用于难度递增计算）
    protected int lastUpdatedLevel = -1;   // 上次执行增强时的等级
    private int backGroundTop = 0; // 背景图像
    private final Timer timer; // 调度器
    private final MusicController musicController; // 音乐控制器

    // 难度配置参数
    protected String difficultyName; // 难度名称
    protected BufferedImage backGroundImage; // 背景图像
    protected String daoFilePath; // 数据访问文件路径
    protected int maxEnemyCount; // 屏幕上最多存在的敌机数量
    protected double spawnCycle; // 敌机生成周期
    protected double heroShootCycle; // 英雄机射击周期
    protected double enemyShootCycle; // 敌机射击周期
    protected Map<EnemyAircraftFactory, Integer> normalFactoryWeights; // 普通敌机工厂权重表
    protected boolean canSpawnBoss; // 是否允许生成Boss机
    protected int bossSpawnThreshold; // Boss机生成阈值
    protected int bossHpBase; // Boss机基础血量
    protected int bossHpIncrement; // Boss机血量递增量
    protected int difficultyUpdateTime; // 难度更新时间（秒）

    // 构造方法
    public AbstractGame() {
        // 单例模式创建英雄机
        heroAircraft = HeroAircraft.getHeroAircraft();
        // 初始化游戏对象
        enemyAircrafts = new LinkedList<>();
        heroBullets = new LinkedList<>();
        enemyBullets = new LinkedList<>();
        props = new LinkedList<>();
        // 启动英雄机鼠标监听
        new HeroController(this, heroAircraft);
        // 启动定时器
        this.timer = new Timer("game-action-timer", true);
        // 单例模式创建音乐控制器
        musicController = MusicController.getMusicController();
        // 调用子类的初始化配置方法
        initializeConfig();
        // 记录游戏开始时间
        gameStartTime = System.currentTimeMillis();
    }

    // 模板方法：游戏启动入口，执行游戏逻辑
    public void action() {
        // 播放背景音乐
        musicController.playBGM(MusicController.BGM);

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                enemyEmergeAction();     // 步骤1: 敌机生成
                shootAction();           // 步骤2: 射击
                bulletsMoveAction();     // 步骤3: 子弹移动
                aircraftsMoveAction();   // 步骤4: 飞机移动
                propsMoveAction();       // 步骤5: 道具移动
                crashCheckAction();      // 步骤6: 碰撞检测
                postProcessAction();     // 步骤7: 后处理
                updateDifficulty();      // 步骤8: 难度更新
                repaint();               // 步骤9: 绘制
                checkResultAction();     // 步骤10: 结束检查
                gameOverAction();        // 步骤11: 游戏结束
            }
        };

        // 以固定延迟时间进行执行：本次任务执行完成后，延迟 timeInterval 再执行下一次
        // 时间间隔(ms)，控制刷新频率
        int timeInterval = 40;
        timer.schedule(task, 0, timeInterval);
    }

    //***********************
    //      Action 各部分
    //***********************

    // 抽象方法，必须由子类实现
    // 初始化该难度级别的所有配置参数
    protected abstract void initializeConfig();

    // 钩子方法，子类可选择性地覆盖
    // 随游戏时间逐步提升难度，默认不做任何操作
    protected void updateDifficulty() {
    }

    // 计算本次召唤Boss时的血量，默认返回基础血量
    protected int calculateBossHp() {
        return bossHpBase;
    }

    // 工具方法
    // 获取当前难度的显示名称
    protected String getDifficultyName() {
        return this.difficultyName;
    }

    // 检查难度等级是否发生了变化，防止每帧重复执行
    protected int checkAndReturnNewLevel() {
        if (difficultyUpdateTime == Integer.MAX_VALUE) return -1;
        long elapsedSeconds = (System.currentTimeMillis() - gameStartTime) / 1000;
        int currentLevel = (int) (elapsedSeconds / difficultyUpdateTime);
        if (currentLevel != this.lastUpdatedLevel) {
            this.lastUpdatedLevel = currentLevel;
            return currentLevel;
        }
        return -1;  // 等级没变，跳过
    }

    // 共享的具体方法
    // 敌机生成
    private void enemyEmergeAction() {
        //根据周期生成敌机
        spawnCounter++;
        if (spawnCounter < spawnCycle) return;
        spawnCounter = 0;

        //如果敌机数量超过限制，则不生成
        if (enemyAircrafts.size() >= maxEnemyCount) return;

        //根据分数和Boss状态选择对应的工厂
        EnemyAircraftFactory factory;
        // 使用钩子方法判断是否能生成Boss
        if (canSpawnBoss && score - lastBossScore >= bossSpawnThreshold && !isBossExist) {
            factory = new BossEnemyFactory(); // 使用工厂模式创建Boss敌机
        } else {
            int totalWeight = 0; //计算总权重
            for (int w : normalFactoryWeights.values()) totalWeight += w;

            double cumulative = 0; //累计权重
            double p = Math.random() * totalWeight;

            factory = normalFactoryWeights.keySet().iterator().next(); //取map中第一个工厂作初始值
            for (Map.Entry<EnemyAircraftFactory, Integer> entry : normalFactoryWeights.entrySet()) {
                cumulative += entry.getValue();
                if (p < cumulative) {
                    factory = entry.getKey();
                    break;
                }
            }
        }

        // 创建敌机
        AbstractEnemyAircraft enemy = factory.createEnemyAircraft();
        // 敌机速度和血量随难度提升
        int level = Math.max(0, lastUpdatedLevel);
        if (level > 0 && !(enemy instanceof BossEnemy)) {
            boolean isHard = this instanceof GameHard;
            enemy.setHp((int) Math.min(enemy.getHp() * (1 + 0.15 * level), isHard ? 500 : 250));
            enemy.setSpeed(
                    enemy.getSpeedX() == 0 ? 0 : Math.min(enemy.getSpeedX() + level / 2, isHard ? 12 : 8),
                    Math.min(enemy.getSpeedY() + level, isHard ? 20 : 15)
            );
        }
        // 如果是Boss，动态计算并设置血量
        if (enemy instanceof BossEnemy) {
            int dynamicHp = calculateBossHp();
            enemy.setHp(dynamicHp);
            musicController.stopBGM(MusicController.BGM);
            musicController.playBGM(MusicController.BGM_BOSS);
            isBossExist = true;
        }
        enemyAircrafts.add(enemy);
    }

    // 飞机射击
    private void shootAction() {
        shootCounter++;
        if (shootCounter >= heroShootCycle) {
            shootCounter = 0;
            //英雄机射击，使用策略模式，执行射击策略
            heroBullets.addAll(heroAircraft.executeShootStrategy());

            // 敌机射击
            for (AbstractEnemyAircraft enemyAircraft : enemyAircrafts) {
                //使用策略模式，执行射击策略
                enemyBullets.addAll(enemyAircraft.executeShootStrategy());
            }

        }
    }

    // 子弹移动
    private void bulletsMoveAction() {
        for (BaseBullet bullet : heroBullets) {
            bullet.forward();
        }
        for (BaseBullet bullet : enemyBullets) {
            bullet.forward();
        }
    }

    // 飞机移动
    private void aircraftsMoveAction() {
        for (AbstractEnemyAircraft enemyAircraft : enemyAircrafts) {
            enemyAircraft.forward();
        }
    }

    // 道具移动
    private void propsMoveAction() {
        for (Prop prop : props) {
            prop.forward();
        }
    }


    /**
     * 碰撞检测：
     * 1. 敌机攻击英雄
     * 2. 英雄攻击/撞击敌机
     * 3. 英雄获得补给
     */
    private void crashCheckAction() {

        // 敌机子弹攻击英雄机
        for (BaseBullet bullet : enemyBullets) {
            if (bullet.notValid()) continue;
            if (heroAircraft.notValid()) break; //英雄机已毁，跳出循环
            if (heroAircraft.crash(bullet)) {
                heroAircraft.decreaseHp(bullet.getPower());
                bullet.vanish();
            }
        }

        // 英雄子弹攻击敌机
        for (BaseBullet bullet : heroBullets) {
            if (bullet.notValid()) {
                continue;
            }
            for (AbstractEnemyAircraft enemyAircraft : enemyAircrafts) {
                if (enemyAircraft.notValid()) {
                    // 已被其他子弹击毁的敌机，不再检测
                    // 避免多个子弹重复击毁同一敌机的判定
                    continue;
                }
                if (enemyAircraft.crash(bullet)) {
                    // 敌机撞击到英雄机子弹
                    // 播放击中音效
                    musicController.playSoundEffect(MusicController.BULLET_HIT);
                    // 敌机损失一定生命值
                    enemyAircraft.decreaseHp(bullet.getPower());
                    bullet.vanish();
                    if (enemyAircraft.notValid()) {

                        // 获得分数，产生道具补给
                        score += enemyAircraft.getScore();
                        Set<Prop> propList = enemyAircraft.generateProp();
                        for (Prop prop : propList) {
                            //生成道具位置在敌机坠毁附近随机区域
                            prop.setLocation(
                                    enemyAircraft.getLocationX() + (0.5 - Math.random()) * 125,
                                    enemyAircraft.getLocationY() + (0.5 - Math.random()) * 75
                            );
                            props.add(prop);
                        }

                        //更新Boss机存活状态
                        if (enemyAircraft instanceof BossEnemy) {
                            // 停止播放Boss背景音乐，播放正常背景音乐
                            musicController.stopBGM(MusicController.BGM_BOSS);
                            musicController.playBGM(MusicController.BGM);
                            // 更改Boss状态
                            isBossExist = false;
                            lastBossScore = score;
                            bossSummonCount++;
                        }

                    }
                }
            }
        }

        // 新增机制：英雄机子弹与敌机子弹相撞，敌机子弹消失，英雄机子弹减少一定伤害值
        for (BaseBullet heroBullet : heroBullets) {
            if (heroBullet.notValid()) continue;
            for (BaseBullet enemyBullet : enemyBullets) {
                if (enemyBullet.notValid()) continue;
                if (heroBullet.crash(enemyBullet)) {
                    heroBullet.decreasePower(enemyBullet.getPower());
                    enemyBullet.vanish();
                }
            }
        }


        // 英雄机 与 敌机 相撞，均损毁
        for (AbstractEnemyAircraft enemyAircraft : enemyAircrafts) {
            if (enemyAircraft.crash(heroAircraft) || heroAircraft.crash(enemyAircraft)) {
                enemyAircraft.vanish();
                heroAircraft.decreaseHp(Integer.MAX_VALUE);
            }
        }

        // 我方获得道具，道具生效
        for (Prop prop : props) {
            if (prop.notValid()) {
                continue;
            }
            if (heroAircraft.crash(prop)) {
                // 播放拾取道具音效
                musicController.playSoundEffect(MusicController.GET_SUPPLY);
                // 观察者模式，注册当前游戏内所有敌机和敌机子弹为观察者
                if (prop instanceof PropSubject) {
                    for (AbstractEnemyAircraft enemyAircraft : enemyAircrafts) {
                        ((PropSubject) prop).addEnemy(enemyAircraft);
                    }
                    for (BaseBullet bullet : enemyBullets) {
                        ((PropSubject) prop).addEnemy((EnemyBullet) bullet);
                    }
                }
                // 执行道具生效逻辑
                prop.action(heroAircraft);
                // 炸弹道具特殊处理，生效时播放音效，总分增加被击毁敌机的分数
                if (prop instanceof BombProp) {
                    musicController.playSoundEffect(MusicController.BOMB_EXPLOSION);
                    score += ((BombProp) prop).getGainScore();
                }
                prop.vanish();
                break; //避免多个道具在同一帧生效
            }
        }

    }

    /**
     * 后处理：
     * 1. 删除无效的子弹
     * 2. 删除无效的敌机
     * 3. 删除无效的道具
     */
    private void postProcessAction() {
        enemyBullets.removeIf(AbstractFlyingObject::notValid);
        heroBullets.removeIf(AbstractFlyingObject::notValid);
        enemyAircrafts.removeIf(AbstractEnemyAircraft::notValid);

        // 删除无效道具
        props.removeIf(AbstractFlyingObject::notValid);

    }

    /**
     * 检查游戏是否结束，若结束：关闭线程池
     */
    private void checkResultAction() {
        // 游戏结束检查英雄机是否存活
        if (heroAircraft.getHp() <= 0) {
            timer.cancel(); // 取消定时器并终止所有调度任务
            gameOverFlag = true;
        }
    }

    /**
     * 游戏结束后，执行的操作
     */
    private void gameOverAction() {
        if (!gameOverFlag) return;
        ScoreBoardDialog.showGameOver(
                Main.mainFrame,
                this.score,
                this.daoFilePath,
                getDifficultyName(),
                this.musicController
        );
    }

    //***********************
    //      Paint 各部分
    //***********************

    /**
     * 重写 paint方法
     * 通过重复调用paint方法，实现游戏动画
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // 绘制背景,图片滚动，使用backGroundImage来区分不同难度的背景图片
        g.drawImage(backGroundImage, 0, this.backGroundTop - Main.WINDOW_HEIGHT, null);
        g.drawImage(backGroundImage, 0, this.backGroundTop, null);
        this.backGroundTop += 1;
        if (this.backGroundTop == Main.WINDOW_HEIGHT) {
            this.backGroundTop = 0;
        }

        // 先绘制子弹，后绘制飞机
        // 这样子弹显示在飞机的下层
        paintImageWithPositionRevised(g, enemyBullets);
        paintImageWithPositionRevised(g, heroBullets);
        paintImageWithPositionRevised(g, enemyAircrafts);

        // 绘制道具
        paintImageWithPositionRevised(g, props);

        g.drawImage(ImageManager.HERO_IMAGE, heroAircraft.getLocationX() - ImageManager.HERO_IMAGE.getWidth() / 2,
                heroAircraft.getLocationY() - ImageManager.HERO_IMAGE.getHeight() / 2, null);

        //绘制得分和生命值
        paintScoreAndLife(g);

    }

    private void paintImageWithPositionRevised(Graphics g, List<? extends AbstractFlyingObject> objects) {
        if (objects.isEmpty()) {
            return;
        }

        for (AbstractFlyingObject object : objects) {

            // 绘制飞机的图片
            BufferedImage image = object.getImage();
            assert image != null : objects.getClass().getName() + " has no image! ";
            g.drawImage(image, object.getLocationX() - image.getWidth() / 2,
                    object.getLocationY() - image.getHeight() / 2, null);

            // 如果是飞机，绘制血条
            if (object instanceof AbstractAircraft) {
                int hp = ((AbstractAircraft) object).getHp();
                int maxHp = ((AbstractAircraft) object).getMaxHp();

                int barWidth = image.getWidth();
                int barHeight = 4;
                int barX = object.getLocationX() - barWidth / 2;
                int barY = object.getLocationY() - image.getHeight() / 2 - barHeight - 2;

                // 血条背景
                g.setColor(new Color(80, 80, 80));
                g.fillRect(barX, barY, barWidth, barHeight);

                // 当前血量（根据比例变色）
                double hpRatio = (double) hp / maxHp;
                int currentBarWidth = (int) (barWidth * hpRatio);

                if (hpRatio > 0.6) g.setColor(Color.GREEN);
                else if (hpRatio > 0.3) g.setColor(Color.YELLOW);
                else g.setColor(Color.RED);
                g.fillRect(barX, barY, currentBarWidth, barHeight);

                // 边框
                g.setColor(Color.WHITE);
                g.drawRect(barX, barY, barWidth, barHeight);
            }

        }


    }

    private void paintScoreAndLife(Graphics g) {
        int x = 10;
        int y = 25;
        g.setColor(Color.RED);
        g.setFont(new Font("SansSerif", Font.BOLD, 22));
        g.drawString("SCORE: " + this.score, x, y);
        y = y + 20;
        g.drawString("LIFE: " + this.heroAircraft.getHp(), x, y);
    }

}