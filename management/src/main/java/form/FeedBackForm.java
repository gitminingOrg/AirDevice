package form;


import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class FeedBackForm {

    @NotNull
    @Min(1)
    private int id;

    @NotNull
    @Max(2)
    @Min(1)
    private int status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public FeedBackForm() {}

    public FeedBackForm(int id, int status) {
        this.id = id;
        this.status = status;
    }
}
