package com.caijia.widget.selectdaterange;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);

        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < 968; i++) {
            Entry entry = new Entry(180 + Math.random() * 20, 2 + Math.random() + 1);
            entries.add(entry);
        }

        Collections.sort(entries, new Comparator<Entry>() {
            @Override
            public int compare(Entry o1, Entry o2) {
                if (o1.getX() == o2.getX()) {
                    return 0;
                }
                return o1.getX() > o2.getX() ? 1 : -1;
            }
        });

        List<Entry> reduce = SeriesReducer.reduce(entries, 0.1);
        for (Entry entry : reduce) {
            System.out.println(entry.toString());
        }
    }
}