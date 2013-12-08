package org.game.bangyouscreen.util;

import org.andengine.entity.Entity;
import org.game.bangyouscreen.managers.ResourceManager;

/**
 * 实体工具类
 * @author zuowhat 2013-12-08
 * @version 1.0
 */
public class EntityUtil {
	
	/**
	 * 定义实体在屏幕中的大小
	 * @author zuowhat 2013-12-08
	 * @since 1.0
	 */
	public static void setSize(String sizeType, float size, Entity entity){
		if("width".equals(sizeType)){
			entity.setSize(size * ResourceManager.getCamera().getWidth(), (size * ResourceManager.getCamera().getWidth()) / (entity.getWidth() / entity.getHeight()));
		}else if("height".equals(sizeType)){
			entity.setSize((size * ResourceManager.getCamera().getHeight()) * (entity.getWidth() / entity.getHeight()), size * ResourceManager.getCamera().getHeight());
		}
	}
	
	/**
	 * 定义实体在父层中的大小
	 * @author zuowhat 2013-12-08
	 * @since 1.0
	 */
	public static void setSizeInParent(String sizeType, float size, Entity sEntity, Entity pEntity){
		if("width".equals(sizeType)){
			sEntity.setSize(size * pEntity.getWidth(), (size * pEntity.getWidth()) / (sEntity.getWidth() / sEntity.getHeight()));
		}else if("height".equals(sizeType)){
			sEntity.setSize((size * pEntity.getHeight()) * (sEntity.getWidth() / sEntity.getHeight()), size * pEntity.getHeight());
		}
	}

}
