package nlrmissues;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class DocDuplicacyWriter {

	ReentrantLock lock=new ReentrantLock();
	
	XSSFWorkbook wb;
	XSSFSheet sheet;
	Row row;
	Cell cell;
	int r=1;
	
	public DocDuplicacyWriter()
	{
		 wb=new XSSFWorkbook();
		 sheet=wb.createSheet("VillageId-DocidDup");
		 row=sheet.createRow(0);
		 cell=row.createCell(0);
		 cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell.setCellValue("VillageId");
		 cell=row.createCell(1);
		 cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell.setCellValue("DocId");
		 cell=row.createCell(2);
		 cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell.setCellValue("Jid");
		 cell=row.createCell(3);
		 cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell.setCellValue("Bookno");
		 cell=row.createCell(4);
		 cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell.setCellValue("Count");
		 cell=row.createCell(5);
		 cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell.setCellValue("Status");
		 cell=row.createCell(6);
		 cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell.setCellValue("Match Percentage");
		 cell=row.createCell(7);
		 cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell.setCellValue("Remarks");
	}
	
	public void write(String villageid,String docid,int jid,int bookno,int count,String status,float per,String remarks)
	{
		 lock.lock();
		 row=sheet.createRow(r);
		 cell=row.createCell(0);
		 cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell.setCellValue(villageid);
		 cell=row.createCell(1);
		 cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell.setCellValue(docid);
		 cell=row.createCell(2);
		 cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell.setCellValue(jid);
		 cell=row.createCell(3);
		 cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell.setCellValue(bookno);
		 cell=row.createCell(4);
		 cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell.setCellValue(count);
		 cell=row.createCell(5);
		 cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell.setCellValue(status);
		 cell=row.createCell(6);
		 cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell.setCellValue(per);
		 cell=row.createCell(7);
		 cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell.setCellValue(remarks);
		 r++;
		 lock.unlock();
		
		
	}
	
	public void writeToDisk(FileOutputStream output) throws IOException
	{
		
		wb.write(output);
	}
	
}
