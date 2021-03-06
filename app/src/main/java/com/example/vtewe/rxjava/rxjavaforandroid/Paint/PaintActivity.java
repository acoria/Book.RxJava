package com.example.vtewe.rxjava.rxjavaforandroid.Paint;

import android.graphics.Paint;
import android.graphics.Path;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.MotionEvent;

import com.example.vtewe.rxjava.R;
import com.jakewharton.rxbinding2.view.RxView;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;

import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;

public class PaintActivity extends AppCompatActivity {

    CanvasPaintingView paintingView;
    BehaviorSubject<Integer> colorSubject;
    BehaviorSubject<Float> thicknessSubject = BehaviorSubject.createDefault(15f);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("The Canvas is your Oyster");
        setContentView(R.layout.activity_paint);


        //test
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String ip = inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
//            Log.e(LOG_TAG, ex.toString());
        }

        colorSubject = BehaviorSubject.createDefault(getResources().getColor(R.color.red));
        paintingView = findViewById(R.id.paint_view);

        ViewUtils.TouchDelta touchDelta = new ViewUtils.TouchDelta();
        paintingView.setOnTouchListener(touchDelta);
        PaintViewModel viewModel = new PaintViewModel(touchDelta.getObservable(), colorSubject, thicknessSubject);

        viewModel.getPaths()
                .subscribe(this::setPaths);

        RxView.clicks(findViewById(R.id.button_red))
                .subscribe(click -> colorSubject.onNext(getResources().getColor(R.color.red)));
        RxView.clicks(findViewById(R.id.button_blue))
                .subscribe(click -> colorSubject.onNext(getResources().getColor(R.color.blue)));
        RxView.clicks(findViewById(R.id.button_green))
                .subscribe(click -> colorSubject.onNext(getResources().getColor(R.color.green)));
        RxView.clicks(findViewById(R.id.button_white))
                .subscribe(click -> colorSubject.onNext(getResources().getColor(R.color.white)));
        RxView.clicks(findViewById(R.id.button_thin))
                .subscribe(click -> {
                    if(thicknessSubject.getValue() > 15f){thicknessSubject.onNext(thicknessSubject.getValue() - 15f);}
                });
        RxView.clicks(findViewById(R.id.button_fat))
                .subscribe(click -> thicknessSubject.onNext(thicknessSubject.getValue() + 15f));
        RxView.clicks(findViewById(R.id.button_empty_canvas))
                .subscribe(click -> viewModel.resetPaths());
    }

    private void setPaths(List<Pair<Path, Paint>> paths) {
        paintingView.setPaths(paths);
    }

}
