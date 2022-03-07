/**
 * @author Estefanía 
 * @versión v1.0
 * Last modification date: 17-06-2021
 */

package model.old.dataaccess;

import java.util.List;

import model.old.domain.Book;
import model.old.domain.ChapterBook;

public interface IChapterBookDAO{
    public List<ChapterBook> getChapterBooksListByBook(String urlFileBook);
    public ChapterBook getChapterBookByURLFile(String urlFileChapterBook);
    public boolean addChapterBook(ChapterBook chapterBook, Book book);
    public boolean updateChepterBook(ChapterBook newChapterBook, String oldUrlFile);
    public boolean deleteChapterBook(String urlFileChapterBook);
}
