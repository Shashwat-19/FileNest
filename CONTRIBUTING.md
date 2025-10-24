# Contributing 

Thank you for your interest in contributing to FileNest! This document provides guidelines and information for contributors.

## 📋 Table of Contents
- [Code of Conduct](#code-of-conduct)
- [Getting Started](#getting-started)
- [How to Contribute](#how-to-contribute)
- [Development Setup](#development-setup)
- [Pull Request Process](#pull-request-process)
- [Issue Guidelines](#issue-guidelines)
- [Coding Standards](#coding-standards)
- [Testing](#testing)
- [Documentation](#documentation)

## 📜 Code of Conduct

This project adheres to a code of conduct. By participating, you are expected to uphold this code. Please report unacceptable behavior to [Email](shashwat1956@gmail.com).

## 🚀 Getting Started

### Prerequisites
- Java 11 or higher
- Git
- Your preferred IDE (IntelliJ IDEA, VS Code, Eclipse)

### Fork and Clone
1. Fork the repository on GitHub
2. Clone your fork locally:
   ```bash
   git clone https://github.com/Shashwat-19/FileNest
   cd FileNest
   ```
3. Add the upstream repository:
   ```bash
   git remote add upstream https://github.com/Shashwat-19/FileNest
   ```

## 🔄 How to Contribute

### 1. Choose an Issue
- Look for issues labeled `good first issue` or `help wanted`
- Comment on the issue to let others know you're working on it
- Ask questions if anything is unclear

### 2. Create a Branch
```bash
git checkout -b feature/your-feature-name
# or
git checkout -b bugfix/issue-number
```

### 3. Make Changes
- Write clean, readable code
- Follow the coding standards
- Add tests for new functionality
- Update documentation as needed

### 4. Test Your Changes
```bash
# Run tests
./gradlew test
# or
mvn test
```

### 5. Commit Your Changes
```bash
git add .
git commit -m "Add: brief description of changes"
```

### 6. Push and Create Pull Request
```bash
git push origin your-branch-name
```

## 🛠️ Development Setup

### Local Development
1. Clone the repository
2. Install dependencies
3. Run the application
4. Make your changes
5. Test thoroughly

### Building the Project
```bash
# Using Gradle
./gradlew build

# Using Maven
mvn clean compile
```

## 📝 Pull Request Process

### Before Submitting
- [ ] Ensure your code follows the project's coding standards
- [ ] Add tests for new functionality
- [ ] Update documentation if needed
- [ ] All tests pass
- [ ] Code is properly commented

### PR Template
When creating a pull request, please fill out the template completely:
- Describe what the PR does
- Link to related issues
- List changes made
- Include test results
- Add screenshots if applicable

### Review Process
1. Automated checks must pass
2. Code review by maintainers
3. Address feedback and suggestions
4. Merge when approved

## 🐛 Issue Guidelines

### Bug Reports
- Use the bug report template
- Provide clear steps to reproduce
- Include environment details
- Add error logs and screenshots

### Feature Requests
- Use the feature request template
- Describe the problem and proposed solution
- Provide use cases and acceptance criteria
- Consider alternatives

### Questions
- Use the question template
- Provide context and what you've tried
- Be specific about what you need help with

## 📏 Coding Standards

### Java Code Style
- Follow Java naming conventions
- Use meaningful variable and method names
- Add Javadoc comments for public methods
- Keep methods focused and concise
- Use proper indentation (4 spaces)

### Example:
```java
/**
 * Organizes files in the specified directory.
 * 
 * @param directoryPath the path to the directory to organize
 * @param organizationType the type of organization to apply
 * @return true if organization was successful, false otherwise
 */
public boolean organizeFiles(String directoryPath, OrganizationType organizationType) {
    // Implementation here
}
```

## 🧪 Testing

### Writing Tests
- Write unit tests for new functionality
- Test edge cases and error conditions
- Aim for good test coverage
- Use descriptive test names

### Running Tests
```bash
# Run all tests
./gradlew test

# Run specific test class
./gradlew test --tests "ClassName"

# Run with coverage
./gradlew test jacocoTestReport
```

## 📚 Documentation

### Code Documentation
- Add Javadoc comments for public APIs
- Include parameter and return value descriptions
- Provide usage examples where helpful

### README Updates
- Update README.md for significant changes
- Include setup and usage instructions
- Add screenshots for UI changes

## 🏷️ Labels and Milestones

### Issue Labels
- `bug` - Something isn't working
- `enhancement` - New feature or request
- `documentation` - Documentation changes
- `good first issue` - Good for newcomers
- `help wanted` - Extra attention needed
- `priority:high/medium/low` - Priority levels

### Pull Request Labels
- `ready for review` - Ready for maintainer review
- `needs changes` - Requires changes before merge
- `approved` - Approved for merge

## 🎯 Getting Help

- Check existing issues and discussions
- Ask questions in GitHub Discussions
- Join our community chat (if available)
- Contact maintainers for urgent issues

## 📩 Contact  
### Shashwat  
**Java Developer | Cloud & NoSQL Enthusiast**  

🔹 **Java** – OOP, Backend Systems, APIs, Automation  
🔹 **Cloud & NoSQL** – Docker, AWS, MongoDB, Firebase Firestore  
🔹 **UI/UX Design** – Scalable, user-focused, and visually engaging apps  

---


## 🙏 Recognition

Contributors will be recognized in:
- CONTRIBUTORS.md file
- Release notes
- Project documentation

---

**Thank you for contributing to FileNest! 🚀**

Your contributions help make this project better for everyone. We appreciate your time and effort!
