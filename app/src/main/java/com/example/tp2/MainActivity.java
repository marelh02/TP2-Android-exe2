package com.example.tp2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button idb;
    Button detailsb;
    Button callb;
    TextView tv;
    int MY_REQUEST_CODE=77;
    int MY_PERMISSIONS_REQUEST_READ_CONTACTS=88;
    public String cname,cId,cNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        idb = (Button) findViewById(R.id.contactid);
        detailsb = (Button) findViewById(R.id.detailscontact);
        callb = (Button) findViewById(R.id.call);
        tv=(TextView) findViewById(R.id.hint);

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        }

    }

    public void contactIdHandler(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts/people"));
        startActivityForResult(intent,MY_REQUEST_CODE);

    }

    @SuppressLint("Range")
    public void contactDetailHandler(View v) {
        String uriContact=detailsb.getText().toString();
        Cursor cursor = getContentResolver().query(Uri.parse(uriContact), null, null, null, null);

        cname=cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        cId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
        Cursor cursorPhone =
                getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                                ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                                ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,
                        new String[]{cId},
                        null);
        cNum=cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

        String dial="Name: "+cname+"\nContact ID: "+cId+"\nPhone number: "+cNum;
        tv.setText(dial);
        callb.setEnabled(true);
    }

    public void callHandler(View v) {
        Intent myActivity2 = new Intent(
                Intent.ACTION_CALL,
                Uri.parse("tel:"+cNum));
        startActivity(myActivity2);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MY_REQUEST_CODE) {
            if (resultCode == RESULT_CANCELED) {
                tv.setText("Opération annulée");
            }else{
                tv.setText(data.getDataString());
                detailsb.setEnabled(true);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 88: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the contacts-related task you need to do.
                    Toast.makeText(this, "You can access contacts man",Toast.LENGTH_SHORT).show();
                } else {
                    // permission denied, boo! Disable the functionality that depends on this permission.
                }
                return;
            }
        }
    }


}