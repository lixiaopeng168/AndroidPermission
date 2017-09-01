package com.lxp.perlibrary;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * 作者：lixiaopeng on 2017/9/1 0001.
 * 邮箱：lixiaopeng186@163.com
 * 描述：
 */

public class PermissionDialog   {
    private static PermissionDialog PermissionDialog = null;
    private static Dialog dialog;
    private TextView textView;
    private TextView ok;
    private TextView cancel;

    private String text;
    private int textColor;
    private int okColor;
    private SignUpCancle signUpCancle;
    private PermissionDialog (){}

    public static PermissionDialog getInstance(){

        if (PermissionDialog == null){
            synchronized (PermissionDialog.class) {
                if (PermissionDialog == null) {
                    PermissionDialog = new PermissionDialog();
                }
            }
        }
        return PermissionDialog;
    }


    public void show(Context context,String text,String leftBtn,String rigntBtn){
        this.text = text;
        initDialog(context,leftBtn,rigntBtn);

    }

    private void setView() {
        textView.setText(text);
        if (okColor != 0){
            ok.setTextColor(okColor);
        }
        if (textColor != 0){
            textView.setTextColor(textColor);
            cancel.setTextColor(textColor);
        }
    }

    private void initDialog(Context context,String leftBtn,String rigntBtn) {
        dialog = new Dialog(context, R.style.dialog);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.gravity = Gravity.CENTER;
//        attributes.width = (int) (UIUtils.getWindowWidth(context) * 0.5);
//        attributes.height = (int) (UIUtils.getWindowHeight(context) * 0.3);
//        attributes.alpha =  0.7f;

        dialog.setContentView(inView(context, leftBtn, rigntBtn));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                clear();
            }
        });
        window.setAttributes(attributes);

        setView();

        dialog.show();


    }

    private View inView(Context context, String leftBtn, String rigntBtn){
        View view = LayoutInflater.from(context).inflate(R.layout.dialog, null);
        textView = (TextView) view.findViewById(R.id.dialogSingUpText);
        ok = (TextView) view.findViewById( R.id.dialogSingUpConfirm);
        cancel = (TextView) view.findViewById( R.id.dialogSingUpCancel);
        ok.setText(leftBtn);
        cancel.setText(rigntBtn);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                    if (signUpCancle != null){
                        signUpCancle.confirm(v);
                        signUpCancle = null;
                    }

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if (signUpCancle != null){

                        signUpCancle.cancle(v);
                        signUpCancle = null;
                    }

                dialog.dismiss();

            }
        });
        return view;
    }

    public PermissionDialog setText(String text){
        this.text = text;
        return this;
    }

    public PermissionDialog setTextColor(int textColor){
        this.textColor = textColor;
        return this;
    }
    public PermissionDialog setOkColor(int okColor){
        this.okColor = okColor;
        return this;
    }



    public PermissionDialog setOnSignUpCancle(SignUpCancle signUpCancle){
        this.signUpCancle = signUpCancle;
        return this;
    }

    public void clear(){
        textColor = 0;
        okColor = 0;
        text = null;
        textView = null;
        ok = null;
        cancel = null;
        dialog = null;
    }
    public interface SignUpCancle{
        void cancle(View v);
        void confirm(View v);

    }
}
