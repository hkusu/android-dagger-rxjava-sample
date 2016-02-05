package io.github.hkusu.daggerapp;

import android.content.Context;
import android.util.Log;

public class HogeSomeManager implements SomeManager {

    @Override
    public void hello(String name) {
        Log.d("dagger", "Hello! " + name);
    }
}
