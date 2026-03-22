//Book Returning Point
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

class Frame3 extends Frame
{
	Label l1 , l2 , l3 ;
	TextField t;
	Button b ;
	int BookNo ;
	Connection con ;
	Frame3(Frame4 f)
	{
		f.setVisible(false);
		setTitle("Book Returning Point");
		setBounds(50,50,600,600);
		setLayout(null);
		addWindowListener(new WindowAdapter(){public void windowClosing(WindowEvent e){dispose();f.setVisible(true);}});
		l1 = new Label("Enter Book No.");l1.setBounds(50,50,100,30);add(l1);
		t = new TextField();t.setBounds(200,50,50,30);add(t);
		b = new Button("Return");b.setBounds(200,150,100,30);add(b);
		l2 = new Label("Massege");l2.setBounds(50,200,100,30);add(l2);
		l3 = new Label();l3.setBounds(200,250,300,30);add(l3);

		b.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){b_Click();}});
		try
			{
					Class.forName("org.gjt.mm.mysql.Driver");
					con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/library","root","");
			}catch(Exception e){System.out.println(e);}
	}
	void b_Click()
	{
		try
		{
			BookNo = Integer.parseInt(t.getText());
			PreparedStatement st  = con.prepareStatement("SELECT count(BookNo) from BookNo where BookNo=?");
			st.setInt(1,BookNo);
			ResultSet rs = st.executeQuery();
			if(rs.next()&&rs.getInt(1)==1)
			{
				st  = con.prepareStatement("SELECT count(BookNo) from issue where BookNo=?");
					st.setInt(1,BookNo);
					 rs = st.executeQuery();
					if(rs.next()&&rs.getInt(1)==1)
					{
					st = con.prepareStatement("delete from issue where BookNo=?");
					st.setInt(1,BookNo);
					st.executeUpdate();
					st =con.prepareStatement("INSERT INTO  `library`.`BookAvailable` VALUES (?);");
					st.setInt(1,BookNo);
					st.executeUpdate();
					l3.setText("Returned . ");
				}
				else
				{
					l3.setText("All-ready Returned . ");
				}

			}
			else
			{
				l3.setText("BookNo is InValid . ");
				//System.out.println(" BookNo is InValid.");
			}
		}catch(Exception e){System.out.println(e);}


	}
}