package Suite;

import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.ExcludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages("Model")
@IncludeTags("gotowka")
@ExcludeTags({"encje", "transakcje", "kontroler", "mock"})
public class SuiteGotowka {
}
