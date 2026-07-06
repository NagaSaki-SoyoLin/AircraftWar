package edu.hitsz.observer;

// 观察者接口
public interface EnemyObserver {

    // 炸弹道具生效后的反应
    int onBomb();

    // 冰冻道具生效后的反应
    void onFreeze();

}