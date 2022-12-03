/*--------------------------------------------------------------------------------------------------------------------------------------------------------------
Файл распространяется под лицензией GPL-3.0-or-later, https://www.gnu.org/licenses/gpl-3.0.txt
----------------------------------------------------------------------------------------------------------------------------------------------------------------
xx.xx.2022	konstantin@5277.ru			Начало
--------------------------------------------------------------------------------------------------------------------------------------------------------------*/
package ru.pp.w5277c.yaji;

import ru.pp.w5277c.yaji.enums.EMode;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import ru.pp.w5277c.yaji.dst.OS;
import ru.pp.w5277c.yaji.dst.GNULinux;
import ru.pp.w5277c.yaji.dst.Windows;
import ru.pp.w5277c.yaji.mvc.controllers.FailController;
import ru.pp.w5277c.yaji.mvc.controllers.WelcomeController;

public class Main extends Application {
	public	final	static	String					VERSION			= "v0.2";
	public	final	static	String					INSTDIR			= ".yaji";
	public			static	Scene						scene;
	public			static	OS							os;
	public			static	EMode						mode				= EMode.INSTALL;
	public			static	String					HOMEDIR;
	public			static	InstRecord				inst_record;
	
	@Override
	public void start(Stage l_stage) throws IOException {
		Parent root;
		
		new Config();
		inst_record = InstRecord.read();
		
		if(EMode.UNINSTALL == mode && null == inst_record) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/ru/pp/w5277c/yaji/mvc/views/Fail.fxml"));
			root = loader.load();
			FailController fail_cntr = (FailController)loader.getController();
			fail_cntr.fill_data("Не найдены компоненты для удаления программы " + Config.APP_NAME + ".");
		}
		else {
	      FXMLLoader loader = new FXMLLoader(getClass().getResource("/ru/pp/w5277c/yaji/mvc/views/Welcome.fxml"));
			root = loader.load();
			WelcomeController cntr = (WelcomeController)loader.getController();
		}
		
		scene = new Scene(root);
		scene.getStylesheets().clear();
      scene.getStylesheets().add(getClass().getResource("/resources/default.css").toExternalForm());
      
		l_stage.setTitle("Установка - " + Config.APP_NAME + (Config.APP_VERSION.isEmpty() ? "" : " " + Config.APP_VERSION));
		try {
			l_stage.getIcons().add(new Image(getClass().getResource("/resources/icon.png").toExternalForm()));
		}
		catch(Exception ex) {
		}
      l_stage.setScene(scene);
		l_stage.setResizable(false);
      l_stage.show();
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
				System.out.println("Yaji v." + VERSION + " by K.Udovichenko(konstanin@5277.ru)");
				System.out.println("Usage: java -jar yaji.jar [-l|-w][-u]");
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
			mode = EMode.UNINSTALL;
		}
		else if(null != inst_record) {
			mode = EMode.REINSTALL;
		}

		launch(args);
	}
}
