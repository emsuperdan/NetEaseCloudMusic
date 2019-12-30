package com.example.tangdan.cloudmusic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.tangdan.cloudmusic.R;
import com.example.tangdan.cloudmusic.model.BaseModel;
import com.example.tangdan.cloudmusic.model.ItemModel;
import com.example.tangdan.cloudmusic.model.MusicModel;

import java.util.List;

public class SearchListAdapter extends BaseAdapter {
    private Context mContext;
    private List<BaseModel> mList;

    public void setData(Context context, List<BaseModel> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public int getCount() {
        if (mList == null) {
            return 0;
        }
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        if (mList == null) {
            return null;
        }
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BaseModel model = mList.get(position);
        if (model instanceof ItemModel) {
            return LayoutInflater.from(mContext).inflate(R.layout.item_search_musiclist_more, null, false);
        }
        View view = null;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_search_musiclist, null, false);
            viewHolder = new ViewHolder();
            viewHolder.mSongName = view.findViewById(R.id.tv_search_title);
            viewHolder.mSingerName = view.findViewById(R.id.tv_search_artist);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        if (model instanceof MusicModel) {
            viewHolder.mSingerName.setText(((MusicModel) model).getmTitle());
            viewHolder.mSongName.setText(((MusicModel) model).getmArtist());
        }
        return view;
    }


    static class ViewHolder {
        TextView mSongName;
        TextView mSingerName;
    }
}
