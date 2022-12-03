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
import javafx.scene.layout.VBox;
import ru.pp.w5277c.yaji.InstRecord;
import ru.pp.w5277c.yaji.Main;
import ru.pp.w5277c.yaji.components.Component;
import ru.pp.w5277c.yaji.dst.OS;

public class InstallController extends Controller {
	@FXML
	private Label title_l;
	@FXML
	private Button next_b;
	@FXML
	private Label body_l;
	@FXML
	private ProgressBar progress_bar;
	@FXML
	private Label progress_info;
	@FXML
	private VBox components;
	@FXML
	private Button cancel_b;

	private	boolean	success = false;
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
	}	

	public void fill_data(String l_path, LinkedList<Component> l_components) {
		progress_bar.setProgress(0d);
		progress_info.setText("подготовка...");
		
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
										next_b.setDisable(false);
										cancel_b.setDisable(true);
									}
									else if(100 == l_percents && 1d != progress_bar.getProgress()) {
										progress_bar.setProgress(1d);
										next_b.setDisable(false);
										cancel_b.setDisable(true);
										success = true;
										
										InstRecord.write(l_path, l_components);
										
										progress_info.setText("Создаем ярлыки");
										if(Main.os.make_shortcuts(l_path, l_components)) {
											progress_info.setText("выполнено");
										}
										else {
											progress_info.setText("!ошибка при создании ярлыков!");
										}
										
									}
									else {
										progress_bar.setProgress(l_percents/100d);
									}
									progress_info.setText(l_info);
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
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/ru/pp/w5277c/yaji/mvc/views/Success.fxml"));
			Parent root = loader.load();
			Main.scene.setRoot(root);
			SuccessController cntr = (SuccessController)loader.getController();
			cntr.fill_data("Установка успешно завершена.  Используйте стандартные механизмы для запуска приложения.");
		}
		else {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/ru/pp/w5277c/yaji/mvc/views/Fail.fxml"));
			Parent root = loader.load();
			Main.scene.setRoot(root);
			FailController cntr = (FailController)loader.getController();
			cntr.fill_data("Установка выполнена с ошибками");
		}
	}

	@FXML
	private void cancel_action(ActionEvent event) {
		Platform.exit();
	}
}
