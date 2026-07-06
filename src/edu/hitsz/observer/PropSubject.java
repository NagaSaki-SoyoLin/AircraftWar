package edu.hitsz.observer;

// 主题接口，即抽象观察目标/被观察者
public interface PropSubject {

    // 定义添加观察者的方法
    void addEnemy(EnemyObserver enemyObserver);

    // 定义通知观察者的方法
    void notifyEnemy();

}
