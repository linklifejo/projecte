<%@ page import="java.util.List"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<%@ page import="com.google.gson.Gson"%>
<%@ page import="org.json.simple.JSONObject"%>
<%@ page import="member.MemberVO"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>  
    

<%	
	/* 안드로이드에게 데이터베이스에 있는 멤버 모두를 json 형태로 보냈습니다 */
	
	Gson gson = new Gson();
	String json = gson.toJson((List<MemberVO>)request.getAttribute("list"));
  
    
   out.println(json);  	
%>
