package bbs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class BbsDAO {
	private Connection conn;
	private ResultSet rs;
	
	public BbsDAO(){
		try{
			String dbURL = "jdbc:mysql://localhost:3306/BBS?autoReconnect=true&useSSL=false&characterEncoding=UTF-8&serverTimezone=UTC";
			String dbID= "root";
			String dbPassword = "java";
			
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn=DriverManager.getConnection(dbURL, dbID, dbPassword);
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public String getDate(){
		String SQL = "select now()";
		
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				return rs.getString(1);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return ""; //데이터 베이스 오류
	}
	
	public int getNext(){
		String SQL = "select bbsID from bbs order by bbsID desc";
		
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				return rs.getInt(1)+1;
			}
			
			return 1; //첫번째 게시물인 경우
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return -1; //데이터 베이스 오류
	}
	
	public int write(String bbsTitle, String userID, String bbsContent){
		String SQL = "insert into bbs values (?,?,?,?,?,?)";

		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, getNext());
			pstmt.setString(2, bbsTitle);
			pstmt.setString(3, userID);
			pstmt.setString(4, getDate());
			pstmt.setString(5, bbsContent);
			pstmt.setInt(6, 1);
			
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return -1; //데이터 베이스 오류
	}
	
	//리스트를 10개씩 받아온다.
	public List<Bbs> getList(int pageNumber){
		String SQL = "select * from bbs where bbsID < ? and bbsAvailable = 1 order by bbsID desc limit 10";
		
		List<Bbs> list = new ArrayList<Bbs>();
		
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, getNext() - (pageNumber - 1)*10);
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				Bbs bbs = new Bbs();
				
				bbs.setBbsID(rs.getInt(1));
				bbs.setBbsTitle(rs.getString(2));
				bbs.setUserID(rs.getString(3));
				bbs.setBbsDate(rs.getString(4));
				bbs.setBbsContent(rs.getString(5));
				bbs.setBbsAvailable(rs.getInt(6));
				
				list.add(bbs);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	//다음 페이지가 있는지 확인 해주는 메서드
	public boolean nextPage(int pageNumber){
		String SQL = "select * from bbs where bbsID < ? and bbsAvailable = 1";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, getNext() - (pageNumber - 1)*10);
			rs = pstmt.executeQuery();

			if(rs.next()){
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}
	
	//게시판의 글 불러오기
	public Bbs getBbs(int bbsID){
		String SQL = "select * from bbs where bbsID = ?";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, bbsID);
			rs = pstmt.executeQuery();

			if(rs.next()){
				Bbs bbs = new Bbs();
				
				bbs.setBbsID(rs.getInt(1));
				bbs.setBbsTitle(rs.getString(2));
				bbs.setUserID(rs.getString(3));
				bbs.setBbsDate(rs.getString(4));
				bbs.setBbsContent(rs.getString(5));
				bbs.setBbsAvailable(rs.getInt(6));
				
				return bbs;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	//업데이트
	public int update(int bbsID, String bbsTitle, String bbsContent){
		String SQL = "update bbs set bbsTitle=?, bbsContent=? where bbsID = ?";

		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, bbsTitle);
			pstmt.setString(2, bbsContent);
			pstmt.setInt(3, bbsID);
			
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return -1; //데이터 베이스 오류
	}
	
	public int delete(int bbsID){
		String SQL = "update bbs set bbsAvailable = 0 where bbsID = ?";

		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, bbsID);
			
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return -1; //데이터 베이스 오류
	}
}