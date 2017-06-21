package com.idohoo.eclipse.plugin.rap.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.transform.Source;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.idohoo.eclipse.plugin.rap.vo.rap.TestVO;

public class JsonUtil {

	/**
	 * 判断是否基础类型
	 * @param type
	 * @return
	 */
	private static boolean isJDKType(String type){
		Set<String> jdkType = new HashSet<String>();
		jdkType.add("int");jdkType.add("Integer");
		jdkType.add("long");jdkType.add("Long");
		jdkType.add("float");jdkType.add("Float");
		jdkType.add("double");jdkType.add("Double");
		jdkType.add("boolean");jdkType.add("Boolean");
		jdkType.add("byte");jdkType.add("Byte");
		jdkType.add("Date");jdkType.add("BigDecimal");
		jdkType.add("BigInteger");jdkType.add("void");
		jdkType.add("String");jdkType.add("char");jdkType.add("Char");
		if(jdkType.contains(type)){
			return true;
		}
		return false;
	}
	
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
	
	private static Object parser(String jsonStr){
		if(jsonStr == null || "".equals(jsonStr)){
			return null;
		}
		//去掉UTF8 BOM头
		if(((int)jsonStr.trim().charAt(0)) == 65279){
			jsonStr = jsonStr.trim().substring(1);
		}
		JSONParser jsonParser = new JSONParser();
		try {
			return jsonParser.parse(jsonStr);
		} catch (ParseException e) {
		}
		return null;
	}
	
	public static Object parseAndGet(String jsonStr,String name){
		Object object = parser(jsonStr);
		return parseAndGet(object,name);
	}
	
	public static Object parseAndGet(String jsonStr,String[] names){
		Object object = parser(jsonStr);
		return parseAndGet(object,names);
	}
	
	public static Object parseAndGet(Object object,String[] names){
		Object obj = object;
		for(String name : names){
			obj = parseAndGet(obj,name);
		}
		return obj;
	}
	
	public static Object parseAndGet(Object object,String name){
		if(object == null) return null;
		if(object instanceof JSONObject){
			return ((JSONObject)object).get(name);
		}else if(object instanceof JSONArray){
			return ((JSONArray)object).get(0);
		}
		return object;
	}
	
	
	
	public static <T> T parseToObject(String jsonStr,Class<T> clazz){
		Object object = parser(jsonStr);
		if(object == null){
			return null;
		}
		return parseToObject(object,clazz);
	}
	
	public static <T> T parseToObject(Object object,Class<T> clazz){
		if(object == null) return null;
		JSONObject jsonObject = null;
		if(object instanceof JSONObject){
			jsonObject = (JSONObject)object;
		}else if(object instanceof JSONArray){
			jsonObject = (JSONObject)((JSONArray)object).get(0);
		}
		return parseToObject(jsonObject,clazz);
	}
	
	public static <T> T parseToObject(JSONObject jsonObject,Class<T> clazz){
		if(jsonObject == null){
			return null;
		}
		if(clazz.equals(Object.class)){
			return (T)jsonObject;
		}
		try {
			T result = clazz.newInstance();
			List<Field> fields = getAllField(clazz);
			for(Field field : fields){
				Object object = jsonObject.get(field.getName());
				if(object == null){
					continue;
				}
				field.setAccessible(true);
				if(object instanceof JSONObject){
					field.set(result, parseToObject((JSONObject)object,field.getType()));
				}else if(object instanceof JSONArray){
					if(field.getGenericType() instanceof ParameterizedType){
						ParameterizedType parameterizedType = (ParameterizedType)field.getGenericType();
						field.set(result, parseToList((JSONArray)object,Class.forName(parameterizedType.getActualTypeArguments()[0].getTypeName())));
					}else{
						field.set(result, formatValue(object,field.getType()));
					}
				}else{
					field.set(result, formatValue(object,field.getType()));
				}
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private static <T> T formatValue(Object v,Class<T> clazz){
		if(v == null){
			return null;
		}
		if(clazz.equals(Date.class)){
			if(v instanceof Long){
				return (T)new Date((long)v);
			}else{
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				try {
					return (T)simpleDateFormat.parse(v.toString());
				} catch (java.text.ParseException e) {
					simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
					try {
						return (T)simpleDateFormat.parse(v.toString());
					} catch (java.text.ParseException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
		try{
			Method valueOf = clazz.getMethod("valueOf", String.class);
			return (T)valueOf.invoke(null,v.toString());
		}catch(Exception nsme){
			return (T)v;
		}
	}
	
	public static <T> List<T> parseToList(String jsonStr,Class<T> clazz){
		Object object = parser(jsonStr);
		if(object == null){
			return null;
		}
		return parseToList(object,clazz);
	}
	
	public static <T> List<T> parseToList(Object object,Class<T> clazz){
		if(object == null){
			return null;
		}
		return parseToList((JSONArray)object,clazz);
	}
	
	public static <T> List<T> parseToList(JSONArray jsonArray,Class<T> clazz){
		if(jsonArray == null){
			return null;
		}
		ArrayList<T> result = new ArrayList<T>();
		for(Object o : jsonArray){
			if(o instanceof JSONObject){
				result.add(parseToObject((JSONObject)o, clazz));
			}else{
				result.add(formatValue(o,clazz));
			}
		}
		return result;
	}
	
	private static String formatValue(Object value){
		if(value == null){
			return "null";
		}else if(value instanceof Date){
			return ((Date)value).getTime()+"";
		}else if(value instanceof String){
			return "\""+value.toString()+"\"";
		}else if(value instanceof List){
			return stringifyList((List)value);
		}else if(isJDKType(value.getClass().getSimpleName())){
			return value.toString();
		}else{
			return stringifyObject(value);
		}
	}
	public static String stringifyObject(Object source){
		
		Class<?> clazz = source.getClass();
		List<Field> fields = getAllField(clazz);
		StringBuilder stringBuilder = new StringBuilder();
		for(Field field : fields){
			try {
				Method method = clazz.getMethod("get"+StringUtil.FU(field.getName()));
				Object value = method.invoke(source);
				stringBuilder.append("\""+field.getName()+"\":"+formatValue(value)+",");
			} catch (Exception e) {
			}
		}
		
		String result = stringBuilder.toString();
		if(result.equals("")){
			result = ",";
		}
		return "{"+result.substring(0,result.length()-1)+"}";
	}
	
	public static String stringifyList(List list){
		StringBuilder stringBuilder = new StringBuilder();
		for(Object obj : list){
			stringBuilder.append(stringify(obj)+",");
		}
		String result = stringBuilder.toString();
		if(result.equals("")){
			result = ",";
		}
		return "["+result.substring(0,result.length()-1)+"]";
	}
	
	public static String stringify(Object source){
		if(source instanceof List){
			return stringifyList((List)source);
		}else{
			return stringifyObject(source);
		}
	}
	
	public static void main(String[] args){
		
		TestVO vo = parseToObject("{\"DATE\":1497254745452,\"listInt\":[{\"DATE\":\"2017-01-01\",\"name\":\"7676\",\"INT\":1,\"LONG\":123,\"BOOLEAN\":false}],\"name\":\"123\",\"INT\":1,\"LONG\":123,\"BOOLEAN\":true,\"testVO\":{\"name\":\"7676\",\"INT\":1,\"LONG\":123,\"BOOLEAN\":true}}",TestVO.class);
//		System.out.println(vo.toString());
//		System.out.println(vo.getListInt().get(0).getBOOLEAN());
		System.out.println(stringify(vo));
	}
}
