package member;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService {
	@Autowired private MemberDAO dao;
	
	@Override
	public int member_insert(MemberVO vo) {
		return dao.member_insert(vo);
	}

	@Override
	public List<MemberVO> member_list() {
		return dao.member_list();
	}

	@Override
	public MemberVO member_info(String id) {
		return dao.member_info(id);
	}

	@Override
	public int member_update(MemberVO vo) {
		return dao.member_update(vo);
	}

	@Override
	public int member_delete(String id) {
		return dao.member_delete(id);
	}

	@Override
	public MemberVO member_info(HashMap<String, String> vo) {
		// 
		return dao.member_info(vo);
	}

}
