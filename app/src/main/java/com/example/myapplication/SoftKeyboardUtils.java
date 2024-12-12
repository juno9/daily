package com.example.myapplication;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;

public class SoftKeyboardUtils {
    public static void setupSoftKeyboardListener(Activity activity, SoftKeyboardListener listener) {
        final View rootView = activity.getWindow().getDecorView().findViewById(android.R.id.content);
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            Rect r = new Rect();
            rootView.getWindowVisibleDisplayFrame(r);
            int screenHeight = rootView.getRootView().getHeight();
            int keyboardHeight = screenHeight - r.bottom;

            boolean isSoftKeyboardVisible = keyboardHeight > screenHeight * 0.15; // 키보드 높이가 화면 높이의 15% 이상이면 키보드가 보이는 것으로 판단
            listener.onSoftKeyboardVisibilityChanged(isSoftKeyboardVisible, keyboardHeight);
        });
    }

    public interface SoftKeyboardListener {
        void onSoftKeyboardVisibilityChanged(boolean isVisible, int keyboardHeight);
    }
}

