/*--------------------------------------------------------------------------------------------------------------------------------------------------------------
Файл распространяется под лицензией GPL-3.0-or-later, https://www.gnu.org/licenses/gpl-3.0.txt
----------------------------------------------------------------------------------------------------------------------------------------------------------------
xx.xx.2022	konstantin@5277.ru			Начало
--------------------------------------------------------------------------------------------------------------------------------------------------------------*/
package ru.pp.w5277c.yaji.mvc.controllers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tooltip;
import ru.pp.w5277c.yaji.Config;
import ru.pp.w5277c.yaji.InstRecord;
import ru.pp.w5277c.yaji.Main;
import ru.pp.w5277c.yaji.components.Component;
import ru.pp.w5277c.yaji.dst.OS;

public class InstallController extends Controller {
	@FXML
	private Button next_b;
	@FXML
	private ProgressBar progress_bar;
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

	private	String					inst_path;
	private	LinkedList<Component>	_components;
	private	boolean					success			= false;
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		appname_label.setText(Config.APP_NAME);
	}	

	public void fill_data(String l_path, LinkedList<Component> l_components) {
		inst_path = l_path;
		_components = l_components;
		
		next_b.setDisable(true);
		repeate_b.setDisable(true);
		progress_bar.setProgress(0d);
		percents_l.setText("0%");
		progress_info_l.setText("подготовка...");
		progress_info_l.setTooltip(new Tooltip(progress_info_l.getText()));
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					new File(l_path).mkdirs();
					
					Main.os.files_copy(l_path, l_components, new OS.InstallingHandler() {
						@Override
						public void update(int l_percents, String l_info) {
							Platform.runLater(new Runnable() {
								@Override
								public void run() {
									if(-1 == l_percents) {
										progress_bar.setProgress(0d);
										percents_l.setText("0%");
										next_b.setDisable(false);
										repeate_b.setDisable(false);
										exit_b.setDisable(true);
									}
									else if(100 == l_percents && 1d != progress_bar.getProgress()) {
										progress_bar.setProgress(1d);
										percents_l.setText("100%");
										next_b.setDisable(false);
										exit_b.setDisable(true);
										success = true;
										
										InstRecord.write(l_path, l_components);
										
										progress_info_l.setText("Создаем ярлыки");
										if(Main.os.make_shortcuts(l_path, l_components)) {
											progress_info_l.setText("выполнено");
										}
										else {
											progress_info_l.setText("!ошибка при создании ярлыков!");
										}
										
									}
									else {
										progress_bar.setProgress(l_percents/100d);
										percents_l.setText(l_percents + "%");
									}
									progress_info_l.setText(l_info);
									progress_info_l.getTooltip().setText(l_info);
								}
							});
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
	private void next(ActionEvent event) throws IOException {
		if(success) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/ru/pp/w5277c/yaji/views/Success.fxml"));
			Parent root = loader.load();
			Main.scene.setRoot(root);
			SuccessController cntr = (SuccessController)loader.getController();
			cntr.fill_data("Установка успешно завершена.  Используйте стандартные механизмы для запуска приложения.",
								inst_path, _components);
		}
		else {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/ru/pp/w5277c/yaji/views/Fail.fxml"));
			Parent root = loader.load();
			Main.scene.setRoot(root);
			FailController cntr = (FailController)loader.getController();
			cntr.fill_data("Установка выполнена с ошибками");
		}
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

	@FXML
	private void repeate(ActionEvent event) {
		fill_data(inst_path, _components);
	}
}
