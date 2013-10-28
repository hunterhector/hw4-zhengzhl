package edu.cmu.lti.f13.hw4.hw4_zhengzhl.annotators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.util.FSCollectionFactory;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

import edu.cmu.lti.f13.hw4.hw4_zhengzhl.typesystems.Document;
import edu.cmu.lti.f13.hw4.hw4_zhengzhl.typesystems.Token;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

public class DocumentVectorAnnotator extends JCasAnnotator_ImplBase {
	private StanfordCoreNLP pipeline;

	public void initialize(UimaContext aContext)
			throws ResourceInitializationException {
		super.initialize(aContext);

		Properties props = new Properties();
		props.put("annotators", "tokenize");

		pipeline = new StanfordCoreNLP(props);
	}

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {

		// FSIterator<Annotation> iter = jcas.getAnnotationIndex().iterator();
		// if (iter.isValid()) {
		// iter.moveToNext();
		// Document doc = (Document) iter.get();
		// createTermFreqVector(jcas, doc);
		// }

		for (Document doc : JCasUtil.select(jcas, Document.class)) {
			createTermFreqVector(jcas, doc);
		}

	}

	/**
	 * 
	 * @param jcas
	 * @param doc
	 */

	private void createTermFreqVector(JCas aJCas, Document doc) {

		String docText = doc.getText();

		Annotation document = new Annotation(docText);
		pipeline.annotate(document);

		// int docBegin = doc.getBegin();
		// TO DO: construct a vector of tokens and update the tokenList in CAS

		List<Token> tokens = new ArrayList<Token>();
		Map<String, Integer> tokenCounts = new HashMap<String, Integer>();
		for (CoreLabel coreToken : document.get(TokensAnnotation.class)) {
			// int beginIndex = coreToken
			// .get(CharacterOffsetBeginAnnotation.class);
			// int endIndex = coreToken.get(CharacterOffsetEndAnnotation.class);
			// Token token = new Token(aJCas, docBegin + beginIndex, endIndex);

			// String word = coreToken.word();
			String word = coreToken.word().toLowerCase();

			// String word = coreToken.lemma().toLowerCase();
			if (tokenCounts.containsKey(word)) {
				tokenCounts.put(word, tokenCounts.get(word) + 1);
			} else {
				tokenCounts.put(word, 1);
			}
		}

		for (Entry<String, Integer> tokenCount : tokenCounts.entrySet()) {
			Token token = new Token(aJCas);
			token.setFrequency(tokenCount.getValue());
			token.setText(tokenCount.getKey());
			token.addToIndexes();
			tokens.add(token);
		}

		doc.setTokenList(FSCollectionFactory.createFSList(aJCas, tokens));
	}
}