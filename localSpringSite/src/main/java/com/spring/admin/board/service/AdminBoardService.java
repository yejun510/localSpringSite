package com.spring.admin.board.service;

import java.util.List;

public interface AdminBoardService {
	public List<BoardVO> boardList(BoardVO bvo);
	public int boardListCnt(BoardVO bvo);

}
