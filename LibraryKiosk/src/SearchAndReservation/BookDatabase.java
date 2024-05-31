package SearchAndReservation;

import CSVController.CSVReaderAndWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BookDatabase {
    private List<Book> books;

    public BookDatabase(String csvFilePath) {
        books = new ArrayList<>();
        loadBooksFromCSV(csvFilePath);
        System.out.println("총 도서 수: " + (books.size()-1));
    }

    private void loadBooksFromCSV(String csvFilePath) {
        try  {
            List<String[]> records = CSVReaderAndWriter.readCSV(csvFilePath);
            for (String[] line : records) {
                // 제목, 저자, ISBN, 예약, 예약 상태, 이미지 경로 여섯 가지 정보 추출
                if (line.length < 6) continue; // 잘못된 형식의 라인 건너뜀
                String title = line[0];          // 제목
                String author = line[1];         // 저자
                String isbn = line[2];           // ISBN
                String reservation = line[3];    // 예약 가능 여부
                boolean reserved = Boolean.parseBoolean(line[4]);    // 예약 상태
                String coverImagePath = line[5]; // 이미지 경로
                books.add(new Book(title, author, isbn, reservation, reserved, coverImagePath));
            }
            for (Book book : books) {
                System.out.println(book);
            }
        } catch (IOException e) {
            // IOException이 발생하면
            // 예외 처리
            e.printStackTrace();
        }
    }

    public List<Book> searchBooks(String query) { // 검색어를 매개변수로 받음
        List<Book> result = new ArrayList<>();    // 결과 리스트
        for (Book book : books) {  // books 리스트에 있는 모든 도서 순회
            // 대소문자 구분하지 않기 위해 소문자로 변환
            if (book.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                    book.getAuthor().toLowerCase().contains(query.toLowerCase())) {
                result.add(book);
            }
        }
        return result;
    }
}


