package edu.hitsz.factory;

import edu.hitsz.prop.*;

//创建一个简单工厂类，负责创建产品
public class PropSimpleFactory {
    //如果工厂类没有状态、不需要被继承、只是用来统一创建对象，那么工厂方法可以写成static
    public static Prop createProp(String propName) {
        switch (propName) {
            case "BloodProp":
                return new BloodProp();
            case "BombProp":
                return new BombProp();
            case "BulletProp":
                return new BulletProp();
            case "BulletPlusProp":
                return new BulletPlusProp();
            case "FreezeProp":
                return new FreezeProp();
            default:
                throw new IllegalArgumentException("Unknown prop name: " + propName);
        }
    }
}
