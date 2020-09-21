package Component;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class WaitingList extends JPanel {
	public JLabel[] lists = new JLabel[4];
	public int[] insNum = new int[4];

	public WaitingList() {
		this.setSize(200, 190);
		this.setLayout(null);
		this.setBackground(new Color(157, 200, 200));
		JLabel title = new JLabel("等待执行的指令队列", JLabel.CENTER);
		title.setForeground(Color.WHITE);
		add(title);
		title.setBounds(0, 0, 200, 30);
		for (int i = 0; i < 4; i++) {
			insNum[i] = -1;
			JLabel l = new JLabel("" + insNum[i], JLabel.CENTER);
			lists[i] = l;
			l.setBackground(new Color(88, 201, 185));
			l.setOpaque(true);
			l.setForeground(Color.WHITE);
			add(l);
			l.setBounds(50, (i + 1) * 40, 100, 30);
		}
	}

	public void clear() {
		// 清空等待列表
		for (int i = 0; i < 4; i++) {
			this.insNum[i] = -1;
			this.lists[i].setText("" + insNum[i]);
		}
	}

	public void turn(int newIns) {
		for (int i = 0; i < 3; i++) {
			insNum[i] = insNum[i + 1];
			lists[i].setText("" + insNum[i]);
		}
		insNum[3] = newIns;
		lists[3].setText("" + insNum[3]);
		return;
	}
}
