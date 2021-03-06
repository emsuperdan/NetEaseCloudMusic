package com.example.tangdan.cloudmusic.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.example.tangdan.cloudmusic.model.MusicModel;

import java.util.ArrayList;
import java.util.List;

public class LocalMusicScanUtils {
    public static List<MusicModel> getMusicData(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        List<MusicModel> data = new ArrayList<>();
        MusicModel model;
        if (cursor != null && cursor.moveToFirst()) {
            for (int i = 0; i < cursor.getCount(); i++) {
                model = new MusicModel();
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
                String url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                long albumId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                int ismusic = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));

                if (ismusic != 0 && duration > 60120) {
                    model.setmTitle(title);
                    model.setmArtist(artist);
                    model.setmAlbum(album);
                    model.setmAlbumId(albumId);
                    model.setmDuration(duration);
                    model.setmId(id);
                    model.setmSize(size);
                    model.setmLocalMicUrl(url);
                    data.add(model);
                }
                cursor.moveToNext();
            }
        }
        cursor.close();
        return data;
    }
}
