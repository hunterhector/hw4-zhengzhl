package edu.cmu.lti.f13.hw4.hw4_zhengzhl.casconsumers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.collection.CasConsumer_ImplBase;
import org.apache.uima.fit.util.FSCollectionFactory;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSList;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.ResourceProcessException;
import org.apache.uima.util.ProcessTrace;

import edu.cmu.lti.f13.hw4.hw4_zhengzhl.typesystems.Document;
import edu.cmu.lti.f13.hw4.hw4_zhengzhl.typesystems.Token;
import edu.cmu.lti.f13.hw4.hw4_zhengzhl.utils.Pair;

public class RetrievalEvaluator extends CasConsumer_ImplBase {

	/** query id number **/
	public ArrayList<Integer> qIdList;

	/** query and text relevant values **/
	public ArrayList<Integer> relList;

	private Set<String> globalWords;

	private List<Map<String, Integer>> documents;

	public void initialize() throws ResourceInitializationException {

		qIdList = new ArrayList<Integer>();

		relList = new ArrayList<Integer>();

		globalWords = new HashSet<String>();

		documents = new ArrayList<Map<String, Integer>>();
	}

	/**
	 * TODO :: 1. construct the global word dictionary 2. keep the word
	 * frequency for each sentence
	 */
	@Override
	public void processCas(CAS aCas) throws ResourceProcessException {

		JCas jcas;
		try {
			jcas = aCas.getJCas();
		} catch (CASException e) {
			throw new ResourceProcessException(e);
		}

		FSIterator it = jcas.getAnnotationIndex(Document.type).iterator();

		if (it.hasNext()) {
			Document doc = (Document) it.next();

			// Make sure that your previous annotators have populated this in
			// CAS
			FSList fsTokenList = doc.getTokenList();
			// ArrayList<Token>tokenList=Utils.fromFSListToCollection(fsTokenList,
			// Token.class);

			qIdList.add(doc.getQueryID());
			relList.add(doc.getRelevanceValue());

			// Do something useful here
			Map<String, Integer> tokenWithFreq = new HashMap<String, Integer>();
			for (Token token : FSCollectionFactory.create(fsTokenList,
					Token.class)) {
				globalWords.add(token.getText());
				tokenWithFreq.put(token.getText(), token.getFrequency());
			}

			documents.add(tokenWithFreq);
		}

	}

	/**
	 * TODO 1. Compute Cosine Similarity and rank the retrieved sentences 2.
	 * Compute the MRR metric
	 */
	@Override
	public void collectionProcessComplete(ProcessTrace arg0)
			throws ResourceProcessException, IOException {

		super.collectionProcessComplete(arg0);

		List<List<Pair<Integer, Double>>> allScorePairs = new ArrayList<List<Pair<Integer, Double>>>();

		Map<String, Integer> currentQuery = null;
		// TODO :: compute the cosine similarity measure
		for (int i = 0; i < qIdList.size(); i++) {
			int qid = qIdList.get(i);
			int rel = relList.get(i);
			Map<String, Integer> document = documents.get(i);
			if (rel == 99) {
				currentQuery = document;
				allScorePairs.add(new ArrayList<Pair<Integer, Double>>());
			} else {
				double cosine = computeCosineSimilarity(currentQuery, document);
				allScorePairs.get(allScorePairs.size() - 1).add(
						Pair.of(rel, cosine));
			}
		}

		// TODO :: compute the rank of retrieved sentences
		for (List<Pair<Integer, Double>> scorePairs : allScorePairs) {
			Collections.sort(scorePairs);
		}

		// TODO :: compute the metric:: mean reciprocal rank
		double metric_mrr = compute_mrr(allScorePairs);
		System.out.println(" (MRR) Mean Reciprocal Rank ::" + metric_mrr);
	}

	/**
	 * 
	 * @return cosine_similarity
	 */
	private double computeCosineSimilarity(Map<String, Integer> queryVector,
			Map<String, Integer> docVector) {
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

	/**
	 * 
	 * @return mrr
	 */
	private double compute_mrr(List<List<Pair<Integer, Double>>> allScorePairs) {
		double metric_mrr = 0.0;

		// TODO :: compute Mean Reciprocal Rank (MRR) of the text collection
		for (List<Pair<Integer, Double>> scorePairs : allScorePairs) {
			for (int i = 0; i < scorePairs.size(); i++) {
				Pair<Integer, Double> scorePair = scorePairs.get(i);
				if (scorePair.first == 1) {

					metric_mrr += 1.0 / (i + 1);
					break;
				}
			}
		}

		metric_mrr /= allScorePairs.size();

		return metric_mrr;
	}

}
