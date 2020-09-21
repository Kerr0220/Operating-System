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
	static public JButton startButton = new JButton("开始模拟");
	static public JButton clearButton = new JButton("数据清零");
	public boolean[] ins = new boolean[320];
	static public int speed = 509;

	public static void main(String args[]) {
		InitGlobalFont(new Font("楷体", Font.BOLD, 20)); // 统一设置字体
		MainFrame f = new MainFrame();
		f.setVisible(true);
	}

	public MainFrame() {
		// 设置框架的基本属性
		setTitle("操作系统——内存管理项目");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(135, 25, 900, 600);
		getContentPane().setLayout(null);
		getContentPane().setBackground(new Color(157, 200, 200));

		// 添加选择列表
		algSelBar.setLocation(557, 30);
		getContentPane().add(algSelBar);
		algSelBar.chooseList.setSelectedIndex(0);

		// 添加内存块
		getContentPane().add(memory);
		memory.setLocation(10, 30);

		// 添加显示器
		getContentPane().add(moniter);
		moniter.setLocation(557, 450);

		// 添加等待执行的指令队列
		getContentPane().add(waitingList);
		waitingList.setLocation(627, 220);

		// 添加开始按钮
		getContentPane().add(startButton);
		startButton.setBounds(555, 160, 165, 50);
		startButton.setBackground(new Color(209, 182, 225));
		startButton.setFont(new Font("楷体", Font.BOLD, 20));
		startButton.addActionListener(new EventListener() {
			public void actionPerformed(ActionEvent e) {
				waitingList.lists[0].setBackground(new Color(209, 182, 225));
				start();
			}
		});

		// 添加归零按钮
		getContentPane().add(clearButton);
		clearButton.setBounds(725, 160, 165, 50);
		clearButton.setBackground(new Color(209, 182, 225));
		clearButton.setFont(new Font("楷体", Font.BOLD, 20));
		clearButton.addActionListener(new EventListener() {
			public void actionPerformed(ActionEvent e) {
				clear();
			}
		});

		// 添加速度滑块
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
		waitingList.lists[0].setBackground(new Color(88, 201, 185));// 清除等待队列的高亮
		MainFrame.startButton.setEnabled(true);// 恢复开始按钮
		this.memory.clear();// 清除内存
		this.moniter.clear();// 清除显示器
		this.waitingList.clear();// 清空等待队列
		// 清除标识位
		for (int i = 0; i < 320; i++) {
			this.ins[i] = false;
		}
	}

	// FIFO
	public void FIFO() {
		new Thread(new Runnable() {
			// 要实时更新JLabel，所以需要单独开一个线程来刷新线程
			public void run() {
				MainFrame.clearButton.setEnabled(false);// 模拟过程中禁止点击清空按钮
				int cnt_miss = 0;// 记录缺页次数
				int turn = 0;// 先进先出相当于物理页轮流调出其存储的逻辑页，turn用来记录轮转到哪一页
				int num = -1;// 当前要执行的指令的编号
				// 先生成前四个，加到等待队列
				for (int i = 0; i < 4; i++) {
					num = next(i, num);
					waitingList.turn(num);
				}

				// 逐个执行320条指令
				for (int i = 0; i < 320; i++) {
					num = waitingList.insNum[0];
					// 判断在不在里面
					if (memory.check(num) != -1) {
						// 在内存中，显示信息
						show(memory.check(num), num, true, -1);
					} else {
						// 不在内存中，调度
						show(memory.check(num), num, false, turn);
						memory.dispatchPage(turn, num / 10);

						// 高亮显示目标指令所在位置
						Memory.pages[turn].blocks[num % 10].setBackground(new Color(209, 182, 225));
						try {
							Thread.sleep(speed);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Memory.pages[turn].blocks[num % 10].setBackground(new Color(88, 201, 185));

						// 修改相应计数器
						turn++;
						cnt_miss++;
						if (turn > 3) {
							turn = turn % 4;
						}
					}
					// 产生下一个待执行指令
					if (i < 316) {
						num = next(i, waitingList.insNum[3]);
						// 修改等待队列
						waitingList.turn(num);
					} else {
						waitingList.turn(-1);
					}

					// 休眠一段时间以方便观察
					try {
						Thread.sleep(speed);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				// 显示结果——缺页率
				Moniter.setResult(((double) cnt_miss / (double) 320));
				// 恢复清零按钮的功能
				MainFrame.clearButton.setEnabled(true);
			}
		}).start();
	}

	// LRU
	public void LRU() {
		new Thread(new Runnable() {
			// 要实时更新JLabel，所以需要单独开一个线程来刷新线程
			public void run() {
				MainFrame.clearButton.setEnabled(false);// 模拟过程中禁止点击清空按钮
				int cnt_miss = 0;// 记录缺页次数
				int[] free = new int[4];// 记录每个物理页的空闲次数
				int num = -1;// 当前要执行的指令的编号
				// 先生成前四个，加入等待队列
				for (int i = 0; i < 4; i++) {
					num = next(i, num);
					waitingList.turn(num);
				}

				// 逐个执行320条指令
				for (int i = 0; i < 320; i++) {
					num = waitingList.insNum[0];
					// 判断在不在里面
					if (memory.check(num) != -1) {
						// 在内存中
						// 现将各页闲置次数加一
						for (int j = 0; j < 4; j++) {
							free[j]++;
						}
						// 再将当前执行的页限制次数置为零
						free[memory.check(num)] = 0;
						// 显示信息
						show(memory.check(num), num, true, -1);
					} else {
						// 不在内存中，调度
						int turn = 0;// 要调度的页的物理页号
						int longest = 0;// 最长闲置时间
						// 判断调度哪一页
						for (int j = 0; j < 4; j++) {
							if (free[j] > longest) {
								turn = j;
								longest = free[j];
							}
						}
						// 现将各页闲置次数加一
						for (int j = 0; j < 4; j++) {
							free[j]++;
						}
						// 再将当前执行的页限制次数置为零
						free[turn] = 0;
						// 显示信息
						show(memory.check(num), num, false, turn);
						// 调度
						memory.dispatchPage(turn, num / 10);
						// 高亮显示目标指令所在位置
						Memory.pages[turn].blocks[num % 10].setBackground(new Color(209, 182, 225));
						try {
							Thread.sleep(speed);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Memory.pages[turn].blocks[num % 10].setBackground(new Color(88, 201, 185));

						// 修改相应计数器
						cnt_miss++;
					}
					// 产生下一个待执行指令
					if (i < 316) {
						num = next(i, waitingList.insNum[3]);
						// 修改等待队列
						waitingList.turn(num);
					} else {
						waitingList.turn(-1);
					}

					// 休眠一段时间以方便观察
					try {
						Thread.sleep(speed);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				// 显示结果——缺页率
				Moniter.setResult(((double) cnt_miss / (double) 320));
				// 恢复清零按钮的功能
				MainFrame.clearButton.setEnabled(true);
			}
		}).start();
	}

	// 展示在内存中的信息
	public void show(int pageNum, int num, boolean tag, int remove) {
		/*
		 * pageNum——物理页号 num——指令编码 tag——是否命中 remove——调出页的页号
		 */
		if (tag) {
			// 如果命中
			this.moniter.showInf(num, true, -1);// 展示信息
			// 高亮显示指令所在位置
			this.memory.pages[pageNum].blocks[num % 10].setBackground(new Color(209, 182, 225));
			try {
				Thread.sleep(speed);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.memory.pages[pageNum].blocks[num % 10].setBackground(new Color(88, 201, 185));
		} else {
			// 缺页
			int rem = this.memory.pages[remove].lPage;// 计算所调的逻辑页郝
			this.moniter.showInf(num / 10, false, rem);// 显示调页信息
		}

	}

	// 随机选一个还未执行的代码
	public int next(int cnt, int last) {
		// cnt——产生的第几个随机数[0,319]
		// last——上一条执行的指令序号
		// 返回下一条执行的指令序号next
		int next = -1;

		// 上一条是-1表示当前是第一条指令
		// 则从0~319中随机产生一条
		if (last == -1) {
			next = (int) (Math.random() * 320);
			return next;
		}

		// 第偶数条指令随机产生
		if (cnt % 2 == 0) {
			// 第偶数条随机产生的指令从last前面产生
			if ((cnt / 2) % 2 == 0) {
				next = (int) (Math.random() * last);
				int times = 0;
				while (true == this.ins[next]) {
					times++;
					next = (int) (Math.random() * last);
					// 如果last前面全部执行过了
					// 就从所有指令中随机产生下一条
					if (times > last - 1) {
						while (true == this.ins[next]) {
							next = (int) (Math.random() * 320);
						}
					}
				}
				// 修改记录的标签位表示此指令已执行过
				this.ins[next] = true;
			}
			// 第奇数条随机产生的指令从last后面产生
			else {
				next = (int) (last + Math.random() * (320 - last));
				int times = 0;
				while (true == this.ins[next]) {
					times++;
					next = (int) (last + Math.random() * (320 - last));
					// 如果last后面全部执行过了
					// 就从所有指令中随机产生下一条
					if (times > (319 - last)) {
						while (true == this.ins[next]) {
							next = (int) (Math.random() * 320);
						}
					}
				}
				// 修改记录的标签位表示此指令已执行过
				this.ins[next] = true;
			}
		}
		// 第奇数条指令顺序产生——last之后第一条未执行的指令
		else {
			next = last + 1;
			// 如果超过319则需要对320进行模运算
			// 循环到最前面
			if (next > 319) {
				next = next % 320;
			}
			while (true == this.ins[next]) {
				next++;
				if (next > 319) {
					next = next % 320;
				}
			}
			// 修改记录的标签位表示此指令已执行过
			this.ins[next] = true;
		}
		return next;
	}

	// 设置全局字体
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
