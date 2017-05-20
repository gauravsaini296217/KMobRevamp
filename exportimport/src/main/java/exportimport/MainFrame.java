package exportimport;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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

import model.ExportBookDetails;

public class MainFrame extends JFrame {

	private JPanel contentPane;
	private JTextField ipaddress;
	private JTextField jid;
	private JTextField book;
	private JTextField m_ipaddress;
	private JComboBox<String> databases, m_databases;
	private Connection ipcon, metacon;
	private JComboBox<String> ipUsers, metaUsers;
	PreparedStatement ps, ps1;
	ExportBookDetails ebDetails;
	String newjid, newcid;
	
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
			     
			     ps1=ipcon.prepareStatement("select count(distinct filename) from tbimagetrans where vqcby is not null and dqcby is not null and status='F' and jid='"+rsp2.getInt(6)+"' and bookno='"+rsp2.getInt(7)+"'");
			     ResultSet rs=ps1.executeQuery();
			     if(rs.next())
			     {
			     System.out.println(rs.getString(1));	 
			     if(rs.getInt(1)==rsp2.getInt(8))
			     {
			     ebDetails=new ExportBookDetails();    
			     ebDetails.setTalukaid(rsp2.getInt(1));   
			     ebDetails.setVillageid(rsp2.getInt(2));
			     ebDetails.setEtitle(rsp2.getString(3));
			     ebDetails.setDocid(rsp2.getInt(4));
			     ebDetails.setEdesc(rsp2.getString(5));
			     ebDetails.setJid(rsp2.getInt(6));
			     ebDetails.setBookno(rsp2.getInt(7));
			     ebDetails.setActualpages(rsp2.getInt(8));
			     ebDetails.setCid(rsp2.getInt(9));
			     Export(ebDetails);
			     
			     }
			     }
			     
			     JOptionPane.showMessageDialog(null,"Successfully Imported", "NLRMEI", JOptionPane.INFORMATION_MESSAGE);
			     
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
	
	public void Export(ExportBookDetails ebDetails)
	 { try{
		 System.out.println(ebDetails.toString());
	     String stmt;
	   //  filenames=new ArrayList();
	     DateFormat dateFormat=new SimpleDateFormat("ddMMyyyy");
	     DateFormat dateFormat1=new SimpleDateFormat("yyyy-MM-dd");
	     FileWriter fileWriter;
	     BufferedWriter bw;
	     FileReader fr;
	     BufferedReader br;
	     String filename;
	     int count=0,c1=0,c2=0,c3=0,c4=0,imgcount=0;
	     String line="";
	     int seqexp,serialno;
	    // System.out.println("List size:"+ebDetailsList.size());
	                
	        c1=0;c2=0;c3=0;c4=0;    
	        ps=metacon.prepareStatement("select * from tbjacketm where refjid='"+ebDetails.getJid()+"'");
	        ResultSet rs=ps.executeQuery();
	        if(rs.next())
	        {
	            c1=1;
	        }    
	        ps=metacon.prepareStatement("select * from tbjacketd where refjid='"+ebDetails.getJid()+"' and bookno='"+ebDetails.getBookno()+"'");
	        rs=ps.executeQuery();
	        if(rs.next())
	        {
	            c2=1;
	        }
	        ps=metacon.prepareStatement("select * from tbimgworkassign where refjid='"+ebDetails.getJid()+"' and bookno='"+ebDetails.getBookno()+"'");
	        rs=ps.executeQuery();
	        if(rs.next())
	        {
	            c3=1;
	        }
	        ps=metacon.prepareStatement("select count(distinct filename) from tbimagetrans where refjid='"+ebDetails.getJid()+"' and bookno='"+ebDetails.getBookno()+"'");
	        rs=ps.executeQuery();
	        if(rs.next())
	        {
	            if(rs.getInt(1)==ebDetails.getActualpages())
	            {
	            c4=1;
	            }
	            
	        }
	        
	        if(c2==0 && c3==0 && c4==0)
	        {
	        System.out.println("For Export:"+ebDetails.toString());    
	        filename="JW"+dateFormat.format(new Date())+formattext(String.valueOf(ebDetails.getBookno()))+String.valueOf(ebDetails.getJid())+"."+formattext(String.valueOf(ebDetails.getCid()));    
	        fileWriter=new FileWriter(filename);    
	        bw=new BufferedWriter(fileWriter);
	        stmt="S|"+ebDetails.getCid()+"|"+ebDetails.getJid()+"|"+ebDetails.getBookno()+"|"+ebDetails.getTalukaid();
	        if(hash(stmt).length()==32)
	        {
	        stmt=hash(stmt)+"|"+stmt;
	        }
	        else if(hash(stmt).length()==31)
	        {
	        stmt="0"+hash(stmt)+"|"+stmt;
	        }
	        else if(hash(stmt).length()==30)
	        {
	        stmt="00"+hash(stmt)+"|"+stmt;
	        }
	        else if(hash(stmt).length()==29)
	        {
	        stmt="000"+hash(stmt)+"|"+stmt;
	        }
	        else if(hash(stmt).length()==28)
	        {
	        stmt="0000"+hash(stmt)+"|"+stmt;
	        }
	        System.out.println(stmt);
	        bw.write(stmt);
	        bw.newLine();
	        stmt="D|insert into tbjacketm(jid,cid, ofcid, docid, books, issuedby, recvby, recvdt, status, stateid, districtid, talukaid, villageid,wvillageid,createdby, datecreated,refjid,refcid) values (NEWJID,NEWCID,";
	        ps=ipcon.prepareStatement("select ofcid, docid, books, issuedby, recvby, recvdt, status, stateid, districtid, talukaid, villageid,wvillageid,createdby, datecreated,jid,cid from tbjacketm where jid='"+ebDetails.getJid()+"' and cid='"+ebDetails.getCid()+"'");
	        rs=ps.executeQuery();
	        if(rs.next())
	        {
	            stmt=stmt+rs.getString(1)+","+rs.getString(2)+","+rs.getString(3)+",$"+rs.getString(4)+"$,$"+rs.getString(5)+"$,$"+rs.getString(6)+"$,$"+rs.getString(7)+"$,"+rs.getString(8)+","+rs.getString(9)+","+rs.getString(10)+","+rs.getString(11)+","+rs.getString(12)+",$"+rs.getString(13)+"$,$"+rs.getString(14)+"$,"+rs.getString(15)+","+rs.getString(16)+");";
	        }
	        if(hash(stmt).length()==32)
	        {
	        stmt=hash(stmt)+"|"+stmt;
	        }
	        else if(hash(stmt).length()==31)
	        {
	        stmt="0"+hash(stmt)+"|"+stmt;
	        }
	        else if(hash(stmt).length()==30)
	        {
	        stmt="00"+hash(stmt)+"|"+stmt;
	        }
	        else if(hash(stmt).length()==29)
	        {
	        stmt="000"+hash(stmt)+"|"+stmt;
	        }
	        else if(hash(stmt).length()==28)
	        {
	        stmt="0000"+hash(stmt)+"|"+stmt;
	        }
	        System.out.println(stmt);
	        bw.write(stmt);
	        bw.newLine();
	        ps=ipcon.prepareStatement("update tbjacketd set readyby='"+ipUsers.getSelectedItem()+"' , readydate='"+dateFormat1.format(new Date())+"' , expby='"+ipUsers.getSelectedItem()+"' , expdate='"+dateFormat1.format(new Date())+"' where jid='"+ebDetails.getJid()+"' and bookno='"+ebDetails.getBookno()+"'");
	        ps.execute();
	        stmt="D|insert into tbjacketd(jid, cid, bookno, deptpages, actualpages, remark, room, rac, shelf, rumaal,refjid,refcid, readyby, readydate,expby,expdate,pendingpages,status) values (NEWJID,NEWCID,";
	        ps=ipcon.prepareStatement("select bookno, deptpages, actualpages, quote_nullable(remark), coalesce(room,0), coalesce(rac,0), coalesce(shelf,0), coalesce(rumaal,0),jid,cid, readyby, readydate,expby,expdate,pendingpages,status from tbjacketd where jid='"+ebDetails.getJid()+"' and bookno='"+ebDetails.getBookno()+"'");
	        rs=ps.executeQuery();
	        if(rs.next())
	        {
	            
	            stmt=stmt+rs.getString(1)+","+rs.getString(2)+","+rs.getString(3)+","+rs.getString(4)+",$"+rs.getString(5)+"$,$"+rs.getString(6)+"$,$"+rs.getString(7)+"$,$"+rs.getString(8)+"$,"+rs.getString(9)+","+rs.getString(10)+",$"+rs.getString(11)+"$,$"+rs.getString(12)+"$,$"+rs.getString(13)+"$,$"+rs.getString(14)+"$,$"+rs.getString(15)+"$,$"+rs.getString(16)+"$);";    
	            
	            
	            
	        }
	        if(hash(stmt).length()==32)
	        {
	        stmt=hash(stmt)+"|"+stmt;
	        }
	        else if(hash(stmt).length()==31)
	        {
	        stmt="0"+hash(stmt)+"|"+stmt;
	        }
	        else if(hash(stmt).length()==30)
	        {
	        stmt="00"+hash(stmt)+"|"+stmt;
	        }
	        else if(hash(stmt).length()==29)
	        {
	        stmt="000"+hash(stmt)+"|"+stmt;
	        }
	        else if(hash(stmt).length()==28)
	        {
	        stmt="0000"+hash(stmt)+"|"+stmt;
	        }
	        System.out.println(stmt);
	        bw.write(stmt);
	        bw.newLine();
	        stmt="D|insert into tbimgworkassign (jid,cid, bookno, assignto, assigndate, assigntarget, pendings,bkstatus,refjid,refcid) values (NEWJID,NEWCID,";
	        ps=ipcon.prepareStatement("select bookno, assignto, assigndate, assigntarget, pendings,bkstatus,jid,cid from tbimgworkassign where jid='"+ebDetails.getJid()+"' and bookno='"+ebDetails.getBookno()+"'");
	        rs=ps.executeQuery();
	        if(rs.next())
	        {
	            stmt=stmt+rs.getString(1)+",$"+rs.getString(2)+"$,$"+rs.getString(3)+"$,"+rs.getString(4)+","+rs.getString(5)+",$"+rs.getString(6)+"$,"+rs.getString(7)+","+rs.getString(8)+");";
	        }
	        if(hash(stmt).length()==32)
	        {
	        stmt=hash(stmt)+"|"+stmt;
	        }
	        else if(hash(stmt).length()==31)
	        {
	        stmt="0"+hash(stmt)+"|"+stmt;
	        }
	        else if(hash(stmt).length()==30)
	        {
	        stmt="00"+hash(stmt)+"|"+stmt;
	        }
	        else if(hash(stmt).length()==29)
	        {
	        stmt="000"+hash(stmt)+"|"+stmt;
	        }
	        else if(hash(stmt).length()==28)
	        {
	        stmt="0000"+hash(stmt)+"|"+stmt;
	        }
	        System.out.println(stmt);
	        bw.write(stmt);
	        bw.newLine();
	        count=0;
	        imgcount=0;
	        ps=ipcon.prepareStatement("select imgid,ofcid, docid, villageid, createdby, datecreated, filename,replace(repository,E'\\\\','#'), fileencrypt, vqcby, vqcdate, dqcby, dqcdate,status, bookno, pageno, imgorgname,replace(replace(replace(imgorgrepository,E'\\\\','#'),'$',''),'''',''), jid, cid from tbimagetrans where jid='"+ebDetails.getJid()+"' and bookno='"+ebDetails.getBookno()+"'");
	        rs=ps.executeQuery();
	        while(rs.next())
	        {
	            String imgid="";
	            PreparedStatement pspy=metacon.prepareStatement("SELECT nextval('seqimgid')");
	            ResultSet rspy=pspy.executeQuery();
	            while(rspy.next()){
	            imgid=rspy.getString(1);
	            } 
	           
	            stmt="D|insert into tbimagetrans(jid, cid,imgid,ofcid, docid, villageid, createdby, datecreated, filename,repository, fileencrypt, vqcby, vqcdate, dqcby, dqcdate,status, bookno, pageno, imgorgname,imgorgrepository, refjid, refcid) values (NEWJID,NEWCID,";
	            stmt=stmt+imgid+","+rs.getString(2)+","+rs.getString(3)+","+rs.getString(4)+",$"+rs.getString(5)+"$,$"+rs.getString(6)+"$,$"+rs.getString(7)+"$,$"+rs.getString(8)+"$,$"+rs.getString(9)+"$,$"+rs.getString(10)+"$,$"+rs.getString(11)+"$,$"+rs.getString(12)+"$,$"+rs.getString(13)+"$,$"+rs.getString(14)+"$,"+rs.getString(15)+","+rs.getString(16)+",$"+rs.getString(17)+"$,$"+rs.getString(18) +"$,"+rs.getString(19)+","+rs.getString(20)+");";
	            if(hash(stmt).length()==32)
	        {
	        stmt=hash(stmt)+"|"+stmt;
	        }
	        else if(hash(stmt).length()==31)
	        {
	        stmt="0"+hash(stmt)+"|"+stmt;
	        }
	        else if(hash(stmt).length()==30)
	        {
	        stmt="00"+hash(stmt)+"|"+stmt;
	        }
	        else if(hash(stmt).length()==29)
	        {
	        stmt="000"+hash(stmt)+"|"+stmt;
	        }
	        else if(hash(stmt).length()==28)
	        {
	        stmt="0000"+hash(stmt)+"|"+stmt;
	        }
	            System.out.println(stmt);
	            bw.write(stmt);
	            bw.newLine();
	            count++;
	        }
	        imgcount=count;
	        count=count+3;
	        stmt="E|"+String.valueOf(count);
	        stmt=hash(stmt)+"|"+stmt;
	        System.out.println(stmt);
	        bw.write(stmt);
	        bw.close();
	     //   filenames.add(filename);
	        ps=ipcon.prepareStatement("update tbjacketd set readyby='"+ipUsers.getSelectedItem()+"' , readydate='"+dateFormat1.format(new Date())+"' , expby='"+ipUsers.getSelectedItem()+"' , expdate='"+dateFormat1.format(new Date())+"' where jid='"+ebDetails.getJid()+"' and bookno='"+ebDetails.getBookno()+"'");
	        ps.execute();
	        fr=new FileReader(filename);
	        br=new BufferedReader(fr);
	        line="";
	        seqexp=0;
	        PreparedStatement pspy=ipcon.prepareStatement("SELECT nextval('seqexp')");
	        ResultSet rspy=pspy.executeQuery();
	        while(rspy.next()){
	        seqexp=Integer.parseInt(rspy.getString(1));
	        }
	        serialno=1;
	        while((line=br.readLine())!=null)
	        {
	        String bkmd5=line.split("\\|")[0];
	        line=line.replace(bkmd5+"|", "");
	        line=line.replace("'", "$");
	        System.out.println(seqexp+","+bkmd5+","+line+","+serialno);
	        ps=ipcon.prepareStatement("insert into tbexportbook values('"+seqexp+"','"+bkmd5+"','"+line+"','"+serialno+"')");
	        ps.execute();
	        serialno++;
	        }
	        PreparedStatement pscheck=metacon.prepareStatement("select * from tbloaddata where jid='"+ebDetails.getJid()+"' and cid='"+ebDetails.getCid()+"' and bookno='"+ebDetails.getBookno()+"'");
	        ResultSet rscheck=pscheck.executeQuery();
	        while(rscheck.next()){
	        ps=metacon.prepareStatement("delete from tbloaddata where jid='"+ebDetails.getJid()+"' and cid='"+ebDetails.getCid()+"' and bookno='"+ebDetails.getBookno()+"'");
	        ps.execute();
	        }
	            
	        ps=metacon.prepareStatement("insert into tbloaddata values('"+ebDetails.getJid()+"','"+ebDetails.getCid()+"','"+ebDetails.getBookno()+"','"+ebDetails.getTalukaid()+"','"+filename+".zip"+"','"+count+"','"+imgcount+"','"+dateFormat1.format(new Date())+"','"+metaUsers.getSelectedItem()+"')");
	        ps.execute();
	        
	        br.close();
	        Importdb(filename);
	        }
	        
	        
	        
	        
	 }catch(Exception exp)
	 {
	 JOptionPane.showMessageDialog(null, "Export Error:"+exp, "NLRM Export Import",JOptionPane.ERROR_MESSAGE);
	 }
	 }
	 
	 public void Importdb(String filename)
	{
	FileReader fr;
	BufferedReader br;
	String line="";
	try{    

	System.out.println("Import Filename:"+filename);    
	fr=new FileReader(filename);    
	br=new BufferedReader(fr);
	line="";
	int already=0;
	while((line=br.readLine())!=null)
	        {  
	System.out.println("RLine:"+line);            
	if(line.split("\\|")[1].equalsIgnoreCase("S"))
	{
	String cid=line.split("\\|")[2];    
	String jid=line.split("\\|")[3];        
	String bookno=line.split("\\|")[4];
	String talukaid=line.split("\\|")[5];
	ps=metacon.prepareStatement("select distinct jid from tbjacketm where refjid='"+jid+"' and refcid='"+cid+"'");
	ResultSet rs=ps.executeQuery();
	if(rs.next())
	{
	newjid=rs.getString(1);
	already=1;
	}
	else{
	PreparedStatement pspy=metacon.prepareStatement("SELECT nextval('seqjktid')");
	ResultSet rspy=pspy.executeQuery();
	while(rspy.next()){
	newjid=rspy.getString(1);
	already=0;
	}
	    
	}
	ps=metacon.prepareStatement("select distinct cid from tbsysparams");
	rs=ps.executeQuery();
	if(rs.next())
	{
	newcid=rs.getString(1);    
	}
	}
	else if(line.split("\\|")[1].equalsIgnoreCase("D"))
	{
	System.out.println("Jid:"+newjid+", Cid:"+newcid);    
	line=line.split("\\|")[2];
	if(line.contains("insert into tbjacketm"))
	{
	if(already==0)
	{
	line=line.replace("NEWJID", newjid).replace("NEWCID", newcid).replaceAll("\\$", "'").replaceAll("##", "\\\\\\\\").replaceAll("#", "\\\\\\\\");
	System.out.println("Query:"+line);
	ps=metacon.prepareStatement(line);
	ps.execute();
	}
	}
	else{
	line=line.replace("NEWJID", newjid).replace("NEWCID", newcid).replaceAll("\\$", "'").replaceAll("##", "\\\\\\\\").replaceAll("#", "\\\\\\\\");
	System.out.println("Query:"+line);
	ps=metacon.prepareStatement(line);
	ps.execute();
	}
	}

	}

	}catch(Exception e)
	{
	System.out.println("Error:"+e);
	}     
	 }
	 
	 
	 public String formattext(String input)
	 {
	   String output=input;
	   if(input.length()==1)
	   {
	   output="00"+input;    
	   return output;
	   }
	   else if(input.length()==2)
	   {
	   output="0"+input;    
	   return output;
	   }
	   return output;
	 }
	 public String hash(String input)
	 {
	 try{    
	         byte ptext[] = input.getBytes();
	         String value = new String(ptext, "UTF-8");
	         MessageDigest m = MessageDigest.getInstance("MD5");
	         byte[] digest = m.digest(value.getBytes());
	         String hash = new BigInteger(1, digest).toString(16);
	         return hash;
	 }catch(Exception e)
	 {
	 JOptionPane.showMessageDialog(null, "Hashgen Error:"+e, "NLRM Export Import",JOptionPane.ERROR_MESSAGE);
	 return null;
	 }
	 }
	
	
}
