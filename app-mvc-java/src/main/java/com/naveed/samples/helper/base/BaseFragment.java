package com.naveed.samples.helper.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.TypedValue;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import butterknife.Unbinder;


/**
 * Created by NaveedAli on 25/2/2017.
 */

public abstract class BaseFragment extends Fragment {


    protected String tag;
    protected Unbinder mUnbinder;
    private String title = "";


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EventBus.getDefault().register(this);
        //setTag();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        changeButtonsColor();
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
        //EventBus.getDefault().unregister(this);
        dismissProgress();

    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
        if (mUnbinder != null)
            mUnbinder.unbind();

    }

    protected void launchActivity(Class<?> activityClass) {
        ((BaseActivity) getActivity()).launchActivity(activityClass);
    }

    protected void launchActivity(Class<?> activityClass, Bundle bundle) {
        ((BaseActivity) getActivity()).launchActivity(activityClass, bundle);
    }

    protected void launchActivityForResult(Class<?> activityClass, Bundle bundle, int requestCode) {
        ((BaseActivity) getActivity()).launchActivityForResult(activityClass, bundle, requestCode);
    }

    protected void addFragment(BaseFragment fragment, int containerId) {
        //FunctionHelper.hideKeyboard(getActivity());
        ((BaseActivity) getActivity()).addFragment(fragment, containerId);
    }


    protected void changeFragment(BaseFragment fragment, int containerId) {
        //FunctionHelper.hideKeyboard(getActivity());
        ((BaseActivity) getActivity()).changeFragment(fragment, containerId);
    }

    protected void changeFragment(BaseFragment fragment, boolean addToStack, int containerId) {
        //FunctionHelper.hideKeyboard(getActivity());
        ((BaseActivity) getActivity()).changeFragment(fragment, addToStack, containerId);
    }

    protected void changeChildFragment(BaseFragment fragment, int childContainerId) {
        //FunctionHelper.hideKeyboard(getActivity());
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        fragmentTransaction.replace(childContainerId, fragment);
        fragmentTransaction.commit();
    }

    protected void addChildFragment(BaseFragment fragment, int childContainerId) {
        //FunctionHelper.hideKeyboard(getActivity());
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        fragmentTransaction.add(childContainerId, fragment);
        fragmentTransaction.addToBackStack(fragment.getFragmentTag());
        fragmentTransaction.commit();
    }


    //public abstract void setTag();

    public String getFragmentTag() {
        return tag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

/*
    protected  void showSnackBar(String message){
        Snackbar.make(getActivity().findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
                .setActionTextColor(Color.RED)
                .show();
    }
*/

    @Subscribe
    public void callBack(String value) {

    }


    public void onBackPressed() {
        getActivity().onBackPressed();
    }

    protected void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    protected void showToast(int messRId) {
        Toast.makeText(getContext(), messRId, Toast.LENGTH_SHORT).show();
    }

    public void showProgressDialog(String message) {
        ((BaseActivity) getActivity()).showProgressDialog(message);

    }

    public void showProgressDialog(int messageResId) {
        ((BaseActivity) getActivity()).showProgressDialog(getString(messageResId));

    }

    public void dismissProgress() {
        ((BaseActivity) getActivity()).dismissProgress();
    }



    protected void changeButtonsColor() {
    }


    protected void showAlert(int id, String message, String btnText) {
        //((BaseActivity) getActivity()).showAlertDialog(id, message, btnText);
    }

    protected void spinnerError(Spinner spinner) {
        ((BaseActivity) getActivity()).spinnerError(spinner);
    }

    public void onAlertDismiss(int actionId) {
    }
}