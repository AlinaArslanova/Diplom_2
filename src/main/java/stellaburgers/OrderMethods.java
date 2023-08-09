package stellaburgers;
import java.util.List;

public class OrderMethods {

    private List<String> ingredients;

    public OrderMethods(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }
}
