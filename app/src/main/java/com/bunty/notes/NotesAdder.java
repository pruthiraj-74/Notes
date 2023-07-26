package com.bunty.notes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.appcompat.app.AppCompatActivity;

import com.bunty.notes.Database.RoomDB;
import com.bunty.notes.Models.Notes;
import com.bunty.notes.databinding.ActivityNotesAdderBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class NotesAdder extends AppCompatActivity {
    ActivityNotesAdderBinding binding;
    Notes notes;
    RoomDB roomDB;
    boolean isOldNote=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding=ActivityNotesAdderBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        notes=new Notes();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("EEE,d MMM yyyy HH:mm a");
        Date date=new Date();
        binding.curdateandtime.setText(simpleDateFormat.format(date));
        binding.charcnt.setText(String.valueOf(binding.editTextNotes.getText().length())+" characters");
        roomDB=RoomDB.getInstance(this);
        try {
            notes= (Notes) getIntent().getSerializableExtra("old_note");
            binding.editTextTitle.setText(Objects.requireNonNull(notes).getTitle());
            binding.editTextNotes.setText(notes.getNotes());
            isOldNote=true;
        } catch (Exception e){
            e.printStackTrace();
        }
        binding.imagesave.setOnClickListener(v -> {
            String title=binding.editTextTitle.getText().toString();
            String desc=binding.editTextNotes.getText().toString();
//            if(desc.isEmpty()){
//                Toast.makeText(getApplicationContext(),"Please add some text",Toast.LENGTH_SHORT).show();
//                return;
//            }
            if(!isOldNote){
                notes=new Notes();
            }
            notes.setTitle(title);
            notes.setNotes(desc);
            notes.setDate(simpleDateFormat.format(date));
            roomDB.mainDAO().isOld(notes.getID(),true);
            Intent intent=new Intent();
            intent.putExtra("note",notes);
            setResult(Activity.RESULT_OK,intent);
            finish();
        });
        binding.backbutton.setOnClickListener(v -> {
            String title=binding.editTextTitle.getText().toString();
            String desc=binding.editTextNotes.getText().toString();
            if(desc.isEmpty()&&title.isEmpty()){
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
            if(!isOldNote){
                notes=new Notes();
            }
            notes.setTitle(title);
            notes.setNotes(desc);
            notes.setDate(simpleDateFormat.format(date));
            roomDB.mainDAO().isOld(notes.getID(),true);
            Intent intent=new Intent();
            intent.putExtra("note",notes);
            setResult(Activity.RESULT_OK,intent);
            finish();
        });
        final TextWatcher mTextEditorWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.charcnt.setText(String.valueOf(s.length())+" characters");
            }
            public void afterTextChanged(Editable s) {}
        };
        binding.editTextNotes.addTextChangedListener(mTextEditorWatcher);
    }
    @Override
    public void onBackPressed() {
        String title=binding.editTextTitle.getText().toString();
        String desc=binding.editTextNotes.getText().toString();
//        if(desc.isEmpty()){
//            Toast.makeText(getApplicationContext(),"Please add some text",Toast.LENGTH_SHORT).show();
//            return;
//        }
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("EEE,d MMM yyyy HH:mm a");
        Date date=new Date();
        if(!isOldNote){
            notes=new Notes();
        }
        notes.setTitle(title);
        notes.setNotes(desc);
        notes.setDate(simpleDateFormat.format(date));
        roomDB.mainDAO().isOld(notes.getID(),true);
        Intent intent=new Intent();
        intent.putExtra("note",notes);
        setResult(Activity.RESULT_OK,intent);
        finish();
    }
}