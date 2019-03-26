package com.example.vtewe.rxjava;

import com.example.vtewe.rxjava.rxjavaforandroid.chapt10_unittests.Blackbox;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import io.reactivex.observers.TestObserver;

import static org.junit.Assert.assertEquals;

public class BlackboxUnitTest {

    Blackbox blackbox;

    @Before
    public void setup(){
        blackbox = new Blackbox();
    }

    @Test
    public void testSortList() {
        // Assemble
        TestObserver<List<Integer>> testObserver = new TestObserver();
        List<Integer> list = Arrays.asList(54, 1, 7);
        // Act
        blackbox.sortList(list).subscribe(testObserver);

        // Assert
        testObserver.assertValueCount(1);
        testObserver.assertComplete();

        List<Integer> sortedList = testObserver.values().get(0);
        assertEquals(3, sortedList.size());
        assertEquals(1, (int) sortedList.get(0));
        assertEquals(7, (int) sortedList.get(1));
        assertEquals(54, (int) sortedList.get(2));
    }

    @Test
    public void testSortListWithNullValue(){
        TestObserver<List<Integer>> testObserver = new TestObserver();

        blackbox.sortList(null).subscribe(testObserver);

        testObserver.assertError(NullPointerException.class);
        testObserver.assertNoValues();
    }

    @Test
    public void testSingleSplit(){
        String iAmOneWord = "giveMeOneString";
        TestObserver<String> testObserver = new TestObserver();

        blackbox.splitWords(iAmOneWord).subscribe(testObserver);

        testObserver.assertComplete();
        testObserver.assertValue(iAmOneWord);
        testObserver.assertValueCount(1);
    }

    @Test
    public void testSplit3Words(){
        String firstWord = "first";
        String secondWord = "second";
        String thirdWord = "third";

        String iAmToBeSplit = firstWord + " " + secondWord + " " + thirdWord;
        TestObserver<String> testObserver = new TestObserver();

        blackbox.splitWords(iAmToBeSplit).subscribe(testObserver);

        testObserver.assertComplete();
        testObserver.assertValues(firstWord,secondWord,thirdWord);
    }

    @Test
    public void testNullSplit(){
        TestObserver<String> testObserver = new TestObserver();
        blackbox.splitWords("").subscribe(testObserver);
        testObserver.assertError(NullPointerException.class);
    }

    @Test
    public void testUncompletable(){
        String toBeEmitted = "emitMeButNeverComplete";
        TestObserver<String> testObserver = new TestObserver();
        blackbox.startUncompletable(toBeEmitted).subscribe(testObserver);

        testObserver.assertValue(toBeEmitted);
        testObserver.assertValueCount(1);
        testObserver.assertNotComplete();
    }
}

