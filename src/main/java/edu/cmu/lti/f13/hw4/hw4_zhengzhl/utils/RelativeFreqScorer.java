/**
 * 
 */
package edu.cmu.lti.f13.hw4.hw4_zhengzhl.utils;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * A scorer using the relative frequency method
 * 
 * @author Zhengzhong Liu, Hector
 * 
 */
public class RelativeFreqScorer implements QueryScorer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.cmu.lti.f13.hw4.hw4_zhengzhl.utils.QueryScorer#name()
	 */
	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Relavtive Cosine";
	}

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
		double dotProd = 0.0;

		int docLength = 0;

		for (Entry<String, Integer> docItem : docVector.entrySet()) {
			docLength += docItem.getValue();
		}

		for (String word : globalWords) {
			double freq1 = 0.0;
			if (queryVector.containsKey(word)) {
				freq1 = (double) queryVector.get(word);
			}
			double freq2 = 0.0;
			if (docVector.containsKey(word)) {
				freq2 = (double) docVector.get(word) / docLength;
			}

			dotProd += freq1 * freq2;

		}

		return dotProd;
	}
}
