package com.mobile18team.coookbab;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class IngredientDetail extends AppCompatActivity {
    private DatabaseReference mReference;
    private FirebaseAuth mAuth;
    private String userUrl="";
    private FirebaseDatabase mDatabase;
    private FirebaseStorage storage;
    private DatabaseReference mTip;
    private ImageView imageView;
    private Button savebtn;
    private Button delbtn;
    private EditText ingredientnum;
    private EditText ingredientlife;
    private TextView tip, tv_igtitle, textView8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient_detail);

        storage= FirebaseStorage.getInstance("gs://cook-bab.appspot.com");
        mDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        userUrl = user.getUid();
        mReference = mDatabase.getReference().child("user").child(userUrl).child("refrigerator").child("ingredient");
        mTip =mDatabase.getReference().child("how_to_sore");
        tip=(TextView)findViewById(R.id.tip);
        imageView = (ImageView)findViewById(R.id.imageView);
        savebtn = (Button)findViewById(R.id.savebtn);
        delbtn = (Button)findViewById(R.id.delbtn);
        ingredientlife =(EditText)findViewById(R.id.inglife);
        ingredientnum = (EditText)findViewById(R.id.ingnum);
        tv_igtitle = findViewById(R.id.tv_igtitle);
        textView8 = findViewById(R.id.textView8);
        Intent intent = getIntent();
        final String filename =intent.getExtras().getString("filename");
        textView8.setText("<재료 보관 방법>");
        mTip.child(filename).child("tip").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue(String.class)!=null){
                    String value = dataSnapshot.getValue(String.class);
                    tip.setText(value);
                }else{
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(filename).child("name").getValue(String.class)!=null){
                    tv_igtitle.setText(dataSnapshot.child(filename).child("name").getValue().toString());
                }else{
                }
                if(dataSnapshot.child(filename).child("num").getValue(String.class)!=null){
                    ingredientnum.setText(dataSnapshot.child(filename).child("num").getValue().toString());
                }else{
                }
                if(dataSnapshot.child(filename).child("life").getValue(String.class)!=null){
                    ingredientlife.setText(dataSnapshot.child(filename).child("life").getValue().toString());
                }else{
                }
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
                Intent intent1 = new Intent(IngredientDetail.this, MainActivity.class);
                mReference.child(filename).removeValue();
                startActivity(intent1);
            }
        });

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(IngredientDetail.this, MainActivity.class);
                mReference.child(filename).child("num").setValue(ingredientnum.getText().toString());
                mReference.child(filename).child("life").setValue(ingredientlife.getText().toString());
                startActivity(intent1);
            }
        });
    }
}
