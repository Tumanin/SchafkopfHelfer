package com.applicatum.schafkopfhelfer.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.applicatum.schafkopfhelfer.R;
import com.applicatum.schafkopfhelfer.models.Player;

import org.askerov.dynamicgrid.BaseDynamicGridAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by Alexx on 02.11.2015.
 */
public class UsersDynamicAdapter extends BaseDynamicGridAdapter {

    //private List<User> users;
    private Context context;
    private LayoutInflater mInflater;
    private ArrayList<Integer> colors;

    public UsersDynamicAdapter(Context context, List<Player> items, int columnCount) {

        super(context, items, columnCount);
        //users = items;
        this.context = context;
        mInflater = LayoutInflater.from(context);
        colors = new ArrayList<>();
        colors.add(0);
        colors.add(1);
        colors.add(2);
        colors.add(3);
        colors.add(4);
        colors.add(5);
        colors.add(6);
        colors.add(7);
        Collections.shuffle(colors);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        UserViewHolder holder;
        Player player = (Player) getItem(position);
        Log.d("ColorIssue", "Player Name is " + player.getName());
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.user_item_layout, null);


            if (player.getColor()==-1) {
                Log.d("ColorIssue", "color is -1");
                player.setColor(getRandomColor());
            }
            Log.d("ColorIssue", "color updated to " + player.getColor());

            holder = new UserViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (UserViewHolder) convertView.getTag();
        }
        //View view = convertView.findViewById(R.id.playerCard);
        //view.setBackgroundResource(player.getColor());
        holder.build(player.getName(), player.getPoints(), getState(player), player.getColor());
        return convertView;
    }

    private int getRandomColor(){
        //Random random = new Random();
        //int randomNumber = random.nextInt(8);
        int randomNumber = colors.remove(0);
        Log.d("ColorIssue", "number is " + randomNumber);
        switch (randomNumber){
            case 0:
                return R.color.red_light;
            case 1:
                return R.color.pink_light;
            case 2:
                return R.color.purple_light;
            case 3:
                return R.color.cyan_light;
            case 4:
                return R.color.teal_light;
            case 5:
                return R.color.indigo_light;
            case 6:
                return R.color.amber_light;
            case 7:
                return R.color.orange_light;
            default:
                return R.color.red_light;
        }

    }

    private String getState(Player player){
        switch (player.getState()){
            case PLAY:
                return "spielt";
            case WAIT:
                return "wartet";
            case WIN:
                return "gewonnen";
        }
        return "";
    }

    private class UserViewHolder {
        private TextView txtName;
        private TextView txtPoints;
        private TextView txtState;

        private UserViewHolder(View view) {
            txtName = (TextView) view.findViewById(R.id.userName);
            txtPoints = (TextView) view.findViewById(R.id.userPoints);
            txtState = (TextView) view.findViewById(R.id.userState);
        }

        void build(String name, int points, String state, int color) {
            txtName.setText(name);
            //txtName.setTextColor(context.getResources().getColor(color));
            txtPoints.setText(String.valueOf(points));
            //txtPoints.setTextColor(context.getResources().getColor(color));
            txtState.setText(state);
        }
    }

}
