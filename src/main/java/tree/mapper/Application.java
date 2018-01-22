package tree.mapper;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application implements CommandLineRunner {
	
	private static final Logger logger = LoggerFactory.getLogger(Application.class);
	private final int TOP_LEVEL = 0;
	
	@Autowired
	private TreeMapper treeMapper;
	
	public static void main(String[] args) throws Exception {
		SpringApplication.run(Application.class, args).close();
	}
	
	 @Override
	 public void run(String... args) throws Exception {
		 treeWalk("reviewers-and-reviewees.txt");
	 }
	 
	 private void treeWalk(String fileName) {
		 List<String> fileDataList = treeMapper.readFile(fileName);
		 Map<String, ReviewNode<String>> reviewList = treeMapper.prepareReviewTreeList(fileDataList);
System.out.println("findReviewerRoot(reviewList)="+treeMapper.findReviewerRoot(reviewList));		 
		 System.out.println(treeMapper.printReviewList(reviewList, treeMapper.findReviewerRoot(reviewList), TOP_LEVEL));
	 }

}
