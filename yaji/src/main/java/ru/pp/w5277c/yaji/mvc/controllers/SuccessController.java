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
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import ru.pp.w5277c.yaji.Config;
import ru.pp.w5277c.yaji.enums.EMode;
import ru.pp.w5277c.yaji.Main;
import ru.pp.w5277c.yaji.components.Component;
import ru.pp.w5277c.yaji.components.LaunchComponent;
import ru.pp.w5277c.yaji.dst.Windows;

public class SuccessController extends Controller {
	@FXML
	private Label title_l;
	@FXML
	private Button exit_b;
	@FXML
	private Label body_l;
	@FXML
	private CheckBox launch_cb;
	@FXML
	private CheckBox install_cb;
	@FXML
	private Label appname_label;

	private	String			inst_path;
	private	LaunchComponent	launch_comp	= null;
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		appname_label.setText(Config.APP_NAME);
	}	

	public void fill_data(String l_text) {
		fill_data(l_text, null, null);
	}
	public void fill_data(String l_text, String l_inst_path, LinkedList<Component> l_components) {
		inst_path = l_inst_path;
		
		body_l.setText(l_text);
		
		if(null != l_components) {
			for(Component comp : l_components) {
				if(comp instanceof LaunchComponent) {
					launch_comp = (LaunchComponent)comp;
					break;
				}
			}
		}

		launch_cb.setVisible(EMode.INSTALL == Main.mode || EMode.REINSTALL == Main.mode);
		launch_cb.setManaged(EMode.INSTALL == Main.mode || EMode.REINSTALL == Main.mode);
		launch_cb.setDisable(null == launch_comp);
		install_cb.setVisible(EMode.UNINSTALL == Main.mode);
		install_cb.setManaged(EMode.UNINSTALL == Main.mode);
	}

	@FXML
	private void exit(ActionEvent event) {
		if(EMode.UNINSTALL == Main.mode || EMode.UNINSTALL_ONLY == Main.mode) {
			Main.os.native_files_delete();
		}

		if(EMode.UNINSTALL == Main.mode && install_cb.isSelected()) {
			Main.mode = EMode.INSTALL;
			Main.inst_record_reload();

			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/ru/pp/w5277c/yaji/views/Welcome.fxml"));
				Parent root = loader.load();
				Main.scene.setRoot(root);
			}
			catch(Exception ex) {
				ex.printStackTrace();
			}
		}
		else if((EMode.INSTALL == Main.mode || EMode.REINSTALL == Main.mode) && launch_cb.isSelected()) {
			try {
				String launcher_path = inst_path + File.separator + launch_comp.get_launcher();
				int pos = launcher_path.lastIndexOf("/");
				String path = launcher_path.substring(0, pos);
				if(Main.os instanceof Windows) {
					path = path.replaceAll("\\/", "\\\\");
					launcher_path = launcher_path.replaceAll("\\/", "\\\\") + ".exe";
				}
				else {
					launcher_path += ".sh";
				}
				
				ProcessBuilder pb = new ProcessBuilder(launcher_path);
				pb.directory(new File(path));
				pb.start();
				
				
			}
			catch(IOException ex) {
				ex.printStackTrace();
			}
			Platform.exit();
		}
		else {
			Platform.exit();
		}
				  
	}
}
