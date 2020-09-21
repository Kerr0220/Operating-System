package Component;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class Moniter extends JPanel {
	static JLabel result=new JLabel("",JLabel.CENTER);
	JLabel inf=new JLabel("",JLabel.CENTER);
	public Moniter() {
		this.setLayout(null);
		
		setSize(330, 100);
		setBackground(new Color(81, 157, 158));
		JLabel head = new JLabel("---------������Ϣ---------", JLabel.CENTER);
		
		add(head);
		head.setBounds(0, 10, 330, 30);
		head.setBackground(new Color(81, 157, 158));
		head.setOpaque(true);
		head.setForeground(Color.WHITE);
		
		add(result);
		result.setBounds(0, 50, 330, 30);
		result.setBackground(new Color(81, 157, 158));
		result.setOpaque(true);
		result.setForeground(Color.WHITE);
		
		add(inf);
		inf.setBounds(0, 90, 330, 30);
		inf.setBackground(new Color(81, 157, 158));
		inf.setOpaque(true);
		inf.setForeground(Color.WHITE);
	}
	
	public void showInf(int ins,boolean IsContained,int remove) {
		/*
		 * ins������ǰִ�д������ڵ��߼�ҳҳ��
		 * IsContained������ҳ�Ƿ����ڴ���
		 * remove������Ҫ�Ƴ����߼�ҳҳ��
		 */
		if(IsContained) {
			// ���ڴ��У���ʾ��Ϣ
			this.result.setText("��"+ins+"ҳ���ڴ��У�����Ҫ����");
			this.inf.setText("");
		}else {
			// �����ڴ��У���ʾ��Ϣ
			this.result.setText("������"+remove+"ҳ�������"+ins+"ҳ");
			this.inf.setText("");
		}
	}
	
	static public void setResult(double rateOfMiss) {
		result.setText("ȱҳ�ʣ�"+rateOfMiss*100+"%");
	}
	
	public void clear() {
		// �����ʾ��
		this.result.setText("");
		this.inf.setText("");
	}
}
