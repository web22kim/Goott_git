package com.miniproj.member.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.miniproj.board.dao.BoardDAO;
import com.miniproj.board.dao.BoardDAOImpl;
import com.miniproj.etc.PagingInfo;
import com.miniproj.member.controller.MemberFactory;
import com.miniproj.member.dao.MemberDAO;
import com.miniproj.member.dao.MemberDAOImpl;
import com.miniproj.vodto.MemberDTO;
import com.miniproj.vodto.MemberPointVo;

public class MyPageMemberService implements MemberService {

	@Override
	public MemberFactory execute(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 마이페이지   
		
		BoardDAO bdao = BoardDAOImpl.getInstance();
		
		// 페이지 번호
		int pageNo = -1;
				if (req.getParameter("pageNo") == null || req.getParameter("pageNo").equals("")) {
					pageNo = 1;
				} else {
					pageNo = Integer.parseInt(req.getParameter("pageNo"));
				}
		
		// 
				int viewPostCntPerPage = 0;
				if (req.getParameter("viewPost") == null || req.getParameter("viewPost").equals("")) {
					viewPostCntPerPage = 3;
				} else 	{
					viewPostCntPerPage = Integer.parseInt( req.getParameter("viewPost"));
				}
				System.out.println("포인트 페이지 번호 : " + pageNo);	
				
		// 파라메터에서 얻기
//		String userId = req.getParameter("userId");
		
		// 세션에서 얻기
		HttpSession ses = req.getSession();
		MemberDTO loginMember = (MemberDTO)ses.getAttribute("loginMember");
		if (loginMember != null) {
			String userId = loginMember.getUserId();
			
			MemberDAO dao = MemberDAOImpl.getInatance();
			
			try {
				MemberDTO memberInfo = dao.getMemberInfo(userId); //회원정보 가져오기
				System.out.println(memberInfo.toString());
				
				// 페이지 번호와 전체 글의 갯수로.....페이징 처리를 하고,
				PagingInfo pi = getPagingInfo(pageNo, dao, viewPostCntPerPage, memberInfo.getUserId());
				
				// 페이징 처리한 쿼리문이 실행되도록 dao단을 호출해서,
				List<MemberPointVo> mpv = dao.getMemberPoint(userId, pi); // 포인트 내역 가져오기 (트랜잭션처리 불필요: select는 dml문(insert, update, delete)이 아니므로 rollback할 게 없다.
				
				
				// 회원정보와 포인트 내역을 request에 바인딩 (여러개 바인딩 가능)
				req.setAttribute("memberInfo", memberInfo);
				req.setAttribute("memberPoint", mpv);
				req.setAttribute("pagingInfo", pi);
				
				// 페이지 이동
				req.getRequestDispatcher("myPage.jsp").forward(req, resp) ;
				
			} catch (NamingException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}  
//		else {
//			
//		}
		
		
		return null;
	}
	
	private PagingInfo getPagingInfo(int pageNo, MemberDAO dao, int viewPostCntPerPage, String userId) throws NamingException, SQLException{
		// 페이징
		PagingInfo pi = new PagingInfo();
		
		// 실질적인 페이징에 필요한 변수들setting
		pi.setViewPostCntPerPage(viewPostCntPerPage);
		pi.setPageNo(pageNo);  // 현재 페이지 번호 세팅
		pi.setTotalPostCnt(dao.getTotalMemberPointCntByUserId("memberpoint", userId));  // 전체 글갯수 얻어와서 세팅
		pi.setTotalPageCnt(pi.getTotalPostCnt(), pi.getViewPostCntPerPage());
		pi.setStartRowInd(pi.getPageNo());
		
		// 페이징 블럭 처리를 위해 필요한 변수들 setting
		//현재 페이지가 속한 페이징 블럭
		pi.setPageBlockOfCurrentPage(pi.getPageNo());
		//현재 페이징 블럭시작번호
		pi.setStartNumOfCurrentPagingBlock(pi.getPageBlockOfCurrentPage());
		//현재 페이징 블럭의 끝번호
		pi.setEndNumOfCurrentPagingBlock(pi.getStartNumOfCurrentPagingBlock());
		
		System.out.println(pi.toString());
		
		return pi;
}
	

}
