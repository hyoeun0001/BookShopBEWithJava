package BS.spring_BS.service;

import BS.spring_BS.dto.BookDetailDto;
import BS.spring_BS.dto.BooksResponseDto;
import BS.spring_BS.jwt.JwtUtil;
import BS.spring_BS.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private static final Logger logger = LoggerFactory.getLogger(BookService.class);

    private final BookRepository bookRepository;

    @Transactional(readOnly = true)
    public Map<String, Object> getAllBooks(Integer categoryId, Boolean news, LocalDate oneMonthAgo, int limit, int currentPage) {

        int offset = limit * (currentPage - 1);

        List<Object[]> resultList = bookRepository.findBooksRaw(categoryId, news, oneMonthAgo, limit, offset);

        if (resultList.isEmpty()) {
            throw new IllegalArgumentException("Books 없음");
        }

        // DTO 변환 로직 추가
        List<BooksResponseDto> books = convertToBooksResponseDto(resultList);
        int totalCount = bookRepository.countBooks(categoryId, news, oneMonthAgo);

        Map<String, Object> response = new HashMap<>();
        response.put("books", books);
        response.put("pagination", Map.of(
                "currentPage", currentPage,
                "totalCount", totalCount
        ));

        return response;
    }

    @Transactional(readOnly = true)
    public BookDetailDto getBookDetail(Long bookId, Long memberId) {
        logger.info("추출된 memberId: {}", memberId);

        Object[] queryResultArray = bookRepository.findBookDetail(bookId, memberId);
        if (queryResultArray == null) {
            logger.warn("책 정보가 존재하지 않습니다 - bookId: {}", bookId);
            throw new IllegalArgumentException("책 정보를 찾을 수 없습니다.");
        }
        logger.info("쿼리 결과 길이: {}, 내용: {}", queryResultArray.length, Arrays.toString(queryResultArray));

        Object[] bookData;
        if (queryResultArray.length == 1 && queryResultArray[0] instanceof Object[]) {
            bookData = (Object[]) queryResultArray[0];
        } else {
            bookData = queryResultArray;
        }

        logger.info("추출된 bookData: {}", Arrays.toString(bookData));
        if (bookData.length < 16) {
            logger.error("쿼리 결과 필드 수가 부족합니다 - 기대: 16, 실제: {}", bookData.length);
            throw new IllegalArgumentException("쿼리 결과 배열의 형식이 예상과 다릅니다.");
        }

        try {
            return convertToBookDetailDto(bookData);
        } catch (Exception e) {
            logger.error("DTO 변환 중 에러 발생", e);
            throw new RuntimeException("책 정보를 처리하는 중 오류가 발생했습니다.", e);
        }
    }

    private List<BooksResponseDto> convertToBooksResponseDto(List<Object[]> resultList) {
        return resultList.stream()
                .map(obj -> new BooksResponseDto(
                        ((Number) obj[0]).longValue(),
                        (String) obj[1],
                        (String) obj[2],
                        (String) obj[3],
                        (String) obj[4],
                        (Integer) obj[5],
                        (obj[6] != null) ? ((java.sql.Date) obj[6]).toLocalDate() : null,
                        (String) obj[7],
                        (String) obj[8],
                        (String) obj[9],
                        (Integer) obj[10],
                        (String) obj[11],
                        ((Number) obj[12]).intValue(),
                        ((Number) obj[13]).intValue()
                ))
                .collect(Collectors.toList());
    }

    private BookDetailDto convertToBookDetailDto(Object[] bookData) {
        if (bookData.length < 16) {
            logger.error("Insufficient fields in query result - expected: 16, actual: {}", bookData.length);
            throw new IllegalStateException("Invalid query result format");
        }
        try {
            return new BookDetailDto(
                    ((Number) bookData[0]).longValue(),
                    (String) bookData[1],
                    (String) bookData[2],
                    (String) bookData[3],
                    (String) bookData[4],
                    ((Number) bookData[5]).intValue(),
                    (bookData[6] != null) ? java.sql.Date.valueOf(bookData[6].toString()).toLocalDate() : null,
                    (String) bookData[7],
                    bookData[8].toString(),
                    (String) bookData[9],
                    ((Number) bookData[10]).intValue(),
                    (String) bookData[11],
                    ((Number) bookData[12]).intValue(),
                    (String) bookData[13],
                    ((Number) bookData[14]).intValue(),
                    ((Number) bookData[15]).intValue()
            );
        } catch (Exception e) {
            logger.error("Error converting to BookDetailDto", e);
            throw new RuntimeException("Failed to process book detail", e);
        }
    }
}
