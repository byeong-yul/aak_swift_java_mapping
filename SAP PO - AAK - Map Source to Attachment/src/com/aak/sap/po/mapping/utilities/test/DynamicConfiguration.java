package com.aak.sap.po.mapping.utilities.test;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.sap.aii.mapping.api.DynamicConfigurationKey;


/**
 * This class is used solely for testing purposes of java mappings outside of SAP PI
 */
public class DynamicConfiguration extends com.sap.aii.mapping.api.DynamicConfiguration {
	Map<DynamicConfigurationKey, String> map = new HashMap<DynamicConfigurationKey, String>();
	

	@Override
	public boolean containsKey(DynamicConfigurationKey dcKey) {
		if (map.containsKey(dcKey)) {
			return true;
		} else {
			return false;	
		}
	}

	
	@Override
	public String get(DynamicConfigurationKey dcKey) {
		if (map.containsKey(dcKey)) {
			return map.get(dcKey);
		} else {
			return null;	
		}
	}

	
	@Override
	public Iterator<DynamicConfigurationKey> getKeys() {
		return map.keySet().iterator();
	}

	
	@Override
	public Iterator<DynamicConfigurationKey> getKeys(String namespace) {
		Map<DynamicConfigurationKey, String> newMap = new HashMap<DynamicConfigurationKey, String>();
		DynamicConfigurationKey key                 = null;
		String value                                = null;
		
		Iterator<DynamicConfigurationKey> itr = getKeys();
		while(itr.hasNext()) {
			key = itr.next();
			value = get(key);
			
			if (namespace.equals(key.getNamespace())) {
				newMap.put(key, value);
			}
		}
		
		return newMap.keySet().iterator();
	}

	
	@Override
	public String put(DynamicConfigurationKey dcKey, String val) {
		String oldValue = null;
		
		if (map.containsKey(dcKey)) {
			oldValue = map.get(dcKey);
		}
				
		map.put(dcKey, val);
		
		return oldValue;
	}

	
	@Override
	public String remove(DynamicConfigurationKey dcKey) {
		String oldValue = null;
		
		if (map.containsKey(dcKey)) {
			oldValue = map.get(dcKey);
		}
				
		map.remove(dcKey);
		
		return oldValue;
	}

	
	@Override
	public void removeAll() {
		// TODO Auto-generated method stub
		
	}

	
	@Override
	public void removeNamespace(String arg0) {
		// TODO Auto-generated method stub
		
	}

	
	@Override
	public int size() {
		return map.size();
	}

	
	@Override
	public int size(String arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
}
