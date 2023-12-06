package cn.telecom.spring.bean;

import cn.telecom.commons.data.KVPair;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DictHandler extends Bean {
    Page<KVPair<String, String>> getDict(Pageable pageable);
}
