package Suite;

import org.junit.platform.suite.api.ExcludeTags;
import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("Suite kontroler")
@SelectPackages("Kontroler")
@IncludeTags("kontroler")
@ExcludeTags({"gotowka", "encje", "transakcje"})
public class SuiteKontroler {
}
