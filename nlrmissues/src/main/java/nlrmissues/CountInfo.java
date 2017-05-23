package nlrmissues;

public class CountInfo {

	int jid,bookno,count;

	public int getJid() {
		return jid;
	}

	public void setJid(int jid) {
		this.jid = jid;
	}

	public int getBookno() {
		return bookno;
	}

	public void setBookno(int bookno) {
		this.bookno = bookno;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return "CountInfo [jid=" + jid + ", bookno=" + bookno + ", count=" + count + "]";
	}
	
	
}
