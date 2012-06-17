FireZenk's ProgressBar
======================

A highly customizable progress bar

USAGE:
----------------------

1- Add FireZenk's ProgressBar to your project has a library

2- Add the progress bar on your xml layout for example:

    <com.firezenk.progressbar.FZProgressBar 
          android:id="@+id/myProgressBar"
          android:layout_width="fill_parent"
          android:layout_height="1dp" />
          
3- In your code:

  1- Import the progress bar:
  
    import com.firezenk.progressbar.FZProgressBar;
    import com.firezenk.progressbar.FZProgressBar.Mode;
    
  2- Retrieve the element:
  
    FZProgressBar mBar = (FZProgressBar) findViewById(R.id.myProgressBar);
    
  3- Customize the progress bar:
  
    mBar.animation_config(int delay, int spacing);
    mBar.bar_config(int height, int width, int radius, int background_color, int[] progress_colors);
    
  4- Start/stop the progress animation:
  
    mBar.animation_start(Mode mode);
    mBar.animation_stop();

4- Enjoy!

LICENSE:
----------------------

    FireZenk's Progress Bar <A highly customizable progress bar>
    Copyright (C) 2012 Jorge Garrido Oval (aka: FireZenk)

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see http://www.gnu.org/licenses/