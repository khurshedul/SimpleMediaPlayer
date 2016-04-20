package b1aiub.aiub.khurshedul.mp3player;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import java.io.File;
import java.util.ArrayList;

public class player extends AppCompatActivity implements View.OnClickListener{
   static MediaPlayer mp;
    Uri u;
    int position;
    SeekBar sb;
    ArrayList<File> mysongs;
    Button bpl,bn,ff,fb,bp;
    Thread updateSeekbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
       bpl = (Button) findViewById(R.id.btplay);
        bn = (Button) findViewById(R.id.btn);
        ff = (Button) findViewById(R.id.btff);
        fb = (Button) findViewById(R.id.btfb);
        bp = (Button) findViewById(R.id.btp);
        bpl.setOnClickListener(this);
        bn.setOnClickListener(this);
        ff.setOnClickListener(this);
        fb.setOnClickListener(this);
        bp.setOnClickListener(this);
        sb = (SeekBar) findViewById(R.id.seekBar);
        updateSeekbar = new Thread() {
            @Override
            public void run() {
                int totalDuration = mp.getDuration();
                int currpos = 0;

                while (currpos < totalDuration) {
                    try {
                        sleep(500);
                        currpos = mp.getCurrentPosition();
                        sb.setProgress(currpos);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //super.run();
            }
        };
        if(mp!=null){
            mp.stop();
            mp.release();
        }
        Intent i=getIntent();
        Bundle b=i.getExtras();
        mysongs=(ArrayList)b.getParcelableArrayList("songlist");
        position=b.getInt("pos",0);
        u=Uri.parse(mysongs.get(position).toString());
        mp=MediaPlayer.create(getApplicationContext(), u);
        mp.start();
        sb.setMax(mp.getDuration());
        updateSeekbar.start();
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mp.seekTo(seekBar.getProgress());
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch(id){
            case R.id.btplay:
                if(mp.isPlaying()){
                    mp.stop();
                }
                else {
                    mp.start();
                    sb.setMax(mp.getDuration());
                }
                break;
            case R.id.btp: {
                mp.stop();
                mp.release();
                position = (position - 1 < 0) ? mysongs.size() - 1 : position - 1;
                Uri u = Uri.parse(mysongs.get(position).toString());
                mp = MediaPlayer.create(getApplicationContext(), u);
                mp.start();
                sb.setMax(mp.getDuration());
            }
                break;
            case R.id.btfb: {
                mp.seekTo(mp.getCurrentPosition() - 5000);
            }
                break;
            case R.id.btff:
                mp.seekTo(mp.getCurrentPosition()+5000);
                break;
            case R.id.btn: {
                mp.stop();
                mp.release();
                position = (position + 1) % mysongs.size();
                u = Uri.parse(mysongs.get(position).toString());
                mp = MediaPlayer.create(getApplicationContext(), u);
                mp.start();
                sb.setMax(mp.getDuration());
            }

                break;

        }
    }
}
