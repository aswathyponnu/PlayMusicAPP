package com.devcentre.playmusicapp.view;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.devcentre.playmusicapp.R;
import com.devcentre.playmusicapp.adapter.MusicAdapter;
import com.devcentre.playmusicapp.databinding.ActivityMainBinding;
import com.devcentre.playmusicapp.model.MusicItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MusicAdapter adapter;
    private List<MusicItem> musicItems;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        musicItems = new ArrayList<MusicItem>();
        adapter = new MusicAdapter(this.getApplicationContext());
        binding.rvMusic.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        binding.rvMusic.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        binding.btnAdd.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setType("audio/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            mediaResultLauncher.launch(Intent.createChooser(intent,"Select Audios"));
        });
    }

    ActivityResultLauncher<Intent> mediaResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                MusicItem musicItem = null;
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        for(int i = 0; i < data.getClipData().getItemCount(); i++) {
                            Uri uri = data.getClipData().getItemAt(i).getUri();

                            String uriString = uri.toString();
                            File myFile = new File(uriString);

                            musicItem = new MusicItem();
                            musicItem.setId(i+1);
                            musicItem.setUri(uriString);
                            musicItem.setPath(myFile.getAbsolutePath());
                            musicItem.setTitle(myFile.getName());
                            musicItems.add(musicItem);
                        }
                    }
                    binding.tvHeader.setVisibility(View.INVISIBLE);
                    adapter.setMusicItems(musicItems);
                    binding.rvMusic.setAdapter(adapter);

                    // doSomeOperations();
                }
            });

}