package org.game.bangyouscreen.managers;

import org.andengine.entity.scene.CameraScene;

public abstract class ManagedLayer extends CameraScene {

	public static final float mSLIDE_PIXELS_PER_SECONDS = 3000f;
	// Is set TRUE if the layer is loaded.
	public boolean mHasLoaded = false;
	// Set by the constructor, if true, the layer will be unloaded after being
	// hidden.
	public boolean mUnloadOnHidden;

	// Convenience constructor. Creates a layer that does not unload when
	// hidden.
	public ManagedLayer() {
		this(false);
	}

	// Constructor. Sets whether the layer will unload when hidden and ensures
	// that there is no background on the layer.
	public ManagedLayer(final boolean pUnloadOnHidden) {
		this.mUnloadOnHidden = pUnloadOnHidden;
		this.setBackgroundEnabled(false);
	}

	public abstract void onHideLayer();

	// Pause the layer, hide it, and unload it if it needs to be unloaded.
	public void onHideManagedLayer() {
		this.setIgnoreUpdate(true);
		this.onHideLayer();
		if (this.mUnloadOnHidden) {
			this.onUnloadLayer();
		}
	}

	// Methods to override in subclasses.
	public abstract void onLoadLayer();

	public abstract void onShowLayer();

	// If the layer is not loaded, load it. Ensure that the layer is not paused.
	public void onShowManagedLayer() {
		if (!this.mHasLoaded) {
			this.mHasLoaded = true;
			this.onLoadLayer();
		}
		//this.onLoadLayer();
		this.setIgnoreUpdate(false);
		this.onShowLayer();
	}

	public abstract void onUnloadLayer();
}