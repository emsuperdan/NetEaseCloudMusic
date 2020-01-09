package com.example.tangdan.cloudmusic.utils;

import android.text.TextUtils;

import com.example.tangdan.cloudmusic.model.BaseModel;
import com.example.tangdan.cloudmusic.model.MusicModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class JsonUtils {

    public static void parseJson(String json, List<BaseModel> list) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            jsonObject = jsonObject.getJSONObject("data");
            jsonObject = jsonObject.getJSONObject("song");
            JSONArray array = jsonObject.getJSONArray("list");

            for (int i = 0; i < array.length(); i++) {
                MusicModel model = new MusicModel();
                JSONObject object = array.getJSONObject(i);
                String albumName = object.optString("albumname");
                if (!TextUtils.isEmpty(albumName)) {
                    model.setmAlbum(albumName);
                }
                String albumPicUrl = object.optString("albummid");
                if (!TextUtils.isEmpty(albumPicUrl)) {
                    model.setAlbumPicUrl(albumPicUrl);
                }
                JSONObject singerInnerJson = object.getJSONArray("singer").getJSONObject(0);
                String singerName = singerInnerJson.optString("name");
                if (!TextUtils.isEmpty(singerName)) {
                    model.setmArtist(singerName);
                }
                String songName = object.optString("songname");
                if (!TextUtils.isEmpty(songName)) {
                    model.setmTitle(songName);
                }

                String songId = object.optString("songmid");
                if (!TextUtils.isEmpty(songId)) {
                    model.setmSongId(songId);
                }

                list.add(model);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String parsePlayUrl(String json) {
        try {
            int len = json.length();
            JSONObject jsonObject = new JSONObject(json);
            jsonObject = jsonObject.getJSONObject("req_0");
            jsonObject = jsonObject.getJSONObject("data");
            JSONArray array = jsonObject.optJSONArray("midurlinfo");

            JSONObject object = array.optJSONObject(0);
            String suffixUrl = object.optString("purl");
            JSONArray sipArray = jsonObject.optJSONArray("sip");
            String prefixUrl = sipArray.optString(0);
            return prefixUrl + suffixUrl;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
