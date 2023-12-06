package cn.telecom.commons.data;

import lombok.Data;

import java.io.Serializable;

@Data
public class KVPair<K, V> implements Serializable {
    private K k;
    private V v;

    public static <K, V> KVPair<K, V> of(K k, V v) {
        KVPair<K, V> kvPair = new KVPair<>();
        kvPair.setK(k);
        kvPair.setV(v);
        return kvPair;
    }
}
