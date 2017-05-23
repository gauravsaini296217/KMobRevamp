package nlrmissues;

public class PageInfo {

	int pageno, serialno;

	public PageInfo(int pageno,int serialno)
	{
		this.pageno=pageno;
		this.serialno=serialno;
	}
	
	public int getPageno() {
		return pageno;
	}

	public void setPageno(int pageno) {
		this.pageno = pageno;
	}

	public int getSerialno() {
		return serialno;
	}

	public void setSerialno(int serialno) {
		this.serialno = serialno;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + pageno;
		result = prime * result + serialno;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PageInfo other = (PageInfo) obj;
		if (pageno != other.pageno)
			return false;
		if (serialno != other.serialno)
			return false;
		return true;
	}
	
	
	
}
