package edu.hitsz.basic;

import edu.hitsz.shoot.ShootStrategy;

import java.util.Map;

public class EnemyConfig {

    private int locationX;
    private int locationY;

    private int speedX;
    private int speedY;
    private int hp;

    private ShootStrategy shootStrategy;

    private int score;

    private int propNum;
    private double propDropRate;
    private Map<String, Integer> propWeights;

    public int getLocationX() {
        return locationX;
    }

    public void setLocationX(int locationX) {
        this.locationX = locationX;
    }

    public int getLocationY() {
        return locationY;
    }

    public void setLocationY(int locationY) {
        this.locationY = locationY;
    }

    public int getSpeedX() {
        return speedX;
    }

    public void setSpeedX(int speedX) {
        this.speedX = speedX;
    }

    public int getSpeedY() {
        return speedY;
    }

    public void setSpeedY(int speedY) {
        this.speedY = speedY;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public ShootStrategy getShootStrategy() {
        return shootStrategy;
    }

    public void setShootStrategy(ShootStrategy shootStrategy) {
        this.shootStrategy = shootStrategy;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getPropNum() {
        return propNum;
    }

    public void setPropNum(int propNum) {
        this.propNum = propNum;
    }

    public double getPropDropRate() {
        return propDropRate;
    }

    public void setPropDropRate(double propDropRate) {
        this.propDropRate = propDropRate;
    }

    public Map<String, Integer> getPropWeights() {
        return propWeights;
    }

    public void setPropWeights(Map<String, Integer> propWeights) {
        this.propWeights = propWeights;
    }
}