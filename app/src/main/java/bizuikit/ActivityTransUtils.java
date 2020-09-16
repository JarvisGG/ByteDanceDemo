package bizuikit;

import android.app.Activity;

import com.example.bytedancedemo.R;


public class ActivityTransUtils {


    //no animation
    public static final int TYPE_ACTIVITY_TRANS_NONE = 0;

    //normal style with animation
    public static final int TYPE_ACTIVITY_TRANS_NORMAL = 1;

    public static final int TYPE_ACTIVITY_TRANS_FADE = 2;

    /**
     * 纵向
     */
    public static final int TYPE_ACTIVITY_TRANS_VERTICAL = 4;


    /**
     * @param activity
     * @param type
     */
    public static void startActivityAnim(Activity activity, int type) {
        if (activity == null) {
            return;
        }
        int in = 0;
        int out = 0;
        switch (type) {
            case TYPE_ACTIVITY_TRANS_NORMAL:
                in = R.anim.slide_in_right;
                out = R.anim.out_still;
                break;
            case TYPE_ACTIVITY_TRANS_FADE:
                in = R.anim.fade_in_dialog;
                out = 0;
                break;
            case TYPE_ACTIVITY_TRANS_VERTICAL:
                in = R.anim.slide_in_bottom;
                out = R.anim.out_still;
                break;
        }
        activity.overridePendingTransition(in, out);
    }

    public static void finishActivityAnim(Activity activity, int type) {
        if (activity == null) {
            return;
        }
        int in = 0;
        int out = 0;
        switch (type) {
            case TYPE_ACTIVITY_TRANS_NORMAL:
                in = R.anim.in_still;
                out = R.anim.slide_out_right;
                break;
            case TYPE_ACTIVITY_TRANS_FADE:
                in = 0;
                out = R.anim.fade_out_dialog;
                break;
            case TYPE_ACTIVITY_TRANS_VERTICAL:
                in = R.anim.in_still;
                out = R.anim.slide_out_bottom;
                break;
        }
            activity.overridePendingTransition(in, out);
    }
}

