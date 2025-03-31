# Shop — Adaptacyjna Aplikacja Webowa dla Sklepu Internetowego

**Shop** to nowoczesna aplikacja webowa umożliwiająca prowadzenie sklepu internetowego. Wykorzystując Spring Boot, łączy intuicyjny interfejs użytkownika z solidną logiką biznesową. Aplikacja oferuje takie funkcje jak:

- Przeglądanie produktów
- Zarządzanie koszykiem
- Składanie zamówień

Dzięki elastycznej, modułowej architekturze, projekt łatwo rozbudować o nowe funkcjonalności, np. integrację z systemami płatności czy narzędziami analitycznymi.

## Demo

Obejrzyj film prezentujący działanie aplikacji: [Zobacz demo na YouTube](https://youtu.be/fai0YzMk6Ws)

## Spis treści

- [Opis](#opis)
- [Funkcje](#funkcje)
- [Wymagania](#wymagania)
- [Instalacja](#instalacja)
- [Konfiguracja](#konfiguracja)
- [Uruchomienie](#uruchomienie)
- [Testy](#testy)
- [Kontakt](#kontakt)

## Opis

Projekt **Shop** to kompleksowe rozwiązanie dla prowadzenia sklepu internetowego. Został zaprojektowany z myślą o łatwości obsługi, skalowalności oraz szybkiej implementacji nowych funkcji.

## Funkcje

- **Przeglądanie produktów:** Intuicyjne i responsywne wyświetlanie asortymentu.
- **Koszyk zakupowy:** Proste dodawanie produktów, edycja zawartości i zarządzanie zamówieniami.
- **Składanie zamówień:** Wieloetapowy proces finalizacji zakupów z walidacją danych.
- **Integracja:** Łatwa integracja z popularnymi systemami płatności i usługami zewnętrznymi.
- **Rozszerzalność:** Modułowa architektura umożliwiająca szybkie wdrażanie nowych funkcji i modyfikacji.

## Wymagania

- **Java:** JDK 22 lub nowszy
- **Maven:** (opcjonalnie) Możesz korzystać z Maven Wrapper (`./mvnw`)

## Instalacja

1. **Klonowanie repozytorium:**

   ```bash
   git clone https://github.com/akotu235/shop.git
   ```

2. **Przejście do katalogu projektu:**

   ```bash
   cd shop
   ```

3. **Budowanie aplikacji przy użyciu Maven Wrapper:**

   ```bash
   ./mvnw clean package
   ```

## Konfiguracja

### Plik konfiguracyjny

Podczas budowania aplikacji generowany jest szablon konfiguracji znajdujący się w `target/config/application.yml`. Należy uzupełnić ten plik odpowiednimi ustawieniami. Szablon dostępny w repozytorium ([`src/main/resources/application.yml`](https://github.com/akotu235/shop/blob/master/src/main/resources/application.yml)) może posłużyć jako punkt odniesienia.

 

### Klucz SSL

Podczas budowania aplikacji automatycznie generowany jest plik `ssl/key.jks` zawierający samopodpisany certyfikat, który można wykorzystać w środowisku deweloperskim lub testowym. Dla środowiska produkcyjnego zaleca się jednak użycie certyfikatu podpisanego przez zaufane centrum certyfikacji (CA).

Aby skonfigurować poprawny, podpisany klucz, wykonaj następujące kroki:
1. Uzyskaj certyfikat SSL od zaufanego dostawcy.
2. Zastąp plik `ssl/key.jks` nowym, zawierającym podpisany certyfikat.
3. Uaktualnij konfigurację w pliku `application.yml`, aby wskazywała na właściwe dane nowego klucza.

## Uruchomienie

Aby uruchomić aplikację, wykonaj poniższe kroki:

1. Przejdź do katalogu `target`:

   ```bash
   cd target
   ```

2. Uruchom aplikację:

   ```bash
   java -jar Shop.jar
   ```

> **Uwaga:** Upewnij się, że przed uruchomieniem aplikacji został poprawnie wygenerowany plik JAR.

## Testy

Testy jednostkowe znajdują się w katalogu `src/test/java`. Aby je uruchomić, wykonaj:

```bash
./mvnw test
```

## Kontakt

Masz pytania lub napotkałeś problemy? Skontaktuj się:

- **Andrzej:** [Zgłoś problem lub zadaj pytanie](https://akotu235.github.io/)

Link do projektu: [https://github.com/akotu235/shop](https://github.com/akotu235/shop)
