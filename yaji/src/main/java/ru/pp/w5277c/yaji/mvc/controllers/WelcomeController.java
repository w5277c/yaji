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
	@FXML
	private Label appname_label;
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		appname_label.setText(Config.APP_NAME);
		
		title_l.setId("title");
		title_l.setText(title_l.getText() + " " + Config.APP_NAME);
		
		body_l.setId("body");

		if(EMode.UNINSTALL == Main.mode || EMode.UNINSTALL_ONLY == Main.mode) {
				body_l.setText("Этот ассистент удалит " + Config.APP_NAME + " с Вашего компьютера.\n\n" +
									"ПЕРЕД ПРОДОЛЖЕНИЕМ ЗАКРОЙТЕ ПРИЛОЖЕНИЕ " + Config.APP_NAME + "\n\n" +		
									"Нажмите кнопку 'Далее' для продолжения, или 'Отмена' для выхода из программы.");
		}
		else {
			if(EMode.REINSTALL == Main.mode || null != Main.inst_record) {
				body_l.setText("Этот ассистент переустановит " + Config.APP_NAME +
									(Main.inst_record.get_version().isEmpty() ? "" : "(версии " + Main.inst_record.get_version() + ")") +	" на Вашем компьютере \n" +
									"и даст Вам пошаговые инструкции для переустановки.\n\n" +
									"ПЕРЕД ПРОДОЛЖЕНИЕМ ЗАКРОЙТЕ ПРИЛОЖЕНИЕ " + Config.APP_NAME.toUpperCase() + "\n\n" +
									"Нажмите кнопку 'Далее' для продолжения, или 'Отмена' для выхода из программы.");
				uninstall_b.setVisible(true);
			}
			else {
				body_l.setText("Этот ассистент установит " + Config.APP_NAME + " на Ваш компьютер \nи даст Вам пошаговые инструкции для установки.\n\n" +
									"Нажмите кнопку 'Далее' для продолжения, или 'Отмена' для выхода из программы.");
			}
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
		if(EMode.UNINSTALL != Main.mode && EMode.UNINSTALL_ONLY != Main.mode) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/ru/pp/w5277c/yaji/views/License.fxml"));
			Parent root = loader.load();
			Main.scene.setRoot(root);
		}
		else {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/ru/pp/w5277c/yaji/views/Uninstall.fxml"));
			Parent root = loader.load();
			Main.scene.setRoot(root);
			UninstallController cntr = (UninstallController)loader.getController();
			cntr.fill_data();
		}
	}

	@FXML
	private void uninstall(ActionEvent event) throws IOException {
		Main.mode = EMode.UNINSTALL;
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/ru/pp/w5277c/yaji/views/Uninstall.fxml"));
		Parent root = loader.load();
		Main.scene.setRoot(root);
		UninstallController cntr = (UninstallController)loader.getController();
		cntr.fill_data();
	}

	@FXML
	private void exit(ActionEvent event) {
		if(Main.close_request()) {
			Platform.exit();
		}
	}
}
