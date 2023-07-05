package com.vishcorp.nws;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.UUID;

public class worker_doc_page extends AppCompatActivity {
    ImageView workerdocument;
    AutoCompleteTextView workerdoc;
    private String empdoc;
    String[] users = {"Aadhar card", "Voter id", "PAN card","Driving Licence"};
    private Uri filelocation;
    private FirebaseDatabase database;
    Bitmap bitmap;
    private Button btn;
    boolean isAllFieldsChecked = false;

    private final  int PICK_IMAGE_REQUEST = 22;

    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_doc_page);
        workerdocument = findViewById(R.id.getuserdocument);
        workerdoc = findViewById(R.id.emp_docname);
        btn = (Button) findViewById(R.id.doc_button);

        choosedoc();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadedoc();
            }
        });
        workerdocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadedocimage();
            }

            });
        }


    private void uploadedoc() {
        if(filelocation==null)
        {
            Toast.makeText(getApplicationContext(),"Image is Empty",Toast.LENGTH_SHORT).show();
        }else {
            final ProgressDialog pd=new ProgressDialog(this);
            pd.setTitle("Uploade data...");
            pd.show();
            FirebaseStorage storage = FirebaseStorage.getInstance();
            database = FirebaseDatabase.getInstance();
            StorageReference uploader = storage.getReference("docimg" + new Random().nextInt(50));
            uploader.putFile(filelocation)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            uploader.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    pd.dismiss();
                                    FirebaseDatabase  firebaseDatabase1= FirebaseDatabase.getInstance();
                                    DatabaseReference reference = firebaseDatabase1.getReference("document");
                                    Toast.makeText(worker_doc_page.this, "document uploaded", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(worker_doc_page.this, worker_profile.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            float percent = (100 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                            pd.setMessage("Uploaded :" + (int) percent + "%");
                            Toast.makeText(getApplicationContext(),"done",Toast.LENGTH_SHORT).show();
                        }
                    });
        }

    }
    private void uploadedocimage() {

        Dexter.withActivity(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        Intent intent=new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        startActivityForResult(Intent.createChooser(intent,"Select Image File"),1);
                    }
                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(com.karumi.dexter.listener.PermissionRequest permission, PermissionToken token) {

                    }


                }).check();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==1 && resultCode==RESULT_OK){
            filelocation=data.getData();
            try {
                InputStream inputStream=getContentResolver().openInputStream(filelocation);
                bitmap= BitmapFactory.decodeStream((inputStream));
                workerdocument.setImageBitmap(bitmap);
            }catch (Exception ex){
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void choosedoc() {

        String[] Subjects = new String[]{"Aadhar card", "Voter id", "PAN card","Driving Licence"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.dropdown_item, Subjects);
        workerdoc.setAdapter(adapter);

        workerdoc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "" + workerdoc.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    }

//        choosedoc();
//        workerdoc=findViewById(R.id.emp_docname);
//        workerdocument.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SelectImage();
//            }
//        });
//        findViewById(R.id.uploadbutton1).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                uploadworkerdocumentImage();
//            }
//        });
//
//    }
//    private void SelectImage()
//    {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select Image from here..."), PICK_IMAGE_REQUEST);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            filePath = data.getData();
//            try {
//                Bitmap bitmap2 = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
//                workerdocument.setImageBitmap(bitmap2);
//            }
//            catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        }
//    }
//    private void uploadworkerdocumentImage() {
//        if (filePath != null) {
//            ProgressDialog progressDialog = new ProgressDialog(this);
//            progressDialog.setTitle("Uploading...");
//            progressDialog.show();
//
//            StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
//
//                    ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            progressDialog.dismiss();
//                            Toast.makeText(worker_doc_page.this, "document Uploaded!!", Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(worker_doc_page.this, worker_login.class);
//                            startActivity(intent);
//                            finish();
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e)
//                        {
//                            progressDialog.dismiss();
//                            Toast.makeText(worker_doc_page.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onProgress(
//                                UploadTask.TaskSnapshot taskSnapshot) {double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
//                           progressDialog.setMessage("Uploaded " + (int)progress + "%");
//
//                        }
//                    });
//        }
//    }
//    private void choosedoc() {
//
//        String[] Subjects = new String[]{"Aadhar card", "Voter id", "PAN card","Driving Licence"};
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.dropdown_item, Subjects);
//        workerdoc.setAdapter(adapter);
//
//        workerdoc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getApplicationContext(), "" + workerdoc.getText().toString(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    }








