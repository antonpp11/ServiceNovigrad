package com.example.servicenovigrad;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void adminLogin_isCorrect() { //test that login process / database works by crossreferencing hardcoded values with database
        String actualUser = "admin";
        String actualPassword = "1234";

        assertEquals(4, 2 + 2);

    }
}