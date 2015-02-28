import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;


public class DeleteClient extends JFrame {

	
	public DeleteClient()
	{
		super("Sterge Client");
		drawGui();
		this.setVisible(true);
		this.setSize(400, 100);
		this.setLocation(500, 200);
	}
	
	public void drawGui()
	{
		this.setLayout(new GridLayout(0,2));
		JLabel labelN=new JLabel("Selecteaza numele clientului");
		
		final Choice n=new Choice();
		
		Connection conn=DBUtil.getConnection();
		String sql="SELECT NUME FROM CLIENTI ORDER BY NUME";
		
		try
		{
			PreparedStatement ps=conn.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			ResultSetMetaData md=rs.getMetaData();
			
			while(rs.next())
			{
				n.add((String) rs.getObject(1));
			}
			conn.commit();
		} catch(SQLException e)
		{
			DBUtil.displaySQLExceptions(e);
		}
		
		this.add(labelN);
		this.add(n);
		
		JButton delClient=new JButton("Sterge Client");
		this.add(delClient);
		JButton butInapoi=new JButton("Inapoi");
		this.add(butInapoi);
		delClient.setBackground(Color.ORANGE);
		butInapoi.setBackground(Color.PINK);
		
		butInapoi.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				setVisible(false);
			}
		});
		
		delClient.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String delName=n.getSelectedItem();
				Connection conn=DBUtil.getConnection();
				try
				{
					PreparedStatement ps=conn.prepareStatement("DELETE FROM CLIENTI WHERE NUME=?");
					ps.setString(1, delName);
					int r=ps.executeUpdate();
					if(r>0)
					{
						JOptionPane.showMessageDialog(null,"Clientul <"+delName+"> a fost sters cu succes!");
						
					}	
					else JOptionPane.showMessageDialog(null,"Eroare! Va rugam reincercati");
					conn.commit();
				
				}catch(SQLException e1)
				{
					DBUtil.displaySQLExceptions(e1);
				}
				
			}
		});
		
	}

	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DeleteClient frame = new DeleteClient();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}



}
