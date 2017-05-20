package model;

public class ExportBookDetails {

	private int talukaid,villageid,docid,jid,bookno,actualpages,cid;
    private String etitle,edesc;

    public int getTalukaid() {
        return talukaid;
    }

    public void setTalukaid(int talukaid) {
        this.talukaid = talukaid;
    }

    public int getVillageid() {
        return villageid;
    }

    public void setVillageid(int villageid) {
        this.villageid = villageid;
    }

    public int getDocid() {
        return docid;
    }

    public void setDocid(int docid) {
        this.docid = docid;
    }

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

    public int getActualpages() {
        return actualpages;
    }

    public void setActualpages(int actualpages) {
        this.actualpages = actualpages;
    }

    public String getEtitle() {
        return etitle;
    }

    public void setEtitle(String etitle) {
        this.etitle = etitle;
    }

    public String getEdesc() {
        return edesc;
    }

    public void setEdesc(String edesc) {
        this.edesc = edesc;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }
    
    
    @Override
    public String toString()
    {
    return "Talukaid:"+this.talukaid+",Villageid:"+this.villageid+",Etitle:"+this.etitle+",Docid:"+this.docid+",Edesc:"+this.edesc+",Jid:"+this.jid+",Bookno:"+this.bookno+",Pages:"+this.actualpages;
    }
	
}
