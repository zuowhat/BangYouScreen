package org.game.bangyouscreen.util;

import org.andengine.entity.Entity;
import org.game.bangyouscreen.managers.ResourceManager;

public class EntityUtil {
	
	public static void setSize(String sizeType, float size, Entity entity){
		if("width".equals(sizeType)){
			entity.setSize(size * ResourceManager.getCamera().getWidth(), (size * ResourceManager.getCamera().getWidth()) / (entity.getWidth() / entity.getHeight()));
		}else if("height".equals(sizeType)){
			entity.setSize((size * ResourceManager.getCamera().getHeight()) * (entity.getWidth() / entity.getHeight()), size * ResourceManager.getCamera().getHeight());
		}
	}

}
