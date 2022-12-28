package kr.co.songeul.converter.nexacro;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Document {

	private Path file;
	private Node root;
	
	public Document(Path file) throws IOException {
		this.file = file;
		load(this.file);
	}
	
	private void load(Path file) throws IOException {
		List<Node> nodes = new ArrayList<>();
		for (String line : Files.readAllLines(file)) {
			nodes.add(new Node(line));
		}
		this.root = parseNodes(nodes);
	}

	private Node parseNodes(List<Node> nodes) {
 		Node root = null;
		Node prev = null, current;
		Stack<String> stack = new Stack<>();
		for (int i = 1; i < nodes.size(); i++) {
			current = nodes.get(i);
			
			if (stack.size() > 0 && ("/" + stack.peek()).equals(current.getType())) {
				// found pair
				stack.pop();
				if (prev.getChildren().size() > 1) {
					for (int j = 0; j < prev.getChildren().size() - 1; j++) {
						Node child = prev.getChildren().get(j);
						child.putNext(prev.getChildren().get(j + 1));
					}
					
					for (int j = prev.getChildren().size() - 1; j > 0; j--) {
						Node child = prev.getChildren().get(j);
						child.putPrev(prev.getChildren().get(j - 1));
					}
				}
				
				if (current.getContent().replace(current.getCloseTag(), "").length() > 0) {
					prev.addContent(current.getContent().replace(current.getCloseTag(), ""));
				}
				prev = prev.getParent();
				continue;
			} else {
				// child or sibling
				if (current.getType() == null) {
					prev.addContent(current.getContent());
					continue;
				}

				if (stack.size() == 0) {
					root = current;
				} else {
					prev.putChild(current);
					current.putParent(prev);
				}

				if (!current.isClosed()) {
					stack.push(current.getType());
				} else {
					continue;
				}
			}
			prev = current;
		}

		return root;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
		sb.append(root.getChildrendContents());

		return sb.toString();
	}

}
