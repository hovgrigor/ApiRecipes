package Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.example.rustam.apirecipes.R;
import java.util.List;
import Classes.Food;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.MyViewHolder> implements RecyclerView.OnItemTouchListener
{

    //Adapter for RecyclerView

    private List<Food> foodList;

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView img;
        private TextView label;
        private ListView listView;


        MyViewHolder(View view)
        {
            super(view);
            img = view.findViewById(R.id.im_food);
            label = view.findViewById(R.id.t_dishNameDetails);
            listView = view.findViewById(R.id.l_ingridients);
        }
    }


    public FoodAdapter(List<Food> foodList)
    {
        this.foodList = foodList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_list_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {
        Food food = foodList.get(position);
        holder.listView.setAdapter(food.getl_ingridientsAdapter());
        holder.img.setImageBitmap(food.getIm_food());
        holder.label.setText(food.getdishName());
        holder.listView.setEnabled(false);
    }

    @Override
    public int getItemCount()
    {
        return foodList.size();
    }

    private OnItemClickListener mListener;
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
    GestureDetector mGestureDetector;
    public FoodAdapter(Context context, OnItemClickListener listener) {
        mListener = listener;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }
    @Override
    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
        View childView = view.findChildViewUnder(e.getX(), e.getY());
        if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
            mListener.onItemClick(childView, view.getChildAdapterPosition(childView));
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
