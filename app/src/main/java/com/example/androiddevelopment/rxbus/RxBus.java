package com.example.androiddevelopment.rxbus;


import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Created by Administrator on 2017/5/6.
 */
public class RxBus {

    private final Subject<Object, Object> bus;


    private RxBus() {
        bus = new SerializedSubject<>(PublishSubject.create());
    }

    private static class RxBusHolder {
        private static final RxBus sInstance = new RxBus();
    }

    public static RxBus getDefault() {
        return RxBusHolder.sInstance;

    }

    public void post(Object o) {
        bus.onNext(o);
    }

    public <T> Observable<T> tObservable(Class<T> eventType) {
        return bus.ofType(eventType);
    }



























}
