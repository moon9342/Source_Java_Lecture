package myBatisSample.book.service;

import java.util.List;
import java.util.Map;

import myBatisSample.book.dao.BookDAO;
import myBatisSample.book.vo.BookVO;

public class BookService {

	public String getBookTitleByISBN(String bisbn) {
		
		BookDAO dao = BookDAO.getInstance();
		String btitle = dao.select(bisbn);
		return btitle;
		
	}

	public int deleteBookAll() {
		
		BookDAO dao = BookDAO.getInstance();
		int num = dao.delete();
		return num;
		
	}
	
	public List<BookVO> getBooksByKeyword(String keyword) {
		BookDAO dao = BookDAO.getInstance();
		List<BookVO> result = dao.selectByKeyword(keyword);
		return result;
	}

	public List<Map<String,String>> getBooksByKeywordMap(String keyword) {
		BookDAO dao = BookDAO.getInstance();
		List<Map<String,String>> result = dao.selectByKeywordMap(keyword);
		return result;
	}
	
}
