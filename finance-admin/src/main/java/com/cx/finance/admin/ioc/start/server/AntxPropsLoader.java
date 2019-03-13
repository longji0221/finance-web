package com.cx.finance.admin.ioc.start.server;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import com.cx.finance.admin.ioc.start.Bootstrap4Jetty;
import com.cx.finance.common.util.GetHostIpUtil;
/**
 * 加载properties
 * @author rongbo
 *
 */
public class AntxPropsLoader {

	Properties pros = new Properties();
	public static String NOTIFY_HOST;
	
	public AntxPropsLoader() {
		load();
	}

	public void load() {
		String antxPath = Bootstrap4Jetty.PARENT_ROOT_PATH + "conf/finance_" + Bootstrap4Jetty.ENV_TYPE + ".properties";
		try {
			pros.load(new FileInputStream(antxPath));
			
			NOTIFY_HOST = "http://" + GetHostIpUtil.getIpAddress() + ":" + Bootstrap4Jetty.PORT;
			pros.setProperty("fbapi.notify.host", NOTIFY_HOST);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Properties getPros() {
		return pros;
	}
	
	public String getProperty(String key) {
		return pros.getProperty(key);
	}
}
