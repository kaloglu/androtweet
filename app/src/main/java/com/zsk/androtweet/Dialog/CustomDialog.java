package com.zsk.androtweet.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.zsk.androtweet.R;
import com.zsk.androtweet.enums.Enums;

/**
 * Created by kaloglu on 22/05/16.
 */
public class CustomDialog extends Dialog {
    private Enums.DialogType dialogType = null;
    private final Context context;
    private int titleID, descriptionID, rulesID, actionButtonID;
    private TextView title, description, rules;
    private Button okButton, actionButton;


    public CustomDialog(Context context, int titleStringID, int descriptionStringID, int rulesStringID, int actionButtonStringID) {
        super(context);
        this.context = context;
        this.titleID = titleStringID;
        this.descriptionID = descriptionStringID;
        this.rulesID = rulesStringID;
        this.actionButtonID = actionButtonStringID;
        initView();
    }

    private void initView() {

        this.setCancelable(false);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getWindow();
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        window.setLayout(DrawerLayout.LayoutParams.MATCH_PARENT, DrawerLayout.LayoutParams.MATCH_PARENT);
        this.setContentView(R.layout.custom_dialog_screen);
        this.setCanceledOnTouchOutside(false);

        title = (TextView) findViewById(R.id.dialog_title);
        title.setText(getString(titleID));

        description = (TextView) findViewById(R.id.dialog_description);
        description.setText(getString(descriptionID));
//        description.

        rules = (TextView) findViewById(R.id.dialog_rules);
        if (rulesID != 0) {
            rules.setText(getString(rulesID));
        }else {
            rules.setText("");
        }

        actionButton = (Button) findViewById(R.id.dialog_actionButton);
        if (actionButtonID != 0) {
            actionButton.setText(getString(actionButtonID));
        } else {
            actionButton.setVisibility(View.GONE);
        }

        okButton = (Button) findViewById(R.id.dialog_okButton);

        if (dialogType != null) {
            switch (dialogType) {
                case info:
                    okButton.setText(getString(R.string.close));
                    break;
                default:
                    break;
            }
        }

        try {
            this.show();
        } catch (Exception e) {
            // WindowManager$BadTokenException will be caught and the app would not display
            // the 'Force Close' message
        }

    }

    private String getString(int stringResourceID) {
        return context.getResources().getString(stringResourceID);
    }

    public interface okButtonClickListener {
        void onClick();
    }

    public interface actionButtonClickListener {
        void onClick();
    }

    public void initOkButtonClickListener(final okButtonClickListener listener) {

        okButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                listener.onClick();


            }
        });

    }

    public void initActionButtonClickListener(final actionButtonClickListener listener) {

        actionButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                listener.onClick();

            }
        });

    }
}
