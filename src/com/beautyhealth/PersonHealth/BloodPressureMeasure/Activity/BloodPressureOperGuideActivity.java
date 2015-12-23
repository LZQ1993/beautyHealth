package com.beautyhealth.PersonHealth.BloodPressureMeasure.Activity;

import android.R.integer;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout.LayoutParams;
import android.support.v7.app.AlertDialog;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher.ViewFactory;

import com.beautyhealth.R;
import com.beautyhealth.Infrastructure.CWComponent.NavAndTabBarActivity;
import com.beautyhealth.Infrastructure.CWComponent.TabBarFragment;

public class BloodPressureOperGuideActivity extends NavAndTabBarActivity
		implements ViewFactory, OnTouchListener, OnClickListener {
	/**
	 * ImagaSwitcher 的引用
	 */
	private ImageSwitcher mImageSwitcher;
	/**
	 * 图片id数组
	 */
	private int[] imgIds;
	/**
	 * 当前选中的图片id序号
	 */
	private int currentPosition = 0;
	/**
	 * 按下点的X坐标
	 */
	private float downX;
	private TabBarFragment tabBarFragment;

	private String[] content;
	private int[] method;
	private ImageButton leftArrow, rightArrow;
	private TextView tv_data, tv_content;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bloodpressure_operationguide);
		if (findViewById(R.id.fragment_container) != null) {
			if (savedInstanceState != null) {
				return;
			}
			tabBarFragment = new TabBarFragment();
			tabBarFragment.setArguments(getIntent().getExtras());
			getFragmentManager().beginTransaction()
					.add(R.id.fragment_container, tabBarFragment).commit();
		}
		initNavBar("操作指南", "<返回", null);
		fetchUIFromLayout();
		setListener();
	}

	private void setListener() {
		rightArrow.setOnClickListener((OnClickListener) this);
		leftArrow.setOnClickListener((OnClickListener) this);

	}

	@SuppressWarnings("deprecation")
	private void fetchUIFromLayout() {
		leftArrow = (ImageButton) findViewById(R.id.left_arrow);
		rightArrow = (ImageButton) findViewById(R.id.right_arrow);
		tv_data = (TextView) findViewById(R.id.tv_data);
		tv_content = (TextView) findViewById(R.id.tv_usercontent_show);
		imgIds = new int[] { R.drawable.pressure_pic1,
				R.drawable.pressure_pic2, R.drawable.pressure_pic3,
				R.drawable.pressure_pic4, R.drawable.pressure_pic5,
				R.drawable.pressure_pic6 };
		content = new String[] { "使用步骤1", "使用步骤2", "使用步骤3", "使用步骤4", "使用步骤5",
				"使用步骤6"};
		method = new int[] { R.string.pressureMethod1,
				R.string.pressureMethod2, R.string.pressureMethod3,
				R.string.pressureMethod4, R.string.pressureMethod5,
				R.string.pressureMethod6 };

		// 实例化ImageSwitcher
		mImageSwitcher = (ImageSwitcher) findViewById(R.id.imageSwitcher1);
		// 设置Factory
		mImageSwitcher.setFactory(this);
		// 设置OnTouchListener，我们通过Touch事件来切换图片
		mImageSwitcher.setOnTouchListener(this);
		mImageSwitcher.setImageResource(imgIds[currentPosition]);
		setContent(currentPosition);
		leftArrow.setVisibility(View.INVISIBLE);
	}

	private void setContent(int selectItems) {
		for (int i = 0; i < content.length; i++) {
			if (i == selectItems) {
				tv_data.setText(content[i]);
				tv_content.setText(getResources().getString(method[i]));
			}
		}
	}

	@Override
	public View makeView() {
		final ImageView i = new ImageView(this);
		i.setBackgroundColor(0xff000000);
		i.setScaleType(ImageView.ScaleType.CENTER_CROP);
		i.setLayoutParams(new ImageSwitcher.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		return i;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: {
			// 手指按下的X坐标
			downX = event.getX();
			break;
		}
		case MotionEvent.ACTION_UP: {
			float lastX = event.getX();
			// 抬起的时候的X坐标大于按下的时候就显示上一张图片
			if (lastX > downX) {
				prev(v);
			}

			if (lastX < downX) {
				next(v);
			}
		}

			break;
		}

		return true;
	}

	@Override
	public void onClick(View v) {
		if (v == leftArrow) {
			leftArrow.setAnimation(AnimationUtils.loadAnimation(
					getApplication(), R.anim.arrow));
			prev(v);
		}
		if (v == rightArrow) {
			rightArrow.setAnimation(AnimationUtils.loadAnimation(
					getApplication(), R.anim.arrow));
			next(v);
		}

	}

	public void prev(View source) {

		rightArrow.setVisibility(View.VISIBLE);
		// 显示上一个组件
		if (currentPosition > 0) {

			// 设置动画，这里的动画比较简单，不明白的去网上看看相关内容

			mImageSwitcher.setInAnimation(AnimationUtils.loadAnimation(
					getApplication(), R.anim.left_in));
			mImageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(
					getApplication(), R.anim.right_out));
			currentPosition--;
			if (currentPosition == 0) {
				leftArrow.setVisibility(View.INVISIBLE);
			}
			mImageSwitcher.setImageResource(imgIds[currentPosition
					% imgIds.length]);
			mImageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(
					getApplication(), R.anim.anim_scale));
			setContent(currentPosition);
		} else {
			/*
			 * Toast.makeText(getApplication(), "已经是第一张",
			 * Toast.LENGTH_SHORT).show();
			 */
			new AlertDialog.Builder(this)
					.setTitle("提示")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									return;
								}
							}).setMessage("已经是第一张").setCancelable(false).show();
			leftArrow.setVisibility(View.INVISIBLE);
		}

	}

	public void next(View source) {
		leftArrow.setVisibility(View.VISIBLE);
		// 显示下一个组件。
		if (currentPosition < imgIds.length - 1) {

			mImageSwitcher.setInAnimation(AnimationUtils.loadAnimation(
					getApplication(), R.anim.right_in));
			mImageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(
					getApplication(), R.anim.left_out));
			currentPosition++;
			if (currentPosition == (imgIds.length - 1)) {
				rightArrow.setVisibility(View.INVISIBLE);
			}
			mImageSwitcher.setImageResource(imgIds[currentPosition]);
			mImageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(
					getApplication(), R.anim.anim_scale));
			setContent(currentPosition);

		} else {
			new AlertDialog.Builder(this)
					.setTitle("提示")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									return;
								}
							}).setMessage("到了最后一张").setCancelable(false).show();
			rightArrow.setVisibility(View.INVISIBLE);
		}

	}

}