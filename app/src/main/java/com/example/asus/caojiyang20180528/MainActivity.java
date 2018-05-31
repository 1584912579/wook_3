package com.example.asus.caojiyang20180528;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;


public class MainActivity extends AppCompatActivity {

    private GramophoneView mGramophoneView;
    private Button mBtnPlayUrl;
    private Button mBtnPause;
    private Button mBtnStop;
    private SeekBar mSkbProgress;
    private MediaPlayer mediaPlayer;
    private AudioManager audioManager;
    private Handler handler =new Handler(){
        //收到Handler发回的消息被回调
        public void handleMessage(Message msg) {
            //更新UI组件
            if(msg.what==2){
                int current=mSkbProgress.getMax()
                        * mediaPlayer.getCurrentPosition() / mediaPlayer.getDuration();
               // int current=mediaPlayer.getCurrentPosition();
                mSkbProgress.setProgress(current);
                handler.sendEmptyMessageDelayed(2,1000);
            }
        }
    };
    private boolean flag=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        mediaPlayer = new MediaPlayer();
        String path= "http://sc1.111ttt.cn:8282/2018/1/03m/13/396131229550.m4a?tflag=1519095601&pin=6cd414115fdb9a950d827487b16b5f97#.mp3";
        try {
//            mediaPlayer.reset();
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            //循环播放
            mediaPlayer.setLooping(flag);
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    // TODO Auto-generated method stub
                    //加载下一首
                    //Toast.makeText(MainActivity.this, "播放完成", Toast.LENGTH_SHORT).show();
                }
            });
            //mSkbProgress.setMax(mediaPlayer.getDuration());
            mSkbProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
               // int progress;
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    // TODO Auto-generated method stub
//                    this.progress = progress * mediaPlayer.getDuration()
//                            / seekBar.getMax();
                    //mediaPlayer.start();
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    // TODO Auto-generated method stub
                   if(fromUser){
                        mediaPlayer.seekTo(progress);
                    }
                }
            });
            audioManager =(AudioManager) getSystemService(AUDIO_SERVICE);

        } catch (Exception e) {
            e.printStackTrace();
        }
        mBtnPlayUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.start();
                mGramophoneView.setPlaying(true);
                handler.sendEmptyMessageDelayed(2,1000);


            }
        });
        mBtnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.pause();
                mGramophoneView.setPlaying(false);
            }
        });
        mBtnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mediaPlayer.stop();
                flag=!flag;
//                mGramophoneView.setPlaying(false);
            }
        });

    }

    private void initView() {
        mGramophoneView = (GramophoneView) findViewById(R.id.gramophone_view);
        mBtnPlayUrl = (Button) findViewById(R.id.btnPlayUrl);
        mBtnPause = (Button) findViewById(R.id.btnPause);
        mBtnStop = (Button) findViewById(R.id.btnStop);
        mSkbProgress = (SeekBar) findViewById(R.id.skbProgress);
    }
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        if(mediaPlayer!=null&&mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer=null;//回收资源
        }
        super.onDestroy();
    }
}
