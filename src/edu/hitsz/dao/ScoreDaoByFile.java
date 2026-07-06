package edu.hitsz.dao;

import java.io.*;
import java.util.ArrayList;

//DAO模式步骤三：创建分数记录数据访问对象接口实现类，基于文件的实现
public class ScoreDaoByFile implements ScoreDao {

    // 数据库：保存分数记录的文件路径
    private String filePath;

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public ArrayList<ScoreRecord> getAllScoreRecords() {
        return readFile(filePath);
    }

    @Override
    public void doAdd(ScoreRecord scoreRecord) {
        ArrayList<ScoreRecord> allRecords = readFile(filePath);
        allRecords.add(scoreRecord);
        allRecords.sort((o1, o2) -> Integer.compare(o2.getScore(), o1.getScore()));
        writeFile(filePath, allRecords);
    }

    @Override
    public void doDelete(int index) {
        ArrayList<ScoreRecord> allRecords = readFile(filePath);
        if (index >= 0 && index < allRecords.size()) {
            allRecords.remove(index);
            writeFile(filePath, allRecords);
        }
    }

    // 读取文件中所有的分数记录
    private ArrayList<ScoreRecord> readFile(String filePath) {
        ArrayList<ScoreRecord> scoreRecords = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                ScoreRecord scoreRecord = new ScoreRecord();
                scoreRecord.setPlayerName(fields[0]);
                scoreRecord.setScore(Integer.parseInt(fields[1]));
                scoreRecord.setTime(fields[2]);
                scoreRecords.add(scoreRecord);
            }
            return scoreRecords;
        } catch (IOException e) {
            throw new RuntimeException("读取分数文件失败: " + filePath, e);
        }
    }

    // 将新生成的分数记录写入文件中
    private void writeFile(String filePath, ArrayList<ScoreRecord> scoreRecords) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, false))) {
            for (ScoreRecord record : scoreRecords) {
                bw.write(
                        record.getPlayerName() + ","
                                + record.getScore() + ","
                                + record.getTime() + "\n"
                );
            }
        } catch (IOException e) {
            throw new RuntimeException("写入分数文件失败: " + filePath, e);
        }
    }

}
