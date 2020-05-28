package com.example.coookbab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class IngredientDetail extends AppCompatActivity {
    private DatabaseReference mReference;
    private FirebaseDatabase mDatabase;
    private FirebaseStorage storage;
    private DatabaseReference mTip;
    private ImageView imageView;
    private Button savebtn;
    private Button delbtn;
    private EditText ingredientnum;
    private EditText ingredientlife;
    private TextView tip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient_detail);

        storage= FirebaseStorage.getInstance("gs://cook-bab.appspot.com");
        mDatabase = FirebaseDatabase.getInstance();
        String uid="hUeiODcSXrSEe1MJ9stKlAbcpcv2";
        mReference = mDatabase.getReference().child("user").child(uid).child("refrigerator").child("ingredient");
        mTip =mDatabase.getReference().child("how_to_sore");
        tip=(TextView)findViewById(R.id.tip);
        imageView = (ImageView)findViewById(R.id.imageView);
        savebtn = (Button)findViewById(R.id.savebtn);
        delbtn = (Button)findViewById(R.id.delbtn);
        ingredientlife =(EditText)findViewById(R.id.inglife);
        ingredientnum = (EditText)findViewById(R.id.ingnum);


        Intent intent = getIntent();
        final String filename =intent.getExtras().getString("filename");
        mTip.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String text = dataSnapshot.child(filename).child("tip").getValue().toString();
                tip.setText(text);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ingredientnum.setText(dataSnapshot.child(filename).child("num").getValue().toString());
                ingredientlife.setText(dataSnapshot.child(filename).child("life").getValue().toString());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        StorageReference storageRef = storage.getReference().child("ingredient_photo/"+filename+".png");
        Glide.with(IngredientDetail.this)
                .using(new FirebaseImageLoader())
                .load(storageRef)
                .into(imageView);

        delbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(IngredientDetail.this, RefrigeratorMain.class);
                mReference.child(filename).removeValue();
                startActivity(intent1);
            }
        });

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(IngredientDetail.this, RefrigeratorMain.class);
                mReference.child(filename).child("num").setValue(ingredientnum.getText().toString());
                mReference.child(filename).child("life").setValue(ingredientlife.getText().toString());
                startActivity(intent1);
            }
        });
    }
}
