/*--------------------------------------------------------------------------------------------------------------------------------------------------------------
Файл распространяется под лицензией GPL-3.0-or-later, https://www.gnu.org/licenses/gpl-3.0.txt
----------------------------------------------------------------------------------------------------------------------------------------------------------------
30.11.2022	konstantin@5277.ru			Начало
--------------------------------------------------------------------------------------------------------------------------------------------------------------*/
package ru.pp.w5277c.yaji;

import java.io.File;
import java.io.FileOutputStream;
import java.util.LinkedList;
import java.util.Scanner;
import ru.pp.w5277c.yaji.components.Component;
import ru.pp.w5277c.yaji.components.LaunchComponent;

public class InstRecord {
	private	String					path;
	private	String					version;
	private	LinkedList<String>	components;

	public InstRecord(String l_path, String l_version, LinkedList<Object> l_components) {
		path = l_path;
		version = l_version;
		components = new LinkedList<>();
		
		for(Object component : l_components) {
			if(component instanceof LaunchComponent) {
				components.add(((Component)component).get_filename());
			}
			else if (component instanceof String) {
				components.add((String)component);
			}
		}
	}

	public static InstRecord read() {
		String path = "";
		String version = "";
		LinkedList<Object> components = new LinkedList<>();

		try {
			File dir = new File(Main.HOMEDIR + File.separator + Main.INSTDIR);
         if(dir.exists()) {
				File file = new File(dir.getAbsolutePath() + File.separator + Config.get_appfilename() + ".uinf");
				if(file.exists()) {
					Scanner scan = new Scanner(file);
					while(scan.hasNextLine()) {
						String parts[] = scan.nextLine().split("\\s+", 0x02);
						if(0x02 == parts.length) {
							if(parts[0x00].equalsIgnoreCase("version")) {
								version = parts[0x01];
							}
							else if(parts[0x00].equalsIgnoreCase("path")) {
								path = parts[0x01];
							}
							else if(parts[0x00].equalsIgnoreCase("components")) {
								components.add(parts[0x01]);
							}
						}
					}
					scan.close();
					if(null != path && !path.isEmpty()) {
						return new InstRecord(path, version, components);
					}
				}
			}
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
   public static void write(String l_path, LinkedList<Component> l_components) {
      FileOutputStream fos = null;
		try {
         File dir = new File(Main.HOMEDIR + File.separator + Main.INSTDIR);
         if(!dir.exists()) {
            dir.mkdirs();
         }
         File file = new File(dir.getAbsolutePath() + File.separator + Config.get_appfilename() + ".uinf");
         if(file.exists()) {
            file.delete();
         }
         StringBuilder str = new StringBuilder();
         if(Config.APP_VERSION.isEmpty()) {
				str.append("version\t").append(Config.APP_VERSION).append("\n");
			}
         str.append("path\t").append(l_path.trim()).append("\n");
			for(Component component : l_components) {
				if(component instanceof LaunchComponent) {
					str.append("components\t").append(((LaunchComponent)component).get_filename().trim()).append("\n");
				}
			}
			fos = new FileOutputStream(file);
			fos.write(str.toString().getBytes());
         fos.flush();
         fos.close();
      }
      catch(Exception ex) {
         if(null != fos) try{fos.close();}catch(Exception ex2){}
			ex.printStackTrace();
      }
   }
	
	public static void remove() {
		File dir = new File(Main.HOMEDIR + File.separator + Main.INSTDIR);
		if(dir.exists()) {
			new File(dir.getAbsolutePath() + File.separator + Config.get_appfilename() + ".uinf").delete();
		}
	}
	
	public String get_path() {
		return path;
	}
	
	public String get_version() {
		return version;
	}
	
	public LinkedList<String> get_components() {
		return components;
	}
}
