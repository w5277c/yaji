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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import ru.pp.w5277c.yaji.Config;
import ru.pp.w5277c.yaji.Main;
import ru.pp.w5277c.yaji.Utils;
import ru.pp.w5277c.yaji.components.Component;
import ru.pp.w5277c.yaji.components.LaunchComponent;
import ru.pp.w5277c.yaji.mvc.items.ComponentItem;

public class ConfigureController extends Controller {

	@FXML
	private Label title_l;
	@FXML
	private Button next_b;
	@FXML
	private Button exit_b;
	@FXML
	private TextField path_tf;
	@FXML
	private Button path_b;
	@FXML
	private Label body_l;
	@FXML
	private Label need_space_l;
	@FXML
	private Label have_space_l;
	@FXML
	private VBox components;
	@FXML
	private CheckBox autostart_shortcut_cb;
	@FXML
	private CheckBox desktop_shortcut_cb;
	@FXML
	private CheckBox programs_shortcut_cb;
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		title_l.setText(Config.APP_NAME);
		
		path_tf.setText(Main.os.get_default_path());
		File file = new File(Main.os.get_default_path());
		while(null != file && !file.exists()) {
			file = file.getParentFile();
		}
		if(null != file) {
			have_space_l.setText(Utils.length_to_human(file.getFreeSpace()));
		}
		
		for(Component component : Config.APP_COMPONENTS) {
			components.getChildren().add(new ComponentItem(component, new ComponentItem.LengthComputeHandler() {
				@Override
				public void update() {
					update_volume_info();
				}
			}));
		}
	}	

	private void previous(ActionEvent event) throws IOException {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/ru/pp/w5277c/yaji/mvc/views/Welcome.fxml"));
		Parent root = loader.load();
		Main.scene.setRoot(root);
	}

	@FXML
	private void next(ActionEvent event) throws IOException {
      LinkedList<Component> _tmp = new LinkedList<>();
      for(Node node : components.getChildrenUnmodifiable()) {
         ComponentItem ci = (ComponentItem)node;
         if(ci.is_checked()) {
            if(ci.get_component() instanceof LaunchComponent) {
					LaunchComponent lc = ((LaunchComponent)ci.get_component());
					if(lc.is_autostart() && !autostart_shortcut_cb.isSelected()) {
						lc.set_autostart(false);
					}
					if(lc.is_desktop() && !desktop_shortcut_cb.isSelected()) {
						lc.set_desktop(false);
					}
					if(lc.is_applications()&& !programs_shortcut_cb.isSelected()) {
						lc.set_applications(false);
					}
				}
				_tmp.add(ci.get_component());
         }
      }
      
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/ru/pp/w5277c/yaji/mvc/views/Install.fxml"));
		Parent root = loader.load();
		((InstallController)loader.getController()).fill_data(path_tf.getText() + File.separator + Config.APP_NAME, _tmp);
		Main.scene.setRoot(root);
	}

	@FXML
	private void path_action(ActionEvent event) {
		DirectoryChooser dc = new DirectoryChooser();
      dc.setTitle("Выбор директории");
      File file = new File(path_tf.getText());
		if(file.exists()) {
			dc.setInitialDirectory(new File(path_tf.getText()));
		}
		File dst_dir = dc.showDialog(null);
		if(null != dst_dir) {
			path_tf.setText(dst_dir.getAbsolutePath());
			have_space_l.setText(Utils.length_to_human(dst_dir.getFreeSpace()));
		}
	}

	private void update_volume_info() {
		long length = 0;
		boolean have_checked = false;
		for(Node node : components.getChildren()) {
			ComponentItem item = (ComponentItem)node;
			length += (item.is_checked() ? item.get_component().get_length() : 0);
			have_checked |= item.is_checked();
		}
		next_b.setDisable(!(components.getChildren().isEmpty() || have_checked));
		need_space_l.setText(Utils.length_to_human(length));
	}

	@FXML
	private void exit(ActionEvent event) {
		Platform.exit();
	}
}
