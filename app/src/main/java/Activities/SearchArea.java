package Activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import com.example.rustam.apirecipes.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Objects;
import Adapters.FoodAdapter;
import Classes.Food;
import Download.GetJson;
import Download.Getbitmap;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchArea.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchArea#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchArea extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ProgressBar psgbar;
    private View parentLayout;
    private FoodAdapter f_adapter;
    private ArrayList<Food> l_FoodList = new ArrayList<>();
    private boolean lock;
    private int Finish = 50;
    private int Start = 0;
    private String ingridient;
    private int number_per_time = Finish;

    private OnFragmentInteractionListener mListener;

    public SearchArea() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchArea.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchArea newInstance(String param1, String param2) {
        SearchArea fragment = new SearchArea();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_area, container, false);
    }

    public void onViewCreated(@NonNull View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        psgbar = Objects.requireNonNull(getView()).findViewById(R.id.psgbar);
        RecyclerView r_foodList = getView().findViewById(R.id.r_foodList);

        f_adapter = new FoodAdapter(l_FoodList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getView().getContext());
        r_foodList.setLayoutManager(mLayoutManager);
        r_foodList.setItemAnimator(new DefaultItemAnimator());
        r_foodList.setAdapter(f_adapter);

        parentLayout = getView().findViewById(android.R.id.content);
        //Event that accrues when page scrolled
        r_foodList.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy)
            {
                super.onScrolled(recyclerView, dx, dy);
                //Getting position of last completely visible item
                int visible = ((LinearLayoutManager) Objects.requireNonNull(recyclerView.getLayoutManager())).findLastCompletelyVisibleItemPosition();
                //Getting the number of items in recyclerView
                int length = recyclerView.getLayoutManager().getItemCount();
                //if the the end is reached this is triggered
                if (visible == length - 1)
                {
                    if(length != 0) {
                        Start += number_per_time;
                        Finish += number_per_time;
                        getJson(ingridient);
                    }
                }
            }
        });

        r_foodList.addOnItemTouchListener(new FoodAdapter(getView().getContext(), (view, position) -> {
                    Intent myIntent = new Intent(getView().getContext(), Details.class);
                    if(position >= 0) {
                        myIntent.putExtra("Image", l_FoodList.get(position).getIm_food());
                        myIntent.putExtra("List", l_FoodList.get(position).getl_lngridients());
                        myIntent.putExtra("DishName", l_FoodList.get(position).getdishName());
                        myIntent.putExtra("link", l_FoodList.get(position).geturl());
                        startActivity(myIntent);
                    }
                })
        );
    }

    public void firstSearch(String ingridient){
        lock = false;
        //Refreshing everything
        Start = 0;
        this.ingridient = ingridient;
        Finish = 50;
        l_FoodList.clear();
        f_adapter.notifyDataSetChanged();
        getJson(ingridient);
    }

    private void getJson(String ingridient){
        //Lock is present to stop continuous creation of Threads
        if(!lock)
        {
            lock = true;
            //Getting Json
            new GetJson(psgbar, json ->
            {
                if (json != null)
                {
                    Search(json);
                }
            }).execute(ingridient, Integer.toString(Start), Integer.toString(Finish));
        }
    }

    private void Search(String json) {
        try
        {
            //Getting Hits from json
            JSONObject jsonObject = new JSONObject(json);
            JSONArray hits = jsonObject.getJSONArray("hits");
            //If no hits are present
            if(hits.toString().equals("[]"))
            {
                Snackbar.make(Objects.requireNonNull(getView()), "No results were found", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                psgbar.setVisibility(View.GONE);
                lock = false;
            }else
            {
                String url;
                String dishName;
                String url_image;
                //Filling variables and adding them to RecyclerView
                for (int x = 0; x < hits.length(); x++)
                {
                    ArrayList<String> ingredients_list = new ArrayList<>();

                    JSONObject num = hits.getJSONObject(x);
                    JSONObject recipe0 = num.getJSONObject("recipe");

                    dishName = recipe0.getString("label");

                    url_image = recipe0.getString("image");
                    url = recipe0.getString("url");
                    JSONArray ingredients = recipe0.getJSONArray("ingredientLines");

                    for (int y = 0; y < ingredients.length(); y++)
                    {
                        ingredients_list.add(ingredients.getString(y));
                    }

                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), R.layout.listview_style, R.id.t_listSyle, ingredients_list);

                    l_FoodList.add(new Food(dishName, url, ingredients_list, arrayAdapter));
                    f_adapter.notifyDataSetChanged();

                    final int i = x;
                    new Getbitmap(url_image, (bm) ->
                    {
                        try {
                            l_FoodList.get(i + (Finish - number_per_time)).setIm_food(bm);
                            psgbar.setVisibility(View.GONE);
                            f_adapter.notifyDataSetChanged();
                            if(i == Finish - 1){
                                lock = false;
                            }
                        }catch(java.lang.IndexOutOfBoundsException e){
                            //System.out.println(e);
                        }
                    }).execute();
                }
            }
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
