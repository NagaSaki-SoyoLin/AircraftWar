package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.observer.EnemyObserver;
import edu.hitsz.observer.PropSubject;

import java.util.ArrayList;
import java.util.List;

//冰冻道具，实现抽象道具接口
public class FreezeProp extends Prop implements PropSubject {

    List<EnemyObserver> enemyAircrafts = new ArrayList<>();

    @Override
    public void addEnemy(EnemyObserver enemy) {
        enemyAircrafts.add(enemy);
    }

    @Override
    public void notifyEnemy() {
        for (EnemyObserver enemy : enemyAircrafts) {
            enemy.onFreeze();
        }
    }

    public void action(HeroAircraft heroAircraft) {
        System.out.println("🧊冰冻道具生效!");
        notifyEnemy();
    }

}
