import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;


public class MainFrame extends JFrame {

	public MainFrame() throws SQLException
	{
		super("Client Manager");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		createTable();
		drawGUI();
		setVisible(true);
		setSize(700,550);
		setLocation(250,100);
	}
	
	
	public void createTable()
	{
		Connection conn=DBUtil.getConnection();
		
		boolean tableExists=false;
		
		try{
			DatabaseMetaData dbmd=conn.getMetaData();
			ResultSet rs=dbmd.getTables(null, null, null, new String[] {"TABLE"});
			if(rs.next())
			{
				if(rs.getString(3).equals("CLIENTI"))
					tableExists=true;
			}
		}catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		if(tableExists!=true)
		{
			try
			{
				Statement s=conn.createStatement();
				s.execute("CREATE TABLE CLIENTI (CNP VARCHAR(20) NOT NULL,NUME VARCHAR(30),TIP_ABONAMENT VARCHAR(20),DATA_INCEPUT DATE,DATA_FINAL DATE,CONSTRAINT CNP_PEY PRIMARY KEY(CNP))");
				conn.commit();
			} catch (SQLException ex)
			{
				DBUtil.displaySQLExceptions(ex);
			}
		}
		
	}
	
	public void drawGUI() 
	{
		this.setLayout(new BorderLayout());
		this.setSize(300, 500);
		this.setVisible(true);
		
		final ArrayList columnNames=new ArrayList();
		final ArrayList data=new ArrayList();
		
		/*	Connection conn1=DBUtil.getConnection();
		String s="DELETE FROM CLIENTI";
		try {
			PreparedStatement ps1=conn1.prepareStatement(s);
			ps1.executeUpdate();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		*/
		
		
		final String sql="SELECT * FROM CLIENTI ORDER BY NUME";
		Connection conn=DBUtil.getConnection();
		
		try
		{
			PreparedStatement ps=conn.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			ResultSetMetaData md=rs.getMetaData();
			int columns=md.getColumnCount();
			
			for(int i=1;i<=columns;i++)
				columnNames.add(md.getColumnName(i));
			
			while(rs.next())
			{
				Vector row=new Vector(columns);
				for(int i=1;i<=columns;i++)
				{
					row.add(rs.getObject(i));
				}
				data.add(row);
			}
		} catch(SQLException e)
		{
			DBUtil.displaySQLExceptions(e);
		}
	
		Vector columnNamesVector=new Vector();
		Vector dataVector=new Vector();
		
		for(int i=0;i<data.size();i++)
		{
			Vector subArray=(Vector)data.get(i);
			Vector subVector=new Vector();
			for(int j=0;j<subArray.size();j++)
			{
				subVector.add(subArray.get(j));
			}
			dataVector.add(subVector);
		}
		
		for(int i=0;i<columnNames.size();i++)
		{
			columnNamesVector.add(columnNames.get(i));
		}
		
		
		
		JTable tabel=new JTable(dataVector,columnNamesVector)
		{
			public Class getColumnClass(int column)
			{
				for(int row=0;row<getRowCount();row++)
				{
					Object obj=getValueAt(row,column);
					if(obj!=null)
					{
						return obj.getClass();
					}
				}
				return Object.class;
			}
			
			public boolean isCellEditable(int row,int column)
			{
				return false;
			}
		};
		
		TitledBorder title=BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black),"Lista Clienti");
		title.setTitleJustification(TitledBorder.CENTER);
		
		
		JScrollPane jsp=new JScrollPane(tabel);
		this.add(jsp,BorderLayout.NORTH);
		jsp.setBorder(title);
		
		JButton butAdNewClient=new JButton("Client Nou");
		JPanel panel1=new JPanel();
		panel1.add(butAdNewClient);
		this.add(panel1);
		JButton butRenew=new JButton("Reinnoire Abonament");
		panel1.add(butRenew);
		JButton butDeleteClient=new JButton("Sterge Client");
		panel1.add(butDeleteClient);
		JButton butRefresh=new JButton("Reactualizare");
		panel1.add(butRefresh);
	
		butRefresh.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				setVisible(false);
				try {
					new MainFrame().setVisible(true);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		
		
		
		
		butAdNewClient.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new NewClient().setVisible(true);
			}
		});
			
		butDeleteClient.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new DeleteClient().setVisible(true);
			}
		});
		
		butRenew.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new RenewAb().setVisible(true);
			}
		});
	
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
