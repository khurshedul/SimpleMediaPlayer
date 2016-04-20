package b1aiub.aiub.khurshedul.mp3player;

import android.content.Intent;
import android.os.Environment;
import android.preference.PreferenceScreen;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    String[] items;
    ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         lv =(ListView)findViewById(R.id.listView);
        final ArrayList<File> mysongs=findSongs(Environment.getExternalStorageDirectory());
        items =new String[mysongs.size()];
        for(int i=0;i<mysongs.size();i++){
            items[i]=mysongs.get(i).getName().toString().replace(".mp3","").replace(".wav","");
            //Toast.makeText(this,mysongs.get(i).getName().toString(),Toast.LENGTH_LONG);
        }
        ArrayAdapter<String> adp=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,items);
        lv.setAdapter(adp);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getApplicationContext(), player.class).putExtra("pos",position).putExtra("songlist",mysongs));
            }
        });
    }
    public ArrayList<File> findSongs(File root){
        ArrayList<File> al=new ArrayList<File>();
        File[] files=root.listFiles();
        for(File singlefile:files){
            if(singlefile.isDirectory()&&!singlefile.isHidden()){

                al.addAll(findSongs(singlefile));
            }else{
                if(singlefile.getName().endsWith(".mp3")||singlefile.getName().endsWith(".wav"))
                {
                    al.add(singlefile);
                }
            }
        }
    return al;
    }
}
