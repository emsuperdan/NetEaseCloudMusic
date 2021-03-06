package com.example.tangdan.cloudmusic.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.tangdan.cloudmusic.R;
import com.example.tangdan.cloudmusic.adapter.SearchListAdapter;
import com.example.tangdan.cloudmusic.adapter.SearchMoreAdapter;
import com.example.tangdan.cloudmusic.model.BaseModel;
import com.example.tangdan.cloudmusic.model.ItemModel;
import com.example.tangdan.cloudmusic.model.MusicModel;
import com.example.tangdan.cloudmusic.utils.JsonUtils;

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

import static com.example.tangdan.cloudmusic.utils.Constants.mic_search_address0;
import static com.example.tangdan.cloudmusic.utils.Constants.mic_search_address1;
import static com.example.tangdan.cloudmusic.utils.Constants.mic_search_address2;

public class SearchActivity extends BaseActivity implements View.OnClickListener {
    private EditText mEditText;
    private String mInputSingerName;
    private TextView mBackText, mSearchConfirm, mResultNotFound;
    private SearchListAdapter mAdapter;
    private SearchMoreAdapter mSearchMoreAdapter;

    private ListView mSearchResult,mMoreExpandList;
    private ProgressBar mProgressBar;
    private RelativeLayout mHeadLayout;

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
                case 0x02:
                    mHeadLayout.setVisibility(View.GONE);
                    mSearchResult.setVisibility(View.GONE);
                    mMoreExpandList.setVisibility(View.VISIBLE);
                    mProgressBar.setVisibility(View.GONE);
                    mSearchMoreAdapter.notifyDataSetChanged();
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
        mMoreExpandList = findViewById(R.id.lv_search_more_expand_list);
        mResultNotFound = findViewById(R.id.tv_show_result_notfound);
        mProgressBar = findViewById(R.id.pb_show_result_before);
        mHeadLayout = findViewById(R.id.activity_search_head);
        mAdapter = new SearchListAdapter();
        mSearchMoreAdapter = new SearchMoreAdapter();
        mSearchResult.setAdapter(mAdapter);
        mMoreExpandList.setAdapter(mSearchMoreAdapter);

        mSearchResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position != mTotalList.size() - 1) {
                    MusicModel model = mSongList.get(position);
                    Intent intent = new Intent(SearchActivity.this, MusicPlayActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("musicmodel", model);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    mHandler.sendEmptyMessage(0x01);
                    okHttpGetMethod(20, true);
                }

            }
        });
        mMoreExpandList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                okHttpGetMethod(5, false);
                break;
            case R.id.tv_search_back:
                finish();
                break;
            default:
                break;
        }
    }

    private void okHttpGetMethod(int count, final boolean getMore) {
        mTotalList.clear();
        mSongList.clear();
        //需要及时更新
        final Request request = new Request.Builder().url(mic_search_address0 + String.valueOf(count) + mic_search_address1 + mInputSingerName + mic_search_address2).get().build();
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
                    JsonUtils.parseJson(result, mTotalList);

                    if (mTotalList.size() != 0) {
                        for (int i = 0; i < mTotalList.size(); i++) {
                            mSongList.add((MusicModel) mTotalList.get(i));
                        }
                        if (!getMore) {
                            mTotalList.add(new ItemModel());
                            mAdapter.setData(SearchActivity.this, mTotalList);
                            mHandler.sendEmptyMessage(0x03);
                        }else {
                            mSearchMoreAdapter.setData(SearchActivity.this,mSongList);
                            mHandler.sendEmptyMessage(0x02);
                        }
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
}
