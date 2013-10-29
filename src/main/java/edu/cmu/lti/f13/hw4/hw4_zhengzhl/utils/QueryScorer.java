/**
 * 
 */
package edu.cmu.lti.f13.hw4.hw4_zhengzhl.utils;

import java.util.Map;
import java.util.Set;

/**
 * The abstract interface for a scorer. Implements should pay attention to the
 * computeScore method
 * 
 * @author Zhengzhong Liu, Hector
 * 
 */
public interface QueryScorer {
	/**
	 * A method for the name of this scorer
	 * 
	 * @return The name of this scorer
	 */
	public String name();

	/**
	 * The method for computing the score
	 * 
	 * @param globalWords
	 *            Global word dictionary
	 * @param queryVector
	 *            Query word list
	 * @param docVector
	 *            Document word list
	 * @return The score of this query, document pair
	 */
	public double computeScore(Set<String> globalWords,
			Map<String, Integer> queryVector, Map<String, Integer> docVector);
}
