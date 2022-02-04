/**
 * @author Estefanía 
 * @versión v1.0
 * Last modification date: 17-06-2021
 */

package sgp.ca.businesslogic;

import java.util.List;
import sgp.ca.domain.Book;
import sgp.ca.domain.ChapterBook;

public interface IChapterBookDAO{
    public List<ChapterBook> getChapterBooksListByBook(String urlFileBook);
    public ChapterBook getChapterBookByURLFile(String urlFileChapterBook);
    public boolean addChapterBook(ChapterBook chapterBook,Book book);
    public boolean updateChepterBook(ChapterBook newChapterBook, String oldUrlFile);
    public boolean deleteChapterBook(String urlFileChapterBook);
}
