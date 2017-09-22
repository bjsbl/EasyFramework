package org.easy.framekwork.mvc;

import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.easy.framekwork.ioc.BeanFactory;
import org.easy.framekwork.orm.TableHandler;
import org.easy.framekwork.plugin.Plugin;
import org.easy.framekwork.plugin.PluginHandler;
import org.easy.framekwork.util.ClassUtil;

@WebListener
public class ContainerListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		List<Plugin> pluginList = PluginHandler.getPluginList();
		for (Plugin plugin : pluginList) {
			plugin.destroy();
		}
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		ClassUtil.loadClass(ActionHandler.class.getName());
		ClassUtil.loadClass(BeanFactory.class.getName());
		ClassUtil.loadClass(TableHandler.class.getName());
	}

}
