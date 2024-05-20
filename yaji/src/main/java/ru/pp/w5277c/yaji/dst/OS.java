/*--------------------------------------------------------------------------------------------------------------------------------------------------------------
Файл распространяется под лицензией GPL-3.0-or-later, https://www.gnu.org/licenses/gpl-3.0.txt
----------------------------------------------------------------------------------------------------------------------------------------------------------------
xx.xx.2022	konstantin@5277.ru			Начало
--------------------------------------------------------------------------------------------------------------------------------------------------------------*/
package ru.pp.w5277c.yaji.dst;

import ru.pp.w5277c.yaji.components.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import ru.pp.w5277c.yaji.Config;
import ru.pp.w5277c.yaji.Main;

public abstract class OS {
	public		byte[]						buffer			= new byte[0x100];
	
	public interface InstallingHandler {
		public void update(int l_percents, String l_info);
	}
	
	public OS() {
	}
	
	public abstract String get_default_path();
	
	public void files_copy(String l_path, LinkedList<Component> l_components, InstallingHandler l_handler) {
		try {
			for(Component component : l_components) {
				File file = new File(Config.APP_SOURCE + File.separator + component.get_path());
				if(-1 == files_copy(file, 0, get_total_size(file, 0), System.currentTimeMillis(), l_path, l_handler)) {
					return;
				}
			}
			l_handler.update(100, "Завершено");
		}
		catch(Exception ex) {
			ex.printStackTrace();
			l_handler.update(-1, "ошибка:" + ex.toString());
		}
	}
	
	protected long files_copy(File l_file, long l_size, long l_total_size, long l_timestamp, String l_path, InstallingHandler l_handler) throws Exception {
		long size = l_size;
		long timestamp = l_timestamp;

		String cur_path = Paths.get("").toAbsolutePath().toString();
		File dst_file = new File(l_path + File.separator + l_file.getAbsolutePath().substring(cur_path.length() + Config.APP_SOURCE.length() + 0x01));

		if(l_file.isDirectory()) {
			boolean succees = false;
			try {
				succees = remove_dir(dst_file);
			}
			catch(Exception ex) {
				ex.printStackTrace();
			}
			if(!succees) {
				l_handler.update(-1, "Ошибка удаления " + dst_file.getAbsolutePath() + "\nЗавершите работу приложения.");
				return -1;
			}
			dst_file.mkdirs();
			
			File[] files = l_file.listFiles();
			if(null != files) {
				for(File file : files) {
					size = files_copy(file, size, l_total_size, timestamp, l_path, l_handler);
					if(-1 == size) {
						return -1;
					}
				}
			}
		}
		else if(!Files.isSymbolicLink(Paths.get(l_file.getPath()))) {
			dst_file.delete();
			if(dst_file.exists()) {
				throw new FileAlreadyExistsException(dst_file.getPath());
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
			fis.close();
			fos.close();
		}
		return size;
	}
	

	public void files_delete() throws FileNotFoundException {
		if(null != Main.inst_record) {
			remove_dir(new File(Main.inst_record.get_path()));
		}
	}
	public static boolean remove_dir(File l_file) throws FileNotFoundException {
		if(l_file.exists()) {
			if(l_file.isDirectory()) {
				for(File file : l_file.listFiles()) {
					if(!remove_dir(file)) {
						return false;
					}
				}
				try{return l_file.delete();}catch(Exception ex){};
			}
			else {
				try{return l_file.delete();}catch(Exception ex){};
			}
		}
		return true;
	}
	
	public long get_total_size(File l_file, long l_total_size) throws Exception {
		long total_size = l_total_size;
		if(l_file.isDirectory()) {
			File[] files = l_file.listFiles();
			if(null != files) {
				for(File file : files) {
					total_size = get_total_size(file, total_size);
				}
			}
		}
		else if(!Files.isSymbolicLink(Paths.get(l_file.getPath()))) {
			total_size+=l_file.length();
		}
		return total_size;
	}

	public abstract boolean make_shortcuts(String l_work_dir, LinkedList<Component> l_components);
	public abstract boolean remove_shortcuts();
	public abstract void native_files_delete();
}
