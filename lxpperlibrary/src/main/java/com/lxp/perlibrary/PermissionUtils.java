package com.lxp.perlibrary;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

/**
 * 作者：lixiaopeng on 2017/9/1 0001.
 * 邮箱：lixiaopeng186@163.com
 * 描述：
 */

public class PermissionUtils {

    private static PermissionUtils permissionUtils = null;
    private static PermissionHelper helper = null;
    private String[] permission;//权限列表
    private String text;//只有直接弹出dialog的时候才用
    private PermissionHelper.OnPermission onPermission;//回调
    private IsAuthor author = IsAuthor.DEFAULT;//默认

    public static final int ALL_PERMISSION = 0;//所有权限
    public static final int REQUEST_PERMISSION = 1;//请求权限
    public PermissionUtils(){
    }
    public static PermissionUtils getInstance(){
        if (permissionUtils == null){
            synchronized (PermissionUtils.class){
                if (permissionUtils == null){
                    permissionUtils = new PermissionUtils();
                    helper = new PermissionHelper();
                }
            }
        }
        return permissionUtils;
    }
    /**
     * 请求应用所用到的所有权限(初始化)
     */
    public boolean applyInitPermission(Activity activity){
        if (!helper.isSdkM()){

            ActivityCompat.requestPermissions(activity,
                    new String[]{
//                            Manifest.permission.ACCESS_COARSE_LOCATION,//定位权限
//                            Manifest.permission.RECORD_AUDIO, //麦克风权限
//                            Manifest.permission.WRITE_EXTERNAL_STORAGE,//sd卡写
//                        Manifest.permission.CAMERA,//相机

//                            Manifest.permission.SEND_SMS, //发送短信
                            Manifest.permission.CALL_PHONE, //拨打电话
                            Manifest.permission.READ_CONTACTS //读取通讯录
                    }, ALL_PERMISSION);
            return true;
        }
        return false;

    }



    /**
     * 检测权限操作
     */
    //申请权限
    public void applyPermission(Context activity, PermissionHelper.OnPermission onPermission){

        judegPermission(activity,onPermission);
    }



    public PermissionUtils setPermission(String[] permission){
        if (this.permission != null){
            this.permission = null;
        }
        if (permission != null && permission.length > 0){
            this.permission = permission;
        }
        return this;
    }

    public PermissionUtils setText(String text){
        this.text = text;
        return this;
    }
    public PermissionUtils setOnPermission(PermissionHelper.OnPermission onPermission){
        this.onPermission = onPermission;
        return this;
    }
    public PermissionUtils setIsAuthor(IsAuthor isAuthor){
        this.author = isAuthor;
        return this;
    }



    /**
     * 初始化权限为拒绝的时候，弹出提示框提示用户原因
     * @param activity
     * @param permission
     */
    public void showDialog(Activity activity,String ...permission){
        for (int i = 0; i < permission.length; i++) {
            //没有授权才弹出
            if (!isAuthorized(activity,permission[i])) {
                Log.i("test","shold??"+helper.shouldPermission(activity,permission[i]));
                if (helper.shouldPermission(activity, permission[i])) {
                    //请求？
//                    ActivityCompat.requestPermissions(activity,new String[]{permission[i]} ,REQUEST_PERMISSION);
                    authorInitPermission(activity,permission[i]);
                    return;
                } else {
                    authorInitPermission(activity, permission[i]);
                    return;
                }
            }

        }
    }
    /**
     * 初始化权限拒绝提示用户
     */
    private void authorInitPermission(Activity activity,String permission){
        String text=null;
        switch (permission){
            case Manifest.permission.ACCESS_COARSE_LOCATION://定位
                text = getString(activity,R.string.permission_refuse_location);
                break;
            case Manifest.permission.RECORD_AUDIO://麦克风
                text = getString(activity,R.string.permission_audio);
                break;
            case Manifest.permission.WRITE_EXTERNAL_STORAGE://sd卡权限
                text = getString(activity,R.string.permission_sd);
                break;
            case Manifest.permission.CAMERA://相机
                text = getString(activity,R.string.permission_photo);
                break;
            case Manifest.permission.SEND_SMS://发送短信
                text = getString(activity,R.string.permission_msn);
                break;
            case Manifest.permission.CALL_PHONE://拨打电话
                text = getString(activity,R.string.permission_call);
                break;
            case Manifest.permission.READ_CONTACTS://读取通讯录
                text = getString(activity,R.string.permission_read_contants);
                break;
        }
        switch (author){
            case INSPECT://请求的权限
                showDialogView(activity,text,true);
                break;
            case DEFAULT://直接弹出的dialog
                showDialogView(activity,text,false);
                break;
        }

    }



    private void judegPermission(Context activity,PermissionHelper.OnPermission onPermission){

        if (!helper.isSdkM()){//sdk是6.0以上系统
            Log.i("test","看看author:"+author);
            if (!isAuthorized(activity,permission)){
                //权限还没有授予

                switch (author){
                    case DEFAULT:
                        showDialog((Activity) activity,permission);
                        break;
                    case INSPECT://请求授权
                        //检查授权还没有写，现在使用需要在activity中实现onRequestPermissionsResult方法，后期有时间会优化
                        ActivityCompat.requestPermissions((Activity) activity,
                                permission, REQUEST_PERMISSION);
                        break;
                }



            } else {
                //权限已经授予
                onPermission.agreePermission();
            }
        }else {
            //5.0以下不操作,想当于已经授权
            onPermission.agreePermission();
        }
    }

    /**
     * 检测使用有该权限false是没有，需要授权
     */
    public boolean isAuthorized(Context activity,String ...permission){
        for (int i = 0; i < permission.length; i++) {
            if (!isAuthorized(activity,permission[i])){
                return false;
            }
        }
        return true;
    }
    public boolean isAuthorized(Context activity,String permision){
        Log.e("test","授权："+(ContextCompat.checkSelfPermission(activity,permision))+"---"+permision);
        if (ContextCompat.checkSelfPermission(activity,permision) != PackageManager.PERMISSION_GRANTED){
            return false;//如果为false的时候，证明条件不否和，权限还没有授权
        }
        return true;
    }

    public boolean isAuthorized(int ...permission){
        if (permission == null || permission.length == 0){
            return false;
        }

        for (int i = 0; i < permission.length; i++) {
            if (permission[i]  != PackageManager.PERMISSION_GRANTED){
                return false;//如果为false的时候，证明条件不否和，权限还没有授权
            }
        }
        return true;
    }
    /**
     * 没有授权弹出的dialog
     * @param activity
     * @param text
     */
    private void showDialogView(final Activity activity,String text,final boolean flag){
        PermissionDialog.getInstance().setOnSignUpCancle(new PermissionDialog.SignUpCancle() {
            @Override
            public void cancle(View v) {
                if (flag) {
                    activity.finish();
                }
            }

            @Override
            public void confirm(View v) {
                helper.goSetting(activity);
                if (flag) {
                    activity.finish();
                }
            }
        }).show(activity, text,getString(activity,R.string.permission_setting),
                getString(activity,R.string.cancle));
    }
    private  String getString(Activity activity,int res){
        return activity.getString(res);
    }
}
