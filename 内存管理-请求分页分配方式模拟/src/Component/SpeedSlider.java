package Component;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;

public class SpeedSlider extends JPanel {
	public JSlider speed_ = new JSlider();
	public SpeedSlider() {
		this.setSize(330, 30);
		this.setLayout(null);
		this.setBackground(new Color(157, 200, 200));
		add(speed_);
		speed_.setOrientation(SwingConstants.HORIZONTAL);
		speed_.setPaintTrack(true);
		speed_.setBounds(30, 0, 200, 30);
		speed_.setBackground(new Color(157, 200, 200));
		speed_.setMaximum(1000);
		speed_.setMinimum(10);
		speed_.setValue(500);

		// Ìí¼Ó¡°¿ì¡±¡¢¡°Âý¡±
		JLabel f = new JLabel("¿ì");
		JLabel l = new JLabel("Âý");
		add(f);
		f.setBounds(240, 0, 30, 30);
		f.setForeground(Color.WHITE);
		add(l);
		l.setBounds(0, 0, 30, 30);
		l.setForeground(Color.WHITE);
	}

}
