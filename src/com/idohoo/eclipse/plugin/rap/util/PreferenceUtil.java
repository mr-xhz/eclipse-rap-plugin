package com.idohoo.eclipse.plugin.rap.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class PreferenceUtil {

	private static List<Field> getAllField(Class<?> clazz){
		
		List<Field> liField = new ArrayList<Field>();
		while(true){
			Field[] fields = clazz.getDeclaredFields();
			for(Field field :fields){
				liField.add(field);
			}
			clazz = clazz.getSuperclass();
			if(clazz == null){
				break;
			}
		}
		return liField;
	}

	/**
	 * 获取所有的字段
	 * @param activator
	 * @param clazz
	 * @return
	 */
	public static <T> T getVO(AbstractUIPlugin activator,Class<T> clazz){
		try {
			T result = clazz.newInstance();
			List<Field> liField = getAllField(clazz);
			IPreferenceStore preferenceStore = activator.getPreferenceStore();
			for(Field field : liField){
				String name = field.getName();
				PreferenceField preferenceField = field.getAnnotation(PreferenceField.class);
				if(preferenceField != null && preferenceField.value() != null && !"".equals(preferenceField.value())){
					name = preferenceField.value();
				}
				String value = preferenceStore.getString(name);
				field.setAccessible(true);
				field.set(result, value);
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}
}
