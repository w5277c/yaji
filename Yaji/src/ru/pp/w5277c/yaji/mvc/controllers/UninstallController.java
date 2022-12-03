/*--------------------------------------------------------------------------------------------------------------------------------------------------------------
Файл распространяется под лицензией GPL-3.0-or-later, https://www.gnu.org/licenses/gpl-3.0.txt
----------------------------------------------------------------------------------------------------------------------------------------------------------------
28.11.2022	konstantin@5277.ru			Начало
--------------------------------------------------------------------------------------------------------------------------------------------------------------*/
package ru.pp.w5277c.yaji.mvc.controllers;

import java.io.File;
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
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import ru.pp.w5277c.yaji.Config;
import ru.pp.w5277c.yaji.InstRecord;
import ru.pp.w5277c.yaji.Main;
import ru.pp.w5277c.yaji.dst.OS;

public class UninstallController extends Controller {
	@FXML
	private Button next_b;
	@FXML
	private Button cancel_b;
	@FXML
	private Label title_l;
	@FXML
	private Label body_l;
	@FXML
	private ProgressBar progress_bar;
	@FXML
	private Label progress_info;
	@FXML
	private VBox components;
	@FXML
	private CheckBox user_data_cb;

	private	boolean	success = false;
	@FXML
	private Button repeate_b;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		if(null == Config.USER_DATA_DIR || Config.USER_DATA_DIR.isEmpty()) {
			user_data_cb.setSelected(false);
			user_data_cb.setDisable(true);
		}
		repeate_b.setVisible(false);
		repeate_b.setManaged(false);
	}	

	void fill_data() {
		cancel_b.setDisable(true);
		next_b.setDisable(true);
		repeate_b.setVisible(false);
		repeate_b.setManaged(false);
		next_b.setVisible(true);
		next_b.setManaged(true);
		next_b.setDisable(true);
		user_data_cb.setDisable(Config.USER_DATA_DIR.isEmpty());
		
		progress_bar.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
		progress_info.setText("Удаление...");
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					
					try {
						Main.os.files_delete();
						success = true;
						InstRecord.remove();
						Main.os.remove_shortcuts();
					}
					catch(Exception ex) {
						ex.printStackTrace();
					}

					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							next_b.setDisable(false);
							progress_bar.setProgress(100d);
							progress_info.setText(success ? "завершено" : "Ошибка при удалении\nУбедитесь что программа закрыта.");
							repeate_b.setVisible(!success);
							repeate_b.setManaged(!success);
							next_b.setVisible(success);
							next_b.setManaged(success);
							if(!success) {
								user_data_cb.setDisable(true);
								cancel_b.setDisable(false);
							}
						}
					});
				}
				catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}).start();
	}
	
	@FXML
	private void next(ActionEvent l_event) throws IOException {
		if(user_data_cb.isSelected()) {
			OS.remove_dir(new File(Config.USER_DATA_DIR));
		}

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/ru/pp/w5277c/yaji/mvc/views/Success.fxml"));
		Parent root = loader.load();
		Main.scene.setRoot(root);
		SuccessController cntr = (SuccessController)loader.getController();
		cntr.fill_data("Удаление успешно завершено.");
	}

	@FXML
	private void repeate(ActionEvent event) {
		fill_data();
	}

	@FXML
	private void cancel_action(ActionEvent event) {
		Platform.exit();
	}
}
