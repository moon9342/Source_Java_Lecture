package myBatisSample.book.vo;

public class BookVO {

	private String btitle;
	private String bisbn;
	private String bprice;
	private String bauthor;
	
	public BookVO() {
	
	}

	public String getBtitle() {
		return btitle;
	}

	public void setBtitle(String btitle) {
		this.btitle = btitle;
	}

	public String getBisbn() {
		return bisbn;
	}

	public void setBisbn(String bisbn) {
		this.bisbn = bisbn;
	}

	public String getBprice() {
		return bprice;
	}

	public void setBprice(String bprice) {
		this.bprice = bprice;
	}

	public String getBauthor() {
		return bauthor;
	}

	public void setBauthor(String bauthor) {
		this.bauthor = bauthor;
	}
	
	@Override
	public String toString() {
		return this.getBtitle() + ", " + this.getBauthor() + ", " + this.getBprice();
	}
}
