package com.idohoo.eclipse.plugin.rap.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;

/**
 * 文件相关的类
 * @author xhz
 *
 */
public class FileUtil {

	public static String findInWorkspace(String className){
		for(IProject project : ResourcesPlugin.getWorkspace().getRoot().getProjects()){
			IResource resource = project.findMember(("src/"+className.replace(".", "/")+".java"));
			if(resource == null){
				resource = project.findMember(("src/main/java/"+className.replace(".", "/")+".java"));
			}
			if(resource == null){
				continue;
			}
			try {
				//BufferedReader bufferReader = new BufferedReader(new FileReader(project.getLocationURI().getPath()+File.separator+resource.getProjectRelativePath().toString()));
				BufferedReader bufferReader = new BufferedReader(new InputStreamReader(new FileInputStream(project.getLocationURI().getPath()+File.separator+resource.getProjectRelativePath().toString()),"UTF-8"));
				StringBuilder result = new StringBuilder();
				String s = null;
				while((s = bufferReader.readLine())!=null){
					result.append(System.lineSeparator()+s);
				}
				bufferReader.close();
				return result.toString();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "";
	}
}
