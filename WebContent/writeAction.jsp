<%@page import="bbs.BbsDAO"%>
<%@page import="java.io.PrintWriter"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 
<%request.setCharacterEncoding("UTF-8");%>
<jsp:useBean id="bbs" class="bbs.Bbs" scope="page"/>
<jsp:setProperty property="bbsTitle" name="bbs"/>
<jsp:setProperty property="bbsContent" name="bbs"/>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>JSP 게시판 웹 사이트</title>
</head>
<body>
	<%	
		
		//---------로그인 되어있을 경우 ------- 접속 못하게 막아준다.
		String userID = null;
		
		if(session.getAttribute("userID") != null){
			userID = (String)session.getAttribute("userID");
		}
		
		if(userID == null){
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('글을 쓰기 위해 로그인을 해주세요')");
			script.println("location.href = 'login.jsp'");
			script.println("</script>");
		}else{
			
			if(bbs.getBbsTitle() == null || bbs.getBbsContent() == null){
				PrintWriter script = response.getWriter();
				script.println("<script>");
				script.println("alert('입력이 안된 사항이 있습니다..');");
				script.println("history.back();");
				script.println("</script>");			
			}else{
				BbsDAO bbsDAO = new BbsDAO();
				
				int result = bbsDAO.write(bbs.getBbsTitle(),userID, bbs.getBbsContent());
				
				if(result == -1){
					PrintWriter script = response.getWriter();
					script.println("<script>");
					script.println("alert('글쓰기에 실패하였습니다.');");
					script.println("history.back();");
					script.println("</script>");
					
				}else{
					PrintWriter script = response.getWriter();
					script.println("<script>");
					script.println("location.href = 'bbs.jsp';");
					script.println("</script>");
				}
			}	
			
		}
		//----------------------------------------------
	
		
		
	%>
</body>
</html>