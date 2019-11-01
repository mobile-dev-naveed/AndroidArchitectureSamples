package com.naveed.samples.helper.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import android.view.inputmethod.InputMethodManager;
import android.widget.Spinner;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import com.naveed.samples.R;
/**
 * Created by NaveedAli on 1/24/2017.
 */


public abstract class BaseActivity extends AppCompatActivity implements DialogInterface.OnDismissListener {


    public BaseFragment mCurrentFragment;
    protected BaseFragment previousFragment;
    protected ProgressDialog mProgressDialog;
    protected String lang = "en";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        EventBus.getDefault().register(this);
        //Log.i("FCM toked","token: "+ FirebaseInstanceId.getInstance().getToken());

       /* if (!(this instanceof Splash)) {
            String deviceToken = FirebaseInstanceId.getInstance().getToken();
            if (deviceToken == null || deviceToken.isEmpty()) return;
            if (!AppSharedPreferences.getInstance(this).isSyncedDeviceToken(deviceToken)) {
                new RegisterDeviceAsync(this).execute(FirebaseInstanceId.getInstance().getToken());
            }
        }*/

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
        dismissProgress();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    public void launchActivity(Class<?> className) {
        launchActivity(className, null);
    }

    public void launchActivity(Class<?> className, Bundle bundle) {
        Intent i = new Intent(this, className);
        if (bundle != null)
            i.putExtra("bundle", bundle);
        startActivity(i);
    }

    public void launchActivityForResult(Class<?> className, Bundle bundle, int id) {
        Intent i = new Intent(this, className);
        if (bundle != null)
            i.putExtra("bundle", bundle);
        startActivityForResult(i, id);
    }

    public void addFragment(BaseFragment fragment, int containerId) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        fragmentTransaction.add(containerId, fragment);
        fragmentTransaction.addToBackStack(fragment.getFragmentTag());
        fragmentTransaction.commit();
        previousFragment = mCurrentFragment;
        mCurrentFragment = fragment;
    }

    public void changeFragment(BaseFragment fragment, int containerId) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        fragmentTransaction.replace(containerId, fragment);
        fragmentTransaction.commit();
        mCurrentFragment = fragment;
    }

   /*  public void changeFragmentWithStack(BaseFragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }*/


    public void changeFragment(BaseFragment fragment, boolean addToStack, int childContainerId) {
        changeFragment(fragment, childContainerId);
    }

    public void showProgressDialog(String message) {
        if (mProgressDialog != null) {
            if (mProgressDialog.isShowing())
                return;
            else {
                mProgressDialog.setMessage(message);
                mProgressDialog.show();
                mProgressDialog.setOnDismissListener(this);

            }
        } else {
            mProgressDialog = new ProgressDialog(this, R.style.AppCompatAlertDialogStyle);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage(message);
            mProgressDialog.show();
        }

    }

    public void showProgressDialog(int stringResId) {
        showProgressDialog(getString(stringResId));
    }

    public void dismissProgress() {
        if (mProgressDialog == null || !mProgressDialog.isShowing())
            return;
        mProgressDialog.dismiss();
    }

    public void showToast(int stringId) {
        Toast.makeText(this, stringId, Toast.LENGTH_SHORT).show();
    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {

    }

    @Subscribe
    public void onEvent(String event) {

    }

   // private SingleButtonAlert singleButtonAlert = null;

//    public void showAlertDialog(String message, String btnText) {
//        showAlertDialog(-1, message, btnText);
//    }
//
//    public void showAlertDialog(int id, String message, String btnText) {
//        if (singleButtonAlert != null) {
//            try {
//                singleButtonAlert.dismiss();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        singleButtonAlert = SingleButtonAlert.newInstance(id, message, btnText);
//        singleButtonAlert.show(getSupportFragmentManager(), null);
//        singleButtonAlert.setAlertActionListener(this);
//    }
//
//    @Override
//    public void onActionDone() {
//
//    }
//
//    @Override
//    public void onActionDone(int actionId) {
//
//    }

    public void spinnerError(Spinner spinner) {

        if (spinner == null)
            return;
        spinner.setBackgroundResource(R.drawable.spinner_error_background);
        spinner.requestFocus();
    }


    protected void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


}
