# MovieDeck

**MovieDeck** — um app Android em **Kotlin + Jetpack Compose** para buscar filmes usando a **OMDb API**.
Estilo “mini IMDb”: busca por título, lista de resultados e tela de detalhes com pôster e informações.

---

# Visão geral

Um app simples, modular e didático para:

* Pesquisar filmes usando a OMDb API.
* Exibir título/ano em uma lista leve.
* Abrir tela de detalhes (com `MovieCard`) ao clicar em um item.
* Seguir boas práticas: ViewModel, Repository, Retrofit, Coil (imagens), Navigation Compose e tratamento de erros com `ApiResult`.

---

# Principais funcionalidades

* Busca por título (`s=`) via OMDb.
* Listagem compacta de resultados (sem poster para performance).
* Tela de detalhe que carrega dados completos via `i=` (imdbID).
* Tratamento de erros (network, HTTP, API response=false).
* Estado reativo com `mutableStateOf` / `StateFlow` conforme apropriado.
* Arquitetura limpa: `MovieRepository` → `ViewModel` → UI (Composables).

---

# Screenshots / Telas (exemplo)

* Tela Home: SearchBar + MovieList (título, ano)
[homescreen]()

---

# Estrutura do repositório (resumo)

```
app/
├─ src/main/java/com/pulz/moviedeck/
│  ├─ data/
│  │  ├─ api/        (OmdbApi, RetrofitClient)
│  │  ├─ model/      (MovieResponse, MovieItem)
│  │  └─ repository/ (MovieRepository, ApiResult)
│  ├─ ui/
│  │  ├─ components/ (SearchBar, MovieList, MovieCard)
│  │  └─ screens/    (HomeScreen, MovieDetailScreen)
│  ├─ viewmodel/     (MovieViewModel, MovieDetailViewModel, factories)
│  └─ MainActivity.kt
```

---

# Requisitos

* Android Studio (Arctic/Chipmunk ou superior; recomendo a versão mais recente estável)
* JDK 11+ configurado (no Windows: `JAVA_HOME`)
* SDK Android conforme `compileSdk` (configurado no projeto)
* OMDb API Key (gratuita) — **não comitar** essa chave

---

# Configuração (local)

1. Clone o repositório:

```bash
git clone <repo-url>
cd MovieDeck
```

2. Crie/edite `local.properties` (no nível do projeto, ao lado de `settings.gradle.kts`) e adicione sua chave:

```
sdk.dir=C\:\\Path\\to\\android-sdk
OMDB_API_KEY=your_omdb_api_key_here
```

> Observação: o Gradle lê `local.properties` e injeta a chave em `BuildConfig.OMDB_API_KEY` (veja `app/build.gradle.kts`).

3. Abra no Android Studio → `File > Sync Project with Gradle Files`.

---

# Dependências (principais)

Versões utilizadas (exemplo estável recomendado — ver `build.gradle.kts` do projeto):

* Kotlin / Gradle plugin (como configurado no projeto)
* Compose UI: `1.7.3`
* Material3: `1.3.0`
* Navigation Compose: `2.8.2`
* Lifecycle / ViewModel: `2.8.4`
* Retrofit: `2.11.0`
* Gson converter: `2.11.0`
* Coil Compose (imagens): `io.coil-kt:coil-compose:2.6.0`
* Coroutines: `kotlinx-coroutines-android:1.9.0`

*(As versões exatas estão no `build.gradle.kts` do módulo app — alinhe todo o bloco `dependencies` sem duplicações.)*

---

# Como rodar (desenvolvimento)

* Abra o projeto no Android Studio.
* `Build > Clean Project` (se necessário).
* `Build > Rebuild Project`.
* Execute no dispositivo/emulador.
* Logs úteis aparecem com a tag `MovieDeck` no Logcat.

**Filtro Logcat sugerido:** `MovieDeck` (se o filtro IDE mostrar “all long entries are hidden by the filter”, use `Search` por `MovieDeck`).

---

# Uso da OMDb API no projeto

* Busca por título (lista): `?apikey=KEY&s=batman`
* Detalhes por imdbID: `?apikey=KEY&i=tt0133093`

**Importante:** Passamos somente o `imdbID` pela navigation (rota `details/{imdbID}`) para evitar crash com `SavedStateHandle` (que não aceita objetos complexos não-`Parcelable`). A tela de detalhes faz a requisição `i=` via `MovieDetailViewModel` -> `MovieRepository.getMovieDetails()`.

---

# Como a navegação funciona (resumo)

* `NavHost` com rotas:

  * `home` → `HomeScreen(movieViewModel, onMovieClick)`
  * `details/{imdbID}` → `MovieDetailScreen(imdbID, viewModel, onBack)`

* Ao clicar em um item na `MovieList`, você chama:

```kotlin
navController.navigate("details/${movie.imdbID}")
```

* `MovieDetailScreen` usa `MovieDetailViewModel.fetchMovieDetail(imdbID)` para carregar os detalhes.

---

# Observações de segurança / boas práticas

* **Nunca** comite `local.properties` com a chave.
* Use `BuildConfig.OMDB_API_KEY` para acessar a chave no código.
* Para produção, considere usar um backend que proteja a chave (evitar expor diretamente no app).
