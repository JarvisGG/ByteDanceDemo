package bizuikit;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;

/**
 * Window 全屏设置Utils
 * 好像叫 WindowUtils 更合适
 */
public class ActivityUtils {

    public static void fullScreen(Activity activity) {
        fullScreen(activity.getWindow());
    }

    /**
     * 通过设置全屏，设置状态栏透明
     *
     * @param window
     */
    public static void fullScreen(@NonNull Window window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //从 >= 21收紧到 >=23 ,因为没有23以下不能修改状态栏文字颜色
            //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
            View decorView = window.getDecorView();
            //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams attributes = window.getAttributes();
            int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            attributes.flags |= flagTranslucentStatus;
            window.setAttributes(attributes);
        }
    }

    /**
     * 底部虚拟导航键的高度
     * @param activity
     * @return
     */
    public static int getNavigationBarHeight(Activity activity) {
        if (activity != null) {
            Rect decorVisibleRect = new Rect();
            activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(decorVisibleRect);
            //获取屏幕的高度
            int screenHeight = activity.getWindow().getDecorView().getRootView().getHeight();
            return (screenHeight - decorVisibleRect.bottom);
        }
        return 0;
    }

    /**
     * 获取屏幕方向
     */
    public static int getScreenOrientation(Activity activity) {
        return activity.getResources().getConfiguration().orientation;
    }

    /**
     * 设置屏幕竖向
     */
    public static void setScreenVertical(Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    /**
     * 设置屏幕横向
     */
    public static void setScreenHorizontal(Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

}
