package Suite;

import org.junit.platform.suite.api.*;

@Suite
@SuiteDisplayName("Suite gotówka")
@SelectPackages("Model")
@IncludeTags("gotowka")
@ExcludeTags({"encje", "transakcje", "kontroler", "mock"})
public class SuiteGotowka {
}
