package com.naveed.samples.helper.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

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




    protected void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


}
