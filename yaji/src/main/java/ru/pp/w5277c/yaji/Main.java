/*--------------------------------------------------------------------------------------------------------------------------------------------------------------
Файл распространяется под лицензией GPL-3.0-or-later, https://www.gnu.org/licenses/gpl-3.0.txt
----------------------------------------------------------------------------------------------------------------------------------------------------------------
xx.xx.2022	konstantin@5277.ru			Начало
--------------------------------------------------------------------------------------------------------------------------------------------------------------*/
package ru.pp.w5277c.yaji;

import ru.pp.w5277c.yaji.enums.EMode;
import java.io.IOException;
import java.util.Optional;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import ru.pp.w5277c.yaji.dst.OS;
import ru.pp.w5277c.yaji.dst.GNULinux;
import ru.pp.w5277c.yaji.dst.Windows;
import ru.pp.w5277c.yaji.mvc.controllers.FailController;
import ru.pp.w5277c.yaji.mvc.controllers.WelcomeController;

public class Main extends Application {
	public	final	static	String					VERSION			= "v0.8";
	public	final	static	String					INSTDIR			= ".yaji";
	public			static	Scene					scene;
	public			static	OS						os;
	public			static	EMode					mode			= EMode.INSTALL;
	public			static	String					HOMEDIR;
	public			static	InstRecord				inst_record;
	private			static	Main					inst;
	
	@Override
	public void start(Stage l_stage) throws IOException {
		Parent root;
		
		inst = this;
		
		l_stage.setMinHeight(500);
		l_stage.setHeight(500);
		l_stage.setMinWidth(700);
		l_stage.setWidth(700);

		new Config();
		inst_record = InstRecord.read();
		
		if((EMode.UNINSTALL == mode || EMode.UNINSTALL_ONLY == mode) && null == inst_record) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/ru/pp/w5277c/yaji/views/Fail.fxml"));
			root = loader.load();
			FailController fail_cntr = (FailController)loader.getController();
			fail_cntr.fill_data("Не найдены компоненты для удаления программы " + Config.APP_NAME + ".");
		}
		else {
	      FXMLLoader loader = new FXMLLoader(getClass().getResource("/ru/pp/w5277c/yaji/views/Welcome.fxml"));
			root = loader.load();
			WelcomeController cntr = (WelcomeController)loader.getController();
		}

		scene = new Scene(root);
		scene.getStylesheets().clear();
		scene.getStylesheets().add(getClass().getResource("/ru/pp/w5277c/yaji/default.css").toExternalForm());

		l_stage.setTitle("Установка - " + Config.APP_NAME + (Config.APP_VERSION.isEmpty() ? "" : " " + Config.APP_VERSION));
		try {
			l_stage.getIcons().add(new Image(getClass().getResource("/ru/pp/w5277c/yaji/icon_48.png").toExternalForm()));
		}
		catch(Exception ex) {
		}
		l_stage.setScene(scene);
		l_stage.show();
		
		l_stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent t) {
				if(!close_request()) {
					t.consume();
				}
			}
		});
	}

	public static void inst_record_reload() {
		inst_record = InstRecord.read();
	}

	public static void main(String[] args) {
		boolean uninstall_arg = false;
		
		HOMEDIR= System.getProperty("user.home");
		if(null == HOMEDIR || HOMEDIR.isEmpty()) {
			HOMEDIR = ".";
		}

		for(String arg : args) {
			if(arg.equalsIgnoreCase("-l")) {
				os = new GNULinux();
			}
			else if(arg.equalsIgnoreCase("-w")) {
				os = new Windows();
			}
			else if(arg.equalsIgnoreCase("-u")) {
				uninstall_arg = true;
			}
			else if(arg.equalsIgnoreCase("-h") || arg.equalsIgnoreCase("/?")) {
				System.out.println("yaji v." + VERSION + " by K.Udovichenko(konstanin@5277.ru)");
				System.out.println("Usage: yaji.jar [-l|-w][-u]");
				System.out.println("\t\t-l - GNU/Linux platform");
				System.out.println("\t\t-w - Windows platform");
				System.out.println("\t\t-u - uninstall mode");
				System.out.println("\t\t-h - this help");
				System.exit(0);
			}
		}

		if(null == os) {
			if(null != System.getProperty("os.name") && System.getProperty("os.name").toLowerCase().contains("windows")) {
				os = new Windows();
			}
			else {
				os = new GNULinux();
			}
		}
		
		if(uninstall_arg) {
			mode = EMode.UNINSTALL_ONLY;
		}
		else if(null != inst_record) {
			mode = EMode.REINSTALL;
		}

		launch(args);
	}
	
	public static boolean close_request() {
		try {
			Alert alert = new Alert(AlertType.NONE);
			DialogPane dialogPane = alert.getDialogPane();
			dialogPane.getStylesheets().add(inst.getClass().getResource("/ru/pp/w5277c/yaji/dialog.css").toExternalForm());
			dialogPane.getStyleClass().add("dialog");

			alert.setTitle("Завершение работы программы");
			alert.setContentText("Вы действительно хотите выйти из программы?");
			ButtonType yes_button = new ButtonType("Да");
			ButtonType no_button = new ButtonType("Нет");
			alert.getButtonTypes().clear();
			alert.getButtonTypes().addAll(yes_button, no_button);
			Optional<ButtonType> result = alert.showAndWait();
			return !(result.get() == null || result.get() == no_button);
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}
	
	public static Main get_inst() {
		return inst;
	}
}
