/*--------------------------------------------------------------------------------------------------------------------------------------------------------------
Файл распространяется под лицензией GPL-3.0-or-later, https://www.gnu.org/licenses/gpl-3.0.txt
----------------------------------------------------------------------------------------------------------------------------------------------------------------
xx.xx.2022	konstantin@5277.ru			Начало
--------------------------------------------------------------------------------------------------------------------------------------------------------------*/
package ru.pp.w5277c.yaji.dst;

import ru.pp.w5277c.yaji.components.Component;
import ru.pp.w5277c.yaji.components.LaunchComponent;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import ru.pp.w5277c.yaji.Config;
import ru.pp.w5277c.yaji.Main;
import mslinks.ShellLink;
import mslinks.ShellLinkHelper;
import ru.pp.w5277c.yaji.components.UninstallComponent;

public class Windows extends OS {
	public	final	static	int	id = 0x00;
	
	@Override
	public String get_default_path() {
		return System.getProperty("user.home") + File.separator;
	}

	@Override
	public boolean make_shortcuts(String l_work_dir, LinkedList<Component> l_components) {
		if(!l_work_dir.endsWith("\\")) {
			l_work_dir = l_work_dir + "\\";
		}
		
		String HOMEDIR = System.getProperty("user.home").replaceAll("\\/", "\\\\");
		String desktop_path =  HOMEDIR + "\\Desktop\\";
		String autostart_path = HOMEDIR + "\\AppData\\Roaming\\Microsoft\\Windows\\Start Menu\\Programs\\Startup\\";
		String applications_path = HOMEDIR + "\\AppData\\Roaming\\Microsoft\\Windows\\Start Menu\\Programs\\" + Config.get_appfilename() + "\\";
		File tmp = new File(applications_path);
		if(!tmp.exists()) {
			tmp.mkdirs();
		}
		
		for(Component component : l_components) {
			if(component instanceof LaunchComponent) {
				LaunchComponent lc = (LaunchComponent)component;

				try {
					if(lc.is_desktop()) {
						ShellLink sl = new ShellLink();
						sl.setWorkingDir(l_work_dir + "\\" + lc.get_path().replaceAll("\\/", "\\\\"));
						sl.setIconLocation(l_work_dir + lc.get_icon_path().replaceAll("\\/", "\\\\") + ".ico");
						sl.setName(lc.get_name());

						Path targetPath = Paths.get(l_work_dir).toAbsolutePath();
						String root = targetPath.getRoot().toString();
						String path = targetPath.subpath(0, targetPath.getNameCount()).toString() + "\\" + lc.get_launcher().replaceAll("\\/", "\\\\") + ".exe";
						ShellLinkHelper slh = new ShellLinkHelper(sl);
						slh.setLocalTarget(root, path, ShellLinkHelper.Options.ForceTypeFile);
						slh.saveTo(desktop_path + lc.get_filename() + ".lnk");
					}
					if(lc.is_autostart()) {
						ShellLink sl = new ShellLink();
						sl.setWorkingDir(l_work_dir + "\\" + lc.get_path().replaceAll("\\/", "\\\\"));
						sl.setIconLocation(l_work_dir + lc.get_icon_path().replaceAll("\\/", "\\\\") + ".ico");
						sl.setName(lc.get_name());

						Path targetPath = Paths.get(l_work_dir).toAbsolutePath();
						String root = targetPath.getRoot().toString();
						String path = targetPath.subpath(0, targetPath.getNameCount()).toString() + "\\" + lc.get_launcher().replaceAll("\\/", "\\\\") + ".exe";
						ShellLinkHelper slh = new ShellLinkHelper(sl);
						slh.setLocalTarget(root, path, ShellLinkHelper.Options.ForceTypeFile);
						slh.saveTo(autostart_path + lc.get_filename() + ".lnk");
					}
					if(lc.is_applications()) {
						ShellLink sl = new ShellLink();
						sl.setWorkingDir(l_work_dir + "\\" + lc.get_path().replaceAll("\\/", "\\\\"));
						sl.setIconLocation(l_work_dir + lc.get_icon_path().replaceAll("\\/", "\\\\") + ".ico");
						sl.setName(lc.get_name());

						Path tp = Paths.get(l_work_dir).toAbsolutePath();
						String root = tp.getRoot().toString();
						String path = tp.subpath(0, tp.getNameCount()).toString() + "\\" + lc.get_launcher().replaceAll("\\/", "\\\\") + ".exe";
						ShellLinkHelper slh = new ShellLinkHelper(sl);
						slh.setLocalTarget(root, path, ShellLinkHelper.Options.ForceTypeFile);
						slh.saveTo(applications_path + lc.get_filename() + ".lnk");
					}
					
					if(!(lc instanceof UninstallComponent) && lc.is_applications() && lc.is_url_assoc()) {
						String reg_command =	"C:\\\\windows\\\\system32\\\\cmd.exe /C \\\"cd /D " +
												(l_work_dir + lc.get_path()).replaceAll("\\/", "\\").replaceAll("\\\\", "\\\\\\\\") +
												"\\\" & launcher.cmd %1";
						StringBuilder sb = new StringBuilder("Windows Registry Editor Version 5.00\r\n\r\n");
						sb.append("[HKEY_CLASSES_ROOT\\" + lc.get_name().toLowerCase() + "]\r\n");
						sb.append("\"URL Protocol\"=\"\"\r\n\r\n");
						sb.append("[HKEY_CLASSES_ROOT\\" + lc.get_name().toLowerCase() + "\\shell]\r\n\r\n");
						sb.append("[HKEY_CLASSES_ROOT\\" + lc.get_name().toLowerCase() + "\\shell\\open]\r\n\r\n");
						sb.append("[HKEY_CLASSES_ROOT\\" + lc.get_name().toLowerCase() + "\\shell\\open\\command]\r\n\r\n");
						sb.append("@=\"" + reg_command + "\"\r\n\r\n");
						
						File file = new File(lc.get_name().toLowerCase() + "_custom_url_" + System.currentTimeMillis() + ".reg");
						FileOutputStream fos = new FileOutputStream(file);
						fos.write(sb.toString().getBytes(StandardCharsets.UTF_8));
						fos.flush();
						fos.close();
						file.deleteOnExit();
						
						ProcessBuilder pb = new ProcessBuilder("regedit", file.getAbsolutePath());
						pb.start();
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
		return true;
	}
	
	@Override
	public boolean remove_shortcuts() {
		String HOMEDIR = System.getProperty("user.home").replaceAll("\\/", "\\\\");
		String desktop_path =  HOMEDIR + "\\Desktop\\";
		String autostart_path = HOMEDIR + "\\AppData\\Roaming\\Microsoft\\Windows\\Start Menu\\Programs\\Startup\\";
		String applications_path = HOMEDIR + "\\AppData\\Roaming\\Microsoft\\Windows\\Start Menu\\Programs\\" + Config.get_appfilename() + "\\";

		if(null != Main.inst_record) {
			for(String item : Main.inst_record.get_components()) {
				File tmp = new File(desktop_path + item + ".lnk");
				if(tmp.exists()) {
					tmp.delete();
				}
				tmp = new File(autostart_path + item + ".lnk");
				if(tmp.exists()) {
					tmp.delete();
				}
			}
		}
		
		File tmp = new File(applications_path);
		try {remove_dir(tmp);}catch(Exception ex){}

		return true;
	}
	
	@Override
	public void native_files_delete() {
		if(null != Main.inst_record) {
			try {
				String HOMEDIR = System.getProperty("user.home").replaceAll("\\/", "\\\\");
				Runtime.getRuntime().exec("cmd /c ping localhost -n 3 > nul && rmdir /s /q " + Main.inst_record.get_path(), null, new File(HOMEDIR));
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}
