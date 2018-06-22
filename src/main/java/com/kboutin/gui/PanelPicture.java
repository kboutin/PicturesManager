package com.kboutin.gui;

import com.kboutin.core.Picture;
import com.kboutin.utils.FileUtils;
import com.kboutin.utils.GUIUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PanelPicture extends JPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger logger = LogManager.getLogger(PanelPicture.class);

	private TitledBorder border;
	private JLabel lblPicture = new JLabel();

	public PanelPicture() {

		super(new BorderLayout());

		border = GUIUtils.createEtchedTitledBorder("");
		setBorder(border);
		add(lblPicture, BorderLayout.CENTER);
	}

	public final void updatePicture(File f) {

		if (f == null || !FileUtils.isPicture(f)) {
			border.setTitle("");
			lblPicture.setIcon(null);
		} else {
			border.setTitle(f.getName());
			lblPicture.setHorizontalAlignment(JLabel.CENTER);
			lblPicture.setIcon(getIconFromFile(f));
		}
		repaint();
	}

	public final void updatePicture(Picture p) {

		if (p == null) {
			border.setTitle("");
			lblPicture.setIcon(null);
		} else {
			border.setTitle(p.getFileName());
			lblPicture.setHorizontalAlignment(JLabel.CENTER);
			lblPicture.setIcon(getIconFromPicture(p));
		}
		repaint();
	}

	private Icon getIconFromFile(File f) {

		if (f == null || !FileUtils.isPicture(f)) {
			return null;
		}

		return getIconFromPicture(new Picture(f));
	}

	private Icon getIconFromPicture(Picture selectedPicture) {

		if (selectedPicture == null) {
			return null;
		}

		Image resizedPicture = null;
		try {
			BufferedImage bufferedPicture = ImageIO.read(new File(selectedPicture.getFilePath()));
			logger.debug("Displaying " + selectedPicture.getFilePath() + " (" + selectedPicture.getOrientation() + ")");
			switch(selectedPicture.getOrientation()) {
				case PORTRAIT:
					resizedPicture = bufferedPicture.getScaledInstance(-1, lblPicture.getHeight(), Image.SCALE_SMOOTH); // -1 is used to keep image ratio
					break;
				case LANDSCAPE:
				default:
					resizedPicture = bufferedPicture.getScaledInstance(lblPicture.getWidth(), -1, Image.SCALE_SMOOTH); // -1 is used to keep image ratio
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return new ImageIcon(resizedPicture);
	}
}

/*int scaledHeight = (int) (resizedPicture.getHeight(null) * 0.75);
int scaledWidth = (int) (resizedPicture.getWidth(null) * 0.75);
BufferedImage outputImage = new BufferedImage(scaledWidth, scaledHeight, bufferedPicture.getType());
// scales the input image to the output image
Graphics2D g2d = outputImage.createGraphics();
g2d.drawImage(bufferedPicture, 0, 0, scaledWidth, scaledHeight, null);
g2d.dispose();

// extracts extension of output file
//String outputImagePath = selectedPicture.getFilePath() + "_small.jpg";

// writes to output file
//ImageIO.write(outputImage, "jpg", new File(outputImagePath));
return new ImageIcon(outputImage);*/
