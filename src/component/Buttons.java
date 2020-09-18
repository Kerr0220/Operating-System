package component;

import java.awt.Color;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Buttons extends JLabel {
	public MyButton[] buttons = new MyButton[23];//num_buttons' array
	public MyButton btnOpen;//the button for opening door
	public MyButton btnClose;//the button for closing door
	public MyButton btnAlarm;//the button for alarming
	public JLabel floorNum;//the digital display

	// Constructor
	public Buttons() {
		MyButton.num_tot = 0;
		// set label's attributes
		this.setLayout(null);//Set the component slay to a fixed layout
		this.setSize(80, 510);//Set the size of the internal button background panel of the elevator
		this.setOpaque(true);//set opaque
//		this.setIcon(new ImageIcon(this.getClass().getResource("/image/slamDunk.jpg")));
		this.setBackground(new Color(237, 206, 223));//set the color of background
		this.setVisible(true);//set visible
		// add label
		String str = "0 1";//Initial status elevator should be on the first floor, display 01

		floorNum = new JLabel(str, JLabel.CENTER);//floor digital display
		floorNum.setForeground(Color.RED);//set the color of font
		Font font = new Font("¡• È", Font.PLAIN, 30);
		floorNum.setFont(font);//set font
		floorNum.setBackground(new Color(58, 60, 79));//set digital display's background color
		floorNum.setOpaque(true);//set opaque
		floorNum.setBounds(5, 5, 70, 35);//set the size of digital display and it's location
		floorNum.setVisible(true);//set visible
		add(floorNum);//add the digital display to the elevator's inner side

		// add number buttons
		for (int i = 0; i < 20; i++) {
			MyButton btn = new MyButton("/image/" + (i + 1) + ".png", "/image/" + (i + 1) + "h.png", 5 + (i % 2) * 40,
					405 - (i / 2) * 40, 30, 30);
			add(btn.btn);
			buttons[i] = btn;
		}
		//add other buttons
		btnOpen = new MyButton("/image/open.png", "/image/openH.png", 5, 445, 30, 30);
		add(btnOpen.btn);
		buttons[20] = btnOpen;

		btnClose = new MyButton("/image/close.png", "/image/closeH.png", 45, 445, 30, 30);
		add(btnClose.btn);
		buttons[21] = btnClose;

		btnAlarm = new MyButton("/image/alarm.png", "/image/alarmH.png", 25, 480, 30, 30);
		add(btnAlarm.btn);
		buttons[22] = btnAlarm;
	}

	public void setFloor(int floor) {
		//set the content of digital display
		String str;
		if (floor == -1) {
			str = "ERR!";
		} else if (floor < 10) {
			str = "0 " + floor;
		} else if (floor < 20) {
			str = "1 " + (floor - 10);
		} else {
			str = "2 0";
		}
		this.floorNum.setText(str);
		return;
	}
}
