package com.idohoo.eclipse.plugin.rap.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;

import com.idohoo.eclipse.plugin.rap.vo.ClassFieldVO;
import com.idohoo.eclipse.plugin.rap.vo.ClassMethodVO;
import com.idohoo.eclipse.plugin.rap.vo.ClassVO;

public class ClassUtil {
	
	private static Map<String,ClassVO> mapClassVO = new HashMap<String,ClassVO>();
	
	/**
	 * 获取classVO
	 * @param type
	 * @return
	 */
	private static ClassVO getClassVO(String type){
		ClassVO classVO = mapClassVO.get(type);
		if(classVO == null){
			classVO = initVO(type);
			mapClassVO.put(type, classVO);
		}
		return classVO;
	}
	
	/**
	 * 判断是否基础类型
	 * @param type
	 * @return
	 */
	public static boolean isJDKType(String type){
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
	
	/**
	 * 获取数据类型
	 * @param basePackage
	 * @param imports
	 * @param type
	 * @return
	 */
	public static String formatType(String basePackage,Map<String,String> imports,String type){
		if(type.indexOf(".") != -1 || isJDKType(type)){
			return type;
		}
		if(imports.get(type) != null){
			return imports.get(type);
		}
		return basePackage.replaceAll(";$", "")+"."+type;
	}

	/**
	 * 设置package 
	 * @param vo
	 * @param javaString
	 */
	private static void setPackage(ClassVO vo,String javaString){
		Pattern pattern = Pattern.compile("^package\\s+?(\\w.*?);",Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
		Matcher matcher = pattern.matcher(javaString);
		if(matcher.find()){
			vo.setClassPackage(matcher.group(1));
		}
	}
	
	/** 
	 * 提取import
	 * @param vo
	 * @param javaString
	 */
	private static void setImport(ClassVO vo,String javaString){
		Pattern pattern = Pattern.compile("^import\\s+?(\\w.*\\.(.*?));",Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
		Matcher matcher = pattern.matcher(javaString);
		while(matcher.find()){
			vo.getClassImport().put(matcher.group(2), matcher.group(1));
		}
	}
	
	/**
	 * 初始化class
	 * @param vo
	 * @param javaString
	 */
	private static void setClass(ClassVO vo,String javaString){
		//package
		setPackage(vo,javaString);
		//import
		setImport(vo,javaString);
		
		//获取类名
		Pattern pattern = Pattern.compile("^public\\s+class\\s+?(\\w.*?)(\\s|\\{$|$)",Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
		Matcher matcher = pattern.matcher(javaString);
		if(matcher.find()){
			vo.setClassName(matcher.group(1));
			vo.setFullClassName(formatType(vo.getClassPackage(),vo.getClassImport(),matcher.group(1)));
		}
		
		//获取父类
		pattern = Pattern.compile("^public\\s+class.+?extends\\s+?(\\w.*?)(\\s|\\{$|$)",Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
		matcher = pattern.matcher(javaString);
		if(matcher.find()){
			vo.setParentClass(formatType(vo.getClassPackage(),vo.getClassImport(),matcher.group(1)));
		}
		
		//获取接口
		pattern = Pattern.compile("^public\\s+class.+?implements\\s+?(\\w.*?)(\\s|\\{$|$)",Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
		matcher = pattern.matcher(javaString);
		if(matcher.find()){
			vo.setParentInterface(formatType(vo.getClassPackage(),vo.getClassImport(),matcher.group(1)));
		}
		
		//获取注释中的author
		pattern = Pattern.compile("/\\*\\*[\\s\\S]*?@author\\s+?(\\w*?)[\\s|$][\\s\\S]*?\\*?/[\\s\\S]*?public\\s+class\\s+",Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
		matcher = pattern.matcher(javaString);
		if(matcher.find()){
			vo.setAuthor(matcher.group(1));
		}
		
		//获取注解
		pattern = Pattern.compile("([\\s\\S]*?)public\\s+class\\s+",Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
		matcher = pattern.matcher(javaString);
		if(matcher.find()){
			String annotationString = matcher.group(1).replaceAll("/\\*[\\s\\S]*?\\*/", "").replaceAll("//.*", "");
			Pattern annotationPattern = Pattern.compile("^@(.*)?\\((.*?)\\)$",Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
			Matcher annotationMatcher = annotationPattern.matcher(annotationString);
			while(annotationMatcher.find()){
				if(annotationMatcher.group(1).equals("Path") || annotationMatcher.group(1).equals("RequestMapping")){
					String[] params = annotationMatcher.group(2).split(",");
					for(String param : params){
						String[] keyValue = param.split("=");
						if(keyValue.length == 1){
							vo.setUrl(keyValue[0].trim().replaceAll("\"", ""));
							break;
						}else{
							if(keyValue[0].trim().equals("value")){
								vo.setUrl(keyValue[1].trim().replaceAll("\"", ""));
								break;
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * 方法
	 * @param vo
	 * @param javaString
	 */
	private static void setMethods(ClassVO vo,String javaString){
		//获取方法
		//格式必须是
		//  /** 
		//   *
		//   */
		//   @xxxxx
		//   public xxx
		Matcher matcher = Pattern.compile("^public\\s+class\\s+[\\s\\S]*?\\{([\\s\\S]*)\\}",Pattern.CASE_INSENSITIVE | Pattern.MULTILINE).matcher(javaString);
		if(!matcher.find()){
			return;
		}
		String classBody = matcher.group(1);
		Pattern pattern = Pattern.compile("(/\\*\\*[\\s\\S]*?\\*/)([\\s\\S]*?)public\\s+?(\\w.*?)\\s+?([a-z_][a-z0-9_]*?)\\((.*)\\).*$",Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
		matcher = pattern.matcher(classBody);
		while(matcher.find()){
			ClassMethodVO classMethodVO = new ClassMethodVO();
			Matcher m = Pattern.compile("@description\\s+?(.*)$",Pattern.CASE_INSENSITIVE | Pattern.MULTILINE).matcher(matcher.group(1));
			if(m.find()){
				classMethodVO.setDescription(m.group(1));
			}
			
			m = Pattern.compile("@nickname\\s+?(.*)$",Pattern.CASE_INSENSITIVE | Pattern.MULTILINE).matcher(matcher.group(1));
			if(m.find()){
				classMethodVO.setNickname(m.group(1));
			}
			
			String resultType = "";
			m = Pattern.compile("@return\\s+?([a-z_][a-z0-9_]*?)$",Pattern.CASE_INSENSITIVE | Pattern.MULTILINE).matcher(matcher.group(1));
			if(m.find()){
				resultType = m.group(1).trim();
			}
			
			m = Pattern.compile("@author\\s+?(.*)$",Pattern.CASE_INSENSITIVE | Pattern.MULTILINE).matcher(matcher.group(1));
			if(m.find()){
				classMethodVO.setAuthor(m.group(1));
			}
			
			m = Pattern.compile("@(.*)?\\((.*?)\\)\\s*$",Pattern.CASE_INSENSITIVE | Pattern.MULTILINE).matcher(matcher.group(2));
			while(m.find()){
				if(m.group(1).equals("Path") || m.group(1).equals("RequestMapping")){
					Matcher paramsMatcher = Pattern.compile("\\{.*?\\}").matcher(m.group(2));
					String paramsStr = m.group(2);
					while(paramsMatcher.find()){
						paramsStr = paramsStr.replace(paramsMatcher.group(0), paramsMatcher.group(0).trim().replaceAll(",", "@@").replaceAll("\\}$", "@@"));
					}
					String[] params = paramsStr.split(",");
					for(String param : params){
						String[] keyValue = param.split("=");
						if(keyValue.length == 1){
							classMethodVO.setUrl(keyValue[0].trim().replaceAll("\"", ""));
							break;
						}else{
							if(keyValue[0].trim().equals("value")){
								classMethodVO.setUrl(keyValue[1].trim().replaceAll("\"", ""));
							}else if(keyValue[0].trim().equals("method")){
								paramsMatcher = Pattern.compile("\\.(.+?)@@").matcher(keyValue[1].trim());
								while(paramsMatcher.find()){
									classMethodVO.setMethod(classMethodVO.getMethod()+paramsMatcher.group(1)+";");
								}
							}
						}
					}
				}else if(m.group(1).equals("Get") || m.group(1).equals("Post") || 
						m.group(1).equals("Head") || m.group(1).equals("Put") || 
						m.group(1).equals("Delete") || m.group(1).equals("Options") ||
						m.group(1).equals("Trace")){
					classMethodVO.setMethod(classMethodVO.getMethod()+m.group(1).toUpperCase()+";");
				}
			}
			if("".equals(resultType)){
				resultType = matcher.group(3);
			}
			if(matcher.group(3).indexOf("List<") == 0){
				resultType = matcher.group(3).replaceAll("List<(.*?)>", "$1");
				classMethodVO.getResult().setList(true);
			}
			resultType = formatType(vo.getClassPackage(),vo.getClassImport(),resultType);
			classMethodVO.getResult().setJavaType(resultType);
			classMethodVO.setName(matcher.group(4));
			
			String[] params = matcher.group(5).split(",");
			for(String param : params){
				if(StringUtil.isEmpty(param.trim())) continue;
				String[] keyValue = param.replaceAll("\\s+", " ").replaceAll("@.*?\\)", "").split(" ");
				if("HttpServletRequest".equals(keyValue[0])
						|| "HttpServletResponse".equals(keyValue[0])){
					continue;
				}
				ClassFieldVO classFieldVO = new ClassFieldVO();
				String type = keyValue[0],name = keyValue[1];
				if(type.indexOf("List<") == 0){
					type = type.replaceAll("List<(.*?)>", "$1");
					classFieldVO.setList(true);
				}
				classFieldVO.setName(name);
				type = formatType(vo.getClassPackage(),vo.getClassImport(),type);
				classFieldVO.setJavaType(type);
				
				//获取注释
				m = Pattern.compile("@param\\s+?"+name+"\\s+([^\\s]+?)\\s*$",Pattern.CASE_INSENSITIVE | Pattern.MULTILINE).matcher(matcher.group(1));
				if(m.find()){
					classFieldVO.setComment(m.group(1));
				}
				classMethodVO.getParams().add(classFieldVO);
			}
			vo.getMethods().add(classMethodVO);
		}
	}
	
	/**
	 * 初始化fields
	 * @param vo
	 * @param javaString
	 * @return
	 */
	private static void setFields(ClassVO vo,String javaString){
		List<ClassFieldVO> fields = new ArrayList<ClassFieldVO>();
		if(StringUtil.isNotEmpty(vo.getParentClass())){
			ClassVO parentVO = getClassVO(vo.getParentClass());
			if(parentVO != null){
				fields.addAll(parentVO.getFields());
			}
		}
		
		Matcher matcher = Pattern.compile("^public\\s+class\\s+[\\s\\S]*?\\{([\\s\\S]*)\\}",Pattern.CASE_INSENSITIVE | Pattern.MULTILINE).matcher(javaString);
		if(!matcher.find()){
			return;
		}
		String classBody = matcher.group(1);
		matcher = Pattern.compile("(private|protected|public)\\s+?([a-z_][a-z0-9_<>]*?)\\s+?([a-z_][a-z0-9_]*?)(\\s.*;$|=.*;$|;$)",Pattern.CASE_INSENSITIVE | Pattern.MULTILINE).matcher(classBody);
		while(matcher.find()){
			String type = matcher.group(2);
			if(type.equals("static") || type.equals("final")){
				continue;
			}
			String name = matcher.group(3);
			//判断是否有getter 如果没有getter就跳过
			Matcher getterMatcher = Pattern.compile("public\\s+?"+type+"\\s+?get"+StringUtil.FU(name)+"\\(\\).*$",Pattern.CASE_INSENSITIVE | Pattern.MULTILINE).matcher(javaString);
			if(!getterMatcher.find()){
				continue;
			}
			ClassFieldVO classFieldVO = new ClassFieldVO();
			if(type.indexOf("List<") == 0){
				type = type.replaceAll("List<(.*?)>", "$1");
				classFieldVO.setList(true);
			}
			type = formatType(vo.getClassPackage(), vo.getClassImport(),type);
			classFieldVO.setJavaType(type);
			classFieldVO.setName(name);
			if(type.indexOf(".") != -1 && !type.equals(formatType(vo.getClassPackage(), vo.getClassImport(), vo.getClassName()))){
				ClassVO childVO = getClassVO(type);
				if(childVO != null){
					classFieldVO.setFields(childVO.getFields());
				}
			}
			fields.add(classFieldVO);
		}
		//去重复
		Map<String,ClassFieldVO> mapFields = new HashMap<String,ClassFieldVO>();
		for(ClassFieldVO classFieldVO : fields){
			mapFields.put(classFieldVO.getName(), classFieldVO);
		}
		
		//拿注释
		matcher = Pattern.compile("/\\*\\*([\\s\\S]*?)\\*/[\\s\\S]*?(private|protected|public)\\s+?(\\w.*?)\\s+?([a-z_][a-z0-9_]*?)(=.*|\\s.*)?;$",Pattern.CASE_INSENSITIVE | Pattern.MULTILINE).matcher(classBody);
		while(matcher.find()){
			if(matcher.group(3).equals("static") || matcher.group(3).equals("final")){
				continue;
			}
			if(mapFields.get(matcher.group(4)) == null) continue;
			mapFields.get(matcher.group(4)).setComment(matcher.group(1).replaceAll("\\*", "").trim());
		}
		
		for(String key : mapFields.keySet()){
			vo.getFields().add(mapFields.get(key));
		}
	}
	
	/**
	 * 初始化vo
	 */
	public static ClassVO initVO(String className){
		
		String javaString = FileUtil.findInWorkspace(className);
		if(StringUtil.isEmpty(javaString)){
			return null;
		}
		ClassVO classVO = new ClassVO();
		//class
		setClass(classVO,javaString);
		//fields
		setFields(classVO,javaString);
		
		//清除所有的缓存
		mapClassVO.clear();
		return classVO;
	}
	/**
	 * 初始化所有的列
	 * @param vo
	 */
	private static void initFields(ClassVO vo){
		for(ClassMethodVO classMethodVO : vo.getMethods()){
			if(classMethodVO.getResult().getJavaType().indexOf(".")== -1) continue;
			ClassVO classVO = getClassVO(classMethodVO.getResult().getJavaType());
			if(classVO != null){
				classMethodVO.getResult().setFields(classVO.getFields());
			}
			for(ClassFieldVO classFieldVO : classMethodVO.getParams()){
				classVO = getClassVO(classFieldVO.getJavaType());
				if(classVO != null){
					classFieldVO.setFields(classVO.getFields());
				}
			}
		}
	}
	
	/**
	 * 编译
	 * @param javaString
	 * @return
	 */
	public static ClassVO compile(IEditorPart editor){

		ClassVO result = new ClassVO();
		
		String javaString = DocumentUtil.getDocument(editor).get();
		if(editor.getEditorInput() instanceof IFileEditorInput){
			result.setLocation(((IFileEditorInput)editor.getEditorInput()).getFile().getLocationURI().getPath());
		}
		//class
		setClass(result,javaString);
		//methods
		setMethods(result,javaString);
		//fields
		initFields(result);
		
		//清除所有的缓存
		mapClassVO.clear();
		return result;
	}
}
