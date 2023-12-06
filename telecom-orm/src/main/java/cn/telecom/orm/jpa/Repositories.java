package cn.telecom.orm.jpa;

import com.google.common.base.Strings;
import jakarta.persistence.criteria.Predicate;

import java.util.List;
import java.util.function.Supplier;

public interface Repositories {
    static void tryAddPredicate(Supplier<Predicate> supplier,
                                List<Predicate> list, String val) {
        if (Strings.isNullOrEmpty(val)) {
            return;
        }
        list.add(supplier.get());
    }
}
