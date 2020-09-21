package component;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;

public class Floor extends JLabel {
	public JLabel floor;
	static int cnt;

	public Floor() {
		setLayout(null);
		Font font = new Font("Á¥Êé", Font.PLAIN, 18);
		floor = new JLabel(" µÚ" + (20 - cnt) + "²ã");
		floor.setFont(font);
		floor.setOpaque(true);
		if (cnt % 2 == 0) {
			floor.setBackground(Color.white);
		} else {
			floor.setBackground(new Color(215, 224, 248));
		}
		floor.setBounds(0, cnt * 30, 950, 30);
		floor.setVisible(true);
		cnt++;
	}
}
