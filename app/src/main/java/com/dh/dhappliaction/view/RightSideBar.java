package com.dh.dhappliaction.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.dh.dhappliaction.R;

public class RightSideBar extends View {

	private String[] letters = { "A", "B", "C", "D", "E", "F", "G", "H", "I",
			"J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
			"W", "X", "Y", "Z", "#" };

	private Paint paint = new Paint();
	private int choose = -1;

	private PopupWindow mPopupWindow;
	private TextView mPopupText;
	private Handler handler = new Handler();

	private OnTouchingLetterChangeListener listener;

	public interface OnTouchingLetterChangeListener {
		public void onTouchingLetterChangeListener(String str);
	}

	public void setOnTouchingLetterChangeListener(
			OnTouchingLetterChangeListener listener) {
		this.listener = listener;
	}

	public RightSideBar(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public RightSideBar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public RightSideBar(Context context) {
		super(context);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int height = getHeight();
		int width = getWidth();
		int singleHeight = height / letters.length;
		for (int i = 0; i < letters.length; i++) {
			paint.setColor(Color.BLACK);
			paint.setTypeface(Typeface.DEFAULT_BOLD);
			paint.setAntiAlias(true);
			paint.setTextSize(25);
			if (i == choose) {
				paint.setColor(Color.BLUE);
				paint.setTextSize(40);
				paint.setFakeBoldText(true);
			}
			float x = width / 2 - paint.measureText(letters[i]) / 2;
			float y = (float) (singleHeight * i + (float) singleHeight / 1.5);
			canvas.drawText(letters[i], x, y, paint);
			paint.reset();
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		float y = event.getY();
		int oldChoose = choose;
		int letterPos = (int) (y / getHeight() * letters.length);

		switch (event.getAction()) {
		case MotionEvent.ACTION_UP:
			choose = -1;
			dismissPopup();
			invalidate();
			break;

		default:
			if (oldChoose != letterPos) {
				if (letterPos > -1 && letterPos < letters.length) {
					if (listener != null) {
						listener.onTouchingLetterChangeListener(letters[letterPos]);
					}
					choose = letterPos;
					invalidate();
				}
			}
			break;
		}
		return true;
	}

	public void showPopup(String letter){
		if(mPopupWindow==null){
			mPopupText = new TextView(getContext());
			mPopupText.setBackgroundResource(R.drawable.sms_chat_me_bg);
            mPopupText.setTextColor(Color.BLACK);
            mPopupText.setTextSize(60);
            int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 120, getResources().getDisplayMetrics());
            mPopupText.setWidth(width);
            mPopupText.setHeight(width);
            mPopupText.setGravity(Gravity.CENTER);
            mPopupWindow = new PopupWindow(mPopupText,ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		mPopupText.setText(letter);
		if(mPopupWindow.isShowing()){
			mPopupWindow.update();
		}else{
			mPopupWindow.showAtLocation(getRootView(), Gravity.CENTER, 0, 0);
			dismissPopup();
		}
	}

	private void dismissPopup() {
		handler.postDelayed(dismissRunnable, 1300);
	}

	Runnable dismissRunnable = new Runnable() {
		@Override
		public void run() {
			if (mPopupWindow != null) {
				mPopupWindow.dismiss();
			}
		}
	};

}
