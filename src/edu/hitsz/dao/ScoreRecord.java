package edu.hitsz.dao;

//DAO模式步骤一：创建分数记录实体类POJO
public class ScoreRecord {

    // 玩家名
    private String playerName;

    // 得分
    private int score;

    // 记录时间
    private String time;

    public ScoreRecord() {}

    public ScoreRecord(String playerName, int score, String time) {
        this.playerName = playerName;
        this.score = score;
        this.time = time;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
