package component;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class MyButton {
	static int num_tot = 0;//MyButton's total number
	public int num;//this button's code
	public JButton btn = new JButton("");

	/**
	 * Create MyButton.
	 */
	public MyButton(String path_nor, String path_high, int x, int y, int width, int height) {
		num = num_tot;
		num_tot++;
		btn.setIcon(new ImageIcon(this.getClass().getResource(path_nor)));
		//set the icon of this button in normal situation
		
		btn.setBounds(x, y, width, height);
		//set the height, width and location
		
		btn.setDisabledIcon(new ImageIcon(this.getClass().getResource(path_high)));
		//set the icon of this button in abnormal situation
		
		btn.setPressedIcon(new ImageIcon(this.getClass().getResource(path_high)));
		//set the icon of this button when it's pressed
	}
}
