package epfl.sweng.test.cache;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import epfl.sweng.cash.CacheManager;
import epfl.sweng.quizquestions.QuizQuestion;
import junit.framework.TestCase;

/**
 * 
 * @author crazybhy
 * 
 */
public class CacheManagerTest extends TestCase {
	
	private String text = "A question?";
	private List<String> answersList = new ArrayList<String>();
	private int solution = 1;
	private Set<String> tagsSet = new HashSet<String>();
	private int questionId = 1;
	private String questionOwner = "owner1";
	private QuizQuestion question;
	
	private void addAnswers() {
		answersList.add("1");
		answersList.add("2");
		answersList.add("3");
	}

	public void testSignletonClass() {
		
	}
	
	public void testCacheOnlineQuizQuestion() {
		CacheManager cache = CacheManager.getInstance();
		addAnswers();
		question = new QuizQuestion(text, answersList, solution, tagsSet, questionId, questionOwner);
		cache.cacheOnlineQuizQuestion(question);
	}
	
}
