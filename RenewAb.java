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
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;


public class RenewAb extends JFrame {

	public RenewAb()
	{
		super("Reinnoire abonament");
		drawGui();
		this.setVisible(true);
		this.pack();
		this.setLocation(500, 200);
	}
	
	public void drawGui()
	{
		this.setLayout(new GridLayout(0,2));
		JLabel labelNume=new JLabel("Nume Client");
		this.add(labelNume);
		final Choice choiceNume=new Choice();
		
		Connection conn=DBUtil.getConnection();
		String sql="SELECT NUME FROM CLIENTI ORDER BY NUME";
		
		try
		{
			PreparedStatement ps=conn.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			ResultSetMetaData md=rs.getMetaData();
			
			while(rs.next())
			{
				choiceNume.add((String) rs.getObject(1));
			}
			conn.commit();
		} catch(SQLException e)
		{
			DBUtil.displaySQLExceptions(e);
		}
		this.add(choiceNume);
		
		JLabel labelTipAb=new JLabel("Tip Abonament");
		final Choice tipAb=new Choice();
		tipAb.add("Fitness 30 zile");
		tipAb.add("Aerobic 10 Sedinte");
		this.add(labelTipAb);
		this.add(tipAb);
		
		JLabel dataI=new JLabel("Data inceperii");
		
		Date today=new Date();
		Calendar cal=Calendar.getInstance();
		cal.setTime(today);
		cal.add(Calendar.DAY_OF_MONTH, 30);
		Date today30=cal.getTime();
		
		final DateFormat df=new SimpleDateFormat("dd-MM-yyyy");
		
		Object objToday=df.format(today);
		final String dataToday=objToday.toString();
		Object obj30=df.format(today30);
		final String data30=obj30.toString();
		
		JLabel dataI2=new JLabel(dataToday);
		this.add(dataI);
		this.add(dataI2);
		JLabel dataE=new JLabel("Data expirarii");
		this.add(dataE);
		JLabel dataE2=new JLabel(data30);
		this.add(dataE2);
		
		JButton butRenew=new JButton("Reinnoire Abonament");
		this.add(butRenew);
		JButton butBack=new JButton("Inapoi");
		this.add(butBack);
		
		butRenew.setBackground(Color.ORANGE);
		butBack.setBackground(Color.PINK);
		
		butBack.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				setVisible(false);
			}
		});
		
		butRenew.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String getNume=null;
				String getAb=null;
				String getDataI=null;
				String getDataE=null;
				
				
				getNume=choiceNume.getSelectedItem();
				getAb=tipAb.getSelectedItem().trim();
				
				Date dT = null;
				Date d30 =null;
				try {
					dT = df.parse(dataToday);
					d30=df.parse(data30);
				} catch (ParseException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				
				Connection conn=DBUtil.getConnection();
				try
				{
					PreparedStatement ps=conn.prepareStatement("UPDATE CLIENTI SET TIP_ABONAMENT=?, DATA_INCEPUT=? , DATA_FINAL=? WHERE NUME=?");
					ps.setString(1,getAb);
					ps.setTimestamp(2, new Timestamp(dT.getTime()));
					ps.setTimestamp(3, new Timestamp(d30.getTime()));
					ps.setString(4, getNume);
					
					int r=ps.executeUpdate();
					if(r>0)
					{
						JOptionPane.showMessageDialog(null,"Abonament reinnoit cu succes");
					}
					else JOptionPane.showMessageDialog(null,"Eroare! Va rugam reincercati");
				}
				catch(SQLException e1)
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
					RenewAb frame = new RenewAb();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		}

}
	
