package kr.co.songeul.converter.nexacro;

import kr.co.songeul.converter.config.Config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
public class Replacer implements CommandLineRunner {

	//	private static final Pattern PATTERN_ID = Pattern.compile("(?<=id=\")(.*?)(?=\")");
	//	private static final Pattern PATTERN_TEXT = Pattern.compile("(?<=text=\")(.*?)(?=\")");
	//	private static final Pattern PATTERN_TOP_ID = Pattern.compile("(?<=top=\")(.*?)(?=:[0-9]{1,}\")");

	private Config config;

	public Replacer(Config config) {
		this.config = config;
	}

	@Override
	public void run(String... args) {
		log.info("test");
		for (int i = 0; i < args.length; ++i) {
			log.info("args[{}]: {}", i, args[i]);
		}

		String path = args[0];
		String filename = args[1];

		Set<Path> files = findFiles(path, filename);
		log.info("files" + files);
	}

//	public static void main(String[] args) throws IOException {
//		Replacer replacer = new Replacer();
//
//		Set<Path> files = replacer.findFiles("D:/workspace/nexacro/dlcbcerp/src/main/app/erp/erp/dz/dzz", "DZZ_SAMPLE6.xfdl");
////		Set<Path> files = replacer.findFiles("D:/workspace/nexacro/dlcbcerp/src/main/app/erp/erp/dz/dzz", "DZZ_MYFOLDER.xfdl");
////		Set<Path> files = replacer.findFiles("D:/workspace/nexacro/dlcbcerp/src/main/app/erp/erp/dz/dzz", null);
//		for (Path file : files) {
//			Document document = replacer.load(file);
//
//			System.out.println(document);
//
////			for (Line line : lines) {
////				System.out.println(line.toString());
////			}
//
//			//				StringBuffer sb = new StringBuffer();
//			//
//			//				List<String> lines = Files.readAllLines(file);
//			//				for (int i = 0; i < lines.size(); i++) {
//			//					String line = lines.get(i);
//			//
//			//					List<String> searchLines = new ArrayList<>();
//			//					if (line.contains("<Div id=\"divSearch\"")) {
//			//						int j = 0;
//			//						while (true) {
//			//							String next = lines.get(i + j++);
//			//							searchLines.add(next);
//			//							if (next.contains("</Div>")) {
//			//								i += j;
//			//								line = lines.get(i);
//			//								break;
//			//							}
//			//						}
//			//
//			//						String previousObjectId = "";
//			//						for (int k = 0; k < targetLines.size(); k++) {
//			//							String targetLine = targetLines.get(k);
//			//
//			//							// TODO height 속성 값을 결정하는 작업이 추가적으로 필요함
//			//							if (targetLine.contains("<Div id=\"divSearch\"")) {
//			//								targetLine = targetLine.replaceAll("height=\".*?\"", "height=\"46\"");
//			//								sb.append(targetLine + "\n");
//			//								continue;
//			//							}
//			//
//			//
//			//							Matcher matcherTopId = patternTopId.matcher(targetLine);
//			//							if (matcherTopId.find()) {
//			//								targetLine = targetLine.replaceAll("top=\".*?\"", "top=\"" + matcherTopId.group() + ":10\"");
//			//							} else {
//			//								targetLine = targetLine.replaceAll("top=\".*?\"", "top=\"10\"");
//			//							}
//			//
//			//
//			//
//			//
//			//
//			//							if (first && !targetLine.contains("<Layout")) { // if first <Static/>
//			//								targetLine = targetLine.replaceAll("left=\".*?\"", "left=\"0\"");
//			//							} else {
//			//								targetLine = targetLine.replaceAll("left=\".*?\"", "left=\"" + id + ":0\"");
//			//							}
//			//
//			//
//			//
//			//
//			//
//			//							targetLine = targetLine.replaceAll("height=\".*?\"", "height=\"24\"");
//			//
//			//
//			//
//			//
//			//
//			//							if (targetLine.contains("<Static")) {
//			//								if (targetLine.contains(" textDecoration=\"underline\"") || targetLine.contains("cssclass=\"sta_SearchY\"")) {
//			//									targetLine = targetLine.replaceAll(" textDecoration=\"underline\"", "");
//			//									if (targetLine.contains("cssclass=\"")) {
//			//										targetLine = targetLine.replaceAll("cssclass=\".*?\"", "cssclass=\"sta_WF_SchLabelE\"");
//			//									} else {
//			//										targetLine = targetLine.replaceAll("/>", " cssclass=\"sta_WF_SchLabelE\"/>");
//			//									}
//			//								} else {
//			//									if (targetLine.contains("cssclass=\"")) {
//			//										targetLine = targetLine.replaceAll("cssclass=\".*?\"", "cssclass=\"sta_WF_SchLabel\"");
//			//									} else {
//			//										targetLine = targetLine.replaceAll("/>", " cssclass=\"sta_WF_SchLabel\"/>");
//			//									}
//			//								}
//			//
//			//								String text = "";
//			//								Matcher matcherText = patternText.matcher(targetLine);
//			//								if (matcherText.find()) {
//			//									text = matcherText.group();
//			//								}
//			//								String width = "";
//			//								switch (text.length()) {
//			//								case 2:
//			//									width = "66";
//			//									break;
//			//								case 3:
//			//									width = "79";
//			//									break;
//			//								case 4:
//			//									width = "92";
//			//									break;
//			//								case 5:
//			//									width = "105";
//			//									break;
//			//								case 6:
//			//									width = "118";
//			//									break;
//			//								}
//			//								targetLine = targetLine.replaceAll("width=\".*?\"", "width=\"" + width + "\"");
//			//							}
//			//
//			//							previousObjectId = css.getAttribute("id", targetLine);
//			//							sb.append(targetLine + "\n");
//			//						}
//			//					}
//			//					sb.append(line + "\n");
//			//				}
//			//				System.out.println(sb.toString());
//		}
//	}

	private Document load(Path file) throws IOException {
		return new Document(file);
	}

	private Set<Path> findFiles(String dir, String filter) {
		try (Stream<Path> stream = Files.list(Paths.get(dir))) {
			return stream
					.filter(file -> !Files.isDirectory(file))
					.filter(name -> filter == null || name.getFileName().toString().equals(filter))
					.collect(Collectors.toSet());
		} catch (IOException e) {
			e.printStackTrace();
		}

		return Collections.emptySet();
	}

}
