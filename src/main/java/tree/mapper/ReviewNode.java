package tree.mapper;

import java.util.ArrayList;
import java.util.List;

public class ReviewNode<T> {

	private T reviewer;
	private List<T> reviewees = new ArrayList<>();

	public ReviewNode(T reviewer, List<T> reviewees) {
		this.reviewer = reviewer;
		this.reviewees = reviewees;
	}

	public T getReviewer() {
		return reviewer;
	}

	public void setReviewer(T reviewer) {
		this.reviewer = reviewer;
	}

	public List<T> getReviewees() {
		return reviewees;
	}

	public void setReviewees(List<T> reviewees) {
		this.reviewees = reviewees;
	}

}