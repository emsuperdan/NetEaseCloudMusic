package com.example.tangdan.cloudmusic.model;

import com.example.tangdan.cloudmusic.callback.ICallBack;

public class BaseAAAModel implements IModel {

    /**
     * @param input
     * @param callBack 开线程模仿网络请求，并把回调写入
     */
    @Override
    public void getResultInCallBack(final String input, final ICallBack<String> callBack) {
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                switch (input) {
                    case "success":
                        callBack.onSuccess("传入参数input请求结果成功");
                        break;
                    case "fail":
                        callBack.onFailure("input请求失败");
                        break;
                    case "error":
                        callBack.onError("input请求出错");
                        break;
                }
                callBack.onComplete();
            }
        };

        /**
         * 模仿耗时操作,并最终把结果返回
         */
        new Thread() {
            @Override
            public void run() {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runnable.run();
            }
        }.start();
    }
}
