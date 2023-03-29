package com.miniproj.member.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class DBConnection {

	public static Connection dbConnect() throws NamingException, SQLException {
		Context initContext = new InitialContext();
		// 디렉토리 서비스에 의해 context.xml 파일의 객체를 얻어와(JNDI)
		Context envContext  = (Context)initContext.lookup("java:/comp/env");
		// 아래 이름의 태그를 찾아 그 부분으로 부터 Connection 객체를 얻어옴
		DataSource ds = (DataSource)envContext.lookup("jdbc/mySQL");
		Connection conn = ds.getConnection();
		
		System.out.println(conn.toString());
		
		return conn;
	}
	
	public static void dbClose(ResultSet rs, Statement stmt, Connection con) throws SQLException {
		rs.close();
		stmt.close();
		con.close();
		
	}
	
	public static void dbClose(Statement stmt, Connection con) throws SQLException {
		stmt.close();
		con.close();
		
	}

	public static void dbClose(Connection con) throws SQLException {
		con.close();
	}
}
