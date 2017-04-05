package com.red;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test {
	
	public static void main(String[] args) {
		long totalMoney = 1000;
		int personNum = 10;
	    int min_money = 1;
	    List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	    for (int i = 0; i < personNum - 1; i++) {
	      int j = i + 1;
	      long safe_money = Math.max((totalMoney - (personNum - j) * min_money) / (personNum - j), 1L);
	      long tmp_money = Math.max((long)((Math.random() * (safe_money * 100L - min_money * 100) + min_money * 100) / 100), 1L);

	      totalMoney -= tmp_money;

	      Map<String, Object> map = new HashMap<String, Object>();
	      map.put("index", Integer.valueOf(j));
	      map.put("amount", Long.valueOf(tmp_money));
	      map.put("remain", Long.valueOf(totalMoney));
	      list.add(map);
	      System.out.println(tmp_money);
	    }

	    Map<String, Object> lastMap = new HashMap<String, Object>();
	    lastMap.put("index", Integer.valueOf(personNum));
	    lastMap.put("amount", Long.valueOf(totalMoney));
	    lastMap.put("remain", Integer.valueOf(0));
	    list.add(lastMap);
	}
	
}
