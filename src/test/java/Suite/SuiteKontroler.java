package Suite;

import org.junit.platform.suite.api.*;

@Suite
@SuiteDisplayName("Suite kontroler")
@SelectPackages("Kontroler")
@IncludeTags("kontroler")
@ExcludeTags({"gotowka", "encje", "transakcje"})
public class SuiteKontroler {
}
