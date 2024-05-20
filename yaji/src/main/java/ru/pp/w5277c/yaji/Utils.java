/*--------------------------------------------------------------------------------------------------------------------------------------------------------------
Файл распространяется под лицензией GPL-3.0-or-later, https://www.gnu.org/licenses/gpl-3.0.txt
----------------------------------------------------------------------------------------------------------------------------------------------------------------
xx.xx.2022	konstantin@5277.ru			Начало
--------------------------------------------------------------------------------------------------------------------------------------------------------------*/
package ru.pp.w5277c.yaji;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class Utils {
	public static String length_to_human(long l_lenth) {
		DecimalFormat format = new DecimalFormat("0.#");
		format.setRoundingMode(RoundingMode.HALF_UP);
		if(l_lenth > (1024 * 1024 * 1024)) {
			return format.format(((double)l_lenth) / (1024 * 1024 * 1024d)) + " ГБ";
		}
		else if(l_lenth > (1024 * 1024)) {
			return format.format(((double)l_lenth) / (1024 * 1024d)) + " МБ";
		}
		else if(l_lenth > 1024) {
			return format.format(((double)l_lenth) / (1024d)) + " КБ";
		}
		return format.format((double)l_lenth) + " Б";
	}
}
