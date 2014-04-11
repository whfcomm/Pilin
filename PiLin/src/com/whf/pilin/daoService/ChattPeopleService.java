package com.whf.pilin.daoService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.whf.pilin.PiLinApplication;
import com.whf.pilin.constVar.CustomConst;
import com.whf.pilin.dao.MessageDAO;
import com.whf.pilin.entity.ChattingPeople;
import com.whf.pilin.entity.CommonMessage;

public class ChattPeopleService {
	
	private MessageDAO messageDAO;
	
	public ChattPeopleService(){
		
		messageDAO = (MessageDAO)PiLinApplication.getInstance().dabatases.get(CustomConst.DAO_MESSAGE);
		
	}
	/**
	 * 获取消息列表的成员
	 * @param uids
	 * @return
	 */
	public List<ChattingPeople> findAll(List<String> uids,String hostUid){
		List<ChattingPeople> cPeoples = new ArrayList<ChattingPeople>();
		for(String uid:uids){
			ChattingPeople people = findByUid(uid,hostUid);
			if(people!=null){
				cPeoples.add(people);
			}
			
		}
		return cPeoples;
		
	}
	
	public ChattingPeople findByUid(String uid,String hostUid){
		
		CommonMessage message = messageDAO.findLastMesgByUid(uid,hostUid);
		long count = messageDAO.findReceiveButNotReadByUid(uid,hostUid);
		ChattingPeople chattingPeople = null;
		if(message!=null){
			chattingPeople = new ChattingPeople(uid, message, count);
		}
		return chattingPeople;
		
	}
 	
}
