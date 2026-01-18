# Uruchamianie testów (Windows)

Jeżeli polecenie `mvn` nie jest rozpoznawane, oznacza to, że Maven nie jest zainstalowany
lub nie ma go w zmiennej `PATH`.

## Opcja 1: Instalacja Maven (zalecane)

1. Pobierz Maven ze strony: https://maven.apache.org/download.cgi
2. Rozpakuj archiwum, np. do `C:\Tools\apache-maven-3.9.6`.
3. Dodaj do zmiennej środowiskowej `PATH` katalog `bin`, np.:
   - `C:\Tools\apache-maven-3.9.6\bin`
4. Otwórz nowe okno PowerShell/CMD i sprawdź:

```powershell
mvn -v
```

Następnie uruchom testy:

```powershell
mvn test
```

## Opcja 2: IntelliJ IDEA

Jeżeli nie chcesz instalować Mavena lokalnie, możesz uruchomić testy bezpośrednio z IntelliJ:

1. Otwórz projekt w IntelliJ.
2. Upewnij się, że IntelliJ rozpoznał plik `pom.xml` jako projekt Maven.
3. Kliknij prawym na katalog `src/test/java` lub na klasę testową i wybierz **Run**.

## Uwagi o sieci

Jeżeli środowisko blokuje pobieranie zależności z Maven Central (błąd 403),
Maven nie pobierze bibliotek JUnit. W takim przypadku spróbuj w środowisku
z dostępem do Internetu lub skonfiguruj lokalny mirror (np. Nexus/Artifactory).
