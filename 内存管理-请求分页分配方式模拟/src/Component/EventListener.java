package Component;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EventListener implements ActionListener {
	public void actionPerformed(ActionEvent e) {
		System.out.println("I'm listening!");
	}
}
