# Java Problem Solutions

[![Java CI with Maven](../../actions/workflows/maven.yml/badge.svg)](../../actions/workflows/maven.yml)

This repository contains solutions to various algorithmic problems implemented in Java.

## Project Structure

```
.
├── src/
│   ├── main/java/com/practice/problems/
│   │   └── LongestPalindromicPathInGraph.java
│   └── test/java/com/practice/problems/
│       └── LongestPalindromicPathInGraphTest.java
├── pom.xml
└── README.md
```

## Prerequisites

- Java JDK 17 or higher
- Maven 3.x

## Building and Testing

### Building the Project

```bash
mvn clean compile
```

### Running Tests

```bash
mvn test
```

To run a specific test class:

```bash
mvn test -Dtest=LongestPalindromicPathInGraphTest
```

## Problems

### 1. Longest Palindromic Path in Graph

Given an undirected graph with `n` nodes labeled with characters, find the longest palindromic path in the graph.

[View Solution](src/main/java/com/practice/problems/LongestPalindromicPathInGraph.java) | [View Tests](src/test/java/com/practice/problems/LongestPalindromicPathInGraphTest.java)

## Adding New Problems

1. Create solution in `src/main/java/com/practice/problems/ProblemName.java`
2. Create tests in `src/test/java/com/practice/problems/ProblemNameTest.java`
3. Run `mvn test` to verify your solution
4. Update this README with problem description

## CI/CD

This project uses GitHub Actions for continuous integration and delivery. The workflow:

- Builds the project
- Runs all tests
- Uploads build artifacts

You can view the build status in the badge at the top of this README.
