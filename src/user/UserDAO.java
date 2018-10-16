package user;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {
	
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	public UserDAO(){
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
	
	public int login(String userID, String userPassword){
		String SQL = "select userPassword from user where userID=?";
		
		try {
			
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userID);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				if(rs.getString(1).equals(userPassword)){
					return 1; //로그인 성공
				}else{
					return 0; // 로그인 불일치
				}
			}
			
			return -1; //아이디가 없다고 알려줌
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return -2; // 데이트 베이스 오류
	}
	
	
	public int join(User user){
		String SQL = "insert into user values (?,?,?,?,?)";
		
		try {
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, user.getUserID());
			pstmt.setString(2, user.getUserPassword());
			pstmt.setString(3, user.getUserName());
			pstmt.setString(4, user.getUserGender());
			pstmt.setString(5, user.getUserEmail());
			
			return pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return -1; //데이터 베이스 오류
	}
}
