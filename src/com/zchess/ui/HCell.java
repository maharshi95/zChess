package com.zchess.ui;

import java.awt.Color;

import acm.graphics.GCompound;
import acm.graphics.GRect;

public class HCell extends GCompound {

	public HCell(double side, boolean self) {
		
		Color c = GColors.HCELL;
		
		if(self) c = GColors.HCELL_SELF;
		int i;
		for(i=0; i<5; i++) {
			GRect r = new GRect(side-2*i,side-2*i);
			r.setColor(c);
			add(r,i,i);
		}
	}
	
	public HCell(double side) {
		
		Color c = GColors.HCELL_ALERT;
		int i;
		for(i=0; i<5; i++) {
			GRect r = new GRect(side-2*i,side-2*i);
			r.setColor(c);
			add(r,i,i);
		}
	}


}
