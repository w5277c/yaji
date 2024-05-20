/*--------------------------------------------------------------------------------------------------------------------------------------------------------------
Файл распространяется под лицензией GPL-3.0-or-later, https://www.gnu.org/licenses/gpl-3.0.txt
----------------------------------------------------------------------------------------------------------------------------------------------------------------
xx.xx.2022	konstantin@5277.ru			Начало
--------------------------------------------------------------------------------------------------------------------------------------------------------------*/
package ru.pp.w5277c.yaji.mvc.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import ru.pp.w5277c.yaji.Config;
import ru.pp.w5277c.yaji.Main;

public class LicenseController extends Controller {
	@FXML
	private Button next_b;
	@FXML
	private Button exit_b;
	@FXML
	private TextArea license_ta;
	@FXML
	private CheckBox accept_cb;
	@FXML
	private Button previous_b;
	@FXML
	private Label appname_label;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		appname_label.setText(Config.APP_NAME);
		
		license_ta.setId("license");
		license_ta.setText(Config.APP_LICENSE);
	}	

	@FXML
	private void next(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/ru/pp/w5277c/yaji/views/Configure.fxml"));
		Parent root = loader.load();
		Main.scene.setRoot(root);
	}

	@FXML
	private void accept(ActionEvent event) {
		next_b.setDisable(!accept_cb.isSelected());
	}

	@FXML
	private void previous(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/ru/pp/w5277c/yaji/views/Welcome.fxml"));
		Parent root = loader.load();
		Main.scene.setRoot(root);
	}

	@FXML
	private void exit(ActionEvent event) {
		if(Main.close_request()) {
			Platform.exit();
		}
	}
}
