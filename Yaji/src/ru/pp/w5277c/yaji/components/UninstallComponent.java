/*--------------------------------------------------------------------------------------------------------------------------------------------------------------
Файл распространяется под лицензией GPL-3.0-or-later, https://www.gnu.org/licenses/gpl-3.0.txt
----------------------------------------------------------------------------------------------------------------------------------------------------------------
xx.xx.2022	konstantin@5277.ru			Начало
--------------------------------------------------------------------------------------------------------------------------------------------------------------*/
package ru.pp.w5277c.yaji.components;

public class UninstallComponent extends LaunchComponent {
   public UninstallComponent( String l_path, String l_name, String l_comment, String l_version, String l_launcher, String l_icon_path,
										String l_categories) {
      super(l_path, l_name, l_comment, l_version, l_launcher, l_icon_path, l_categories, false, false, true, false);
   }
}
