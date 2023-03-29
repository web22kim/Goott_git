package com.miniproj.member.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.miniproj.member.controller.MemberFactory;

public class LogoutMemberService implements MemberService {

	@Override
	public MemberFactory execute(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 로그아웃
		
		// 로그아웃을 요청한 클라이언트의 세션객체를 얻어옴
		 HttpSession ses = req.getSession(); 
		 ses.removeAttribute("loginMember");
		 ses.invalidate(); // 세션객체 만료. --> 새로운 세션이 생성
		 
		 MemberFactory mf = MemberFactory.getInstance();
		 mf.setRedirect(true);
		 mf.setWhereisgo("../index.jsp");
		 
		return mf;
	}

}
