package org.game.bangyouscreen.util;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.AnimationData;
import org.andengine.entity.sprite.IAnimationData;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.entity.sprite.vbo.ITiledSpriteVertexBufferObject;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.shader.PositionColorTextureCoordinatesShaderProgram;
import org.andengine.opengl.shader.ShaderProgram;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.DrawType;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.debug.Debug;
import org.andengine.util.time.TimeConstants;

/**
 * 结合AnimatedSprite 和 ButtonSprite 的功能
 * @author zuowhat 2013-11-25
 * @version 1.0
 */
public class AnimatedButtonSprite extends TiledSprite {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int FRAMEINDEX_INVALID = -1;

	// ===========================================================
	// Fields
	// ===========================================================
	
	private int mStateCount;
	private OnClickListenerABS mOnClickListener;

	private boolean mEnabled = true;
	private State mState;
	
	private boolean mAnimationRunning;
	private boolean mAnimationStartedFired;

	private int mCurrentFrameIndex;
	private long mAnimationProgress;
	private int mRemainingLoopCount;

	@SuppressWarnings("deprecation")
	private final IAnimationData mAnimationData = new AnimationData();
	private IAnimationListener mAnimationListener;

	// ===========================================================
	// Constructors
	// ===========================================================

	public AnimatedButtonSprite(final float pX, final float pY, final ITiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager, DrawType.DYNAMIC);
		//this.mStateCount = pTiledTextureRegion.getTileCount();
	}

	public AnimatedButtonSprite(final float pX, final float pY, final ITiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final ShaderProgram pShaderProgram) {
		super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager, DrawType.DYNAMIC, pShaderProgram);
	}

	public AnimatedButtonSprite(final float pX, final float pY, final ITiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType) {
		super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager, pDrawType);
	}

	public AnimatedButtonSprite(final float pX, final float pY, final ITiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType, final ShaderProgram pShaderProgram) {
		super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager, pDrawType, pShaderProgram);
	}

	public AnimatedButtonSprite(final float pX, final float pY, final ITiledTextureRegion pTiledTextureRegion, final ITiledSpriteVertexBufferObject pTiledSpriteVertexBufferObject) {
		super(pX, pY, pTiledTextureRegion, pTiledSpriteVertexBufferObject);
	}

	public AnimatedButtonSprite(final float pX, final float pY, final ITiledTextureRegion pTiledTextureRegion, final ITiledSpriteVertexBufferObject pTiledSpriteVertexBufferObject, final ShaderProgram pShaderProgram) {
		super(pX, pY, pTiledTextureRegion, pTiledSpriteVertexBufferObject, pShaderProgram);
	}

	public AnimatedButtonSprite(final float pX, final float pY, final float pWidth, final float pHeight, final ITiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pWidth, pHeight, pTiledTextureRegion, pVertexBufferObjectManager, DrawType.DYNAMIC);
	}

	public AnimatedButtonSprite(final float pX, final float pY, final float pWidth, final float pHeight, final ITiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final ShaderProgram pShaderProgram) {
		super(pX, pY, pWidth, pHeight, pTiledTextureRegion, pVertexBufferObjectManager, DrawType.DYNAMIC, pShaderProgram);
	}

	public AnimatedButtonSprite(final float pX, final float pY, final float pWidth, final float pHeight, final ITiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType) {
		super(pX, pY, pWidth, pHeight, pTiledTextureRegion, pVertexBufferObjectManager, pDrawType);
	}

	public AnimatedButtonSprite(final float pX, final float pY, final float pWidth, final float pHeight, final ITiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType, final ShaderProgram pShaderProgram) {
		super(pX, pY, pWidth, pHeight, pTiledTextureRegion, pVertexBufferObjectManager, pDrawType, pShaderProgram);
	}

	public AnimatedButtonSprite(final float pX, final float pY, final float pWidth, final float pHeight, final ITiledTextureRegion pTiledTextureRegion, final ITiledSpriteVertexBufferObject pTiledSpriteVertexBufferObject) {
		super(pX, pY, pWidth, pHeight, pTiledTextureRegion, pTiledSpriteVertexBufferObject, PositionColorTextureCoordinatesShaderProgram.getInstance());
	}

	public AnimatedButtonSprite(final float pX, final float pY, final float pWidth, final float pHeight, final ITiledTextureRegion pTiledTextureRegion, final ITiledSpriteVertexBufferObject pTiledSpriteVertexBufferObject, final ShaderProgram pShaderProgram) {
		super(pX, pY, pWidth, pHeight, pTiledTextureRegion, pTiledSpriteVertexBufferObject, pShaderProgram);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public boolean isAnimationRunning() {
		return this.mAnimationRunning;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onManagedUpdate(final float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);

		if (this.mAnimationRunning) {
			final int loopCount = this.mAnimationData.getLoopCount();
			final int[] frames = this.mAnimationData.getFrames();
			final long animationDuration = this.mAnimationData.getAnimationDuration();

			if (!this.mAnimationStartedFired && (this.mAnimationProgress == 0)) {
				this.mAnimationStartedFired = true;
				if (frames == null) {
					this.setCurrentTileIndex(this.mAnimationData.getFirstFrameIndex());
				} else {
					this.setCurrentTileIndex(frames[0]);
				}
				this.mCurrentFrameIndex = 0;
				if (this.mAnimationListener != null) {
					this.mAnimationListener.onAnimationStarted(this, loopCount);
					this.mAnimationListener.onAnimationFrameChanged(this, AnimatedButtonSprite.FRAMEINDEX_INVALID, 0);
				}
			}
			final long nanoSecondsElapsed = (long) (pSecondsElapsed * TimeConstants.NANOSECONDS_PER_SECOND);
			this.mAnimationProgress += nanoSecondsElapsed;

			if (loopCount == IAnimationData.LOOP_CONTINUOUS) {
				while (this.mAnimationProgress > animationDuration) {
					this.mAnimationProgress -= animationDuration;
					if (this.mAnimationListener != null) {
						this.mAnimationListener.onAnimationLoopFinished(this, this.mRemainingLoopCount, loopCount);
					}
				}
			} else {
				while (this.mAnimationProgress > animationDuration) {
					this.mAnimationProgress -= animationDuration;
					this.mRemainingLoopCount--;
					if (this.mRemainingLoopCount < 0) {
						break;
					} else if (this.mAnimationListener != null) {
						this.mAnimationListener.onAnimationLoopFinished(this, this.mRemainingLoopCount, loopCount);
					}
				}
			}

			if ((loopCount == IAnimationData.LOOP_CONTINUOUS) || (this.mRemainingLoopCount >= 0)) {
				final int newFrameIndex = this.mAnimationData.calculateCurrentFrameIndex(this.mAnimationProgress);

				if (this.mCurrentFrameIndex != newFrameIndex) {
					if (frames == null) {
						this.setCurrentTileIndex(this.mAnimationData.getFirstFrameIndex() + newFrameIndex);
					} else {
						this.setCurrentTileIndex(frames[newFrameIndex]);
					}
					if (this.mAnimationListener != null) {
						this.mAnimationListener.onAnimationFrameChanged(this, this.mCurrentFrameIndex, newFrameIndex);
					}
				}
				this.mCurrentFrameIndex = newFrameIndex;
			} else {
				this.mAnimationRunning = false;
				if (this.mAnimationListener != null) {
					this.mAnimationListener.onAnimationFinished(this);
				}
			}
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void stopAnimation() {
		this.mAnimationRunning = false;
	}

	public void stopAnimation(final int pTileIndex) {
		this.mAnimationRunning = false;
		this.setCurrentTileIndex(pTileIndex);
	}

	public void animate(final long pFrameDurationEach) {
		this.animate(pFrameDurationEach, null);
	}

	public void animate(final long pFrameDurationEach, final IAnimationListener pAnimationListener) {
		this.mAnimationData.set(pFrameDurationEach, this.getTileCount());

		this.initAnimation(pAnimationListener);
	}

	public void animate(final long pFrameDurationEach, final boolean pLoop) {
		this.animate(pFrameDurationEach, pLoop, null);
	}

	public void animate(final long pFrameDurationEach, final boolean pLoop, final IAnimationListener pAnimationListener) {
		this.mAnimationData.set(pFrameDurationEach, this.getTileCount(), pLoop);

		this.initAnimation(pAnimationListener);
	}

	public void animate(final long pFrameDurationEach, final int pLoopCount) {
		this.animate(pFrameDurationEach, pLoopCount, null);
	}

	public void animate(final long pFrameDurationEach, final int pLoopCount, final IAnimationListener pAnimationListener) {
		this.mAnimationData.set(pFrameDurationEach, this.getTileCount(), pLoopCount);

		this.initAnimation(pAnimationListener);
	}

	public void animate(final long[] pFrameDurations) {
		this.animate(pFrameDurations, (IAnimationListener) null);
	}

	public void animate(final long[] pFrameDurations, final IAnimationListener pAnimationListener) {
		this.mAnimationData.set(pFrameDurations);

		this.initAnimation(pAnimationListener);
	}

	public void animate(final long[] pFrameDurations, final boolean pLoop) {
		this.animate(pFrameDurations, pLoop, null);
	}

	public void animate(final long[] pFrameDurations, final boolean pLoop, final IAnimationListener pAnimationListener) {
		this.mAnimationData.set(pFrameDurations, pLoop);

		this.initAnimation(pAnimationListener);
	}

	public void animate(final long[] pFrameDurations, final int pLoopCount) {
		this.animate(pFrameDurations, pLoopCount, null);
	}

	public void animate(final long[] pFrameDurations, final int pLoopCount, final IAnimationListener pAnimationListener) {
		this.mAnimationData.set(pFrameDurations, pLoopCount);

		this.initAnimation(pAnimationListener);
	}

	public void animate(final long[] pFrameDurations, final int pFirstTileIndex, final int pLastTileIndex, final boolean pLoop) {
		this.animate(pFrameDurations, pFirstTileIndex, pLastTileIndex, pLoop, null);
	}

	public void animate(final long[] pFrameDurations, final int pFirstTileIndex, final int pLastTileIndex, final boolean pLoop, final IAnimationListener pAnimationListener) {
		this.mAnimationData.set(pFrameDurations, pFirstTileIndex, pLastTileIndex, pLoop);

		this.initAnimation(pAnimationListener);
	}

	public void animate(final long[] pFrameDurations, final int pFirstTileIndex, final int pLastTileIndex, final int pLoopCount) {
		this.animate(pFrameDurations, pFirstTileIndex, pLastTileIndex, pLoopCount, null);
	}

	public void animate(final long[] pFrameDurations, final int pFirstTileIndex, final int pLastTileIndex, final int pLoopCount, final IAnimationListener pAnimationListener) {
		this.mAnimationData.set(pFrameDurations, pFirstTileIndex, pLastTileIndex, pLoopCount);

		this.initAnimation(pAnimationListener);
	}

	/**
	 * Animate specifics frames.
	 *
	 * @param pFrameDurations must have the same length as pFrames.
	 * @param pFrames indices of the frames to animate.
	 */
	public void animate(final long[] pFrameDurations, final int[] pFrames) {
		this.animate(pFrameDurations, pFrames, null);
	}

	/**
	 * Animate specifics frames.
	 *
	 * @param pFrameDurations must have the same length as pFrames.
	 * @param pFrames indices of the frames to animate.
	 * @param pAnimationListener
	 */
	public void animate(final long[] pFrameDurations, final int[] pFrames, final IAnimationListener pAnimationListener) {
		this.mAnimationData.set(pFrameDurations, pFrames);

		this.initAnimation(pAnimationListener);
	}

	/**
	 * Animate specifics frames.
	 *
	 * @param pFrameDurations must have the same length as pFrames.
	 * @param pFrames indices of the frames to animate.
	 * @param pLoop
	 */
	public void animate(final long[] pFrameDurations, final int[] pFrames, final boolean pLoop) {
		this.animate(pFrameDurations, pFrames, pLoop, null);
	}

	/**
	 * Animate specifics frames.
	 *
	 * @param pFrameDurations must have the same length as pFrames.
	 * @param pFrames indices of the frames to animate.
	 * @param pLoop
	 * @param pAnimationListener
	 */
	public void animate(final long[] pFrameDurations, final int[] pFrames, final boolean pLoop, final IAnimationListener pAnimationListener) {
		this.mAnimationData.set(pFrameDurations, pFrames, pLoop);

		this.initAnimation(pAnimationListener);
	}

	/**
	 * Animate specifics frames.
	 *
	 * @param pFrameDurations must have the same length as pFrames.
	 * @param pFrames indices of the frames to animate.
	 * @param pLoopCount
	 */
	public void animate(final long[] pFrameDurations, final int[] pFrames, final int pLoopCount) {
		this.animate(pFrameDurations, pFrames, pLoopCount, null);
	}

	/**
	 * Animate specifics frames.
	 *
	 * @param pFrameDurations must have the same length as pFrames.
	 * @param pFrames indices of the frames to animate.
	 * @param pLoopCount
	 * @param pAnimationListener
	 */
	public void animate(final long[] pFrameDurations, final int[] pFrames, final int pLoopCount, final IAnimationListener pAnimationListener) {
		this.mAnimationData.set(pFrameDurations, pFrames, pLoopCount);

		this.initAnimation(pAnimationListener);
	}

	public void animate(final IAnimationData pAnimationData) {
		this.animate(pAnimationData, null);
	}

	private void animate(final IAnimationData pAnimationData, final IAnimationListener pAnimationListener) {
		this.mAnimationData.set(pAnimationData);

		this.initAnimation(pAnimationListener);
	}

	private void initAnimation(final IAnimationListener pAnimationListener) {
		this.mAnimationStartedFired = false;
		this.mAnimationListener = pAnimationListener;
		this.mRemainingLoopCount = this.mAnimationData.getLoopCount();

		this.mAnimationProgress = 0;
		this.mAnimationRunning = true;
	}

	public void setOnClickListenerABS(final OnClickListenerABS pOnClickListener) {
		this.mOnClickListener = pOnClickListener;
	}
	
	public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
		if (!this.isEnabled()) {
			this.changeState(State.DISABLED);
		} else if (pSceneTouchEvent.isActionDown()) {
			this.changeState(State.PRESSED);
		} else if (pSceneTouchEvent.isActionCancel() || !this.contains(pSceneTouchEvent.getX(), pSceneTouchEvent.getY())) {
			this.changeState(State.NORMAL);
		} else if (pSceneTouchEvent.isActionUp() && this.mState == State.PRESSED) {
			this.changeState(State.NORMAL);

			if (this.mOnClickListener != null) {
				this.mOnClickListener.onClick(this, pTouchAreaLocalX, pTouchAreaLocalY);
			}
		}

		return true;
	}
	
	public boolean isEnabled() {
		return this.mEnabled;
	}
	
	private void changeState(final State pState) {
		if (pState == this.mState) {
			return;
		}

		this.mState = pState;

		final int stateTiledTextureRegionIndex = this.mState.getTiledTextureRegionIndex();
		if (stateTiledTextureRegionIndex >= this.mStateCount) {
			//this.setCurrentTileIndex(0);
			Debug.w(this.getClass().getSimpleName() + " changed its " + State.class.getSimpleName() + " to " + pState.toString() + ", which doesn't have a " + ITextureRegion.class.getSimpleName() + " supplied. Applying default " + ITextureRegion.class.getSimpleName() + ".");
		} else {
			//this.setCurrentTileIndex(stateTiledTextureRegionIndex);
		}
	}
	
	public interface OnClickListenerABS {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		public void onClick(final AnimatedButtonSprite pButtonSprite, final float pTouchAreaLocalX, final float pTouchAreaLocalY);
	}

	public static enum State {
		// ===========================================================
		// Elements
		// ===========================================================

		NORMAL(0),
		PRESSED(1),
		DISABLED(2);

		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		private final int mTiledTextureRegionIndex;

		// ===========================================================
		// Constructors
		// ===========================================================

		private State(final int pTiledTextureRegionIndex) {
			this.mTiledTextureRegionIndex = pTiledTextureRegionIndex;
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		public int getTiledTextureRegionIndex() {
			return this.mTiledTextureRegionIndex;
		}

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static interface IAnimationListener {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		/**
		 * @param pAnimatedSprite
		 * @param pInitialLoopCount is {@link AnimatedSprite#LOOP_CONTINUOUS} when {@link AnimatedSprite} loops infinitely.
		 */
		public void onAnimationStarted(final AnimatedButtonSprite pAnimatedSprite, final int pInitialLoopCount);
		/**
		 * @param pAnimatedSprite
		 * @param pOldFrameIndex equals {@link AnimatedSprite#FRAMEINDEX_INVALID}, the first time {@link IAnimationListener#onAnimationFrameChanged(AnimatedSprite, int, int)} is called.
		 * @param pNewFrameIndex the new frame index of the currently active animation.
		 */
		public void onAnimationFrameChanged(final AnimatedButtonSprite pAnimatedSprite, final int pOldFrameIndex, final int pNewFrameIndex);
		/**
		 * @param pAnimatedSprite
		 * @param pRemainingLoopCount is {@link AnimatedSprite#LOOP_CONTINUOUS} when {@link AnimatedSprite} loops infinitely.
		 * @param pInitialLoopCount is {@link AnimatedSprite#LOOP_CONTINUOUS} when {@link AnimatedSprite} loops infinitely.
		 */
		public void onAnimationLoopFinished(final AnimatedButtonSprite pAnimatedSprite, final int pRemainingLoopCount, final int pInitialLoopCount);
		public void onAnimationFinished(final AnimatedButtonSprite pAnimatedSprite);
	}
}
