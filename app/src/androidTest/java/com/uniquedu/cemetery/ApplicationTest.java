package com.uniquedu.cemetery;

import android.app.Application;
import android.content.Context;
import android.test.ApplicationTestCase;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
        Fresco.initialize(getContext());
    }
}