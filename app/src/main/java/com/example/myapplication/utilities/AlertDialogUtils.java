package com.example.myapplication.utilities;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.example.myapplication.R;

public class AlertDialogUtils {

    public static void showAlertDialog(Context context, Events id, DialogInterface.OnDismissListener onDismissListener) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        String alertDialogTitle = null;
        String alertDialogMessage = null;
        switch (id) {
            case SERVER_REGISTRATION_ERROR:
                alertDialogTitle = context.getString(R.string.todo_registration_error_message_title);
                alertDialogMessage = context.getString(R.string.todo_registration_error_message_text);
                break;
            case SERVER_LOGIN_ERROR:
                alertDialogTitle = context.getString(R.string.activity_todo_login_error_dialog_title);
                alertDialogMessage = context.getString(R.string.activity_todo_login_error_dialog_message);
                break;
            case SERVER_INIT_ERROR:
                alertDialogTitle = context.getString(R.string.activity_todo_list_server_init_error_dialog_title);
                alertDialogMessage = context.getString(R.string.activity_todo_list_firebase_init_error_dialog_message);
                break;
            case NETWORK_ERROR:
                alertDialogTitle = context.getString(R.string.activity_todo_text_note_network_error_dialog_title);
                alertDialogMessage = context.getString(R.string.activity_todo_text_note_network_error_dialog_message);
                break;
            case INVALID_INPUT_ERROR:
                alertDialogTitle = context.getString(R.string.activity_todo_text_note_dialog_title);
                alertDialogMessage = context.getString(R.string.activity_todo_text_note_dialog_message);
                break;
            case SENDING_ERROR:
                alertDialogTitle = context.getString(R.string.activity_todo_text_sending_message_error_dialog_title);
                alertDialogMessage = context.getString(R.string.activity_todo_text_sending_message_error_dialog_message);
                break;
            case GET_TODO_LIST_ERROR:
                alertDialogTitle = context.getString(R.string.activity_todo_text_note_dialog_title);
                alertDialogMessage = context.getString(R.string.activity_todo_list_get_todo_list_error_dialog_message);
        }
        alertDialog.setTitle(alertDialogTitle);
        alertDialog.setMessage(alertDialogMessage);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, context.getString(R.string.activity_todo_text_note_dialog_button_tittle),
                (dialogInterface, i) -> dialogInterface.dismiss()
        );
        alertDialog.setOnDismissListener(onDismissListener);
        alertDialog.show();
    }

    public enum Events {
        NETWORK_ERROR, INVALID_INPUT_ERROR, SENDING_ERROR, GET_TODO_LIST_ERROR, SERVER_INIT_ERROR, TODO_ADDED, TODO_EDITED, SERVER_LOGIN_ERROR,
        SERVER_REGISTRATION_ERROR
    }
}
