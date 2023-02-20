package member;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class MemberDAO implements MemberService {
	@Autowired @Qualifier("hanul") private SqlSession sql;
	
	@Override
	public int member_insert(MemberVO vo) {
		return sql.insert("me.insert", vo);
	}

	@Override
	public List<MemberVO> member_list() {
		return sql.selectList("me.list");
	}

	@Override
	public MemberVO member_info(String id) {
		return sql.selectOne("me.info", id);
	}

	@Override
	public int member_update(MemberVO vo) {
		return sql.update("me.update", vo);
	}

	@Override
	public int member_delete(String id) {
		return sql.delete("me.delete", id);
	}

	@Override
	public MemberVO member_info(HashMap<String, String> vo) {
		// TODO Auto-generated method stub
		return sql.selectOne("me.myinfo", vo);
	}

}
