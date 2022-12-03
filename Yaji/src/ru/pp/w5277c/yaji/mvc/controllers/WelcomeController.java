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
import javafx.scene.control.Label;
import ru.pp.w5277c.yaji.Config;
import ru.pp.w5277c.yaji.enums.EMode;
import ru.pp.w5277c.yaji.Main;
import ru.pp.w5277c.yaji.dst.OS;

public class WelcomeController extends Controller {
	@FXML
	private Label title_l;
	@FXML
	private Button next_b;
	@FXML
	private Label body_l;
	@FXML
	private Button uninstall_b;
	@FXML
	private Button exit_b;
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		title_l.setId("title");
		title_l.setText(title_l.getText() + Config.APP_NAME);
		
		body_l.setId("body");

		switch(Main.mode) {
			case INSTALL:
				body_l.setText("Этот ассистент установит " + Config.APP_NAME + " на Ваш компьютер и даст Вам пошаговые инструкции для установки.\n\n" +
									"Нажмите кнопку 'Далее' для продолжения, или 'Отмена' для выхода из программы.");
				break;
			case REINSTALL:
				body_l.setText("Этот ассистент переустановит " + Config.APP_NAME +
									(Main.inst_record.get_version().isEmpty() ? "" : "(версии " + Main.inst_record.get_version() + ")") +
									" на Вашем компьютере и даст Вам пошаговые инструкции для " +
									"переустановки.\n\nНажмите кнопку 'Далее' для продолжения, или 'Отмена' для выхода из программы.");
				break;
			case UNINSTALL:
				body_l.setText("Этот ассистент удалит " + Config.APP_NAME + " с Вашего компьютера.\n\n" +
									"Нажмите кнопку 'Далее' для продолжения, или 'Отмена' для выхода из программы.");
		}	
		
		if(EMode.UNINSTALL != Main.mode && null != Main.inst_record) {
			uninstall_b.setVisible(true);
		}
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				next_b.requestFocus();
			}
		});
	}		

	@FXML
	private void next(ActionEvent event) throws IOException {
		if(EMode.UNINSTALL != Main.mode) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/ru/pp/w5277c/yaji/mvc/views/License.fxml"));
			Parent root = loader.load();
			Main.scene.setRoot(root);
		}
		else {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/ru/pp/w5277c/yaji/mvc/views/Uninstall.fxml"));
			Parent root = loader.load();
			Main.scene.setRoot(root);
			UninstallController cntr = (UninstallController)loader.getController();
			cntr.fill_data();
		}
	}

	@FXML
	private void uninstall(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/ru/pp/w5277c/yaji/mvc/views/Uninstall.fxml"));
		Parent root = loader.load();
		Main.scene.setRoot(root);
		UninstallController cntr = (UninstallController)loader.getController();
		cntr.fill_data();
	}

	@FXML
	private void exit(ActionEvent event) {
		Platform.exit();
	}
}
