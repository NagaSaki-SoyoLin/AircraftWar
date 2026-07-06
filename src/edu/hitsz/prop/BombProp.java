package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.observer.EnemyObserver;
import edu.hitsz.observer.PropSubject;

import java.util.ArrayList;
import java.util.List;

//炸弹道具，实现抽象道具接口
public class BombProp extends Prop implements PropSubject {

    // 炸弹道具生效后击毁敌机所获得的分数
    private int gainScore = 0;

    List<EnemyObserver> enemyAircrafts = new ArrayList<>();

    @Override
    public void addEnemy(EnemyObserver enemy) {
        enemyAircrafts.add(enemy);
    }

    @Override
    public void notifyEnemy() {
        for (EnemyObserver enemy : enemyAircrafts) {
            this.gainScore += enemy.onBomb();
        }
    }

    public void action(HeroAircraft heroAircraft) {
        System.out.println("💣炸弹道具生效!");
        notifyEnemy();
    }

    // 获取炸弹道具生效后击毁敌机所获得的分数
    public int getGainScore() {
        return gainScore;
    }

}
