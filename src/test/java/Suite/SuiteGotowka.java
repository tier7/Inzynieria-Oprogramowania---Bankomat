package Suite;

import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages({"Model", "main.java.Model", "test.java.Model"})
@IncludeTags("gotowka")
public class SuiteGotowka {
}
