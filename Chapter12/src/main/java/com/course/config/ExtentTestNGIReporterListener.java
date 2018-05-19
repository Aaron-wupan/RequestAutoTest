package com.course.config;


import com.aventstack.extentreports.ExtentReporter;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.ResourceCDN;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.model.TestAttribute;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;
import java.io.File;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.testng.IReporter;
import org.testng.IResultMap;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.xml.XmlSuite;

public class ExtentTestNGIReporterListener implements IReporter {
    private static final String OUTPUT_FOLDER = "test-output/";
    private static final String FILE_NAME = "index.html";
    private ExtentReports extent;

    public ExtentTestNGIReporterListener() {
    }

    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
        this.init();
        boolean createSuiteNode = false;
        if (suites.size() > 1) {
            createSuiteNode = true;
        }

        Iterator var5 = suites.iterator();

        while(true) {
            ISuite suite;
            Map result;
            do {
                if (!var5.hasNext()) {
                    this.extent.flush();
                    return;
                }

                suite = (ISuite)var5.next();
                result = suite.getResults();
            } while(result.size() == 0);

            int suiteFailSize = 0;
            int suitePassSize = 0;
            int suiteSkipSize = 0;
            ExtentTest suiteTest = null;
            if (createSuiteNode) {
                suiteTest = this.extent.createTest(suite.getName()).assignCategory(new String[]{suite.getName()});
            }

            boolean createSuiteResultNode = false;
            if (result.size() > 1) {
                createSuiteResultNode = true;
            }

            Iterator var13 = result.values().iterator();

            while(var13.hasNext()) {
                ISuiteResult r = (ISuiteResult)var13.next();
                ITestContext context = r.getTestContext();
                ExtentTest resultNode;
                if (createSuiteResultNode) {
                    if (null == suiteTest) {
                        resultNode = this.extent.createTest(r.getTestContext().getName());
                    } else {
                        resultNode = suiteTest.createNode(r.getTestContext().getName());
                    }
                } else {
                    resultNode = suiteTest;
                }

                if (resultNode != null) {
                    resultNode.getModel().setName(suite.getName() + " : " + r.getTestContext().getName());
                    if (resultNode.getModel().hasCategory()) {
                        resultNode.assignCategory(new String[]{r.getTestContext().getName()});
                    } else {
                        resultNode.assignCategory(new String[]{suite.getName(), r.getTestContext().getName()});
                    }

                    resultNode.getModel().setStartTime(r.getTestContext().getStartDate());
                    resultNode.getModel().setEndTime(r.getTestContext().getEndDate());
                    int passSize = r.getTestContext().getPassedTests().size();
                    int failSize = r.getTestContext().getFailedTests().size();
                    int skipSize = r.getTestContext().getSkippedTests().size();
                    suitePassSize += passSize;
                    suiteFailSize += failSize;
                    suiteSkipSize += skipSize;
                    if (failSize > 0) {
                        resultNode.getModel().setStatus(Status.FAIL);
                    }

                    resultNode.getModel().setDescription(String.format("Pass: %s; Fail: %s ; Skip: %s ;", passSize, failSize, skipSize));
                }

                this.buildTestNodes(resultNode, context.getFailedTests(), Status.FAIL);
                this.buildTestNodes(resultNode, context.getSkippedTests(), Status.SKIP);
                this.buildTestNodes(resultNode, context.getPassedTests(), Status.PASS);
            }

            if (suiteTest != null) {
                suiteTest.getModel().setDescription(String.format("Pass: %s ; Fail: %s ; Skip: %s ;", suitePassSize, suiteFailSize, suiteSkipSize));
                if (suiteFailSize > 0) {
                    suiteTest.getModel().setStatus(Status.FAIL);
                }
            }
        }
    }

    private void init() {
        File reportDir = new File("test-output/");
        if (!reportDir.exists() && !reportDir.isDirectory()) {
            reportDir.mkdir();
        }

        ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter("test-output/index.html");
        htmlReporter.config().setResourceCDN(ResourceCDN.EXTENTREPORTS);
        htmlReporter.config().setDocumentTitle("api自动化测试报告");
        htmlReporter.config().setReportName("api自动化测试报告");
        htmlReporter.config().setChartVisibilityOnOpen(true);
        htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);
        htmlReporter.config().setTheme(Theme.STANDARD);
        htmlReporter.config().setCSS(".node.level-1  ul{ display:none;} .node.level-1.active ul{display:block;}");
        this.extent = new ExtentReports();
        this.extent.attachReporter(new ExtentReporter[]{htmlReporter});
        this.extent.setReportUsesManualConfiguration(true);
    }

    private void buildTestNodes(ExtentTest extenttest, IResultMap tests, Status status) {
        String[] categories = new String[0];
        if (extenttest != null) {
            List<TestAttribute> categoryList = extenttest.getModel().getCategoryContext().getAll();
            categories = new String[categoryList.size()];

            for(int index = 0; index < categoryList.size(); ++index) {
                categories[index] = ((TestAttribute)categoryList.get(index)).getName();
            }
        }

        if (extenttest != null) {
            ;
        }

        if (tests.size() > 0) {
            Set<ITestResult> treeSet = new TreeSet(new Comparator<ITestResult>() {
                public int compare(ITestResult o1, ITestResult o2) {
                    return o1.getStartMillis() < o2.getStartMillis() ? -1 : 1;
                }
            });
            treeSet.addAll(tests.getAllResults());
            Iterator var7 = treeSet.iterator();

            while(var7.hasNext()) {
                ITestResult result = (ITestResult)var7.next();
                Object[] parameters = result.getParameters();
                String name = "";
                Object[] var11 = parameters;
                int var12 = parameters.length;

                int var13;
                for(var13 = 0; var13 < var12; ++var13) {
                    Object param = var11[var13];
                    name = name + param.toString();
                }

                if (name.length() > 0) {
                    if (name.length() > 50) {
                        name = name.substring(0, 49) + "...";
                    }
                } else {
                    name = result.getMethod().getMethodName();
                }

                ExtentTest test;
                if (extenttest == null) {
                    test = this.extent.createTest(name);
                } else {
                    test = extenttest.createNode(name).assignCategory(categories);
                }

                String[] var17 = result.getMethod().getGroups();
                var12 = var17.length;

                for(var13 = 0; var13 < var12; ++var13) {
                    String group = var17[var13];
                    test.assignCategory(new String[]{group});
                }

                List<String> outputList = Reporter.getOutput(result);
                Iterator var19 = outputList.iterator();

                while(var19.hasNext()) {
                    String output = (String)var19.next();
                    test.debug(output);
                }

                if (result.getThrowable() != null) {
                    test.log(status, result.getThrowable());
                } else {
                    test.log(status, "Test" + status.toString().toLowerCase() + "ed");
                }

                test.getModel().setStartTime(this.getTime(result.getStartMillis()));
                test.getModel().setEndTime(this.getTime(result.getEndMillis()));
            }
        }

    }

    private Date getTime(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return calendar.getTime();
    }
}

