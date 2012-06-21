/**
 * 	FireZenk's Progress Bar <A highly customizable progress bar>
 *  Copyright (C) 2012 Jorge Garrido Oval (aka: FireZenk)
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.firezenk.progressbar;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class FZProgressBar extends LinearLayout {

	public enum Mode {ONESHOT, DETERMINATE, INDETERMINATE};
	
	private LinearLayout bar;
	private LinearLayout limits;
	
	private Handler animation = new Handler();
	private Runnable basic_one, basic_two, basic_three, basic_four;
	private Context context;
	private int LIMITS_WIDTH;
	    
	//DEFAULT CONFIG
	private int 	ANIMATION_DELAY = 2;
	private int 	ANIMATION_SPACING = 10;
	private int 	BAR_SIZE_W = 0; // 0 = FILL_PARENT
	private int 	BAR_SIZE_H = 100;
	private int 	BAR_CORNER = 0;
	private int 	BAR_BACKGROUND = Color.WHITE;
	private int[] 	COLORS = {Color.BLACK, Color.TRANSPARENT};
	
	public FZProgressBar(Context context) {
		super(context);
		this.context = context;
		init();
	}
	
	public FZProgressBar(Context context, AttributeSet attrs) {
    	super(context, attrs);
    	this.context = context;
    	init();
	}
	
	private void init() {
		limits = this;
		bar = new LinearLayout(context);
		this.addView(bar);
	}
	
	public void animation_config(int delay, int spacing) {
		ANIMATION_DELAY = delay;
		ANIMATION_SPACING = spacing;
	}
	
	@SuppressWarnings("deprecation")
	public void bar_config(
			int height, int width, int radius, int background_color, int[] progress_colors) {
		BAR_SIZE_W = width;
		BAR_SIZE_H = height;
		BAR_CORNER = radius;
		BAR_BACKGROUND = background_color;
		COLORS = progress_colors;
		
		LinearLayout.LayoutParams limits_params = null;
		
		switch (BAR_SIZE_W) {
			case 0:
				limits_params = 
						new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, BAR_SIZE_H);
				break;
			default:
				limits_params = 
						new LinearLayout.LayoutParams(BAR_SIZE_W, BAR_SIZE_H);
				break;
		}
		
		limits.setLayoutParams(limits_params);
		limits.setBackgroundColor(BAR_BACKGROUND);
		
		LinearLayout.LayoutParams bar_params = 
				new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.FILL_PARENT);
		bar.setLayoutParams(bar_params);
	}
	
	public synchronized void animation_start(final Mode mode) {
		LIMITS_WIDTH = new Integer(limits.getWidth());
		
		basic_one = new Runnable() {
			private int i = 0;
			
			@Override
			public void run() {
				LinearLayout.LayoutParams params = 
						new LinearLayout.LayoutParams(i+=ANIMATION_SPACING, BAR_SIZE_H);
				bar.setLayoutParams(params);
				
				if(i<LIMITS_WIDTH+1)
					animation.postDelayed(this, ANIMATION_DELAY);
				else {
					animation.removeCallbacks(this);
					animation.postDelayed(basic_two, ANIMATION_DELAY);
				}
			}
		};
		
		basic_two = new Runnable() {
			private int i = LIMITS_WIDTH;
			private int margin = 0;
			
			@Override
			public void run() {
				LinearLayout.LayoutParams params = 
						new LinearLayout.LayoutParams(i-=ANIMATION_SPACING, BAR_SIZE_H);
				params.leftMargin = margin+=ANIMATION_SPACING;
				bar.setLayoutParams(params);
				
				if(i>-1)
					animation.postDelayed(this, ANIMATION_DELAY);
				else {
					animation.removeCallbacks(this);
					
					GradientDrawable gd = new GradientDrawable(Orientation.LEFT_RIGHT, COLORS);
					gd.setCornerRadius(BAR_CORNER);
					bar.setBackgroundDrawable(gd);
					
					animation.postDelayed(basic_three, ANIMATION_DELAY*10);
				}
			}
		};
		
		basic_three = new Runnable() {
			private int i = 0;
			private int margin = LIMITS_WIDTH-1;
			
			@Override
			public void run() {
				LinearLayout.LayoutParams params = 
						new LinearLayout.LayoutParams(i+=ANIMATION_SPACING, BAR_SIZE_H);
				params.leftMargin = margin-=ANIMATION_SPACING;
				bar.setLayoutParams(params);
				
				if(i<LIMITS_WIDTH+1)
					animation.postDelayed(this, ANIMATION_DELAY);
				else {
					animation.removeCallbacks(this);
					animation.postDelayed(basic_four, ANIMATION_DELAY);
				}
			}
		};
		
		basic_four = new Runnable() {
			private int i = LIMITS_WIDTH;
			
			@Override
			public void run() {
				LinearLayout.LayoutParams params = 
						new LinearLayout.LayoutParams(i-=ANIMATION_SPACING, BAR_SIZE_H);
				bar.setLayoutParams(params);
				
				if(i>-1)
					animation.postDelayed(this, ANIMATION_DELAY);
				else {
					animation.removeCallbacks(this);
					switch (mode) {
						case ONESHOT:
							bar.setVisibility(View.INVISIBLE);
							break;
						case INDETERMINATE:
							animation_start(mode);
							break;
						default: break;
					}
				}
			}
		};
		
		bar.setVisibility(View.VISIBLE);
		GradientDrawable gd = new GradientDrawable(Orientation.RIGHT_LEFT, COLORS);
		gd.setCornerRadius(BAR_CORNER);
		bar.setBackgroundDrawable(gd);
		
		if(mode == Mode.DETERMINATE) {
			LinearLayout.LayoutParams params = 
					new LinearLayout.LayoutParams(bar.getWidth()+ANIMATION_SPACING, BAR_SIZE_H);
			bar.setLayoutParams(params);
	 	} else {
			animation.post(basic_one);
		}
	}
	
	public synchronized void animation_stop() {
		animation.removeCallbacks(basic_one);
		animation.removeCallbacks(basic_two);
		animation.removeCallbacks(basic_three);
		animation.removeCallbacks(basic_four);
		LinearLayout.LayoutParams params = 
				new LinearLayout.LayoutParams(0, BAR_SIZE_H);
		bar.setLayoutParams(params);
	}
	
	public synchronized void animation_stop(final Mode mode) {
		LinearLayout.LayoutParams params = 
				new LinearLayout.LayoutParams(0, BAR_SIZE_H);
		bar.setLayoutParams(params);
	}

}
