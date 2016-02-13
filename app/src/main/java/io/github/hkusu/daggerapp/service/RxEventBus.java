package io.github.hkusu.daggerapp.service;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

public class RxEventBus {
    private final Subject<Object, Object> subject = new SerializedSubject<>(PublishSubject.create());

    public <T> Subscription onEvent(Class<T> clazz, Action1<T> handler) {
        return subject
                .ofType(clazz)
                .subscribe(handler);
    }

    public <T> Subscription onEventMainThread(Class<T> clazz, Action1<T> handler) {
        return subject
                .ofType(clazz)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(handler);
    }

    public void post(Object event) {
        subject.onNext(event);
    }
}