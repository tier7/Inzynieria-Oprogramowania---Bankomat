package Suite;

import org.junit.platform.suite.api.*;

@Suite
@SuiteDisplayName("Suite encje modelu")
@SelectPackages("Model")
@IncludeTags("encje")
@ExcludeTags({"gotowka", "transakcje", "kontroler", "mock"})
public class SuiteModelEncje {
}
