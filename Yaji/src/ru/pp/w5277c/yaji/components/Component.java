/*--------------------------------------------------------------------------------------------------------------------------------------------------------------
Файл распространяется под лицензией GPL-3.0-or-later, https://www.gnu.org/licenses/gpl-3.0.txt
----------------------------------------------------------------------------------------------------------------------------------------------------------------
xx.xx.2022	konstantin@5277.ru			Начало
--------------------------------------------------------------------------------------------------------------------------------------------------------------*/
package ru.pp.w5277c.yaji.components;

public class Component {
	private	String	path;
	private	String	name;
   private  String   comment;
   private  String   version;
   private	boolean	mandatory;
	private	long		length;
   
   
	
	public Component(String l_path, String l_name, String l_comment, String l_version, boolean l_mandatory) {
		path = l_path;
		name = l_name;
      comment = l_comment;
      version = l_version;
		mandatory = l_mandatory;
	}
	
	public String get_path() {
		return path;
	}
	
	public String get_name() {
		return name;
	}
	public String get_filename() {
		return name.replaceAll("[\"*\\+,\\/:;?<=>\\\\\\[\\]\\|\\s\\.]", "_");
	}

   public String get_comment() {
      return comment;
   }
   
   public String get_version() {
      return version;
   }
   
	public boolean is_mandatory() {
		return mandatory;
	}
	
	public void set_length(long l_length) {
		length = l_length;
	}
	public long get_length() {
		return length;
	}
}
