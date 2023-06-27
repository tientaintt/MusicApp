package hcmute.vika.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import hcmute.vika.myapplication.Model.Song;

public class UploadMusic extends AppCompatActivity {
    Button btnupload;
    String image= "https://firebasestorage.googleapis.com/v0/b/applicationmusic-56667.appspot.com/o/mussic.jpg?alt=media&token=19ef4722-5373-41c4-8606-9a9008f78204";
    EditText input_artist,input_song,input_genre;
    StorageReference storageReference;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_music);
        Init();


        //Database
        storageReference= FirebaseStorage.getInstance().getReference();
        databaseReference= FirebaseDatabase.getInstance().getReference();

        btnupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectFiles();
            }
        });

    }

    private void Init() {
        btnupload = findViewById(R.id.upload);
        input_song=findViewById(R.id.song_name_input);
        input_artist=findViewById(R.id.artist_name_input);
        input_genre=findViewById(R.id.genre_input);
    }

    private String convertGenre(String genre){
        switch (genre){
            case "Trữ Tình":
                genre= "TruTinh";
                break;
            case "EDM":
                genre="EDM";
                break;
            case "Nhạc Việt":
                genre="VPop";
                break;
            case "US/UK":
                genre="USUK";
                break;
            case "Other":
                genre="Other";
                break;
        }
        return genre;
    }
    private void selectFiles(){
        Intent intent = new Intent();
        intent.setType("audio/mpeg");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select PDF Files..."), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if((requestCode == 1) && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            UploadFiles(data.getData());
        }
    }
    private void UploadFiles(Uri data){
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();
        StorageReference reference = storageReference.child(System.currentTimeMillis()+".mp3");
        reference.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isComplete());
                        Uri url =uriTask.getResult();
                        Song Song = new Song(input_song.getText().toString(),input_artist.getText().toString(),url.toString(),image);
                        databaseReference.child("Song").child(convertGenre(input_genre.getText().toString())).child(databaseReference.push().getKey()).setValue(Song);
                        Toast.makeText(UploadMusic.this,"File uploaded!!",Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double progress = (100* snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                        progressDialog.setMessage("Upload..."+(int)progress+"%");
                    }
                });

    }
}
