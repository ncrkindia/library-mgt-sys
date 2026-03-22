//Student Entry Point
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

class Frame1 extends Frame
{
	Label l1 , l2 , l3 ;
	Button b1;
	TextField t1;
	Connection con ;
	Frame1(Frame4 f)
	{
		f.setVisible(false);
		setTitle("Library :: Student Entry Point");
		try
		{
			Class.forName("org.gjt.mm.mysql.Driver");
			con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/library","root","");
		}catch(Exception e){System.out.println(e);}
		setBounds(50,50,600,500);
		setLayout(null);
		l2 = new Label();l2.setBounds(50,200,100,50);add(l2);
		l3 = new Label();l3.setBounds(100,250,300,50);add(l3);
		addWindowListener(new WindowAdapter(){public void windowClosing(WindowEvent e){dispose();f.setVisible(true);}});
		l1 = new Label("Student ID");l1.setBounds(50,50,100,30);add(l1);
		t1 = new TextField();t1.setBounds(200,50,200,30);add(t1);
		b1 = new Button("Enter");b1.setBounds(300,150,100,50);add(b1);
		b1.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){b1_Click();}});
	}
	void b1_Click()
	{
		try
		{
			PreparedStatement st  = con.prepareStatement("select * From Studentdetail where ID = ?");
			String s = t1.getText();
			st.setString(1,s);
			ResultSet rs = st.executeQuery();
			if(rs.next())
			{
				l2.setText("");
				l3.setText("");
				new Frame2(this,s).setVisible(true);
			}
			else
			{
				l2.setText("Error");
				l3.setText("Sorry , Invalid Student ID .");

			}

		}catch(Exception e){System.out.println(e);}


	}
}