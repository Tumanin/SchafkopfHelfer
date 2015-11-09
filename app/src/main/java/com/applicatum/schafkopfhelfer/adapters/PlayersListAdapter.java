package com.applicatum.schafkopfhelfer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.applicatum.schafkopfhelfer.R;
import com.applicatum.schafkopfhelfer.models.Player;

import java.util.List;

/**
 * Created by Alexx on 09.11.2015.
 */
public class PlayersListAdapter extends BaseAdapter{

    private static String TAG = "PlayersListAdapter";

    private Context mContext;
    private LayoutInflater mInflater;
    private List<Player> mList;

    public PlayersListAdapter (Context context, List<Player> list){

        mContext = context;
        mInflater = LayoutInflater.from(context);
        mList = list;
    }

    @Override
    public int getCount() {
        return mList != null ? mList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        int type = getItemViewType(position);
        Player player = (Player)getItem(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.player_list_item, null);
            holder.playerName = (TextView) convertView.findViewById(R.id.playerName);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.playerName.setText(player.getName());
        return convertView;
    }



    static class ViewHolder {
        TextView playerName;
    }
}

