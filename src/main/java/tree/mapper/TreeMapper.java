package tree.mapper;

import java.util.List;
import java.util.Map;

public interface TreeMapper {
	
	public List<String> readFile(String fileName);
	
	public Map<String, ReviewNode<String>> prepareReviewTreeList(List<String> fileDataList);
	
	public String findReviewerRoot(Map<String, ReviewNode<String>> reviewList);
	
	public String printReviewList(Map<String, ReviewNode<String>> reviewList, String reviewerName, int level);

}
