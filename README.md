# Adega Virtual (Android)

Aplicativo Android em Kotlin + Jetpack Compose para gerenciar uma adega virtual.

## O que já está implementado

- Cadastro de bebidas a partir de foto do rótulo (OCR com ML Kit).
- Extração inicial de informações sobre a bebida (serviço `FakeLabelInsightService`, pronto para troca por API de IA).
- Exibição de informações úteis:
  - marca e tipo;
  - descrição e curiosidades;
  - dicas de drinks;
  - sugestões de acompanhamentos/harmonização.
- Controle de estoque local com Room.
- Ação de **dar baixa** no estoque ao consumir uma garrafa.

## Fluxo do app

1. Usuário escolhe a foto do rótulo.
2. OCR lê o texto da imagem.
3. O texto é processado no serviço de insights para gerar metadados da bebida.
4. Registro é salvo no banco local (`Room`) com quantidade inicial.
5. Em cada consumo, o usuário toca em **Dar baixa** para decrementar o estoque.

## Como testar o app

### Pré-requisitos

- Android Studio Iguana ou superior.
- Android SDK com API 34 instalada.
- Emulador Android (Pixel API 34) **ou** celular Android físico com depuração USB.

### Passo a passo (Android Studio)

1. Abra este diretório no Android Studio.
2. Aguarde o Gradle Sync concluir.
3. Clique em **Run ▶ app** para instalar no emulador/celular.
4. Teste o fluxo principal:
   - toque em **Escolher foto** e selecione uma imagem de rótulo;
   - confira o texto em **Texto do rótulo (OCR)**;
   - ajuste **Quantidade inicial** e toque em **Adicionar na adega**;
   - confirme se o item aparece em **Meu estoque** com marca, curiosidades, drinks e acompanhamentos;
   - toque em **Dar baixa** para validar redução de estoque.

### Testes automatizados

No terminal, dentro do projeto:

```bash
./gradlew test
./gradlew connectedAndroidTest
```

- `test`: roda testes locais (JVM).
- `connectedAndroidTest`: roda testes instrumentados no dispositivo/emulador conectado.

> Se seu ambiente ainda não tiver o Gradle Wrapper (`gradlew`), execute os mesmos comandos no painel Gradle do Android Studio.

## Como evoluir para produção

- Integrar `LabelInsightService` com um backend/LLM real.
- Melhorar inferência de marca/tipo com prompt estruturado + validação.
- Usar CameraX preview + captura embutida (em vez de seletor da galeria).
- Adicionar autenticação e sincronização em nuvem.
- Implementar alerta de estoque mínimo.

## Estrutura principal

- `MainActivity`: inicialização do app e injeção manual de dependências.
- `AdegaScreen`: interface principal em Compose.
- `OcrLabelReader`: OCR usando ML Kit.
- `BeverageRepository`: regra de negócio de cadastro e baixa.
- `Room` (`AppDatabase`, `BeverageDao`, `Beverage`).
