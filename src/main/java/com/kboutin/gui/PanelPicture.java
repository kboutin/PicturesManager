package com.kboutin.gui;

import com.kboutin.core.Picture;
import com.kboutin.utils.GUIUtils;

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

	private TitledBorder border;
	private JLabel lblPicture = new JLabel();

	public PanelPicture() {

		super(new BorderLayout());

		border = GUIUtils.createEtchedTitledBorder("");
		setBorder(border);
		add(lblPicture, BorderLayout.CENTER);
	}

	public final void updatePicture(Picture p) {

		if (p == null) {
			border.setTitle("");
			lblPicture.setIcon(null);
		} else {
			border.setTitle(p.getFileName());
			lblPicture.setIcon(getIconFromPicture(p));
		}
	}

	private Icon getIconFromPicture(Picture selectedPicture) {

		if (selectedPicture == null) {
			return null;
		}

		Image resizedPicture = null;
		try {
			BufferedImage bufferedPicture = ImageIO.read(new File(selectedPicture.getFilePath()));
			switch(selectedPicture.getOrientation()) {
				case PORTRAIT:
					// TODO KBO : Center picture in PORTRAIT MODE.
					resizedPicture = bufferedPicture.getScaledInstance(-1, lblPicture.getHeight(), Image.SCALE_SMOOTH); // -1 is used to keep image ratio
					break;
				case LANDSCAPE:
					resizedPicture = bufferedPicture.getScaledInstance(lblPicture.getWidth(), -1, Image.SCALE_SMOOTH); // -1 is used to keep image ratio
					break;
				default:
					resizedPicture = bufferedPicture.getScaledInstance(lblPicture.getWidth(), -1, Image.SCALE_SMOOTH); // -1 is used to keep image ratio
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return new ImageIcon(resizedPicture);
	}
}
