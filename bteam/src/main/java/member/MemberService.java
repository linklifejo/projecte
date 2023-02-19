package member;

import java.util.List;

public interface MemberService {
	//CRUD(Create, Read, Update, Delete)
	int member_insert(MemberVO vo);		
	List<MemberVO> member_list();		
	MemberVO member_info(String id);		
	int member_update(MemberVO vo);		
	int member_delete(String id); 			
}
