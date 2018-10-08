package com.example.abhishekrawat.questionstudy.Util;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.example.abhishekrawat.questionstudy.R;

public class FragmentUtil {

    /**
     * This method pops all the fragments which matches with the specified tag from back-stack and
     * replaces the given fragment.
     *
     * @param id
     * @param frgmt
     */
    public static void popBackStackAndReplace(FragmentActivity activity, Fragment frgmt, int id) {
        try {
            if (activity != null && !activity.isFinishing()) {
                FragmentManager manager = activity.getSupportFragmentManager();
                while (manager.getBackStackEntryCount() > 0) {

                    manager.popBackStackImmediate();

                }
                //manager.popBackStack(null, 0);
                manager.beginTransaction().replace(id, frgmt).commitAllowingStateLoss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Replaces fragment without adding it to the back stack .
     */
    public static void replaceFragment(FragmentActivity activity, Fragment fragment,
                                       int containerId) {
        if (!activity.isFinishing()) {
            FragmentManager manager = activity.getSupportFragmentManager();
            manager.beginTransaction().replace(containerId, fragment).commitAllowingStateLoss();
        }
    }

    /**
     * Replaces fragment without adding it to the back stack .
     */
    public static void replaceFragment(FragmentActivity activity, Fragment fragment,
                                       int containerId, String tag) {
        if (!activity.isFinishing()) {
            FragmentManager manager = activity.getSupportFragmentManager();
            manager.beginTransaction().replace(containerId, fragment, tag).commitAllowingStateLoss();
        }
    }


    /**
     * Replaces and adds the fragment to the back stack.
     */
    public static void replaceAndAddFragment(FragmentActivity activity, Fragment fragment, int containerId) {
        if (activity != null && !activity.isFinishing()) {
            FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
            transaction.replace(containerId, fragment);
            transaction.addToBackStack(null);
            transaction.commitAllowingStateLoss();
        }
    }

    /**
     * Replaces and adds the fragment to the back stack.
     */
    public static void addFragment(FragmentActivity activity, Fragment fragment, int containerId) {
        if (activity != null && !activity.isFinishing()) {
            FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
            transaction.add(containerId, fragment);
            transaction.addToBackStack(null);
            transaction.commitAllowingStateLoss();
        }
    }

    public static void replaceAndAddFragment(FragmentActivity activity, Fragment fragment, int containerId, String tag) {
        if (activity != null && !activity.isFinishing()) {
            FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
            transaction.replace(containerId, fragment, tag);
            transaction.addToBackStack(tag);
            transaction.commitAllowingStateLoss();
        }
    }

    /**
     * This removes current fragment and adds a new child fragment.
     */
    public static void replaceChildFragment(Fragment parentFragment, int containerId, Fragment childFragment) {
        if (parentFragment != null && !parentFragment.isDetached())
            parentFragment.getChildFragmentManager().beginTransaction().replace(containerId, childFragment).commitAllowingStateLoss();
    }

    /**
     * This adds current fragment to backstack and child fragment is added to
     * container
     */

    public static void replaceAndAddChildFragment(Fragment parentFragment, final int containerId, Fragment childFragment) {
        if (parentFragment != null && !parentFragment.isDetached()) {
            FragmentManager fragmentManager = parentFragment.getChildFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(containerId, childFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    public static void popChildBackStackImmediate(Fragment parentFragment) {
        if (parentFragment != null && !parentFragment.isDetached())
            parentFragment.getChildFragmentManager().popBackStackImmediate();
    }

    public static Fragment getCurrentFragment(FragmentActivity activity, int containerId) {
        if (activity != null)
            return activity.getSupportFragmentManager().findFragmentById(containerId);
        return null;
    }

    public static void popBackStackImmediate(FragmentActivity activity) {
        if (activity != null && !activity.isFinishing())
            activity.getSupportFragmentManager().popBackStackImmediate();
    }

    public static void removeFragment(FragmentActivity activity, int containerId) {
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        Fragment f = getCurrentFragment(activity, containerId);
        transaction.detach(f);
        transaction.commit();
    }

    public static void showDialogFragment(FragmentManager fragmentManager, DialogFragment dialogFragment, String tag) {
        if (fragmentManager.findFragmentByTag(tag) == null) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(dialogFragment, tag);
            transaction.commitAllowingStateLoss();
        }
    }

    public static void refreshFragment(FragmentActivity activity, int containerId) {
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        Fragment f = getCurrentFragment(activity, containerId);
        transaction.detach(f);
        transaction.attach(f);
        transaction.replace(containerId, f);
        transaction.commit();
    }

    public static int getBackStackEntryCount(FragmentActivity activity) {
        if (activity != null && !activity.isFinishing())
            return activity.getSupportFragmentManager().getBackStackEntryCount();
        return 0;
    }

    /**
     * This method pops all the fragments which matches with the specified tag from back-stack and
     * replaces the given fragment.
     *
     * @param containerId
     * @param fragment
     */
    public static void popBackStackAndAdd(FragmentActivity activity, Fragment fragment, int containerId) {
        try {
            if (activity != null && !activity.isFinishing()) {
                FragmentManager manager = activity.getSupportFragmentManager();
                while (manager.getBackStackEntryCount() > 0) {
                    manager.popBackStackImmediate();
                }
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(containerId, fragment);
                transaction.addToBackStack(null);
                transaction.commitAllowingStateLoss();
            }
        } catch (Exception e) {

        }
    }
}
