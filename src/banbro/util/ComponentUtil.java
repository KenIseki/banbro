package banbro.util;

import java.awt.GridBagConstraints;

public class ComponentUtil {

	public static GridBagConstraints createConstraints(int x, int y, int w, int h) {
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = x;
		constraints.gridy = y;
		constraints.gridwidth = w;
		constraints.gridheight = h;
		return constraints;
	}

}
