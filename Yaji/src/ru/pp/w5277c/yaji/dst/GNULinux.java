/*--------------------------------------------------------------------------------------------------------------------------------------------------------------
Файл распространяется под лицензией GPL-3.0-or-later, https://www.gnu.org/licenses/gpl-3.0.txt
----------------------------------------------------------------------------------------------------------------------------------------------------------------
xx.xx.2022	konstantin@5277.ru			Начало
--------------------------------------------------------------------------------------------------------------------------------------------------------------*/
package ru.pp.w5277c.yaji.dst;

import ru.pp.w5277c.yaji.components.Component;
import ru.pp.w5277c.yaji.components.LaunchComponent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Scanner;
import ru.pp.w5277c.yaji.Config;
import ru.pp.w5277c.yaji.Main;

public class GNULinux extends OS {
	public	final	static	int		id				= 0x01;
   public						String	desktop;
	
	public GNULinux() {
       desktop = System.getenv("XDG_DATA_DIRS").toLowerCase();
	}
	
	@Override
	public String get_default_path() {
		return (Main.HOMEDIR + "/Programs/");
	}

   @Override
	protected long files_copy(File l_file, long l_size, long l_total_size, long l_timestamp, String l_path, InstallingHandler l_handler) throws Exception {
		long size = l_size;
		long timestamp = l_timestamp;

		String cur_path = Paths.get("").toAbsolutePath().toString();
		File dst_file = new File(l_path + File.separator + l_file.getAbsolutePath().substring(cur_path.length() + Config.APP_SOURCE.length() + 0x01));

		if(l_file.isDirectory()) {
			dst_file.mkdirs();
			
			File[] files = l_file.listFiles();
			if(null != files) {
				for(File file : files) {
					size = files_copy(file, size, l_total_size, timestamp, l_path, l_handler);
				}
			}
		}
		else if(!Files.isSymbolicLink(Paths.get(l_file.getPath()))) {
			if(dst_file.exists()) {
            dst_file.delete();
         }
         FileInputStream fis = new FileInputStream(l_file);
			FileOutputStream fos = new FileOutputStream(dst_file);
			while(true) {
				if((System.currentTimeMillis() - timestamp) > 300) {
					timestamp = System.currentTimeMillis();
					l_handler.update((int)((100d / (double)l_total_size) * (double)size), dst_file.getAbsolutePath());
				}
				int length = fis.read(buffer);
				if(0 < length) {
					fos.write(buffer, 0x00, length);
					size+=length;
				}
				else {
					break;
				}
			}
         dst_file.setExecutable(l_file.canExecute());
         dst_file.setWritable(l_file.canWrite());
         dst_file.setReadable(l_file.canRead());
			fis.close();
			fos.close();
		}
		return size;
	}

   
	@Override
	public boolean make_shortcuts(String l_work_dir, LinkedList<Component> l_components) {
		try {
         if(desktop.contains("xfce")) {
            //TODO
         }
         else if(desktop.contains("kde")) {
            //TODO
         }
         else {
   			return make_gnome_shortcut(l_work_dir, l_components);
         }
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}
	
	@Override
	public boolean remove_shortcuts() {
		try {
         if(desktop.contains("xfce")) {
            //TODO
         }
         else if(desktop.contains("kde")) {
            //TODO
         }
         else {
   			return remove_gnome_shortcut();
         }
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	private boolean make_gnome_shortcut(String l_dst_path, LinkedList<Component> l_components) throws Exception {
		if(!l_dst_path.endsWith("/")) {
         l_dst_path = l_dst_path + "/";
      }
      String desktop_path = null;
		String applications_path = Main.HOMEDIR + "/.local/share/applications";
		Scanner scanner = new Scanner(new File(Main.HOMEDIR + "/.config/user-dirs.dirs"));
		while(scanner.hasNextLine()) {
			String line = scanner.nextLine();
			int comment_pos = line.indexOf("#");
			if(-1 != comment_pos) {
				line = line.substring(0, comment_pos);
			}
			line = line.trim();
			if(!line.isEmpty() && line.toUpperCase().startsWith("XDG_DESKTOP_DIR")) {
				String[] parts = line.split("=");
				if(0x02 == parts.length) {
					desktop_path = parts[0x01].trim();
					if(desktop_path.startsWith("\"")) desktop_path = desktop_path.substring(0x01);
					if(desktop_path.endsWith("\"")) desktop_path = desktop_path.substring(0x00, desktop_path.length()-0x01);
					desktop_path = desktop_path.trim();
               
               desktop_path = desktop_path.replaceFirst("\\$HOME", Main.HOMEDIR);
               
				}
			}
		}
		scanner.close();
		
		for(Component component : l_components) {
			if(component instanceof LaunchComponent) {
				LaunchComponent lc = (LaunchComponent)component;

				if(lc.is_autostart()) {
					StringBuilder str = new StringBuilder("[Desktop Entry]\n")
					.append("Encoding=UTF-8").append("\n")
					.append("Name=").append(lc.get_name()).append("\n")
					.append("GenericName=").append(lc.get_name()).append("\n")
					.append("Comment=").append(lc.get_comment()).append("\n")
					.append("Exec=nohup ").append(l_dst_path).append(lc.get_launcher()).append(".sh")
					.append(" > /dev/null &\n");
					if(null != lc.get_icon_path() && !lc.get_icon_path().isEmpty()) {
						str.append("Icon=").append(l_dst_path).append(lc.get_icon_path()).append(".png\n");
					}
					str.append("Categories=").append(lc.get_categories()).append("\n")
					.append("Version=").append(lc.get_version()).append("\n")
					.append("Type=Application").append("\n")
					.append("Terminal=0");

					File dir = new File(Main.HOMEDIR + "/.config/autostart/");
					if(!dir.exists()) {
						dir.mkdirs();
					}
					File file = new File(Main.HOMEDIR + "/.config/autostart/" + lc.get_filename() + ".desktop");
					FileOutputStream fos = new FileOutputStream(file);
					fos.write(str.toString().getBytes());
					fos.flush();
					fos.close();
					file.setExecutable(true);
				}

				if(lc.is_desktop()) {
					StringBuilder str = new StringBuilder("[Desktop Entry]\n")
					.append("Encoding=UTF-8").append("\n")
					.append("Name=").append(lc.get_name()).append("\n")
					.append("GenericName=").append(lc.get_name()).append("\n")
					.append("Comment=").append(lc.get_comment()).append("\n")
					.append("Exec=").append(l_dst_path).append(lc.get_launcher()).append(".sh\n");
					if(null != lc.get_icon_path() && !lc.get_icon_path().isEmpty()) {
						str.append("Icon=").append(l_dst_path).append(lc.get_icon_path()).append(".png\n");
					}
					str.append("Categories=").append(lc.get_categories()).append("\n")
					.append("Version=").append(lc.get_version()).append("\n")
					.append("Type=Application").append("\n")
					.append("Terminal=0");

					File file = new File(desktop_path + "/" + lc.get_filename() + ".desktop");
					FileOutputStream fos = new FileOutputStream(file);
					fos.write(str.toString().getBytes());
					fos.flush();
					fos.close();
					file.setExecutable(true);
				}

				if(lc.is_applications()) {
					StringBuilder str = new StringBuilder("[Desktop Entry]\n")
					.append("Encoding=UTF-8").append("\n")
					.append("Name=").append(lc.get_name()).append("\n")
					.append("GenericName=").append(lc.get_name()).append("\n")
					.append("Comment=").append(lc.get_comment()).append("\n")
					.append("Exec=").append(l_dst_path).append(lc.get_launcher()).append(".sh\n");
					if(null != lc.get_icon_path() && !lc.get_icon_path().isEmpty()) {
						str.append("Icon=").append(l_dst_path).append(lc.get_icon_path()).append(".png\n");
					}
					str.append("Categories=").append(lc.get_categories()).append("\n")
					.append("Version=").append(lc.get_version()).append("\n")
					.append("Type=Application").append("\n")
					.append("Terminal=0");

					File dir = new File(applications_path);
					if(!dir.exists()) {
						dir.mkdirs();
					}
					File file = new File(applications_path + "/" + lc.get_filename() + ".desktop");
					FileOutputStream fos = new FileOutputStream(file);
					fos.write(str.toString().getBytes());
					fos.flush();
					fos.close();
					file.setExecutable(true);
				}
			}
		}
		return true;
	}
	
	private boolean remove_gnome_shortcut() throws Exception {
      if(null != Main.inst_record) {
			String desktop_path = null;
			String applications_path = Main.HOMEDIR + "/.local/share/applications";
			Scanner scanner = new Scanner(new File(Main.HOMEDIR + "/.config/user-dirs.dirs"));
			while(scanner.hasNextLine()) {
				String line = scanner.nextLine();
				int comment_pos = line.indexOf("#");
				if(-1 != comment_pos) {
					line = line.substring(0, comment_pos);
				}
				line = line.trim();
				if(!line.isEmpty() && line.toUpperCase().startsWith("XDG_DESKTOP_DIR")) {
					String[] parts = line.split("=");
					if(0x02 == parts.length) {
						desktop_path = parts[0x01].trim();
						if(desktop_path.startsWith("\"")) desktop_path = desktop_path.substring(0x01);
						if(desktop_path.endsWith("\"")) desktop_path = desktop_path.substring(0x00, desktop_path.length()-0x01);
						desktop_path = desktop_path.trim();

						desktop_path = desktop_path.replaceFirst("\\$HOME", Main.HOMEDIR);

					}
				}
			}
			scanner.close();

			for(String component_filename : Main.inst_record.get_components()) {
				new File(Main.HOMEDIR + "/.config/autostart/" + component_filename + ".desktop").delete();
				new File(desktop_path + "/" + component_filename + ".desktop").delete();
				new File(applications_path + "/" + component_filename + ".desktop").delete();
			}
		}
		return true;
	}

	@Override
	public void native_files_delete() {
		//TODO
		//Runtime.getRuntime().exec("cmd /c ping localhost -n 6 > nul && del Delete.jar");
	}
}
