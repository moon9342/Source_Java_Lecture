package myBatisSample.book;

import java.util.List;
import java.util.Map;
import java.util.Set;

import myBatisSample.book.service.BookService;
import myBatisSample.book.vo.BookVO;

public class Main {

	public static void main(String[] args) {
		BookService service = new BookService();
		// 책 제목 : C로 구현한 알고리즘, ISBN : 89-7914-063-0
		
		// 1. 책 ISBN으로 1권의 책 select
		String btitle = service.getBookTitleByISBN("89-7914-063-0");
		System.out.println("책 제목 : " + btitle);
		
		// 2. 책 제목에 대한 keyword를 입력으로 다수의 책 select
		List<BookVO> blist = service.getBooksByKeyword("java");
		for(BookVO book : blist) {
			System.out.println("책 정보 : " + book);	
		}
		// 3. 모든 책 삭제
		//int num = service.deleteBookAll();
		//System.out.println("삭제된 책 수 : " + num);
		
		// 4. Map형태의 데이터 가져오기
		List<Map<String,String>> result = service.getBooksByKeywordMap("여행");
		for(Map<String,String> k : result) {
			System.out.println("책 ISBN : " + k.get("bisbn") + 
					", 책 제목 : " + k.get("btitle"));	
		}
	}

}