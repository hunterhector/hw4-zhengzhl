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
public class CosineQueryScorer implements QueryScorer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.cmu.lti.f13.hw4.hw4_zhengzhl.utils.QueryScorer#computeCosineSimilarity
	 * (java.util.Map, java.util.Map)
	 */
	@Override
	public double computeScore(Set<String> globalWords,
			Map<String, Integer> queryVector, Map<String, Integer> docVector) {
		double cosine_similarity = 0.0;

		// TODO :: compute cosine similarity between two sentences
		double length1 = 0.0;
		double length2 = 0.0;
		for (String word : globalWords) {
			double freq1 = 0.0;
			if (queryVector.containsKey(word)) {
				freq1 = queryVector.get(word);
			}
			double freq2 = 0.0;
			if (docVector.containsKey(word)) {
				freq2 = docVector.get(word);
			}

			cosine_similarity += freq1 * freq2;

			length1 += freq1 * freq1;
			length2 += freq2 * freq2;
		}

		cosine_similarity = cosine_similarity == 0 ? 0.0 : cosine_similarity
				/ (Math.sqrt(length1) * Math.sqrt(length2));

		return cosine_similarity;
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Cosine";
	}

}
