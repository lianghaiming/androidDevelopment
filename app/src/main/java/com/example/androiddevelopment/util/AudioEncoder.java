package com.example.androiddevelopment.util;

import android.annotation.TargetApi;
import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.os.Build;
import android.os.Handler;

import com.duzun.player.App;
import com.duzun.player.net.TCPRometeLocalClient;
import com.duzun.player.service.HandleCommandService;

import java.io.IOException;
import java.net.SocketException;
import java.nio.ByteBuffer;

//import org.apache.http.util.ByteArrayBuffer;

/**
 * 音频解码
 *
 * @author asus
 */
public class AudioEncoder extends Thread {

    private MediaCodec mediaCodec;

    private MediaExtractor extractor;

    private MediaFormat mediaFormat;

    private String mime;

    private ByteBuffer[] inputBuffers;

    private ByteBuffer[] outputBuffers;

    private MediaCodec.BufferInfo info;

    private Handler mHandler;

//	private ByteArrayBuffer bab;

    private String filePath;
//    private TCPRometeLocalClient tcpClient;
    private boolean bStopDecode = false;
    private int connectTime=0;

    public void closeThread () {
        isFinish = false;
        bStopDecode = true;
    }

    public AudioEncoder (Handler handler) {
        mHandler = handler;
    }

    public void StopDecode () {
        Utils.log ("AudioDecoder StopDecode");
        bStopDecode = true;
    }

    /**
     * setFilePath 线程将向8886建立连接，发送samplerate，解码并持续发送数据
     */
    public void setFilePath (String filePath) {
        this.filePath = filePath;
        Utils.log ("AudioEncoder  setFilePath  filePath=" + filePath);
        isChange = true;
        bStopDecode = true;
    }

    @TargetApi (Build.VERSION_CODES.JELLY_BEAN)
    public void decode () throws IOException, RuntimeException {

//		bab = new ByteArrayBuffer(10 * 4608);
        bStopDecode = false;
        // 解码器从这里面读取数据
        extractor = new MediaExtractor ();
        Utils.log ("filePath = " + filePath);
        extractor.setDataSource (filePath);
        mediaFormat = extractor.getTrackFormat (0);

        // 发送samplerate
        int simpleRate = mediaFormat.getInteger (MediaFormat.KEY_SAMPLE_RATE);
        Utils.log ("simpleRate = " + simpleRate);
        //tcpClient.sendDecodeData ((simpleRate + "#").getBytes ());
//        tcpClient.sendDecodeData ((String.valueOf (simpleRate) + "#").getBytes ());

        // 获取音频信息
        mime = mediaFormat.getString (MediaFormat.KEY_MIME);

        // 根据音频信息获得解码器
        mediaCodec = MediaCodec.createDecoderByType (mime);

        /*
        mediaFormat.setInteger(MediaFormat.KEY_SAMPLE_RATE, 44100);
        mediaFormat.setInteger(MediaFormat.KEY_BIT_RATE, 2500000);
        mediaFormat.setInteger(MediaFormat.KEY_FRAME_RATE, 20);
        mediaFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Planar);
        mediaFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 1); //关键帧间隔时间 单位s
        mediaFormat.setInteger(MediaFormat.KEY_CHANNEL_COUNT, 1);
        */
        mediaCodec.configure (mediaFormat, null, null, 0/*MediaCodec.CONFIGURE_FLAG_ENCODE*/);//MediaCodec.CONFIGURE_FLAG_ENCODE 说明该组件是一个编码器
        mediaCodec.start ();
        inputBuffers = mediaCodec.getInputBuffers ();
        outputBuffers = mediaCodec.getOutputBuffers ();

        extractor.selectTrack (0);
        boolean sawInputEOS = false;
        boolean sawOutputEOS = false;
        int count = 0;
        info = new MediaCodec.BufferInfo ();
//		App.STOPLOCALMUSIC &&
        while (!bStopDecode && !sawOutputEOS) {
//			if (App.PAUSELOCALMUSIC) { // 如果用户点击了暂停
//				continue;
//			}
//            Utils.log (sawInputEOS == true ? "AudioEncoder  sawInputEOS  为true" : "AudioEncoder  sawInputEOS  为false");
            if (!sawInputEOS) {
                int inputBufIndex = 0;
                try {
//                    Utils.log ("AudioEncoder  dequeueInputBuffer 之前  inputBuffers的length==" + inputBuffers.length);
//                    Utils.log ("AudioEncoder  dequeueInputBuffer  time11111="+System.currentTimeMillis ());
                    inputBufIndex = mediaCodec.dequeueInputBuffer (10); //1000，表示等待10毫秒，负数表示一直等待
//                    Utils.log ("AudioEncoder  dequeueInputBuffer   inputBufIndex==" + inputBufIndex);
                } catch (Exception e) {
                    Utils.log ("AudioEncoder  捕获到异常 =" + e.getMessage ());
                }
                if (inputBufIndex >= 0) {
//                    Utils.log ("AudioEncoder  dequeueInputBuffer 之后  inputBuffers   length==" + inputBuffers.length);
                    ByteBuffer buffer = inputBuffers[inputBufIndex];
                    int sampleSize = extractor.readSampleData (buffer, 0);
                    long presentationTimeUs = 0;
                    Utils.log ("AudioEncoder  sampleSize  =" + sampleSize);
                    if (sampleSize < 0) {//sampleSize==-1说明歌曲解码结束
                        sawInputEOS = true;
                        sampleSize = 0;
                    } else {
                        presentationTimeUs = extractor.getSampleTime ();
                    }
//                    Utils.log ("AudioEncoder  presentationTimeUs  =" + presentationTimeUs);
                    mediaCodec.queueInputBuffer (inputBufIndex, 0, sampleSize,
                            presentationTimeUs,
                            sawInputEOS ? MediaCodec.BUFFER_FLAG_END_OF_STREAM : 0);
                    if (!sawInputEOS) {
                        extractor.advance ();
                    }
                }
            }
            final int res = mediaCodec.dequeueOutputBuffer (info, 10);
//            Utils.log ("AudioEncoder  dequeueOutputBuffer  下标为=" + res + ",outputBuffers length=" + outputBuffers.length);
            if (res >= 0) {
                int outputBufIndex = res;
                ByteBuffer buf = outputBuffers[outputBufIndex];
                final byte[] chunk = new byte[info.size];
                buf.get (chunk);
                try {
                    tcpClient.sendDecodeData (chunk);
                } catch (SocketException se){
                    tcpClient.closeSocket ();
                    Utils.log ("AndioEncoder  捕获到SocketException  se=" + se.getMessage ());
                    break;
//                    while(true) {
//                        Utils.log ("AndioEncoder  开始第  " + connectTime+" 次的重新连接");
//                        //TODO
//
//                        if (HandleCommandService.serverIp != null) {
//                            try {
//                                tcpClient.initSocketNext (HandleCommandService.serverIp);
//                                break;//说明重建连接成功，退出重连循环
//                            }catch (IOException e){
//                                connectTime++;
//                                if(connectTime<=3){
//                                    Utils.log ("AndioEncoder  连接失败后重新连接 ");
//                                    continue;
//                                }else {
//                                    Utils.log ("AndioEncoder  重连失败");
//                                    mHandler.sendEmptyMessage (App.SENDDECODEFAIL);
//                                    break;
//                                }
//
//                            }
//                        }
//                    }
//                    continue;//重连成功后，重新进行发送数据
                } catch (IOException e) {
                    mHandler.sendEmptyMessage (App.SENDDECODEFAIL);
                    Utils.log ("AndioEncoder 发送数据失败：" + e.getMessage ());
                    tcpClient.closeSocket ();
                    break;
                }

                buf.clear ();
                mediaCodec.releaseOutputBuffer (outputBufIndex, false);
                if ((info.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {//文件解码完成
                    Utils.log ("AudioEncoder 解码完成，准备退出");
                    mHandler.sendEmptyMessage (App.SENDDECODESUCCESS);
                    sawOutputEOS = true;
                    bStopDecode = true;
                }

            } else if (res == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
                Utils.log ("AudioEncoder INFO_OUTPUT_BUFFERS_CHANGED");
                outputBuffers = mediaCodec.getOutputBuffers ();

            } else if (res == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                Utils.log ("AudioEncoder INFO_OUTPUT_FORMAT_CHANGED");

            }

        }
        Utils.log ("AudioEncoder 循环解码退出");
        if (mediaCodec != null) {
            mediaCodec.stop ();
            mediaCodec.release ();
            mediaCodec = null;
        }
        if (extractor != null) {
            extractor.release ();
            extractor = null;
        }
        Utils.log ("AudioDecoder 解码结束");
    }

    public boolean isChange = false;
    private boolean isFinish = true;

    @Override
    public void run () {


        while (isFinish) {
            try {
                Thread.sleep (500);
            } catch (InterruptedException e) {
                Utils.log ("AudioEncoder 打断线程休眠异常");
            }

            if (isChange) {
                try {
                    if(HandleCommandService.serverIp!=null) {
                        tcpClient = new TCPRometeLocalClient ();
                        tcpClient.initSocket (HandleCommandService.serverIp, 1);
                    }
                }catch (IOException e){
                    mHandler.sendEmptyMessage (App.SENDDECODEFAIL);
                }
                try {

                    decode (); // 解码并循环发送
                    tcpClient.closeSocket ();
                    isChange = false;

                } catch (IOException e) {
                    Utils.log ("解析 = " + e.getMessage ());

                    //isFinish = false;
                } catch (RuntimeException ee) {
                    mHandler.sendEmptyMessage (App.DECODEFAIL);
                    //提示解码失败
                    Utils.log ("AudioEncoder RuntimeException");
                    isChange = false;
                }
            }
        }
        Utils.log ("AudioEncoder 线程结束！");
    }
}
