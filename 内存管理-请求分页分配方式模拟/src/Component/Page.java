package Component;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class Page extends JPanel {
	public Block[] blocks = new Block[10];
	static int cnt = 0;
	public JLabel logicalPage = new JLabel("Âß¼­-1Ò³", JLabel.CENTER);
	public int lPage = -1;

	public Page() {
		this.setSize(100, 480);
		this.setOpaque(true);
		this.setLayout(null);
		this.setBackground(new Color(81, 157, 158));
		this.logicalPage.setFont(new Font("¿¬Ìå", Font.BOLD, 20));
		JLabel label = new JLabel("ÎïÀí" + cnt + "Ò³", JLabel.CENTER);
		label.setFont(new Font("¿¬Ìå", Font.BOLD, 20));
		label.setForeground(Color.white);
		cnt++;
		add(label);
		label.setBounds(0, 0, 100, 30);
		for (int i = 0; i < 10; i++) {
			Block b = new Block();
			add(b);
			b.setBounds(0, (i + 1) * 40, 100, 30);
			blocks[i] = b;
		}

		logicalPage.setForeground(Color.white);
		add(logicalPage);
		logicalPage.setBounds(0, 450, 100, 30);
	}

	public void change(int logicalPage) {
		this.lPage = logicalPage;
		this.logicalPage.setText("Âß¼­" + lPage + "Ò³");
		for (int i = 0; i < 10; i++) {
			this.blocks[i].setText("" + (lPage * 10 + i));
		}
	}

	public void clear() {
		// Çå¿ÕÒ³
		for (int i = 0; i < 10; i++) {
			blocks[i].setText("NULL");
		}
		lPage = -1;
		this.logicalPage.setText("Âß¼­"+lPage+"Ò³");
	}

	public int getLogicalPage() {
		return lPage;
	}
}
