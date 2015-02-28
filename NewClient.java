import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.border.EmptyBorder;


public class NewClient extends JFrame {
	
	private JLabel labelCNP;
	private JTextField textCNP;
	private JTextField textNume;
	private Choice tipAb;
	private JSpinner spinner;
	
	public NewClient()
	{
		super("Adaugare Client Nou");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		drawGUI();
		this.pack();
		this.setLocation(400, 200);
	}
	
	public void drawGUI()
	{
		this.setLayout(new GridLayout(0,2));
		labelCNP=new JLabel("CNP");
		textCNP=new JTextField(15);
		this.add(labelCNP);
		this.add(textCNP);
		
		JLabel labelNume=new JLabel("Nume");
		textNume=new JTextField(30);
		this.add(labelNume);
		this.add(textNume);
		
		JLabel labelTipA=new JLabel("Tip Abonament");
		tipAb = new Choice();
		tipAb.add("Fitness 30 zile");
		tipAb.add("Aerobic 10 Sedinte");
		this.add(labelTipA);
		this.add(tipAb);
		
		JLabel labeData=new JLabel("Data incepere");
	 
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
	    
		this.add(labeData);
		JLabel labelDataI=new JLabel(dataToday);
		JLabel labelDataFinal=new JLabel("Data Expirarii");
		JLabel labelDataF=new JLabel(data30);
		this.add(labelDataI);
		this.add(labelDataFinal);
		this.add(labelDataF);
		
		JButton butAdd=new JButton("Adauga Client");
		JButton butDel=new JButton("Inapoi");
		
		this.add(butAdd);
		this.add(butDel);
		butAdd.setSize(2,2);
		butAdd.setBackground(Color.ORANGE);
		butDel.setBackground(Color.PINK);
		
		
		
		butDel.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				setVisible(false);
			}
		});
		
		butAdd.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String getCnp=null;
				String getNume=null;
				String getAb=null;
				String dataS=null;
				getCnp=textCNP.getText().trim();
				getNume=textNume.getText().trim();
				getAb=tipAb.getSelectedItem().trim();
			
				/*Object obj=df.format(spinner.getValue());
				dataS=obj.toString();
				System.out.println(dataS);*/
				
				Date dT = null;
				Date d30 =null;
				try {
					dT = df.parse(dataToday);
					d30=df.parse(data30);
				} catch (ParseException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				
				if(getCnp.length()>5 &&  getNume.length()>3 && getAb.length()>5 && dataToday.length()>3 && data30.length()>3)
				{
					Connection conn=DBUtil.getConnection();
					try
					{
						PreparedStatement ps=conn.prepareStatement("INSERT INTO CLIENTI(CNP,NUME,TIP_ABONAMENT,DATA_INCEPUT,DATA_FINAL) VALUES(?,?,?,?,?)");
						ps.setString(1,getCnp);
						ps.setString(2,getNume);
						ps.setString(3, getAb);
						ps.setTimestamp(4,new Timestamp(dT.getTime()));
						ps.setTimestamp(5, new Timestamp(d30.getTime()));
						int r=ps.executeUpdate();
						if(r>0)
						{
							JOptionPane.showMessageDialog(null,"Client adaugat");
							
						}	
						else JOptionPane.showMessageDialog(null,"Eroare! Va rugam reincercati");
						conn.commit();
					} catch(SQLException e1)
					{
						DBUtil.displaySQLExceptions(e1);
					}
				}
				else JOptionPane.showMessageDialog(null,"Toate campurile trebuiesc completate");
				}
			
	}
		
		);
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					NewClient frame = new NewClient();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
