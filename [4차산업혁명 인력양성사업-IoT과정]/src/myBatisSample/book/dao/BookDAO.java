package myBatisSample.book.dao;

import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import myBatisSample.book.vo.BookVO;

public class BookDAO {

	private static BookDAO instance;
	private SqlSessionFactory factory;

	// Singleton pattern
	public static BookDAO getInstance() {
		if (instance == null) {
			synchronized (BookDAO.class) {
				instance = new BookDAO();
			}
		}
		return instance;
	}

	private BookDAO() {
		try {
			Reader reader = Resources.getResourceAsReader("myBatisConfig/myBatisConfig.xml");
			factory = new SqlSessionFactoryBuilder().build(reader);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	public String select(String bisbn) {
		
		SqlSession sqlSession = factory.openSession();
		String btitle = sqlSession.selectOne("BookSQL.getBookTitle",bisbn);
		sqlSession.close();
		return btitle;
		
	}
	
	public List<BookVO> selectByKeyword(String keyword) {
		SqlSession sqlSession = factory.openSession();
		List<BookVO> result = sqlSession.selectList("BookSQL.getBooksKeyword",
				"%" + keyword + "%");
		sqlSession.close();
		return result;
	}

	public List<Map<String,String>> selectByKeywordMap(String keyword) {
		SqlSession sqlSession = factory.openSession();
		List<Map<String,String>> result = sqlSession.selectList("BookSQL.getBooksKeywordMap",
				"%" + keyword + "%");
		sqlSession.close();
		return result;
	}
	
	public int delete() {
		
		SqlSession sqlSession = factory.openSession();
		int num = sqlSession.delete("BookSQL.deleteBook");
		sqlSession.close();
		return num;
		
	}
	

	
	
}
