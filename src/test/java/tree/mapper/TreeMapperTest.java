package tree.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.spy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class TreeMapperTest {
	
	private TreeMapper treeMapper = spy(TreeMapperImpl.class);
	
	@Test
	public void testReadFileSunnyDay() {
		List<String> result = treeMapper.readFile("test-file-1.txt");
		assertNotNull(result);
		assertEquals(result.size(), 6);
	
		IntStream.iterate(0, i -> i + 1).limit(result.size()).forEach(i -> {
			assertEquals("line" + (i+1), result.get(i));
		});
	}
	
	@Test
	public void testReadFileFileNotFound() { 
		List<String> result = treeMapper.readFile("no-such-file");
		assertTrue(result.isEmpty());
	}
	
	@Test
	public void testPrepareReviewTreeList() { 
		Map<String, ReviewNode<String>> reviewList = treeMapper.prepareReviewTreeList(prepareFileDataList());
		assertNotNull(reviewList);
		assertEquals(2, reviewList.size());
		assertEquals("A", reviewList.get("A").getReviewer());
		assertEquals(2, reviewList.get("A").getReviewees().size());
		assertEquals("B", reviewList.get("B").getReviewer());
		assertEquals(1, reviewList.get("B").getReviewees().size());
	}
	
	@Test
	public void testFindReviewerRoot() {
		Map<String, ReviewNode<String>> reviewList = treeMapper.prepareReviewTreeList(prepareFileDataList());
		assertEquals("A", treeMapper.findReviewerRoot(reviewList));
	}
	
	@Test
	public void testPrintReviewList() {
		Map<String, ReviewNode<String>> reviewList = treeMapper.prepareReviewTreeList(prepareFileDataList());
		String reviewTree = treeMapper.printReviewList(reviewList, treeMapper.findReviewerRoot(reviewList), 0);
		assertEquals(buildReviewMapTree(), reviewTree);
	}
	
	private List<String> prepareFileDataList() {
		List<String> fileDataList = new ArrayList<>();
		fileDataList.add("A reviews B");
		fileDataList.add("B reviews C");
		fileDataList.add("A reviews D");
		return fileDataList;
	}
	
	private String buildReviewMapTree() {
		return "A\n|B\n||\\-C\n|\\-D\n";
	}
	
}

