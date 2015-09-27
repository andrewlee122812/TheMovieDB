package in.reduxpress.themoviedb.DataModels;

/**
 * Created by kumardivyarajat on 27/09/15.
 */
public class Cast {

    String order;
    String character;
    String name;
    String id;
    String profile_path;
    String credit_id;

    public Cast() {
    }

    public String getOrder() {

        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProfile_path() {
        return profile_path;
    }

    public void setProfile_path(String profile_path) {
        this.profile_path = profile_path;
    }

    public String getCredit_id() {
        return credit_id;
    }

    public void setCredit_id(String credit_id) {
        this.credit_id = credit_id;
    }
}
