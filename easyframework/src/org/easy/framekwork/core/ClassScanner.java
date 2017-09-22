package org.easy.framekwork.core;

import java.io.File;
import java.io.FileFilter;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.easy.framekwork.log.Logger;
import org.easy.framekwork.util.ClassUtil;
import org.easy.framekwork.util.StringUtil;

public class ClassScanner {

	private Logger log = Logger.getLogger(this.getClass());

	public List<Class<?>> getClassListByAnnotation(String packageName, Class<? extends Annotation> annotationClass) {
		return invokeFile(packageName, annotationClass);
	}

	public final List<Class<?>> invokeFile(String packageName, Class<? extends Annotation> annotationClass) {
		List<Class<?>> classList = new ArrayList<Class<?>>();
		try {
			Enumeration<URL> urls = ClassUtil.getClassLoader().getResources(packageName.replace(".", "/"));
			while (urls.hasMoreElements()) {
				URL url = urls.nextElement();
				if (url != null) {
					String protocol = url.getProtocol();
					if (protocol.equals("file")) {
						String packagePath = url.getPath().replaceAll("%20", " ");
						addClass(classList, packagePath, packageName, annotationClass);
					}
//					} else if (protocol.equals("jar")) {
//						JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
//						JarFile jarFile = jarURLConnection.getJarFile();
//						Enumeration<JarEntry> jarEntries = jarFile.entries();
//						while (jarEntries.hasMoreElements()) {
//							JarEntry jarEntry = jarEntries.nextElement();
//							String jarEntryName = jarEntry.getName();
//							if (jarEntryName.endsWith(".class")) {
//								String className = jarEntryName.substring(0, jarEntryName.lastIndexOf(".")).replaceAll("/", ".");
//								Class<?> cls = ClassUtil.loadClass(className, false);
//								if (cls.isAnnotationPresent(annotationClass)) {
//									classList.add(cls);
//								}
//							}
//						}
//					}
				}
			}
		} catch (Exception e) {
			log.error("获取类出错！", e);
		}
		return classList;
	}

	private void addClass(List<Class<?>> classList, String packagePath, String packageName, Class<? extends Annotation> annotationClass) {
		try {
			File[] files = new File(packagePath).listFiles(new FileFilter() {
				@Override
				public boolean accept(File file) {
					return (file.isFile() && file.getName().endsWith(".class")) || file.isDirectory();
				}
			});
			for (File file : files) {
				String fileName = file.getName();
				if (file.isFile()) {
					String className = fileName.substring(0, fileName.lastIndexOf("."));
					if (StringUtil.isNotEmpty(packageName)) {
						className = packageName + "." + className;
					}
					Class<?> cls = ClassUtil.loadClass(className, false);
					if (cls.isAnnotationPresent(annotationClass)) {
						classList.add(cls);
						log.info("-----------" + packagePath + "----" + cls.getName());
					}
				} else {
					String subPackagePath = fileName;
					if (StringUtil.isNotEmpty(packagePath)) {
						subPackagePath = packagePath + "/" + subPackagePath;
					}
					String subPackageName = fileName;
					if (StringUtil.isNotEmpty(packageName)) {
						subPackageName = packageName + "." + subPackageName;
					}
					addClass(classList, subPackagePath, subPackageName, annotationClass);
				}
			}
		} catch (Exception e) {
			log.error("添加类出错！", e);
		}
	}
}
