package com.spring.admin.member.service;

import java.util.List;
import java.util.Map;

public interface AdminMemberService {
	public List<MemberVO> memberList(MemberVO bvo);
	public Map<String, Integer> memberAgeList();
	public Map<String, Integer> memberGenderList();

}
