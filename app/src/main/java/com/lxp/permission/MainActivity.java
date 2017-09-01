package com.lxp.permission;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.lxp.perlibrary.IsAuthor;
import com.lxp.perlibrary.PermissionHelper;
import com.lxp.perlibrary.PermissionUtils;

public class MainActivity extends AppCompatActivity {
    private String[] per = {Manifest.permission.SEND_SMS,Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private String[] err = {Manifest.permission.RECORD_AUDIO};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.init).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PermissionUtils.getInstance().applyInitPermission(MainActivity.this);
            }
        });
        findViewById(R.id.apply).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PermissionUtils.getInstance().setIsAuthor(IsAuthor.INSPECT).setPermission(per)
                .applyPermission(MainActivity.this, new PermissionHelper.OnPermission() {
                    @Override
                    public void agreePermission() {
                        show("授权成功");
                    }
                });
            }
        });
        findViewById(R.id.refuse).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PermissionUtils.getInstance().setIsAuthor(IsAuthor.DEFAULT).setPermission(err)
                        .applyPermission(MainActivity.this, new PermissionHelper.OnPermission() {
                            @Override
                            public void agreePermission() {
                                show("授权成功");
                            }
                        });
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionUtils.REQUEST_PERMISSION){
            if (!PermissionUtils.getInstance().isAuthorized(grantResults)){
                //弹出dialog
                PermissionUtils.getInstance().showDialog(MainActivity.this,permissions);
            }else {
                show("授权成功，执行");
            }
        }else if (requestCode == PermissionUtils.ALL_PERMISSION){

        }
    }

    private void show(String text){
        Toast.makeText(this,text,Toast.LENGTH_SHORT).show();
    }
}
