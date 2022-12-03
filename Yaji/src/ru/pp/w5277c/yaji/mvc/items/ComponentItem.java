/*--------------------------------------------------------------------------------------------------------------------------------------------------------------
Файл распространяется под лицензией GPL-3.0-or-later, https://www.gnu.org/licenses/gpl-3.0.txt
----------------------------------------------------------------------------------------------------------------------------------------------------------------
xx.xx.2022	konstantin@5277.ru			Начало
--------------------------------------------------------------------------------------------------------------------------------------------------------------*/
package ru.pp.w5277c.yaji.mvc.items;

import java.io.File;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import ru.pp.w5277c.yaji.Config;
import ru.pp.w5277c.yaji.Main;
import ru.pp.w5277c.yaji.Utils;
import ru.pp.w5277c.yaji.components.Component;
import ru.pp.w5277c.yaji.components.LaunchComponent;
import ru.pp.w5277c.yaji.components.UninstallComponent;

public class ComponentItem extends HBox {
	public static abstract class LengthComputeHandler {
		public abstract void update();
	}

	private	final	Component		component;
	private			CheckBox			cb;
	private	final	Label				label			= new Label("...");
	
	public ComponentItem(Component l_component, LengthComputeHandler l_handler) {
		component = l_component;
		
		label.setId("body");
	
		if(l_component instanceof LaunchComponent && ((LaunchComponent)l_component).is_autostart()) {
         cb = new CheckBox(component.get_name() + "(автозагрузка)");
      }
      else {
         cb = new CheckBox(component.get_name());
      }
		cb.setId("body");
		cb.setMinWidth(250);
		cb.setMaxWidth(250);
		cb.setTooltip(new Tooltip(component.get_name()));
		cb.setSelected(component.is_mandatory() || component instanceof UninstallComponent);
		if(component.is_mandatory()) {
			cb.setDisable(true);
			cb.setStyle("-fx-opacity: 0.7");
		}
		cb.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent l_ae) {
				l_handler.update();
			}
		});

		setSpacing(10);
		getChildren().addAll(cb, label);
	
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					long length = Main.os.get_total_size(new File(Config.APP_SOURCE + File.separator + component.get_path()), 0);
					component.set_length(length);
					
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							label.setText(Utils.length_to_human(length));
							l_handler.update();
						}
					});
				}
				catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		}).start();
	}
	
	public boolean is_checked() {
		return cb.isSelected();
	}
	
	public Component get_component() {
		return component;
	}
}
