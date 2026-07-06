package edu.hitsz.dao;

import java.util.ArrayList;

//DAO模式步骤三：创建分数记录数据访问对象接口DAO
public interface ScoreDao {

    // 获取所有分数记录
    ArrayList<ScoreRecord> getAllScoreRecords();

    // 添加分数记录
    void doAdd(ScoreRecord scoreRecord);

    // 根据索引删除分数记录
    void doDelete(int index);

}
