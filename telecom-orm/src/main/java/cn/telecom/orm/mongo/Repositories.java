package cn.telecom.orm.mongo;

import com.google.common.base.Strings;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.regex.Pattern;

public interface Repositories {
    static Pattern regex(String s) {
        return Pattern.compile("^.*" + s + ".*$", Pattern.CASE_INSENSITIVE);
    }

    static void tryAddCriteria(Query query, String k, String v) {
        if (Strings.isNullOrEmpty(v)) {
            return;
        }
        Criteria criteria = Criteria.where(k).is(v);
        query.addCriteria(criteria);
    }
}
