package com.hengxin.platform.fund.service.atom.comparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.hengxin.platform.fund.dto.biz.req.atom.TransferInfo;

public class ComparatorTransferInfo implements Comparator<TransferInfo> {

	@Override
	public int compare(TransferInfo obj1, TransferInfo obj2) {
		String userId1 = obj1.getUserId();
		String userId2 = obj2.getUserId();
		return userId1.compareTo(userId2);
	}
	
	public static void main(String[] args){
		
		List<TransferInfo> list = new ArrayList<TransferInfo>();
		TransferInfo info1 = new TransferInfo();
		info1.setUserId("1234");
		list.add(info1);
		TransferInfo info2 = new TransferInfo();
		info2.setUserId("AAAA");
		list.add(info2);
		TransferInfo info3 = new TransferInfo();
		info3.setUserId("erww");
		list.add(info3);
		TransferInfo info4 = new TransferInfo();
		info4.setUserId("0987");
		list.add(info4);
		TransferInfo info5 = new TransferInfo();
		info5.setUserId("1234");
		list.add(info5);
		
		System.out.println(new ComparatorTransferInfo().compare(info2, info2));
		
		System.out.println("排序前.....");
		for(TransferInfo info:list){
			System.out.println(info.getUserId());
		}
		
		Collections.sort(list, new ComparatorTransferInfo());
		System.out.println("排序后.....");
		for(TransferInfo info:list){
			System.out.println(info.getUserId());
		}
	}

}
