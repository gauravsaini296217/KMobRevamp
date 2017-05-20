package exportimport;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class MainFrame extends JFrame {

	private JPanel contentPane;
	private JTextField ipaddress;
	private JTextField jid;
	private JTextField book;
	private JTextField m_ipaddress;
	private JComboBox<String> databases, m_databases;
	private Connection ipcon, metacon;
	private JComboBox<String> ipUsers, metaUsers;
	
	/**
	 * Launch the application.
	 */
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

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		setTitle("NLRM ExportImport");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 627, 374);
		contentPane = new JPanel();
		contentPane.setBackground(SystemColor.controlShadow);
		contentPane.setBorder(new LineBorder(new Color(0, 0, 0)));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "IP Config", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Meta Config", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		
		JLabel label_1 = new JLabel("IP Address");
		
		m_ipaddress = new JTextField();
		m_ipaddress.setColumns(10);
		
		JLabel label_2 = new JLabel("Database");
		
		m_databases = new JComboBox();
		
		JButton button = new JButton("R");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Connection con;
				try {
					
					m_databases.removeAllItems();
					Class.forName("org.postgresql.Driver");
					con=DriverManager.getConnection("jdbc:postgresql://"+ipaddress.getText()+"/?","postgres","root");
					PreparedStatement ps=con.prepareStatement("SELECT datname FROM pg_database WHERE datistemplate = false;");
					ResultSet rs=ps.executeQuery();
					while(rs.next())
					{
						m_databases.addItem(rs.getString(1));
					}
					ps.close();
					con.close();
					
				} catch (Exception e2) {
				
					JOptionPane.showMessageDialog(null, "Error:"+e2, "NLRMEI", JOptionPane.ERROR_MESSAGE);
					
				}
				
				
			}
		});
		
		metaUsers = new JComboBox();
		
		JButton btnNewButton_3 = new JButton("Con");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
	try {
					
					metaUsers.removeAllItems();
					Class.forName("org.postgresql.Driver");
					metacon=DriverManager.getConnection("jdbc:postgresql://"+m_ipaddress.getText()+"/"+m_databases.getSelectedItem().toString(),"postgres","root");
					PreparedStatement ps=metacon.prepareStatement("select distinct userid from tbprivileged where madmin='Y' and length(userid)=6 and userid!='VOL000'");
					ResultSet rs=ps.executeQuery();
					while(rs.next())
					{
						metaUsers.addItem(rs.getString(1));
					}
					ps.close();
					
				} catch (Exception e2) {
				
					JOptionPane.showMessageDialog(null, "Error:"+e2, "NLRMEI", JOptionPane.ERROR_MESSAGE);
					
				}	
				
			}
		});
		
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_1.createSequentialGroup()
							.addComponent(label_1)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(m_ipaddress, GroupLayout.PREFERRED_SIZE, 121, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel_1.createSequentialGroup()
							.addComponent(label_2, GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE)
							.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel_1.createSequentialGroup()
									.addGap(20)
									.addComponent(metaUsers, GroupLayout.PREFERRED_SIZE, 106, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_panel_1.createSequentialGroup()
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(m_databases, 0, 121, Short.MAX_VALUE)))))
					.addGap(18)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
						.addComponent(button, GroupLayout.PREFERRED_SIZE, 51, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnNewButton_3))
					.addGap(26))
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_1.createSequentialGroup()
							.addComponent(button)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(btnNewButton_3))
						.addGroup(gl_panel_1.createSequentialGroup()
							.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
								.addComponent(label_1)
								.addComponent(m_ipaddress, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
								.addComponent(label_2)
								.addComponent(m_databases, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(metaUsers, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(51, Short.MAX_VALUE))
		);
		panel_1.setLayout(gl_panel_1);
		
		JButton btnNewButton_1 = new JButton("Start Import");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
				 PreparedStatement psp2=ipcon.prepareStatement("select m.talukaid,m.villageid,md.etitle,m.docid,dm.edesc,m.jid,d.bookno,d.actualpages,m.cid from tbimgworkassign i inner join tbjacketd d on i.jid=d.jid and i.bookno=d.bookno and i.assigntarget=d.actualpages and i.pendings=0 inner join tbjacketm m on i.jid=m.jid inner join tbmasterdata md on m.villageid=md.id inner join tbdocumentm dm on m.docid=dm.docid and m.ofcid=dm.ofcid where d.jid='"+jid.getText()+"' and d.bookno='"+book.getText()+"' order by m.jid,d.bookno");
			     ResultSet rsp2=psp2.executeQuery();
			     while(rsp2.next()){
			     PreparedStatement psp3=metacon.prepareStatement("select distinct jid,bookno from tbimagetrans where refjid="+rsp2.getString(6)+" and bookno="+rsp2.getString(7)+" group by jid,bookno");
			     ResultSet rsp3=psp3.executeQuery();
			     if(rsp3.next()){    
			         System.out.println("NFJid:"+rsp2.getString(6)+" ,Bookno:"+rsp2.getString(7));    
			     }
			         else
			     {
			      
			     System.out.println("F_Jid:"+rsp2.getString(6)+" ,Bookno:"+rsp2.getString(7));    
			     }
			     }
				}catch(Exception e2)
				{
					
					JOptionPane.showMessageDialog(null, "Error:"+e2, "NLRMEI", JOptionPane.ERROR_MESSAGE);
					
				}
			}
		});
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(14)
							.addComponent(panel, GroupLayout.PREFERRED_SIZE, 290, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 289, Short.MAX_VALUE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(255)
							.addComponent(btnNewButton_1)))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 189, GroupLayout.PREFERRED_SIZE)
						.addComponent(panel, GroupLayout.PREFERRED_SIZE, 189, GroupLayout.PREFERRED_SIZE))
					.addGap(44)
					.addComponent(btnNewButton_1)
					.addContainerGap(67, Short.MAX_VALUE))
		);
		
		JLabel lblNewLabel = new JLabel("IP Address");
		
		ipaddress = new JTextField();
		ipaddress.setColumns(10);
		
		JLabel lblDb = new JLabel("Database");
		
		databases = new JComboBox<String>();
		
		JButton btnNewButton = new JButton("R");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Connection con;
			try {
				
				databases.removeAllItems();
				Class.forName("org.postgresql.Driver");
				con=DriverManager.getConnection("jdbc:postgresql://"+ipaddress.getText()+"/?","postgres","root");
				PreparedStatement ps=con.prepareStatement("SELECT datname FROM pg_database WHERE datistemplate = false;");
				ResultSet rs=ps.executeQuery();
				while(rs.next())
				{
					databases.addItem(rs.getString(1));
				}
				ps.close();
				con.close();
				
			} catch (Exception e2) {
			
				JOptionPane.showMessageDialog(null, "Error:"+e2, "NLRMEI", JOptionPane.ERROR_MESSAGE);
				
			}
			
				
			}
		});
		
		JLabel lblJid = new JLabel("Jid");
		lblJid.setHorizontalAlignment(SwingConstants.CENTER);
		
		jid = new JTextField();
		jid.setColumns(10);
		
		JLabel lblBook = new JLabel("Book");
		lblBook.setHorizontalAlignment(SwingConstants.CENTER);
		
		book = new JTextField();
		book.setColumns(10);
		
		ipUsers = new JComboBox();
		
		JButton btnNewButton_2 = new JButton("Con");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					
					ipUsers.removeAllItems();
					Class.forName("org.postgresql.Driver");
					ipcon=DriverManager.getConnection("jdbc:postgresql://"+ipaddress.getText()+"/"+databases.getSelectedItem().toString(),"postgres","root");
					PreparedStatement ps=ipcon.prepareStatement("select distinct userid from tbprivileged where madmin='Y' and length(userid)=6 and userid!='VOL000'");
					ResultSet rs=ps.executeQuery();
					while(rs.next())
					{
						ipUsers.addItem(rs.getString(1));
					}
					ps.close();
					
				} catch (Exception e2) {
				
					JOptionPane.showMessageDialog(null, "Error:"+e2, "NLRMEI", JOptionPane.ERROR_MESSAGE);
					
				}	
				
			}
		});
		
		
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel.createSequentialGroup()
									.addComponent(lblNewLabel)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(ipaddress, GroupLayout.PREFERRED_SIZE, 121, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_panel.createSequentialGroup()
									.addComponent(lblDb, GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
										.addGroup(gl_panel.createSequentialGroup()
											.addGap(10)
											.addComponent(ipUsers, GroupLayout.PREFERRED_SIZE, 103, GroupLayout.PREFERRED_SIZE))
										.addComponent(databases, 0, 127, Short.MAX_VALUE))))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING, false)
								.addComponent(btnNewButton_2, 0, 0, Short.MAX_VALUE)
								.addComponent(btnNewButton, GroupLayout.DEFAULT_SIZE, 51, Short.MAX_VALUE))
							.addGap(22))
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(lblJid, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(jid, GroupLayout.PREFERRED_SIZE, 62, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(lblBook, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(book, GroupLayout.PREFERRED_SIZE, 62, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel)
						.addComponent(ipaddress, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnNewButton))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblDb)
						.addComponent(databases, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnNewButton_2, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(ipUsers, GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
					.addGap(21)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblJid)
						.addComponent(jid, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblBook)
						.addComponent(book, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		panel.setLayout(gl_panel);
		contentPane.setLayout(gl_contentPane);
	}
}
