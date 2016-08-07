package com.intrist.agent;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class Flip3dAnimation extends Animation {
	private final float mFromDegrees;
	private final float mToDegrees;
	private final float mCenterX;
	private final float mCenterY;
	private Camera mCamera;
	public boolean rotateY = true;

	public Flip3dAnimation(float fromDegrees, float toDegrees, float centerX, float centerY) {
		mFromDegrees = fromDegrees;
		mToDegrees = toDegrees;
		mCenterX = centerX;
		mCenterY = centerY;
	}

	@Override
	public void initialize(int width, int height, int parentWidth,
			int parentHeight) {
		super.initialize(width, height, parentWidth, parentHeight);
		mCamera = new Camera();
	}

	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {

		//float degrees1 = mToDegrees * interpolatedTime;
		final float fromDegrees = mFromDegrees;
		
		float degrees1 = fromDegrees + ((mToDegrees - fromDegrees) * interpolatedTime);
		
		final Camera camera = mCamera;

		final Matrix matrix = t.getMatrix();

		camera.save();

		if (rotateY) {
			
			camera.rotateY(degrees1);
			
		} else {
			
			camera.rotateX(degrees1);
			
		}

		camera.getMatrix(matrix);
		camera.restore();

		matrix.preTranslate(-mCenterX, -mCenterY);
		matrix.postTranslate(mCenterX, mCenterY);

	}

}
