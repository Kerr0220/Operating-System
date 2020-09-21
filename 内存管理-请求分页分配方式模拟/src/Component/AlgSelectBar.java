package Component;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

public class AlgSelectBar extends JPanel {
	public String[] items = new String[] { " FIFO-�Ƚ��ȳ��㷨", " LRU--�������ʹ��ҳ����̭�㷨"};
	public JList<String> chooseList = new JList<String>(items); // ����JList		
	public AlgSelectBar() {
		setLayout(null);
		setBounds(550, 60, 330, 120);
		this.setBackground(new Color(81, 157, 158));
		// ��ӱ���
		JLabel label=new JLabel("-----��ѡ������㷨-----",JLabel.CENTER);
		add(label);
		label.setBounds(0, 0, 330, 40);
		label.setForeground(Color.WHITE);
		// ���ѡ���б�
		add(chooseList);
		chooseList.setBounds(0, 42, 330, 80);
		chooseList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		chooseList.setBackground(new Color(81, 157, 158));
		chooseList.setForeground(Color.WHITE);
	}
}
