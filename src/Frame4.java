import java.awt.*;
import java.awt.event.*;
import java.sql.*;
//Library panal
class Frame4 extends Frame
{
	Label l1, l2 ;
	Font font;
	Button b1 ;
	Button b2 ;
	Button b3 ;
	Font font1 ;
	Frame4()
	{
		setTitle("Library Panel");
		setBounds(50,50,600,600);
		setLayout(null);
		addWindowListener(new WindowAdapter(){public void windowClosing(WindowEvent e){System.exit(0);}});
		font = new Font("arial",1,20);
		font1 = new Font("Arial",2,15);
		l1 = new Label("JP Institute of Engineering & Technology ",Label.CENTER);l1.setBounds(0,50,550,30);add(l1);l1.setFont(font);
		l2 = new Label(" Meerut (U.P) - 250001",Label.CENTER);l2.setBounds(0,90,550,30);add(l2);l2.setFont(font);
		Button b1 = new Button("Student Detail / Issue");b1.setBounds(200,150,200,50);add(b1);b1.setFont(font1);
		Button b2 = new Button("Return");b2.setBounds(200,200,200,50);add(b2);b2.setFont(font1);
		Button b3 = new Button("Book Details");b3.setBounds(200,250,200,50);add(b3);b3.setFont(font1);
		b1.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){b1_Click();}});
		b2.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){b2_Click();}});
		b3.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){b3_Click();}});
	}
	void b1_Click()
	{
		//Student Entry Point
		new Frame1(this).setVisible(true);
	}
	void b2_Click()
	{
		//Book Returning Point
		new Frame3(this).setVisible(true);
	}
void b3_Click()
	{
		//Book details searching and adding
		new Frame5(this).setVisible(true);
	}
}