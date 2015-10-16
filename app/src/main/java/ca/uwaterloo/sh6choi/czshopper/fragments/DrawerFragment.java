package ca.uwaterloo.sh6choi.czshopper.fragments;

/**
 * Created by Samson on 2015-10-15.
 */
public interface DrawerFragment {

    String getFragmentTag();

    int getTitleStringResId();

    boolean shouldShowUp();

    boolean shouldAddToBackstack();

    boolean onBackPressed();

}

