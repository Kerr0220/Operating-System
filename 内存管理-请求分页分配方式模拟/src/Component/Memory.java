package Component;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class Memory extends JPanel {
	static public Page[] pages = new Page[4];

	public Memory() {
		setSize(540, 520);
		setBackground(new Color(81, 157, 158));
		this.setOpaque(true);
		setLayout(null);
		JLabel label = new JLabel("模拟内存", JLabel.CENTER);
		label.setFont(new Font("楷体", Font.BOLD, 20));
		label.setForeground(Color.white);
		add(label);
		label.setBounds(0, 0, 540, 30);
		for (int i = 0; i < 4; i++) {
			Page p = new Page();
			add(p);
			p.setLocation(10 + i * 140, 30);
			pages[i] = p;
		}
	}

	public void dispatchPage(int physicPage, int logicalPage) {
		this.pages[physicPage].change(logicalPage);
	}

	public int check(int num) {
		//检查是否有num对应的页在内存中
		//有则返回该页
		//否则返回-1
		for (int i = 0; i < 4; i++) {
			if (this.pages[i].lPage == num / 10) {
				return i;
			}
		}
		return -1;
	}

	public void clear() {
		// 清空内存
		for (int i = 0; i < 4; i++) {
			pages[i].clear();
		}
	}
}
