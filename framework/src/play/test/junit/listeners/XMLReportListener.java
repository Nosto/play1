package play.test.junit.listeners;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;

import org.apache.tools.ant.taskdefs.optional.junit.JUnitResultFormatter;
import org.apache.tools.ant.taskdefs.optional.junit.JUnitTest;
import org.apache.tools.ant.taskdefs.optional.junit.XMLJUnitResultFormatter;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

import junit.framework.JUnit4TestAdapterCache;
import play.test.junit.listeners.xmlout.DescriptionAsTest;


/**
 * Adopts {@link JUnitResultFormatter} into {@link RunListener},
 * and also captures stdout/stderr by intercepting the likes of {@link System#out}.
 * <p>
 * Because Ant JUnit formatter uses one stderr/stdout per one test suite,
 * we capture each test case into a separate report file.
 */
public class XMLReportListener extends RunListener {

    protected final JUnitResultFormatter formatter;
    private ByteArrayOutputStream stdout, stderr;
    private PrintStream oldStdout, oldStderr;
    private int problem;
    private long startTime;

    public XMLReportListener() {
        this(new XMLJUnitResultFormatter());
    }

    public XMLReportListener(JUnitResultFormatter formatter) {
        this.formatter = formatter;
    }

    @Override
    public void testRunStarted(Description description) {

    }

    @Override
    public void testRunFinished(Result result) {

    }

    @Override
    public void testStarted(Description description) throws Exception {
        formatter.setOutput(new FileOutputStream(new File("test-result", "TEST-" + description.getClassName() + "-" + description.getMethodName() + ".xml")));
        formatter.startTestSuite(new JUnitTest(description.getDisplayName()));
        formatter.startTest(JUnit4TestAdapterCache.getDefault().asTest(description));
        problem = 0;
        startTime = System.currentTimeMillis();

        this.oldStdout = System.out;
        this.oldStderr = System.err;
        System.setOut(new PrintStream(stdout = new ByteArrayOutputStream()));
        System.setErr(new PrintStream(stderr = new ByteArrayOutputStream()));
    }

    @Override
    public void testFinished(Description description) {
        System.out.flush();
        System.err.flush();
        System.setOut(oldStdout);
        System.setErr(oldStderr);

        formatter.setSystemOutput(stdout.toString());
        formatter.setSystemError(stderr.toString());
        formatter.endTest(JUnit4TestAdapterCache.getDefault().asTest(description));

        JUnitTest suite = new JUnitTest(description.getDisplayName());
        suite.setCounts(1, problem, 0);
        suite.setRunTime(System.currentTimeMillis() - startTime);
        formatter.endTestSuite(suite);
    }

    @Override
    public void testFailure(Failure failure) {
        testAssumptionFailure(failure);
    }

    @Override
    public void testAssumptionFailure(Failure failure) {
        problem++;
        formatter.addError(new DescriptionAsTest(failure.getDescription()), failure.getException());
    }

    @Override
    public void testIgnored(Description description) throws Exception {
        super.testIgnored(description);
    }
}