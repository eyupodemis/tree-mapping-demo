package tree.mapper;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class TreeMapperImpl implements TreeMapper {

	private static final Logger logger = LoggerFactory.getLogger(TreeMapperImpl.class);
	StringBuffer buffer = new StringBuffer();

	@Override
	public List<String> readFile(String fileName) {

		List<String> fileDataList = new ArrayList<>();

		try {
			Resource resource = new ClassPathResource(fileName);
			BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
			reader.lines().forEach(line -> fileDataList.addAll(Arrays.asList(line)));
			
		} catch (Exception e) {
			logger.error("Could not read file. Reason: ", e.getMessage());
		}

		return fileDataList;

	}

	@Override
	public Map<String, ReviewNode<String>> prepareReviewTreeList(List<String> fileDataList) {

		Map<String, ReviewNode<String>> reviewList = new HashMap<>();

		Pattern pattern = Pattern.compile("(.*) reviews (.*)");

		Map<String, List<String>> reviewMap = fileDataList.stream().sorted().map(pattern::matcher).filter(Matcher::find)
				.collect(groupingBy(map -> map.group(1), LinkedHashMap::new, mapping(map -> map.group(2), toList())));

		reviewMap.forEach((parent, children) -> {
			if (!reviewList.containsKey(parent)) {
				ReviewNode<String> reviewNode = new ReviewNode<String>(parent, children);
				reviewList.put(parent, reviewNode);
			}
		});

		return reviewList;

	}

	@Override
	public String findReviewerRoot(Map<String, ReviewNode<String>> reviewList) {
		
		/*
		 * Set<String> reviewers = reviewList.keySet(); Set<String> reviewees =
		 * new HashSet<>(); reviewList.forEach((k, v) -> {
		 * reviewees.addAll(v.getReviewees()); });
		 * reviewers.removeAll(reviewees); return
		 * reviewers.iterator().next().trim();
		 */

		String result = new String();
		int foundCount = 0;

		for (String key : reviewList.keySet()) {
			for (String key2 : reviewList.keySet()) {
				if (reviewList.get(key2).getReviewees().contains(key))
					foundCount++;
			}

			if (foundCount == 0)
				result = key;

			foundCount = 0;
		}

		return result;
	}

	@Override
	public String printReviewList(Map<String, ReviewNode<String>> reviewList, String reviewerName, int level) {

		IntStream.iterate(0, i -> i + 1).limit(level).forEach(i -> buffer.append("|"));

		if (!Optional.ofNullable(reviewList.get(reviewerName)).isPresent() || reviewList.get(reviewerName).getReviewees().isEmpty()) {
			buffer.append("\\-").append(reviewerName).append("\n");
		} else {
			buffer.append(reviewerName).append("\n");
			reviewList.get(reviewerName).getReviewees().forEach(node -> {
				printReviewList(reviewList, node, (level + 1));
			});
		}

		return buffer.toString();
	}

}
