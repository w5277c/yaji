/*--------------------------------------------------------------------------------------------------------------------------------------------------------------
Файл распространяется под лицензией GPL-3.0-or-later, https://www.gnu.org/licenses/gpl-3.0.txt
----------------------------------------------------------------------------------------------------------------------------------------------------------------
xx.xx.2022	konstantin@5277.ru			Начало
--------------------------------------------------------------------------------------------------------------------------------------------------------------*/
package ru.pp.w5277c.yaji.components;

public class LaunchComponent extends Component {
   protected	String   launcher;
   protected	String   icon_path;
   protected	String   categories;
   protected	boolean  desktop;
   protected	boolean	applications;
	protected	boolean  autostart;
   
   public LaunchComponent( String l_path, String l_name, String l_comment, String l_version, String l_launcher, String l_icon_path,
                           String l_categories, boolean l_autostart, boolean l_desktop, boolean l_applications , boolean l_mandatory) {
      super(l_path, l_name, l_comment, l_version, l_mandatory);
      
      launcher = l_launcher;
      icon_path = l_icon_path;
      categories = l_categories;
      autostart = l_autostart;
		desktop = l_desktop;
		applications = l_applications;
   }
   
   public String get_launcher() {
      return launcher;
   }
   
   public String get_icon_path() {
      return icon_path;
   }
   
   public String get_categories() {
      return categories;
   }
   
   public boolean is_autostart() {
      return autostart;
   }
	public void set_autostart(boolean l_autostart) {
		autostart = l_autostart;
	}
	public boolean is_desktop() {
      return desktop;
   }
	public void set_desktop(boolean l_desktop) {
		desktop = l_desktop;
	}
	public boolean is_applications() {
      return applications;
   }
	public void set_applications(boolean l_applications) {
		applications = l_applications;
	}
}
