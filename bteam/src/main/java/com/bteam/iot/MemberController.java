package com.bteam.iot;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import com.google.gson.Gson;

import member.MemberServiceImpl;
import member.MemberVO;
@Controller
public class MemberController {
	@Autowired private MemberServiceImpl service;
		
	//신규회원등록처리 요청
	@RequestMapping("/insert.me")
	public String insert(MemberVO vo) {
		//화면에서 입력한 정보로  DB에 신규삽입저장한다.
		service.member_insert(vo);
		//응답화면연결 - 회원목록
		return "redirect:list.me";
	}
	
	
	//신규회원등록화면 요청
	@RequestMapping("/new.me")
	public String member() {
		return "member/new";
	}
	
	//선택한 회원정보 수정저장처리 요청
	@RequestMapping("/update.me")
	public String update(MemberVO vo) {
		//화면에서 변경입력한 정보를 DB에 변경저장한다
		service.member_update(vo);
		//응답화면연결-고객정보
		return "redirect:info.me?no=" + vo.getId();
	}
	
	
	
	//선택한 회원정보 수정화면 요청
	@RequestMapping("/modify.me")
	public String modify(Model model, String id) {
		//선택한 회원정보를 DB에서 조회해와
		MemberVO vo = service.member_info(id);
		//회원수정화면에서 조회한 정보를 출력할 수 있도록 Model에 담는다
		model.addAttribute("vo", vo);
		//응답화면연결 - 회원수정
		return "member/modify";
	}
	
	
	//선택한 회원정보 삭제처리 요청
	@RequestMapping("/delete.me")
	public String delete(String id) {
		//선택한 회원정보를 DB에서 삭제
		service.member_delete(id);
		//응답화면연결 - 회원목록
		return "redirect:list.me";
	}
	
	
	//선택한 회원정보화면 요청
	@RequestMapping("/info.me")
	public String info(String id, Model model) {
		//해당 회원정보를 DB에서 조회해온다
		MemberVO vo = service.member_info(id);
		//화면에 출력할 수 있도록 Model에 attribute로 담는다
		model.addAttribute("vo", vo);
		
		//응답화면연결
		return "member/info";
	}
	
	
	//회원목록화면 요청
	@RequestMapping("/list.me")
	public String list(Model model, HttpSession session ) {
		session.setAttribute("category", "me");
		List<MemberVO> list = service.member_list();
		model.addAttribute("list", list);
		return "member/list";
	}
	
	@RequestMapping("/anJoin" )
	public String anJoin(HttpServletRequest req,MultipartRequest mReq, Model model) {
		
		String data = (String) req.getParameter("param");
		System.out.println("data : " + data);
		MemberVO vo = new Gson().fromJson(data, MemberVO.class);
		MultipartFile file = mReq.getFile("file");
		String fileName = "";

		if(file != null) {
			fileName = file.getOriginalFilename();
			fileName = "My" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".jpg";
			makeDir(req);	
				
			if(file.getSize() > 0){			
				String realImgPath = req.getSession().getServletContext()
						.getRealPath("/resources/");
												
			 	try {
					file.transferTo(new File(realImgPath, fileName));										
				} catch (Exception e) {
					e.getMessage();
				} 
									
			}else{
				fileName = "FileFail.jpg";
				String realImgPath = req.getSession().getServletContext()
						.getRealPath("/resources/" + fileName);
				System.out.println(fileName + " : " + realImgPath);
						
			}		
		}
		
		vo.setProfile(fileName);
		service.member_insert(vo);
		String id = vo.getId();
		vo = service.member_info(id);
		
		model.addAttribute("vo", vo); 	
		return "member/anList";
	}	
	

	
	@ResponseBody @RequestMapping(value="/anLogin", produces="text/plain; charset=utf-8" )
	public String anLogin(HttpServletRequest req, Model model) {

		String id = (String) req.getParameter("id");
		String pw = (String) req.getParameter("pw");
		System.out.println("id : " + id + ", pw : " + pw);
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("id", id);
		map.put("pw", pw);

		MemberVO list = service.member_info(map);
		
		if( list != null ) {
			
			Gson gson = new Gson();
			return gson.toJson( (MemberVO)list );
			
		}else {
			
			return "아이디 또는 페스워드 확인";
			
		}		
		
	}	
	
	@ResponseBody @RequestMapping(value="/selectMembers", produces="text/plain; charset=utf-8" )
	public String selectMembers(HttpServletRequest req, Model model) {
		
		ArrayList<MemberVO> list = (ArrayList<MemberVO>)service.member_list();
		Gson gson = new Gson();
		return gson.toJson( list );		
		
	}	
	
	@ResponseBody @RequestMapping(value="/deleteMember", produces="text/plain; charset=utf-8" )
	public String deleteMember(HttpServletRequest req, Model model) {
	
		String id = (String) req.getParameter("id");		
		service.member_delete(id);
		
		ArrayList<MemberVO> list = (ArrayList<MemberVO>)service.member_list();
		Gson gson = new Gson();
		return gson.toJson( list );		
	}	

		
	public void makeDir(HttpServletRequest req){
		File f = new File(req.getSession().getServletContext()
				.getRealPath("/resources"));
		if(!f.isDirectory()){
			f.mkdir();
		}	
	}
	
	@RequestMapping(value = "/file.f", produces = "text/html;charset=utf-8")
	public String file(MultipartRequest mReq, HttpServletRequest req) {

		MultipartFile file = mReq.getFile("file");
		if(file != null) {
			String fileName = file.getOriginalFilename();
			System.out.println(fileName);
			fileName = "My" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".jpg";
			makeDir(req);	
				
			if(file.getSize() > 0){			
				String realImgPath = req.getSession().getServletContext()
						.getRealPath("/resources/");
				
				System.out.println( fileName + " : " + realImgPath);
				System.out.println( "fileSize : " + file.getSize());					
												
			 	try {
					file.transferTo(new File(realImgPath, fileName));										
				} catch (Exception e) {
					e.printStackTrace();
				} 
									
			}else{
				fileName = "FileFail.jpg";
				String realImgPath = req.getSession().getServletContext()
						.getRealPath("/resources/" + fileName);
				System.out.println(fileName + " : " + realImgPath);
						
			}			
		
		}				
		
		return "";
	}
	
	
	public String fileUpload(String category, MultipartFile file, HttpServletRequest request) {
		// D:\Study_Spring\Workspace\.metadata\.plugins\org...er.core\tmp1\wtpwebapps\iot\resources
		// String path =
		// request.getSession().getServletContext().getRealPath("resources");
		// d://app/iot
		String path = "c://app" + request.getContextPath();

		// upload/profile/2022/08/23
		String upload = "/upload/" + category + new SimpleDateFormat("/yyyy/MM/dd").format(new Date());
		// D:\Study_Spring\Wo....\iot\resources/upload/profile/2022/08/23
		path += upload;
		
		File folder = new File(path);
		if (!folder.exists())
			folder.mkdirs();

		// dafqer32-g38fgfa_abc.png
		String uuid = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

		try {
			file.transferTo(new File(path, uuid));
		} catch (Exception e) {
		}

		// /upload/profile/2022/08/23/dafqer32-g38fgfa_abc.png
		return upload + "/" + uuid;
	}
}
