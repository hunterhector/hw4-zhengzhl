/**
 * 
 */
package edu.cmu.lti.f13.hw4.hw4_zhengzhl.utils;

import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * @author hector
 * 
 */
public class Answer implements Comparable<Answer> {

	private double score;
	private int relevance;
	private int qid;
	private int sentid;

	public Answer(int qid, int sentid, int relevance) {
		this.qid = qid;
		this.sentid = sentid;
		this.relevance = relevance;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public int getQid() {
		return qid;
	}

	public void setQid(int qid) {
		this.qid = qid;
	}

	public int getSentid() {
		return sentid;
	}

	public void setSentid(int sentid) {
		this.sentid = sentid;
	}

	public int getRelevance() {
		return relevance;
	}

	public void setRelevance(int relevance) {
		this.relevance = relevance;
	}

	@Override
	public int compareTo(Answer o) {
		if (o == null) {
			return 1;
		} else {
			if (this.score == o.score) {
				return 0;
			} else if (this.score > o.score) {
				return 1;
			} else {
				return -1;
			}
		}
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(qid).append(sentid)
				.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Answer))
			return false;
		if (this == obj)
			return true;
		return (qid == ((Answer) obj).qid) && (sentid == ((Answer) obj).sentid);
	}

}
