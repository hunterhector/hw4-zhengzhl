/**
 * 
 */
package edu.cmu.lti.f13.hw4.hw4_zhengzhl.utils;

import java.util.Map;
import java.util.Set;

/**
 * Implement the interface to compute Jaccard coefficient
 * 
 * @author Zhengzhong Liu, Hector
 * 
 */
public class JaccardScorer implements QueryScorer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.cmu.lti.f13.hw4.hw4_zhengzhl.utils.QueryScorer#computeScore(java.
	 * util.Set, java.util.Map, java.util.Map)
	 */
	@Override
	public double computeScore(Set<String> globalWords,
			Map<String, Integer> queryVector, Map<String, Integer> docVector) {
		double intersection = 0.0;
		double union = 0.0;

		for (String word : globalWords) {
			double freq1 = 0.0;
			if (queryVector.containsKey(word)) {
				freq1 = queryVector.get(word);
			}
			double freq2 = 0.0;
			if (docVector.containsKey(word)) {
				freq2 = docVector.get(word);
			}

			intersection += Math.min(freq1, freq2);
			union += Math.max(freq1, freq2);
		}

		return intersection / union;
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Jaccard";
	}

}
