package com.example.androiddevelopment.util;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.duzun.player.R;
import com.duzun.player.activity.SpeakActivity;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;


/**
 * Created by asus on 2015/10/9.
 */
public class VoiceUtils {
    public static final int UPDATEIMG = 9987;
    public static final int ENDOFSPEECH = 9986;


    private Context context;
    private SpeakActivity mActivity;
    private LinearLayout popup_image_ll;
    private LinearLayout voice_rcd_hint_rcding;
    private ImageView volume_iv;
    private StringBuffer stringBuffer;
    private SpeechRecognizer mIat;

    public VoiceUtils(Context context) {
        this.context = context;
        mActivity = (SpeakActivity) context;
        initView();
    }

    public void startVoice() {

        popup_image_ll.setVisibility(View.VISIBLE);
        stringBuffer = new StringBuffer();

        popup_image_ll.setVisibility(View.VISIBLE);
        mIat = SpeechRecognizer.createRecognizer(context, mInitListener);
        mIat.setParameter(SpeechConstant.PARAMS, null);
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_MIX);
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");
        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        mIat.setParameter(SpeechConstant.ACCENT, "mandarin");
        mIat.setParameter(SpeechConstant.ASR_PTT, "1");

        int ret = mIat.startListening(mRecoListener);
    }

    public void stopVoice(){
        if(mIat!=null){
            mIat.stopListening();
            popup_image_ll.setVisibility(View.GONE);
        }
    }
    private void initView() {
        popup_image_ll = (LinearLayout) mActivity.findViewById(R.id.ll_main_chat_popup);
        voice_rcd_hint_rcding = (LinearLayout) mActivity.findViewById(R.id.ll_audio);
        volume_iv = (ImageView) mActivity.findViewById(R.id.iv_audio_volume);
    }

    private RecognizerListener mRecoListener = new RecognizerListener() {
        //isLast等于true时会话结束。
        public void onResult(RecognizerResult results, boolean isLast) {
            stringBuffer.append(parseIatResult(results.getResultString()));
            Utils.log("翻译");
            if (isLast) {
                mActivity.setSpeakText(stringBuffer.toString());
            }
        }

        //会话发生错误回调接口
        public void onError(SpeechError error) {
            error.getPlainDescription(true); //获取错误码描述
            Utils.showToast(mActivity, error.getErrorDescription());
        }

        //开始录音
        public void onBeginOfSpeech() {
            Utils.log("开始录音");
        }

        //音量值0~30
        public void onVolumeChanged(int volume, byte[] b) {
            updateDisplay(volume / 12);
        }

        //结束录音
        public void onEndOfSpeech() {
            Utils.log("结束录音");
            popup_image_ll.setVisibility(View.GONE);
        }

        //扩展用接口
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
        }
    };

    private InitListener mInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            Utils.log("SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                Utils.log("初始化失败，错误码：" + code);
            }
        }
    };

    /**
     * 更新音量图片
     *
     * @param signalEMA
     */
    private void updateDisplay(int signalEMA) {
        switch (signalEMA) {
            case 0:
            case 1:
                volume_iv.setImageResource(R.drawable.voice1);
                break;
            case 2:
            case 3:
                volume_iv.setImageResource(R.drawable.voice2);
                break;
            case 4:
            case 5:
                volume_iv.setImageResource(R.drawable.voice3);
                break;
            case 6:
            case 7:
                volume_iv.setImageResource(R.drawable.voice4);
                break;
            case 8:
            case 9:
                volume_iv.setImageResource(R.drawable.voice5);
                break;
            case 10:
            case 11:
                volume_iv.setImageResource(R.drawable.voice6);
                break;
            default:
                volume_iv.setImageResource(R.drawable.voice7);
                break;
        }
    }

    /**
     * 解析返回的json
     *
     * @param json
     * @return
     */
    public String parseIatResult(String json) {
        StringBuffer ret = new StringBuffer();
        try {
            JSONTokener tokener = new JSONTokener(json);
            JSONObject joResult = new JSONObject(tokener);

            JSONArray words = joResult.getJSONArray("ws");
            for (int i = 0; i < words.length(); i++) {
                // 转写结果词，默认使用第一个结果
                JSONArray items = words.getJSONObject(i).getJSONArray("cw");
                JSONObject obj = items.getJSONObject(0);
                ret.append(obj.getString("w"));
            }
        } catch (Exception e) {
            Log.i("TAG", e.toString());
           // e.printStackTrace();
        }
        return ret.toString();
    }
}
