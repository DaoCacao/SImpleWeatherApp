package legion.core.simpleweatherapp.base;

public interface BaseMvp {

    interface View {
        void showToast(String message);
    }

    interface Presenter {
        void onViewResume();
        void onViewPause();
    }
}
