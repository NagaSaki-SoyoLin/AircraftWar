package edu.hitsz.factory;

import edu.hitsz.aircraft.AbstractEnemyAircraft;

//创建一个敌机工厂接口，充当创建者角色
public interface EnemyAircraftFactory {
    AbstractEnemyAircraft createEnemyAircraft();
}