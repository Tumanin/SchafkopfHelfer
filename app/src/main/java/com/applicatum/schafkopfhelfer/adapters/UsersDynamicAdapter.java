package com.applicatum.schafkopfhelfer.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.applicatum.schafkopfhelfer.R;
import com.applicatum.schafkopfhelfer.models.Player;

import org.askerov.dynamicgrid.BaseDynamicGridAdapter;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by Alexx on 02.11.2015.
 */
public class UsersDynamicAdapter extends BaseDynamicGridAdapter {

    //private List<User> users;
    //private Context context;
    private LayoutInflater mInflater;
    private ArrayList<Integer> colors;
    private boolean statistic;

    public UsersDynamicAdapter(Context context, List<Player> items, int columnCount) {

        super(context, items, columnCount);
        //users = items;
        //this.context = context;
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
        statistic = false;
    }

    public boolean isStatistic() {
        return statistic;
    }

    public void setStatistic(boolean statistic) {
        this.statistic = statistic;
    }

    public List<Player> getAllItems() {
        ArrayList<Player> players = new ArrayList<>();
        for(Object object : super.getItems()){
            players.add((Player) object);
        }

        return players;
    }

    @Override
    public void set(List<?> items) {
        super.set(items);
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
                int color = colors.remove(0);
                player.setColor(color);
            }
            Log.d("ColorIssue", "color updated to " + player.getColor());

            holder = new UserViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (UserViewHolder) convertView.getTag();
        }
        View view = convertView.findViewById(R.id.playerCard);
        if (!statistic) {
            if (player.getState()== Player.State.WIN) {
                view.setBackgroundResource(R.drawable.bg_button_green);
                //view.setForeground(getContext().getResources().getDrawable(R.drawable.bg_button_green, null));
                //view.setCardBackgroundColor(R.color.green_light);
                Log.d("ColorIssue", "WIN");
            }else if(player.getState()== Player.State.PLAY){
                //view.setForeground(getContext().getResources().getDrawable(R.drawable.bg_button_teal, null));
                view.setBackgroundResource(R.drawable.bg_button_primary);
                //view.setCardBackgroundColor(R.color.teal_dark);
                Log.d("ColorIssue", "PLAY");
            }else if(player.getState()== Player.State.WAIT){
                //view.setForeground(getContext().getResources().getDrawable(R.drawable.bg_button_gray, null));
                view.setBackgroundResource(R.drawable.bg_button_gray);
                //view.setCardBackgroundColor(R.color.gray_dark);
                Log.d("ColorIssue", "WAIT");
            }
        } else {
            //view.setForeground(getContext().getResources().getDrawable(getRandomColor(player.getColor()), null));
            view.setBackgroundResource(getRandomColor(player.getColor()));
            //view.setCardBackgroundColor(getRandomColor(player.getColor()));
            Log.d("ColorIssue", "statistic");
        }
        holder.build(player.getName(), player.getPoints(), player.getChangePoints(), getState(player), player.getColor());
        return convertView;
    }

    private int getRandomColor(int randomNumber){
        //Random random = new Random();
        //int randomNumber = random.nextInt(8);
        //int randomNumber = colors.remove(0);
        Log.d("ColorIssue", "number is " + randomNumber);
        switch (randomNumber){
            case 0:
                return R.drawable.bg_button_pink;
            case 1:
                return R.drawable.bg_button_lime;
            case 2:
                return R.drawable.bg_button_green;
            case 3:
                return R.drawable.bg_button_purple;
            case 4:
                return R.drawable.bg_button_amber;
            case 5:
                return R.drawable.bg_button_blue;
            case 6:
                return R.drawable.bg_button_red;
            case 7:
                return R.drawable.bg_button_teal;
            default:
                return R.drawable.bg_button_red;
        }
        //return R.color.purple_dark;
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
        private TextView txtChangePoints;
        private TextView txtState;

        private UserViewHolder(View view) {
            txtName = (TextView) view.findViewById(R.id.userName);
            txtPoints = (TextView) view.findViewById(R.id.userPoints);
            txtChangePoints = (TextView) view.findViewById(R.id.userChangePoints);
            txtState = (TextView) view.findViewById(R.id.userState);
        }

        void build(String name, int points, int pointChange, String state, int color) {
            txtName.setText(name);
            //txtName.setTextColor(context.getResources().getColor(color));
            txtPoints.setText(String.valueOf(points));
            txtChangePoints.setText("("+String.valueOf(pointChange)+")");
            //txtPoints.setTextColor(context.getResources().getColor(color));
            txtState.setText(state);
        }
    }

}
