package com.spring.admin.board.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AdminBoardServiceImpl implements AdminBoardService{
	
	@Autowired
	private BoardDao boardDao;

	@Override
	public List<BoardVO> boardList(BoardVO bvo) {
		List<BoardVO> aList = null;
		
		// 페이지 세팅
		Paging.setPage(bvo);
		
		// 정렬에 대한 기본값 설정
		if(bvo.getOrder_by()==null) bvo.setOrder_by("b_num");
		if(bvo.getOrder_sc()==null) bvo.setOrder_se("DESC");
		
		if(!bvo.getKeyword().equals("")) {
			bvo.setStart_date("");
			bvo.setEnd_date("");
		}
		aList = boardDao.boardList(bvo);
		return aList;
	}

	@Override
	public int boardListCnt(BoardVO bvo) {
		int countNum = 0;
		countNum = boardDao.boardListCnt(bvo);
		return countNum;
	}

}
