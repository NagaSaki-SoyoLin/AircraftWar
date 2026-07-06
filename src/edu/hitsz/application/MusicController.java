package edu.hitsz.application;

import javax.sound.sampled.*;
import java.util.Map;

// 用于游戏中各种音效的控制
public class MusicController {

    // 音频文件路径常量
    public static final String BGM = "src/videos/bgm.wav"; // 背景音乐
    public static final String BGM_BOSS = "src/videos/bgm_boss.wav"; // Boss背景音乐
    public static final String BOMB_EXPLOSION = "src/videos/bomb_explosion.wav"; // 炸弹爆炸音效
    public static final String BULLET_HIT = "src/videos/bullet_hit.wav"; // 子弹击中音效
    public static final String GAME_OVER = "src/videos/game_over.wav"; // 游戏结束音效
    public static final String GET_SUPPLY = "src/videos/get_supply.wav"; // 获得道具音效

    // 音乐线程的映射，用于管理背景音乐实例
    private final Map<String, MusicThread> musicThreads;

    // 单例实例
    private volatile static MusicController musicController;

    // 私有构造函数，防止外部实例化
    private MusicController() {
        musicThreads = new java.util.HashMap<>();
    }

    // 获取单例实例
    public static MusicController getMusicController() {
        if (musicController == null) {
            synchronized (MusicController.class) {
                if (musicController == null) {
                    musicController = new MusicController();
                }
            }
        }
        return musicController;
    }

    // 播放指定的背景音乐
    public void playBGM(String filename) {
        stopBGM(filename); // 先停止同名音乐，避免重复播放
        MusicThread thread = new MusicThread(filename);
        thread.setLooping(true); // 背景音乐默认循环
        thread.start();
        musicThreads.put(filename, thread);
    }

    // 停止指定背景音乐
    public void stopBGM(String filename) {
        MusicThread thread = musicThreads.get(filename);
        if (thread != null) {
            thread.stopPlaying();
            musicThreads.remove(filename);
        }
    }

    // 停止所有背景音乐
    public void stopAllBGM() {
        for (String filename : musicThreads.keySet()) {
            stopBGM(filename);
        }
        musicThreads.clear();
    }

    // 播放音效
    public void playSoundEffect(String filename) {
        MusicThread thread = new MusicThread(filename);
        thread.setLooping(false); // 音效默认不循环
        thread.start(); // 立即开始播放

    }

}