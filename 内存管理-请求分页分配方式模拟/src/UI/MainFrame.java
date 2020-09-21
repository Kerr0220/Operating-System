package UI;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.Enumeration;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.FontUIResource;

import Component.AlgSelectBar;
import Component.EventListener;
import Component.Memory;
import Component.Moniter;
import Component.SpeedSlider;
import Component.WaitingList;

public class MainFrame extends JFrame {
	public AlgSelectBar algSelBar = new AlgSelectBar();
	static public Memory memory = new Memory();
	public Moniter moniter = new Moniter();
	public WaitingList waitingList = new WaitingList();
	static public JButton startButton = new JButton("��ʼģ��");
	static public JButton clearButton = new JButton("��������");
	public boolean[] ins = new boolean[320];
	static public int speed = 509;

	public static void main(String args[]) {
		InitGlobalFont(new Font("����", Font.BOLD, 20)); // ͳһ��������
		MainFrame f = new MainFrame();
		f.setVisible(true);
	}

	public MainFrame() {
		// ���ÿ�ܵĻ�������
		setTitle("����ϵͳ�����ڴ������Ŀ");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(135, 25, 900, 600);
		getContentPane().setLayout(null);
		getContentPane().setBackground(new Color(157, 200, 200));

		// ���ѡ���б�
		algSelBar.setLocation(557, 30);
		getContentPane().add(algSelBar);
		algSelBar.chooseList.setSelectedIndex(0);

		// ����ڴ��
		getContentPane().add(memory);
		memory.setLocation(10, 30);

		// �����ʾ��
		getContentPane().add(moniter);
		moniter.setLocation(557, 450);

		// ��ӵȴ�ִ�е�ָ�����
		getContentPane().add(waitingList);
		waitingList.setLocation(627, 220);

		// ��ӿ�ʼ��ť
		getContentPane().add(startButton);
		startButton.setBounds(555, 160, 165, 50);
		startButton.setBackground(new Color(209, 182, 225));
		startButton.setFont(new Font("����", Font.BOLD, 20));
		startButton.addActionListener(new EventListener() {
			public void actionPerformed(ActionEvent e) {
				waitingList.lists[0].setBackground(new Color(209, 182, 225));
				start();
			}
		});

		// ��ӹ��㰴ť
		getContentPane().add(clearButton);
		clearButton.setBounds(725, 160, 165, 50);
		clearButton.setBackground(new Color(209, 182, 225));
		clearButton.setFont(new Font("����", Font.BOLD, 20));
		clearButton.addActionListener(new EventListener() {
			public void actionPerformed(ActionEvent e) {
				clear();
			}
		});

		// ����ٶȻ���
		SpeedSlider speedSlider = new SpeedSlider();
		this.getContentPane().add(speedSlider);
		speedSlider.setLocation(597, 420);
		speedSlider.speed_.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				MainFrame.speed = 1009 - speedSlider.speed_.getValue();
			}
		});
	}

	// start
	public void start() {
		this.startButton.setEnabled(false);
		int index = this.algSelBar.chooseList.getSelectedIndex();
		if (0 == index) {
			FIFO();
		} else {
			LRU();
		}
	}

	// clear
	public void clear() {
		waitingList.lists[0].setBackground(new Color(88, 201, 185));// ����ȴ����еĸ���
		MainFrame.startButton.setEnabled(true);// �ָ���ʼ��ť
		this.memory.clear();// ����ڴ�
		this.moniter.clear();// �����ʾ��
		this.waitingList.clear();// ��յȴ�����
		// �����ʶλ
		for (int i = 0; i < 320; i++) {
			this.ins[i] = false;
		}
	}

	// FIFO
	public void FIFO() {
		new Thread(new Runnable() {
			// Ҫʵʱ����JLabel��������Ҫ������һ���߳���ˢ���߳�
			public void run() {
				MainFrame.clearButton.setEnabled(false);// ģ������н�ֹ�����հ�ť
				int cnt_miss = 0;// ��¼ȱҳ����
				int turn = 0;// �Ƚ��ȳ��൱������ҳ����������洢���߼�ҳ��turn������¼��ת����һҳ
				int num = -1;// ��ǰҪִ�е�ָ��ı��
				// ������ǰ�ĸ����ӵ��ȴ�����
				for (int i = 0; i < 4; i++) {
					num = next(i, num);
					waitingList.turn(num);
				}

				// ���ִ��320��ָ��
				for (int i = 0; i < 320; i++) {
					num = waitingList.insNum[0];
					// �ж��ڲ�������
					if (memory.check(num) != -1) {
						// ���ڴ��У���ʾ��Ϣ
						show(memory.check(num), num, true, -1);
					} else {
						// �����ڴ��У�����
						show(memory.check(num), num, false, turn);
						memory.dispatchPage(turn, num / 10);

						// ������ʾĿ��ָ������λ��
						Memory.pages[turn].blocks[num % 10].setBackground(new Color(209, 182, 225));
						try {
							Thread.sleep(speed);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Memory.pages[turn].blocks[num % 10].setBackground(new Color(88, 201, 185));

						// �޸���Ӧ������
						turn++;
						cnt_miss++;
						if (turn > 3) {
							turn = turn % 4;
						}
					}
					// ������һ����ִ��ָ��
					if (i < 316) {
						num = next(i, waitingList.insNum[3]);
						// �޸ĵȴ�����
						waitingList.turn(num);
					} else {
						waitingList.turn(-1);
					}

					// ����һ��ʱ���Է���۲�
					try {
						Thread.sleep(speed);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				// ��ʾ�������ȱҳ��
				Moniter.setResult(((double) cnt_miss / (double) 320));
				// �ָ����㰴ť�Ĺ���
				MainFrame.clearButton.setEnabled(true);
			}
		}).start();
	}

	// LRU
	public void LRU() {
		new Thread(new Runnable() {
			// Ҫʵʱ����JLabel��������Ҫ������һ���߳���ˢ���߳�
			public void run() {
				MainFrame.clearButton.setEnabled(false);// ģ������н�ֹ�����հ�ť
				int cnt_miss = 0;// ��¼ȱҳ����
				int[] free = new int[4];// ��¼ÿ������ҳ�Ŀ��д���
				int num = -1;// ��ǰҪִ�е�ָ��ı��
				// ������ǰ�ĸ�������ȴ�����
				for (int i = 0; i < 4; i++) {
					num = next(i, num);
					waitingList.turn(num);
				}

				// ���ִ��320��ָ��
				for (int i = 0; i < 320; i++) {
					num = waitingList.insNum[0];
					// �ж��ڲ�������
					if (memory.check(num) != -1) {
						// ���ڴ���
						// �ֽ���ҳ���ô�����һ
						for (int j = 0; j < 4; j++) {
							free[j]++;
						}
						// �ٽ���ǰִ�е�ҳ���ƴ�����Ϊ��
						free[memory.check(num)] = 0;
						// ��ʾ��Ϣ
						show(memory.check(num), num, true, -1);
					} else {
						// �����ڴ��У�����
						int turn = 0;// Ҫ���ȵ�ҳ������ҳ��
						int longest = 0;// �����ʱ��
						// �жϵ�����һҳ
						for (int j = 0; j < 4; j++) {
							if (free[j] > longest) {
								turn = j;
								longest = free[j];
							}
						}
						// �ֽ���ҳ���ô�����һ
						for (int j = 0; j < 4; j++) {
							free[j]++;
						}
						// �ٽ���ǰִ�е�ҳ���ƴ�����Ϊ��
						free[turn] = 0;
						// ��ʾ��Ϣ
						show(memory.check(num), num, false, turn);
						// ����
						memory.dispatchPage(turn, num / 10);
						// ������ʾĿ��ָ������λ��
						Memory.pages[turn].blocks[num % 10].setBackground(new Color(209, 182, 225));
						try {
							Thread.sleep(speed);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Memory.pages[turn].blocks[num % 10].setBackground(new Color(88, 201, 185));

						// �޸���Ӧ������
						cnt_miss++;
					}
					// ������һ����ִ��ָ��
					if (i < 316) {
						num = next(i, waitingList.insNum[3]);
						// �޸ĵȴ�����
						waitingList.turn(num);
					} else {
						waitingList.turn(-1);
					}

					// ����һ��ʱ���Է���۲�
					try {
						Thread.sleep(speed);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				// ��ʾ�������ȱҳ��
				Moniter.setResult(((double) cnt_miss / (double) 320));
				// �ָ����㰴ť�Ĺ���
				MainFrame.clearButton.setEnabled(true);
			}
		}).start();
	}

	// չʾ���ڴ��е���Ϣ
	public void show(int pageNum, int num, boolean tag, int remove) {
		/*
		 * pageNum��������ҳ�� num����ָ����� tag�����Ƿ����� remove��������ҳ��ҳ��
		 */
		if (tag) {
			// �������
			this.moniter.showInf(num, true, -1);// չʾ��Ϣ
			// ������ʾָ������λ��
			this.memory.pages[pageNum].blocks[num % 10].setBackground(new Color(209, 182, 225));
			try {
				Thread.sleep(speed);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.memory.pages[pageNum].blocks[num % 10].setBackground(new Color(88, 201, 185));
		} else {
			// ȱҳ
			int rem = this.memory.pages[remove].lPage;// �����������߼�ҳ��
			this.moniter.showInf(num / 10, false, rem);// ��ʾ��ҳ��Ϣ
		}

	}

	// ���ѡһ����δִ�еĴ���
	public int next(int cnt, int last) {
		// cnt���������ĵڼ��������[0,319]
		// last������һ��ִ�е�ָ�����
		// ������һ��ִ�е�ָ�����next
		int next = -1;

		// ��һ����-1��ʾ��ǰ�ǵ�һ��ָ��
		// ���0~319���������һ��
		if (last == -1) {
			next = (int) (Math.random() * 320);
			return next;
		}

		// ��ż����ָ���������
		if (cnt % 2 == 0) {
			// ��ż�������������ָ���lastǰ�����
			if ((cnt / 2) % 2 == 0) {
				next = (int) (Math.random() * last);
				int times = 0;
				while (true == this.ins[next]) {
					times++;
					next = (int) (Math.random() * last);
					// ���lastǰ��ȫ��ִ�й���
					// �ʹ�����ָ�������������һ��
					if (times > last - 1) {
						while (true == this.ins[next]) {
							next = (int) (Math.random() * 320);
						}
					}
				}
				// �޸ļ�¼�ı�ǩλ��ʾ��ָ����ִ�й�
				this.ins[next] = true;
			}
			// �����������������ָ���last�������
			else {
				next = (int) (last + Math.random() * (320 - last));
				int times = 0;
				while (true == this.ins[next]) {
					times++;
					next = (int) (last + Math.random() * (320 - last));
					// ���last����ȫ��ִ�й���
					// �ʹ�����ָ�������������һ��
					if (times > (319 - last)) {
						while (true == this.ins[next]) {
							next = (int) (Math.random() * 320);
						}
					}
				}
				// �޸ļ�¼�ı�ǩλ��ʾ��ָ����ִ�й�
				this.ins[next] = true;
			}
		}
		// ��������ָ��˳���������last֮���һ��δִ�е�ָ��
		else {
			next = last + 1;
			// �������319����Ҫ��320����ģ����
			// ѭ������ǰ��
			if (next > 319) {
				next = next % 320;
			}
			while (true == this.ins[next]) {
				next++;
				if (next > 319) {
					next = next % 320;
				}
			}
			// �޸ļ�¼�ı�ǩλ��ʾ��ָ����ִ�й�
			this.ins[next] = true;
		}
		return next;
	}

	// ����ȫ������
	private static void InitGlobalFont(Font font) {
		FontUIResource fontRes = new FontUIResource(font);
		for (Enumeration<Object> keys = UIManager.getDefaults().keys(); keys.hasMoreElements();) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value instanceof FontUIResource) {
				UIManager.put(key, fontRes);
			}
		}
	}
}
