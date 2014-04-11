package com.whf.pilin.dao;

import java.util.List;

import com.whf.pilin.entity.CommonMessage;

public interface MessageDAO extends BaseDAO{
	/**
	 * 保存聊天信息
	 * @param message
	 */
	public long save(CommonMessage message,String hostUid);
	/**
	 * 根据用户ID获取跟用户的聊天信息
	 * @param page 页码
	 * @param pageSize 每页数据
	 * @param uid  用户ID
	 * @return
	 */
	public List<CommonMessage> findMessageByUid(int page,int pageSize,String uid,String hostUid);
	/**
	 * 找出与某人聊天，没有阅读对方发送的消息数量
	 * @param 对方uid
	 * @return
	 */
	public long findReceiveButNotReadByUid(String uid,String hostUid);
	/**
	 * 查找与某人对话的最后一条信息
	 * @param uid
	 * @return
	 */
	public CommonMessage findLastMesgByUid(String uid,String hostUid);
	/**
	 * 根据ROWID查找
	 * @param rowid
	 * @return
	 */
	public CommonMessage findByRownum(long rowid,String uid); 
	/**
	 * 根据ID修改
	 * @param id
	 */
	public void updateById(int id,String uid,int state);
	/**
	 * 通过ROWID更新信息状态
	 * @param rowid rowid
	 * @param uid 表的用户ID
	 */
	public void updateStateByRowid(long rowid,String uid , int state);
	/**
	 * 获取能找到的最大的页面
	 * @param uid  用户id
	 * @param pageSize 每页大小
	 * @return
	 */
	public int getMaxPage(String uid,int pageSize,String hostUid);
}
