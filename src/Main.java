import java.io.*;
import java.util.*;

class Main {

    static Map<Character, String> huffman;

    static class Node implements Comparable<Node> {
        char letter;
        int frequency;
        Node left, right;

        Node(char letter, int frequency) {
            this.letter = letter;
            this.frequency = frequency;
            left=null;
            right=null;
        }

        Node(int frequency, Node left, Node right) {
            this.frequency = frequency;
            this.left = left;
            this.right = right;
        }

        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.frequency, other.frequency);
        }
    }

    public static void generator(Node root) {
        huffman = new HashMap<>();
        createCodes(root, "");
    }

    private static void createCodes(Node searchedNode, String sequence) {
        if (searchedNode == null) return;

        if (searchedNode.left == null && searchedNode.right == null) {
            huffman.put(searchedNode.letter, sequence);
            return;
        }

        createCodes(searchedNode.left, sequence + "0");
        createCodes(searchedNode.right, sequence + "1");
    }

    public static void main(String[] args) throws IOException {

        String inputFilePath = args[0];
        Map<Character, Integer> initialFrequencies = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(inputFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                char character = parts[0].charAt(0);
                int frequency = Integer.parseInt(parts[1]);
                initialFrequencies.put(character, frequency);
            }
        }

        PriorityQueue<Node> priorityQ = new PriorityQueue<>();

        for (Map.Entry<Character, Integer> entry : initialFrequencies.entrySet()) {
            priorityQ.add(new Node(entry.getKey(), entry.getValue()));
        }

        while (priorityQ.size() > 1) {
            Node left = priorityQ.poll();
            Node right = priorityQ.poll();
            Node merged = new Node(left.frequency + right.frequency, left, right);
            priorityQ.add(merged);
        }

        Node root = priorityQ.poll();

        generator(root);

        huffman.forEach((character, code) -> System.out.println(character + " " + code));
    }
}