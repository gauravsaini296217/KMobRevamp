package nlrmissues;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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
import javax.swing.UIManager;
import javax.swing.border.LineBorder;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

@Component
public class ConnectionFrame extends JFrame {

	private JPanel contentPane;
	private JTextField ipaddress;
    private JComboBox databases;
	private Connection con;
	private JTextField threads;
    private PreparedStatement ps, ps1;
    private ResultSet rs;
    private JTextField mper;
	
    
	public ConnectionFrame() {
		
		System.out.println("Inside ConnectionFrame");
		
		setTitle("NLRM Issues");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBackground(UIManager.getColor("CheckBox.background"));
		contentPane.setBorder(new LineBorder(new Color(0, 0, 0)));
		setContentPane(contentPane);
		
		ipaddress = new JTextField();
		ipaddress.setColumns(10);
		
		JLabel lblIpAddress = new JLabel("IP Address");
		lblIpAddress.setFont(new Font("Arial", Font.BOLD, 12));
		
		databases = new JComboBox();
		
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
		btnNewButton.setFont(new Font("Arial", Font.BOLD, 12));
		
		JLabel lblDatabases = new JLabel("Databases");
		lblDatabases.setFont(new Font("Arial", Font.BOLD, 12));
		
		
		
		JButton btnConn = new JButton("Conn");
		btnConn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			
				try {
					
					Class.forName("org.postgresql.Driver");
					con=DriverManager.getConnection("jdbc:postgresql://"+ipaddress.getText()+"/"+databases.getSelectedItem(),"postgres","root");
					System.out.println("Connected");
					
				} catch (Exception e2) {
				
					JOptionPane.showMessageDialog(null, "Error:"+e2, "NLRMEI", JOptionPane.ERROR_MESSAGE);
					
				}
				
			
				
				
			}
		});
		btnConn.setFont(new Font("Arial", Font.BOLD, 12));
		
		threads = new JTextField();
		threads.setColumns(10);
		
		JLabel lblNoOfThreads = new JLabel("No of Threads");
		lblNoOfThreads.setFont(new Font("Arial", Font.BOLD, 12));
		
		JButton btnCheckDuplicacy = new JButton("Village & Doc Dup");
		btnCheckDuplicacy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			
				try{
				if(con==null || threads.getText().equalsIgnoreCase("") || mper.getText().equalsIgnoreCase(""))
				{
					
					JOptionPane.showMessageDialog(null,"1) Connection must be Established.\n 2) No of Threads & Match Percentage are mandatory Fields", "NLRM Issues", JOptionPane.ERROR_MESSAGE);
					
				}
				else
				{
				List<DuplicacyCheck> jobs=new ArrayList<DuplicacyCheck>();	
				DuplicacyCheck dCheck;	
				
				DuplicacyCheck.con=con;
				DuplicacyCheck.mPercentage=Float.valueOf(mper.getText());
				DocDuplicacyWriter docWriter=new DocDuplicacyWriter();
				
				
				ps=con.prepareStatement("select distinct villageid,docid,ofcid from tbimagetrans where villageid='6739' order by villageid,docid,ofcid");
				rs=ps.executeQuery();
				while(rs.next())
				{
			    dCheck=new DuplicacyCheck();
			    dCheck.villageId=rs.getString(1);
			    dCheck.docid=rs.getString(2);
			    dCheck.ofcid=rs.getString(3);
				dCheck.docWriter=docWriter;
			    
			    jobs.add(dCheck);
			    
				}
				
				System.out.println("Jobs Size:"+jobs.size());
					
				ExecutorService service=Executors.newFixedThreadPool(Integer.parseInt(threads.getText()));
				for(DuplicacyCheck job:jobs)
				{
					
					service.submit(job);
					
				}
				
				service.shutdown();
				while (!service.awaitTermination(24L, TimeUnit.HOURS)) {
				    System.out.println("Not yet. Still waiting for termination");
				}
				
				File file=new File("VillageId-DocIdDup.xlsx");
				FileOutputStream output=new FileOutputStream(file);
				docWriter.writeToDisk(output);
				
				JOptionPane.showMessageDialog(null,"Completed| File Generated At "+file.getAbsolutePath(), "NLRM Issues", JOptionPane.INFORMATION_MESSAGE);
				
				}
				
				}catch(Exception e2)
				{
					JOptionPane.showMessageDialog(null, "Error:"+e2, "NLRM Issues", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnCheckDuplicacy.setFont(new Font("Arial", Font.BOLD, 12));
		
		JButton btnBookDup = new JButton("Book Dup");
		btnBookDup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			
				try{
					
					XSSFWorkbook workbook=new XSSFWorkbook();
					XSSFSheet sheet=workbook.createSheet("Book Dup");
					
					Row row=sheet.createRow(0);
					Cell cell=row.createCell(0);
					cell.setCellValue("jid");
					
					cell=row.createCell(1);
					cell.setCellValue("bookno");
					
					cell=row.createCell(2);
					cell.setCellValue("imgorgname");
					
					cell=row.createCell(3);
					cell.setCellValue("pageno");
					
					cell=row.createCell(4);
					cell.setCellValue("filename");
					
					cell=row.createCell(5);
					cell.setCellValue("status");
					
					
					int r=1;
					
					String jid="",bookno="",imgorgname="";
					
					ps=con.prepareStatement("select jid,bookno, imgorgname,pageno, filename from tbimagetrans where concat(jid,',',bookno,',',imgorgname) in ( select concat(jid,',',bookno,',',imgorgname) from tbimagetrans group by jid,bookno,imgorgname having count(*)>1 ) order by jid ,bookno,imgorgname, pageno, filename");
                    rs=ps.executeQuery();
                    while(rs.next())
                    {
                    	if(jid.equalsIgnoreCase("") && bookno.equalsIgnoreCase("") && imgorgname.equalsIgnoreCase(""))
                    	{
                    		ps1=con.prepareStatement("update tbimagetrans set status='F' where jid='"+rs.getString(1)+"' and bookno='"+rs.getString(2)+"' and imgorgname='"+rs.getString(3)+"' and pageno='"+rs.getString(4)+"' and filename='"+rs.getString(5)+"'");
                			ps1.execute();
                    		
                    		System.out.println("jid:"+rs.getString(1)+", bookno:"+rs.getString(2)+", imgorgname:"+rs.getString(3));
                			
                			row=sheet.createRow(r);
        					cell=row.createCell(0);
        					cell.setCellValue(rs.getString(1));
        					
        					cell=row.createCell(1);
        					cell.setCellValue(rs.getString(2));
        					
        					cell=row.createCell(2);
        					cell.setCellValue(rs.getString(3));
        					
        					cell=row.createCell(3);
        					cell.setCellValue(rs.getString(4));
        					
        					cell=row.createCell(4);
        					cell.setCellValue(rs.getString(5));
        					
        					cell=row.createCell(5);
        					cell.setCellValue("F");
                			
        					r++;
                    		
                    		jid=rs.getString(1);
                    		bookno=rs.getString(2);
                    		imgorgname=rs.getString(3);
                    		
                    		
                    	}else{
                    		
                    		
                    		
                    		if(jid.equalsIgnoreCase(rs.getString(1)) && bookno.equalsIgnoreCase(rs.getString(2)) && imgorgname.equalsIgnoreCase(rs.getString(3)))
                    		{
                    			
                    			ps1=con.prepareStatement("update tbimagetrans set status='D' where jid='"+rs.getString(1)+"' and bookno='"+rs.getString(2)+"' and imgorgname='"+rs.getString(3)+"' and pageno='"+rs.getString(4)+"' and filename='"+rs.getString(5)+"'");
                    			ps1.execute();
                    			
                    			System.out.println("jid:"+rs.getString(1)+", bookno:"+rs.getString(2)+", imgorgname:"+rs.getString(3));
                    			
                    			row=sheet.createRow(r);
            					cell=row.createCell(0);
            					cell.setCellValue(rs.getString(1));
            					
            					cell=row.createCell(1);
            					cell.setCellValue(rs.getString(2));
            					
            					cell=row.createCell(2);
            					cell.setCellValue(rs.getString(3));
            					
            					cell=row.createCell(3);
            					cell.setCellValue(rs.getString(4));
            					
            					cell=row.createCell(4);
            					cell.setCellValue(rs.getString(5));
            					
            					cell=row.createCell(5);
            					cell.setCellValue("D");
                    			
            					r++;
            					
            					jid=rs.getString(1);
            					bookno=rs.getString(2);
            					imgorgname=rs.getString(3);
                    		}
                    		else{
                    			
                    			ps1=con.prepareStatement("update tbimagetrans set status='F' where jid='"+rs.getString(1)+"' and bookno='"+rs.getString(2)+"' and imgorgname='"+rs.getString(3)+"' and pageno='"+rs.getString(4)+"' and filename='"+rs.getString(5)+"'");
                    			ps1.execute();
                    			
                    			System.out.println("jid:"+rs.getString(1)+", bookno:"+rs.getString(2)+", imgorgname:"+rs.getString(3));
                    			
                    			row=sheet.createRow(r);
            					cell=row.createCell(0);
            					cell.setCellValue(rs.getString(1));
            					
            					cell=row.createCell(1);
            					cell.setCellValue(rs.getString(2));
            					
            					cell=row.createCell(2);
            					cell.setCellValue(rs.getString(3));
            					
            					cell=row.createCell(3);
            					cell.setCellValue(rs.getString(4));
            					
            					cell=row.createCell(4);
            					cell.setCellValue(rs.getString(5));
            					
            					cell=row.createCell(5);
            					cell.setCellValue("F");
                    			
            					r++;
            					
            					jid=rs.getString(1);
            					bookno=rs.getString(2);
            					imgorgname=rs.getString(3);
                    			
                    		}
                    		
                    	}
                    		
                    	
                    }
                    
                    File file=new File("Book Duplicacy.xlsx");
                    FileOutputStream output=new FileOutputStream(file);
					workbook.write(output);
                    
					output.close();
					workbook.close();
					
					JOptionPane.showMessageDialog(null, "Result File Generated At "+file.getAbsolutePath(), "NLRM Issues", JOptionPane.INFORMATION_MESSAGE);
					
				}catch(Exception e2)
				{
					
					JOptionPane.showMessageDialog(null, "Error:"+e2, "NLRM Issues", JOptionPane.ERROR_MESSAGE);
				}
				
			}
		});
		btnBookDup.setFont(new Font("Arial", Font.BOLD, 12));
		
		JLabel lblMatch = new JLabel("Match %");
		lblMatch.setFont(new Font("Arial", Font.BOLD, 12));
		
		mper = new JTextField();
		mper.setColumns(10);
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addComponent(btnBookDup, GroupLayout.PREFERRED_SIZE, 95, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(btnCheckDuplicacy))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap(85, Short.MAX_VALUE)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(lblIpAddress, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(ipaddress, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(lblDatabases, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(databases, 0, 120, Short.MAX_VALUE))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(lblNoOfThreads, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(threads, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(lblMatch, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
									.addGap(10)
									.addComponent(mper, GroupLayout.PREFERRED_SIZE, 86, GroupLayout.PREFERRED_SIZE)))))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(btnNewButton)
						.addComponent(btnConn))
					.addGap(62))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(31)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(ipaddress, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblIpAddress)
						.addComponent(btnNewButton))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblDatabases, GroupLayout.PREFERRED_SIZE, 15, GroupLayout.PREFERRED_SIZE)
						.addComponent(databases, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnConn))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(threads, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNoOfThreads, GroupLayout.PREFERRED_SIZE, 15, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(2)
							.addComponent(lblMatch, GroupLayout.PREFERRED_SIZE, 15, GroupLayout.PREFERRED_SIZE))
						.addComponent(mper, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnCheckDuplicacy, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnBookDup, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(46, Short.MAX_VALUE))
		);
		contentPane.setLayout(gl_contentPane);
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ConnectionFrame frame = new ConnectionFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
