package nlrmissues;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


public class DuplicacyCheck implements Runnable {

	public static float mPercentage;
	
	PreparedStatement ps,ps1;
	ResultSet rs;
	
    protected static Connection con;	
	
	protected String villageId,docid,ofcid;
	protected DocDuplicacyWriter docWriter;
	
	
	public void run() {
        
		try{
		
		int jid=0,bookno=0;	
		int count=0;
		Thread.currentThread().setName("VDO-"+villageId+"-"+docid+"-"+ofcid);
		String tname=Thread.currentThread().getName();
		List<CountInfo> countInfos=new ArrayList<CountInfo>();
		CountInfo countInfo;
		
		Map<Integer,Integer> map=new LinkedHashMap<Integer,Integer>();
		
		
	//	System.out.println("Thread:"+tname);
		
		ps=con.prepareStatement("select distinct jid,bookno,count(*) from tbimagetrans where villageid='"+villageId+"' and docid='"+docid+"' group by jid,bookno order by count(*) desc");
		rs=ps.executeQuery();
		while(rs.next())
		{
			
			countInfo=new CountInfo();
			countInfo.setJid(rs.getInt(1));
			countInfo.setBookno(rs.getInt(2));
			countInfo.setCount(rs.getInt(3));
			
			countInfos.add(countInfo);
			if(map.containsKey(rs.getInt(3)))
			{
				
				map.put(rs.getInt(3), map.get(rs.getInt(3))+1);
				
			}
			else{
				
				map.put(rs.getInt(3), 1);
			}
			
			
		}
		
		
		
		List<CountInfo> reqCountInfos;
		CountInfo reqCountInfo , reqCountInfo1;
		Iterator<CountInfo> itr;
		
	/*	Iterator itr1=map.entrySet().iterator();
		while(itr1.hasNext())
		{
			java.util.Map.Entry<Integer, Integer> entry=(java.util.Map.Entry<Integer, Integer>) itr1.next();
		    System.out.println(tname+"-"+"Entry:"+entry.getKey()+" "+entry.getValue());
		}
	*/	
		Iterator<CountInfo> itr2;
		
		String col="";
		String cols[];
		
		Map<PageInfo, TbDocTran> cMap1;
		Map<PageInfo, TbDocTran> cMap2;
		
		PageInfo pageInfo;
		TbDocTran tbDocTran;
		
		float matchPer;
		
		Iterator<Integer> iterator=map.keySet().iterator();
		while(iterator.hasNext())
		{
			Integer c=(Integer) iterator.next();
			if(map.get(c)>1)
			{
				reqCountInfos=new ArrayList<CountInfo>();
				itr=countInfos.iterator();
				while(itr.hasNext())
				{
					
					reqCountInfo=(CountInfo) itr.next();
					if(reqCountInfo.getCount()==c)
					{   reqCountInfos.add(reqCountInfo);
					}
				}
				
//				System.out.println(tname+"-"+"reqCountInfos:"+reqCountInfos);
				col="pageno, serialno";
				
				ps=con.prepareStatement("select cname from tbdoctrancolumns where ofcid='"+ofcid+"' and docid='"+docid+"' order by ofcid,docid,colid");
				rs=ps.executeQuery();
				while(rs.next())
				{
					
					col=col+","+rs.getString(1);
					
				}
				
//				System.out.println("Col:"+col);
				
				cols=col.replace("pageno, serialno,", "").split(",");
				
				itr2=reqCountInfos.iterator();
				while(itr2.hasNext())
				{
					cMap1=new LinkedHashMap<PageInfo, TbDocTran>();
					reqCountInfo=(CountInfo)itr2.next();
					ps=con.prepareStatement("select "+col+" from tbtransactions where jid='"+reqCountInfo.getJid()+"' and bookno='"+reqCountInfo.getBookno()+"' order by pageno,serialno");
					rs=ps.executeQuery();
					while(rs.next())
					{
						pageInfo=new PageInfo(rs.getInt(1),rs.getInt(2));
						
						tbDocTran=new TbDocTran();
						
						for(String cl:cols)
						{
							tbDocTran.getClass().getDeclaredField(cl).set(tbDocTran, rs.getString(cl));
						}
						
						cMap1.put(pageInfo, tbDocTran);
						
					//	System.out.println(tname+"-"+tbDocTran);
					}
					
					for(int i=1;i<reqCountInfos.size();i++)
					{
						
						cMap2=new LinkedHashMap<PageInfo, TbDocTran>();
						reqCountInfo1=reqCountInfos.get(i);
						ps=con.prepareStatement("select "+col+" from tbtransactions where jid='"+reqCountInfo1.getJid()+"' and bookno='"+reqCountInfo1.getBookno()+"' order by pageno,serialno");
						rs=ps.executeQuery();
						while(rs.next())
						{
							pageInfo=new PageInfo(rs.getInt(1),rs.getInt(2));
							
							tbDocTran=new TbDocTran();
							
							for(String cl:cols)
							{
								tbDocTran.getClass().getDeclaredField(cl).set(tbDocTran, rs.getString(cl));
							}
							
							cMap2.put(pageInfo, tbDocTran);
							
						//	System.out.println(tname+"-"+tbDocTran);
						}
						
//						System.out.println(tname+"-"+"cMap1.size:"+cMap1.size()+" cMap2.size:"+cMap2.size());
						
						matchPer=0;
						
						if(cMap1.size()>=cMap2.size())
						{
							matchPer=matchPercentage(cMap1,cMap2);
						}
						else{
							matchPer=matchPercentage(cMap2,cMap1);
						}
						
						if(matchPer>=mPercentage)
						{
							
							ps=con.prepareStatement("update tbtransactions set status='D' where jid='"+reqCountInfo1.getJid()+"' and bookno='"+reqCountInfo1.getCount()+"'");
							ps.execute();
							ps=con.prepareStatement("update tbimagetrans set status='D' where jid='"+reqCountInfo1.getJid()+"' and bookno='"+reqCountInfo1.getCount()+"'");
							ps.execute();
							docWriter.write(villageId, docid, reqCountInfo1.getJid(), reqCountInfo1.getBookno(), reqCountInfo1.getCount(), "D", matchPer, "Matched with Jid="+reqCountInfo.getJid()+" and Bookno="+reqCountInfo.getBookno());
							
						}
						
						
						
					}
					
					itr2.remove();
				}
				
			}
		}
			
		
		}catch(Exception e)
		{
			
			System.out.println("DuplicacyCheck Error:"+e);
		}

	}
	
	
	public float matchPercentage(Map<PageInfo,TbDocTran> cMap1,Map<PageInfo,TbDocTran> cMap2)
	{
		float per=0;
		int match=0;
		int total=cMap1.size();
		TbDocTran t1,t2;
		Iterator<Entry<PageInfo, TbDocTran>> iterator=cMap1.entrySet().iterator();
		while(iterator.hasNext())
		{
			Entry<PageInfo, TbDocTran> entry=iterator.next();
			t1=entry.getValue();
			t2=cMap2.get(entry.getKey()); 
			
			if(t1.equals(t2))
			{
				match++;
			}
		}
		
		per=100*((float)match/(float)total);
		System.out.println(Thread.currentThread().getName()+"- Match:"+match+" out of Total "+total+" with per "+per);
		
		return per;
	}
	

}
