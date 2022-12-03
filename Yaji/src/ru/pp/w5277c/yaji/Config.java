/*--------------------------------------------------------------------------------------------------------------------------------------------------------------
Файл распространяется под лицензией GPL-3.0-or-later, https://www.gnu.org/licenses/gpl-3.0.txt
----------------------------------------------------------------------------------------------------------------------------------------------------------------
30.11.2022	konstantin@5277.ru			Начало
--------------------------------------------------------------------------------------------------------------------------------------------------------------*/
package ru.pp.w5277c.yaji;

import ru.pp.w5277c.yaji.components.UninstallComponent;
import ru.pp.w5277c.yaji.components.Component;
import ru.pp.w5277c.yaji.components.LaunchComponent;
import java.io.File;
import java.util.LinkedList;

public class Config {
	public			static	String						APP_NAME			= "APPNAME";
   public			static	String						APP_VERSION    = "APPVERSION";
	public			static	String						APP_DIRNAME		= "";
	public			static	String						APP_SOURCE		= "app";
	public			static	String						APP_LICENSE		= "License...";
	public			static	String						USER_DATA_DIR	= Main.HOMEDIR + File.separator + "." + APP_NAME.toLowerCase();
	public			static	LinkedList<Component>	APP_COMPONENTS	= new LinkedList<>();
	
	public Config() {
		APP_COMPONENTS.add(new Component("jre", "JRE", "", "8", true));
		APP_COMPONENTS.add(new LaunchComponent("app", APP_NAME, "comment", "0.1",
															"app/launcher", "app/icon", "Other;", true, true, true, true));
		APP_COMPONENTS.add(new UninstallComponent("uninstall", "Uninstaller", APP_NAME + " uninstaller", "",	"uninstall/uninstaller", "uninstall/icon",
																"Other;"));
	}
	
	public static String get_appfilename() {
      return APP_NAME.replaceAll("[\"*\\+,\\/:;?<=>\\\\\\[\\]\\|\\s\\.]", "_");
   }
}
