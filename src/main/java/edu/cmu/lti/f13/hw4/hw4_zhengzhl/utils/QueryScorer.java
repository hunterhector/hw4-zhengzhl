/**
 * 
 */
package edu.cmu.lti.f13.hw4.hw4_zhengzhl.utils;

import java.util.Map;
import java.util.Set;

/**
 * @author Zhengzhong Liu, Hector
 * 
 */
public interface QueryScorer {
	public String name();

	public double computeScore(Set<String> globalWords,
			Map<String, Integer> queryVector, Map<String, Integer> docVector);
}
