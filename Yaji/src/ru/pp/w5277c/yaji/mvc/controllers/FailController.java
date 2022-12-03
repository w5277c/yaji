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

public class FailController extends Controller {
	@FXML
	private Label title_l;
	@FXML
	private Button exit_b;
	@FXML
	private Label body_l;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
	}	

	public void fill_data(String l_text) {
		body_l.setText(l_text);
	}

	@FXML
	private void exit(ActionEvent event) {
		Platform.exit();
	}
}
