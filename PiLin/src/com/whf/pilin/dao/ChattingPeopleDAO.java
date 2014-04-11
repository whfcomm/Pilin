package com.whf.pilin.dao;



public interface ChattingPeopleDAO extends BaseDAO{

	public void save(String uid,String hostUid);
	
	public void delete(String uid,String hostUid);
	
	//public List<String> findByUids(String [] uid);
	
	public boolean peopleChatting(String uid,String hostUid);
	
}
