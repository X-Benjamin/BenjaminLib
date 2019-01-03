package com.benjamin.utils.eighteen;

/**
 * Created by Angus on 2016/8/15.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.benjamin.R;
import com.benjamin.app.AppConfig;


public class ToastUtils {

    public static String DEFAULT_BG_COLOR = "#D8000000";
    public static int DEFAULT_CORNER_RADIUS = 10;
    public static int DEFAULT_TEXT_SP = 16;
    public static int DEFAULT_X_OFF = 0;
    public static int DEFAULT_Y_OFF = 325;
    private static Toast toast;
    private static Handler handler = new Handler();
    private static Context context = UtilsInitializer.getContext();
    private static boolean isJumpWhenMore = true;

    private static ToastShower toastShowTask = new ToastShower();

    private static class ToastShower implements Runnable {

        private final long BASE_TIME = 500;

        private long timeMillis;

        private long count;

        void setTimeMillis(long timeMillis) {
            if (timeMillis < 2000) {
                this.timeMillis = 0;
            } else {
                this.timeMillis = timeMillis - 2000;
            }
            this.count = 0;
        }

        @Override
        public void run() {
            if (count <= timeMillis) {
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.show();
                handler.postDelayed(this, BASE_TIME);
                count += BASE_TIME;
            }
        }
    }

    public static void setIsJumpWhenMore(boolean isJumpWhenMore) {
        ToastUtils.isJumpWhenMore = isJumpWhenMore;
    }

    /**
     * 弹出较长时间提示信息
     *
     * @param msg 要显示的信息
     */
    public static void showShort(String msg) {
        show(msg, 2500, true);
    }

    /**
     * 弹出较长时间提示信息
     *
     * @param msg 要显示的信息
     */
    public static void showLong(String msg) {
        show(msg, 3500, true);
    }

    /**
     * 弹出较长时间提示信息
     *
     * @param msg 要显示的信息
     */
    public static void showShortNormal(String msg) {
        show(msg, 2500, false);
    }

    /**
     * 弹出较长时间提示信息
     *
     * @param msg 要显示的信息
     */
    public static void showLongNormal(String msg) {
        show(msg, 3500, false);
    }

    /**
     * 弹出较长时间提示信息
     *
     * @param msg 要显示的信息
     */
    public static void show(final String msg, final long timeMillis, boolean isJumpWhenMore) {
        if (isJumpWhenMore) {
            buildToast(context, msg);
            showToast(timeMillis);
        } else {
            int duration;
            if (timeMillis == 2500) {
                duration = Toast.LENGTH_SHORT;
            } else {
                duration = Toast.LENGTH_LONG;
            }
            handler.removeCallbacksAndMessages(null);
            buildToast(context, msg, duration).show();
        }
    }

    private static void showToast(long timeMillis) {
        toastShowTask.setTimeMillis(timeMillis);
        handler.removeCallbacksAndMessages(null);
        handler.post(toastShowTask);
    }

    /**
     * 构造Toast
     *
     * @param context 上下文
     * @return
     */
    public static Toast buildToast(Context context, String msg) {
        return buildToast(context, msg, Toast.LENGTH_SHORT);
    }

    /**
     * 构造Toast
     *
     * @param context 上下文
     * @return
     */
    public static Toast buildToast(Context context, String msg, int duration) {
        return buildToast(context, msg, duration, DEFAULT_BG_COLOR);
    }

    /**
     * 构造Toast
     *
     * @param context 上下文
     * @return
     */
    public static Toast buildToast(Context context, String msg, int duration, String bgColor) {
        return buildToast(context, msg, duration, bgColor, DEFAULT_TEXT_SP);
    }


    /**
     * 构造Toast
     *
     * @param context  上下文
     * @param msg      消息
     * @param duration 显示时间
     * @param bgColor  背景颜色
     * @return
     */
    public static Toast buildToast(Context context, String msg, int duration, String bgColor, int textSp) {
        return buildToast(context, msg, duration, bgColor, textSp, DEFAULT_CORNER_RADIUS);
    }


    /**
     * 构造Toast
     *
     * @param context  上下文
     * @param msg      消息
     * @param duration 显示时间
     * @param bgColor  背景颜色
     * @param textSp   文字大小
     * @return
     */
    public static Toast buildToast(Context context, String msg, int duration, String bgColor, int textSp, int cornerRadius) {
        return buildToast(context, msg, duration, bgColor, textSp, cornerRadius, DEFAULT_X_OFF);
    }

    /**
     * 构造Toast
     *
     * @param context  上下文
     * @param msg      消息
     * @param duration 显示时间
     * @param bgColor  背景颜色
     * @param textSp   文字大小
     * @return
     */
    public static Toast buildToast(Context context, String msg, int duration, String bgColor, int textSp, int cornerRadius, int xOff) {
        return buildToast(context, msg, duration, bgColor, textSp, cornerRadius, xOff, DEFAULT_Y_OFF);
    }

    /**
     * 构造Toast
     *
     * @param context      上下文
     * @param msg          消息
     * @param duration     显示时间
     * @param bgColor      背景颜色
     * @param textSp       文字大小
     * @param cornerRadius 四边圆角弧度
     * @return
     */
    public static Toast buildToast(Context context, String msg, int duration, String bgColor, int textSp, int cornerRadius, int xOff, int yOff) {
        if (null == toast) {
            toast = Toast.makeText(context, null, duration);
        }
        toast.setGravity(Gravity.BOTTOM, xOff, yOff);
        //设置Toast文字
        TextView tv = new TextView(context);
        float scale = context.getResources().getDisplayMetrics().density;
        int dpPadding = (int) (10 * scale + 0.5f);
        tv.setPadding(dpPadding, dpPadding, dpPadding, dpPadding);
        tv.setGravity(Gravity.CENTER);
        tv.setText(msg);
        tv.setTextColor(Color.WHITE);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSp);

        //Toast文字TextView容器
        LinearLayout mLayout = new LinearLayout(context);
        GradientDrawable shape = new GradientDrawable();
        shape.setColor(Color.parseColor(bgColor));
        shape.setCornerRadius(cornerRadius);
        shape.setStroke(1, Color.parseColor(bgColor));
        shape.setAlpha(180);
        mLayout.setBackground(shape);
        mLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置layout_gravity
        params.gravity = Gravity.CENTER;
        mLayout.setLayoutParams(params);
        //设置gravity
        mLayout.setGravity(Gravity.CENTER);
        mLayout.addView(tv);

        //将自定义View覆盖Toast的View
        toast.setView(mLayout);
        return toast;
    }

    @SuppressLint("StaticFieldLeak")
    private static AppToast appToast;

    private static AppToast getToast(int layoutId){
        if(appToast == null){
            appToast = new AppToast(context,layoutId);
        }else{
            if(appToast.getLayoutId() != layoutId){
                appToast = new AppToast(context,layoutId);
            }
        }
        return appToast;
    }

    /**
     * 显示笑脸toast
     * @param msg
     */
    public static void showSuccess(String msg){
        AppToast toast = getToast(R.layout.toast_app_msg);
        toast.setText(msg);
        toast.show();
    }

    /**
     * 显示哭脸toast
     * @param msg
     */
    public static void showFail(String msg){
        AppToast toast = getToast(R.layout.toast_fail_app_msg);
        toast.setText(msg);
        toast.show();
    }

    private static class AppToast extends Toast {

        private static final int LONG_DELAY = 3500; // 3.5 seconds
        private static final int SHORT_DELAY = 2000; // 2 seconds
        private long lastShowTime = 0;
        private TextView tvMessage;
        private View layout;
        private int layoutId;

        AppToast(Context context, int layoutId) {
            super(context);
            this.layoutId = layoutId;
            layout = LayoutInflater.from(context).inflate(layoutId,null);
            tvMessage = layout.findViewById(android.R.id.message);
            tvMessage.setLayoutParams(new LinearLayout.LayoutParams((int) (AppConfig.SCREEN_WIDTH * 0.6), ViewGroup.LayoutParams.WRAP_CONTENT));
            setDuration(Toast.LENGTH_SHORT);
            setGravity(Gravity.CENTER,0,0);
            setView(layout);
        }

        int getLayoutId() {
            return layoutId;
        }

        @Override
        public void setText(int resId) {
            tvMessage.setText(resId);
        }

        @Override
        public void setText(CharSequence s) {
            tvMessage.setText(s);
        }

        @Override
        public void show() {
            long nowTime = System.currentTimeMillis();
            if(nowTime - (getDuration() == Toast.LENGTH_LONG?LONG_DELAY:SHORT_DELAY) > lastShowTime){
                super.show();
                lastShowTime = nowTime;
            }
        }
    }
}
