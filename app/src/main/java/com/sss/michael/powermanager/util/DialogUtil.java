package com.sss.michael.powermanager.util;

import android.content.Context;
import android.content.DialogInterface;

import com.sss.michael.powermanager.callback.OnDialogCallBack;

import androidx.appcompat.app.AlertDialog;

public class DialogUtil {

    public static void showDialog(Context context, AlertDialog dialog, String content, final OnDialogCallBack onDialogCallBack) {
        showDialog(context,dialog,"询问",content,onDialogCallBack);
    }

    public static void showDialog(Context context, AlertDialog dialog, String title, String content, final OnDialogCallBack onDialogCallBack) {
        showDialog(context,dialog,title,content,"取消","确定",onDialogCallBack);
    }
    public static void showDialog(Context context, AlertDialog dialog, String title, String content, String cancelBtnTitle, String confirmBtnTitle, final OnDialogCallBack onDialogCallBack) {
        if (dialog == null) {
            dialog = new AlertDialog.Builder(context)
                    .setTitle(title)//设置对话框的标题
                    .setMessage(content)//设置对话框的内容
                    //设置对话框的按钮
                    .setNegativeButton(cancelBtnTitle, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (onDialogCallBack != null) {
                                onDialogCallBack.onCancel();
                            }
                            dialog.dismiss();
                        }
                    })
                    .setPositiveButton(confirmBtnTitle, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (onDialogCallBack != null) {
                                onDialogCallBack.onConfirm();
                            }
                            dialog.dismiss();
                        }
                    }).create();
        }
        dialog.show();
    }
}
