//Stdent Detail / Book Issue
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

class Frame2 extends Frame
{
	String ID;
	Label l1 , l2 , l3 , l4 , l5 , l6 ;
	Label l11 , l22 , l33 , l44 , l55 , l66 ;
	Label bn[] = new Label[5];
	Label a[] = new Label[5];
	TextField t;
	Button b ;
	int BookIssued = 0 ;
	int BookNo ;
	Connection con ;
	Frame2(Frame1 f,String s)
	{
		f.setVisible(false);
		ID =s;
		setTitle(s);
		setBounds(50,50,600,600);
		setLayout(null);
		addWindowListener(new WindowAdapter(){public void windowClosing(WindowEvent e){dispose();f.setVisible(true);}});
		l1 = new Label("Student ID");l1.setBounds(50,50,100,30);add(l1);
		l2 = new Label("Name");l2.setBounds(50,100,100,30);add(l2);
		l3 = new Label("Father");l3.setBounds(50,150,100,30);add(l3);
		l4 = new Label("Book Issude");l4.setBounds(50,200,100,30);add(l4);
		l5 = new Label("Book No.");l5.setBounds(50,250,100,30);add(l5);
		l6 = new Label("Message");l6.setBounds(50,300,100,30);add(l6);
		l66 = new Label();l66.setBounds(200,300,300,30);add(l66);
		t = new TextField();t.setBounds(200,250,200,30);add(t);
		b = new Button("Issue");b.setBounds(500,250,100,30);add(b);
		bn[0] = new Label();bn[0].setBounds(50,350,300,50);add(bn[0]);
		bn[1] = new Label();bn[1].setBounds(50,400,300,50);add(bn[1]);
		bn[2] = new Label();bn[2].setBounds(50,450,300,50);add(bn[2]);
		bn[3] = new Label();bn[3].setBounds(50,500,300,50);add(bn[3]);
		a[0] = new Label();a[0].setBounds(400,350,200,50);add(a[0]);
		a[1] = new Label();a[1].setBounds(400,400,200,50);add(a[1]);
		a[2] = new Label();a[2].setBounds(400,450,200,50);add(a[2]);
		a[3] = new Label();a[3].setBounds(400,500,200,50);add(a[3]);
		b.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){b_Click();}});
		try
			{
					Class.forName("org.gjt.mm.mysql.Driver");
					con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/library","root","");
					PreparedStatement st  = con.prepareStatement("select * From Studentdetail where ID = ?");
					st.setString(1,ID);
					ResultSet rs = st.executeQuery();
					if(rs.next())
					{
						String s1 = rs.getString(1);
						String s2= rs.getString(2);
						String s3 = rs.getString(3);
						l11 = new Label(s1);l11.setBounds(200,50,100,30);add(l11);
						l22 = new Label(s2);l22.setBounds(200,100,200,30);add(l22);
						l33 = new Label(s3);l33.setBounds(200,150,200,30);add(l33);
						st  = con.prepareStatement("SELECT count(bookno) FROM `issue` WHERE ID=?");
						st.setString(1,ID);
						rs = st.executeQuery();
						if(rs.next())
						{
							BookIssued = rs.getInt(1);
							l44 = new Label(BookIssued+"");l44.setBounds(200,200,100,30);add(l44);
							if(BookIssued>=4) b.disable();
						}
						printBookDetails();
					}
			}catch(Exception e){System.out.println(e);}
	}
	void b_Click()
	{
		try
		{
			BookNo = Integer.parseInt(t.getText());
			PreparedStatement st  = con.prepareStatement("SELECT count(BookNo) from bookavailable where BookNo=?");
			st.setInt(1,BookNo);
			ResultSet rs = st.executeQuery();
			if(rs.next()&&rs.getInt(1)==1)
			{
				st = con.prepareStatement("delete from bookavailable where BookNo=?");
				st.setInt(1,BookNo);
				st.executeUpdate();
				st =con.prepareStatement("INSERT INTO  `library`.`issue` (`ID` ,`BookNo` ,`Date` ,`Time`)VALUES (?,  ?,  '2015-07-17',  '00:00:00');");
				st.setString(1,ID);
				st.setInt(2,BookNo);
				st.executeUpdate();
				l66.setText(" Book Issued");
				st  = con.prepareStatement("SELECT count(bookno) FROM `issue` WHERE ID=?");
				st.setString(1,ID);
				rs = st.executeQuery();

				if(rs.next())
				{
				BookIssued = rs.getInt(1);
				l44.setText(BookIssued+"");
				if(BookIssued>=4) b.disable();
				}
				printBookDetails();
			}
			else
			{
				l66.setText(" Book is Not Available");
			}
		}catch(Exception e){System.out.println(e);}
	}
	void printBookDetails()
	{
		int i =0;
		int[] bno = new int[BookIssued];
		int[] bid = new int[BookIssued];
		try
		{
			PreparedStatement st  = con.prepareStatement("SELECT BookNo from issue where ID=?");
			st.setString(1,ID);
			ResultSet rs = st.executeQuery();
			while(rs.next())
			{
				bno[i] = rs.getInt(1);
				i++;
			}
			i=0;
			while(i<BookIssued)
			{
				st  = con.prepareStatement("SELECT BookID from BookNo where BookNo=?");
				st.setInt(1,bno[i]);
				rs = st.executeQuery();
				rs.next();
				bid[i] = rs.getInt(1);
				i++;
			}
			i=0;
			while(i<BookIssued)
			{
					st  = con.prepareStatement("SELECT BookName , Author from BookDetail where BookID=?");
					st.setInt(1,bid[i]);
					rs = st.executeQuery();
					rs.next();
					String s1 = rs.getString("BookName");bn[i].setText(s1);
					String s2 = rs.getString("Author");a[i].setText(s2);
					System.out.println(s1+"   "+s2);
					i++;
			}
		}catch(Exception e)
		{
			System.out.println(" In Frame2.printBookDetails() \n"+e);
		}
	}
}