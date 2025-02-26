## Web_UI
 
This project is focused on automating user management tasks (like adding new users and verifying them in the user list). It uses:

### Table of Contents

- [Introduction](#introduction)
- [Prerequisites](#prerequisites)
- [Setup Instructions](#setup-instructions)
- [How to Run the Tests](#how-to-run-the-tests)
- [Example YML File](#example-yml-file)
- [Test Data](#test-data)
- [Test Reports](#test-reports)
- [Project Structure](project-structure)
- [Troubleshooting](#troubleshooting)
- [Contributions](#contributions)
- [License](#license)
- [Screenshots](#screenshots)


### Introduction

This project includes Selenium tests for the **Web_UI**, focusing on:

- **Selenium WebDriver** for browser automation.
- **TestNG** for managing tests and assertions.
- **ExtentReports** for generating HTML test reports.
- **Log4j** for logging execution details.
- **WebDriverManager** for managing browser driver binaries automatically.


### Prerequisites:

- **Java (JDK 8 or higher)** - Needed to compile and run the test code.
- **Maven** - For dependency management and building the project.
- **IDE (like IntelliJ IDEA or Eclipse)** - To edit and open the project files.
 
### Setup Instructions:

**Clone the Repository**: Clone the repository using the following command:


git clone https://github.com/yourusername/user-management-tests.git

### Navigate to the Project Directory:

cd user-management-tests
Install Dependencies: Ensure Maven is installed, then run:

mvn clean install
This will install the required dependencies defined in the pom.xml file.

### Set Up WebDriver Executables:

WebDriverManager is used to manage browser drivers. Ensure that the necessary browsers (Chrome, Firefox, Edge) are installed on your machine.

### Running Tests:

**Configure Test Browser:** In the testng.xml file, specify which browser (Chrome, Firefox, Edge) to use for tests.

**Run Tests with Maven:**

mvn clean test
This will execute the tests based on your testng.xml configuration.

You can also specify the browser by running:

mvn clean test -Dbrowser=chrome

### Example YML File:

Here is an example of a YAML configuration file that you can use for a CI/CD pipeline (such as GitHub Actions) to automatically run the tests:

name: Run Selenium Tests

on:
  push:
    branches:
      - master
  pull_request:
    branches:
      - master

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
    - name: Checkout repository
      uses: actions/checkout@v2

    - name: Set up JDK 17
      uses: actions/setup-java@v2
      with:
        java-version: '17'

    - name: Install dependencies with Maven
      run: mvn clean install -DskipTests

    - name: Run Tests with Maven
      run: mvn clean test

### Explanation of the YML File:
- **Trigger:** The workflow is triggered on pushes and pull requests to the main branch.
- **Jobs:** The build job runs on ubuntu-latest.
- **Java:** It sets up JDK 17.
- **Maven:** Runs the Maven tests using mvn clean test.

This YML file is designed to run your tests on every push or pull request to the main branch, ensuring that the tests are executed automatically in your CI/CD pipeline.

### Test Reports:

After the tests are run, the results will be available in the target/extent-reports/ directory. You can open the index.html file to view the detailed test execution reports.

### Project Structure:

```bash

user-management-tests/
├── src/
│   ├── main/
│   │   ├── java/
│   │   └── resources/
│   └── test/
│       ├── java/
│       └── resources/
├── Reports/
│   └── extent-reports/
└── pom.xml
```

### Key Folders:

**src/test/java/tests/:** Contains test classes like UserTest.java.
**src/test/resources/:** Contains test data and configuration files like testdata.json and testng.xml.
**reports/extent-reports/:** The folder where ExtentReports generates HTML reports.

### Test Data Example (testdata.json):

json

{
  "users": [
    {
      "firstName": "John",
      "lastName": "Doe",
      "username": "john.doe",
      "password": "password123",
      "company": "Company A",
      "role": "Admin",
      "email": "john.doe@example.com",
      "cellPhone": "+1234567890"
    }
  ]
}
### Troubleshooting:

**Browser Issues:** Ensure the correct browser drivers are installed. You may need:

- chromedriver for Chrome
- geckodriver for Firefox
- msedgedriver for Edge

**Logs:** Log4j is used to log execution details, which can be helpful for debugging.

### Contributions:

You are encouraged to fork the repository and submit pull requests for any improvements or fixes.

### License:

This project is licensed under the MIT License, and you can see the LICENSE file for more details.

### Screenshots
These screenshots demonstrate that the project runs successfully both on a local machine and through GitHub Actions.

![Local run](Screenshot_2025-02-26_153115_Local.png)

![GitHub run](Screenshot_2025-02-26_153355_Github_actions.png)








