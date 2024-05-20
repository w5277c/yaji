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
import ru.pp.w5277c.yaji.Config;
import ru.pp.w5277c.yaji.InstRecord;
import ru.pp.w5277c.yaji.Main;
import ru.pp.w5277c.yaji.dst.OS;

public class UninstallController extends Controller {
	@FXML
	private Button next_b;
	@FXML
	private ProgressBar progress_bar;
	@FXML
	private CheckBox user_data_cb;
	@FXML
	private Button previous_b;
	@FXML
	private Button exit_b;
	@FXML
	private Label percents_l;
	@FXML
	private Label progress_info_l;
	@FXML
	private Button repeate_b;
	@FXML
	private Label appname_label;

	private	boolean	success	= false;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		appname_label.setText(Config.APP_NAME);
		
		if(null == Config.USER_DATA_DIR || Config.USER_DATA_DIR.isEmpty()) {
			user_data_cb.setSelected(false);
			user_data_cb.setDisable(true);
		}
	}	

	void fill_data() {
		exit_b.setDisable(true);
		repeate_b.setDisable(true);
		next_b.setDisable(true);
		user_data_cb.setDisable(Config.USER_DATA_DIR.isEmpty());
		
		percents_l.setText("0%");
		progress_bar.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
		progress_info_l.setText("Удаление...");
		
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
							percents_l.setText("100%");
							progress_info_l.setText(success ? "Завершено" : "Ошибка при удалении\nУбедитесь что программа закрыта.");
							repeate_b.setDisable(success);
							if(!success) {
								user_data_cb.setDisable(true);
								exit_b.setDisable(false);
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

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/ru/pp/w5277c/yaji/views/Success.fxml"));
		Parent root = loader.load();
		Main.scene.setRoot(root);
		SuccessController cntr = (SuccessController)loader.getController();
		cntr.fill_data("Удаление успешно завершено.");
	}

	@FXML
	private void repeate(ActionEvent event) {
		fill_data();
	}

	private void cancel_action(ActionEvent event) {
		if(Main.close_request()) {
			Platform.exit();
		}
	}

	@FXML
	private void previous(ActionEvent event) {
	}

	@FXML
	private void exit(ActionEvent event) {
	}
}
