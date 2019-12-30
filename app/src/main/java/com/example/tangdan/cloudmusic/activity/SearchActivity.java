package com.example.tangdan.cloudmusic.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.tangdan.cloudmusic.R;
import com.example.tangdan.cloudmusic.adapter.SearchListAdapter;
import com.example.tangdan.cloudmusic.model.BaseModel;
import com.example.tangdan.cloudmusic.model.ItemModel;
import com.example.tangdan.cloudmusic.model.MusicModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchActivity extends BaseActivity implements View.OnClickListener {
    private EditText mEditText;
    private String mInputSingerName;
    private TextView mBackText, mSearchConfirm, mResultNotFound;
    private SearchListAdapter mAdapter;
    private ListView mSearchResult;
    private ProgressBar mProgressBar;

    //用来储存每次搜索的结果List
    private List<BaseModel> mTotalList = new ArrayList<>();
    //储存每次要点击的有效songlist
    private List<MusicModel> mSongList = new ArrayList<>();

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                //开始动画
                case 0x01:
                    mSearchResult.setVisibility(View.GONE);
                    mProgressBar.setVisibility(View.VISIBLE);
                    break;
                case 0x03:
                    mSearchResult.setVisibility(View.VISIBLE);
                    mResultNotFound.setVisibility(View.GONE);
                    mProgressBar.setVisibility(View.GONE);
                    mAdapter.notifyDataSetChanged();
                    break;
                case 0x04:
                    mSearchResult.setVisibility(View.GONE);
                    mProgressBar.setVisibility(View.GONE);
                    mResultNotFound.setVisibility(View.VISIBLE);
                    mAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mEditText = findViewById(R.id.et_search_edit);
        mBackText = findViewById(R.id.tv_search_back);
        mBackText.setOnClickListener(this);

        mSearchConfirm = findViewById(R.id.tv_search_confirm);
        mSearchConfirm.setOnClickListener(this);
        mSearchResult = findViewById(R.id.lv_search_list);
        mResultNotFound = findViewById(R.id.tv_show_result_notfound);
        mProgressBar = findViewById(R.id.pb_show_result_before);
        mAdapter = new SearchListAdapter();
        mSearchResult.setAdapter(mAdapter);

        mSearchResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MusicModel model = mSongList.get(position);
                Intent intent = new Intent(SearchActivity.this, MusicPlayActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("musicmodel", model);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        mInputSingerName = mEditText.getText().toString();
        switch (v.getId()) {
            case R.id.tv_search_confirm:
                mHandler.sendEmptyMessage(0x01);
                okHttpGetMethod();
                break;
            case R.id.tv_search_back:
                finish();
                break;
            default:
                break;
        }
    }

    private void okHttpGetMethod() {
        final Request request = new Request.Builder().url("https://c.y.qq.com/soso/fcgi-bin/client_search_cp?aggr=1&cr=1&flag_qc=0&p=1&n=5&w=" + mInputSingerName).get().build();
        OkHttpClient client = new OkHttpClient();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("TAGTAG", "error" + e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    InputStream inputStream = response.body().byteStream();
                    byte[] B = new byte[4096];
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    int length;
                    while ((length = inputStream.read(B)) > 0) {
                        baos.write(B, 0, length);
                    }
                    String result = baos.toString();
                    //完整的返回数据结果可以通过在浏览器中输入完整连接即可获得
                    parseJson(result, mTotalList);

                    if (mTotalList.size() != 0) {
                        for (int i = 0; i < mTotalList.size(); i++) {
                            mSongList.add((MusicModel) mTotalList.get(i));
                        }
                        mTotalList.add(new ItemModel());
                        mAdapter.setData(SearchActivity.this, mTotalList);
                        mHandler.sendEmptyMessage(0x03);
                    } else {
                        mHandler.sendEmptyMessage(0x04);
                    }
                }
            }
        });
    }

    private void okhttpPost() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS).build();
        Request request = new Request.Builder().url("https://c.y.qq.com/soso/fcgi-bin/client_search_cp?aggr=1&cr=1&flag_qc=0&p=10&n=10&w=" + mInputSingerName)
                .header("Cookie", "appver=1.5.0.75771")
                .header("User-Agent", "Mozilla/5.0 (lWindows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.90 Safari/537.36")
                .header("Referer", "http://music.163.com/")
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    InputStream inputStream = response.body().byteStream();
                    byte[] B = new byte[8192];
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    int length;
                    while ((length = inputStream.read(B)) > 0) {
                        baos.write(B, 0, length);
                    }
                    String result = baos.toString();
                }

            }
        });
    }

    private void parseJson(String json, List<BaseModel> list) {
        try {
            int len = json.length();
            String result = json.substring(9, len - 1);
            JSONObject jsonObject = new JSONObject(result);
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
}
