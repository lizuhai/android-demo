package com.example.zhli.mediaplayer;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends ActionBarActivity {

    private EditText etPath;
    private String filePath;
    private MediaPlayer player;
    private Button btPlay, btPause, btStop, btReplay;
    private SurfaceView sv;
    private SurfaceHolder holder;
    private int position;
    private SeekBar seekBar;
    private TimerTask task;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etPath = (EditText) findViewById(R.id.et_path);
        btPlay = (Button) findViewById(R.id.bt_play);
        btPause = (Button) findViewById(R.id.bt_pause);
        btStop = (Button) findViewById(R.id.bt_stop);
        btReplay = (Button) findViewById(R.id.bt_replay);
        sv = (SurfaceView) findViewById(R.id.sv);

        seekBar = (SeekBar) findViewById(R.id.seekbar);

        holder = sv.getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
            }
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                System.out.println("changed");
                if(position > 0) {
                    try {
                        player = new MediaPlayer();
                        player.setDataSource(filePath);
                        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        player.setDisplay(holder);

                        player.prepare();
                        player.start();

                        player.seekTo(position);

                        btPlay.setEnabled(false);
                        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                btPlay.setEnabled(true);
                            }
                        });

                        // 设置拖动进度条的最大值
                        int max = player.getDuration();
                        seekBar.setMax(max);
                        timer = new Timer();
                        task = new TimerTask() {
                            @Override
                            public void run() {
                                seekBar.setProgress(player.getCurrentPosition());
                            }
                        };
                        // 每隔 0.5s 更新进度条
                        timer.schedule(task, 0, 500);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                System.out.println("destory");
                if(player != null && player.isPlaying()) {
                    position = player.getCurrentPosition();
                    player.stop();
                    player.release();
                    player = null;

                    timer.cancel();
                    task.cancel();
                    timer = null;
                    timer = null;
                }
            }
        });
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);    // 兼容 4.0 以前，4.0 以后的版本不用加这句话


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar s) {
                int position = s.getProgress();
                player.seekTo(position);
            }
        });
    }


    public void play(View v) {
        filePath = etPath.getText().toString().trim();
        File file = new File(filePath);
        if(file.exists()) {
//        if(filePath.startsWith("http://")) {
            try {
                player = new MediaPlayer();
                player.setDataSource(filePath);
                player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                player.setDisplay(holder);

                player.prepare();
                player.start();

                // 设置拖动进度条的最大值
                int max = player.getDuration();
                seekBar.setMax(max);
                timer = new Timer();
                task = new TimerTask() {
                    @Override
                    public void run() {
                        seekBar.setProgress(player.getCurrentPosition());
                    }
                };
                // 每隔 0.5s 更新进度条
                timer.schedule(task, 0, 500);

                btPlay.setEnabled(false);

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
