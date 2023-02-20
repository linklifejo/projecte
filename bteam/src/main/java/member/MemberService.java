package member;

import java.util.HashMap;
import java.util.List;

public interface MemberService {
	//CRUD(Create, Read, Update, Delete)
	int member_insert(MemberVO vo);		
	List<MemberVO> member_list();		
	MemberVO member_info(String id);		
	MemberVO member_info(HashMap<String, String> vo);		
	int member_update(MemberVO vo);		
	int member_delete(String id); 			
}
