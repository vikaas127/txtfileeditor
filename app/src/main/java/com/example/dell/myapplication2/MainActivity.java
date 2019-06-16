package com.example.dell.myapplication2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.*;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import android.graphics.Typeface;
import  android.widget.PopupMenu;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity implements OnClickListener {
ImageView mImageview;
    private Button btn;
    private EditText edittext1;
    private EditText edittext2;
    private static final int PICKFILE_RESULT_CODE = 1;

    private static final int PERMISSION_CODE =1001 ;
    private static final int IMAGE_PICK_CODE =1000 ;
    MultiAutoCompleteTextView mainview;
    String copydata;
    Button btn_bold,btn_italic,btn_normal,btn_size,btn_copydata,btn_new,btn_save,btn_file;
    Intent myfileIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
btn_file=(Button)findViewById(R.id.filepicker);
        btn_save=(Button)findViewById(R.id.save);
        btn_bold = (Button) findViewById(R.id.boldtext);
        btn_italic =(Button) findViewById(R.id.italictext);
        btn_normal =(Button) findViewById(R.id.normaltext);
        btn_size =(Button) findViewById(R.id.fontsize);
        btn_copydata =(Button) findViewById(R.id.copytext);
        btn_new =(Button) findViewById(R.id.newdoc);
mImageview=(ImageView)findViewById((R.id.image2));

        mainview = (MultiAutoCompleteTextView) findViewById(R.id.mainpart);
btn_file.setOnClickListener(this);
btn_save.setOnClickListener(this);
        btn_bold.setOnClickListener(this);
        btn_italic.setOnClickListener(this);
        btn_normal.setOnClickListener(this);
        btn_size.setOnClickListener(this);
        btn_new.setOnClickListener(this);
        btn_copydata.setOnClickListener(this);
        btn = (Button) findViewById(R.id.select);
        edittext1 = (EditText) findViewById(R.id.edittext1);
        edittext2 = (EditText) findViewById(R.id.edittext2);

        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("text/*");
                startActivityForResult(intent,PICKFILE_RESULT_CODE);}
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            switch (requestCode) {
                case PICKFILE_RESULT_CODE:

                    String selectedFile = data.getData().getPath();
                    String filename = selectedFile.substring(selectedFile.lastIndexOf("/") + 1);
                    edittext1.setText(filename);
                    selectedFile = selectedFile.substring(selectedFile.lastIndexOf(":") + 1);

                    try {
                        File myFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + selectedFile);
                        FileInputStream fIn = new FileInputStream(myFile);
                        BufferedReader myReader = new BufferedReader(new InputStreamReader(fIn));
                        String aDataRow = "";
                        String aBuffer = "";
                        while ((aDataRow = myReader.readLine()) != null) {
                            aBuffer += aDataRow + "\n";
                        }
                        mainview.setText(aBuffer);
                        myReader.close();
                        Toast.makeText(getBaseContext(), "Done reading file " + filename + " from sdcard", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    break;
            }


        }

        registerForContextMenu(btn_size);
    }

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch(requestCode){
            case 10:
                if (resultCode==RESULT_OK && requestCode== IMAGE_PICK_CODE){

                    mImageview.setImageURI(data.getData());
                }
        }

    }*/

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
       switch (requestCode){
           case PERMISSION_CODE:{
               if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                 pickImageFromGallery();
               }else {
                   Toast.makeText(this,"permission denied",Toast.LENGTH_SHORT).show();
               }
           }
       }


    }

    public  void onClick(View view) {
        if (view==btn_save){
            String filename= edittext1.getText().toString();
            String content =mainview.getText().toString();
            if (!filename.equals("")&&!content.equals("")) {
                savefile_save(filename, content);

            }
        }
     else   if (view==btn_file){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                String permissionss =(Manifest.permission.READ_EXTERNAL_STORAGE);
                requestPermissions(new String[]{permissionss},PERMISSION_CODE);

        }else {
              pickImageFromGallery();
            }}
        else {   pickImageFromGallery();  }




        }
      else  if (view == btn_bold) {
            Typeface setfontstyle = Typeface.defaultFromStyle(Typeface.BOLD);
            mainview.setTypeface(setfontstyle);
            Toast.makeText(getApplicationContext(), "Bold", Toast.LENGTH_LONG).show();
        } else if (view == btn_italic) {
            Typeface setfontstyle = Typeface.defaultFromStyle(Typeface.ITALIC);
            mainview.setTypeface(setfontstyle);
            Toast.makeText(getApplicationContext(), "Italic", Toast.LENGTH_LONG).show();
        } else if (view == btn_normal) {
            Typeface setfontstyle = Typeface.defaultFromStyle(Typeface.NORMAL);
            mainview.setTypeface(setfontstyle);
            Toast.makeText(getApplicationContext(), "Normal", Toast.LENGTH_LONG).show();
        }
        else if (view == btn_size) {
            view.showContextMenu();
            Toast.makeText(getApplicationContext(), "Font Size", Toast.LENGTH_LONG).show();
        }
        else if(view == btn_new)
        {
            String appnametitle ="Notepoint";
            setTitle(appnametitle+" - New Document.txt");
            mainview.setText("");
            mainview.setHint("New Document");
            mainview.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            Toast.makeText(getApplicationContext(), "New Document", Toast.LENGTH_LONG).show();
        }
        else if(view == btn_copydata)
        {


            copydata = mainview.getText().toString();
            copydata = mainview.getText().toString();
            Toast.makeText(getApplicationContext(), "Copied Successfully !!", Toast.LENGTH_LONG).show();

        }
    }

    private  void savefile_save(String filename,String content){
        String fileName =filename+".txt";
        File file =new File(Environment.getExternalStorageDirectory().getAbsolutePath(), fileName);
        FileOutputStream fileOutputStream= null;
        try {
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(content.getBytes());
            fileOutputStream.close();
            Toast.makeText(this,"saved",Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this,"file not found",Toast.LENGTH_SHORT).show();
        }catch (IOException e){
            e.printStackTrace();
            Toast.makeText(this,"error saving",Toast.LENGTH_SHORT).show();
        }



    }
    private void pickImageFromGallery() {
        myfileIntent= new Intent(Intent.ACTION_PICK);
        myfileIntent.setType("image/*");
        startActivityForResult(myfileIntent,IMAGE_PICK_CODE);
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.setHeaderTitle("Select Font Size");
        menu.add(0, v.getId(), 0, "5+");
        menu.add(0, v.getId(), 0, "10+");
        menu.add(0, v.getId(), 0, "20+");
        menu.add(0, v.getId(), 0, "25+");
        menu.add(0, v.getId(), 0, "30+");
        menu.add(0, v.getId(), 0, "35+");
        menu.add(0, v.getId(), 0, "40+");
        menu.add(0, v.getId(), 0, "45+");
        menu.add(0, v.getId(), 0, "50");
    }
    @Override
    public boolean onContextItemSelected(MenuItem item){

        if (item.getTitle() == "5+") {
            mainview.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8);
        } else if (item.getTitle() == "10+") {
            mainview.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        } else if (item.getTitle() == "20+") {
            mainview.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        } else if (item.getTitle() == "25+") {
            mainview.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
        } else if (item.getTitle() == "30+") {
            mainview.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
        } else if (item.getTitle() == "35+") {
            mainview.setTextSize(TypedValue.COMPLEX_UNIT_SP, 33);
        } else if (item.getTitle() == "40+") {
            mainview.setTextSize(TypedValue.COMPLEX_UNIT_SP, 43);
        } else if (item.getTitle() == "45+") {
            mainview.setTextSize(TypedValue.COMPLEX_UNIT_SP, 48);
        } else if (item.getTitle() == "50") {
            mainview.setTextSize(TypedValue.COMPLEX_UNIT_SP, 50);
        } else {
            Toast.makeText(getApplicationContext(), "Something is Worng", Toast.LENGTH_LONG).show();
        }
        return true;
    }
}

