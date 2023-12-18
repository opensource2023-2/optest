package com.example.test;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class board extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        FirebaseApp.initializeApp(board.this);
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        ImageButton gotohome = findViewById(R.id.homeinboard);

        gotohome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), home_page.class);
                startActivity(intent);
            }
        });

        FloatingActionButton add = findViewById(R.id.addNote);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View view1 = LayoutInflater.from(board.this).inflate(R.layout.board_more,null);
                TextInputLayout materialLayout, titleLayout, contentLayout;
                materialLayout = view1.findViewById(R.id.materialLayout);
                titleLayout = view1.findViewById(R.id.titleLayout);
                contentLayout = view1.findViewById(R.id.contentLayout);

                TextInputEditText materialET, titleET, contentET;
                materialET = view1.findViewById(R.id.materialET);
                titleET = view1.findViewById(R.id.titleET);
                contentET = view1.findViewById(R.id.contentET);
                AlertDialog alertDialog = new AlertDialog.Builder(board.this)
                        .setTitle("추가하기")
                        .setView(view1)
                        .setPositiveButton("추가", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (Objects.requireNonNull(titleET.getText()).toString().isEmpty()) {
                                    titleLayout.setError("필수로 입력해야 하는 영역입니다.");
                                } else if (Objects.requireNonNull(contentET.getText()).toString().isEmpty()) {
                                    contentLayout.setError("필수로 입력해야 하는 영역입니다.");
                                } else if (Objects.requireNonNull(materialET.getText()).toString().isEmpty()){
                                    materialLayout.setError("필수로 입력해야 하는 영역입니다.");
                                }
                                else {
                                    ProgressDialog dialog = new ProgressDialog(board.this);
                                    dialog.setMessage("Database에 저장 중입니다...");
                                    dialog.show();
                                    Note note1 = new Note();
                                    note1.setBnum("1");
                                    note1.setWriterID("1");
                                    note1.setMaterial(materialET.getText().toString());
                                    note1.setTitle(titleET.getText().toString());
                                    note1.setContent(contentET.getText().toString());
                                    database.getReference().child("notes").push().setValue(note1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            dialog.dismiss();
                                            dialogInterface.dismiss();
                                            Toast.makeText(board.this, "성공적으로 저장되었습니다!", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            dialog.dismiss();
                                            Toast.makeText(board.this, "저장 과정에 오류가 있었습니다", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .create();
                alertDialog.show();
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recycler);

        database.getReference().child("notes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Note> arrayList = new ArrayList<>();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Note note = dataSnapshot.getValue(Note.class);
                    Objects.requireNonNull(note).setKey(dataSnapshot.getKey());
                    arrayList.add(note);
                }

                if(arrayList.isEmpty()){
                    recyclerView.setVisibility(View.GONE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                }

                boardadapter adapter = new boardadapter(board.this, arrayList);
                recyclerView.setAdapter(adapter);

                adapter.setOnItemClickListener(new boardadapter.OnItemClickListener() {
                    @Override
                    public void onClick(Note note) {
                        View view = LayoutInflater.from(board.this).inflate(R.layout.board_more, null);
                        TextInputLayout materialLayout, titleLayout, contentLayout;
                        TextInputEditText materialET, titleET, contentET;


                        materialET = view.findViewById(R.id.materialET);
                        titleET = view.findViewById(R.id.titleET);
                        contentET = view.findViewById(R.id.contentET);
                        materialLayout = view.findViewById(R.id.materialLayout);
                        titleLayout = view.findViewById(R.id.titleLayout);
                        contentLayout = view.findViewById(R.id.contentLayout);

                        materialET.setText(note.getMaterial());
                        titleET.setText(note.getTitle());
                        contentET.setText(note.getContent());

                        ProgressDialog progressDialog = new ProgressDialog(board.this);

                        AlertDialog alertDialog = new AlertDialog.Builder(board.this)
                                .setTitle("보기 - 편집하기")
                                .setView(view)
                                .setPositiveButton("저장", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if  (Objects.requireNonNull(materialET.getText()).toString().isEmpty()){
                                            materialLayout.setError("필수로 입력해야 하는 영역입니다.");
                                        } else if(Objects.requireNonNull(titleET.getText()).toString().isEmpty()) {
                                            titleLayout.setError("필수로 입력해야 하는 영역입니다.");
                                        } else if (Objects.requireNonNull(contentET.getText()).toString().isEmpty()) {
                                            contentLayout.setError("필수로 입력해야 하는 영역입니다.");
                                        } else {
                                            progressDialog.setMessage("저장 중입니다...");
                                            progressDialog.show();
                                            Note note = new Note();
                                            note.setMaterial(materialET.getText().toString());
                                            note.setTitle(titleET.getText().toString());
                                            note.setContent(contentET.getText().toString());
                                            database.getReference().child("notes").child(note.getKey()).setValue(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    progressDialog.dismiss();
                                                    dialogInterface.dismiss();
                                                    Toast.makeText(board.this, "성공적으로 저장되었습니다!", Toast.LENGTH_SHORT).show();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(board.this, "저장 과정에 오류가 있었습니다", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    }
                                })
                                .setNeutralButton("닫기", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                })
                                .setNegativeButton("삭제", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        progressDialog.setTitle("삭제 중...");
                                        progressDialog.show();
                                        database.getReference().child("notes").child(note.getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(board.this,"성공적으로 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                progressDialog.dismiss();
                                            }
                                        });
                                    }
                                }).create();
                        alertDialog.show();
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}