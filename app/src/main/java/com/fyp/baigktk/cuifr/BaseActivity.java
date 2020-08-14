package com.fyp.baigktk.cuifr;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.transition.Slide;
import android.view.Gravity;
import android.view.Window;

import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class BaseActivity extends AppCompatActivity {

    private NavBar navBar;
    private ProgressDialog mProgressDialog;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage("Loading...");
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public void setNavBar(Activity activity){
        this.navBar = NavBar.getInstance();
        navBar.initializeTab(activity);



        navBar.tabListener(activity);
    }

    public boolean setupWindowAnimation(Intent intent, Window window, Activity activity){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Slide slide = new Slide(Gravity.LEFT);
            slide.setDuration(1000);
            window.setExitTransition(slide);
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(activity).toBundle());
            return true;
        }
        else{
            return false;
        }
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }


}
