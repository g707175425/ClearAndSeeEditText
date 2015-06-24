package cn.schope.lightning.view;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;
import cn.schope.lightning.R;
import cn.schope.lightning.utils.UIUtils;

public class CanSeeEditText extends EditText{

	private Context context;
	private Bitmap clearBut;
	private Bitmap passwordEye;
	private Paint bitmapPaint;
	private Bitmap passwordEyeInvisible;
	private Bitmap passwordEyeVisible;
	private int rightOffset;
	private int rightPadding;
	private Bitmap clearButShow;
	private int initPaddingRight;

	public CanSeeEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		init();
	}

	public CanSeeEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}

	public CanSeeEditText(Context context) {
		super(context);
		this.context = context;
		init();
	}

	private void init() {
		rightOffset = UIUtils.dip2px(5);
		clearButShow = ((BitmapDrawable)getResources().getDrawable(R.drawable.icon_edittext_delete)).getBitmap();
		passwordEyeInvisible = ((BitmapDrawable)getResources().getDrawable(R.drawable.password_invisible)).getBitmap();
		passwordEyeVisible = ((BitmapDrawable)getResources().getDrawable(R.drawable.password_visible)).getBitmap();
		passwordEye = passwordEyeInvisible;
		bitmapPaint = new Paint();
		bitmapPaint.setAntiAlias(true);
		
		initPaddingRight = getPaddingRight();
		if(TextUtils.isEmpty(getText().toString())){
			clearBut = null;
			rightPadding = initPaddingRight+passwordEye.getWidth()+rightOffset;
		}else{
			clearBut = clearButShow;
			rightPadding = initPaddingRight+clearBut.getWidth()+passwordEye.getWidth()+rightOffset+rightOffset;
		}
		setPadding(getPaddingLeft(), getPaddingTop(), rightPadding, getPaddingBottom());
//		setOnTouchListener(this);
		initTextChange();
	}
	
	private void initTextChange() {
		addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
			}
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (s.length() > 0) {
					clearBut = clearButShow;
					rightPadding = initPaddingRight+clearBut.getWidth()+passwordEye.getWidth()+rightOffset+rightOffset;
					invalidate();
				} else {
					clearBut = null;
					rightPadding = initPaddingRight+passwordEye.getWidth()+rightOffset;
					invalidate();
				}
				setPadding(getPaddingLeft(), getPaddingTop(), rightPadding, getPaddingBottom());
			}
		});
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_UP:
			float distanceToDrawLeft = event.getX() - (getMeasuredWidth()-rightPadding);
			if(distanceToDrawLeft >= 0){
				if(distanceToDrawLeft >= (rightPadding - (passwordEye.getWidth()+rightOffset))){
					//眼睛区域被点击了
					System.out.println("眼睛区域被点击了");
					if(getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)){
                		setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                		setSelection(getText().length());
                		passwordEye = passwordEyeVisible;
                		invalidate();
                	}else{
                		setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                		setSelection(getText().length());
                		passwordEye = passwordEyeInvisible;
                		invalidate();
                	}
				}else{
					//清除区域被点击了
					setText("");
					clearBut = null;
					rightPadding = initPaddingRight+passwordEye.getWidth()+rightOffset;
					setPadding(getPaddingLeft(), getPaddingTop(), rightPadding, getPaddingBottom());
					invalidate();
				}
			}
			break;
		}
		return super.onTouchEvent(event);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.save();
		canvas.translate(getScrollX(),0);
		int eyeLeft = getMeasuredWidth()-passwordEye.getWidth()-rightOffset;
		canvas.drawBitmap(passwordEye,eyeLeft,(getMeasuredHeight()-passwordEye.getHeight())/2,bitmapPaint);
		if(clearBut != null){
			canvas.drawBitmap(clearBut,eyeLeft-clearBut.getWidth()-rightOffset,(getMeasuredHeight()-clearBut.getHeight())/2,bitmapPaint);
		}
		canvas.restore();
	}

}
