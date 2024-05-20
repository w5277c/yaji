/*--------------------------------------------------------------------------------------------------------------------------------------------------------------
Файл распространяется под лицензией GPL-3.0-or-later, https://www.gnu.org/licenses/gpl-3.0.txt
----------------------------------------------------------------------------------------------------------------------------------------------------------------
xx.xx.2022	konstantin@5277.ru			Начало
--------------------------------------------------------------------------------------------------------------------------------------------------------------*/
package ru.pp.w5277c.yaji.mvc.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import ru.pp.w5277c.yaji.Config;

public class FailController extends Controller {
	@FXML
	private Button exit_b;
	@FXML
	private Label body_l;
	@FXML
	private Label appname_label;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		appname_label.setText(Config.APP_NAME);
	}	

	public void fill_data(String l_text) {
		body_l.setText(l_text);
	}

	@FXML
	private void exit(ActionEvent event) {
		Platform.exit();
	}
}
