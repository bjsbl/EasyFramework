package org.easy.framekwork.plugin;

import java.util.ArrayList;
import java.util.List;

public class PluginHandler {

	private static final List<Plugin> pluginList = new ArrayList<Plugin>();

	public static List<Plugin> getPluginList() {
		return pluginList;
	}
}
