package com.woovan.poker.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class MapList<K, V> implements Iterable<List<V>> {

	private final Map<K, List<V>> mapList = new LinkedHashMap<K, List<V>>();

    public V put(K key, V value) {
        List<V> values = mapList.get(key);

        if (values == null) {
            values = new ArrayList<V>();
            mapList.put(key, values);
        }

        values.add(value);
        return value;
    }
    
    public int size() {
    	return mapList.size();
    }
    
    public List<V> get(K key) {
        return mapList.get(key);
    }
    
    public boolean containsKey(K key) {
    	return mapList.containsKey(key);
    }

    public Set<K> keySet() {
        return mapList.keySet();
    }
    
    public Set<Map.Entry<K, List<V>>> entrySet() {
        return mapList.entrySet();
    }

    public Iterator<List<V>> iterator() {
        return mapList.values().iterator();
    }
    
    @Override
    public String toString() {
    	StringBuilder sb = new StringBuilder();
    	for (List<V> list : this) {
			sb.append(list.toString()).append("\r\n");
		}
    	return super.toString();
    }
    
}
