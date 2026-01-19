package Suite;

import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.ExcludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("Suite – encje modelu")
@SelectPackages("Model")
@IncludeTags("encje")
@ExcludeTags({"gotowka", "transakcje", "kontroler", "mock"})
public class SuiteModelEncje {
}
