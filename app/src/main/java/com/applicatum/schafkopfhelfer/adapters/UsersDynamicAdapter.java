package com.applicatum.schafkopfhelfer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.applicatum.schafkopfhelfer.R;
import com.applicatum.schafkopfhelfer.models.Player;

import org.askerov.dynamicgrid.BaseDynamicGridAdapter;

import java.util.List;

/**
 * Created by Alexx on 02.11.2015.
 */
public class UsersDynamicAdapter extends BaseDynamicGridAdapter {

    //private List<User> users;
    //private Context context;
    private LayoutInflater mInflater;

    public UsersDynamicAdapter(Context context, List<Player> items, int columnCount) {

        super(context, items, columnCount);
        //users = items;
        //this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        UserViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.user_item_layout, null);
            holder = new UserViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (UserViewHolder) convertView.getTag();
        }

        Player player = (Player) getItem(position);
        holder.build(player.getName(), player.getPoints(), getState(player));
        return convertView;
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

        void build(String name, int points, String state) {
            txtName.setText(name);
            txtPoints.setText(String.valueOf(points));
            txtState.setText(state);
        }
    }

}
