//Name: Deval Patel
//Date: June 13, 2017
//Purpose: To make the best game of Sokoban (but not really). I did what I could (please don't fail :) )

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.applet.Applet;
import java.applet.*;
import java.lang.Object;
import java.util.Vector;


public class FinalGame extends Applet implements ActionListener, KeyListener
{
    Panel p_card;  //to hold all of the screens
    Panel card1, card2, card3, card4, card5, card6; //the six screens
    CardLayout cdLayout = new CardLayout ();
    //sound
    AudioClip soundFile;
    //declaring global JLabels
    JTextArea ins;
    JLabel moves;
    JLabel curlevel;
    JLabel showtime;
    //all starting positions for the character
    int startx[] = {15, 13, 5, 11, 13, 9};
    int starty[] = {14, 4, 11, 3, 16, 10};
    //cur location
    int curx = 15;
    int cury = 14;
    //undo cur location
    int undox[] = new int [1000];
    int undoy[] = new int [1000];
    //grid
    int level = 1;
    int score = 0;
    int row = 20;
    int col = 20;
    JLabel a[] = new JLabel [row * col];
    //elapsed time
    static int time = 0;

    Thread timecount = new Thread ()
    {
	public void run ()
	{
	    while (true)
	    {
		try
		{
		    Thread.sleep (1000);
		}
		catch (Exception e)
		{
		}
		time++;
		//Calculates minutes and seconds and displays it
		if (time % 60 < 10)
		    showtime.setText ("Elapsed Time: " + time / 60 + ":0" + time % 60);
		else
		    showtime.setText ("Elapsed Time: " + time / 60 + ":" + time % 60);

	    }
	}
    }


    ;


    //Undo Array
    int undo[] [] [] = new int [1000] [row] [col];
    int where = -1;
    //MAIN GRID
    //array which changes
    int b[] [] = {{2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 1, 3, 3, 3, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 1, 3, 3, 3, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 1, 3, 3, 3, 3, 5, 3, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 1, 1, 1, 3, 5, 3, 3, 3, 3, 3, 1, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 1, 3, 3, 3, 3, 3, 5, 1, 3, 3, 1, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 1, 3, 3, 3, 3, 3, 5, 3, 3, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 1, 3, 5, 1, 3, 1, 1, 3, 3, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 1, 3, 3, 1, 3, 1, 1, 3, 3, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 1, 3, 3, 3, 3, 3, 3, 3, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2},
	    {2, 1, 1, 1, 1, 5, 3, 3, 3, 3, 3, 3, 3, 1, 3, 3, 4, 4, 1, 2},
	    {2, 2, 2, 2, 1, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 1, 2},
	    {2, 2, 2, 2, 1, 3, 1, 1, 1, 1, 3, 1, 1, 1, 3, 3, 4, 4, 1, 2},
	    {2, 2, 2, 2, 1, 3, 3, 3, 3, 3, 3, 1, 1, 1, 1, 1, 1, 1, 1, 2},
	    {2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2}};

    //blank spaces array, without boulders and character
    int ogb[] [] = {{2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 1, 3, 3, 3, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 1, 3, 3, 3, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 1, 3, 3, 3, 3, 3, 3, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 1, 1, 1, 3, 3, 3, 3, 3, 3, 3, 1, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 1, 3, 3, 3, 3, 3, 3, 1, 3, 3, 1, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 1, 3, 3, 3, 3, 3, 3, 3, 3, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 1, 3, 3, 1, 3, 1, 1, 3, 3, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 1, 3, 3, 1, 3, 1, 1, 3, 3, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 1, 3, 3, 3, 3, 3, 3, 3, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2},
	    {2, 1, 1, 1, 1, 3, 3, 3, 3, 3, 3, 3, 3, 1, 3, 3, 4, 4, 1, 2},
	    {2, 2, 2, 2, 1, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 1, 2},
	    {2, 2, 2, 2, 1, 3, 1, 1, 1, 1, 3, 1, 1, 1, 3, 3, 4, 4, 1, 2},
	    {2, 2, 2, 2, 1, 3, 3, 3, 3, 3, 3, 1, 1, 1, 1, 1, 1, 1, 1, 2},
	    {2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2}};

    //Level one
    //array which changes
    int b1[] [] = {{2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 1, 3, 3, 3, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 1, 3, 3, 3, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 1, 3, 3, 3, 3, 5, 3, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 1, 1, 1, 3, 5, 3, 3, 3, 3, 3, 1, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 1, 3, 3, 3, 3, 3, 5, 1, 3, 3, 1, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 1, 3, 3, 3, 3, 3, 5, 3, 3, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 1, 3, 5, 1, 3, 1, 1, 3, 3, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 1, 3, 3, 1, 3, 1, 1, 3, 3, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 1, 3, 3, 3, 3, 3, 3, 3, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2},
	    {2, 1, 1, 1, 1, 5, 3, 3, 3, 3, 3, 3, 3, 1, 3, 3, 4, 4, 1, 2},
	    {2, 2, 2, 2, 1, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 1, 2},
	    {2, 2, 2, 2, 1, 3, 1, 1, 1, 1, 3, 1, 1, 1, 3, 3, 4, 4, 1, 2},
	    {2, 2, 2, 2, 1, 3, 3, 3, 3, 3, 3, 1, 1, 1, 1, 1, 1, 1, 1, 2},
	    {2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2}};
    //blank spaces array, without boulders and character
    int ogb1[] [] = {{2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 1, 3, 3, 3, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 1, 3, 3, 3, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 1, 3, 3, 3, 3, 3, 3, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 1, 1, 1, 3, 3, 3, 3, 3, 3, 3, 1, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 1, 3, 3, 3, 3, 3, 3, 1, 3, 3, 1, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 1, 3, 3, 3, 3, 3, 3, 3, 3, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 1, 3, 3, 1, 3, 1, 1, 3, 3, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 1, 3, 3, 1, 3, 1, 1, 3, 3, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 1, 3, 3, 3, 3, 3, 3, 3, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2},
	    {2, 1, 1, 1, 1, 3, 3, 3, 3, 3, 3, 3, 3, 1, 3, 3, 4, 4, 1, 2},
	    {2, 2, 2, 2, 1, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 1, 2},
	    {2, 2, 2, 2, 1, 3, 1, 1, 1, 1, 3, 1, 1, 1, 3, 3, 4, 4, 1, 2},
	    {2, 2, 2, 2, 1, 3, 3, 3, 3, 3, 3, 1, 1, 1, 1, 1, 1, 1, 1, 2},
	    {2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2}};

    //Level two
    //second map
    int b2[] [] = {{2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 1, 4, 4, 3, 3, 1, 3, 3, 3, 1, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 1, 4, 4, 3, 3, 3, 3, 3, 3, 1, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 1, 4, 4, 3, 3, 1, 3, 3, 1, 1, 1, 1, 2, 2, 2},
	    {2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 3, 3, 1, 3, 3, 1, 1, 2, 2},
	    {2, 2, 2, 2, 1, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 1, 2, 2},
	    {2, 2, 2, 2, 1, 3, 3, 1, 3, 3, 1, 1, 3, 3, 1, 3, 3, 1, 2, 2},
	    {2, 2, 1, 1, 1, 1, 3, 1, 1, 3, 3, 1, 1, 1, 1, 3, 1, 1, 2, 2},
	    {2, 2, 1, 3, 3, 5, 3, 3, 1, 1, 1, 1, 1, 3, 1, 3, 3, 1, 2, 2},
	    {2, 2, 1, 3, 1, 3, 5, 3, 3, 5, 3, 3, 1, 3, 5, 3, 3, 1, 2, 2},
	    {2, 2, 1, 3, 3, 5, 3, 3, 5, 3, 3, 3, 1, 3, 3, 3, 1, 1, 2, 2},
	    {2, 2, 1, 1, 1, 1, 3, 1, 1, 3, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2},
	    {2, 2, 2, 2, 2, 1, 3, 3, 3, 3, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2}};
    //blank spaces array, without boulders and character
    int ogb2[] [] = {{2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 1, 4, 4, 3, 3, 1, 3, 3, 3, 1, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 1, 4, 4, 3, 3, 3, 3, 3, 3, 1, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 1, 4, 4, 3, 3, 1, 3, 3, 1, 1, 1, 1, 2, 2, 2},
	    {2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 3, 3, 1, 3, 3, 1, 1, 2, 2},
	    {2, 2, 2, 2, 1, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 1, 2, 2},
	    {2, 2, 2, 2, 1, 3, 3, 1, 3, 3, 1, 1, 3, 3, 1, 3, 3, 1, 2, 2},
	    {2, 2, 1, 1, 1, 1, 3, 1, 1, 3, 3, 1, 1, 1, 1, 3, 1, 1, 2, 2},
	    {2, 2, 1, 3, 3, 3, 3, 3, 1, 1, 1, 1, 1, 3, 1, 3, 3, 1, 2, 2},
	    {2, 2, 1, 3, 1, 3, 3, 3, 3, 3, 3, 3, 1, 3, 3, 3, 3, 1, 2, 2},
	    {2, 2, 1, 3, 3, 3, 3, 3, 3, 3, 3, 3, 1, 3, 3, 3, 1, 1, 2, 2},
	    {2, 2, 1, 1, 1, 1, 3, 1, 1, 3, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2},
	    {2, 2, 2, 2, 2, 1, 3, 3, 3, 3, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2}};

    //Level three
    //third map
    int b3[] [] = {{2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 1, 1, 1, 2, 2, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 1, 1, 4, 1, 1, 1, 1, 3, 3, 1, 1, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 1, 4, 4, 4, 3, 3, 1, 3, 3, 3, 1, 1, 1, 2, 2, 2, 2, 2},
	    {2, 2, 1, 1, 4, 4, 3, 3, 3, 3, 3, 5, 3, 3, 1, 2, 2, 2, 2, 2},
	    {2, 2, 1, 4, 4, 4, 3, 3, 1, 3, 5, 3, 5, 3, 1, 2, 2, 2, 2, 2},
	    {2, 2, 1, 1, 4, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 2, 2},
	    {2, 2, 2, 1, 1, 1, 2, 2, 2, 2, 1, 3, 5, 3, 5, 3, 3, 1, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 3, 5, 3, 5, 1, 3, 1, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 3, 3, 3, 5, 3, 3, 3, 1, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 3, 3, 5, 3, 5, 1, 3, 1, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 3, 1, 3, 1, 3, 1, 3, 1, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 3, 3, 3, 3, 3, 3, 3, 1, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2}};
    //blank spaces array, without boulders and character
    int ogb3[] [] = {{2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 1, 1, 1, 2, 2, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 1, 1, 4, 1, 1, 1, 1, 3, 3, 1, 1, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 1, 4, 4, 4, 3, 3, 1, 3, 3, 3, 1, 1, 1, 2, 2, 2, 2, 2},
	    {2, 2, 1, 1, 4, 4, 3, 3, 3, 3, 3, 3, 3, 3, 1, 2, 2, 2, 2, 2},
	    {2, 2, 1, 4, 4, 4, 3, 3, 1, 3, 3, 3, 3, 3, 1, 2, 2, 2, 2, 2},
	    {2, 2, 1, 1, 4, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 2, 2},
	    {2, 2, 2, 1, 1, 1, 2, 2, 2, 2, 1, 3, 3, 3, 3, 3, 3, 1, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 3, 3, 3, 3, 1, 3, 1, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 3, 3, 3, 3, 3, 3, 3, 1, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 3, 3, 3, 3, 3, 1, 3, 1, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 3, 1, 3, 1, 3, 1, 3, 1, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 3, 3, 3, 3, 3, 3, 3, 1, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2}};

    //Level four
    //fourth map
    int b4[] [] = {{2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 1, 3, 3, 3, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 1, 3, 5, 5, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 1, 3, 3, 5, 3, 5, 3, 1, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 1, 3, 5, 1, 3, 3, 3, 1, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 1, 1, 1, 1, 3, 3, 1, 1, 5, 3, 1, 1, 1, 1, 1, 1, 2, 2},
	    {2, 2, 1, 3, 3, 3, 3, 3, 1, 1, 3, 3, 3, 3, 3, 4, 4, 1, 2, 2},
	    {2, 2, 1, 1, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 1, 2, 2},
	    {2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 4, 4, 1, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2}};
    //blank spaces array, without boulders and character
    int ogb4[] [] = {{2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 1, 3, 3, 3, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 1, 3, 3, 3, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 1, 3, 3, 3, 3, 3, 3, 1, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 1, 3, 3, 1, 3, 3, 3, 1, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 1, 1, 1, 1, 3, 3, 1, 1, 3, 3, 1, 1, 1, 1, 1, 1, 2, 2},
	    {2, 2, 1, 3, 3, 3, 3, 3, 1, 1, 3, 3, 3, 3, 3, 4, 4, 1, 2, 2},
	    {2, 2, 1, 1, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 1, 2, 2},
	    {2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 4, 4, 1, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2}};

    //Level five
    //fifth map
    int b5[] [] = {{2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 1, 3, 3, 1, 3, 3, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2},
	    {2, 2, 2, 1, 1, 3, 3, 1, 3, 3, 1, 4, 4, 4, 1, 1, 1, 2, 2, 2},
	    {2, 2, 2, 1, 3, 3, 5, 1, 3, 3, 1, 4, 4, 4, 3, 3, 1, 2, 2, 2},
	    {2, 2, 2, 1, 3, 5, 3, 1, 5, 5, 3, 4, 4, 4, 3, 3, 1, 2, 2, 2},
	    {2, 2, 2, 1, 3, 3, 5, 1, 3, 3, 1, 4, 4, 4, 3, 4, 1, 2, 2, 2},
	    {2, 2, 2, 1, 3, 3, 3, 1, 3, 5, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2},
	    {2, 2, 2, 1, 1, 5, 3, 3, 3, 3, 3, 3, 3, 5, 3, 5, 3, 1, 2, 2},
	    {2, 2, 2, 1, 1, 3, 3, 1, 3, 3, 5, 5, 3, 1, 3, 3, 3, 1, 2, 2},
	    {2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 3, 3, 1, 1, 5, 5, 3, 1, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 3, 3, 3, 3, 3, 3, 1, 1, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2}};
    //blank spaces array, without boulders and character
    int ogb5[] [] = {{2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 1, 3, 3, 1, 3, 3, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2},
	    {2, 2, 2, 1, 1, 3, 3, 1, 3, 3, 1, 4, 4, 4, 1, 1, 1, 2, 2, 2},
	    {2, 2, 2, 1, 3, 3, 3, 1, 3, 3, 1, 4, 4, 4, 3, 3, 1, 2, 2, 2},
	    {2, 2, 2, 1, 3, 3, 3, 1, 3, 3, 3, 4, 4, 4, 3, 3, 1, 2, 2, 2},
	    {2, 2, 2, 1, 3, 3, 3, 1, 3, 3, 1, 4, 4, 4, 3, 4, 1, 2, 2, 2},
	    {2, 2, 2, 1, 3, 3, 3, 1, 3, 3, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2},
	    {2, 2, 2, 1, 1, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 1, 2, 2},
	    {2, 2, 2, 1, 1, 3, 3, 1, 3, 3, 3, 3, 3, 1, 3, 3, 3, 1, 2, 2},
	    {2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 3, 3, 1, 1, 3, 3, 3, 1, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 3, 3, 3, 3, 3, 3, 1, 1, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2}};

    //Level six
    //sixth map
    int b6[] [] = {{2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
	    {2, 1, 1, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 1, 1},
	    {2, 2, 1, 1, 3, 3, 5, 3, 5, 4, 5, 4, 5, 3, 5, 3, 3, 1, 1, 2},
	    {2, 2, 2, 1, 1, 3, 3, 5, 4, 5, 4, 5, 4, 5, 3, 3, 1, 1, 2, 2},
	    {2, 2, 2, 2, 1, 1, 3, 4, 5, 4, 5, 4, 5, 4, 3, 1, 1, 2, 2, 2},
	    {2, 2, 2, 2, 2, 1, 1, 3, 4, 5, 4, 5, 4, 3, 1, 1, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 2, 1, 1, 3, 4, 5, 4, 3, 1, 1, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 1, 1, 3, 4, 3, 1, 1, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 3, 1, 1, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 1, 1, 3, 4, 3, 1, 1, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 2, 1, 1, 3, 4, 5, 4, 3, 1, 1, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 1, 1, 3, 4, 5, 4, 5, 4, 3, 1, 1, 2, 2, 2, 2},
	    {2, 2, 2, 2, 1, 1, 3, 4, 5, 4, 5, 4, 5, 4, 3, 1, 1, 2, 2, 2},
	    {2, 2, 2, 1, 1, 3, 3, 5, 4, 5, 4, 5, 4, 5, 3, 3, 1, 1, 2, 2},
	    {2, 2, 1, 1, 3, 3, 5, 3, 5, 4, 5, 4, 5, 3, 5, 3, 3, 1, 1, 2},
	    {2, 1, 1, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 1, 1},
	    {2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2}};
    //blank spaces array, without boulders and character
    int ogb6[] [] = {{2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
	    {2, 1, 1, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 1, 1},
	    {2, 2, 1, 1, 3, 3, 3, 3, 3, 4, 3, 4, 3, 3, 3, 3, 3, 1, 1, 2},
	    {2, 2, 2, 1, 1, 3, 3, 3, 4, 3, 4, 3, 4, 3, 3, 3, 1, 1, 2, 2},
	    {2, 2, 2, 2, 1, 1, 3, 4, 3, 4, 3, 4, 3, 4, 3, 1, 1, 2, 2, 2},
	    {2, 2, 2, 2, 2, 1, 1, 3, 4, 3, 4, 3, 4, 3, 1, 1, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 2, 1, 1, 3, 4, 3, 4, 3, 1, 1, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 1, 1, 3, 4, 3, 1, 1, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 3, 1, 1, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 1, 1, 3, 4, 3, 1, 1, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 2, 1, 1, 3, 4, 3, 4, 3, 1, 1, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 1, 1, 3, 4, 3, 4, 3, 4, 3, 1, 1, 2, 2, 2, 2},
	    {2, 2, 2, 2, 1, 1, 3, 4, 3, 4, 3, 4, 3, 4, 3, 1, 1, 2, 2, 2},
	    {2, 2, 2, 1, 1, 3, 3, 3, 4, 3, 4, 3, 4, 3, 3, 3, 1, 1, 2, 2},
	    {2, 2, 1, 1, 3, 3, 3, 3, 3, 4, 3, 4, 3, 3, 3, 3, 3, 1, 1, 2},
	    {2, 1, 1, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 1, 1},
	    {2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
	    {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2}};

    public void init ()
    {
	addKeyListener (this);
	soundFile = getAudioClip (getDocumentBase (), "despacito.wav");
	//this attaches the sound file "despacito"
	soundFile.loop ();
	//put the sound on repeat
	p_card = new Panel ();
	p_card.setLayout (cdLayout);
	storemove ();
	screen1 ();
	screen2 ();
	screen3 ();
	screen4 ();
	screen5 ();
	screen6 ();
	resize (750, 565);
	setLayout (new BorderLayout ());
	add ("Center", p_card);
    }


    public void screen1 ()
    { //screen 1 is set up.
	card1 = new Panel ();
	card1.setBackground (new Color (80, 130, 160));
	//main menu pic
	JButton pic = new JButton (createImageIcon ("sokoban title screen.jpg"));
	//title button
	pic.setActionCommand ("s2");
	pic.addActionListener (this);
	pic.setBorderPainted (false);
	pic.setPreferredSize (new Dimension (750, 565));
	card1.add (pic);
	p_card.add ("1", card1);
    }


    public void screen2 ()
    { //screen 2 is set up.
	card2 = new Panel ();
	card2.setBackground (new Color (80, 130, 160));
	//title of the screen
	JLabel title = new JLabel (createImageIcon ("ins title.png"));
	//Next button
	JButton next = new JButton ("Next");
	next.setForeground (new Color (46, 76, 119));
	next.setBackground (new Color (179, 205, 216));
	next.setPreferredSize (new Dimension (100, 50));
	next.setOpaque (true);
	next.setBorder (BorderFactory.createLineBorder (Color.black));
	next.setActionCommand ("s3");
	next.addActionListener (this);
	//Instructions
	JLabel ins = new JLabel (createImageIcon ("instructions.fw.png"));
	card2.add ("North", title);
	card2.add ("Center", ins);
	card2.add ("South", next);

	p_card.add ("2", card2);
    }


    public void screen3 ()
    {
	timecount.start ();
	//screen 3 is set up.
	card3 = new Panel (new BorderLayout ());
	card3.setBackground (new Color (80, 130, 160));
	//title
	JLabel title = new JLabel ("Sokoban", SwingConstants.CENTER);
	title.setFont (new Font ("Serif", Font.BOLD, 36));
	title.setPreferredSize (new Dimension (1000, 50));
	title.setForeground (Color.white);
	//Moves 
	moves = new JLabel ("Moves: " + score);
	moves.setFont (new Font ("Garamond", Font.BOLD, 20));
	moves.setPreferredSize (new Dimension (1000, 50));
	moves.setForeground (Color.black);
	//Level Label
	curlevel = new JLabel ("Level: " + level);
	curlevel.setFont (new Font ("Garamond", Font.BOLD, 20));
	curlevel.setPreferredSize (new Dimension (1000, 50));
	curlevel.setForeground (Color.black);
	//Instructions
	ins = new JTextArea ("Use arrow keys ");
	ins.append ("\n");
	ins.append ("or Use W, A, S, D");
	ins.setFont (new Font ("Garamond", Font.BOLD, 20));
	ins.setPreferredSize (new Dimension (1000, 50));
	ins.setForeground (Color.black);
	ins.setBackground (new Color (80, 130, 160));
	// Elapsed Time
	showtime = new JLabel ("Elapsed Time: " + time / 60 + ": " + time % 60);
	showtime.setFont (new Font ("Garamond", Font.BOLD, 20));
	//next button
	JButton next = new JButton ("Next");
	next.setForeground (new Color (46, 76, 119));
	next.setBackground (new Color (179, 205, 216));
	next.setPreferredSize (new Dimension (100, 50));
	next.setBorder (BorderFactory.createLineBorder (Color.black));
	next.setActionCommand ("s4");
	next.addActionListener (this);
	//reset button
	JButton reset = new JButton ("Reset");
	reset.setForeground (new Color (46, 76, 119));
	reset.setBackground (new Color (179, 205, 216));
	reset.setPreferredSize (new Dimension (100, 50));
	reset.setBorder (BorderFactory.createLineBorder (Color.black));
	reset.setActionCommand ("reset");
	reset.addActionListener (this);
	//undo button
	JButton undo = new JButton ("Undo");
	undo.setForeground (new Color (46, 76, 119));
	undo.setBackground (new Color (179, 205, 216));
	undo.setPreferredSize (new Dimension (100, 50));
	undo.setBorder (BorderFactory.createLineBorder (Color.black));
	undo.setActionCommand ("undo");
	undo.addActionListener (this);
	//Level select button
	JButton lvlselect = new JButton ("Levels");
	lvlselect.setForeground (new Color (46, 76, 119));
	lvlselect.setBackground (new Color (179, 205, 216));
	lvlselect.setPreferredSize (new Dimension (100, 50));
	lvlselect.setBorder (BorderFactory.createLineBorder (Color.black));
	lvlselect.setActionCommand ("lvls");
	lvlselect.addActionListener (this);
	// up button
	JButton up = new JButton ("Up");
	up.setForeground (new Color (46, 76, 119));
	up.setBackground (new Color (179, 205, 216));
	up.setActionCommand ("up");
	up.addActionListener (this);
	// down button
	JButton down = new JButton ("Down");
	down.setForeground (new Color (46, 76, 119));
	down.setBackground (new Color (179, 205, 216));
	down.setActionCommand ("down");
	down.addActionListener (this);
	// right button
	JButton right = new JButton ("Right");
	right.setForeground (new Color (46, 76, 119));
	right.setBackground (new Color (179, 205, 216));
	right.setActionCommand ("right");
	right.addActionListener (this);
	// left button
	JButton left = new JButton ("Left");
	left.setForeground (new Color (46, 76, 119));
	left.setBackground (new Color (179, 205, 216));
	left.setActionCommand ("left");
	left.addActionListener (this);
	// empty buttons
	JButton empty1 = new JButton ("");
	empty1.setBackground (new Color (80, 130, 160));
	empty1.setBorderPainted (false);
	empty1.setEnabled (false);
	JButton empty2 = new JButton ("");
	empty2.setBackground (new Color (80, 130, 160));
	empty2.setBorderPainted (false);
	empty2.setEnabled (false);
	JButton empty3 = new JButton ("");
	empty3.setBackground (new Color (80, 130, 160));
	empty3.setBorderPainted (false);
	empty3.setEnabled (false);
	JButton empty4 = new JButton ("");
	empty4.setBackground (new Color (80, 130, 160));
	empty4.setBorderPainted (false);
	empty4.setEnabled (false);
	JButton empty5 = new JButton ("");
	empty5.setBackground (new Color (80, 130, 160));
	empty5.setBorderPainted (false);
	empty5.setEnabled (false);
	//Set up grid
	Panel p = new Panel (new GridLayout (row, col));
	int move = 0;
	//makes the grid for the first time
	for (int i = 0 ; i < row ; i++)
	{
	    for (int j = 0 ; j < col ; j++)
	    {
		a [move] = new JLabel (createImageIcon (b [i] [j] + ".jpg"));
		a [move].setPreferredSize (new Dimension (24, 24));
		p.add (a [move]);
		move++;
	    }
	}
	a [curx * col + cury].setIcon (createImageIcon ("6.jpg"));
	card3.add ("North", title);
	//Panel for level, moves and instructions
	Panel p6 = new Panel (new GridLayout (4, 1));
	p6.add (curlevel);
	p6.add (moves);
	p6.add (ins);
	p6.add (showtime);
	//Panel for formatting
	Panel p3 = new Panel (new GridLayout (3, 1));
	p3.add (p6);
	card3.add ("West", p);
	//Panel for important buttons
	Panel p1 = new Panel (new GridLayout (2, 2));
	p1.add (next);
	p1.add (reset);
	p1.add (undo);
	p1.add (lvlselect);
	Panel p5 = new Panel ();
	p5.add (p1);
	//panel for direction arrows
	Panel p4 = new Panel ();
	Panel p2 = new Panel (new GridLayout (3, 3));
	p2.add (empty1);
	p2.add (up);
	p2.add (empty2);
	p2.add (left);
	p2.add (empty3);
	p2.add (right);
	p2.add (empty4);
	p2.add (down);
	p2.add (empty5);
	p4.add (p2);
	p3.add (p4);
	p3.add (p5);
	card3.add ("Center", p3);
	p_card.add ("3", card3);
	setFocusable (true);
    }


    public void screen4 ()
    { //screen 4 is set up.
	card4 = new Panel ();
	card4.setBackground (Color.black);
	JLabel title = new JLabel (createImageIcon ("wintitle.jpg"));
	title.setPreferredSize (new Dimension (750, 265));
	JButton next = new JButton (createImageIcon ("lvlselect.jpg"));
	next.setActionCommand ("s5");
	next.addActionListener (this);
	next.setPreferredSize (new Dimension (750, 150));
	next.setBorder (BorderFactory.createLineBorder (Color.black));
	JButton quit = new JButton (createImageIcon ("quit.jpg"));
	quit.setActionCommand ("quit");
	quit.addActionListener (this);
	quit.setPreferredSize (new Dimension (750, 150));
	quit.setBorder (BorderFactory.createLineBorder (Color.black));
	card4.add (title);
	card4.add (next);
	card4.add (quit);
	p_card.add ("4", card4);
    }


    public void screen5 ()
    { //screen 5 is set up.
	card5 = new Panel ();
	card5.setBackground (Color.black);
	//title
	JLabel title = new JLabel (createImageIcon ("losetitle.jpg"));
	title.setPreferredSize (new Dimension (750, 265));
	//Next button, goes to intro
	JButton next = new JButton (createImageIcon ("intro.jpg"));
	next.setActionCommand ("s1");
	next.addActionListener (this);
	next.setPreferredSize (new Dimension (750, 100));
	next.setBorder (BorderFactory.createLineBorder (Color.black));
	//Level Select
	JButton lvl = new JButton (createImageIcon ("lvlselect2.jpg"));
	lvl.setActionCommand ("lvlend");
	lvl.addActionListener (this);
	lvl.setPreferredSize (new Dimension (750, 100));
	lvl.setBorder (BorderFactory.createLineBorder (Color.black));
	//Quit button
	JButton end = new JButton (createImageIcon ("quit.jpg"));
	end.setActionCommand ("s6");
	end.addActionListener (this);
	end.setPreferredSize (new Dimension (750, 100));
	end.setBorder (BorderFactory.createLineBorder (Color.black));
	card5.add (title);
	card5.add (next);
	card5.add (lvl);
	card5.add (end);
	p_card.add ("5", card5);
    }


    public void screen6 ()
    { //screen 6 is set up. Level Select Screen
	card6 = new Panel (new BorderLayout ());
	card6.setBackground (new Color (80, 130, 160));
	//title of the screen
	JLabel title = new JLabel ("Level Select", SwingConstants.CENTER);
	title.setFont (new Font ("Serif", Font.BOLD, 46));
	title.setForeground (Color.white);
	//blank Labels
	JLabel blank = new JLabel ("");
	JLabel blank2 = new JLabel ("");
	JLabel blank3 = new JLabel ("");
	//Level 1 button
	JButton lvl1 = new JButton ("Level 1");
	lvl1.setFont (new Font ("Garamond", Font.BOLD, 36));
	lvl1.setForeground (new Color (46, 76, 119));
	lvl1.setBackground (new Color (179, 205, 216));
	lvl1.setPreferredSize (new Dimension (150, 100));
	lvl1.setBorder (BorderFactory.createLineBorder (Color.black));
	lvl1.setActionCommand ("level1");
	lvl1.addActionListener (this);
	//Level 2 button
	JButton lvl2 = new JButton ("Level 2");
	lvl2.setFont (new Font ("Garamond", Font.BOLD, 36));
	lvl2.setForeground (new Color (46, 76, 119));
	lvl2.setBackground (new Color (179, 205, 216));
	lvl2.setPreferredSize (new Dimension (150, 100));
	lvl2.setBorder (BorderFactory.createLineBorder (Color.black));
	lvl2.setActionCommand ("level2");
	lvl2.addActionListener (this);
	//Level 3 button
	JButton lvl3 = new JButton ("Level 3");
	lvl3.setFont (new Font ("Garamond", Font.BOLD, 36));
	lvl3.setForeground (new Color (46, 76, 119));
	lvl3.setBackground (new Color (179, 205, 216));
	lvl3.setPreferredSize (new Dimension (150, 100));
	lvl3.setBorder (BorderFactory.createLineBorder (Color.black));
	lvl3.setActionCommand ("level3");
	lvl3.addActionListener (this);
	//Level 4 button
	JButton lvl4 = new JButton ("Level 4");
	lvl4.setFont (new Font ("Garamond", Font.BOLD, 36));
	lvl4.setForeground (new Color (46, 76, 119));
	lvl4.setBackground (new Color (179, 205, 216));
	lvl4.setPreferredSize (new Dimension (150, 100));
	lvl4.setBorder (BorderFactory.createLineBorder (Color.black));
	lvl4.setActionCommand ("level4");
	lvl4.addActionListener (this);
	//Level 5 button
	JButton lvl5 = new JButton ("Level 5");
	lvl5.setFont (new Font ("Garamond", Font.BOLD, 36));
	lvl5.setForeground (new Color (46, 76, 119));
	lvl5.setBackground (new Color (179, 205, 216));
	lvl5.setPreferredSize (new Dimension (150, 100));
	lvl5.setBorder (BorderFactory.createLineBorder (Color.black));
	lvl5.setActionCommand ("level5");
	lvl5.addActionListener (this);
	//Level 6 button
	JButton lvl6 = new JButton ("Level 6");
	lvl6.setFont (new Font ("Garamond", Font.BOLD, 36));
	lvl6.setForeground (new Color (46, 76, 119));
	lvl6.setBackground (new Color (179, 205, 216));
	lvl6.setPreferredSize (new Dimension (200, 100));
	lvl6.setBorder (BorderFactory.createLineBorder (Color.black));
	lvl6.setActionCommand ("level6");
	lvl6.addActionListener (this);
	//Panel for all lvls
	Panel p1 = new Panel (new GridLayout (2, 3));
	p1.add (lvl1);
	p1.add (lvl2);
	p1.add (lvl3);
	p1.add (lvl4);
	p1.add (lvl5);
	p1.add (lvl6);
	//Panel to format levels 
	Panel p4 = new Panel ();
	p4.add (p1);
	Panel p5 = new Panel ();
	p5.add (title);
	card6.add ("North", p5);
	card6.add ("Center", p4);
	card6.add ("South", blank);
	card6.add ("East", blank2);
	card6.add ("West", blank3);
	p_card.add ("6", card6);
    }


    protected static ImageIcon createImageIcon (String path)
    { //Makes images possible
	java.net.URL imgURL = FinalGame.class.getResource (path);
	if (imgURL != null)
	{
	    return new ImageIcon (imgURL);
	}
	else
	{
	    System.err.println ("Couldn't find file: " + path);
	    return null;
	}
    }


    public void redraw ()
    { //redraws the screen
	int move = 0;
	for (int i = 0 ; i < row ; i++)
	{
	    for (int j = 0 ; j < col ; j++)
	    {
		a [move].setIcon (createImageIcon (b [i] [j] + ".jpg"));
		move++;
	    }
	}
	//sets current position
	a [curx * col + cury].setIcon (createImageIcon ("6.jpg"));
    }



    public void newLevel (int a[] [], int c[] [], int lvl)
    { //loads a new level
	//sets current to the respective level's starting position
	curx = startx [lvl];
	cury = starty [lvl];
	//sets the grid to the new level
	for (int i = 0 ; i < row ; i++)
	{
	    for (int j = 0 ; j < col ; j++)
	    {
		b [i] [j] = a [i] [j];
	    }
	}
	//sets the original grid to the new level
	for (int i = 0 ; i < row ; i++)
	{
	    for (int j = 0 ; j < col ; j++)
	    {
		ogb [i] [j] = c [i] [j];
	    }
	}
	redraw ();
	where = -1;
	storemove ();

    }


    public void storemove ()
    {
	//stores current position of grid into undo array
	where++;
	for (int i = 0 ; i < row ; i++)
	{
	    for (int j = 0 ; j < col ; j++)
	    {
		undo [where] [i] [j] = b [i] [j];
	    }
	}
	undoy [where] = cury;
	undox [where] = curx;
    }


    public boolean checkwin ()
    {
	//win condition checks if each of the yellow dot spaces contain boulders. If there are no storage locations then it returns true. else returns false
	int count = 0;
	for (int i = 0 ; i < row ; i++)
	{
	    for (int j = 0 ; j < col ; j++)
	    {
		if (b [i] [j] == 4)
		    count++;
	    }
	}
	//if win is true
	if (count == 0)
	    return true;
	else
	    return false;
    }


    //Checks if win condition is met, if it is then proceed to win screen
    public void proceedtowin ()
    {
	if (checkwin ())
	{
	    cdLayout.show (p_card, "4");
	    b [0] [0] = 4;
	}
    }


    //RESET METHOD
    public void reset ()
    {
	if (level == 1)
	    newLevel (b1, ogb1, 0);
	else if (level == 2)
	    newLevel (b2, ogb2, 1);
	else if (level == 3)
	    newLevel (b3, ogb3, 2);
	else if (level == 4)
	    newLevel (b4, ogb4, 3);
	else if (level == 5)
	    newLevel (b5, ogb5, 4);
	else if (level == 6)
	    newLevel (b6, ogb6, 5);
	score = 0;
	moves.setText ("Moves: " + score);
	cdLayout.show (p_card, "3");
	time = 0;
    }


    //UNDO METHOD
    public void undo ()
    {
	//reduce score and where variables by 1 because of the undo
	score--;
	//if score does not become negative, also means that there was a move made
	if (score > -1)
	{
	    where--;
	    //copies previous move into the current array
	    for (int i = 0 ; i < row ; i++)
	    {
		for (int j = 0 ; j < col ; j++)
		{
		    b [i] [j] = undo [where] [i] [j];
		}
	    }
	    curx = undox [where];
	    cury = undoy [where];
	    redraw ();
	    moves.setText ("Moves: " + score);
	}
	else
	{
	    score++;
	    ins.setText ("Make a move to undo.");
	}
    }


    //LEVEL SELECT METHOD
    public void lvlselect (int lvl)
    {
	//Sets the level to the screen based on which level was selected
	if (lvl == 1)
	{
	    level = 1;
	    newLevel (b1, ogb1, 0);
	    score = 0;
	    time = 0;
	    moves.setText ("Moves: " + score);
	    curlevel.setText ("Level: " + level);
	    cdLayout.show (p_card, "3");
	}
	else if (lvl == 2)
	{
	    level = 2;
	    newLevel (b2, ogb2, 1);
	    score = 0;
	    time = 0;
	    moves.setText ("Moves: " + score);
	    curlevel.setText ("Level: " + level);
	    cdLayout.show (p_card, "3");
	}
	else if (lvl == 3)
	{
	    level = 3;
	    newLevel (b3, ogb3, 2);
	    score = 0;
	    time = 0;
	    moves.setText ("Moves: " + score);
	    curlevel.setText ("Level: " + level);
	    cdLayout.show (p_card, "3");
	}
	else if (lvl == 4)
	{
	    level = 4;
	    newLevel (b4, ogb4, 3);
	    score = 0;
	    time = 0;
	    moves.setText ("Moves: " + score);
	    curlevel.setText ("Level: " + level);
	    cdLayout.show (p_card, "3");
	}
	else if (lvl == 5)
	{
	    level = 5;
	    newLevel (b5, ogb5, 4);
	    score = 0;
	    time = 0;
	    moves.setText ("Moves: " + score);
	    curlevel.setText ("Level: " + level);
	    cdLayout.show (p_card, "3");
	}
	else if (lvl == 6)
	{
	    level = 6;
	    newLevel (b6, ogb6, 5);
	    score = 0;
	    time = 0;
	    moves.setText ("Moves: " + score);
	    curlevel.setText ("Level: " + level);
	    cdLayout.show (p_card, "3");
	}
    }


    //MOVEMENT METHODS
    public void moveup ()
    { //move character up
	if (b [curx - 1] [cury] != 1)
	{
	    //box and box
	    if (b [curx - 1] [cury] == 5 && b [curx - 2] [cury] == 5)
	    {
		ins.setText ("Boulder beside a Boulder.\nTry something else!");
	    }
	    //box and blank
	    else if (b [curx - 1] [cury] == 5 && b [curx - 2] [cury] != 1)
	    {
		b [curx - 1] [cury] = ogb [curx - 1] [cury];
		b [curx - 2] [cury] = 5;
		curx--;
		ins.setText ("Use arrow keys.\nor Use W, A, S, D");
	    }
	    //box and wall
	    else if (b [curx - 1] [cury] == 5 && b [curx - 2] [cury] == 1)
	    {
		ins.setText ("The boulder is against the wall.\nTry something else!");
	    }
	    //no box just move
	    else
	    {
		curx--;
		ins.setText ("Use arrow keys.\nor Use W, A, S, D");
	    }
	    score++;
	    moves.setText ("Moves: " + score);
	    storemove ();
	}
	//checks if win condition is met, if it is then takes you to win screen
	proceedtowin ();
    }


    public void moveleft ()
    { //move character left
	if (b [curx] [cury - 1] != 1)
	{ //box and box
	    if (b [curx] [cury - 1] == 5 && b [curx] [cury - 2] == 5)
	    {
		ins.setText ("Boulder beside a Boulder.\nTry something else!");
	    }

	    //box and blank
	    else if (b [curx] [cury - 1] == 5 && b [curx] [cury - 2] != 1)
	    {
		b [curx] [cury - 1] = ogb [curx] [cury - 1];
		b [curx] [cury - 2] = 5;
		cury--;
		ins.setText ("Use arrow keys.\nor Use W, A, S, D");
	    }

	    //box and wall
	    else if (b [curx] [cury - 1] == 5 && b [curx] [cury - 2] == 1)
	    {
		ins.setText ("The boulder is against the wall.\nTry something else!");

	    }
	    //no box just move
	    else
	    {
		cury--;
		ins.setText ("Use arrow keys.\nor Use W, A, S, D");
	    }
	    score++;
	    moves.setText ("Moves: " + score);
	    storemove ();
	}
	//checks if win condition is met, if it is then takes you to win screen
	proceedtowin ();
    }


    public void moveright ()
    { //move character right
	if (b [curx] [cury + 1] != 1)
	{
	    //box and box
	    if (b [curx] [cury + 1] == 5 && b [curx] [cury + 2] == 5)
	    {
		ins.setText ("Boulder beside a Boulder.\nTry something else!");
	    }
	    //box and blank
	    else if (b [curx] [cury + 1] == 5 && b [curx] [cury + 2] != 1)
	    {
		b [curx] [cury + 1] = ogb [curx] [cury + 1];
		b [curx] [cury + 2] = 5;
		cury++;
		ins.setText ("Use arrow keys.\nor Use W, A, S, D");
	    }
	    //box and wall
	    else if (b [curx] [cury + 1] == 5 && b [curx] [cury + 2] == 1)
	    {
		ins.setText ("The boulder is against the wall.\nTry something else!");

	    }
	    //no box just move
	    else
	    {
		cury++;
		ins.setText ("Use arrow keys.\nor Use W, A, S, D");
	    }
	    score++;
	    moves.setText ("Moves: " + score);
	    storemove ();
	}
	//checks if win condition is met, if it is then takes you to win screen
	proceedtowin ();
    }


    public void movedown ()
    { //move character down
	if (b [curx + 1] [cury] != 1)
	{
	    //box and box
	    if (b [curx + 1] [cury] == 5 && b [curx + 2] [cury] == 5)
	    {
		ins.setText ("Boulder beside a Boulder.\nTry something else!");
	    }
	    //box and blank
	    else if (b [curx + 1] [cury] == 5 && b [curx + 2] [cury] != 1)
	    {
		b [curx + 1] [cury] = ogb [curx + 1] [cury];
		b [curx + 2] [cury] = 5;
		curx++;
		ins.setText ("Use arrow keys.\nor Use W, A, S, D");
	    }
	    //box and wall
	    else if (b [curx + 1] [cury] == 5 && b [curx + 2] [cury] == 1)
	    {
		ins.setText ("The boulder is against the wall.\nTry something else!");

	    }
	    //no box just move
	    else
	    {
		curx++;
		ins.setText ("Use arrow keys.\nor Use W, A, S, D");
	    }
	    score++;
	    moves.setText ("Moves: " + score);
	    storemove ();
	}
	//checks if win condition is met, if it is then takes you to win screen
	proceedtowin ();
    }


    //END OF MOVEMENT METHODS
    //ACTION PERFORMED
    public void actionPerformed (ActionEvent e)
    { //moves between the screens
	if (e.getActionCommand ().equals ("s1"))
	    cdLayout.show (p_card, "1");
	else if (e.getActionCommand ().equals ("s2"))
	    cdLayout.show (p_card, "2");
	else if (e.getActionCommand ().equals ("s3"))
	{
	    cdLayout.show (p_card, "3");
	    time = 0;
	}
	else if (e.getActionCommand ().equals ("s4"))
	{
	    boolean win = checkwin ();
	    if (!win)
		cdLayout.show (p_card, "5");
	    else
		cdLayout.show (p_card, "4");
	}
	else if (e.getActionCommand ().equals ("lvls"))
	    cdLayout.show (p_card, "6");
	else if (e.getActionCommand ().equals ("s5"))
	    cdLayout.show (p_card, "6");
	else if (e.getActionCommand ().equals ("lvlend"))
	    cdLayout.show (p_card, "6");
	else if (e.getActionCommand ().equals ("s6"))
	    System.exit (0);
	else if (e.getActionCommand ().equals ("quit"))
	    System.exit (0);
	//Reset Button
	else if (e.getActionCommand ().equals ("reset"))
	    reset ();
	//Undo button
	else if (e.getActionCommand ().equals ("undo"))
	    undo ();
	//Movement Commands
	else if (e.getActionCommand ().equals ("up"))
	    moveup ();
	else if (e.getActionCommand ().equals ("left"))
	    moveleft ();
	else if (e.getActionCommand ().equals ("right"))
	    moveright ();
	else if (e.getActionCommand ().equals ("down"))
	    movedown ();

	//Level Select Buttons
	else if (e.getActionCommand ().equals ("level1"))
	    lvlselect (1);
	else if (e.getActionCommand ().equals ("level2"))
	    lvlselect (2);
	else if (e.getActionCommand ().equals ("level3"))
	    lvlselect (3);
	else if (e.getActionCommand ().equals ("level4"))
	    lvlselect (4);
	else if (e.getActionCommand ().equals ("level5"))
	    lvlselect (5);
	else if (e.getActionCommand ().equals ("level6"))
	    lvlselect (6);

	else
	{ //code to handle the game
	    int n = Integer.parseInt (e.getActionCommand ());
	    int x = n / col;
	    int y = n % col;
	    showStatus ("(" + x + ", " + y + ")");
	}
	redraw ();
	proceedtowin ();
    }

//KEY LISTENING CODE ***Only works if no buttons are pressed, stops working after a button is pressed unless you leave the screen and come back***
    public void keyPressed (KeyEvent e)
    {
    }


    public void keyReleased (KeyEvent e)
    {
	switch (e.getKeyCode ())
	{
	    case KeyEvent.VK_D:
		{
		    moveright ();
		    redraw ();
		    proceedtowin ();
		    break;

		}
	    case KeyEvent.VK_A:
		{
		    moveleft ();
		    redraw ();
		    proceedtowin ();
		    break;
		}
	    case KeyEvent.VK_W:
		{

		    moveup ();
		    redraw ();
		    proceedtowin ();
		    break;
		}
	    case KeyEvent.VK_S:
		{
		    movedown ();
		    redraw ();
		    proceedtowin ();
		    break;
		}
	}
    }


    public void keyTyped (KeyEvent e)
    {
	switch (e.getKeyCode ())
	{
	    case KeyEvent.VK_RIGHT:
		break;
	    case KeyEvent.VK_LEFT:
		break;
	    case KeyEvent.VK_UP:
		break;
	    case KeyEvent.VK_DOWN:
		break;
	}
    }
}


