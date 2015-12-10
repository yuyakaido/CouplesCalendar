package com.yuyakaido.android.couplescalendar.util;

import com.yuyakaido.android.rruleprocessor.RRuleProcessor;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by yuyakaido on 8/14/15.
 */
public class RRuleUtils {

    /**
     * 繰り返し情報を持つ親イベントを引数で指定した範囲で展開する
     * @param rule
     * @param dtStart
     * @param from
     * @param to
     * @return
     */
    public static List<DateTime> getDates(String rule, DateTime dtStart, DateTime from, DateTime to) {
        List<DateTime> result = new ArrayList<>();
        List<Date> dates = RRuleProcessor.getDates(rule, dtStart.toDate(), from.toDate(), to.toDate());
        for (Date date : dates) {
            result.add(new DateTime(date));
        }
        return result;
    }

}
