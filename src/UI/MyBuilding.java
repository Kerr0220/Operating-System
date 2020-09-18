package UI;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.JFrame;

import component.Elevator;
import component.EventListener;
import component.Floor;
import component.MyButton;

public class MyBuilding extends JFrame {
	boolean whichFloorIsWaitUp[] = { false, false, false, false, false, false, false, false, false, false, false, false,
			false, false, false, false, false, false, false, false, false, false };

	boolean whichFloorIsWaitDown[] = { false, false, false, false, false, false, false, false, false, false, false,
			false, false, false, false, false, false, false, false, false, false };

	Elevator[] elevators = new Elevator[5];

	public static MyButton[] upButtons = new MyButton[20];
	// for easily to match the floor(1~19)，Redundant 1 button element

	public static MyButton[] downButtons = new MyButton[21];
	// for easily to match the floor(2~20)，Redundant 2 button elements

	// main
	public static void main(String args[]) {
		MyBuilding frame = new MyBuilding();
		frame.setVisible(true);
	}

	// Constructor
	public MyBuilding() {
		setBackground(new Color(102, 204, 255));
		setTitle("\u64CD\u4F5C\u7CFB\u7EDF\u2014\u2014\u7535\u68AF\u8C03\u5EA6\u7B97\u6CD5");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(135, 25, 950, 630);
		setLayout(null);

		// up buttons

		int x = 71, y = 600;
		for (int i = 1; i < 20; i++) {
			MyButton button = new MyButton("/image/up.png", "/image/upH.png", x, y - 30 * i, 30, 30);
			upButtons[i] = button;
			button.btn.addActionListener(new EventListener() {
				public void actionPerformed(ActionEvent e) {
					button.btn.setEnabled(false);
					whichFloorIsWaitUp[(button.num + 1)] = true;
					System.out.println((button.num + 1) + "层请求上行");
					int choice = dispatchAlgorithm(button.num + 1, 1);
					elevators[choice].toWhichFloor[button.num] = true;
				}
			});
			add(button.btn);
		}

		// down buttons
		x = 116;
		y = 600;
		for (int i = 2; i < 21; i++) {
			MyButton button = new MyButton("/image/down.png", "/image/downH.png", x, y - 30 * i, 30, 30);
			downButtons[i] = button;
			button.btn.addActionListener(new EventListener() {
				public void actionPerformed(ActionEvent e) {
					button.btn.setEnabled(false);
					System.out.println(button.num);
					whichFloorIsWaitDown[button.num - 17] = true;
					System.out.println((button.num - 17) + "层请求下行");
					int choice = dispatchAlgorithm(button.num - 17, 0);
					elevators[choice].toWhichFloor[button.num - 18] = true;
				}
			});
			add(button.btn);
		}

		// add elevators

		Elevator elevator1 = new Elevator();
		elevator1.setLocation(150, 0);
		add(elevator1);
		elevators[0] = elevator1;

		Elevator elevator2 = new Elevator();
		elevator2.setLocation(300, 0);
		add(elevator2);
		elevators[1] = elevator2;

		Elevator elevator3 = new Elevator();
		elevator3.setLocation(450, 0);
		add(elevator3);
		elevators[2] = elevator3;

		Elevator elevator4 = new Elevator();
		elevator4.setLocation(600, 0);
		add(elevator4);
		elevators[3] = elevator4;

		Elevator elevator5 = new Elevator();
		elevator5.setLocation(750, 0);
		add(elevator5);
		elevators[4] = elevator5;

		// add floors
		for (int i = 0; i < 20; i++) {
			add(new Floor().floor);
		}

		// start all the threads
		new Thread(elevator1).start();
		new Thread(elevator2).start();
		new Thread(elevator3).start();
		new Thread(elevator4).start();
		new Thread(elevator5).start();
	}

	// dispatch the elevators
	public int dispatchAlgorithm(int callFloor, int up) {
		int elevatorNum = 0;
		int wayLenth[] = { 0, 0, 0, 0, 0 };
		for (int j = 0; j < 5; j++) {
			int currentY = elevators[j].getMyY();
			if (elevators[j].alarm) {
				wayLenth[j] += 2000;
			} else {
				if (elevators[j].IsDown) {
					wayLenth[j] += elevators[j].getFarthest() * 2;
					// Firstly add the distance it takes to run to the farthest destination
					// and then back to the current level.
					wayLenth[j] += currentY - (600 - 30 * callFloor);
					// Plus the journey from the current layer to this layer.
				} else if (elevators[j].IsUp) {
					wayLenth[j] += elevators[j].getFarthest() * 2;
					// Firstly add the distance it takes to run to the farthest destination
					// and then back to the current level.
					wayLenth[j] += (600 - 30 * callFloor) - currentY;
					// Plus the journey from the current layer to this layer.
				} else {
					if (600 - 30 * callFloor >= currentY) {
						wayLenth[j] += (600 - 30 * callFloor) - currentY;
					} else {
						wayLenth[j] += currentY - (600 - 30 * callFloor);
					}
				}
				if ((up == 1) && (this.whichFloorIsWaitDown[callFloor]) && (elevators[j].toWhichFloor[callFloor - 1])) {
					wayLenth[j] += 2000;
				}else if ((up == 0) && (this.whichFloorIsWaitUp[callFloor]) && (elevators[j].toWhichFloor[callFloor - 1])) {
					wayLenth[j] += 2000;
				}
				if (wayLenth[j] < wayLenth[elevatorNum])
					elevatorNum = j;
			}
		}
		return elevatorNum;
	}
}
