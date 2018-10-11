package com.eudycontreras.weathersense.dialogs;

/**
 * Created by Eudy Contreras on 9/9/2016.
 */

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eudycontreras.weathersense.R;

public class DialogConfirmation extends Dialog {
    private Button yesButton;
    private Button noButton;
    private LinearLayout dialogLayout;
    private TextView dialogTitle;
    private TextView dialogMessage;
    private Runnable yesScript;
    private Runnable noScript;
    private DialogEventListener listener;
    private Context context;
    private String message;
    private int titleBarColor;

    public DialogConfirmation(Activity context, int customTheme) {
        super(context, customTheme);
        this.context = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_confirmation);
        initComponents();
        initControls();
    }

    public DialogConfirmation(Activity context, int customTheme, String errorMessage, int titleBarColor) {
        super(context, customTheme);
        this.context = context;
        this.message = errorMessage;
        this.titleBarColor = titleBarColor;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_confirmation);
        initComponents();
        initControls();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(dialogLayout,
                PropertyValuesHolder.ofFloat("scaleX", 0.3f, 1.0f),
                PropertyValuesHolder.ofFloat("scaleY", 0.3f, 1.0f),
                PropertyValuesHolder.ofFloat("alpha", 0.0f, 1.0f));
        animation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
            }

            @Override
            public void onAnimationStart(Animator animation) {
                listener.onDialogShown();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.setDuration(500);
        animation.start();

    }

    private void initComponents() {
        dialogMessage = (TextView) findViewById(R.id.dialog_confirm_message);
        yesButton = (Button) findViewById(R.id.dialog_confirm_yes);
        noButton = (Button) findViewById(R.id.dialog_confirm_no);
        dialogLayout = (LinearLayout) findViewById(R.id.dialog_confirm);

        yesButton.setOnClickListener(v -> {
            final View decorView = getWindow().getDecorView();
            ObjectAnimator alphaDown = ObjectAnimator.ofPropertyValuesHolder(decorView,
                    PropertyValuesHolder.ofFloat("alpha", 1.0f, 0.0f)
            );
            alphaDown.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    dismiss();
                    if(yesScript !=null){
                        yesScript.run();
                    }
                }

                @Override
                public void onAnimationStart(Animator animation) {
                    listener.onDialogHidden();
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
            alphaDown.setDuration(350);
            alphaDown.start();
        });
        noButton.setOnClickListener(v -> {
            final View decorView = getWindow().getDecorView();
            ObjectAnimator alphaDown = ObjectAnimator.ofPropertyValuesHolder(decorView,
                    PropertyValuesHolder.ofFloat("alpha", 1.0f, 0.0f)
            );
            alphaDown.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    dismiss();
                    if(noScript !=null){
                        noScript.run();
                    }
                }

                @Override
                public void onAnimationStart(Animator animation) {
                    listener.onDialogHidden();
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
            alphaDown.setDuration(350);
            alphaDown.start();
        });
    }

    protected void setStatusBarTranslucent(boolean makeTranslucent) {
        if (makeTranslucent) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    public void setListener(DialogEventListener listener){
        this.listener = listener;
    }

    public void setYesScript(Runnable yesScript){
        this.yesScript = yesScript;
    }

    public void setNoScript(Runnable noScript){
        this.noScript = noScript;
    }

    private void initControls() {
        if(titleBarColor!=-1) {
            dialogTitle.setBackgroundColor(titleBarColor);
        }
        dialogMessage.setText(message);
    }


    @Override
    public void onBackPressed() {
        listener.onDialogHidden();
        super.onBackPressed();
    }


    public interface DialogEventListener{
        void onDialogShown();
        void onDialogHidden();
    }
}
