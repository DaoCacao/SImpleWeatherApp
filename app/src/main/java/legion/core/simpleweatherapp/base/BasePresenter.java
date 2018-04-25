package legion.core.simpleweatherapp.base;

public class BasePresenter<V extends BaseMvp.View> {
    private V view;

    public BasePresenter(V view) {
        this.view = view;
    }

    public V getView() {
        return view;
    }
}
