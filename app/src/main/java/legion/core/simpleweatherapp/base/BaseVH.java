package legion.core.simpleweatherapp.base;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import butterknife.ButterKnife;

public abstract class BaseVH<I> extends RecyclerView.ViewHolder {

    public BaseVH(ViewGroup parent, @LayoutRes int layout) {
        super(LayoutInflater.from(parent.getContext()).inflate(layout, parent, false));
        ButterKnife.bind(this, itemView);
    }

    protected abstract void bind(I item);
}
