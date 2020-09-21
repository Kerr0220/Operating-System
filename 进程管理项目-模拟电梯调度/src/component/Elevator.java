package component;

import java.awt.Font;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import UI.MyBuilding;

public class Elevator extends JPanel implements Runnable {
	// Elevator's inside buttons
	Buttons buttons;

	// Elevator's doors
	final public Image doorL;
	final public Image doorR;

	// label
	public JLabel label;

	// states
	public boolean IsOpen;// whether the doors are open
	public boolean IsUp;// whether the elevator is going up
	public boolean IsDown;// whether the elevator is going down
	public boolean IsRun;// whether the elevator is running
	public boolean toWhichFloor[] = { false, false, false, false, false, false, false, false, false, false, false,
			false, false, false, false, false, false, false, false, false };// go to which floor
	public boolean alarm;
	// door's location
	private int x = 20;
	private int y = 570;
	// elevator's number
	static int cnt = 0;
	
	//Constructor
	public Elevator() {
		cnt++;
		this.setLayout(null);
		this.setSize(150, 650);
		this.setOpaque(false);
		this.IsOpen = false;
		this.IsUp = false;
		this.IsDown = false;
		this.IsRun = false;
		this.alarm = false;
		// this.setBackground(new Color(153, 204, 255));

		// add doors
		doorL = new ImageIcon(this.getClass().getResource("/image/door.png")).getImage();
		doorR = new ImageIcon(this.getClass().getResource("/image/door.png")).getImage();

		// add label
		Font font = new Font("¡• È", Font.PLAIN, 20);
		label = new JLabel(cnt + "∫≈µÁÃ›");
		label.setFont(font);
		label.setSize(100, 20);
		label.setLocation(68, 35);
		add(label);

		// add buttons
		buttons = new Buttons();
		add(buttons);
		buttons.setLocation(65, 60);

		// add listener
		for (int i = 0; i < 20; i++) {
			MyButton btn = buttons.buttons[i];
			btn.btn.addActionListener(new EventListener() {
				public void actionPerformed(ActionEvent e) {
					btn.btn.setEnabled(false);
					toWhichFloor[btn.num] = true;
				}
			});
		}

		buttons.btnAlarm.btn.addActionListener(new EventListener() {
			public void actionPerformed(ActionEvent e) {
				alarm = true;
				buttons.setFloor(-1);
				buttons.btnAlarm.btn.setEnabled(false);
				alarming();
			}
		});

		buttons.btnOpen.btn.addActionListener(new EventListener() {
			public void actionPerformed(ActionEvent e) {
				buttons.btnOpen.btn.setEnabled(false);
				openDoor();
			}
		});

		buttons.btnClose.btn.addActionListener(new EventListener() {
			public void actionPerformed(ActionEvent e) {
				buttons.btnClose.btn.setEnabled(false);
				closeDoor();
			}
		});
	}

	// rewrite JPanel's function paint()
	public void paint(Graphics g) {
		super.paint(g);
		g.drawImage(doorR, x, y, 13, 30, null);
		g.drawImage(doorL, 33 + (20 - x), y, 13, 30, null);
	}
	// rewrite Thread's function run()
	public void run() {
		while (true) {
			if (alarm) {
				alarming();
				break;
			}
			dispatch();
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	//let elevator move to the certain floor
	public void moveToFloor(int floor) {
		if(y==600-floor*30) {//right here
			if(floor>1) {
				if(!MyBuilding.downButtons[20 - (y / 30)].btn.isEnabled()) {
					MyBuilding.downButtons[20 - (y / 30)].btn.setEnabled(true);
				}else {
					MyBuilding.upButtons[20 - (y / 30)].btn.setEnabled(true);
				}
			}
			toWhichFloor[20 - (y / 30 + 1)] = false;
			return;
		}
		while (y > 600 - floor * 30) {
			IsUp = true;
			IsDown = false;
			IsRun = true;
			if (alarm) {
				return;
			}
			y -= 1;
			this.repaint();
			if (y % 30 == 0) {
				this.buttons.setFloor(20 - (y / 30));
				if (20 - (y / 30) != floor && toWhichFloor[20 - (y / 30 + 1)]) {
					// If it finds a passing floor that needs to be stopped during the ascent, 
					// it need to stop. 
					if (MyBuilding.upButtons[20 - (y / 30)].btn.isEnabled()
							&& MyBuilding.downButtons[20 - (y / 30)].btn.isEnabled()) {
						IsRun = false;
						openDoor();
						buttons.buttons[20 - (y / 30 + 1)].btn.setEnabled(true);
						try {// wait for a second
							Thread.sleep(2000);
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						closeDoor();
						toWhichFloor[20 - (y / 30 + 1)] = false;
					} else {
						if (!MyBuilding.upButtons[20 - (y / 30)].btn.isEnabled()) {
							MyBuilding.upButtons[20 - (y / 30)].btn.setEnabled(true);
							IsRun = false;
							openDoor();
							buttons.buttons[20 - (y / 30 + 1)].btn.setEnabled(true);
							try {// wait for a second
								Thread.sleep(2000);
							} catch (InterruptedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							closeDoor();
							toWhichFloor[20 - (y / 30 + 1)] = false;
							IsUp = false;
							IsDown = true;
						}
						if (!MyBuilding.downButtons[20 - (y / 30)].btn.isEnabled()) {
							if (this.getFarthest() == y) {
								toWhichFloor[20 - (y / 30 + 1)] = false;
								IsRun = false;
								openDoor();
								buttons.buttons[20 - (y / 30 + 1)].btn.setEnabled(true);
								try {// wait for a second
									Thread.sleep(2000);
								} catch (InterruptedException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								closeDoor();
								MyBuilding.downButtons[20 - (y / 30)].btn.setEnabled(true);
								IsUp = false;
								IsDown = true;
							} else {
								toWhichFloor[20 - (y / 30 + 1)] = true;
							}
						}
					}
				}
			}
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		while (y < 600 - floor * 30) {
			IsDown = true;
			IsUp = false;
			IsRun = true;
			if (alarm) {
				return;
			}
			y += 1;
			this.repaint();
			if(y % 30 == 0) {
				this.buttons.setFloor(20 - (y / 30));
				if (20 - (y / 30) != floor && toWhichFloor[20 - (y / 30 + 1)]) {
					// If it find a passing floor that needs to be stopped during the descent,
					// it need to stop.
					if (MyBuilding.downButtons[20 - (y / 30)].btn.isEnabled()
							&& MyBuilding.upButtons[20 - (y / 30)].btn.isEnabled()) {
						IsRun = false;
						openDoor();
						buttons.buttons[20 - (y / 30 + 1)].btn.setEnabled(true);
						try {// wait for a second
							Thread.sleep(2000);
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						closeDoor();
						toWhichFloor[20 - (y / 30 + 1)] = false;
					} else {
						if (!MyBuilding.downButtons[20 - (y / 30)].btn.isEnabled()) {
							MyBuilding.downButtons[20 - (y / 30)].btn.setEnabled(true);
							IsRun = false;
							openDoor();
							buttons.buttons[20 - (y / 30 + 1)].btn.setEnabled(true);
							try {// wait for a second
								Thread.sleep(2000);
							} catch (InterruptedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							closeDoor();
							toWhichFloor[20 - (y / 30 + 1)] = false;
							IsUp = true;
							IsDown = false;
						}
						if (!MyBuilding.upButtons[20 - (y / 30)].btn.isEnabled()) {
							if (this.getFarthest() == y) {
								toWhichFloor[20 - (y / 30 + 1)] = false;
								IsRun = false;
								openDoor();
								buttons.buttons[20 - (y / 30 + 1)].btn.setEnabled(true);
								try {// wait for a second
									Thread.sleep(2000);
								} catch (InterruptedException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								closeDoor();
								MyBuilding.upButtons[20 - (y / 30)].btn.setEnabled(true);
								IsUp = true;
								IsDown = false;
							} else {
								toWhichFloor[20 - (y / 30 + 1)] = true;
							}
						}
					}
				}
			}
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		boolean tag = false;
		if (floor > 1 && (!MyBuilding.downButtons[floor].btn.isEnabled())) {
			if (IsDown) {
				MyBuilding.downButtons[floor].btn.setEnabled(true);
				tag = true;
			} 
				else {
				if (this.getFarthest() == 0) {
					toWhichFloor[20 - (y / 30 + 1)] = false;
					tag = true;
					MyBuilding.downButtons[floor].btn.setEnabled(true);
					IsDown = true;
					IsUp = false;
				}
			}
		}
		else if (floor < 20 && (!MyBuilding.upButtons[floor].btn.isEnabled())) {
			if(!tag) {
				if (IsUp) {
					MyBuilding.upButtons[floor].btn.setEnabled(true);
				} else if (IsDown) {
					if (this.getFarthest() == 0) {
						toWhichFloor[20 - (y / 30 + 1)] = false;
						MyBuilding.upButtons[floor].btn.setEnabled(true);
						IsDown = false;
						IsUp = true;
					}
				}
			}
		}
		toWhichFloor[floor - 1] = false;
		IsRun = false;
		IsUp=false;
		IsDown=false;
	}
	
	// open the door when it's allowed
	private void openDoor() {
		IsOpen = true;
		buttons.btnClose.btn.setEnabled(true);
		while (x > 15) {
			if (IsRun) {
				break;
			}
			x -= 1;
			this.repaint();
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	//close the door
	private void closeDoor() {
		while (x < 20) {
			x += 1;
			this.repaint();
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		IsOpen = false;
	}

	//alarming  
	private void alarming() {
		if (alarm) {
			for (int i = 0; i < 20; i++) {
				buttons.buttons[i].btn.setDisabledIcon(new ImageIcon(this.getClass().getResource("/image/" + (i + 1) + "A.png")));
			}
			buttons.btnOpen.btn.setDisabledIcon(new ImageIcon(this.getClass().getResource("/image/openA.png")));
			buttons.btnClose.btn.setDisabledIcon(new ImageIcon(this.getClass().getResource("/image/closeA.png")));
			
			for (int i = 0; i < 20; i++) {
				buttons.buttons[i].btn.setEnabled(false);
			}
			buttons.btnOpen.btn.setEnabled(false);
			buttons.btnClose.btn.setEnabled(false);
		}
	}

	// lonely dispatch this elevator 
	public void dispatch() {
		while ((!IsOpen) && (!alarm)) {
			buttons.btnOpen.btn.setEnabled(true);
			for (int i = 0; i < 20; i++) {
				if (toWhichFloor[i]) {
					moveToFloor(i + 1);
					openDoor();
					buttons.buttons[i].btn.setEnabled(true);
					try {// wait for a second
						Thread.sleep(2000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					closeDoor();
				}
			}
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		closeDoor();
	}

	// return the elevator's vertical location
	public int getMyY() {
		return y;
	}

	// return this elevator's current floor
	public int getCurrentFloor() {
		int floor = (600 - y) / 30;
		return floor;
	}

	// get this elevator's farthest destination 
	public int getFarthest() {
		int currentFloor = getCurrentFloor();
		if (IsUp) {
			for (int i = 19; i > currentFloor - 1; i--) {
				if (toWhichFloor[i]) {
					return getMyY() - (570 - 30 * i);
				}
			}
		}
		if (IsDown) {
			for (int i = 0; i < currentFloor - 1; i++) {
				if (toWhichFloor[i]) {
					return 570 - 30 * i - getMyY();
				}
			}
		}
		return 0;
	}
}