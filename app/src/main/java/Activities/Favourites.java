package Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import com.example.rustam.apirecipes.R;
import java.io.File;
import java.util.ArrayList;
import java.util.Objects;
import Adapters.FoodAdapter;
import Classes.CommonModelClass;
import Classes.FavouriteListener;
import Classes.Food;
import Saving.SaveImage;
import Saving.SaveObject;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Favourites.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Favourites#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Favourites extends Fragment implements FavouriteListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View parentLayout;
    private int numberOfFavourites = 0;
    private FoodAdapter f_adapter;
    private ArrayList<Food> l_FoodList = new ArrayList<>();

    private OnFragmentInteractionListener mListener;

    public Favourites() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Favourites.
     */
    // TODO: Rename and change types and number of parameters
    public static Favourites newInstance(String param1, String param2) {
        Favourites fragment = new Favourites();
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
        return inflater.inflate(R.layout.fragment_favourites, container, false);
    }

    public void onViewCreated(@NonNull View v, Bundle savedInstanceState) {
        CommonModelClass commonModelClass = CommonModelClass.getSingletonObject();
        commonModelClass.setbaseActivity(this);

        super.onViewCreated(v, savedInstanceState);
        RecyclerView recyclerView = Objects.requireNonNull(getView()).findViewById(R.id.r_foodListFavourite);
        //Configuring RecyclerView
        f_adapter = new FoodAdapter(l_FoodList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getView().getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(f_adapter);
        parentLayout = getView().findViewById(android.R.id.content);

        //Taking to Details Activity when click on item inside Recycler
        recyclerView.addOnItemTouchListener(new FoodAdapter(getView().getContext(), (view, position) -> {
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

        refreshFavourites();
    }


    public void refreshFavourites(){
        l_FoodList.clear();
        f_adapter.notifyDataSetChanged();
        File file = Objects.requireNonNull(getContext()).getFilesDir();
        File[] files = file.listFiles();
        String dishName = "";
        ArrayList<String> ingridientsList = new ArrayList<>();
        String link = "";
        Bitmap image = null;
        int x  = files.length;
        int k = 0;
        if(x > 0) {
            for (File member : files) {
                String name = member.getName();
                String temp = name.substring(name.indexOf(",")+1, name.length());
                String type = temp.substring( 0, temp.indexOf(","));
                switch (type) {
                    case "DishName":
                        k++;
                        String ser = SaveObject.ReadSettings(getContext(), name);
                        if (!ser.equalsIgnoreCase("")) {
                            Object obj = SaveObject.stringToObject(ser);
                            if (obj instanceof String) {
                                dishName = (String) obj;
                            }
                        }
                        break;
                    case "ArrayList":
                        k++;
                        String ser1 = SaveObject.ReadSettings(getContext(), name);
                        if (!ser1.equalsIgnoreCase("")) {
                            Object obj = SaveObject.stringToObject(ser1);
                            if (obj instanceof ArrayList) {
                                ingridientsList = (ArrayList<String>) obj;
                            }
                        }
                        break;
                    case "Link":
                        k++;
                        String ser2 = SaveObject.ReadSettings(getContext(), name);
                        if (!ser2.equalsIgnoreCase("")) {
                            Object obj = SaveObject.stringToObject(ser2);
                            if (obj instanceof String) {
                                link = (String) obj;
                            }
                        }
                        break;
                    case "Image":
                        k++;
                        image = new SaveImage().loadImageFromStorage(getContext(),name);
                        break;
                }

                if(k == 4){
                    k = 0;
                    numberOfFavourites++;
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), R.layout.listview_style, R.id.t_listSyle, ingridientsList);
                    l_FoodList.add(new Food(dishName,link,image,ingridientsList, arrayAdapter));
                    f_adapter.notifyDataSetChanged();
                }
            }
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

    @Override
    public void onFavouriteSaved() {
        refreshFavourites();
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
