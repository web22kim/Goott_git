package com.miniproj.member.service;

import java.io.IOException;
import java.sql.SQLException;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.miniproj.error.CommonException;
import com.miniproj.member.controller.MemberFactory;
import com.miniproj.member.dao.MemberDAOImpl;
import com.miniproj.vodto.LoginDTO;
import com.miniproj.vodto.MemberDTO;

public class LoginMemberService implements MemberService {

	@Override
	public MemberFactory execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException  {
//		System.out.println("회원 로그인 처리 하자~");
		MemberFactory mf = MemberFactory.getInstance();
		
		String userId = req.getParameter("userId");
		String pwd = req.getParameter("pwd");
		
		LoginDTO dto = new LoginDTO(userId, pwd);
		System.out.println(dto.toString());
		
		try {
			MemberDTO loginMember = MemberDAOImpl.getInatance().loginWithTransaction(dto); // 로그인한 멤버
			if (loginMember != null) {  // 로그인 성공
				
				// 로그인한 유저의 정보를 세션 객체에 바인딩 (브라우저닫기전까지 살아있으니까 세션 이용)
				HttpSession ses =  req.getSession();
				ses.setAttribute("loginMember", loginMember);
				
//				req.getRequestDispatcher("../index.jsp").forward(req, resp); // 설명: 
				
				mf.setRedirect(true);
				mf.setWhereisgo("../index.jsp");
				
			} else {
				mf.setRedirect(true);
				mf.setWhereisgo("login.jsp?status=fail");
			}
		} catch (NamingException | SQLException e) {
			if (e instanceof NamingException) {
				// NamingException은 개발자 실수이기 때문에 개발자만 보도록 공통 에러페이지(error.jsp)를 만들었고,
				// 에러 정보를 error.jsp로 바인딩하여 error.jsp페이지에서 에러 정보를 출력하였다. 
				// forward사용함.
//				e.printStackTrace();  // 개발자 잘못....
				CommonException ce = new CommonException(e.getMessage(), 99);
//				throw ce;  // 강제로 예외를 발생시킴
				
				ce.setErrorMsg(e.getMessage());  // 에러가 난 종류
				ce.setStackTrace(e.getStackTrace());  // 에러가 난 단계
				
				req.setAttribute("error",  ce);  // 에러 정보를 가진 CommonException 객체 바인딩
				req.getRequestDispatcher("../error.jsp").forward(req, resp); // 페이지 이동 (주소는 바뀌지않고 에러메시지는 출력됨)
				// (forwarding하므로, return mf하지 않음)
				
			} else if (e instanceof SQLException) {
				// SQL Exception은 대부분 실제 유저의 입력 오류로 인한 예외
				mf.setRedirect(true);
				mf.setWhereisgo("login.jsp?status=fail");
			}
		}
		
		
		return mf;
	}

}
