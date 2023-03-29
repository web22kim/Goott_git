package com.miniproj.member.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.miniproj.member.controller.MemberFactory;

public interface MemberService {
	public abstract MemberFactory execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException ;
}
