package in.reduxpress.themoviedb.DataModels;

/**
 * Created by kumardivyarajat on 30/09/15.
 */
public class Review {
    String author;
    String content;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Review(String author, String content) {

        this.author = author;
        this.content = content;
    }

    public Review() {

    }
}
