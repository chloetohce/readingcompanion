package sg.edu.nus.iss.readingcompanion.model;

import java.util.Date;
import java.util.List;

public class Book {
    private String title;
    private String id;
    private List<String> authors;
    private String publisher;
    private String isbn;
    private List<String> genres;
    private String imageLink;

    private Date start;
    private Date end;
    private String status;
    
    public Book() {
    }

    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}

    public String getId() {return id;}
    public void setId(String id) {this.id = id;}

    public List<String> getAuthors() {return authors;}
    public void setAuthors(List<String> authors) {this.authors = authors;}

    public String getPublisher() {return publisher;}
    public void setPublisher(String publisher) {this.publisher = publisher;}

    public String getIsbn() {return isbn;}
    public void setIsbn(String isbn) {this.isbn = isbn;}

    public List<String> getGenres() {return genres;}
    public void setGenres(List<String> genres) {this.genres = genres;}

    public String getImageLink() {return imageLink;}
    public void setImageLink(String imageLink) {this.imageLink = imageLink;}

    public Date getStart() {return start;}
    public void setStart(Date start) {this.start = start;}

    public Date getEnd() {return end;}
    public void setEnd(Date end) {this.end = end;}

    public String getStatus() {return status;}
    public void setStatus(String status) {this.status = status;}

    
}
