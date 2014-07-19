package squotes.db;

/**
 * Represents a quote
 * @author stack
 *
 */
public class TempQuote {
	private int id;	
	private String content;
	private String author;
	private boolean isPosted;
	
	public TempQuote(int id, String content, String author, boolean isPosted) {
		this.id = id;
		this.content = content;
		this.author = author;
		this.isPosted = isPosted;
	}

	public boolean isPosted() {
		return isPosted;
	}

	public void setPosted(boolean isPosted) {
		this.isPosted = isPosted;
	}

	public int getId() {
		return id;
	}

	public String getContent() {
		return content;
	}

	public String getAuthor() {
		return author;
	}
	
	
}
