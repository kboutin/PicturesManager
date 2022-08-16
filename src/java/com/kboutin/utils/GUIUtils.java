package com.kboutin.utils;

import javax.swing.border.TitledBorder;

import static javax.swing.BorderFactory.createEtchedBorder;
import static javax.swing.BorderFactory.createTitledBorder;
import static javax.swing.border.EtchedBorder.RAISED;

public class GUIUtils {

	public static TitledBorder createEtchedTitledBorder(String title) {

		return createTitledBorder(createEtchedBorder(RAISED), title);
	}
}
