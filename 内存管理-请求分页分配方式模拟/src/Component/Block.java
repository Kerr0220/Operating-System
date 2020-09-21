package Component;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class Block extends JLabel {
	public Block() {
		setLayout(null);
		setText("NULL");
		setHorizontalAlignment(SwingConstants.CENTER);
		this.setBackground(new Color(88,201,185));
		setOpaque(true);
		setForeground(Color.white);
		setFont(new Font("¿¬Ìå", Font.BOLD, 20));
	}
}
