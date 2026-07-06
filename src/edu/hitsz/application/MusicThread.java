package edu.hitsz.application;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

public class MusicThread extends Thread {

    //音频文件名
    private final String filename;
    private AudioFormat audioFormat;
    private byte[] samples;

    // 播放或暂停标志
    private volatile boolean running = true;

    // 循环播放标志
    private volatile boolean looping;

    // 静态缓存，避免重复读取同一文件
    private static final Map<String, byte[]> sampleCache = new HashMap<>();

    public MusicThread(String filename) {
        //初始化filename
        this.filename = filename;
        reverseMusic();
    }

    public void reverseMusic() {
        // 先查缓存
        if (sampleCache.containsKey(filename)) {
            samples = sampleCache.get(filename);
            // 还是需要获取 audioFormat
            try {
                AudioInputStream stream = AudioSystem.getAudioInputStream(new File(filename));
                audioFormat = stream.getFormat();
                stream.close();
            } catch (Exception e) {
                System.out.println("音频格式获取失败!");
            }
            return;
        }
        // 缓存未命中，才读磁盘
        try {
            //定义一个AudioInputStream用于接收输入的音频数据，使用AudioSystem来获取音频的音频输入流
            AudioInputStream stream = AudioSystem.getAudioInputStream(new File(filename));
            //用AudioFormat来获取AudioInputStream的格式
            audioFormat = stream.getFormat();
            samples = getSamples(stream);
            sampleCache.put(filename, samples); // 存入缓存
        } catch (UnsupportedAudioFileException e) {
            System.out.println("不支持的音频文件格式!");
        } catch (IOException e) {
            System.out.println("音频文件读取失败!");
        }
    }

    public byte[] getSamples(AudioInputStream stream) {
        int size = (int) (stream.getFrameLength() * audioFormat.getFrameSize());
        byte[] samples = new byte[size];
        DataInputStream dataInputStream = new DataInputStream(stream);
        try {
            dataInputStream.readFully(samples);
        } catch (IOException e) {
            System.out.println("音频文件读取失败!");
        }
        return samples;
    }

    public void play(InputStream source) {
        int size = (int) (audioFormat.getFrameSize() * audioFormat.getSampleRate());
        byte[] buffer = new byte[size];
        //源数据行SoureDataLine是可以写入数据的数据行
        SourceDataLine dataLine;
        //获取受数据行支持的音频格式DataLine.info
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
        try {
            dataLine = (SourceDataLine) AudioSystem.getLine(info);
            dataLine.open(audioFormat, size);
        } catch (LineUnavailableException e) {
            System.out.println("音频设备不可用!");
            return;
        }
        dataLine.start();
        try {
            int numBytesRead = 0;
            while (numBytesRead != -1 && running) {
                //从音频流读取指定的最大数量的数据字节，并将其放入缓冲区中
                numBytesRead = source.read(buffer, 0, buffer.length);
                //通过此源数据行将数据写入混频器
                if (numBytesRead != -1) {
                    dataLine.write(buffer, 0, numBytesRead);
                }
            }

        } catch (IOException ex) {
            System.out.println("音频文件读取失败!");
        } finally {
            // 等待缓冲区数据全部播放完毕
            dataLine.drain();
            dataLine.stop();
            dataLine.close();
        }
    }

    // 动态切换循环模式
    public void setLooping(boolean loop) {
        this.looping = loop;
    }

    // 停止播放，让线程自然退出
    public void stopPlaying() {
        this.running = false;
    }

    @Override
    public void run() {
        while (running) {
            InputStream stream = new ByteArrayInputStream(samples);
            this.play(stream);
            if (!looping) break;
        }
    }
}