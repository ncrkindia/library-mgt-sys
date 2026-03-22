//Book details searching and adding
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

class Frame5 extends Frame
{
	Label l1, l2 ,l3, l4 ,l5 ,l6;
	TextField t1 , t2 , t3,t4;
	Font font;
	Button b1 ;
	Button b2 ;
	Button b3 ;
	Button b4 ;
	Connection con ;
	PreparedStatement st;
	ResultSet rs;
	Frame5(Frame4 f)
	{
		f.setVisible(false);
		setTitle("Book Detail :: Add/Search");
		setBounds(50,50,600,600);
		setLayout(null);
		addWindowListener(new WindowAdapter(){public void windowClosing(WindowEvent e){dispose();f.setVisible(true);}});
		font = new Font("arial",1,15);
		l1 = new Label("Book ID");l1.setBounds(50,50,100,30);add(l1);l1.setFont(font);
		l2 = new Label("Book Name");l2.setBounds(50,100,100,30);add(l2);l2.setFont(font);
		l3 = new Label("Author");l3.setBounds(50,150,100,30);add(l3);l3.setFont(font);
		l6 = new Label("Book No");l6.setBounds(50,200,100,30);add(l6);l6.setFont(font);
		t1 = new TextField();t1.setBounds(200,50,200,30);add(t1);t1.setFont(font);
		t2 = new TextField();t2.setBounds(200,100,200,30);add(t2);t2.setFont(font);
		t3 = new TextField();t3.setBounds(200,150,200,30);add(t3);t3.setFont(font);
		t4 = new TextField();t4.setBounds(200,200,200,30);add(t4);t4.setFont(font);
		l4 = new Label("Massege");l4.setBounds(50,450,100,30);add(l4);l4.setFont(font);
		l5 = new Label();l5.setBounds(50,500,400,30);add(l5);l5.setFont(font);
		Button b1 = new Button("Search Name");b1.setBounds(200,250,100,50);add(b1);
		Button b2 = new Button("Search ID");b2.setBounds(200,300,100,50);add(b2);
		Button b3 = new Button("Add to DataBase");b3.setBounds(300,250,100,50);add(b3);
		Button b4 = new Button("Seach BookNo");b4.setBounds(300,300,100,50);add(b4);
		b1.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){b1_Click();}});
		b2.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){b2_Click();}});
		b3.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){b3_Click();}});
		b4.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){b4_Click();}});
		try
		{
			Class.forName("org.gjt.mm.mysql.Driver");
			con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/library","root","");
		}catch(Exception e ){System.out.println(e);}
	}
	void b1_Click()
	{
		try{
		st = con.prepareStatement("Select * From BookDetail where BookName = ?");
		st.setString(1,t2.getText());
		rs = st.executeQuery();
		if(rs.next())
		{
			int id = rs.getInt("BookID");
			String s1 = rs.getString("BookName");
			String s2 = rs.getString("Author");
			t1.setText(id+"");
			t2.setText(s1);
			t3.setText(s2);
			t4.setText("");
			l5.setText("Searching Success !");
		}
		else
		{
			t1.setText("");
			t2.setText("");
			t3.setText("");
			t4.setText("");
			l5.setText("Error :: Invalid Book Name .");
		}
		}catch(Exception e){System.out.println(e);}
	}
	void b2_Click()
	{
try{
		st = con.prepareStatement("Select * From BookDetail where BookID = ?");
		st.setInt(1,Integer.parseInt(t1.getText()));
		rs = st.executeQuery();
		if(rs.next())
		{
			int id = rs.getInt("BookID");
			String s1 = rs.getString("BookName");
			String s2 = rs.getString("Author");
			t1.setText(id+"");
			t2.setText(s1);
			t3.setText(s2);
			t4.setText("");
			l5.setText("Searching Success !");
		}
		else
		{
			t1.setText("");
			t2.setText("");
			t3.setText("");
			t4.setText("");
			l5.setText("Error :: Invalid Book ID.");
		}
		}catch(NumberFormatException ee){l5.setText("Error :: Plesae , Fill Valid(integer) value for Book ID .");}
		catch(Exception e){System.out.println(e);}
	}
	void b3_Click()
		{
try{
		st = con.prepareStatement("Insert Into BookDetail Values(? , ? , ?)");
		st.setInt(1,Integer.parseInt(t1.getText()));
		st.setString(2,t2.getText());
		st.setString(3,t3.getText());
		st.executeUpdate();
		t1.setText("");
		t2.setText("");
		t3.setText("");
		l5.setText("Successfully added to Database .");
		}catch(NumberFormatException ee){l5.setText("Error :: Please , Fill Valid(integer) value for Book ID .");}
		catch(Exception e){
		t1.setText("");
		t2.setText("");
		t3.setText("");
		t4.setText("");
		l5.setText("Error  :: Please , Fill the entry correctly .");
		System.out.println(e);}
	}
	void b4_Click()
	{
try{
		st = con.prepareStatement("Select * From BookNo where BookNo= ?");
		st.setInt(1,Integer.parseInt(t4.getText()));
		rs = st.executeQuery();
		if(rs.next())
		{
			int id = rs.getInt("BookID");
			st = con.prepareStatement("Select * From BookDetail where BookID = ?");
			st.setInt(1,id);
			rs = st.executeQuery();
			rs.next();
			String s1 = rs.getString("BookName");
			String s2 = rs.getString("Author");
			t1.setText(id+"");
			t2.setText(s1);
			t3.setText(s2);
			l5.setText("Searching Success !");
		}
		else
		{
			t1.setText("");
			t2.setText("");
			t3.setText("");
			t4.setText("");
			l5.setText("Error :: Invalid Book No.");
		}
		}catch(NumberFormatException ee){l5.setText("Error :: Plesae , Fill Valid(integer) value for Book No .");}
		catch(Exception e){System.out.println(e);}
	}
}