package com.spring.client.member.controller;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.spring.client.login.service.LoginService;
import com.spring.client.login.vo.LoginVO;
import com.spring.client.member.service.MemberService;
import com.spring.client.member.vo.MemberVO;

@Controller
@RequestMapping(value = "/member")
public class MemberController {
	private Logger log = LoggerFactory.getLogger(MemberController.class);
	@Autowired
	private MemberService memberService;

	@Autowired
	private LoginService loginService;

	/******************************************************
	 * 회원 가입 폼
	 ******************************************************/
	@RequestMapping(value = "/join.do", method = RequestMethod.GET)
	public String joinForm(Model model) {
		log.info("join.do get 방식에 의한 메서드 호출 성공");
		return "member/join";
	}

	/******************************************************
	 * 사용자 아이디 중복 체크 메서드
	 ******************************************************/
	@ResponseBody
	@RequestMapping(value = "/userIdConfirm.do", method = RequestMethod.POST)
	public String userIdConfirm(@RequestParam("userId") String userId) {
		int result = memberService.userIdConfirm(userId);
			return result+"";
	}
	
	//회원 가입 처리
	@RequestMapping(value = "/join.do", method = RequestMethod.POST)
	public ModelAndView memberInsert(@ModelAttribute MemberVO mvo) {
		log.info("join.do post 방식에 의한 메서드 호출 성공");
		ModelAndView mav = new ModelAndView();

		int result = 0;
		result = memberService.memberInsert(mvo);
		System.out.println(result);
		switch (result) {
		case 1:
			mav.addObject("errCode", 1); // userId already exist
			mav.setViewName("member/join");
			break;
		case 3:
			mav.addObject("errCode", 3); // userId already exist
			mav.setViewName("member/join_success");
			// success to add new member; move to login page
			break;
		default:
			mav.addObject("errCode", 2); // failed to add new member
			mav.setViewName("member/join");
			break;
		}
		return mav;
	}

	@RequestMapping(value = "/modify.do", method = RequestMethod.GET)
	public ModelAndView memberModify(HttpSession session) {
		log.info("modify.do get 방식에 의한 메서드 호출 성공");
		ModelAndView mav = new ModelAndView();

		LoginVO login = (LoginVO) session.getAttribute("login");

		if (login == null) {
			mav.setViewName("member/login");
			return mav;
		}

		MemberVO vo = memberService.memberSelect(login.getUserId());
		mav.addObject("member", vo);
		mav.setViewName("member/modify");
		return mav;
	}

	@RequestMapping(value = "/modify.do", method = RequestMethod.POST)
	public ModelAndView memberModifyProcess(@ModelAttribute("MemberVO") MemberVO mvo, HttpSession session) {
		log.info("modify.do post 방식에 의한 메서드 호출 성공");
		ModelAndView mav = new ModelAndView();

		LoginVO login = (LoginVO) session.getAttribute("login");

		if (login == null) {
			mav.setViewName("member/login");
			return mav;
		}

		mvo.setUserId(login.getUserId());
		MemberVO vo = memberService.memberSelect(mvo.getUserId());
		if (loginService.loginSelect(mvo.getUserId(), mvo.getOldUserPw()) == null) {
			mav.addObject("errCode", 1);
			mav.addObject("member", vo);
			mav.setViewName("member/modify");
			return mav;
		}
		if (memberService.memberUpdate(mvo)) {
			mav.setViewName("redirect:/member/logout.do");
			return mav;
		} else {
			mav.addObject("errCode", 2);
			mav.addObject("member", vo);
			mav.setViewName("member/modify");
			return mav;
		}

	}

	@RequestMapping("/delete.do")
	public ModelAndView memberDelete(HttpSession session) {
		log.info("delete.do get 방식에 의한 메서드 호출 성공");

		ModelAndView mav = new ModelAndView();
		LoginVO login = (LoginVO) session.getAttribute("login");

		if (login == null) {
			mav.setViewName("member/login");
			return mav;
		}

		int errCode = memberService.memberDelete(login.getUserId());
		switch (errCode) {
		case 2:
			mav.setViewName("redirect:/member/logout.do");
			break;
		case 3:
			mav.addObject("errCode", 3);
			mav.setViewName("member/login");
			break;
		}
		return mav;
	}

}
