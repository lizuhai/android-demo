package com.example.zhli.musicplayer;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;


public class MainActivity extends ActionBarActivity {

    private EditText etPath;
    private String filePath;
    private MediaPlayer player;
    private Button btPlay, btPause, btStop, btReplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etPath = (EditText) findViewById(R.id.et_path);
        btPlay = (Button) findViewById(R.id.bt_play);
        btPause = (Button) findViewById(R.id.bt_pause);
        btStop = (Button) findViewById(R.id.bt_stop);
        btReplay = (Button) findViewById(R.id.bt_replay);
    }


    public void play(View v) {
        filePath = etPath.getText().toString().trim();
//        File file = new File(filePath);
//        if(file.exists()) {
        if(filePath.startsWith("http://")) {
            try {
                player = new MediaPlayer();
                player.setDataSource(filePath);
                player.setAudioStreamType(AudioManager.STREAM_MUSIC);

                // 同步的准备方法, 本地播放用
//                player.prepare();
//                player.start();
//                btPlay.setEnabled(false);

                // 异步加载，播放网络的音频
                player.prepareAsync();
                player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        player.start();
                        btPlay.setEnabled(false);
                    }
                });

                // 播放完按钮设置为可用
                player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        btPlay.setEnabled(true);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "play error", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "not exixts", Toast.LENGTH_SHORT).show();
        }
    }

    public void pause(View v) {
        if(btPause.getText().toString().equals("继续")) {
            player.start();
            btPause.setText("暂停");
            return;
        }

        if(player != null && player.isPlaying()) {
            player.pause();
            btPause.setText("继续");
        }
    }

    public void stop(View v) {
        if(player != null) {
            player.stop();
            player.release();
            player = null;
        }
        btPause.setText("暂停");
        btPlay.setEnabled(true);
    }

    public void replay(View v) {
        if(player != null) {
            player.seekTo(0);   // 从第 0ms 开始播放
        }
        btPause.setText("暂停");
    }
}
