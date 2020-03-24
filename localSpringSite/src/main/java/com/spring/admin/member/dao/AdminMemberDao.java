package com.spring.admin.member.dao;

import java.util.List;
import java.util.Map;

public interface AdminMemberDao {
	public List<MemberVO> memberList(MemberVO mvo);
	public Map<String, Integer> memberAgeList();
	public Map<String, Integer> memberGenderList();
}
